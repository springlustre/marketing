package com.wangchunze.marketing.ptcl

import io.circe.{Json, JsonObject}
import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._
/**
  * Created by springlustre on 2017/4/10.
  */

object HotelProtocol {
  sealed trait RestResponse{
    val errCode: Int
    val msg: String
  }

  case class SuccessResponse(
    msg: String = "ok",
    errCode: Int = 0
  )

  case class ErrorResponse(errCode: Int, msg: String) extends RestResponse

  case class RspData()

  case class HotelInfo(
    id:Long,
    name:String,
    logo:String,
    img:String,
    address:String,
    longitude:Double,
    latitude:Double,
    phone:String,
    state:Int,
    star:Int,
    openTime:String,
    mark:Double,
    commentNum:Int,
    description:String
  )

  case class HotelInfoRsp(
    data:HotelInfo,
    msg: String = "ok",
    errCode: Int = 0
  ) extends RestResponse


  case class CreateHotelRst(
    name:String,
    logo:String,
    img:String,
    address:String,
    longitude:Double,
    latitude:Double,
    phone:String,
    star:Int,
    openTime:String,
    description:String
  )

  case class ModifyHotelRst(
    id:Long,
    name:String,
    logo:String,
    img:String,
    address:String,
    longitude:Double,
    latitude:Double,
    phone:String,
    star:Int,
    openTime:String, //2017-04-20
    description:String
  )


  case class PicRsp(
    data:String,
    msg: String = "ok",
    errCode: Int = 0
  )extends RestResponse


}
