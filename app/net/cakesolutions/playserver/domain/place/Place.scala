package net.cakesolutions.playserver.domain.place

import play.api.libs.json.Json

/**
 * Created by user on 13/01/15.
 */

case class Location(lat: Double, long: Double)

case class Resident(name: String, age: Int, role: Option[String])

case class Place(name: String, location: Location, residents: Seq[Resident])

object Place {
  implicit val locationFormat = Json.format[Location]
  implicit val residentFormat = Json.format[Resident]
  implicit val placeFormat = Json.format[Place]
}