package Dao

import models.{MatchData, Teams, TeamsWin}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.mvc.{AbstractController, ControllerComponents}
import slick.jdbc.JdbcProfile
import slick.lifted.ProvenShape

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class TeamsWinDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider, cc: ControllerComponents)(
  implicit ec: ExecutionContext) extends AbstractController(cc) with HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  class TeamsWinTable(tag: Tag) extends Table[TeamsWin](tag, "Teams_Win") {
    def id: Rep[Option[Int]] = column[Int]("id", O.PrimaryKey,O.AutoInc)
    def teamname: Rep[String] = column[String]("Team")
    def wins:Rep[Int]=column[Int]("Wins")

    override def * : ProvenShape[TeamsWin] = (id,teamname,wins) <> ((TeamsWin.apply _).tupled, TeamsWin.unapply)
  }


  private val teamsTableQuery =TableQuery[TeamsWinTable]


  def insert(teamswin: TeamsWin): Future[Unit] =
    db.run(DBIO.seq(teamsTableQuery.schema.createIfNotExists, teamsTableQuery += teamswin))

  def getmatch(team: String): Future[Seq[TeamsWin]] =
    db.run(teamsTableQuery.filter(_.teamname === team).result)

  def fetchAll(): Future[Seq[TeamsWin]] = db.run(teamsTableQuery.result)


}