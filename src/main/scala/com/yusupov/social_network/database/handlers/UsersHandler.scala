package com.yusupov.social_network.database.handlers

import akka.actor.ActorRef
import com.typesafe.scalalogging.LazyLogging
import com.yusupov.social_network.actors.Database.{GetUsers, Users}
import com.yusupov.social_network.database.DatabaseProvider
import slick.jdbc.JdbcProfile

class UsersHandler[T <: JdbcProfile](databaseProvider: DatabaseProvider[T]) extends Handler with LazyLogging {
  import databaseProvider.profile.api._

  def getUsers(filterExpr: Option[String]) = filterExpr match {
    case Some("") | None => sql"SELECT f.id, f.first_name, f.last_name FROM Users u JOIN UserForms f ON u.id=f.id".as[(String,String,String)]
    case Some(expr) => sql"SELECT f.id, f.first_name, f.last_name FROM Users u JOIN UserForms f ON u.id=f.id WHERE f.first_name LIKE '%#$expr%'".as[(String,String,String)]
  }

  override def handle(sender: ActorRef) = {
    case GetUsers(filterExpr) =>
      logger.debug("Getting users")
      val query = getUsers(filterExpr)
      val result = databaseProvider.exec(query)
      sender ! Users(result)
  }
}
