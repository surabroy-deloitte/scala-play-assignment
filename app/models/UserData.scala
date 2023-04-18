package models

import play.api.libs.json.{Json, OFormat}

import scala.language.implicitConversions

case class UserData(id: Int, name: String, email: String)

object UserData {
  implicit val formatter: OFormat[UserData] = Json.format[UserData]
}