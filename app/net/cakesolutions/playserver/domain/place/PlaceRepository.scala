package net.cakesolutions.playserver.domain.place

import javax.inject.Singleton

import play.api.Logger
import play.api.libs.json.Json
import play.modules.reactivemongo.ReactiveMongoPlugin
import reactivemongo.bson.BSONObjectID
import reactivemongo.extensions.json.dao.JsonDao
import net.cakesolutions.playserver.play.JsonFormat._

//TODO pass implicitly
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.Play.current
import scala.concurrent.Future

@Singleton
class PlaceRepository extends JsonDao[Place, BSONObjectID](ReactiveMongoPlugin.db, "place") {

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
