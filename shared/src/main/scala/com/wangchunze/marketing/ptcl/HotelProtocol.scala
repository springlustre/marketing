package com.wangchunze.marketing.ptcl

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
    openTime:Long,
    mark:Double,
    commentNum:Int,
    description:String
  )

  case class HotelInfoRsp(
    data:HotelInfo,
    msg: String = "ok",
    errCode: Int = 0
  )





}
