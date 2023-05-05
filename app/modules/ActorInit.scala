package modules

import Dao.{MatchDataDao, TeamsDao, TeamsWinDao, VenueDao}
import akka.actor.{Actor, ActorSystem, PoisonPill, Props, Terminated}
import models.{MatchData, Teams, TeamsWin, Venues}
import modules.actor.ReaderActor

import javax.inject.{Inject, Singleton}
import scala.collection.mutable
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.io.Source

@Singleton
class ActorInit @Inject()(matchDataDao: MatchDataDao,teamsDao: TeamsDao,venueDao: VenueDao,teamsWinDao: TeamsWinDao) {

  def populatetables(matchDataDao: MatchDataDao,teamsWinDao: TeamsWinDao,teamsDao: TeamsDao): Unit ={

    val Matchlistfuture = matchDataDao.fetchAll()
    val teams = collection.mutable.Set[String]()
    val winners=collection.mutable.ListBuffer[String]()
    val matchList: Seq[MatchData] = Await.result(Matchlistfuture, Duration.Inf)
    val teamf=teamsDao.fetchAll()
    val teamList: Seq[Teams] = Await.result(teamf, Duration.Inf)
    for(m<-matchList)
      {
        winners += m.winner
      }

      for(t<-teamList) {
        var count=0;
        {
          for(w<-winners)
            {
              if(w.equalsIgnoreCase(t.teams)) {
                count+=1
              }
            }
            teamsWinDao.insert(new TeamsWin(None,t.teams,count))
        }
      }
  }
  val source = Source.fromFile("/home/surabroy/Downloads/IPL.csv")
  val lines = source.getLines()
  val linewithoutheader=lines.drop(1)

  val system = ActorSystem("FileReaderSystem")
  val fileReaderActor = system.actorOf(Props(new ReaderActor(matchDataDao,teamsDao, venueDao)), "fileReaderActor")

  system.actorOf(Props(new Actor {
    context.watch(fileReaderActor)

    def receive = {
      case Terminated(`fileReaderActor`) =>

        populatetables(matchDataDao,teamsWinDao,teamsDao)
    }
  }))

  var bool=false
  while (linewithoutheader.hasNext){
    val line =lines.next()
    val record = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)(?<![ ])(?![ ])")
    fileReaderActor ! record
  }

if(!lines.hasNext)
  {
    fileReaderActor!PoisonPill
  }






}
