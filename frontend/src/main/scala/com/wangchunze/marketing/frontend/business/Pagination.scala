package com.neo.sk.feeler3.frontend.business

import com.neo.sk.feeler3.frontend.Component
import org.scalajs.dom.MouseEvent
import org.scalajs.dom.html.{Div, Element}

import scala.collection.mutable

/**
  * Created by hongruying on 2017/3/28.
  */
class Pagination(dataLength:Int,pageSize:Int,showPageFun:Int => Unit) extends Component[Div]{
  import scalatags.JsDom.short._

  val tmp = mutable.HashMap[String,Element]()

  val paginationNum = {
    val t = dataLength / pageSize
    println(s"dataLength=$dataLength,pageSiez=$pageSize")
    if(t*pageSize == dataLength && dataLength != 0)
      t
    else
      t+1
  }

  var curPage = 1

  showPageFun(1)

  private [this] def getPreLi() ={
    if(curPage == 1){
      li(
        a()(
          "<<"
        )
      ).render
    }else{
      li(
        a(*.onclick:=clickPageAction(curPage - 1))(
          "<<"
        )
      ).render
    }
  }

  private [this] def getnextLi() ={
    if(curPage == paginationNum){
      li(
        a()(
          ">>"
        )
      ).render
    }else{
      li(
        a(*.onclick:=clickPageAction(curPage + 1))(
          ">>"
        )
      ).render
    }
  }

//  val pre = li(
//    a(*.onclick:=clickPageAction(curPage - 1))(
//      "<<"
//    )
//  ).render
//
//  val next = li(
//    a(*.onclick:=clickPageAction(curPage + 1))(
//      ">>"
//    )
//  ).render

  private[this] def getPageLi(i:Int) = {
    if(curPage == i){
      li(
        a()(
          i.toString
        )
      ).render
    }else{
      li(
        a(*.onclick:=clickPageAction(i))(
          i.toString
        )
      ).render
    }

  }

  val pagination = ul(*.cls:="pagination").render

  def makePaginationBuild() = {
    pagination.innerHTML =""
    val pre = getPreLi()
    pagination.appendChild(pre)
    tmp.put("pre",pre)
    if(curPage == 1){
      pre.className = "disabled"
    }else{
      pre.className = ""
      pre.onclick = {e:MouseEvent => e.preventDefault();}
    }
//    println(s"paginationNum=$paginationNum")
    for(i <- 1 to paginationNum){
      val t = getPageLi(i)
      pagination.appendChild(t)
      tmp.put(i.toString,t)
      if(i == curPage)
        t.className = "active"
    }
    val next = getnextLi()
    pagination.appendChild(next)
    tmp.put("next",next)
    if(curPage == paginationNum){
      next.className = "disabled"
    }else{
      next.className = ""
      pre.onclick = {e:MouseEvent => e.preventDefault();}
    }

  }

  def clickPageAction(x:Int) :MouseEvent =>Unit ={ e:MouseEvent =>
    e.preventDefault()
    showPageFun(x)
    curPage = x
    makePaginationBuild()

  }

  override def render() = {
    makePaginationBuild
    div(
      pagination
    ).render
  }
}
