package com.yusupov.social_network.injection

import akka.actor.{ActorSystem, Props}
import akka.util.Timeout
import com.softwaremill.macwire.akkasupport.wireProps
import com.softwaremill.tagging._
import com.yusupov.social_network.actors.Authenticator.AuthenticatorTag
import com.yusupov.social_network.actors.FormsManager.FormsManagerTag
import com.yusupov.social_network.actors.FriendsManager.FriendsManagerTag
import com.yusupov.social_network.actors.{Authenticator, FormsManager, FriendsManager}

trait ServiceModule extends DatabaseModule {

  implicit def system: ActorSystem
  implicit def requestTimeout: Timeout

  lazy val authenticator: Props @@ AuthenticatorTag = wireProps[Authenticator].taggedWith[AuthenticatorTag]
  lazy val formsManager: Props @@ FormsManagerTag = wireProps[FormsManager].taggedWith[FormsManagerTag]
  lazy val friendsManager: Props @@ FriendsManagerTag = wireProps[FriendsManager].taggedWith[FriendsManagerTag]

}
