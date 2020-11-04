package com.yusupov.social_network.database.handlers

import java.util.UUID

import com.typesafe.scalalogging.LazyLogging
import com.yusupov.social_network.actors.Database._
import com.yusupov.social_network.database.DatabaseProvider
import com.yusupov.social_network.utils.HexUtils.bytesToHex
import com.yusupov.social_network.utils.SecurityUtils
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext

class AuthHandler[T <: JdbcProfile](
  databaseProvider: DatabaseProvider[T]
) extends Handler with LazyLogging {
  import databaseProvider.profile.api._

  def createUser(email: String, name: String, passwordHash: String, salt: String) =
    sqlu"INSERT INTO Users(id,password_hash,salt,first_name) VALUES('#$email','#$passwordHash', #${databaseProvider.unhex(salt)}, '#$name')"

  def createSession(sessionId: String, userId: String) =
    sqlu"INSERT INTO Sessions(id,user_id) VALUES('#$sessionId','#$userId')"

  def getSession(sessionId: String) =
    sql"SELECT id,user_id FROM Sessions WHERE id='#$sessionId' LIMIT 1"
      .as[(String, String)]

  def deleteSession(sessionId: String) =
    sqlu"DELETE FROM Sessions WHERE id='#$sessionId'"

  def getUser(id: String) =
    sql"SELECT id,first_name,password_hash,#${databaseProvider.hex("salt")} FROM Users WHERE id='#$id' LIMIT 1"
      .as[(String,String,String,String)]

  override def handle(implicit ec: ExecutionContext) = {
    case GetUserById(id) =>
      logger.debug(s"Getting user by id: $id")
      getUser(id).map {
        case Vector(user, _*) => UserById(user._1, user._2, user._3, user._4)
        case _ => UserNotFound
      }

    case CreateUser(email, name, password) =>
      logger.debug(s"Creating user $name")
      val salt = SecurityUtils.generateSalt()
      val passwordHash = SecurityUtils.passwordHash(password, salt)
      createUser(email, name, passwordHash, bytesToHex(salt))
        .map(_ => UserCreated)

    case CreateSession(userId) =>
      logger.debug(s"Creating session for $userId")
      val sessionId = UUID.randomUUID().toString
      createSession(sessionId, userId)
        .map(_ => SessionCreated(sessionId))

    case CheckSession(sessionId) =>
      logger.debug(s"Checking session $sessionId")
      getSession(sessionId).map {
        case Vector((_, userId), _*) => SessionIsValid(userId)
        case _ => SessionIsInvalid
      }

    case DeleteSession(sessionId) =>
      logger.debug(s"Deleting session $sessionId")
      deleteSession(sessionId)
        .map(_ => SessionDeleted)
  }
}
