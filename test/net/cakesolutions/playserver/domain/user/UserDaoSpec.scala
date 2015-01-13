package net.cakesolutions.playserver.domain.user

import net.cakesolutions.playserver.scalatest.UnitSpec
import org.scalatest.concurrent.ScalaFutures
import play.api.Logger
import reactivemongo.api.MongoDriver

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
 *
 */
class UserDaoSpec extends UnitSpec with ScalaFutures {

  val logger: Logger = Logger(this.getClass)

  val driver = new MongoDriver

  //  val config = app.configuration

  //  val connection = driver.connection(config.getStringSeq("mongodb.servers").getOrElse(Seq("localhost:27017")))
  val connection = driver.connection(Seq("localhost:27017"))

  "UserDao" should "Create new User Document" in {
    val userDao: UserDao = new UserDao(connection)
    val future = userDao.save(UserModel(22, "Test"))

    future.onComplete {
      case Success(userModel) =>
        assert(userModel.name == "Test")

      case Failure(t) =>
        fail(t)
    }

    userDao.findByName("Test").map(userModels =>
      userModels.map(userModel =>
        assert(userModel.name == "Test")
      )
    )
  }

  it should "Find and Update Document" in {
    logger.warn("!!!!!!!!")
    val userDao: UserDao = new UserDao(connection)

    val futureUserModel: Future[UserModel] = for {
      userList <- userDao.findByName("Test")
      updatedUserModel = userList.head.copy(name="Changed")
      futureUserModel <- userDao.save(updatedUserModel)
    } yield {
      futureUserModel
    }

    logger.warn("COMPLETED1")
    whenReady(futureUserModel) { userModel =>
      logger.warn("COMPLETED2")
      assert(userModel.name.equals("Changed"))
      assert(userModel.age.equals(22))
    }
  }
}
