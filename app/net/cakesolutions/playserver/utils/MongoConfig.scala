package net.cakesolutions.playserver.utils

import com.typesafe.config.ConfigFactory
import play.api.Logger
import reactivemongo.api.MongoDriver

import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Provides configuration for MongoDb connection, should only be mixed-in once as pool management is done here.
 */
trait MongoConfig {

  val logger: Logger = Logger(this.getClass)

  // gets an instance of the driver
  // (creates an actor system)
  def driver = new MongoDriver

  val regularConfig = ConfigFactory.load()

  val urls = regularConfig.getString("mongodb.urls")
  val dbName = regularConfig.getString("mongodb.name")

  /* Gets a connection using this MongoDriver instance.
   * The connection creates a pool of connections.
   * *** USE WITH CAUTION ***
   * Both ReactiveMongo class and MongoConnection class should be instantiated one time in your application
   * (each time you make a ReactiveMongo instance, you create an actor system; each time you create a MongoConnection
   * instance, you create a pool of connections).
   */
  val connection = driver.connection(urls.split(","))


  // Gets a reference to the database "{mongodb.name}"
  val db = {
    logger.info(s"Establishing MongoDB connection to ${dbName}, ${urls}")
    println(s"Establishing MongoDB connection to ${dbName}, ${urls}")
    connection(dbName)
  }
}

/**
 * Singleton for accessing mongo connection.
 */
object MongoContext extends MongoConfig