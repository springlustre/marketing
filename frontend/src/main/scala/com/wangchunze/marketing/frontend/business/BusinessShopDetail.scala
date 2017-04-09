package com.neo.sk.feeler3.frontend.business

//import com.neo.sk.feeler3.frontend.business.facede.clipboard.Clipboard
import com.neo.sk.feeler3.frontend.business.panel.{BoxesPanel, ObserversPanel, ShopInfoPanel}
import com.neo.sk.feeler3.frontend.{Component, Route}
import com.neo.sk.feeler3.frontend.utils.{Http, JsFunc, Modal, MyUtil}
import com.neo.sk.feeler3.ptcl.ShopProtocol.{CommonRsp, CreateBoxRsp, GetShareCodeRsp, GetShopDetailRsp}
import com.neo.sk.feeler3.ptcl.ShopProtocol.ShopModel.{BoxInfo, ShopDetail, ShopInfo}
import io.circe.Json
import org.scalajs.dom.MouseEvent
import org.scalajs.dom.html._
import org.scalajs.dom.document

import scala.collection.mutable
import scala.concurrent.Future

/**
  * Created by hongruying on 2017/3/24.
  */
class BusinessShopDetail(shopId:Long) extends Component[Div]{
  import scalatags.JsDom.short._
  import io.circe.generic.auto._
  import io.circe.syntax._

  import concurrent.ExecutionContext.Implicits.global

  private val modalContainer = div().render

  private val boxesComponent = new BoxesPanel(shopId)

  private val observersComponent = new ObserversPanel(shopId)

