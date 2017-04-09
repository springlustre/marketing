package com.neo.sk.feeler3.frontend.business

import org.scalajs.dom

import scala.scalajs.js.annotation.JSExport

/**
  * Created by springlustre on 2017/3/24.
  */
@JSExport
object HongTest{

  import scalatags.JsDom.short._

  private val bodyContent = div().render

  dom.document.body.appendChild(bodyContent)

  @JSExport
  def showPage() ={
    val page = BusinessShopList.render()

//    val page = h1("123").render
    bodyContent.innerHTML = ""
    bodyContent.appendChild(page)
  }
}
