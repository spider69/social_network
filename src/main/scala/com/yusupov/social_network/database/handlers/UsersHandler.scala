package com.yusupov.social_network.database.handlers

import com.yusupov.social_network.database.DatabaseProvider
import slick.jdbc.JdbcProfile

class UsersHandler[T <: JdbcProfile](databaseProvider: DatabaseProvider[T]) {
  import databaseProvider.profile.api._

  def getUsers =
    sql"SELECT id,name FROM Users"
      .as[(String,String)]

  def getUser(userId: String) =
    sql"SELECT id,name FROM Users WHERE id='#$userId'"
      .as[(String, String)]

}
