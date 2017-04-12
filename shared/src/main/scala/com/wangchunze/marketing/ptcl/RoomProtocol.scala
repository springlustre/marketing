package com.wangchunze.marketing.ptcl


import com.wangchunze.marketing.ptcl.HotelProtocol.RestResponse
import io.circe.generic.auto._
import io.circe._

/**
  * Created by springlustre on 2017/4/11.
  */
object RoomProtocol {

  case class PriceInfo(
    date:String,
    price:Double
  )

  case class PackageInfo(
    id:Long,
    hotelId:Long,
    roomId:Long,
    title:String,
    img:String,
    stock:Int,
    description:String,
    price:Double,
    priceInfo:Seq[PriceInfo]
  )

  case class RoomInfo(
    id:Long,
    hotelId:Long,
    name:String,
    logo:String,
    img:Seq[String],
    description:JsonObject,
    packageInfo: Seq[PackageInfo]
  )

  case class Desc(
    key:String,
    value:String
  )

  case class RoomListRsp(
    data:Seq[RoomInfo],
    msg: String = "ok",
    errCode: Int = 0
  )




}
