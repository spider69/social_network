package com.yusupov.social_network

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.Materializer
import akka.util.Timeout
import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.scalalogging.LazyLogging
import com.yusupov.social_network.actors.RestApi
import com.yusupov.social_network.injection.ServiceModule

object Main extends App
  with RequestTimeout
  with ServiceModule
  with LazyLogging
{

  val config = ConfigFactory.load()
  val requestTimeout = requestTimeout(config)

  val host = config.getString("service-settings.host")
  val port = config.getInt("service-settings.port")

  implicit val system = ActorSystem()
  implicit val ec = system.dispatcher

  val api = new RestApi(system, requestTimeout, socialNetwork).routes

  implicit val materializer = Materializer(system)
  val bindingFuture = Http().newServerAt(host, port).bind(api)

  bindingFuture.map {
    serverBinding => logger.info(s"RestApi bounds to ${serverBinding.localAddress}")
  }.recover {
    case e: Exception =>
      logger.error(s"Failed to bind to $host:$port!")
      logger.error(s"${e.getMessage}")
      system.terminate()
  }
}

trait RequestTimeout {
  import scala.concurrent.duration._
  def requestTimeout(config: Config): Timeout = {
    val t = config.getString("akka.http.server.request-timeout")
    val d = Duration(t)
    FiniteDuration(d.length, d.unit)
  }
}
