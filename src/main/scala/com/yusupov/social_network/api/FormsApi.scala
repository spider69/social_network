package com.yusupov.social_network.api

import akka.actor.ActorRef
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{as, complete, entity, onSuccess, pathPrefix, _}
import akka.pattern.ask
import akka.util.Timeout
import com.softwaremill.tagging._
import com.typesafe.scalalogging.LazyLogging
import com.yusupov.social_network.actors.FormsManager
import com.yusupov.social_network.data.{JsonMarshaller, UserForm}

trait FormsApi extends LazyLogging with JsonMarshaller {
  this: SessionChecker =>

  import com.yusupov.social_network.actors.FormsManager._

  def createFormsManager(): ActorRef @@ FormsManagerTag

  lazy val formsManager = createFormsManager()

  implicit def requestTimeout: Timeout

  def formsRoute = {
    (pathPrefix("create_form") & post) {
      checkSession(userId => {
        entity(as[UserForm]) {
          form =>
            onSuccess(createForm(userId, form)) {
              case FormsManager.FormCreated => complete(StatusCodes.Created)
              case _ => complete(StatusCodes.InternalServerError)
            }
        }
      })
    } ~
      pathPrefix("get_form" / Segment) { userId =>
        checkSession(_ => {
          onSuccess(getForm(userId)) {
            case FormsManager.RequestedForm(form) => complete(StatusCodes.Created, form)
            case _ => complete(StatusCodes.InternalServerError)
          }
        })
      } ~
      (pathPrefix("update_form" / Segment) & post) { userId =>
        checkSession(loggedInUserId => {
          if (loggedInUserId != userId) {
            complete(StatusCodes.Forbidden, "Editing page of other user is forbidden")
          } else {
            entity(as[UserForm]) {
              form =>
                onSuccess(updateForm(userId, form)) {
                  case FormsManager.FormUpdated => complete(StatusCodes.OK)
                  case _ => complete(StatusCodes.InternalServerError)
                }
            }
          }
        })
      }
  }

  private def createForm(userId: String, form: UserForm) = {
    logger.debug(s"API: createForm for ${form.firstName}")
    formsManager.ask(CreateForm(userId, form)).mapTo[Response]
  }

  private def getForm(userId: String) = {
    logger.debug(s"API: getForm for $userId")
    formsManager.ask(GetForm(userId)).mapTo[Response]
  }

  private def updateForm(userId: String, form: UserForm) = {
    logger.debug(s"API: updateForm for $userId")
    formsManager.ask(UpdateForm(userId, form)).mapTo[Response]
  }

}
