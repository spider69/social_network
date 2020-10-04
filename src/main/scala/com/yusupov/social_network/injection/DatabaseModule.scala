package com.yusupov.social_network.injection

import akka.actor.ActorSystem
import com.softwaremill.macwire.akkasupport.wireProps
import com.softwaremill.tagging._
import com.yusupov.social_network.actors.Database
import com.yusupov.social_network.actors.Database.DatabaseTag

trait DatabaseModule {

  implicit def system: ActorSystem

  lazy val database = system.actorOf(wireProps[Database], "database").taggedWith[DatabaseTag]

}
