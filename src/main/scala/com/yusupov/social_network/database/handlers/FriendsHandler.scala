package com.yusupov.social_network.database.handlers

import com.yusupov.social_network.database.DatabaseProvider
import slick.jdbc.JdbcProfile

class FriendsHandler[T <: JdbcProfile](databaseProvider: DatabaseProvider[T]) {
  import databaseProvider.profile.api._

  def addFriend(userId: String, friendId: String) =
    sqlu"INSERT INTO Friends SET user_id='#$userId', friend_id='#$friendId'"

  def removeFriend(userId: String, friendId: String) =
    sqlu"DELETE FROM Friends WHERE user_id='#$userId' AND friend_id='#$friendId'"

  def getFriends(userId: String) =
    sql"SELECT u.id, u.first_name, u.last_name FROM Friends f JOIN UserForms u ON f.friend_id=u.id WHERE f.user_id='#$userId'"
      .as[(String, String, String)]

}
