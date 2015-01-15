package net.cakesolutions.playserver.domain.place

import play.api.libs.json._
import play.modules.reactivemongo.json.BSONFormats.PartialFormat
import reactivemongo.bson.{BSONValue, BSONObjectID}

import scala.util.{Try, Failure, Success}

case class Place(_id: Option[BSONObjectID] = Some(BSONObjectID.generate),
                 name: String, location: Location, residents: Seq[Resident])

case class Location(lat: Double, long: Double)

case class Resident(name: String, age: Int, role: Option[String])


object Place {
  implicit val locationFormat = Json.format[Location]
  implicit val residentFormat = Json.format[Resident]
  implicit val placeFormat = Json.format[Place]

  /**
   * Converts between _id Json field and BSONObjectID required by Reactive Mongo
   * TOOD move to separate class
   */
  implicit object BSONObjectIDFormat extends PartialFormat[BSONObjectID] {
    def partialReads: PartialFunction[JsValue, JsResult[BSONObjectID]] = {

      /* Id sourced from mongo via reactive mongo e.g. {"_id:{"oid":xxx} */
      case JsObject(("$oid", JsString(v)) +: Nil) => JsSuccess(BSONObjectID(v))

      /* Id sourced from API e.g. {"_id":"xxx"}*/
      case JsString(v) => JsSuccess(BSONObjectID(v))
    }
    val partialWrites: PartialFunction[BSONValue, JsValue] = {
      case oid: BSONObjectID => JsString(oid.stringify)
    }
  }
}