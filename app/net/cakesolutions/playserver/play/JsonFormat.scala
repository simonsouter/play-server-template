package net.cakesolutions.playserver.play

import play.api.Logger
import play.api.libs.json._
import play.modules.reactivemongo.json.BSONFormats.PartialFormat
import reactivemongo.bson.{BSONValue, BSONObjectID}

import scala.util.{Failure, Success}

/**
 * Created by user on 15/01/15.
 */
object JsonFormat {
  val logger: Logger = Logger(this.getClass)

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
