package com.yusupov.social_network.api

import akka.actor.ActorRef
import akka.http.scaladsl.marshalling.{Marshaller, ToResponseMarshaller}
import akka.http.scaladsl.model.headers.{HttpCookie, `Set-Cookie`}
import akka.http.scaladsl.model.{ContentTypes, DateTime, HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives.{complete, cookie, deleteCookie, formFields, onComplete, optionalCookie, pathPrefix, _}
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import akka.util.Timeout
import com.softwaremill.tagging._
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import com.yusupov.social_network.actors.Authenticator

import scala.concurrent.ExecutionContext
import scala.util.Success

trait AuthenticatorApi extends SessionChecker with LazyLogging {
  import com.yusupov.social_network.actors.Authenticator._

  def createAuthenticator(): ActorRef @@ AuthenticatorTag

  val config = ConfigFactory.load()
  val cookieName = config.getString("service-settings.auth-settings.cookie-name")
  val cookieDomain = config.getString("service-settings.auth-settings.cookie-domain")
  logger.info(s"Cookie domain: $cookieDomain")

  lazy val authenticator = createAuthenticator()

  implicit def executionContext: ExecutionContext
  implicit def requestTimeout: Timeout

  def authRoute: Route = {
    (pathPrefix("create_user") & post) {
      formFields(Symbol("email"), Symbol("userName"), Symbol("password")) { (email, userName, password) =>
        complete(signUp(email, userName, password))
      }
    } ~
      pathPrefix("current_session") {
        checkSession(userId => complete(StatusCodes.OK, userId))
      } ~
      (pathPrefix("sign_in") & post) {
        formFields(Symbol("login"), Symbol("password")) { (login, password) =>
          complete(signIn(login, password))
        }
      } ~
      (pathPrefix("sign_out") & cookie(cookieName)) { cookie =>
        onComplete(signOut(cookie.value)) {
          case Success(Authenticator.SessionInvalidated) =>
            deleteCookie(cookieName) {
              complete(StatusCodes.OK)
            }
          case _ =>
            complete(StatusCodes.InternalServerError)
        }
      }
  }

  override def checkSession(action: String => Route) =
    optionalCookie(cookieName) {
      case Some(sessionCookie) =>
        onComplete(checkCurrentSession(sessionCookie.value)) {
          case Success(Authenticator.SessionIsValid(userId)) =>
            action(userId)
          case Success(Authenticator.SessionIsInvalid) =>
            complete(StatusCodes.Unauthorized, "No current user")
          case _ =>
            complete(StatusCodes.InternalServerError)
        }
      case None =>
        complete(StatusCodes.Unauthorized, "No current user")
    }

  private def signUp(email: String, userName: String, password: String) = {
    logger.debug(s"API: signUp for $userName")
    authenticator.ask(SignUp(email, userName, password)).mapTo[Response]
  }

  private def signIn(login: String, password: String) = {
    logger.debug(s"API: signUp for $login")
    authenticator.ask(SignIn(login, password)).mapTo[Response]
  }

  private def checkCurrentSession(sessionId: String) = {
    logger.debug(s"API: checkCurrentSession $sessionId")
    authenticator.ask(CheckSession(sessionId)).mapTo[Response]
  }

  private def signOut(sessionId: String) = {
    logger.debug(s"API: signOut $sessionId")
    authenticator.ask(InvalidateSession(sessionId)).mapTo[Response]
  }

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
              name = cookieName,
              value = sessionId,
              path = Some("/"),
              domain = Some(cookieDomain),
              expires = Some(DateTime.MaxValue),
              secure = false,
              httpOnly = true
            )
          ) :: Nil,
          userId
        )

      case _ =>
        HttpResponse(StatusCodes.OK)
    }

}
