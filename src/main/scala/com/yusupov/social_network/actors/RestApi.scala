package com.yusupov.social_network.actors

import akka.pattern.ask
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import com.softwaremill.tagging._
import akka.http.scaladsl.server.Route
import akka.actor.{ActorRef, ActorSystem, Props}
import akka.util.Timeout
import com.typesafe.scalalogging.LazyLogging
import com.yusupov.social_network.actors.SocialNetwork.{SignUpSuccessful, SocialNetworkTag}
import com.yusupov.social_network.data.{Credentials, Form, JsonMarshaller}

import scala.concurrent.ExecutionContext

class RestApi(
  system: ActorSystem,
  timeout: Timeout,
  socialNetworkProps: Props @@ SocialNetworkTag
) extends RestRoutes {
  implicit val requestTimeout = timeout
  implicit def executionContext = system.dispatcher

  override def createSocialNetwork() = system.actorOf(socialNetworkProps).taggedWith[SocialNetworkTag]
}

trait RestRoutes extends SocialNetworkApi
  with JsonMarshaller
{
  import StatusCodes._

  def routes: Route = authRoute ~ formsRoute

  def authRoute = {
    pathPrefix("sign_up") {
      pathEndOrSingleSlash {
        post {
          entity(as[Credentials]) {
            credentials =>
              onSuccess(signUp(credentials)) {
                case SignUpSuccessful => complete(Created)
                case _ => complete(InternalServerError)
              }
          }
        }
      }
    }
  }

  def formsRoute = {
    pathPrefix("form") {
      pathEndOrSingleSlash {
        post {
          entity(as[Form]) {
            form =>
              onSuccess(createForm(form)) {
                case SocialNetwork.FormCreated => complete(Created)
                case _ => complete(InternalServerError)
              }
          }
        }
      }
    }
  }

}

trait SocialNetworkApi extends LazyLogging {
  import SocialNetwork._

  def createSocialNetwork(): ActorRef

  lazy val socialNetwork = createSocialNetwork()

  implicit def executionContext: ExecutionContext
  implicit def requestTimeout: Timeout

  def signUp(credentials: Credentials) = {
    logger.debug(s"API: signUp for ${credentials.userName}")
    socialNetwork.ask(SignUp(credentials)).mapTo[Response]
  }

  def createForm(form: Form) = {
    logger.debug(s"API: createForm for ${form.name}")
    socialNetwork.ask(CreateForm(form)).mapTo[Response]
  }
}
