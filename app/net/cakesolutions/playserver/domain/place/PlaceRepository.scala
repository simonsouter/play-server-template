package net.cakesolutions.playserver.domain.place

import javax.inject.{Inject, Singleton}

import net.cakesolutions.playserver.play.JsonFormat._
import play.api.Logger
import play.api.libs.json.Json
import reactivemongo.api.DB
import reactivemongo.bson.BSONObjectID
import reactivemongo.extensions.json.dao.JsonDao

//TODO pass implicitly
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class PlaceRepository @Inject() (db:DB) extends JsonDao[Place, BSONObjectID](db, "place") {

  val logger: Logger = Logger(this.getClass)

  def findByName(name: String): Future[Option[Place]] = {
    logger.debug(s"Find ${name}")
    findOne(Json.obj("name" -> name))
  }

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
