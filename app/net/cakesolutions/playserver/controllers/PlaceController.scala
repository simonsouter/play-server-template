package net.cakesolutions.playserver.controllers

import net.cakesolutions.playserver.domain.place.{Place, PlaceRepository}
import play.api.Logger
import play.api.libs.json.{JsError, JsValue, Json}
import play.api.mvc._
import reactivemongo.bson.BSONObjectID

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object PlaceController extends Controller {

  val logger: Logger = Logger(this.getClass)

  def findById(id: String) = Action.async {
    val future = PlaceRepository.findById(BSONObjectID(id))

    future.map(
      _ match {
        case Some(place) =>
          Ok(Json.toJson(place))
        case None =>
          NoContent
      })
  }

  def create() = Action.async(BodyParsers.parse.json) { request =>
    logger.debug("Create Place action")

    val modelResult = request.body.validate[Place]
    modelResult.fold(
      errors => {
        Future {
          logger.warn("Error parsing request:" + JsError.toFlatJson(errors))
          BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toFlatJson(errors)))
        }
      },
      place => {
        PlaceRepository.save(place).map(lastError =>
          Created(Json.obj("status" -> "OK", "message" -> s"Place [${place.name}] saved")))
      }
    )
  }

  def update() = Action.async(BodyParsers.parse.json) { request =>
    logger.debug("Update Place action")

    if (!validIdPresent(request.body))
      Future {
        BadRequest(Json.obj("status" -> "KO", "message" -> "Failed to parse ObjectId"))
      }
    else {
      val modelResult = request.body.validate[Place]
      modelResult.fold(
        errors => {
          Future {
            logger.warn("Error parsing request:" + JsError.toFlatJson(errors))
            BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toFlatJson(errors)))
          }
        },
        place => {
          PlaceRepository.save(place).map(lastError =>
            Created(Json.obj("status" -> "OK", "message" -> s"Place [${place.name}] saved")))
        }
      )
    }
  }

  def delete(id: String) = Action.async {
    val future = PlaceRepository.removeById(BSONObjectID(id))

    future.map(
      _ match {
        case lastError if lastError.ok =>
          Ok(Json.obj("status" -> s"Deleted place with ID: [${id}]"))
        case _ =>
          NotFound
      })
  }

  private def validIdPresent(json: JsValue): Boolean = {
    try {
      val oid = (json \ "_id" \ "$oid").as[String]
      BSONObjectID(oid)
      true
    } catch {
      case e: Exception =>
        logger.error(s"Failed to Parse objectId: ${e.getMessage}")
        false;
    }

  }
}