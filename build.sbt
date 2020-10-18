import com.typesafe.sbt.packager.MappingsHelper.directory

version := "1.0.0"
scalaVersion := "2.13.3"

lazy val backend = (project in file("."))
  .enablePlugins(JavaAppPackaging)
  .settings(
    name := "social_network",
    packagingSettings,
    libraryDependencies ++= {
      val akkaVersion = "2.6.9"
      val akkaHttpVersion = "10.2.0"
      val macWireVersion = "2.3.7"
      Seq(
        "com.typesafe.akka" %% "akka-actor"  % akkaVersion,
        "com.typesafe.akka" %% "akka-stream" % akkaVersion,
        "com.typesafe.akka" %% "akka-http-core" % akkaHttpVersion,
        "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,

        "ch.qos.logback" % "logback-classic" % "1.2.3",
        "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",

        "com.softwaremill.macwire" %% "macros" % macWireVersion % Provided,
        "com.softwaremill.macwire" %% "macrosakka" % macWireVersion % Provided,
        "com.softwaremill.macwire" %% "util" % macWireVersion,
        "com.softwaremill.macwire" %% "proxy" % macWireVersion,

        "mysql" % "mysql-connector-java" % "8.0.21",
        "com.typesafe.slick" %% "slick" % "3.3.3",
        "com.typesafe.slick" %% "slick-hikaricp" % "3.3.3",
        "org.flywaydb" % "flyway-core" % "7.0.0",

        "commons-codec" % "commons-codec" % "1.15"
      )
    }
  )

lazy val bashConfigDefines = Seq(
  """addJava "-Dconfig.file=${app_home}/../conf/application.conf"""",
  """addJava "-Dlogback.configurationFile=${app_home}/../conf/logback.xml""""
)

lazy val batConfigDefines = Seq(
  """call :add_java "-Dconfig.file=%APP_HOME%\conf\application.conf"""",
  """call :add_java "-Dlogback.configurationFile=%APP_HOME%\conf\logback.xml""""
)

lazy val packagingSettings = Seq(
  executableScriptName := "run_social_network",
  mappings in Universal ++= directory(baseDirectory.value / "conf"),
  mappings in Universal ++= directory(baseDirectory.value / "cert"),
  mappings in(Compile, packageDoc) := Seq(),
  bashScriptExtraDefines ++= bashConfigDefines,
  batScriptExtraDefines ++= batConfigDefines
)