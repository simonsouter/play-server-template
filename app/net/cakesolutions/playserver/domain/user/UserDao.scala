package net.cakesolutions.playserver.domain.user

import play.api.Logger
import play.api.libs.json.Json
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.api.{Cursor, MongoConnection, QueryOpts}
import reactivemongo.core.errors.GenericDatabaseException

import scala.concurrent.{ExecutionContext, Future}

/**
 *
 */
class UserDao(connection: MongoConnection)(implicit val executor: ExecutionContext) {

  val logger: Logger = Logger(this.getClass)

  /**
   * Defines the default write concern for this Dao which defaults to `GetLastError()`.
   *
   * Related API functions should allow overriding this value.
   */
  //  def defaultWriteConcern: GetLastError = GetLastError()

  def collection: JSONCollection = connection.db("rapier").collection[JSONCollection]("user")

  def save(userModel: UserModel) = {
    logger.debug(s"Saving userModel with name+[$userModel.name]")
    collection.save(userModel).flatMap {
      case ok if ok.ok =>
        //saved
        Future.successful(userModel)
      case error =>
        Future.failed(GenericDatabaseException("Fail", Some(1)))
    }
  }

  /**
    * Find all the messages.
   *
   * @param page The page to retrieve, 0 based.
   * @param perPage The number of results per page.
   * @return All of the messages.
   */
  def findAll(page: Int, perPage: Int): Future[Seq[UserModel]] = {
    logger.debug(s"Finding all users")
    collection.find(Json.obj())
      .options(QueryOpts(page * perPage))
      .sort(Json.obj("_id" -> -1))
      .cursor[UserModel]
      .collect[Seq](perPage)
  }

  def findByName(name: String): Future[List[UserModel]] = {
    logger.debug(s"Find users by name=[$name]")
    val cursor: Cursor[UserModel] = collection.
      // find all people with name `name`
      find(Json.obj("name" -> name)).
      // sort them by creation date
      sort(Json.obj("created" -> -1)).
      // perform the query and get a cursor of UserModels
      cursor[UserModel]

    cursor.collect[List]()
  }


}

//TODO singleton userDao?
object UserDao {
  def apply(connection: MongoConnection)(implicit executor: ExecutionContext) = new UserDao(connection)
}
