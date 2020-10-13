package com.yusupov.social_network.database.handlers

import com.yusupov.social_network.database.DatabaseProvider
import slick.jdbc.JdbcProfile

class AuthHandler[T <: JdbcProfile](databaseProvider: DatabaseProvider[T]) {
  import databaseProvider.profile.api._

  def createUser(email: String, name: String, password: String) =
    sqlu"INSERT INTO Users(id,name,password) VALUES('#$email','#$name','#$password')"

  def createSession(sessionId: String, userId: String) =
    sqlu"INSERT INTO Sessions(id,user_id) VALUES('#$sessionId','#$userId')"

  def getSession(sessionId: String) =
    sql"SELECT id,user_id FROM Sessions WHERE id='#$sessionId' LIMIT 1"
      .as[(String, String)]

  def deleteSession(sessionId: String) =
    sqlu"DELETE FROM Sessions WHERE id='#$sessionId'"

  def getUser(id: String) =
    sql"SELECT id,name,password FROM Users WHERE id='#$id' LIMIT 1"
      .as[(String,String,String)]

}
