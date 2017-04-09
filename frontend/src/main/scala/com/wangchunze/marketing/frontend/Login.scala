package com.neo.sk.feeler3.frontend

/**
  * Created by springlustre on 2017/3/14.
  */

import com.neo.sk.feeler3.frontend.Route.AdminRoutes
import com.neo.sk.feeler3.frontend.utils.{Http, JsFunc}
import com.neo.sk.feeler3.ptcl.AdminProtocol._
import org.scalajs.dom._
import org.scalajs.dom.html._

import scalatags.JsDom.short._

object Login extends Component[Div]{
  import io.circe.generic.auto._
  import io.circe.syntax._

  import concurrent.ExecutionContext.Implicits.global

  val loginBar = div(*.cls:="login-bar")(
    div(*.cls:="login-bar-title")(
      img(*.src:="../static/pc/img/manage.png"),
      "feeler3后台管理系统"
    )
  )

  val username = input(*.`type`:="text",*.placeholder:="用户名").render
  val password = input(*.`type`:="password",*.placeholder:="密 码").render

  val loginDiv = div(*.cls:="login-div")(
    div(*.cls:="login-board")(
      div(*.cls:="login-title")(
        img(*.src:="../static/pc/img/login-img.png"),
        "管理登录"
      ),
      div(*.cls:="login-input")(
        img(*.src:="../static/pc/img/username.png"),
        username
      ),
      div(*.cls:="login-input")(
        img(*.src:="../static/pc/img/password.png"),
        password
      ),
      div(*.cls:="login-btn")(
        button(*.onclick:=loginSubmit)("登录")
      )
    )
  )

  override def render()= {
    println("render userList")
    div(
      loginBar,
      loginDiv
    ).render
  }


  def loginSubmit = {event:MouseEvent =>
    event.preventDefault()
    val data = LoginRst(username.value.trim,password.value).asJson.noSpaces
    val url= Route.AdminRoutes.login
    Http.postJsonAndParse[AdminLoginRsp](url, data).map {
      case Right(rsp) =>
        if (rsp.errCode == 0) {
          window.location.href = "userlist"
        } else {
          println(s"error: ${rsp.msg}")
          JsFunc.alert(s"error: ${rsp.msg}")
        }
      case Left(error) => JsFunc.alert(s"error: $error")
    }

  }



}
