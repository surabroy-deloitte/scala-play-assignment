package controllers

import Dao.UserDao
import akka.actor.ActorRef
import akka.pattern.ask
import akka.stream.Materializer
import akka.util.Timeout
import models.UserData
import modules.actor.UserCountActor.GetCurrentUserCount
import play.api.libs.json.Json
import play.api.mvc._

import javax.inject._
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.language.postfixOps

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(val userDataDao: UserDao,
                               val controllerComponents: ControllerComponents,
                               @Named("userCountActor") userCountActor: ActorRef)
                              (implicit mat: Materializer, ec: ExecutionContext)
  extends BaseController {

  implicit val timeout: Timeout = Timeout(10 seconds)

  def index(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def addUser(): Action[AnyContent] = Action.async { request =>
    val payload = request.body.asJson.get
    val userData = payload.as[UserData]

    userDataDao.insert(userData).map { _ => Ok("User inserted successfully!")}
      .recover{
        case e => InternalServerError("Something went wrong")
      }
  }

  def getUser(id: Int): Action[AnyContent] = Action.async { _ =>
    userDataDao.getUser(id).map(x => Ok(Json.toJson(x)))
  }

  def getAllUsers: Action[AnyContent] = Action.async { _ =>
    userDataDao.fetchAll().map(x => Ok(Json.toJson(x)))
  }

  def updateUser(): Action[AnyContent] = Action.async { request =>
    val payload = request.body.asJson.get
    val updatedUserData = payload.as[UserData]

    userDataDao.update(updatedUserData).map(_ => Ok("User updated successfully!"))
      .recover{
        case e => InternalServerError("Something went wrong")
      }
  }

  def countUsers(): Action[AnyContent] = Action.async { _ =>
    val res = (userCountActor ? GetCurrentUserCount).mapTo[Int]
    res.map { response =>
      Ok(f"Got $response users.")
    }.recover {
      case ex: Exception =>
        InternalServerError(f"Something went wrong! ${ex.getMessage}")
    }
  }

}
