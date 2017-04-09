package com.neo.sk.feeler3.frontend.business


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
import com.neo.sk.feeler3.frontend.Route.AdminRoutes
import com.neo.sk.feeler3.ptcl.WsProtocol.UserWxInfoRsp
import io.circe.syntax._
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by springlustre on 2017/3/28.
  */
object MainPage extends Component[Div]{

  val list = List((1,"按日查询",databyDay),(2,"按周查询",databyWeek),(3,"按月查询",databyMonth),
                   (4,"我的店铺",BusinessShopList))
  val cateList = list.map{x=>
    if(x._1==1){
      li(*.id := x._1,*.cls:="active")(a(*.href := "#")(x._2)).render
    }else {
      li(*.id := x._1)(a(*.href := "#")(x._2)).render
    }
  }
  cateList.zip(list).foreach{x=>
    x._1.onclick = switchCate(x._2._1,x._2._3)
  }



  val userName = a(*.href:="#")().render
  val headImg = img().render

  val navBar = div(*.cls:="navbar navbar-inverse navbar-fixed-top feeler3-nav")(
      div(*.cls:="container-fluid feeler3-container")(
        div(*.cls:="navbar-header")(
          a(*.cls:="navbar-brand feeler3-brand",*.href:="#")(
            "Feeler3平台"
          )
        ),
        div(*.cls:="navbar-collapse collapse")(
          ul(*.cls:="nav navbar-nav navbar-right feeler3-navbar")(
            li()(
              a(*.href:="#")(headImg)
            ),
            li()(
              userName
            ),
            li()(
              a(*.href:="#")("帮助")
            ),
            li()(
              a(*.href:="#",*.onclick:=logout)("退出")
            )
          )
        )
      )
  )

  val contentDiv = div(*.cls:="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main")(
  ).render

  val mainBody = div(*.cls:="container-fluid")(
    div(*.cls:="row")(
      div(*.cls:="col-sm-3 col-md-2 sidebar")(
        ul(*.cls:="nav nav-sidebar")(
          cateList.take(3)
        ),
        ul(*.cls:="nav nav-sidebar")(
          cateList.takeRight(1)
        )
      ),
      contentDiv
    )
  )



  override def render()= {
    println("render userList")
    getUserInfo
    div(
      navBar,
      mainBody
    ).render
  }


  def switchCate(id:Int,content:Component[Div]) = {e:MouseEvent=>
    e.preventDefault()
    cateList.foreach{x=>
      x.className = ""
    }
    window.document.getElementById(id.toString).setAttribute("class","active")
    contentDiv.innerHTML = ""
    contentDiv.appendChild(content.render())
  }


  def getUserInfo = {
    val url = "/feeleriii/user/getUserInfo"
    Http.getAndParse[UserWxInfoRsp](url).map {
      case Right(rsp) =>
        if (rsp.errCode == 0) {
          userName.innerHTML = rsp.data.nickname
          headImg.src = rsp.data.headImg
          contentDiv.appendChild(databyDay.render())
        } else {
          println(s"get list error: ${rsp.msg}")
        }
      case Left(error) =>
        println(s"get list error: $error")
        window.location.href = "login"
    }
  }

  def logout = {e:MouseEvent=>
    e.preventDefault()
    window.location.href = "login"
  }


}
