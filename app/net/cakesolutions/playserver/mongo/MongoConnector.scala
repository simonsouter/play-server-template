package net.cakesolutions.playserver.mongo

import com.typesafe.config.Config
import play.api.Logger
import reactivemongo.api.{DB, MongoConnection, MongoDriver}

import scala.util.{Failure, Success}

object MongoConnector {
  val logger: Logger = Logger(this.getClass)

  def db(config: Config) = {
    val mongoConfig = parseMongoConfig(config)

    //TODO creates an actor system (should provide play one?)
    val connection = new MongoDriver().connection(mongoConfig)
    logger.info("Conntected to " + mongoConfig.toString)
    DB(mongoConfig.db.get, connection)
  }

  def parseMongoConfig(config: Config): MongoConnection.ParsedURI = {
    val uri = config.getString("mongodb.uri")
    MongoConnection.parseURI(uri) match {
      case Success(parsedURI) if parsedURI.db.isDefined =>
        parsedURI
      case Success(_) =>
        //TODO guice ConfigurationException?
        throw new RuntimeException(s"Missing database name in mongodb.uri '$uri'")
      case Failure(e) => throw new RuntimeException(s"Invalid mongodb.uri '$uri'", e)
    }
  }
}
