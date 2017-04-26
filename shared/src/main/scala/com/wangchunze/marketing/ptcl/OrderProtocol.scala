package com.wangchunze.marketing.ptcl

import com.wangchunze.marketing.ptcl.RoomProtocol.{Room, RoomPackage}

/**
  * Created by springlustre on 2017/4/12.
  */

object OrderProtocol {

  case class OrderRst(
    hotelId:Long,
    roomId:Long,
    packageId:Long,
    dateStart:String,
    dateEnd:String,
    comeTime:String,
    totalPrice:Double,
    priceInfo:String,
    userName:String,
    userMobile:String,
    userIdentifyNum:String,
    invoice:String,
    remark:String,
    roomNum:Int,
    stayDays:Int
  )

  case class OrderInfo(
    id:Long,
    hotelId:Long,
    roomId:Long,
    packageId:Long,
    dateStart:String,
    dateEnd:String,
    comeTime:String,
    totalPrice:Double,
    priceInfo:String,
    userName:String,
    userMobile:String,
    userIdentifyNum:String,
    invoice:String,
    remark:String,
    createTime:Long,
    roomInfo:Room,
    packageInfo:RoomPackage,
    roomNum:Int,
    stayDays:Int,
    state:String
  )


  case class OrderCreateSuc(
    data:OrderInfo,
    msg: String = "ok",
    errCode: Int = 0
  )

  case class OrderBrief(
    id:Long,
    roomId:Long,
    dateStart:String,
    dateEnd:String,
    totalPrice:Double,
    remark:String,
    createTime:String,
    roomInfo:Room,
    packageInfo:RoomPackage,
    roomNum:Int,
    stayDays:Int,
    state:String,
    userName:String = "",
    userMobile:String = ""
  )

  case class ListOrderBrief(
    data:Seq[OrderBrief],
    msg: String = "ok",
    errCode: Int = 0
  )

  case class OrderDetailRsp(
    data:OrderInfo,
    msg: String = "ok",
    errCode: Int = 0
  )


}
