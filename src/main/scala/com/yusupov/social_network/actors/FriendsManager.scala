package com.yusupov.social_network.actors

import akka.actor.{Actor, ActorRef}
import akka.pattern.ask
import akka.util.Timeout
import com.softwaremill.tagging.@@
import com.typesafe.scalalogging.LazyLogging
import com.yusupov.social_network.actors.Database.DatabaseTag

import scala.util.Success

object FriendsManager {
  trait FriendsManagerTag

  def name = "friends-manager"

  sealed trait Request
  case class GetUsers(filterExpr: Option[String]) extends Request
  case class AddFriend(userId: String, friendId: String) extends Request
  case class RemoveFriend(userId: String, friendId: String) extends Request
  case class GetFriends(userId: String) extends Request

  sealed trait Response
  case class Users(users: Seq[(String, String, String)]) extends Response
  case object FriendAdded extends Response
  case object FriendRemoved extends Response
  case object FriendsInternalError extends Response

}

import com.yusupov.social_network.actors.FriendsManager._

class FriendsManager(
  requestTimeout: Timeout,
  database: ActorRef @@ DatabaseTag
) extends Actor with LazyLogging {

  import context.dispatcher

  implicit val timeout = requestTimeout

  override def receive = {
    case GetUsers(filterExpr) =>
      logger.debug("Getting users")
      val requester = sender()
      (database ? Database.GetUsers(filterExpr))
        .onComplete {
          case Success(Database.Users(users)) =>
            requester ! Users(users)
          case _ =>
            requester ! FriendsInternalError
        }

    case AddFriend(userId, friendId) =>
      logger.debug(s"Add friend $friendId for $userId")
      val requester = sender()
      (database ? Database.AddFriend(userId, friendId))
        .onComplete {
          case Success(Database.FriendAdded) =>
            requester ! FriendAdded
          case _ =>
            requester ! FriendsInternalError
        }

    case GetFriends(userId) =>
      logger.debug(s"Get friends for $userId")
      val requester = sender()
      (database ? Database.GetFriends(userId))
        .onComplete {
          case Success(Database.Users(users)) =>
            requester ! Users(users)
          case _ =>
            requester ! FriendsInternalError
        }

    case RemoveFriend(userId, friendId) =>
      logger.debug(s"Remove friend $friendId for $userId")
      val requester = sender()
      (database ? Database.RemoveFriend(userId, friendId))
        .onComplete {
          case Success(Database.FriendRemoved) =>
            requester ! FriendRemoved
          case _ =>
            requester ! FriendsInternalError
        }
  }

}