  private val shopInfoComponent = new ShopInfoPanel(shopId,boxesComponent,observersComponent)

//  object shopInfoComponent extends Component[Div]{
//    val componentTitle = div(*.cls:="panel-heading shop-panel-heading")(
//      "店铺信息"
//    ).render
//    val shopNameLabel = span("sssss").render
//    val createTimeLabel = span("2017-102 -2").render
//    val editShopNameButton = button(*.cls:="btn-link",*.onclick:=showEditShopNameModal)("修改店铺名称").render
//    val shopDiv = div(*.cls:="panel panel-default")(
//      componentTitle,
//      div(*.cls:="panel-body")(
//        div(*.cls:="row shop-info-row")(
//          div(*.cls:="col-md-3 none-padding shop-info-row-left")(
//            "店铺名称："
//          ),
//          div(*.cls:="col-md-9 none-padding shop-info-row-right")(
//            span()(
//              shopNameLabel
//            ),
//            span(*.cls:="shop-info-row-right-button")(
//              editShopNameButton
//            )
//          )
//        ),
//        div(*.cls:="row shop-info-row")(
//          div(*.cls:="col-md-3 none-padding shop-info-row-left")(
//            span()(
//              "创建时间："
//            )
//          ),
//          div(*.cls:="col-md-9 none-padding shop-info-row-right")(
//            span()(
//              createTimeLabel
//            )
//          )
//        ),
//        div(*.cls:="row shop-info-row")(
//          div(*.cls:="col-md-10 col-md-offset-1")(
//            BoxesComponent.render()
//          )
//        ),
//        div(*.cls:="row shop-info-row")(
//          div(*.cls:="col-md-10 col-md-offset-1")(
//            ObserversComponent.render()
//          )
//        )
//      )
//    ).render
//
//    private[this] def showEditShopNameModal = { e: MouseEvent =>
//      e.preventDefault()
//      val modalInputBox = input(
//        *.`type` := "text",
//        *.cls := "form-control",
//        *.value:=shopNameLabel.innerHTML
//      ).render
//
//      val successFunc: () => Unit = { () =>
//        val newShopName = modalInputBox.value
//
//
//        val url = Route.baseUrl + s"/shop/editShopName?shopId=$shopId&shopName=$newShopName"
//
//        Http.getAndParse[CommonRsp](url).map{
//          case Right(rsp) =>
//            if(rsp.errCode == 0){//重新刷新店铺列表
//              shopNameLabel.innerHTML = newShopName
//            }else{
//              println(s"编辑店铺名称: ${rsp.msg}")
//              JsFunc.alert(s"编辑店铺名称错误: msg=${rsp.msg}")
//            }
//          case Left(error) =>
//            println(s"编辑店铺名称错误: $error")
//            JsFunc.alert(s"编辑店铺名称错误: $error")
//        }
//      }
//
//      val modal = new Modal(
//        div(h2("编辑店铺名称")).render,
//        div(*.cls := "form-group")(
//          label(*.`for` := shopId.toString)("店铺名称").render,
//          modalInputBox
//        ).render,
//        successFunc,
//        shopId.toString
//      )
//
//      modalContainer.innerHTML = ""
//      modalContainer.appendChild(modal.render())
//      modal.show
//    }
//
//    def makeShopBuild(shop:ShopInfo) = {
//      shopNameLabel.innerHTML = shop.shopName
//      createTimeLabel.innerHTML = MyUtil.dataFormatDefault(shop.createTime)
//      //弹出框
//      //      editShopNameButton.onclick =
//    }
//
//    override def render() = {
//      shopDiv
//    }
//  }

//  object BoxesComponent extends Component[Div]{
//    import com.neo.sk.feeler3.ptcl.ShopProtocol.ShopModel._
//    val boxesMap = new mutable.HashMap[Long,(BoxInfo,Element)]()
//    val pageSize = 5
//    val boxesTitle = div(*.cls:="panel-heading shop-panel-heading")(
//      span()(
//        "设备管理"
//      ),
//      span(*.cls:="panel-heading__button")(
//        button(*.cls:="btn btn-primary",*.onclick:=showAddBoxModal)("添加").render
//      )
//    ).render
//
//    private[this] def showAddBoxModal = { e: MouseEvent =>
//      e.preventDefault()
//      val boxNameBox = input(
//        *.`type`:="text",
//        *.cls := "form-control",
//        *.placeholder:="设备名称").render
//
//      val boxMacBox = input(
//        *.`type`:="text",
//        *.cls := "form-control",
//        *.placeholder:="设备Mac").render
//
//      val rssiBox = input(
//        *.`type`:="text",
//        *.cls := "form-control",
//        *.placeholder:="rssi值").render
//
//
//      val successFunc: () => Unit = { () =>
//        val data = Json.obj(
//          ("shopId",shopId.asJson),
//          ("shopName",shopInfoComponent.shopNameLabel.innerHTML.asJson),
//          ("boxName",boxNameBox.value.asJson),
//          ("boxMac",boxMacBox.value.asJson),
//          ("rssi",rssiBox.value.asJson)
//        ).noSpaces
//
//        val url = Route.baseUrl + s"/shop/createShop"
//
//        Http.postJsonAndParse[CreateBoxRsp](url,data).map{
//          case Right(rsp) =>
//            if(rsp.errCode == 0){//重新刷新店铺列表
//              val boxs = boxesMap.map(_._2._1).toSeq :+ rsp.data.get
//              makeBoxesBuild(boxs)
//            }else{
//              println(s"添加盒子错误: ${rsp.msg}")
//              JsFunc.alert(s"添加盒子错误: msg=${rsp.msg}")
//            }
//          case Left(error) =>
//            println(s"添加盒子错误: $error")
//            JsFunc.alert(s"添加盒子错误: $error")
//        }
//      }
//
//      val modal = new Modal(
//        div(h2("添加设备")).render,
//        div(*.cls := "form-group")(
//          label(*.`for` := shopId.toString)("设备名称").render,
//          boxNameBox,
//          label(*.`for` := shopId.toString)("设备Mac").render,
//          boxMacBox,
//          label(*.`for` := shopId.toString)("设备Rssi过滤").render,
//          rssiBox
//        ).render,
//        successFunc,
//        shopId.toString
//      )
//
//      modalContainer.innerHTML = ""
//      modalContainer.appendChild(modal.render())
//      modal.show
//    }
//
//    val boxesTbody = tbody().render
//    val paginationContainer = div(*.textAlign:="center").render
//    val boxesTable = div(*.cls:="panel-body")(
//      table(*.cls:="table table-striped")(
//        colgroup(*.colspan:="5")(
//          col(*.cls:="box-table-name"),
//          col(*.cls:="box-table-mac"),
//          col(*.cls:="box-table-rssi"),
//          col(*.cls:="box-table-time"),
//          col(*.cls:="box-table-operator")
//        ),
//        thead()(
//          tr()(
//            th()(
//              "设备名称"
//            ),
//            th()(
//              "设备Mac"
//            ),
//            th()(
//              "Rssi过滤值"
//            ),
//            th()(
//              "创建时间"
//            ),
//            th()(
//              "操作"
//            )
//          )
//        ),
//        boxesTbody
//      ),paginationContainer
//    ).render
//
//    private [this] def insertBox(box:BoxInfo) = {
//      val newRow = tr(
//        td()(
//          box.boxName
//        ),
//        td()(
//          box.boxMac
//        ),
//        td()(
//          box.rssi
//        ),
//        td()(
//          MyUtil.dataFormatDefault(box.createTime)
//        ),
//        td(*.cls:="btn-group")(
//          button(*.cls:="btn btn-info",*.onclick:=showEditBoxNameModal(box.id,box.boxName))("编辑设备名称").render,
//          button(*.cls:="btn btn-danger",*.onclick:=showDeleteBoxModal(box.id,box.boxName))("解绑").render
//        )
//      ).render
//      boxesMap.put(box.id,(box,newRow))
//      boxesTbody.appendChild(newRow)
//    }
//
//    def makeBoxesBuild(boxes:Seq[BoxInfo]) = {
//      boxesTbody.innerHTML = ""
//      boxes.sortBy(_.createTime).map{ box =>
//        val newRow = tr(
//          td()(
//            box.boxName
//          ),
//          td()(
//            box.boxMac
//          ),
//          td()(
//            box.rssi
//          ),
//          td()(
//            MyUtil.dataFormatDefault(box.createTime)
//          ),
//          td(*.cls:="btn-group")(
//            button(*.cls:="btn btn-info",*.onclick:=showEditBoxNameModal(box.id,box.boxName))("编辑设备名称").render,
//            button(*.cls:="btn btn-danger",*.onclick:=showDeleteBoxModal(box.id,box.boxName))("解绑").render
//          )
//        ).render
//        boxesMap.put(box.id,(box,newRow))
////        boxesTbody.appendChild(newRow)
//      }
//      if(boxesMap.isEmpty){
//        val noData = div(*.cls:="shop-no-data")(
//          "暂无数据"
//        ).render
//        boxesTable.firstChild.appendChild(noData)
//      }
//
//      paginationContainer.innerHTML = ""
//      val pagination = new Pagination(boxesMap.size,pageSize,showPageContent)
//      paginationContainer.appendChild(pagination.render())
//    }
//
//    val showPageContent :Int =>Unit ={ x =>
//      boxesTbody.innerHTML = ""
//      val content = boxesMap.toList.drop((x-1)*pageSize).take(pageSize)
//      content.map{c =>
//        boxesTbody.appendChild(c._2._2)
//      }
//    }
//
//    private [this] def editBoxNameBuild(boxId:Long,boxName:String) = {
//      val valueOpt = boxesMap.get(boxId)
//      if(valueOpt.isDefined){
//        val value = valueOpt.get
//        val box = value._1.copy(boxName = boxName)
//        value._2.innerHTML = ""
//        value._2.appendChild(
//          td()(
//            boxName
//          ).render
//        )
//        value._2.appendChild(
//          td()(
//            value._1.boxMac
//          ).render
//        )
//        value._2.appendChild(
//          td()(
//            value._1.rssi
//          ).render
//        )
//        value._2.appendChild(
//          td()(
//            MyUtil.dataFormatDefault(value._1.createTime)
//          ).render
//        )
//        value._2.appendChild(
//          td(*.cls:="btn-group")(
//            button(*.cls:="btn btn-info",*.onclick:=showEditBoxNameModal(box.id,box.boxName))("编辑设备名称").render,
//            button(*.cls:="btn btn-danger",*.onclick:=showDeleteBoxModal(box.id,box.boxName))("解绑").render
//          ).render
//        )
//        boxesMap.put(boxId,(box,value._2))
//      }
//    }
//
//    private[this] def showEditBoxNameModal(id:Long,boxName:String): MouseEvent => Unit = { e: MouseEvent =>
//      e.preventDefault()
//      val modalInputBox = input(
//        *.`type` := "text",
//        *.cls := "form-control",
//        *.value:=boxName
//      ).render
//
//      val successFunc: () => Unit = { () =>
//        val newBoxName = modalInputBox.value
//
//        val url = Route.baseUrl + s"/shop/editBoxName?shopId=$shopId&boxId=$id&boxName=$newBoxName"
//
//        Http.getAndParse[CommonRsp](url).map{
//          case Right(rsp) =>
//            if(rsp.errCode == 0){
//              editBoxNameBuild(id,newBoxName)
//            }else{
//              println(s"编辑盒子名称: ${rsp.msg}")
//              JsFunc.alert(s"编辑盒子名称错误: msg=${rsp.msg}")
//            }
//          case Left(error) =>
//            println(s"编辑盒子名称错误:  $error")
//            JsFunc.alert(s"编辑盒子名称错误:  $error")
//        }
//      }
//
//      val modal = new Modal(
//        div(h2("编辑设备名称")).render,
//        div(*.cls := "form-group")(
//          label(*.`for` := shopId.toString)("设备名称").render,
//          modalInputBox
//        ).render,
//        successFunc,
//        shopId.toString
//      )
//
//      modalContainer.innerHTML = ""
//      modalContainer.appendChild(modal.render())
//      modal.show
//    }
//
//    private[this] def showDeleteBoxModal(id:Long,boxName:String):MouseEvent => Unit = { e: MouseEvent =>
//      e.preventDefault()
//
//
//      val successFunc: () => Unit = { () =>
//
//
//        val url = Route.baseUrl + s"/shop/boxUnbundling?shopId=$shopId&boxId=$id"
//
//        Http.getAndParse[CommonRsp](url).map{
//          case Right(rsp) =>
//            if(rsp.errCode == 0){
//              boxesMap.remove(id)
//              makeBoxesBuild(boxesMap.map(_._2._1).toSeq)
//            }else{
//              println(s"删除设备名称: ${rsp.msg}")
//              JsFunc.alert(s"删除设备名称错误: msg=${rsp.msg}")
//            }
//          case Left(error) =>
//            println(s"删除盒子设备错误:  $error")
//            JsFunc.alert(s"删除盒子设备错误:  $error")
//        }
//      }
//
//      val modal = new Modal(
//        div(h2("解绑设备")).render,
//        div(*.cls := "form-group")(
//          label(*.`for` := shopId.toString)(s"确认是否解绑该设备（$boxName）").render
//        ).render,
//        successFunc,
//        shopId.toString
//      )
//
//      modalContainer.innerHTML = ""
//      modalContainer.appendChild(modal.render())
//      modal.show
//    }
//
////    def addBoxBuild(box:BoxInfo) ={
////      val newRow = tr(
////        td()(
////          box.boxName
////        ),
////        td()(
////          box.boxMac
////        ),
////        td()(
////          box.rssi
////        ),
////        td()(
////          MyUtil.timeFormat(box.createTime)
////        ),
////        td()(
////          button()("修改设备名称").render,
////          button()("解绑").render
////        )
////      ).render
////      boxesMap.put(box.id,(box,newRow))
////      boxesTbody.appendChild(newRow)
////    }
//
//    override def render() = {
//      div(*.cls:="panel panel-default")(
//        boxesTitle,
//        boxesTable
//      ).render
//    }
//  }

//  object ObserversComponent extends Component[Div]{
//    import com.neo.sk.feeler3.ptcl.ShopProtocol.ShopModel._
//    val observersMap = new mutable.HashMap[Long,(Observer,Element)]()
//    val pageSize = 5
//    val observerTitle = div(*.cls:="panel-heading shop-panel-heading")(
//      span()(
//        "观察者列表"
//      ),
//      span(*.cls:="panel-heading__button")(
//        button(*.cls:="btn btn-primary",*.onclick:=getShareCode)("分享店铺").render
//      )
//    ).render
//
//    private[this] def getShareCode = {e : MouseEvent =>
//      e.preventDefault()
//      val url = Route.baseUrl + s"/shop/getShopShareCode?shopId=$shopId"
//
//      Http.getAndParse[GetShareCodeRsp](url).map{
//        case Right(rsp) =>
//          if(rsp.errCode == 0){//重新刷新店铺列表
//            showShareCodeModal(rsp.data.getOrElse(""))
//          }else{
//            println(s"获取分享码错误: ${rsp.msg}")
//            JsFunc.alert(s"获取分享码错误: msg=${rsp.msg}")
//          }
//        case Left(error) =>
//          println(s"获取分享码错误: $error")
//          JsFunc.alert(s"获取分享码错误: $error")
//      }
//    }
//
//    private[this] def showShareCodeModal(shareCode:String) :Unit = {
//      val successFunc: () => Unit = { () =>
//        println("")
//      }
//      val shareCodeBox = input(
//        *.id:="ur12",
//        *.value:=shareCode,
//        *.border:="none",
//        *.readonly:="readonly"
//      ).render
//
//      val copy = button(
//        *.id:="copy",
//        *.cls:="text-primary btn btn-link",
//        *.onclick:={e:MouseEvent =>
//          e.preventDefault()
//          shareCodeBox.select()
//          document.execCommand("Copy")
//          JsFunc.alert("复制成功")}
//      )(
//        "复制分享码"
//      ).render
//
//
//
//
//      val modal = new Modal(
//        div(h2("店铺分享码")).render,
//        div(*.cls := "form-group")(
//          label(*.`for` := shopId.toString)(shareCodeBox).render,
//          copy
//        ).render,
//        successFunc,
//        shopId.toString
//      )
//
//      modalContainer.innerHTML = ""
//      modalContainer.appendChild(modal.render())
//      modal.show
//    }
//
//
//
//    val observersTbody = tbody().render
//    val paginationContainer = div(*.textAlign:="center").render
//    val observerTable = div(*.cls:="panel-body")(
//      table(*.cls:="table table-striped")(
//        colgroup(*.colspan:="3")(
//          col(*.cls:="observer-table-nickname"),
//          col(*.cls:="observer-table-headImg"),
//          col(*.cls:="observer-table-operator")
//        ),
//        thead()(
//          tr()(
//            th()(
//              "昵称"
//            ),
//            th()(
//              "头像"
//            ),
//            th()(
//              "操作"
//            )
//          )
//        ),
//        observersTbody
//      ),
//      paginationContainer
//    ).render
//
//    val showPageContent :Int =>Unit ={ x =>
//      observersTbody.innerHTML = ""
//      val content = observersMap.toList.drop((x-1)*pageSize).take(pageSize)
//      content.map{c =>
//        observersTbody.appendChild(c._2._2)
//      }
//    }
//
//    def makeObserversBuild(observers:Seq[Observer]):Unit = {
//      observersTbody.innerHTML = ""
//      observers.map{ observer =>
//        val newRow = tr(*.cls:="observer-row")(
//          td()(
//            observer.nickname
//          ),
//          td()(
//            img(*.src:=observer.headImg,*.cls:="img-responsive observer-headImg")
//          ),
//          td()(
//            button(*.cls:="btn btn-danger",*.onclick:=showDeleteObserverModal(observer.id,observer.openId,observer.openId))("删除").render
//          )
//        ).render
//        observersMap.put(observer.id,(observer,newRow))
////        observersTbody.appendChild(newRow)
//      }
//      if(observersMap.isEmpty){
//        val noData = div(*.cls:="shop-no-data")(
//          "暂无数据"
//        ).render
//        observerTable.firstChild.appendChild(noData)
//      }
//      paginationContainer.innerHTML = ""
//      val pagination = new Pagination(observersMap.size,pageSize,showPageContent)
//      paginationContainer.appendChild(pagination.render())
//    }
//
//    private[this] def showDeleteObserverModal(id:Long,openId:String,nickname:String) = { e: MouseEvent =>
//      e.preventDefault()
//      val successFunc: () => Unit = { () =>
//        val url = Route.baseUrl + s"/shop/deleteObserver?shopId=$shopId&openId=$openId"
//
//        Http.getAndParse[CommonRsp](url).map{
//          case Right(rsp) =>
//            if(rsp.errCode == 0){
//              observersMap.remove(id)
//              makeObserversBuild(observersMap.map(_._2._1).toSeq)
//            }else{
//              println(s"删除观察者错误: ${rsp.msg}")
//              JsFunc.alert(s"删除观察者错误: msg=${rsp.msg}")
//            }
//          case Left(error) =>
//            println(s"删除观察者错误:  $error")
//            JsFunc.alert(s"删除观察者错误:  $error")
//        }
//      }
//
//      val modal = new Modal(
//        div(h2("删除观察者")).render,
//        div(*.cls := "form-group")(
//          label(*.`for` := shopId.toString)(s"确认是否删除该用户（$nickname）对本店的观察权限。").render
//        ).render,
//        successFunc,
//        shopId.toString
//      )
//
//      modalContainer.innerHTML = ""
//      modalContainer.appendChild(modal.render())
//      modal.show
//    }
//
//    override def render() = {
//      div(*.cls:="panel panel-default")(
//        observerTitle,
//        observerTable
//      ).render
//    }
//
//  }

