package com.neo.sk.feeler3.frontend.business

import com.neo.sk.feeler3.frontend.Component
import com.neo.sk.feeler3.frontend.utils.Http
import com.neo.sk.feeler3.ptcl.WsProtocol.UserWxInfoRsp
import io.circe.generic.auto._
import org.scalajs.dom._
import org.scalajs.dom.html._

import scala.concurrent.ExecutionContext.Implicits.global
import scalatags.JsDom.short._

object toTop extends Component[Div]{
  val to_top = i(*.id:="to_top")("回到顶部").render
  to_top.onclick ={ e:MouseEvent =>
    document.documentElement.scrollTop =0
    document.body.scrollTop = 0
    e.preventDefault()
  }

  override def render()= {
    div(*.position:="fixed",*.right:="5%",*.top:="90%",*.float:="right")(
      to_top
    ).render
  }

}
