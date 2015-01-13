package net.cakesolutions.playserver.domain.place

import net.cakesolutions.playserver.scalatest.UnitSpec
import org.scalatest.concurrent.ScalaFutures
import play.api.Logger
import reactivemongo.api.MongoDriver

import scala.concurrent.ExecutionContext.Implicits.global

class PlaceRepositorySpec extends UnitSpec with ScalaFutures {
  val logger: Logger = Logger(this.getClass)
  val driver = new MongoDriver
  val connection = driver.connection(Seq("localhost:27017"))
  val placeRepository = new PlaceRepository(connection)

  val testPlace = Place(name="Test", location=Location(0.2, 0.1),
    residents=Seq(Resident("Res", 21, Some("Admin")), Resident("Res", 21, Some("Admin"))))

  "PlaceRepository" should "Create new Place Document" in {
    val future = placeRepository.save(testPlace)

    whenReady(future) { lastError =>
      if (!lastError.ok) {
        fail("Failed to save Place", lastError.getCause)
      }
    }
  }

  it should "Find and Update Place" in {

    val future = for {
      place <- placeRepository.findByName("Test")
      updatedPlace = place.get.copy(name = "New")
      future <- placeRepository.save(updatedPlace)
      newPlace <- placeRepository.findByName("New")
    } yield {
      newPlace
    }

    whenReady(future) {
      _ match {
        case Some(place) =>
          assert(place.name equals "New")
        case None =>
          fail("Failed to save Place")
      }
    }
  }
}