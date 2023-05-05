//import play.core.PlayVersion.akkaVersion
name := """play-demo-app"""
organization := "com.hashedin"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.10"
//akkaVersion := "2.8.0"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % Test

libraryDependencies += "com.typesafe.play" %% "play-slick" % "5.1.0"
libraryDependencies += "org.postgresql" % "postgresql" % "42.5.4"
//libraryDependencies += "com.typesafe.akka" %% "akka-actor" % akkaVersion
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.7.0",
  "com.typesafe.akka" %% "akka-actor-typed" % "2.7.0",
  "com.typesafe.akka" %% "akka-slf4j" % "2.7.0",
  "com.typesafe.akka" %% "akka-protobuf-v3" % "2.7.0",
  "com.typesafe.akka" %% "akka-stream" % "2.7.0",
  "com.typesafe.akka" %% "akka-serialization-jackson" % "2.7.0"
)

libraryDependencies += "com.lightbend.akka" %% "akka-stream-alpakka-csv" % "5.0.0"


// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.hashedin.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.hashedin.binders._"
