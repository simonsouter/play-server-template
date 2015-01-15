package net.cakesolutions.playserver.domain.place

import net.cakesolutions.playserver.scalatest.UnitSpec
import net.cakesolutions.playserver.domain.place.Place._
import play.api.Logger
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import reactivemongo.bson.BSONObjectID

/**
 * Tests Place Domain model
 */
class PlaceSpec extends UnitSpec {
  val logger: Logger = Logger(this.getClass)
  val locationJson = """{"lat" : 51.235685,"long" : -1.309197}"""

  val residentJson = """{"name" : "Fiver","age" : 4}"""

  val placeJson = s"""{"_id":"54b66e514c0a73530095cfdd","name" : "Watership Down","location" : $locationJson,"residents" : [ $residentJson, $residentJson ]}"""

  "A location entity" should "read" in {
    val ast: JsValue = Json.parse(locationJson)
    ast.validate[Location] match {
      case s: JsSuccess[Location] =>
        val location = s.get
        assert(location.lat == 51.235685)
        assert(location.long == -1.309197)
      case e: JsError =>
        fail("Failed to parse: " + JsError.toFlatForm(e))
    }
  }

  it should " write" in {
    val ast: JsValue = Json.toJson(Location(1.1, 2.2))

    //TODO assertions
    println(Json.stringify(ast))
  }

  "A resident entity" should "read" in {
    val ast: JsValue = Json.parse(residentJson)
    ast.validate[Resident] match {
      case s: JsSuccess[Resident] =>
        val resident = s.get
        assert(resident.name equals "Fiver")
        assert(resident.age equals 4)
      case e: JsError =>
        fail("Failed to parse: " + JsError.toFlatForm(e))
    }
  }

  "A place entity" should "Parse and Read a Place Json String" in {
    val ast: JsValue = Json.parse(placeJson)
    ast.validate[Place] match {
      case s: JsSuccess[Place] =>
        val place = s.get
        assert(place._id.get equals BSONObjectID("54b66e514c0a73530095cfdd"))
        assert(place.name equals "Watership Down")
        assert(place.location.lat == 51.235685)
        assert(place.location.long == -1.309197)
        assert(place.residents.length == 2)

        //Write Place and compare to original AST
        val parsedAst = Json.toJson(place)
//        assert(parsedAst equals ast)

      case e: JsError =>
        fail("Failed to parse: " + JsError.toFlatForm(e))
    }
  }
}
