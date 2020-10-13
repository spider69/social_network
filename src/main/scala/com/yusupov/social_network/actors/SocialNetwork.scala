package com.yusupov.social_network.actors

import akka.actor.{Actor, ActorRef}
import akka.pattern.ask
import akka.util.Timeout
import com.softwaremill.tagging.@@
import com.typesafe.scalalogging.LazyLogging
import com.yusupov.social_network.actors.Database.DatabaseTag
import com.yusupov.social_network.actors.SocialNetwork.CreateForm
import com.yusupov.social_network.data.UserForm

import scala.util.Success

object SocialNetwork {
  trait SocialNetworkTag

  def name = "social_network"

  sealed trait Request
  case object GetUsers extends Request
  case class CreateForm(userId: String, form: UserForm) extends Request
  case class GetForm(userId: String) extends Request
  case class UpdateForm(userId: String, form: UserForm) extends Request

  sealed trait Response
  case class Users(users: Seq[(String, String)]) extends Response
  case object FormCreated extends Response
  case object FormUpdated extends Response
  case class RequestedForm(form: UserForm) extends Response
  case object InternalError extends Response
}

import com.yusupov.social_network.actors.SocialNetwork._

class SocialNetwork(
  requestTimeout: Timeout,
  database: ActorRef @@ DatabaseTag
) extends Actor with LazyLogging {

  import context.dispatcher

  implicit val timeout = requestTimeout

  override def receive: Receive = {
    case GetUsers =>
      logger.debug("Getting users")
      val requester = sender()
      (database ? Database.GetUsers)
        .onComplete {
          case Success(Database.Users(users)) =>
            requester ! Users(users)
          case _ =>
            requester ! InternalError
        }

    case CreateForm(userId, form) =>
      logger.debug(s"Creating form for $userId")
      val requester = sender()
      (database ? Database.CreateForm(userId, form))
        .onComplete {
          case Success(Database.FormCreated) =>
            requester ! FormCreated
          case _ =>
            requester ! InternalError
        }

    case GetForm(userId) =>
      logger.debug(s"Getting form by $userId")
      val requester = sender()
      (database ? Database.GetForm(userId))
        .onComplete {
          case Success(Database.RequestedForm(form)) =>
            requester ! RequestedForm(form)
          case _ =>
            requester ! InternalError
        }

    case UpdateForm(userId, form) =>
      logger.debug(s"Updating form for $userId")
      val requester = sender()
      (database ? Database.UpdateForm(userId, form))
        .onComplete {
          case Success(Database.FormUpdated) =>
            requester ! FormUpdated
          case _ =>
            requester ! InternalError
        }

  }
}
