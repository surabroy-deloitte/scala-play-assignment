package controllers

import Dao.{MatchDataDao, TeamsWinDao}
import akka.actor.ActorRef
import akka.stream.Materializer
import akka.util.Timeout
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}

import javax.inject.{Inject, Named}
import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

class TimesWinController @Inject()(val teamsWinDao: TeamsWinDao,
                         val controllerComponents: ControllerComponents)
                        (implicit mat: Materializer, ec: ExecutionContext)
  extends BaseController {

  implicit val timeout: Timeout = Timeout(10 seconds)


  def getwin(team: String): Action[AnyContent] = Action.async { _ =>
    teamsWinDao.getmatch(team).map(x => Ok(Json.toJson(x)))
  }

  def getAllWins: Action[AnyContent] = Action.async { _ =>
    teamsWinDao.fetchAll().map(x => Ok(Json.toJson(x)))
  }


}