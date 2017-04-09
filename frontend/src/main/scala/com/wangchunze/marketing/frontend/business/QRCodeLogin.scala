package com.neo.sk.feeler3.frontend.business

import com.neo.sk.feeler3.frontend.Component
import com.neo.sk.feeler3.frontend.utils.{Http, JsFunc}
import com.neo.sk.feeler3.ptcl.AdminProtocol._
import io.circe.Json
import org.scalajs.dom._
import org.scalajs.dom.html._

import scalatags.JsDom.short._
import io.circe.generic.auto._
import io.circe.parser.parse
import io.circe.syntax._
import cats.syntax.either._
import org.scalajs.dom

import concurrent.ExecutionContext.Implicits.global

/**
  * Created by springlustre on 2017/3/28.
  */

class QRCodeLogin(outSign:String,wsUrl:String,scanUrl:String) extends Component[Div] {


  val loginDiv = div(*.cls:="qr-code-board")(
    "手机扫码 安全登录",
      img(*.src:=scanUrl),
      div(*.cls:="qr-bottom")(
        span()(
          img(*.src:="../static/pc/img/scan-logo.png")
        ),
        span(*.cls:="qr-bottom-text")(
          p()("打开微信"),
          p()("扫一扫")
        )
      )
    )


  val qrDiv = div()().render


  override def render()= {
    println("render userList")
    div(
      loginDiv
    ).render
  }



  val ws = new dom.WebSocket(wsUrl)
  ws.onopen = {
    (e: Event) =>
      println(s"ws.onopen...${e.timeStamp}")
  }
  ws.onmessage = {
    (e: dom.MessageEvent) =>
      val info = e.data.toString
      val json = parse(info).getOrElse(Json.Null)
      json.hcursor.downField("dataType").as[Int].getOrElse(0) match {
        case 2 => //登陆成功
          window.location.href = "main"
      }
  }

  ws.onclose = {
    (e: Event) =>
      window.alert("ws 断开")
  }


  window.setInterval(tickTock(),30000)

  def tickTock() = new Function0[Unit] {
    val str =
      s"""
        {
          "HandleInfo": "tick tock"
        }
      """
    val json = parse(str).getOrElse(Json.Null)
    def apply(): Unit = ws.send(json.toString())
  }




}
