package com.yusupov.social_network.database.handlers

import java.util.UUID

import akka.actor.ActorRef
import com.typesafe.scalalogging.LazyLogging
import com.yusupov.social_network.actors.Database.{CheckSession, CreateSession, CreateUser, DeleteSession, GetUserById, SessionCreated, SessionDeleted, SessionIsInvalid, SessionIsValid, UserById, UserCreated, UserNotFound}
import com.yusupov.social_network.data.UserForm
import com.yusupov.social_network.database.DatabaseProvider
import slick.jdbc.JdbcProfile

class AuthHandler[T <: JdbcProfile](
  databaseProvider: DatabaseProvider[T],
  formsHandler: FormsHandler[T]
) extends Handler with LazyLogging {
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

  override def handle(sender: ActorRef) = {
    case GetUserById(id) =>
      logger.debug(s"Getting user by id: $id")
      val query = getUser(id)
      val result = databaseProvider.exec(query)
      result match {
        case Vector(user, _*) => sender ! UserById(user._1, user._2, user._3)
        case _ => sender ! UserNotFound
      }

    case CreateUser(email, name, password) =>
      logger.debug(s"Creating user $name")
      val createUserQuery = createUser(email, name, password)
      val createDefaultFormQuery = formsHandler.createForm(email, UserForm(firstName = name))
      databaseProvider.exec(
        createUserQuery.andThen(createDefaultFormQuery)
      )
      sender ! UserCreated

    case CreateSession(userId) =>
      logger.debug(s"Creating session for $userId")
      val sessionId = UUID.randomUUID().toString
      val query = createSession(sessionId, userId)
      databaseProvider.exec(query)
      sender ! SessionCreated(sessionId)

    case CheckSession(sessionId) =>
      logger.debug(s"Checking session $sessionId")
      val query = getSession(sessionId)
      val result = databaseProvider.exec(query)
      result match {
        case Vector((_, userId), _*) => sender ! SessionIsValid(userId)
        case _ => sender ! SessionIsInvalid
      }

    case DeleteSession(sessionId) =>
      logger.debug(s"Deleting session $sessionId")
      val query = deleteSession(sessionId)
      databaseProvider.exec(query)
      sender ! SessionDeleted
  }
}
