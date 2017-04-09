package com.neo.sk.feeler3.frontend.business

/**
  * Created by springlustre on 2017/3/14.
  */

import com.neo.sk.feeler3.frontend.{Component, Route}
import com.neo.sk.feeler3.frontend.utils.{Http, JsFunc}
import com.neo.sk.feeler3.ptcl.AdminProtocol._
import com.neo.sk.feeler3.ptcl.WsProtocol.{UserWxInfoRsp, WsInfoRsp}
import org.scalajs.dom
import org.scalajs.dom._
import org.scalajs.dom.html._

import scalatags.JsDom.short._

object UserLogin extends Component[Div]{
  import io.circe.generic.auto._
  import io.circe._
  import io.circe.parser._
  import cats.syntax.either._
  import io.circe.syntax._

  import concurrent.ExecutionContext.Implicits.global

  val loginBar = div(*.cls:="login-bar")(
    div(*.cls:="login-bar-title")(
      img(*.src:="../static/pc/img/manage.png"),
      "feeler3后台管理系统"
    )
  )

  val loginDiv = div(*.cls:="login-div")(
  ).render

  override def render()= {
    println("render userList")
    getOutSign
    div(
      loginBar,
      loginDiv
    ).render
  }

  def getOutSign = {
    val url = "/feeleriii/login/getOutSign"
    Http.getAndParse[WsInfoRsp](url).map {
      case Right(rsp) =>
        if (rsp.errCode == 0) {
          val qr = new QRCodeLogin(rsp.outSign,rsp.wsUrl,rsp.scanUrl).render
          loginDiv.appendChild(qr)
        } else {
          println(s"get list error: ${rsp.msg}")
        }
      case Left(error) =>
        println(s"get list error: $error")
        window.location.href = "login"
    }
  }





}
