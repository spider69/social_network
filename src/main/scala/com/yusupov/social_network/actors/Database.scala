package com.yusupov.social_network.actors

import akka.actor.Actor
import com.typesafe.scalalogging.LazyLogging
import com.yusupov.social_network.actors.Database.CreateForm
import com.yusupov.social_network.data.Form
import com.yusupov.social_network.database.DatabaseProvider
import com.yusupov.social_network.database.handlers.AuthHandler
import slick.jdbc.JdbcProfile

object Database {
  trait DatabaseTag

  def name = "database"

  // requests
  case class CreateUser(name: String, password: String)
  case class CreateForm(form: Form)

  // responses
  case object UserCreated
  case object FormCreated
}

import com.yusupov.social_network.actors.Database._

class Database[T <: JdbcProfile](
  databaseProvider: DatabaseProvider[T]
) extends Actor
  with LazyLogging
{

  val authHandler = new AuthHandler[T](databaseProvider)

  override def receive: Receive = {
    case CreateUser(name, password) =>
      logger.debug(s"Creating user $name")
      val query = authHandler.createUser(name, password)
      databaseProvider.exec(query)
      sender() ! UserCreated

    case CreateForm(form) =>
      logger.debug(s"Creating form for ${form.name}")
      // write form to database
      sender() ! FormCreated
  }
}
