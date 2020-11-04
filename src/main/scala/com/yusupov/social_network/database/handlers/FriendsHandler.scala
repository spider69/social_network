package com.yusupov.social_network.database.handlers

import com.typesafe.scalalogging.LazyLogging
import com.yusupov.social_network.actors.Database._
import com.yusupov.social_network.database.DatabaseProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext

class FriendsHandler[T <: JdbcProfile](databaseProvider: DatabaseProvider[T]) extends Handler with LazyLogging {
  import databaseProvider.profile.api._

  def addFriend(userId: String, friendId: String) =
    sqlu"INSERT INTO Friends(user_id, friend_id) VALUES ('#$userId','#$friendId')"

  def removeFriend(userId: String, friendId: String) =
    sqlu"DELETE FROM Friends WHERE user_id='#$userId' AND friend_id='#$friendId'"

  def getFriends(userId: String) =
    sql"SELECT u.id, u.first_name, u.last_name FROM Friends f JOIN Users u ON f.friend_id=u.id WHERE f.user_id='#$userId'"
      .as[(String, String, String)]

  override def handle(implicit ec: ExecutionContext) = {
    case AddFriend(userId, friendId) =>
      logger.debug(s"Add friend $friendId for $userId")
      addFriend(userId, friendId)
        .andThen(addFriend(friendId, userId))
        .map(_ => FriendAdded)

    case GetFriends(userId) =>
      logger.debug(s"Get friends of $userId")
      getFriends(userId)
        .map(result => Users(result))

    case RemoveFriend(userId, friendId) =>
      logger.debug(s"Remove friend $friendId for $userId")
      removeFriend(userId, friendId)
        .andThen(removeFriend(friendId, userId))
        .map(_ => FriendRemoved)
  }
}
