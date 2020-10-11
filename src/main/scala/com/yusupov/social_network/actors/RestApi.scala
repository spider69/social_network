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
import com.yusupov.social_network.data.{Form, JsonMarshaller}

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
  with JsonMarshaller
  with AuthenticatorApi
{
  def routes: Route = {
      uiRoute ~
      authRoute ~
      formsRoute
  }

  def uiRoute = {
    pathEndOrSingleSlash {
      getFromResource("web/index.html")
    } ~
      pathPrefix("login") {
        indexRoute
      } ~
      (pathPrefix("signup") | pathPrefix("home")) {
        extractUnmatchedPath {_ =>
          indexRoute
        }
      } ~
      getFromResourceDirectory("web")
  }

  private def indexRoute: Route = getFromResource("web/index.html")

  def authRoute = {
    (pathPrefix("create_user") & post) {
      formFields(Symbol("email"), Symbol("userName"), Symbol("password")) { (email, userName, password) =>
        complete(signUp(email, userName, password))
      }
    } ~
      (pathPrefix("current_session") & optionalCookie("ssid")) {
        case Some(sessionCookie) =>
          onComplete(checkCurrentSession(sessionCookie.value)) {
            case Success(Authenticator.SessionIsValid) =>
              complete(StatusCodes.OK)
            case Success(Authenticator.SessionIsInvalid) =>
              complete(StatusCodes.NotFound)
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

  private implicit def marshaller: ToResponseMarshaller[Authenticator.Response] =
    Marshaller.withFixedContentType(ContentTypes.`application/json`) {
      case FailureAuthResponse(statusCode, cause) =>
        HttpResponse(statusCode, entity = cause)

      case SignUpSuccessful =>
        HttpResponse(StatusCodes.Created)

      case SignInSuccessful(sessionId) =>
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
          ) :: Nil
        )
    }

  def formsRoute = {
    pathPrefix("form") {
      pathEndOrSingleSlash {
        post {
          entity(as[Form]) {
            form =>
              onSuccess(createForm(form)) {
                case SocialNetwork.FormCreated => complete(StatusCodes.Created)
                case _ => complete(StatusCodes.InternalServerError)
              }
          }
        }
      }
    }
  }

}

trait SocialNetworkApi extends LazyLogging {
  import SocialNetwork._

  def createSocialNetwork(): ActorRef

  lazy val socialNetwork = createSocialNetwork()

  implicit def executionContext: ExecutionContext
  implicit def requestTimeout: Timeout

  def createForm(form: Form) = {
    logger.debug(s"API: createForm for ${form.name}")
    socialNetwork.ask(CreateForm(form)).mapTo[Response]
  }
}
