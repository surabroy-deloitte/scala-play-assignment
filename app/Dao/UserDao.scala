package Dao

import models.UserData
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.mvc.{AbstractController, ControllerComponents}
import slick.jdbc.JdbcProfile
import slick.lifted.ProvenShape

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class UserDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider, cc: ControllerComponents)(
  implicit ec: ExecutionContext) extends AbstractController(cc) with HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  class UserTable(tag: Tag) extends Table[UserData](tag, "user_data") {
    def id: Rep[Int] = column[Int]("id", O.PrimaryKey)
    def name: Rep[String] = column[String]("name")
    def email: Rep[String] = column[String]("email")
    override def * : ProvenShape[UserData] = (id, name, email) <> ((UserData.apply _).tupled, UserData.unapply)
  }

  private val userTableQuery =TableQuery[UserTable]


  def insert(userData: UserData): Future[Unit] =
    db.run(DBIO.seq(userTableQuery.schema.createIfNotExists, userTableQuery += userData))

  def fetchAll(): Future[Seq[UserData]] = db.run(userTableQuery.result)

  def update(newUserData: UserData): Future[Int] =
    db.run(userTableQuery.filter(_.id === newUserData.id).update(newUserData))

  def getUser(id: Int): Future[Seq[UserData]] =
    db.run(userTableQuery.filter(_.id === id).result)

  def countTotalUsers(): Future[Int] = db.run(userTableQuery.length.result)

}
