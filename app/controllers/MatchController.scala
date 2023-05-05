package controllers

import Dao.MatchDataDao
import akka.actor.ActorRef
import akka.stream.Materializer
import akka.util.Timeout

import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}

import javax.inject.{Inject, Named}
import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

class MatchController @Inject()(val matchDataDao: MatchDataDao,
                                val controllerComponents: ControllerComponents)
                               (implicit mat: Materializer, ec: ExecutionContext)
  extends BaseController {

  implicit val timeout: Timeout = Timeout(10 seconds)


  def getmatch(id: Int): Action[AnyContent] = Action.async { _ =>
    matchDataDao.getmatch(id).map(x => Ok(Json.toJson(x)))
  }

  def getAllmatches: Action[AnyContent] = Action.async { _ =>
    matchDataDao.fetchAll().map(x => Ok(Json.toJson(x)))
  }

  def getmatchesbyteam(team:String): Action[AnyContent] = Action.async { _ =>
    matchDataDao.getmatchesbyteam(team).map(x => Ok(Json.toJson(x)))
  }

}
