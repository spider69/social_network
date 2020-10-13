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
  case class GetUsers(filterExpr: Option[String]) extends Request
  case class CreateForm(userId: String, form: UserForm) extends Request
  case class GetForm(userId: String) extends Request
  case class UpdateForm(userId: String, form: UserForm) extends Request
  case class AddFriend(userId: String, friendId: String) extends Request
  case class RemoveFriend(userId: String, friendId: String) extends Request
  case class GetFriends(userId: String) extends Request

  sealed trait Response
  case class Users(users: Seq[(String, String, String)]) extends Response
  case object FormCreated extends Response
  case object FormUpdated extends Response
  case class RequestedForm(form: UserForm) extends Response
  case object InternalError extends Response
  case object FriendAdded extends Response
  case object FriendRemoved extends Response
}

import com.yusupov.social_network.actors.SocialNetwork._

class SocialNetwork(
  requestTimeout: Timeout,
  database: ActorRef @@ DatabaseTag
) extends Actor with LazyLogging {

  import context.dispatcher

  implicit val timeout = requestTimeout

  override def receive: Receive = {
    case GetUsers(filterExpr) =>
      logger.debug("Getting users")
      val requester = sender()
      (database ? Database.GetUsers(filterExpr))
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

    case AddFriend(userId, friendId) =>
      logger.debug(s"Add friend $friendId for $userId")
      val requester = sender()
      (database ? Database.AddFriend(userId, friendId))
        .onComplete {
          case Success(Database.FriendAdded) =>
            requester ! FriendAdded
          case _ =>
            requester ! InternalError
        }

    case GetFriends(userId) =>
      logger.debug(s"Get friends for $userId")
      val requester = sender()
      (database ? Database.GetFriends(userId))
        .onComplete {
          case Success(Database.Users(users)) =>
            requester ! Users(users)
          case _ =>
            requester ! InternalError
        }

    case RemoveFriend(userId, friendId) =>
      logger.debug(s"Remove friend $friendId for $userId")
      val requester = sender()
      (database ? Database.RemoveFriend(userId, friendId))
        .onComplete {
          case Success(Database.FriendRemoved) =>
            requester ! FriendRemoved
          case _ =>
            requester ! InternalError
        }


  }
}
