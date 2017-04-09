package com.neo.sk.feeler3.frontend.business

import com.neo.sk.feeler3.frontend.Component

/**
  * Created by springlustre on 2017/3/29.
  */
import com.neo.sk.feeler3.frontend.{Component, Route}
import com.neo.sk.feeler3.frontend.utils.{Http, JsFunc}
import com.neo.sk.feeler3.ptcl.AdminProtocol._
import org.scalajs.dom
import org.scalajs.dom._
import org.scalajs.dom.html._

import scalatags.JsDom.short._
import io.circe.generic.auto._
import io.circe._
import io.circe.parser._
import cats.syntax.either._
import io.circe.syntax._
import scala.concurrent.ExecutionContext.Implicits.global
import com.neo.sk.feeler3.frontend.utils.MyUtil._

object MobileComfirm extends Component[Div]{
  val headBar = header(*.cls:="mobile-bar")("微信安全登录")

  val loginDiv = div(*.cls:="logo-div")(
    img(*.src:="../static/pc/img/test.png"),
    div(*.cls:="logo-div-tips")(
      "即将登录feeler3，请确认是否本人操作"
    ),
    div(*.cls:="logo-div-remind")(
      "使用你的账号登录该应用"
    )
  ).render

  val confirmBtn =  button(*.cls:="mob-condirm-btn")(
    "确认登录"
  ).render
  confirmBtn.onclick = confirm

  val btnDiv = div(*.cls:="login-btn-div")(
    confirmBtn,
    button(*.cls:="mob-cancel-btn")(
      "取 消"
    )
  ).render

  val mainComtent = div(*.cls:="mob-background")(
    loginDiv,
    btnDiv
  ).render


  val confirmSuc = div(*.cls:="confirm-success")(
    img(*.src:="../static/pc/img/comfirm-suc.png"),
    p()("已确认登录应用")
  ).render


  override def render() = {
    div()(
      headBar,
      mainComtent
    ).render
  }

  val outSign = GetQueryString("outSign").getOrElse("")

  def confirm = {e:MouseEvent=>
    e.preventDefault()
    val url = "/feeleriii/login/scanConfirm?outSign="+outSign
    Http.getAndParse[SuccessResponse](url).map {
      case Right(rsp) =>
        if (rsp.errCode == 0) {
          showSuccess
        } else {
          println(s"get list error: msg=${rsp.msg}")
          JsFunc.alert(s"授权失败！msg:${rsp.msg}")
        }
      case Left(error) =>
        println(s"get list error: $error")
        JsFunc.alert(s"授权失败！error:$error")
    }
  }

  def showSuccess = {
    mainComtent.className="mob-background-white"
    mainComtent.innerHTML = ""
    mainComtent.appendChild(confirmSuc)
  }


}







