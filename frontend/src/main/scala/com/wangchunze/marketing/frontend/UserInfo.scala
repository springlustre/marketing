package com.neo.sk.feeler3.frontend

/**
  * Created by springlustre on 2017/3/7.
  */
import com.neo.sk.feeler3.frontend.Route.AdminRoutes
import com.neo.sk.feeler3.frontend.utils.{Http, JsFunc}
import com.neo.sk.feeler3.ptcl.AdminProtocol._
import org.scalajs.dom
import org.scalajs.dom._
import org.scalajs.dom.html._

import scalatags.JsDom.short._
import com.neo.sk.feeler3.frontend.utils.MyUtil._

import io.circe.generic.auto._
import io.circe.syntax._
import concurrent.ExecutionContext.Implicits.global

class UserInfo(nickName:String,headImg:String) extends Component[Div] {


    import scalatags.JsDom.short._

    override def render(): Div = {
      div(*.backgroundColor := "yellow", *.width := "200px")(
        p("this is a Counter")
//        nameDom,
//        countDom,
//        minusButton,
//        plusButton
      ).render
    }



}
