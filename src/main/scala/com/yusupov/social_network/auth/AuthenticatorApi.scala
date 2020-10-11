package com.yusupov.social_network.auth

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.ExecutionContext

trait AuthenticatorApi extends LazyLogging {
  import com.yusupov.social_network.actors.Authenticator._

  def createAuthenticator(): ActorRef

  lazy val authenticator = createAuthenticator()

  implicit def executionContext: ExecutionContext
  implicit def requestTimeout: Timeout

  def signUp(email: String, userName: String, password: String) = {
    logger.debug(s"API: signUp for $userName")
    authenticator.ask(SignUp(email, userName, password)).mapTo[Response]
  }

  def signIn(login: String, password: String) = {
    logger.debug(s"API: signUp for $login")
    authenticator.ask(SignIn(login, password)).mapTo[Response]
  }

  def checkCurrentSession(sessionId: String) = {
    logger.debug(s"API: checkCurrentSession $sessionId")
    authenticator.ask(CheckSession(sessionId)).mapTo[Response]
  }

  def signOut(sessionId: String) = {
    logger.debug(s"API: signOut $sessionId")
    authenticator.ask(InvalidateSession(sessionId)).mapTo[Response]
  }
}
