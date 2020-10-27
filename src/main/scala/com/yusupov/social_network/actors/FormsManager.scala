package com.yusupov.social_network.actors

import akka.actor.{Actor, ActorRef}
import akka.pattern.ask
import akka.util.Timeout
import com.softwaremill.tagging.@@
import com.typesafe.scalalogging.LazyLogging
import com.yusupov.social_network.actors.Database.DatabaseTag
import com.yusupov.social_network.data.UserForm

import scala.util.Success

object FormsManager {
  trait FormsManagerTag

  def name = "forms-manager"

  sealed trait Request
  case class GetForm(userId: String) extends Request
  case class UpdateForm(userId: String, form: UserForm) extends Request

  sealed trait Response
  case object FormUpdated extends Response
  case class RequestedForm(form: UserForm) extends Response
  case object FormsInternalError extends Response

}

import FormsManager._

class FormsManager(
  requestTimeout: Timeout,
  database: ActorRef @@ DatabaseTag
) extends Actor with LazyLogging {

  import context.dispatcher

  implicit val timeout = requestTimeout

  override def receive = {
    case GetForm(userId) =>
      logger.debug(s"Getting form by $userId")
      val requester = sender()
      (database ? Database.GetForm(userId))
        .onComplete {
          case Success(Database.RequestedForm(form)) =>
            requester ! RequestedForm(form)
          case _ =>
            requester ! FormsInternalError
        }

    case UpdateForm(userId, form) =>
      logger.debug(s"Updating form for $userId")
      val requester = sender()
      (database ? Database.UpdateForm(userId, form))
        .onComplete {
          case Success(Database.FormUpdated) =>
            requester ! FormUpdated
          case _ =>
            requester ! FormsInternalError
        }
  }
}
