package models

import play.api.libs.json.{Json, OFormat}

case class Venues(id:Option[Int],venue:String)

object Venues
{
  implicit val formatter: OFormat[Venues] = Json.format[Venues]
}
