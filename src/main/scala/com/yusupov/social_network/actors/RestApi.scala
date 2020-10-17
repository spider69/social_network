package com.yusupov.social_network.actors

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import com.softwaremill.tagging._
import com.yusupov.social_network.actors.Authenticator.AuthenticatorTag
import com.yusupov.social_network.actors.FormsManager.FormsManagerTag
import com.yusupov.social_network.actors.FriendsManager.FriendsManagerTag
import com.yusupov.social_network.api.{AuthenticatorApi, FormsApi, FriendsApi}

class RestApi(
  system: ActorSystem,
  timeout: Timeout,
  authenticatorProps: Props @@ AuthenticatorTag,
  formsManagerProps: Props @@ FormsManagerTag,
  friendsManagerProps: Props @@ FriendsManagerTag
) extends RestRoutes {

  implicit val requestTimeout = timeout
  implicit def executionContext = system.dispatcher

  override def createAuthenticator() = system.actorOf(authenticatorProps).taggedWith[AuthenticatorTag]

  override def createFormsManager() = system.actorOf(formsManagerProps).taggedWith[FormsManagerTag]

  override def createFriendsManager() = system.actorOf(friendsManagerProps).taggedWith[FriendsManagerTag]
}

trait RestRoutes extends AuthenticatorApi
  with FormsApi
  with FriendsApi
{
  def routes: Route = {
    uiRoute ~
      authRoute ~
      usersRoute ~
      friendsRoute ~
      formsRoute
  }

  private def uiRoute = {
    pathEndOrSingleSlash {
      getFromResource("web/index.html")
    } ~
      pathPrefix("login") {
        indexRoute
      } ~
      (pathPrefix("signup") | pathPrefix("home") | pathPrefix("user_forms") | pathPrefix("edit_form") | pathPrefix("users")) {
        extractUnmatchedPath {_ =>
          indexRoute
        }
      } ~
      getFromResourceDirectory("web")
  }

  private def indexRoute: Route = getFromResource("web/index.html")

}