name := """play-server-template"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

resolvers += "maven-repository" at "http://maven-repository.com/"

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  anorm,
  cache,
  "org.scalatestplus" %% "play" % "1.2.0" % "test",
  "org.reactivemongo" %% "play2-reactivemongo" % "0.10.5.0.akka23",
  "org.reactivemongo" % "reactivemongo-extensions-json_2.11" % "0.10.5.0.0.akka23",
  "com.google.inject" % "guice" % "4.0-beta5",
  "net.codingwell" %% "scala-guice" % "4.0.0-beta5"
)
