package models

import play.api.libs.json.{Json, OFormat}

case class TeamsWin(id:Option[Int],team:String,wins:Int)

object TeamsWin
{
  implicit val formatter: OFormat[TeamsWin] = Json.format[TeamsWin]
}