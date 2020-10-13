package com.yusupov.social_network.database.handlers

import com.yusupov.social_network.database.DatabaseProvider
import slick.jdbc.JdbcProfile

class UsersHandler[T <: JdbcProfile](databaseProvider: DatabaseProvider[T]) {
  import databaseProvider.profile.api._

  def getUsers(filterExpr: Option[String]) = filterExpr match {
    case Some("") | None => sql"SELECT f.id, f.first_name, f.last_name FROM Users u JOIN UserForms f ON u.id=f.id".as[(String,String,String)]
    case Some(expr) => sql"SELECT f.id, f.first_name, f.last_name FROM Users u JOIN UserForms f ON u.id=f.id WHERE f.first_name LIKE '%#$expr%'".as[(String,String,String)]
  }

  def getUser(userId: String) =
    sql"SELECT id,name FROM Users WHERE id='#$userId'"
      .as[(String, String)]

}
