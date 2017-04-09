package com.neo.sk.feeler3.ptcl


/**
  * Created by springlustre on 2017/3/3.
  */

import com.neo.sk.feeler3.ptcl._


object AdminProtocol {

  sealed trait RestResponse{
    val errCode: Int
    val msg: String
  }

  case class SuccessResponse(
    msg: String = "ok",
    errCode: Int = 0
  )

  case class ErrorResponse(errCode: Int, msg: String) extends RestResponse

  case class LoginRst(
    userName:String,
    password:String
  )

  case class ModifyPwdRst(
    oldPwd:String,
    newPwd:String
  )

  case class BindBoxRst(
    mac:String,
    boxName:String,
    shopId:Long
  )

  case class UnbindBoxRst(
    shopId:Long,
    boxId:Long
  )

  case class AddObserverRst(
    shopId:Long,
    openId:String
  )

  case class DeleteObserverRst(
    shopId:Long,
    openId:String
  )


  case class AdminInfo(
    loginName:String
  )

  case class AdminLoginRsp(
    data:AdminInfo,
    errCode:Int = 0,
    msg:String = "ok"
  )


  case class User(
    id:Long,
    openId:String,
    nickName:String,
    headImg:String,
    createTime:Long,
    districtId:Long
  )

  case class UserListRsp(
    data:Seq[User],
    page:Int,
    totalPage:Int,
    errCode:Int = 0,
    msg:String = "ok"
  )

  case class UserInfoRsp(
    data:User,
    errCode:Int = 0,
    msg:String = "ok"
  )

  case class UserSearchRsp(
    data:Seq[User],
    errCode:Int = 0,
    msg:String = "ok"
  )


  case class ShopInfoAdmin(
    id:Long,
    name:String,
    createTime:Long,
    groupId:Long
  )

  case class UserShop(
    id:Long,
    name:String,
    createTime:Long,
    groupId:Long,
    privilege:Int  //1:店主  2:观察者
  )

  case class ListUserShopRsp(
    data:Seq[UserShop],
    errCode:Int = 0,
    msg:String = "ok"
  )

  case class ShopSearchRsp(
    data:Seq[ShopInfoAdmin],
    errCode:Int = 0,
    msg:String = "ok"
  )

  case class ListShopRsp(
    data:Seq[ShopInfoAdmin],
    page:Int,
    totalPage:Int,
    errCode:Int = 0,
    msg:String = "ok"
  )

  case class BoxInfo(
    id:Long,
    name:String,
    mac:String,
    createTime:Long
  )

  case class ListBoxRsp(
    data:Seq[BoxInfo],
    errCode:Int = 0,
    msg:String = "ok"
  ) extends RestResponse

  case class Observer(
    userId:Long,
    openId:String,
    nickName:String,
    headImg:String,
    createTime:Long
  )

  case class ListObserverRsp(
    data:Seq[Observer],
    errCode:Int = 0,
    msg:String = "ok"
  )

  case class ShopObserver(
    id: Long,
    shopName: String = "",
    createTime: Long = 0L,
    groupId: Long = 0
  )

  case class AddObserverRsp(
    data:Long,
    msg: String = "ok",
    errCode: Int = 0
  )

}
