package com.yusupov.social_network.actors

import java.util.UUID

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
  case class GetUserById(id: String)
  case class CreateUser(email: String, name: String, password: String)
  case class CreateSession(userId: String)
  case class CheckSession(sessionId: String)
  case class DeleteSession(sessionId: String)

  case class CreateForm(form: Form)

  // responses
  case class UserById(id: String, name: String, password: String)
  case object UserNotFound
  case object UserCreated
  case class SessionCreated(sessionId: String)
  case object SessionIsValid
  case object SessionIsInvalid
  case object SessionDeleted

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
    case GetUserById(id) =>
      logger.debug(s"Getting user by id: $id")
      val query = authHandler.getUser(id)
      val result = databaseProvider.exec(query)
      result match {
        case Vector(user, _*) => sender() ! UserById(user._1, user._2, user._3)
        case _ => sender() ! UserNotFound
      }

    case CreateUser(email, name, password) =>
      logger.debug(s"Creating user $name")
      val query = authHandler.createUser(email, name, password)
      databaseProvider.exec(query)
      sender() ! UserCreated

    case CreateSession(userId) =>
      logger.debug(s"Creating session for $userId")
      val sessionId = UUID.randomUUID().toString
      val query = authHandler.createSession(sessionId, userId)
      databaseProvider.exec(query)
      sender() ! SessionCreated(sessionId)

    case CheckSession(sessionId) =>
      logger.debug(s"Checking session $sessionId")
      val query = authHandler.getSession(sessionId)
      val result = databaseProvider.exec(query)
      result match {
        case Vector(_, _*) => sender() ! SessionIsValid
        case _ => sender() ! SessionIsInvalid
      }

    case DeleteSession(sessionId) =>
      logger.debug(s"Deleting session $sessionId")
      val query = authHandler.deleteSession(sessionId)
      databaseProvider.exec(query)
      sender() ! SessionDeleted


    case CreateForm(form) =>
      logger.debug(s"Creating form for ${form.name}")
      // write form to database
      sender() ! FormCreated
  }
}
