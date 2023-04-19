package modules.actor

import Dao.UserDao
import akka.actor.{Actor, ActorLogging}
import modules.actor.UserCountActor.GetCurrentUserCount
import play.api.libs.concurrent.InjectedActorSupport

import javax.inject.Inject
import scala.concurrent.ExecutionContext

object UserCountActor {
  object GetCurrentUserCount
}

class UserCountActor @Inject()(val userDataDao: UserDao)(implicit ec: ExecutionContext) extends Actor with ActorLogging with InjectedActorSupport {
  override def receive: Receive = {
    case GetCurrentUserCount =>
      val s = sender()
      userDataDao.countTotalUsers().map(x => s ! x)
  }
}