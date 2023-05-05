package Dao

import models.{MatchData}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.mvc.{AbstractController, ControllerComponents}
import slick.jdbc.{JdbcProfile, JdbcType}
import slick.lifted.ProvenShape

import java.time.LocalDate
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}



class MatchDataDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider, cc: ControllerComponents)(
  implicit ec: ExecutionContext) extends AbstractController(cc) with HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  class MatchTable(tag: Tag) extends Table[MatchData](tag, "Match_details") {
    def id: Rep[Int] = column[Int]("id", O.PrimaryKey)
    def city: Rep[String] = column[String]("city")
    def date: Rep[LocalDate] = column[LocalDate]("Date")
    def MoM: Rep[String] = column[String]("ManOfMatch")
    def venue: Rep[String] = column[String]("Venue")
    def team1: Rep[String] = column[String]("Team1")
    def team2: Rep[String] = column[String]("Team2")
    def toss_win: Rep[String] = column[String]("Toss_Winner")
    def toss_descion: Rep[String] = column[String]("Toss_Descion")
    def winner: Rep[String] = column[String]("Winner")
    def merged_result: Rep[String] = column[String]("Merged_Result")
    def eliminator: Rep[String] = column[String]("Eliminator")
    def umpire1: Rep[String] = column[String]("Umpire1")
    def umpire2: Rep[String] = column[String]("Umpire2")
    override def * : ProvenShape[MatchData] = (id, city, date,MoM,venue,team1,team2,toss_win,toss_descion,winner,merged_result,eliminator,umpire1,umpire2) <> ((MatchData.apply _).tupled, MatchData.unapply)
  }

  private val matchTableQuery =TableQuery[MatchTable]


  def insert(matchData: MatchData): Future[Unit] =
    db.run(DBIO.seq(matchTableQuery.schema.createIfNotExists, matchTableQuery += matchData))

  def fetchAll(): Future[Seq[MatchData]] = db.run(matchTableQuery.result)

  def getmatch(id: Int): Future[Seq[MatchData]] =
    db.run(matchTableQuery.filter(_.id === id).result)

  def getMatchDetail(id: Int): Future[Seq[MatchData]] =
    db.run(matchTableQuery.filter(_.id === id).result)

  def getmatchesbyteam(team: String): Future[Seq[MatchData]] =
    db.run(matchTableQuery.filter(_.team1 === team).result)


}

