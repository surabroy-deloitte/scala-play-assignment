package models

import play.api.libs.json.{Json, OFormat}

import java.time.LocalDate
case class MatchData(id:Int,city:String,date:LocalDate,ManOfMatch:String,venue:String,team1:String,team2:String,toss_Winner:String,toss_descion:String,winner:String,merged_result:String,eliminator:String,umpire1:String,umpire2:String)

  object MatchData {
    implicit val formatter: OFormat[MatchData] = Json.format[MatchData]
}
