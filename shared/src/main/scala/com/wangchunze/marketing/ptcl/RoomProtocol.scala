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

  case class Room(
    id:Long,
    hotelId:Long,
    name:String,
    logo:String,
    description:JsonObject
  )
  case class RoomPackage(
    id:Long,
    hotelId:Long,
    roomId:Long,
    title:String,
    img:String,
    description:JsonObject
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


  /***** for business ***/
  case class CreateRoomRst(
    hotelId:Long,
    name:String,
    logo:String,
    img:String,
    stock:Int,
    description:String
  )

  case class ModifyRoomRst(
    roomId:Long,
    name:String,
    logo:String,
    img:String,
    stock:Int,
    description:String
  )

  case class RoomPrice(
    roomId:Long,
    packageId:Long,
    price:Double
  )

  case class SetPrice(
    hotelId:Long,
    roomList:List[RoomPrice],
    startTime:String,
    endTime:String
  )


}
