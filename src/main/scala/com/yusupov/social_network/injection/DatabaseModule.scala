package com.yusupov.social_network.injection

import akka.actor.ActorSystem
import com.softwaremill.macwire.akkasupport.wireProps
import com.softwaremill.macwire.wire
import com.softwaremill.tagging._
import com.typesafe.config.ConfigFactory
import com.yusupov.social_network.actors.Database
import com.yusupov.social_network.actors.Database.DatabaseTag
import com.yusupov.social_network.database.{MySqlProvider, PostgresProvider}
import slick.jdbc.{MySQLProfile, PostgresProfile}

trait DatabaseModule {

  implicit def system: ActorSystem

  val config = ConfigFactory.load().resolve()
  val dbType = config.getString("database.type")

  lazy val database = dbType match {
    case "my" =>
      val databaseProvider = wire[MySqlProvider]
      system.actorOf(wireProps[Database[MySQLProfile]], "database").taggedWith[DatabaseTag]
    case "postgre" =>
      val databaseProvider = wire[PostgresProvider]
      system.actorOf(wireProps[Database[PostgresProfile]], "database").taggedWith[DatabaseTag]
    case _ =>
      throw new Exception("Invalid database type")
  }

}
