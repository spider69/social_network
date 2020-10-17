package com.yusupov.social_network.database.handlers

import akka.actor.ActorRef
import com.typesafe.scalalogging.LazyLogging
import com.yusupov.social_network.actors.Database.{AddFriend, FriendAdded, FriendRemoved, GetFriends, RemoveFriend, Users}
import com.yusupov.social_network.database.DatabaseProvider
import slick.jdbc.JdbcProfile

class FriendsHandler[T <: JdbcProfile](databaseProvider: DatabaseProvider[T]) extends Handler with LazyLogging {
  import databaseProvider.profile.api._

  def addFriend(userId: String, friendId: String) =
    sqlu"INSERT INTO Friends SET user_id='#$userId', friend_id='#$friendId'"

  def removeFriend(userId: String, friendId: String) =
    sqlu"DELETE FROM Friends WHERE user_id='#$userId' AND friend_id='#$friendId'"

  def getFriends(userId: String) =
    sql"SELECT u.id, u.first_name, u.last_name FROM Friends f JOIN UserForms u ON f.friend_id=u.id WHERE f.user_id='#$userId'"
      .as[(String, String, String)]

  override def handle(sender: ActorRef) = {
    case AddFriend(userId, friendId) =>
      logger.debug(s"Add friend $friendId for $userId")
      val query = addFriend(userId, friendId).andThen(addFriend(friendId, userId))
      databaseProvider.exec(query)
      sender ! FriendAdded

    case GetFriends(userId) =>
      logger.debug(s"Get friends of $userId")
      val query = getFriends(userId)
      val result = databaseProvider.exec(query)
      sender ! Users(result)

    case RemoveFriend(userId, friendId) =>
      logger.debug(s"Remove friend $friendId for $userId")
      val query = removeFriend(userId, friendId).andThen(removeFriend(friendId, userId))
      databaseProvider.exec(query)
      sender ! FriendRemoved
  }
}
