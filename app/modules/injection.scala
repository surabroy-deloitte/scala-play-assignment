package modules

import com.google.inject.AbstractModule
import modules.actor.UserCountActor
import play.api.libs.concurrent.AkkaGuiceSupport

class injection extends AbstractModule with AkkaGuiceSupport {

  override def configure(): Unit = {
    bindActor[UserCountActor]("userCountActor")
  }
}
