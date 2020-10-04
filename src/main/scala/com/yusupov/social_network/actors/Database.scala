package com.yusupov.social_network.actors

import java.util.UUID

import akka.actor.{Actor, Props}
import akka.util.Timeout
import com.typesafe.scalalogging.LazyLogging
import com.yusupov.social_network.actors.Database.CreateForm
import com.yusupov.social_network.data.Form

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
  override def receive: Receive = {
    case CreateUser(name, password) =>
      logger.debug(s"Creating user $name")
      // create user in database
      val userId = UUID.randomUUID()
      sender() ! UserCreated(userId)

    case CreateForm(form) =>
      logger.debug(s"Creating form for ${form.name}")
      // write form to database
      sender() ! FormCreated
  }
}
