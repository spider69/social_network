package com.yusupov.social_network.actors

import akka.actor.{Actor, ActorRef}
import akka.http.scaladsl.model.{StatusCode, StatusCodes}
import akka.pattern.ask
import akka.util.Timeout
import com.softwaremill.tagging.@@
import com.typesafe.scalalogging.LazyLogging
import com.yusupov.social_network.actors.Database.{DatabaseTag, SessionCreated, UserById, UserCreated, UserNotFound}

import scala.concurrent.Future
import scala.util.{Failure, Success}

object Authenticator {
  trait AuthenticatorTag

  def name = "authenticator"

  sealed trait Request
  case class SignUp(email: String, userName: String, password: String) extends Request
  case class SignIn(email: String, password: String) extends Request
  case class SignOut(sessionId: String) extends Request
  case class CheckSession(sessionId: String) extends Request
  case class InvalidateSession(sessionId: String) extends Request

  sealed trait Response
  case object UserAlreadyExists extends Response
  case object WrongCredentials extends Response
  case object SignUpSuccessful extends Response
  case object SessionIsValid extends Response
  case object SessionIsInvalid extends Response
  case object SessionInvalidated extends Response
  case class SignInSuccessful(sessionId: String) extends Response

  case class FailureAuthResponse(statusCode: StatusCode, cause: String) extends Response
}

import Authenticator._

class Authenticator(
  requestTimeout: Timeout,
  database: ActorRef @@ DatabaseTag
) extends Actor with LazyLogging {

  import context.dispatcher

  implicit val timeout = requestTimeout

  override def receive = {
    case SignUp(email, userName, password) =>
      logger.debug(s"Signing up for $userName")
      val requester = sender()
      (database ? Database.GetUserById(email))
        .flatMap {
          case UserById(_, _, _) =>
            Future.successful(UserAlreadyExists)
          case UserNotFound =>
            database ? Database.CreateUser(email, userName, password)
        }.onComplete {
        case Success(UserCreated) =>
          requester ! SignUpSuccessful
        case Success(UserAlreadyExists) =>
          requester ! FailureAuthResponse(StatusCodes.NotFound, "User already exists")
        case Failure(e) =>
          requester ! FailureAuthResponse(StatusCodes.InternalServerError, e.getMessage)
        case _ =>
          requester ! FailureAuthResponse(StatusCodes.InternalServerError, "Internal error")
      }

    case SignIn(email, password) =>
      logger.debug(s"Signing in for $email")
      val requester = sender()
      (database ? Database.GetUserById(email))
        .flatMap {
          case UserById(_, _, storedPassword) =>
            if (storedPassword == password) {
              database ? Database.CreateSession(email)
            } else {
              Future.successful(WrongCredentials)
            }
          case UserNotFound =>
            Future.successful(WrongCredentials)
        }.onComplete {
        case Success(SessionCreated(id)) =>
          requester ! SignInSuccessful(id)
        case Success(WrongCredentials) =>
          requester ! FailureAuthResponse(StatusCodes.Unauthorized, "Wrong credentials")
        case Failure(e) =>
          requester ! FailureAuthResponse(StatusCodes.InternalServerError, e.getMessage)
        case _ =>
          requester ! FailureAuthResponse(StatusCodes.InternalServerError, "Internal error")
      }

    case CheckSession(sessionId) =>
      logger.debug(s"Checking session $sessionId")
      val requester = sender()
      (database ? Database.CheckSession(sessionId))
        .onComplete {
          case Success(Database.SessionIsValid) =>
            requester ! SessionIsValid
          case Success(Database.SessionIsInvalid) =>
            requester ! SessionIsInvalid
          case Failure(e) =>
            requester ! FailureAuthResponse(StatusCodes.InternalServerError, e.getMessage)
          case _ =>
            requester ! FailureAuthResponse(StatusCodes.InternalServerError, "Internal error")
        }

    case InvalidateSession(sessionId) =>
      logger.debug(s"Invalidating session $sessionId")
      val requester = sender()
      (database ? Database.DeleteSession(sessionId))
        .onComplete {
          case Success(Database.SessionDeleted) =>
            requester ! SessionInvalidated
          case Failure(e) =>
            requester ! FailureAuthResponse(StatusCodes.InternalServerError, e.getMessage)
          case _ =>
            requester ! FailureAuthResponse(StatusCodes.InternalServerError, "Internal error")
        }

  }

}
