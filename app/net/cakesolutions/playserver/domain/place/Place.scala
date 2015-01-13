package net.cakesolutions.playserver.domain.place

import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID
import net.cakesolutions.playserver.domain.JsonFormats._

/**
 * Created by user on 13/01/15.
 */
case class Place(_id: BSONObjectID = BSONObjectID.generate,
                 name: String, location: Location, residents: Seq[Resident])

case class Location(lat: Double, long: Double)

case class Resident(name: String, age: Int, role: Option[String])


object Place {
  implicit val locationFormat = Json.format[Location]
  implicit val residentFormat = Json.format[Resident]
  implicit val placeFormat = Json.format[Place]
}