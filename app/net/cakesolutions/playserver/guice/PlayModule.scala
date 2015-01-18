package net.cakesolutions.playserver.guice

import javax.inject.Singleton

import com.google.inject.{AbstractModule, Provides}
import com.typesafe.config.{Config, ConfigFactory}
import net.cakesolutions.playserver.mongo.MongoConnector
import net.codingwell.scalaguice.ScalaModule
import play.api.Logger
import reactivemongo.api.DB

/**
 * Created by user on 16/01/15.
 */
class PlayModule extends AbstractModule with ScalaModule {
  val logger: Logger = Logger(this.getClass)

  override def configure(): Unit = {
//    bind[Service].to[ServiceImpl].in[Singleton]
  }

  @Provides
  @Singleton
  def provideConfig(): Config = {
    logger.info("Loading Typesafe config")

    ConfigFactory.load("application.conf")
  }

//  @Provides
//  @Singleton
//  def mongoConnection(config: Config): MongoConnection = {
//    val mongoConfig = parseConf(config)
//
//    //TODO creates an actor system (should provide play one?)
//    val connection = new MongoDriver().connection(mongoConfig)
//    connection
//  }

  @Provides
  @Singleton
  def providesDB(config: Config): DB = {
    MongoConnector.db(config)
  }
}
