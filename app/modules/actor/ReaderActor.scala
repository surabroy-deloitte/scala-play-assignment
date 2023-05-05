package modules.actor

import Dao.{MatchDataDao, TeamsDao, VenueDao}
import akka.actor.Actor.Receive
import akka.actor.TypedActor.context
import akka.actor.{Actor, ActorRef, Props, Terminated}
import akka.routing.RoundRobinPool
import models.{MatchData, Teams, Venues}
import modules.actor.ChildActor.ChildActor
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.mvc.DefaultControllerComponents
import play.mvc.Controller
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

import javax.inject.Inject
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext}




  class ReaderActor @Inject()(val matchDataDao: MatchDataDao,val teamsDao: TeamsDao,val venueDao: VenueDao)extends Actor {



    val venues = collection.mutable.Set[String]()
    val teams = collection.mutable.Set[String]()

    implicit val ec: ExecutionContext = context.system.dispatcher

    val workerRouter: ActorRef = context.actorOf(RoundRobinPool(10).props(Props(new ChildActor(matchDataDao,venueDao,teamsDao,venues,teams))), "workerRouter")


    override def receive: Receive = {
      case record: Array[String] => {
        workerRouter ! record


//        val Matchlistfuture = matchDataDao.fetchAll()
//        val matchlist: Seq[MatchData] = Await.result(Matchlistfuture, Duration.Inf)
      }


      case Terminated(child) =>
        println(s"Child actor ${child.path.name} terminated")

    }
}

