package com.yusupov.social_network.actors

import java.util.UUID

import akka.actor.{Actor, ActorRef}
import akka.pattern.ask
import akka.util.Timeout
import com.softwaremill.tagging.@@
import com.typesafe.scalalogging.LazyLogging
import com.yusupov.social_network.actors.Database.DatabaseTag
import com.yusupov.social_network.actors.SocialNetwork.CreateForm
import com.yusupov.social_network.data.{Credentials, Form}

import scala.util.Success

object SocialNetwork {
  trait SocialNetworkTag

  def name = "social_network"

  sealed trait Request
  case class SignUp(credentials: Credentials)
  case class CreateForm(form: Form) extends Request

  sealed trait Response
  case class SignUpSuccessful(id: UUID) extends Response
  case object FormCreated extends Response
  case object InternalError extends Response
}

import com.yusupov.social_network.actors.SocialNetwork._

class SocialNetwork(
  requestTimeout: Timeout,
  database: ActorRef @@ DatabaseTag
) extends Actor with LazyLogging {

  import context.dispatcher

  implicit val timeout = requestTimeout
  def createDatabase = context.actorOf(Database.props(requestTimeout), UUID.randomUUID().toString)

  override def receive: Receive = {
    case SignUp(Credentials(userName, password)) =>
      logger.debug(s"Signing up for $userName")
      val requester = sender()
      (database ? Database.CreateUser(userName, password))
        .onComplete {
          case Success(Database.UserCreated(id)) =>
            requester ! SignUpSuccessful(id)
          case _ =>
            requester ! InternalError
        }

    case CreateForm(form) =>
      logger.debug(s"Creating form for ${form.name}")
      val requester = sender()
      (database ? Database.CreateForm(form))
        .onComplete {
          case Success(Database.FormCreated) =>
            requester ! FormCreated
          case _ =>
            requester ! InternalError
        }
  }
}
