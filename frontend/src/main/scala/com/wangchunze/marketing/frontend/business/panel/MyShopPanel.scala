package com.neo.sk.feeler3.frontend.business.panel

import com.neo.sk.feeler3.frontend.business.{BusinessShopDetail, BusinessShopList, Pagination}
import com.neo.sk.feeler3.frontend.utils.{Http, JsFunc, Modal}
import com.neo.sk.feeler3.frontend.{Component, Route}
import com.neo.sk.feeler3.ptcl.ShopProtocol._
import io.circe.Json
import org.scalajs.dom.MouseEvent
import org.scalajs.dom.html.{Div, Element}

import scala.collection.mutable

/**
  * Created by hongruying on 2017/3/29.
  */
class MyShopPanel(enterShopFun: (Long)=>Unit) extends Component[Div]{
  import ShopModel._
  import scalatags.JsDom.short._
  import io.circe.generic.auto._
  import io.circe.syntax._

  import concurrent.ExecutionContext.Implicits.global
  import com.neo.sk.feeler3.ptcl.ShopProtocol._
  import com.neo.sk.feeler3.frontend.utils.MyUtil._

  private val modalContainer = div().render

  private val shopMap = new mutable.HashMap[Long,(ShopInfo,Element)]()
  private val pageSize = 5
  private val tableBar = div(*.cls:="panel-heading shop-panel-heading")(
    span(*.cls:="panel-heading__title")(
      "我的店铺"
    ),
    span(*.cls:="panel-heading__button")(
      button(*.cls:="btn btn-primary",*.onclick:=showCreateShopModal)("创建")
    )
  ).render

  private [this] def showCreateShopModal = { e: MouseEvent =>
    e.preventDefault()
    val modalInputBox = input(
      *.`type` := "text",
      *.cls := "form-control",
      *.placeholder:="店铺名称"
    ).render

    def checkForm = {
      if(modalInputBox.value == ""){
        JsFunc.alert(s"店铺名称不能为空")
        false
      }else{
        true
      }
    }

    val successFunc: () => Unit = { () =>
      if(checkForm){
        val newShopName = modalInputBox.value
        val data = Json.obj(
          ("shopId",0.asJson),
          ("shopName",newShopName.asJson),
          ("boxName","".asJson),
          ("boxMac","".asJson)
        ).noSpaces

        val url = Route.baseUrl + s"/shop/createShop"

        Http.postJsonAndParse[CreateShopRsp](url,data).map{
          case Right(rsp) =>
            if(rsp.errCode == 0){//重新刷新店铺列表
            val d = shopMap.map(_._2._1).toSeq :+ rsp.data.get
              makeShopTbody(d)
            }else{
              println(s"create shop  error: ${rsp.msg}")
              JsFunc.alert(s"create shop  error: msg=${rsp.msg}")
            }
          case Left(error) =>
            println(s"create shop  error:$error")
            JsFunc.alert(s"create shop  error:$error")
        }
      }
    }

    val modal = new Modal(
      div(h2("创建店铺")).render,
      div(*.cls := "form-group")(
        label(*.`for` := "0",*.cls:="modal-label")("新店铺名称").render,
        modalInputBox
      ).render,
      successFunc,
      "0"
    )

    modalContainer.innerHTML = ""
    modalContainer.appendChild(modal.render())
    modal.show
  }

  private val shopTbody = tbody().render
  private val paginationContainer = div(*.textAlign:="center").render
  val noDataDiv = div(*.textAlign:="center").render
  private val shopTable = div(*.cls:="panel-body")(
    table(*.cls:="table table-striped")(
      colgroup(*.colspan:="3")(
        col(*.cls:="myshop-table-name"),
        col(*.cls:="myshop-table-time"),
        col(*.cls:="myshop-table-operator")
      ),
      thead()(
        tr()(
          th("店铺名称"),
          th("创建时间"),
          th("操作")
        )
      ),
      shopTbody
    ),
    noDataDiv,
    paginationContainer
  ).render


  //插入一个店铺
  private def insertNewShop(shop:ShopInfo) ={
    val newRow = tr(
      td()(
        shop.shopName
      ),
      td()(
        dataFormatDefault(shop.createTime)
      ),
      td()(
        button(*.cls:="btn btn-link",*.onclick:=enterShopDetail(shop.id))("管理").render
      )
    ).render
    shopMap.put(shop.id,(shop,newRow))
    shopTbody.appendChild(newRow)
  }

  //更新店铺名称
  private def updateShopName(id:Long,shopName:String) = {
    val shopOpt = shopMap.get(id)
    if(shopOpt.isDefined){
      val value = shopOpt.get
      value._2.innerHTML = ""
      value._2.appendChild(
        td()(
          shopName
        ).render
      )
      value._2.appendChild(
        td()(
          dataFormatDefault(value._1.createTime)
        ).render
      )
      value._2.appendChild(
        td()(
          button(*.cls:="btn btn-link",*.onclick:=enterShopDetail(id))("管理").render
        ).render
      )
      shopMap.put(id,(value._1.copy(shopName = shopName),value._2))
    }else{
      println(s"update Shop Name error:id=$id is not exists")
    }
  }

  //将数据渲染
  def makeShopTbody(shops:Seq[ShopInfo]) = {
    shopTbody.innerHTML = ""
    shopMap.clear()
    shops.sortBy(_.createTime).map{ shop =>
      val newRow = tr(
        td()(
          shop.shopName
        ),
        td()(
          dataFormatDefault(shop.createTime)
        ),
        td()(
          button(*.cls:="btn btn-link",*.onclick:=enterShopDetail(shop.id))("管理").render
        )
      ).render
      shopMap.put(shop.id,(shop,newRow))
      //        shopTbody.firstChild.appendChild(newRow)
    }

    if(shopMap.isEmpty){
      val noData = div(*.cls:="shop-no-data")(
        img(*.src:="/feeleriii/static/pc/img/icon/noData.png"),
        "暂无数据"
      ).render
      noDataDiv.innerHTML =""
      noDataDiv.appendChild(noData)
//      shopTable.firstChild.appendChild(noData)
    }else{
      noDataDiv.innerHTML =""
    }

    paginationContainer.innerHTML = ""
    val pagination = new Pagination(shopMap.size,pageSize,showPageContent)
    paginationContainer.appendChild(pagination.render())
  }

  private val showPageContent :Int =>Unit ={ x =>
    shopTbody.innerHTML = ""
    val content = shopMap.toList.sortBy(_._2._1.createTime).drop((x-1)*pageSize).take(pageSize)
    content.map{c =>
      shopTbody.appendChild(c._2._2)
    }
  }

  private def enterShopDetail(shopId:Long) = { e:MouseEvent =>
    e.preventDefault()
    enterShopFun(shopId)
//    val shopDetailPage = new BusinessShopDetail(shopId)
//
//    hide
//    shopDetailContainer.innerHTML = ""
//    shopDetailContainer.appendChild(shopDetailPage.render())
//    shopDetailPage.show
  }

  override def render() = {
    div(*.cls:="panel panel-default none-margin")(
      tableBar,
      shopTable,
      modalContainer
    ).render
  }

}
