package com.yusupov.social_network.injection

import akka.actor.ActorSystem
import com.softwaremill.macwire.akkasupport.wireProps
import com.softwaremill.macwire.wire
import com.softwaremill.tagging._
import com.yusupov.social_network.actors.Database
import com.yusupov.social_network.actors.Database.DatabaseTag
import com.yusupov.social_network.database.MySqlProvider
import slick.jdbc.MySQLProfile

trait DatabaseModule {

  implicit def system: ActorSystem

  lazy val databaseProvider = wire[MySqlProvider]
  lazy val database = system.actorOf(wireProps[Database[MySQLProfile]], "database").taggedWith[DatabaseTag]

}
