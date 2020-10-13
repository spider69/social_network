package com.yusupov.social_network.actors

import java.util.UUID

import akka.actor.Actor
import com.typesafe.scalalogging.LazyLogging
import com.yusupov.social_network.actors.Database.CreateForm
import com.yusupov.social_network.data.UserForm
import com.yusupov.social_network.database.DatabaseProvider
import com.yusupov.social_network.database.handlers.{AuthHandler, FormsHandler, UsersHandler}
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

  case class GetUsers(filterExpr: Option[String])
  case class CreateForm(userId: String, form: UserForm)
  case class GetForm(userId: String)
  case class UpdateForm(userId: String, form: UserForm)

  // responses
  case class Users(users: Seq[(String, String, String)])
  case class UserById(id: String, name: String, password: String)
  case object UserNotFound
  case object UserCreated
  case class SessionCreated(sessionId: String)
  case class SessionIsValid(user: String)
  case object SessionIsInvalid
  case object SessionDeleted

  case object FormCreated
  case class RequestedForm(form: UserForm)
  case object FormUpdated
  case object FormNotFound
}

import com.yusupov.social_network.actors.Database._

class Database[T <: JdbcProfile](
  databaseProvider: DatabaseProvider[T]
) extends Actor
  with LazyLogging
{

  val authHandler = new AuthHandler[T](databaseProvider)
  val usersHandler = new UsersHandler[T](databaseProvider)
  val formsHandler = new FormsHandler[T](databaseProvider)

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
      val createUser = authHandler.createUser(email, name, password)
      val createDefaultForm = formsHandler.createForm(email, UserForm(firstName = name))
      databaseProvider.exec(
        createUser.andThen(createDefaultForm)
      )
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
        case Vector((_, userId), _*) => sender() ! SessionIsValid(userId)
        case _ => sender() ! SessionIsInvalid
      }

    case DeleteSession(sessionId) =>
      logger.debug(s"Deleting session $sessionId")
      val query = authHandler.deleteSession(sessionId)
      databaseProvider.exec(query)
      sender() ! SessionDeleted

    case GetUsers(filterExpr) =>
      logger.debug("Getting users")
      val query = usersHandler.getUsers(filterExpr)
      val result = databaseProvider.exec(query)
      sender() ! Users(result)

    case CreateForm(userId, form) =>
      logger.debug(s"Creating form for $userId")
      val query = formsHandler.createForm(userId, form)
      databaseProvider.exec(query)
      sender() ! FormCreated

    case GetForm(userId) =>
      logger.debug(s"Getting form for $userId")
      val query = formsHandler.getForm(userId)
      val result = databaseProvider.exec(query)
      result match {
        case Vector((firstName, lastName, age, gender, interests, city), _*) => sender() ! RequestedForm(UserForm(firstName, lastName, age, gender, interests, city))
        case _ => sender() ! FormNotFound
      }

    case UpdateForm(userId, form) =>
      logger.debug(s"Updating form for $userId")
      val query = formsHandler.updateForm(userId, form)
      databaseProvider.exec(query)
      sender() ! FormUpdated

  }
}
