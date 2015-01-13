package net.cakesolutions.playserver.domain.place

import net.cakesolutions.playserver.domain.JsonFormats._
import play.api.Logger
import play.api.libs.json.Json
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.api.MongoConnection
import reactivemongo.bson.BSONObjectID
import reactivemongo.core.errors.GenericDatabaseException
import reactivemongo.extensions.json.dao.JsonDao

import scala.concurrent.{Future, ExecutionContext}

class PlaceRepository(connection: MongoConnection)(implicit val executor: ExecutionContext)
  extends JsonDao[Place, BSONObjectID](connection.db("play"), "place") {

  val logger: Logger = Logger(this.getClass)

  def findByName(name: String): Future[Option[Place]] = {
    findOne(Json.obj("name" -> name))
  }

  //  def collection: JSONCollection = connection.db("play").collection[JSONCollection]("place")

  //  def save(place: Place) = {
  //    logger.debug(s"Saving place with name+[$place.name]")
  //    collection.save(place).flatMap {
  //      case ok if ok.ok =>
  //        Future.successful(place)
  //      case error =>
  //        error
  //        Future.failed(GenericDatabaseException("Fail", Some(1)))
  //    }
  //  }
}
