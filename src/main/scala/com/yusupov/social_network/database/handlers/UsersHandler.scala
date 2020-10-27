package com.yusupov.social_network.database.handlers

import akka.actor.ActorRef
import com.typesafe.scalalogging.LazyLogging
import com.yusupov.social_network.actors.Database.{GetUsers, Users}
import com.yusupov.social_network.database.DatabaseProvider
import slick.jdbc.JdbcProfile

class UsersHandler[T <: JdbcProfile](databaseProvider: DatabaseProvider[T]) extends Handler with LazyLogging {
  import databaseProvider.profile.api._

  def getUsers(filterExpr: Option[String]) = filterExpr match {
    case Some("") | None => sql"SELECT id, first_name, last_name FROM Users".as[(String,String,String)]
    case Some(expr) => sql"SELECT id, first_name, last_name FROM Users WHERE first_name LIKE '%#$expr%'".as[(String,String,String)]
  }

  override def handle(sender: ActorRef) = {
    case GetUsers(filterExpr) =>
      logger.debug("Getting users")
      val query = getUsers(filterExpr)
      val result = databaseProvider.exec(query)
      sender ! Users(result)
  }
}
