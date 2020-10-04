name := "social_network"

version := "0.1"

scalaVersion := "2.13.3"

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
    "com.softwaremill.macwire" %% "proxy" % macWireVersion
  )
}