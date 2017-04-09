package com.neo.sk.feeler3.frontend.business.panel

import com.neo.sk.feeler3.frontend.{Component, Route}
import com.neo.sk.feeler3.frontend.business.Pagination
import com.neo.sk.feeler3.frontend.utils.{Http, JsFunc, Modal, MyUtil}
import com.neo.sk.feeler3.ptcl.ShopProtocol.{CommonRsp, CreateBoxRsp}
import io.circe.Json
import org.scalajs.dom.MouseEvent
import org.scalajs.dom.html.{Div, Element}
import scala.collection.mutable
/**
  * Created by hongruying on 2017/3/30.
  */
class BoxesPanel(shopId:Long) extends Component[Div]{
  import com.neo.sk.feeler3.ptcl.ShopProtocol.ShopModel._
  import scalatags.JsDom.short._
  import io.circe.generic.auto._
  import io.circe.syntax._
  import scala.concurrent.ExecutionContext.Implicits.global

  val boxesMap = new mutable.HashMap[Long,(BoxInfo,Element)]()
  val pageSize = 5
  private val modalContainer = div().render
  val boxesTitle = div(*.cls:="panel-heading shop-panel-heading")(
    span()(
      "设备管理"
    ),
    span(*.cls:="panel-heading__button")(
      button(*.cls:="btn btn-primary",*.onclick:=showAddBoxModal)("添加").render
    )
  ).render

  private[this] def showAddBoxModal = { e: MouseEvent =>
    e.preventDefault()
    val boxNameBox = input(
      *.`type`:="text",
      *.cls := "form-control",
      *.placeholder:="设备名称").render

    val boxMacBox = input(
      *.`type`:="text",
      *.cls := "form-control",
      *.placeholder:="设备Mac").render

    val rssiBox = input(
      *.`type`:="number",
      *.cls := "form-control",
      *.placeholder:="Rssi过滤值").render

    def checkForm = {
      if(boxNameBox.value == ""){
        JsFunc.alert(s"设备名称不能为空")
        false
      }else if(boxMacBox.value == ""){
        JsFunc.alert(s"设备Mac不能为空")
        false
      }else if(rssiBox.value == ""){
        JsFunc.alert(s"设备Rssi值不为空")
        false
      }else{
        true
      }
    }

    val successFunc: () => Unit = { () =>
      if (checkForm) {
        val shopName = org.scalajs.dom.document.getElementById("shopNameLabel").innerHTML
        val data = Json.obj(
          ("shopId", shopId.asJson),
          ("shopName", shopName.asJson),
          ("boxName", boxNameBox.value.asJson),
          ("boxMac", boxMacBox.value.asJson),
          ("rssi", rssiBox.value.asJson)
        ).noSpaces

        val url = Route.baseUrl + s"/shop/createShop"

        Http.postJsonAndParse[CreateBoxRsp](url, data).map {
          case Right(rsp) =>
            if (rsp.errCode == 0) {
              //重新刷新店铺列表
              val boxs = boxesMap.map(_._2._1).toSeq :+ rsp.data.get
              makeBoxesBuild(boxs)
            } else {
              println(s"添加盒子错误: ${rsp.msg}")
              JsFunc.alert(s"添加盒子错误: msg=${rsp.msg}")
            }
          case Left(error) =>
            println(s"添加盒子错误: $error")
            JsFunc.alert(s"添加盒子错误: $error")
        }
      }
    }


    val modal = new Modal(
      div(h2("添加设备")).render,
      div(*.cls := "form-group")(
        label(*.`for` := shopId.toString,*.cls:="modal-label")("设备名称").render,
        boxNameBox,
        label(*.`for` := shopId.toString,*.cls:="modal-label")("设备Mac").render,
        boxMacBox,
        label(*.`for` := shopId.toString,*.cls:="modal-label")("设备Rssi过滤").render,
        rssiBox
      ).render,
      successFunc,
      shopId.toString
    )

    modalContainer.innerHTML = ""
    modalContainer.appendChild(modal.render())
    modal.show
  }

  val boxesTbody = tbody().render
  val paginationContainer = div(*.textAlign:="center").render
  val noDataDiv = div(*.textAlign:="center").render
  val boxesTable = div(*.cls:="panel-body")(
    table(*.cls:="table table-striped")(
      colgroup(*.colspan:="5")(
        col(*.cls:="box-table-name"),
        col(*.cls:="box-table-mac"),
        col(*.cls:="box-table-rssi"),
        col(*.cls:="box-table-time"),
        col(*.cls:="box-table-operator")
      ),
      thead()(
        tr()(
          th()(
            "设备名称"
          ),
          th()(
            "设备Mac"
          ),
          th()(
            "Rssi过滤值"
          ),
          th()(
            "创建时间"
          ),
          th()(
            "操作"
          )
        )
      ),
      boxesTbody
    ),
    noDataDiv,
    paginationContainer
  ).render

  private [this] def insertBox(box:BoxInfo) = {
    val newRow = tr(
      td()(
        box.boxName
      ),
      td()(
        box.boxMac
      ),
      td()(
        box.rssi
      ),
      td()(
        MyUtil.dataFormatDefault(box.createTime)
      ),
      td(*.cls:="btn-group")(
        button(*.cls:="btn btn-info",*.onclick:=showEditBoxNameModal(box.id,box.boxName))("编辑设备名称").render,
        button(*.cls:="btn btn-danger",*.onclick:=showDeleteBoxModal(box.id,box.boxName))("解绑").render
      )
    ).render
    boxesMap.put(box.id,(box,newRow))
    boxesTbody.appendChild(newRow)
  }

