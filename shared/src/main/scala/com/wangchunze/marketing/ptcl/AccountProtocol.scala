package com.wangchunze.marketing.ptcl

/**
  * Created by springlustre on 2017/4/13.
  */

object AccountProtocol {

  case class AccountInfo(
    id:Long,
    email:String,
    mobile:String,
    uType:Int
  )

  case class AccountLoginSuc(
    data:AccountInfo,
    msg: String = "ok",
    errCode: Int = 0
  )


}
