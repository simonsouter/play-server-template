package net.cakesolutions.playserver.domain.place

import com.typesafe.config.ConfigFactory
import net.cakesolutions.playserver.mongo.MongoConnector
import net.cakesolutions.playserver.scalatest.UnitSpec
import org.scalatest.concurrent.ScalaFutures
import play.api.Logger
import reactivemongo.api.DB

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.FiniteDuration

class PlaceRepositorySpec extends UnitSpec with ScalaFutures {
  val logger: Logger = Logger(this.getClass)
  val config = ConfigFactory.load("application.conf")
  val db:DB = MongoConnector.db(config)
  val placeRepository: PlaceRepository = new PlaceRepository(db)

  val testPlace = Place(name = "Test", location = Location(0.2, 0.1),
    residents = Seq(Resident("Res", 21, Some("Admin")), Resident("Res", 21, Some("Admin"))))

  "PlaceRepository" should "Create Place Document" in {
    val a = Await.result(placeRepository.save(testPlace), FiniteDuration(5, "seconds"))
    if (!a.ok) {
      fail("Failed to save Place", a.getCause)
    }
  }

//  it should "Find and Update Place" in {
//
//    val future = for {
//      place <- placeRepository.findByName("Test")
//      updatedPlace = place.get.copy(name = "New")
//      future <- placeRepository.save(updatedPlace)
//      newPlace <- placeRepository.findByName("New")
//    } yield {
//      newPlace
//    }
//
//    whenReady(future) {
//      _ match {
//        case Some(place) =>
//          assert(place.name equals "New")
//        case None =>
//          fail("Failed to save Place")
//      }
//    }
//  }

  it should "Find Place Document by id and delete" in {
    val maybePlace = Await.result(placeRepository.findById(testPlace._id.get), FiniteDuration(5, "seconds"))
    maybePlace match {
      case Some(place) =>
        assert(place.name equals "Test")

        val lastError = Await.result(placeRepository.removeById(place._id.get), FiniteDuration(5, "seconds"))
        assert(lastError.ok)
      case None =>
        fail("Failed to find Place")
    }
  }
}