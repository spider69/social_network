package com.yusupov.social_network.actors

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.http.scaladsl.marshalling.{Marshaller, ToResponseMarshaller}
import akka.http.scaladsl.model.headers.{HttpCookie, `Set-Cookie`}
import akka.http.scaladsl.model.{ContentTypes, DateTime, HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import akka.util.Timeout
import com.softwaremill.tagging._
import com.typesafe.scalalogging.LazyLogging
import com.yusupov.social_network.actors.Authenticator.{AuthenticatorTag, FailureAuthResponse, SignInSuccessful, SignUpSuccessful}
import com.yusupov.social_network.actors.SocialNetwork.SocialNetworkTag
import com.yusupov.social_network.auth.AuthenticatorApi
import com.yusupov.social_network.data.{JsonMarshaller, User, UserForm}

import scala.concurrent.ExecutionContext
import scala.util.Success

class RestApi(
  system: ActorSystem,
  timeout: Timeout,
  socialNetworkProps: Props @@ SocialNetworkTag,
  authenticatorProps: Props @@ AuthenticatorTag
) extends RestRoutes {
  implicit val requestTimeout = timeout
  implicit def executionContext = system.dispatcher

  override def createSocialNetwork() = system.actorOf(socialNetworkProps).taggedWith[SocialNetworkTag]

  override def createAuthenticator() = system.actorOf(authenticatorProps).taggedWith[AuthenticatorTag]
}

trait RestRoutes extends SocialNetworkApi
  with AuthenticatorApi
  with JsonMarshaller
{
  def routes: Route = {
    uiRoute ~
      authRoute ~
      usersRoute ~
      formsRoute
  }

  def uiRoute = {
    pathEndOrSingleSlash {
      getFromResource("web/index.html")
    } ~
      pathPrefix("login") {
        indexRoute
      } ~
      (pathPrefix("signup") | pathPrefix("home") | pathPrefix("user_forms") | pathPrefix("edit_form") | pathPrefix("users")) {
        extractUnmatchedPath {_ =>
          indexRoute
        }
      } ~
      getFromResourceDirectory("web")
  }

  def authRoute = {
    (pathPrefix("create_user") & post) {
      formFields(Symbol("email"), Symbol("userName"), Symbol("password")) { (email, userName, password) =>
        complete(signUp(email, userName, password))
      }
    } ~
      (pathPrefix("current_session") & optionalCookie("ssid")) {
        case Some(sessionCookie) =>
          onComplete(checkCurrentSession(sessionCookie.value)) {
            case Success(Authenticator.SessionIsValid(userId)) =>
              complete(StatusCodes.OK, userId)
            case Success(Authenticator.SessionIsInvalid) =>
              complete(StatusCodes.Unauthorized, "No current user")
            case _ =>
              complete(StatusCodes.InternalServerError)
          }
        case None =>
          complete(StatusCodes.Unauthorized, "No current user")
      } ~
      (pathPrefix("sign_in") & post) {
        formFields(Symbol("login"), Symbol("password")) { (login, password) =>
          complete(signIn(login, password))
        }
      } ~
      (pathPrefix("sign_out") & cookie("ssid")) { cookie =>
        onComplete(signOut(cookie.value)) {
          case Success(Authenticator.SessionInvalidated) =>
            deleteCookie("ssid") {
              complete(StatusCodes.OK)
            }
          case _ =>
            complete(StatusCodes.InternalServerError)
        }
      }
  }

  def usersRoute = {
    pathPrefix("all_users") {
      parameters("filter".optional) { filterExpr =>
        optionalCookie("ssid") {
          case Some(sessionCookie) =>
            onComplete(checkCurrentSession(sessionCookie.value)) {
              case Success(Authenticator.SessionIsValid(_)) =>
                onSuccess(getAllUsers(filterExpr)) {
                  case SocialNetwork.Users(users) => complete(StatusCodes.Created, users.map(u => User(u._1, u._2, u._3)))
                  case _ => complete(StatusCodes.InternalServerError)
                }

              case Success(Authenticator.SessionIsInvalid) =>
                complete(StatusCodes.NotFound)
              case _ =>
                complete(StatusCodes.InternalServerError)
            }
          case None =>
            complete(StatusCodes.Unauthorized)
        }
      }
    }
  }

  def formsRoute = {
    (pathPrefix("create_form") & post & optionalCookie("ssid")) {
      case Some(sessionCookie) =>
        onComplete(checkCurrentSession(sessionCookie.value)) {
          case Success(Authenticator.SessionIsValid(userId)) =>
            entity(as[UserForm]) {
              form =>
                onSuccess(createForm(userId, form)) {
                  case SocialNetwork.FormCreated => complete(StatusCodes.Created)
                  case _ => complete(StatusCodes.InternalServerError)
                }
            }
          case Success(Authenticator.SessionIsInvalid) =>
            complete(StatusCodes.NotFound)
          case _ =>
            complete(StatusCodes.InternalServerError)
        }
      case None =>
        complete(StatusCodes.Unauthorized, "No current user")
    } ~
      pathPrefix("get_form" / Segment) { userId =>
        optionalCookie("ssid") {
          case Some(sessionCookie) =>
            onComplete(checkCurrentSession(sessionCookie.value)) {
              case Success(Authenticator.SessionIsValid(_)) =>
                onSuccess(getForm(userId)) {
                  case SocialNetwork.RequestedForm(form) => complete(StatusCodes.Created, form)
                  case _ => complete(StatusCodes.InternalServerError)
                }
              case Success(Authenticator.SessionIsInvalid) =>
                complete(StatusCodes.NotFound)
              case _ =>
                complete(StatusCodes.InternalServerError)
            }
          case None =>
            complete(StatusCodes.Unauthorized, "No current user")
        }
      } ~
      (pathPrefix("update_form" / Segment) & post) { userId =>
        optionalCookie("ssid") {
          case Some(sessionCookie) =>
            onComplete(checkCurrentSession(sessionCookie.value)) {
              case Success(Authenticator.SessionIsValid(loggedInUserId)) =>
                if (loggedInUserId != userId) {
                  complete(StatusCodes.Forbidden, "Editing page of other user is forbidden")
                } else {
                  entity(as[UserForm]) {
                    form =>
                      onSuccess(updateForm(userId, form)) {
                        case SocialNetwork.FormUpdated => complete(StatusCodes.OK)
                        case _ => complete(StatusCodes.InternalServerError)
                      }
                  }
                }
              case Success(Authenticator.SessionIsInvalid) =>
                complete(StatusCodes.NotFound)
              case _ =>
                complete(StatusCodes.InternalServerError)
            }
          case None =>
            complete(StatusCodes.Unauthorized, "No current user")
        }
      }
  }

  private def indexRoute: Route = getFromResource("web/index.html")

  private implicit def marshaller: ToResponseMarshaller[Authenticator.Response] =
    Marshaller.withFixedContentType(ContentTypes.`application/json`) {
      case FailureAuthResponse(statusCode, cause) =>
        HttpResponse(statusCode, entity = cause)

      case SignUpSuccessful =>
        HttpResponse(StatusCodes.Created)

      case SignInSuccessful(sessionId, userId) =>
        HttpResponse(
          StatusCodes.OK,
          `Set-Cookie`(
            HttpCookie(
              name = "ssid",
              value = sessionId,
              path = Some("/"), domain = Some("localhost"),
              expires = Some(DateTime.MaxValue),
              secure = false,
              httpOnly = true
            )
          ) :: Nil,
          userId
        )
    }

}

trait SocialNetworkApi extends LazyLogging {
  import SocialNetwork._

  def createSocialNetwork(): ActorRef

  lazy val socialNetwork = createSocialNetwork()

  implicit def executionContext: ExecutionContext
  implicit def requestTimeout: Timeout

  def getAllUsers(filterExpr: Option[String]) = {
    logger.debug(s"API: getting all users")
    socialNetwork.ask(GetUsers(filterExpr)).mapTo[Response]
  }

  def createForm(userId: String, form: UserForm) = {
    logger.debug(s"API: createForm for ${form.firstName}")
    socialNetwork.ask(CreateForm(userId, form)).mapTo[Response]
  }

  def getForm(userId: String) = {
    logger.debug(s"API: getForm for $userId")
    socialNetwork.ask(GetForm(userId)).mapTo[Response]
  }

  def updateForm(userId: String, form: UserForm) = {
    logger.debug(s"API: updateForm for $userId")
    socialNetwork.ask(UpdateForm(userId, form)).mapTo[Response]
  }

}
