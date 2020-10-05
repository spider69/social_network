package com.yusupov.social_network.database

import slick.dbio.DBIO
import slick.jdbc.JdbcProfile

import scala.concurrent.Await
import scala.concurrent.duration.Duration

trait DatabaseProvider[T <: JdbcProfile] {

  val profile: T

  lazy val database = profile.api.Database.forConfig("database")

  def exec[R](action: DBIO[R], timeout: Duration = Duration.Inf) = {
    val future = database.run(action)
    Await.result(future, timeout)
  }

}
