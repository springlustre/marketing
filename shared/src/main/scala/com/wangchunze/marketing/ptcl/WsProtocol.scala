package com.neo.sk.feeler3.ptcl

/**
  * Created by springlustre on 2017/3/27.
  */
package object WsProtocol {

  sealed trait LoginWsData

  //对应session信息字段
  case class UserSession(
    nickname:String,
    headImg :String,
    timestamp:String,
    openId :String,
    u_type :String,
    districtId :String
  )

  case class ScanUrl(
    dataType:Int,    //1
    data:String
  )extends LoginWsData

  case class LoginRes(
    dataType:Int,    //2
    errCode:Int = 0,  //0 登录成功 else失败
    msg:String = "ok"
  )extends LoginWsData


  case class WsInfoRsp(
    outSign:String,
    wsUrl:String,
    scanUrl:String,
    errCode:Int = 0,  //0 登录成功 else失败
    msg:String = "ok"
  )

  case class UserWxInfoRsp(
    data:UserSession,
    errCode:Int = 0,  //0 登录成功 else失败
    msg:String = "ok"
  )


}
