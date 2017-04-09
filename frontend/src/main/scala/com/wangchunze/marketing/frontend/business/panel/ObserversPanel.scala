package com.neo.sk.feeler3.frontend.business.panel

import com.neo.sk.feeler3.frontend.{Component, Route}
import com.neo.sk.feeler3.frontend.business.Pagination
import com.neo.sk.feeler3.frontend.utils.{Http, JsFunc, Modal}
import com.neo.sk.feeler3.ptcl.ShopProtocol.{CommonRsp, GetShareCodeRsp}
import org.scalajs.dom.{MouseEvent, document}
import org.scalajs.dom.html.{Div, Element}

import scala.collection.mutable
import scalatags.JsDom.short._
/**
  * Created by hongruying on 2017/3/30.
  */
class ObserversPanel(shopId:Long) extends Component[Div]{
  import com.neo.sk.feeler3.ptcl.ShopProtocol.ShopModel._
  import scala.concurrent.ExecutionContext.Implicits.global
  import io.circe.generic.auto._
  import io.circe.syntax._

  import concurrent.ExecutionContext.Implicits.global


  private val modalContainer = div().render

  val observersMap = new mutable.HashMap[Long,(Observer,Element)]()
  val pageSize = 5
  val observerTitle = div(*.cls:="panel-heading shop-panel-heading")(
    span()(
      "观察者列表"
    ),
    span(*.cls:="panel-heading__button")(
      button(*.cls:="btn btn-primary",*.onclick:=getShareCode)("分享店铺").render
    )
  ).render

  private[this] def getShareCode = {e : MouseEvent =>
    e.preventDefault()
    val url = Route.baseUrl + s"/shop/getShopShareCode?shopId=$shopId"

    Http.getAndParse[GetShareCodeRsp](url).map{
      case Right(rsp) =>
        if(rsp.errCode == 0){//重新刷新店铺列表
          showShareCodeModal(rsp.data.getOrElse(""))
        }else{
          println(s"获取分享码错误: ${rsp.msg}")
          JsFunc.alert(s"获取分享码错误: msg=${rsp.msg}")
        }
      case Left(error) =>
        println(s"获取分享码错误: $error")
        JsFunc.alert(s"获取分享码错误: $error")
    }
  }

  private[this] def showShareCodeModal(shareCode:String) :Unit = {
    val successFunc: () => Unit = { () =>
      println("")
    }
    val shareCodeBox = input(
      *.id:="ur12",
      *.value:=shareCode,
      *.border:="none",
      *.readonly:="readonly"
    ).render

    val copy = button(
      *.id:="copy",
      *.cls:="text-primary btn btn-link",
      *.onclick:={e:MouseEvent =>
        e.preventDefault()
        shareCodeBox.select()
        document.execCommand("Copy")
        JsFunc.alert("复制成功")}
    )(
      "复制分享码"
    ).render




    val modal = new Modal(
      div(h2("店铺分享码")).render,
      div(*.cls := "form-group")(
        label(*.`for` := shopId.toString,*.cls:="modal-label")(shareCodeBox).render,
        copy
      ).render,
      successFunc,
      shopId.toString
    )

    modalContainer.innerHTML = ""
    modalContainer.appendChild(modal.render())
    modal.show
  }



  val observersTbody = tbody().render
  val paginationContainer = div(*.textAlign:="center").render
  val noDataDiv = div(*.textAlign:="center").render
  val observerTable = div(*.cls:="panel-body")(
    table(*.cls:="table table-striped")(
      colgroup(*.colspan:="3")(
        col(*.cls:="observer-table-nickname"),
        col(*.cls:="observer-table-headImg"),
        col(*.cls:="observer-table-operator")
      ),
      thead()(
        tr()(
          th()(
            "昵称"
          ),
          th()(
            "头像"
          ),
          th()(
            "操作"
          )
        )
      ),
      observersTbody
    ),
    noDataDiv,
    paginationContainer
  ).render

  val showPageContent :Int =>Unit ={ x =>
    observersTbody.innerHTML = ""
    val content = observersMap.toList.drop((x-1)*pageSize).take(pageSize)
    content.map{c =>
      observersTbody.appendChild(c._2._2)
    }
  }

  def makeObserversBuild(observers:Seq[Observer]):Unit = {
    observersMap.clear()
    observersTbody.innerHTML = ""
    observers.map{ observer =>
      val newRow = tr(*.cls:="observer-row")(
        td()(
          observer.nickname
        ),
        td()(
          img(*.src:=observer.headImg,*.cls:="img-responsive observer-headImg")
        ),
        td()(
          button(*.cls:="btn btn-danger",*.onclick:=showDeleteObserverModal(observer.id,observer.openId,observer.nickname))("删除").render
        )
      ).render
      observersMap.put(observer.id,(observer,newRow))
      //        observersTbody.appendChild(newRow)
    }
    if(observersMap.isEmpty){
      val noData = div(*.cls:="shop-no-data")(
        img(*.src:="/feeleriii/static/pc/img/icon/noData.png"),
        "暂无数据"
      ).render
      noDataDiv.innerHTML =""
      noDataDiv.appendChild(noData)
    }else{
      noDataDiv.innerHTML =""
    }
    paginationContainer.innerHTML = ""
    val pagination = new Pagination(observersMap.size,pageSize,showPageContent)
    paginationContainer.appendChild(pagination.render())
  }

  private[this] def showDeleteObserverModal(id:Long,openId:String,nickname:String) = { e: MouseEvent =>
    e.preventDefault()
    val successFunc: () => Unit = { () =>
      val url = Route.baseUrl + s"/shop/deleteObserver?shopId=$shopId&openId=$openId"

      Http.getAndParse[CommonRsp](url).map{
        case Right(rsp) =>
          if(rsp.errCode == 0){
            observersMap.remove(id)
            makeObserversBuild(observersMap.map(_._2._1).toSeq)
          }else{
            println(s"删除观察者错误: ${rsp.msg}")
            JsFunc.alert(s"删除观察者错误: msg=${rsp.msg}")
          }
        case Left(error) =>
          println(s"删除观察者错误:  $error")
          JsFunc.alert(s"删除观察者错误:  $error")
      }
    }

    val modal = new Modal(
      div(h2("删除观察者")).render,
      div(*.cls := "form-group")(
        label(*.`for` := shopId.toString,*.cls:="modal-label")(s"确认是否删除该用户（$nickname）对本店的观察权限。").render
      ).render,
      successFunc,
      shopId.toString
    )

    modalContainer.innerHTML = ""
    modalContainer.appendChild(modal.render())
    modal.show
  }

  override def render() = {
    div(*.cls:="panel panel-default")(
      observerTitle,
      observerTable,
      modalContainer
    ).render
  }

}

