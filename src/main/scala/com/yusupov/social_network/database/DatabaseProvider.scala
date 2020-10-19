package com.yusupov.social_network.database

import com.typesafe.config.ConfigFactory
import slick.dbio.DBIO
import slick.jdbc.JdbcProfile

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object DatabaseProvider {

  def create = {
    val config = ConfigFactory.load()
    val dbType = config.getString("database.type")
    dbType match {
      case "my" =>
        new MySqlProvider
      case "postgre" =>
        new PostgresProvider
      case _ =>
        throw new Exception("Invalid database type")
    }
  }


}

trait DatabaseProvider[T <: JdbcProfile] {

  val profile: T

  val database: profile.backend.Database

  def hex(value: String): String
  def unhex(value: String): String

  def exec[R](action: DBIO[R], timeout: Duration = Duration.Inf) = {
    val future = database.run(action)
    Await.result(future, timeout)
  }

}
