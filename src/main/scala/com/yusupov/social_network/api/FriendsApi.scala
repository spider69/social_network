package com.yusupov.social_network.api

import akka.actor.ActorRef
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{complete, onSuccess, parameters, pathPrefix, _}
import akka.pattern.ask
import akka.util.Timeout
import com.softwaremill.tagging._
import com.typesafe.scalalogging.LazyLogging
import com.yusupov.social_network.actors.FriendsManager
import com.yusupov.social_network.data.{JsonMarshaller, User}

trait FriendsApi extends LazyLogging with JsonMarshaller {
  this: SessionChecker =>

  import com.yusupov.social_network.actors.FriendsManager._

  def createFriendsManager(): ActorRef @@ FriendsManagerTag

  lazy val friendsManager = createFriendsManager()

  implicit def requestTimeout: Timeout

  def usersRoute = {
    pathPrefix("all_users") {
      parameters("filter".optional) { filterExpr =>
        checkSession(_ => {
          onSuccess(getAllUsers(filterExpr)) {
            case FriendsManager.Users(users) => complete(StatusCodes.OK, users.map(u => User(u._1, u._2, u._3)))
            case _ => complete(StatusCodes.InternalServerError)
          }
        })
      }
    }
  }

  def friendsRoute = {
    pathPrefix("add_friend") {
      parameters("user", "friend") { (user, friend) =>
        checkSession(_ => {
          onSuccess(addFriend(user, friend)) {
            case FriendsManager.FriendAdded => complete(StatusCodes.Created)
            case _ => complete(StatusCodes.InternalServerError)
          }
        })
      }
    } ~
      pathPrefix("get_friends" / Segment) { user =>
        checkSession(_ => {
          onSuccess(getFriends(user)) {
            case FriendsManager.Users(users) => complete(StatusCodes.Created, users.map(u => User(u._1, u._2, u._3)))
            case _ => complete(StatusCodes.InternalServerError)
          }
        })
      } ~
      pathPrefix("remove_friend") {
        parameters("user", "friend") { (user, friend) =>
          checkSession(_ => {
            onSuccess(removeFriend(user, friend)) {
              case FriendsManager.FriendRemoved => complete(StatusCodes.OK)
              case _ => complete(StatusCodes.InternalServerError)
            }
          })
        }
      }
  }

  private def getAllUsers(filterExpr: Option[String]) = {
    logger.debug(s"API: getting all users")
    friendsManager.ask(GetUsers(filterExpr)).mapTo[Response]
  }

  private def addFriend(userId: String, friendId: String) = {
    logger.debug(s"API: add to friends $friendId for $userId")
    friendsManager.ask(AddFriend(userId, friendId)).mapTo[Response]
  }

  private def removeFriend(userId: String, friendId: String) = {
    logger.debug(s"API: remove from friends $friendId for $userId")
    friendsManager.ask(RemoveFriend(userId, friendId)).mapTo[Response]
  }

  private def getFriends(userId: String) = {
    logger.debug(s"API: get friends of $userId")
    friendsManager.ask(GetFriends(userId)).mapTo[Response]
  }

}
