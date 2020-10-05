package com.yusupov.social_network.database.handlers

import java.util.UUID

import com.yusupov.social_network.database.DatabaseProvider
import slick.jdbc.JdbcProfile

class AuthHandler[T <: JdbcProfile](databaseProvider: DatabaseProvider[T]) {
  import databaseProvider.profile.api._

  def createUser(name: String, password: String) = {
    val userId = UUID.randomUUID()
    val userIdString = userId.toString
    sqlu"INSERT INTO Users(id,name,password) VALUES('#$userIdString','#$name','#$password')"
  }
}
