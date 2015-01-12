name := """play-server-template"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

resolvers += "maven-repository" at "http://maven-repository.com/"

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  anorm,
  cache,
  "org.scalatestplus" %% "play" % "1.2.0" % "test",
  "org.scalatest" % "scalatest_2.11" % "2.2.2" % "test",
  "org.reactivemongo" %% "play2-reactivemongo" % "0.10.5.0.akka23"
)
