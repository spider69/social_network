package com.yusupov.social_network.actors

import akka.actor.Actor
import com.typesafe.scalalogging.LazyLogging
import com.yusupov.social_network.data.UserForm
import com.yusupov.social_network.database.DatabaseProvider
import com.yusupov.social_network.database.handlers.{AuthHandler, FormsHandler, FriendsHandler, UsersHandler}
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

  case class AddFriend(userId: String, friendId: String)
  case class RemoveFriend(userId: String, friendId: String)
  case class GetFriends(userId: String)

  // responses
  case class Users(users: Seq[(String, String, String)])
  case class UserById(id: String, name: String, passwordHash: String, salt: String)
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

  case object FriendAdded
  case object FriendRemoved
}

class Database[T <: JdbcProfile](
  databaseProvider: DatabaseProvider[T]
) extends Actor
  with LazyLogging
{

  val formsHandler = new FormsHandler[T](databaseProvider)
  val authHandler = new AuthHandler[T](databaseProvider, formsHandler)
  val usersHandler = new UsersHandler[T](databaseProvider)
  val friendsHandler = new FriendsHandler[T](databaseProvider)

  val handlers = List(
    authHandler,
    formsHandler,
    usersHandler,
    friendsHandler
  )

  override def receive: Receive = {
    case msg =>
      val requester = sender()
      val handler = handlers.map(_.handle(requester)).reduceLeft(_ orElse _)
      handler(msg)
  }

}
