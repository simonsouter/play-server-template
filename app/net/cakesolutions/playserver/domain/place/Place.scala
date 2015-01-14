package net.cakesolutions.playserver.domain.place

import play.api.libs.json._
import reactivemongo.bson.BSONObjectID
import play.modules.reactivemongo.json.BSONFormats._

import scala.util.{Failure, Success}

/**
 * Created by user on 13/01/15.
 */
case class Place(_id: Option[BSONObjectID] = Some(BSONObjectID.generate),
                 name: String, location: Location, residents: Seq[Resident])

case class Location(lat: Double, long: Double)

case class Resident(name: String, age: Int, role: Option[String])


object Place {
  implicit val locationFormat = Json.format[Location]
  implicit val residentFormat = Json.format[Resident]
  implicit val placeFormat = Json.format[Place]

  implicit val objectIdFormatter = new Format[Option[BSONObjectID]] {
    implicit def reads(json: JsValue): JsResult[Option[BSONObjectID]] =
      json match {
        case s: JsString => BSONObjectID.parse(s.toString) match {
          case Success(v) => JsSuccess(Some(v))
          case Failure(e) => JsError("notvalid.objectid.value")
        }
        case _ => JsError("notvalid.objectid")
      }

    implicit def writes(o: Option[BSONObjectID]): JsValue =
      o match {
        case Some(b) => JsString(b.stringify)
        case _ => JsNull
      }
  }
}