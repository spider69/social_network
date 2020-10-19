package com.yusupov.social_network

import java.security.{KeyStore, SecureRandom}

import akka.actor.ActorSystem
import akka.http.scaladsl.{ConnectionContext, Http}
import akka.stream.Materializer
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import com.yusupov.social_network.actors.RestApi
import com.yusupov.social_network.injection.ServiceModule
import javax.net.ssl.{KeyManagerFactory, SSLContext, TrustManagerFactory}
import org.flywaydb.core.Flyway

import scala.concurrent.duration.{Duration, FiniteDuration}
import scala.reflect.io.File

object Main extends App
  with ServiceModule
  with LazyLogging
{

  implicit val system = ActorSystem()
  implicit val materializer = Materializer(system)
  implicit val ec = system.dispatcher

  val requestTimeout = getRequestTimeout

  val host = config.getString("service-settings.host")
  val port = config.getInt("service-settings.port")

  performMigration()

  startServer().map {
    serverBinding => logger.info(s"RestApi bounds to ${serverBinding.localAddress}")
  }.recover {
    case e: Exception =>
      logger.error(s"Failed to bind to $host:$port!")
      logger.error(s"${e.getMessage}")
      system.terminate()
  }

  private def startServer() = {
    val api = new RestApi(system, requestTimeout, authenticator, formsManager, friendsManager).routes
    val httpsContext = createHttpsContext()
    Http().newServerAt(host, port).enableHttps(httpsContext).bind(api)
  }

  private def createHttpsContext() = {
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
    ConnectionContext.httpsServer(sslContext)
  }

  private def performMigration() =
    Flyway
      .configure()
      .baselineOnMigrate(true)
      .locations(s"classpath:/db/migration/${config.getString("database.type")}sql")
      .dataSource(
        config.getString("database.url"),
        config.getString("database.user"),
        config.getString("database.password")
      )
      .load()
      .migrate()

  private def getRequestTimeout = {
    val t = config.getString("akka.http.server.request-timeout")
    val d = Duration(t)
    FiniteDuration(d.length, d.unit)
  }

}