  def makeBoxesBuild(boxes:Seq[BoxInfo]) = {
    boxesMap.clear()
    boxesTbody.innerHTML = ""
    boxes.sortBy(_.createTime).map{ box =>
      val newRow = tr(
        td()(
          box.boxName
        ),
        td()(
          box.boxMac
        ),
        td()(
          box.rssi
        ),
        td()(
          MyUtil.dataFormatDefault(box.createTime)
        ),
        td(*.cls:="btn-group")(
          button(*.cls:="btn btn-info",*.onclick:=showEditBoxNameModal(box.id,box.boxName))("编辑设备名称").render,
          button(*.cls:="btn btn-danger",*.onclick:=showDeleteBoxModal(box.id,box.boxName))("解绑").render
        )
      ).render
      boxesMap.put(box.id,(box,newRow))
      //        boxesTbody.appendChild(newRow)
    }
    if(boxesMap.isEmpty){
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
    val pagination = new Pagination(boxesMap.size,pageSize,showPageContent)
    paginationContainer.appendChild(pagination.render())
  }

  val showPageContent :Int =>Unit ={ x =>
    boxesTbody.innerHTML = ""
    val content = boxesMap.toList.sortBy(_._2._1.createTime).drop((x-1)*pageSize).take(pageSize)
    content.map{c =>
      boxesTbody.appendChild(c._2._2)
    }
  }

  private [this] def editBoxNameBuild(boxId:Long,boxName:String) = {
    val valueOpt = boxesMap.get(boxId)
    if(valueOpt.isDefined){
      val value = valueOpt.get
      val box = value._1.copy(boxName = boxName)
      value._2.innerHTML = ""
      value._2.appendChild(
        td()(
          boxName
        ).render
      )
      value._2.appendChild(
        td()(
          value._1.boxMac
        ).render
      )
      value._2.appendChild(
        td()(
          value._1.rssi
        ).render
      )
      value._2.appendChild(
        td()(
          MyUtil.dataFormatDefault(value._1.createTime)
        ).render
      )
      value._2.appendChild(
        td(*.cls:="btn-group")(
          button(*.cls:="btn btn-info",*.onclick:=showEditBoxNameModal(box.id,box.boxName))("编辑设备名称").render,
          button(*.cls:="btn btn-danger",*.onclick:=showDeleteBoxModal(box.id,box.boxName))("解绑").render
        ).render
      )
      boxesMap.put(boxId,(box,value._2))
    }
  }

  private[this] def showEditBoxNameModal(id:Long,boxName:String): MouseEvent => Unit = { e: MouseEvent =>
    e.preventDefault()
    val modalInputBox = input(
      *.`type` := "text",
      *.cls := "form-control",
      *.value:=boxName
    ).render

    def checkForm = {
      if(modalInputBox.value == ""){
        JsFunc.alert(s"设备名称不能为空")
        false
      }else{
        true
      }
    }

    val successFunc: () => Unit = { () =>
      if(checkForm){
        val newBoxName = modalInputBox.value

        val url = Route.baseUrl + s"/shop/editBoxName?shopId=$shopId&boxId=$id&boxName=$newBoxName"

        Http.getAndParse[CommonRsp](url).map{
          case Right(rsp) =>
            if(rsp.errCode == 0){
              editBoxNameBuild(id,newBoxName)
            }else{
              println(s"编辑盒子名称: ${rsp.msg}")
              JsFunc.alert(s"编辑盒子名称错误: msg=${rsp.msg}")
            }
          case Left(error) =>
            println(s"编辑盒子名称错误:  $error")
            JsFunc.alert(s"编辑盒子名称错误:  $error")
        }
      }
    }

    val modal = new Modal(
      div(h2("编辑设备名称")).render,
      div(*.cls := "form-group")(
        label(*.`for` := shopId.toString,*.cls:="modal-label")("设备名称").render,
        modalInputBox
      ).render,
      successFunc,
      shopId.toString
    )

    modalContainer.innerHTML = ""
    modalContainer.appendChild(modal.render())
    modal.show
  }

  private[this] def showDeleteBoxModal(id:Long,boxName:String):MouseEvent => Unit = { e: MouseEvent =>
    e.preventDefault()


    val successFunc: () => Unit = { () =>


      val url = Route.baseUrl + s"/shop/boxUnbundling?shopId=$shopId&boxId=$id"

      Http.getAndParse[CommonRsp](url).map{
        case Right(rsp) =>
          if(rsp.errCode == 0){
            boxesMap.remove(id)
            makeBoxesBuild(boxesMap.map(_._2._1).toSeq)
          }else{
            println(s"删除设备名称: ${rsp.msg}")
            JsFunc.alert(s"删除设备名称错误: msg=${rsp.msg}")
          }
        case Left(error) =>
          println(s"删除盒子设备错误:  $error")
          JsFunc.alert(s"删除盒子设备错误:  $error")
      }
    }

    val modal = new Modal(
      div(h2("解绑设备")).render,
      div(*.cls := "form-group")(
        label(*.`for` := shopId.toString,*.cls:="modal-label")(s"确认是否解绑该设备（$boxName）").render
      ).render,
      successFunc,
      shopId.toString
    )

    modalContainer.innerHTML = ""
    modalContainer.appendChild(modal.render())
    modal.show
  }

  //    def addBoxBuild(box:BoxInfo) ={
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
  //          MyUtil.timeFormat(box.createTime)
  //        ),
  //        td()(
  //          button()("修改设备名称").render,
  //          button()("解绑").render
  //        )
  //      ).render
  //      boxesMap.put(box.id,(box,newRow))
  //      boxesTbody.appendChild(newRow)
  //    }

  override def render() = {
    div(*.cls:="panel panel-default")(
      boxesTitle,
      boxesTable,
      modalContainer
    ).render
  }


}
