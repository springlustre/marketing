package com.neo.sk.feeler3.ptcl

import com.neo.sk.feeler3.ptcl.ShopProtocol.ShopModel.{BoxInfo, ShopDetail, ShopInfo, UserShopList}

/**
  * Created by hongruying on 2017/3/24.
  */
object ShopProtocol {
  object ShopModel{
    case class ShopInfo(
                       id:Long,
                       shopName:String,
                       createTime:Long,
                       groupId:Long
                       )

    case class UserShopList(
                             merchantShop:Seq[ShopInfo],
                             observedShop:Seq[ShopInfo]
                           )

    case class BoxInfo(
                        id:Long,
                        boxName:String,
                        boxMac:String,
                        createTime:Long,
                        shopId:Long,
                        rssi:Long
                      )

    case class Observer(
                       id:Long,
                       openId:String,
                       nickname:String,
                       headImg:String
                       )

    case class ShopDetail(
                         id:Long,
                         shopName:String,
                         createTime:Long,
                         groupId:Long,
                         boxs:Seq[BoxInfo],
                         observers:Seq[Observer]
                         )

  }

  sealed trait Response{
    val errCode: Int
    val msg: String
  }

  case class CommonRsp(errCode: Int,msg: String) extends Response

  case class CreateShopRsp(
                          data:Option[ShopInfo],
                          errCode: Int,
                          msg: String
                          ) extends Response


  case class GetShopListRsp(
                             data: Option[UserShopList],
                             errCode: Int,
                             msg: String
                           ) extends Response

  case class GetShopDetailRsp(
                             data: Option[ShopDetail],
                             errCode: Int,
                             msg: String
                             ) extends Response

  case class CheckShareCodeRsp(
                                data: Option[ShopInfo],
                                errCode: Int,
                                msg: String
                              ) extends Response


  case class GetShareCodeRsp(
                            data:Option[String],
                            errCode: Int,
                            msg: String
                            ) extends Response

  case class CreateBoxRsp(
                           data:Option[BoxInfo],
                           errCode: Int,
                           msg: String
                          ) extends Response





}
