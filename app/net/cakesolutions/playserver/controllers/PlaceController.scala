package net.cakesolutions.playserver.controllers

import net.cakesolutions.playserver.domain.place.{Place, PlaceRepository}
import play.api.Logger
import play.api.libs.json.{JsError, Json}
import play.api.mvc._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object PlaceController extends Controller {

  val logger: Logger = Logger(this.getClass)

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
}