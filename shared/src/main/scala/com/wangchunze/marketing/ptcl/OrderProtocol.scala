package com.wangchunze.marketing.ptcl

import com.wangchunze.marketing.ptcl.RoomProtocol.RoomInfo

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
    comeTime:Long,
    totalPrice:Double,
    priceInfo:String,
    userName:String,
    userMobile:String,
    userIdentifyNum:String,
    invoice:String,
    remark:String
  )

  case class OrderInfo(
    id:Long,
    hotelId:Long,
    roomId:Long,
    packageId:Long,
    roomInfo:RoomInfo,
    dateStart:String,
    dateEnd:String,
    comeTime:Long,
    totalPrice:Double,
    priceInfo:String,
    userName:String,
    userMobile:String,
    userIdentifyNum:String,
    invoice:String,
    remark:String,
    createTime:Long
  )


  case class OrderCreateSuc(
    data:OrderInfo,
    msg: String = "ok",
    errCode: Int = 0
  )

}
