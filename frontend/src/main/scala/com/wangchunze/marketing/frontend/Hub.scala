package com.neo.sk.feeler3.frontend


import org.scalajs.dom
import org.scalajs.dom.html.Heading

import scalajs.js

/**
  * User: Taoz
  * Date: 1/11/2017
  * Time: 2:01 PM
  */
object Hub extends js.JSApp{


  import scalatags.JsDom.short._

  val p1 = """/""".r

  val basePath =
    Option(dom.document.getElementById("basePath"))
      .map(_.innerHTML).getOrElse("/feeleriii/")

  def elementIdHtml(id: String): Option[String] = Option(dom.document.getElementById(id)).map(_.innerHTML)


  @scala.scalajs.js.annotation.JSExport
  override def main(): Unit = {

    println(s"fake basePath: ${elementIdHtml("basePath")}")
    println(s"fake pathName: ${elementIdHtml("pathname")}")
    println(s"fake urlSearch: ${elementIdHtml("urlSearch")}")

    println(s"current href: ${dom.window.location.href}")
    println(s"current search: ${dom.window.location.search}")
    println(s"current pathname: ${dom.window.location.pathname}")


    val path =
      elementIdHtml("pathname").getOrElse(dom.window.location.pathname)


    println(s"full path: $path")

    val paths = if (path.startsWith(basePath)) {
      p1.split(path.substring(basePath.length))
    } else {
      Array[String]()
    }

    println(s"valid paths: ${paths.mkString("[", ";", "]")}")

    val page = if (paths.length < 1) {
      println(s"paths.length error: $path")
      todo(s"paths.length error: $path")
    } else {

      //JsFunc.alert(s"here is Hub. full href: ${dom.document.location.href}")
      paths(0) match {

        case "pc" => paths(1) match {
          case x => todo(s"user match error: ${paths.mkString("/")}")
        }

        case x => todo(s"paths(0) match error: ${paths(0)}")
      }
    }

    dom.document.body.appendChild(page)
  }

  def todo(title: String): Heading = h1(
    s"TO DO PAGE: $title"
  ).render

}
