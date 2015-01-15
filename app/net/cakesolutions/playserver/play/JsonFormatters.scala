package net.cakesolutions.playserver.play

import play.api.Logger
import play.api.libs.json._
import reactivemongo.bson.BSONObjectID

import scala.util.{Failure, Success}

/**
 * Created by user on 15/01/15.
 */
object JsonFormatters {
  val logger: Logger = Logger(this.getClass)

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
        case Some(b) => {
          logger.info("!!!" + b.stringify)
          JsString(b.stringify)
        }
        case _ => JsNull
      }
  }
}
