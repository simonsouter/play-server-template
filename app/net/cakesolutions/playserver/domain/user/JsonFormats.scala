package net.cakesolutions.playserver.domain.user

import play.api.libs.json._
import reactivemongo.bson.BSONObjectID

import scala.util.Try

/**
 * Created by simonsouter on 01/11/14.
 */
object JsonFormats {
  import play.api.libs.json.Json

  // Generates Writes and Reads for User thanks to Json Macros
  implicit val userFormat = Json.format [UserModel]

  implicit object BSONObjectIDFormat extends Format[BSONObjectID] {
    def writes(objectId: BSONObjectID): JsValue = JsString(objectId.toString())

    def reads(json: JsValue): JsResult[BSONObjectID] = json match {
      case JsString(x) => {
        val maybeOID: Try[BSONObjectID] = BSONObjectID.parse(x)
        if(maybeOID.isSuccess) JsSuccess(maybeOID.get) else {
          JsError("Expected BSONObjectID as JsString")
        }
      }
      case _ => JsError("Expected BSONObjectID as JsString")
    }
  }
}