  def getShopDetail :Future[Unit]= {
    val url = Route.baseUrl + s"/shop/shopDetail?shopId=$shopId"
    Http.getAndParse[GetShopDetailRsp](url).map{
      case Right(rsp) =>
        if(rsp.errCode == 0){
          val shopInfo = ShopInfo(rsp.data.get.id,rsp.data.get.shopName,rsp.data.get.createTime,rsp.data.get.groupId)
          shopInfoComponent.makeShopBuild(shopInfo)
          boxesComponent.makeBoxesBuild(rsp.data.get.boxs)
          observersComponent.makeObserversBuild(rsp.data.get.observers)
        }else{
          println(s"get shop list error: ${rsp.msg}")
          JsFunc.alert(s"get shop list error: msg=${rsp.msg}")
        }
      case Left(error) =>
        println(s"get shop list error:$error")
        JsFunc.alert(s"get shop list error:$error")
    }
  }

  val breadcrumb = ol(*.cls:="breadcrumb none-margin")(
    li()(
      a()(*.onclick:=switch2ShopList)(
        "店铺列表"
      )
    ),
    li(*.cls:="active")(
      "店铺详情"
    )
  )

  def switch2ShopList = {e:MouseEvent =>
    e.preventDefault()
    hide
    BusinessShopList.show
  }

  val page = div(*.id:=s"$shopId",*.cls:="shop-detail")(
    breadcrumb,
    shopInfoComponent.render(),
//    BoxesComponent.render(),
//    ObserversComponent.render(),
    modalContainer
  ).render

  def show : Unit = {
    page.setAttribute("style","display:block")
  }

  def hide : Unit = {
    page.setAttribute("style","display:none")
  }

  override def render() = {
    getShopDetail
    println(s"render business shop detail")
    page
  }

}
