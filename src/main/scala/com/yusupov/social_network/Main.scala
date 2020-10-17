package com.yusupov.social_network

import java.security.{KeyStore, SecureRandom}

import akka.actor.ActorSystem
import akka.http.scaladsl.{ConnectionContext, Http}
import akka.stream.Materializer
import akka.util.Timeout
import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.scalalogging.LazyLogging
import com.yusupov.social_network.actors.RestApi
import com.yusupov.social_network.injection.ServiceModule
import javax.net.ssl.{KeyManagerFactory, SSLContext, TrustManagerFactory}
import org.flywaydb.core.Flyway

import scala.reflect.io.File

object Main extends App
  with RequestTimeout
  with ServiceModule
  with LazyLogging
{

  val config = ConfigFactory.load()
  val requestTimeout = requestTimeout(config)

  val host = config.getString("service-settings.host")
  val port = config.getInt("service-settings.port")

  lazy val flyway = Flyway
    .configure()
    .baselineOnMigrate(true)
    .dataSource(
      config.getString("database.properties.url"),
      config.getString("database.properties.user"),
      config.getString("database.properties.password")
    )
    .load()
  flyway.migrate()

  implicit val system = ActorSystem()
  implicit val ec = system.dispatcher

  val api = new RestApi(system, requestTimeout, authenticator, formsManager, friendsManager).routes

  implicit val materializer = Materializer(system)

  val certificatePath = config.getString("service-settings.https-settings.certificate-path")
  val password = config.getString("service-settings.https-settings.password").toCharArray
  val keyStore = KeyStore.getInstance("PKCS12")
  val certificate = File(certificatePath).inputStream()

  require(certificate != null, "Keystore required!")
  keyStore.load(certificate, password)

  val keyManagerFactory = KeyManagerFactory.getInstance("SunX509")
  keyManagerFactory.init(keyStore, password)

  val tmf = TrustManagerFactory.getInstance("SunX509")
  tmf.init(keyStore)

  val sslContext = SSLContext.getInstance("TLS")
  sslContext.init(keyManagerFactory.getKeyManagers, tmf.getTrustManagers, new SecureRandom)
  val https = ConnectionContext.httpsServer(sslContext)

  val bindingFuture = Http().newServerAt(host, port).enableHttps(https).bind(api)

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
