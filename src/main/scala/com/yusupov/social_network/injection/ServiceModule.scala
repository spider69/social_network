package com.yusupov.social_network.injection

import akka.actor.{ActorSystem, Props}
import akka.util.Timeout
import com.softwaremill.macwire.akkasupport.wireProps
import com.softwaremill.tagging._
import com.yusupov.social_network.actors.SocialNetwork
import com.yusupov.social_network.actors.SocialNetwork.SocialNetworkTag

trait ServiceModule extends DatabaseModule {

  implicit def system: ActorSystem
  implicit def requestTimeout: Timeout

  lazy val socialNetwork: Props @@ SocialNetworkTag = wireProps[SocialNetwork].taggedWith[SocialNetworkTag]

}
