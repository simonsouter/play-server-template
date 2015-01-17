package net.cakesolutions.playserver.play

import com.google.inject.Guice
import net.cakesolutions.playserver.guice.PlayModule
import play.api._
import play.api.libs.json.Json
import play.api.mvc.Results._
import play.api.mvc._

import scala.concurrent.Future

object Global extends GlobalSettings {

  /**
   * Bind types such that whenever TextGenerator is required, an instance of WelcomeTextGenerator will be used.
   */
  val injector = Guice.createInjector(new PlayModule())

  override def onStart(app: Application) {
    Logger.info("Application has started")
  }

  override def onStop(app: Application) {
    Logger.info("Application shutdown...")
  }

  override def onHandlerNotFound(request: RequestHeader) = {
    Future.successful(NotFound(Json.obj("status" -> "Invalid path specified")))
  }

  override def onBadRequest(request: RequestHeader, error: String) = {
    Future.successful(BadRequest(Json.obj("status" -> s"Failed to parse request: ${error}")))
  }

  override def getControllerInstance[A](controllerClass: Class[A]): A = injector.getInstance(controllerClass)
}
