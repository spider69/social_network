package com.yusupov.social_network.actors

import java.util.UUID

import akka.actor.{Actor, Props}
import akka.util.Timeout
import com.typesafe.scalalogging.LazyLogging
import com.yusupov.social_network.actors.Database.CreateForm
import com.yusupov.social_network.data.Form
import slick.jdbc.MySQLProfile

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object Database {
  trait DatabaseTag

  def props(implicit timeout: Timeout) = Props(new Database)
  def name = "database"

  // requests
  case class CreateUser(name: String, password: String)
  case class CreateForm(form: Form)

  // responses
  case class UserCreated(id: UUID)
  case object FormCreated
}

import Database._

class Database() extends Actor with LazyLogging {

  import MySQLProfile.api._

  lazy val database = MySQLProfile.api.Database.forConfig("database")

  override def receive: Receive = {
    case CreateUser(name, password) =>
      logger.debug(s"Creating user $name")
      // create user in database
      val userId = UUID.randomUUID()
      val userIdString = userId.toString
      val query = sqlu"INSERT INTO Users(id,name,password) VALUES('#$userIdString','#$name','#$password')"
      val future = database.run(query)
      Await.result(future, Duration.Inf)
      sender() ! UserCreated(userId)

    case CreateForm(form) =>
      logger.debug(s"Creating form for ${form.name}")
      // write form to database
      sender() ! FormCreated
  }
}
