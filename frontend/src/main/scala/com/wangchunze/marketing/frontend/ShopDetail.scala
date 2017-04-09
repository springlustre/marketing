package com.neo.sk.feeler3.frontend

/**
  * Created by springlustre on 2017/3/7.
  */


import com.neo.sk.feeler3.frontend.Route.AdminRoutes
import com.neo.sk.feeler3.frontend.utils.{Http, JsFunc}
import com.neo.sk.feeler3.ptcl.AdminProtocol._
import org.scalajs.dom._
import org.scalajs.dom.html._

import scalatags.JsDom.short._
import com.neo.sk.feeler3.frontend.utils.MyUtil._
import com.neo.sk.feeler3.ptcl._

object ShopDetail extends Component[Div] {
  import io.circe.generic.auto._
  import io.circe.syntax._
  import concurrent.ExecutionContext.Implicits.global

  val headBar = new HeadBar()

  val content = div(*.`class`:="content")(
    span(*.`class`:="content-left")(
      ul(*.`class`:="ul-cate")(
        li(*.`class`:="li-cate",*.onclick:=openCate("userlist"))(
          "用户列表"
        ),
        li(*.`class`:="li-cate",*.onclick:=openCate("shoplist"))(
          "店铺列表"
        )
      )
    )
  ).render


  val headImg = img(*.verticalAlign:="middle",*.width:="2rem").render
  val nickName = span(*.padding:="0 0.2rem")().render
  val userInfo = div(*.`class`:="user-search",*.cursor:="pointer")(
    headImg,
    nickName
  ).render

  val shopName = a()().render

  val createBtn = button()("绑定设备").render
  createBtn.onclick = openCreateBoard

  val bindBtn = button()("添加观察者").render
  bindBtn.onclick = openBindBoard
  val mainBoard = div(*.`class`:="main-board")(
    userInfo,
    div(*.`class`:="shop-title")(
      shopName,
      span(*.onclick:="window.location.href='#boxDiv'")("设备列表"),
      span(*.onclick:="window.location.href='#observerDiv'")("角色列表"),
      createBtn,
      bindBtn
    ),
    //设备列表dom
    div(*.`class`:="user-list-board",*.id:="boxDiv")(
      div(*.`class`:="table-title")("店铺设备"),
      table(*.`class`:="table-view",*.id:="shopTable")(
        thead()(
          tr()(
            th()("id"),
            th()("设备名称"),
            th()("mac"),
            th()("创建时间"),
            th()("操作")
          )
        ),
        tbody(*.id:="boxBody")(

        )
      ),
      div(*.`class`:="page-control-div")(

      )
    ),

    //观察者列表dom
    div(*.`class`:="user-list-board",*.id:="observerDiv")(
      div(*.`class`:="table-title")("观察者列表"),
      table(*.`class`:="table-view",*.id:="shopTable")(
        thead()(
          tr()(
            th()("id"),
            th()("昵称"),
            th()("openId"),
            th()("头像"),
            th()("创建时间"),
            th()("操作")
          )
        ),
        tbody(*.id:="observerBody")(

        )
      ),
      div(*.`class`:="page-control-div")(

      )
    )
  ).render

  val mainBody = div()(
    headBar.render,
    content,
    mainBoard
  ).render

  val cancelBtn = button(*.`class`:="cancel")("取消").render
  cancelBtn.onclick=closeCreateBoard
  val okBtn = button(*.`class`:="ok")("确定").render
  val boxName = input(*.`type`:="text").render
  val boxMac = input(*.`type`:="text").render

  val createBoard = div(*.`class`:="create-board",*.id:="createBoard")(
    div(*.`class`:="board-title")("创建设备"),
    div(*.`class`:="board-content")(
      div()(
        label()("设备mac"),
        boxMac
      ),
      div()(
        label()("设备名称"),
        boxName
      )
    ),
    div(*.`class`:="board-footer")(
      okBtn,
      cancelBtn
    )
  ).render

  val obCancelBtn = button(*.`class`:="cancel")("取消").render
  obCancelBtn.onclick=closeBindBoard
  val obOkBtn = button(*.`class`:="ok")("确定").render
  val obSearchBtn = button(*.cls:="searchBtn")("搜索").render
  val observerName = input(*.`type`:="text").render
  val tipsDiv = div(*.cls:="tips-div-hide")().render
  val bindBoard = div(*.`class`:="create-board",*.id:="bindBoard")(
    div(*.`class`:="board-title")("绑定观察者"),
    div(*.`class`:="board-content")(
      div()(
        label()("搜索用户"),
        observerName,
        obSearchBtn,
        tipsDiv
      )
    ),
    div(*.`class`:="board-footer")(
      obOkBtn,
      obCancelBtn
    )
  ).render



  override def render()= {
    println("render user detail")
    firstGetInfo
    div(
      mainBody,
      createBoard,
      bindBoard
    ).render
  }


  def openCreateBoard={event:MouseEvent =>
    createBoard.className+=" create-board-show"
    mainBody.className+="cover"
  }

  def closeCreateBoard = {event:MouseEvent =>
    event.preventDefault()
    createBoard.className="create-board"
    mainBody.className=""
  }

  def openBindBoard={event:MouseEvent =>
    event.preventDefault()
    bindBoard.className += " create-board-show"
    mainBody.className += "cover"
  }

  def closeBindBoard = {event:MouseEvent =>
    event.preventDefault()
    tipsDiv.innerHTML=""
    observerName.innerHTML=""
    bindBoard.className="create-board"
    mainBody.className=""
  }


  def firstGetInfo = {
    GetQueryString("shopId") match {
      case Some(shopId) =>
        try{
          val id = shopId.toLong
          getObserverList(id)
          getBoxList(id)
          getUserInfo(id)
          okBtn.onclick = addBox(id)
          obSearchBtn.onclick = searchUser(id)
          observerName.oninput = searchUser(id)
        }catch{
          case e:Exception=>
          println(s"GetQueryString shopId exception:$e")
        }
      case None =>
        println("GetQueryString shopId not exist")
    }

    GetQueryString("shopName") match {
      case Some(name) =>
        insertShopName(name)
      case None =>
        println("GetQueryString shopName not exist")
    }
  }

  def getUserInfo(shopId:Long) = {
    val url = AdminRoutes.getShopOwner+"?shopId="+shopId
    Http.getAndParse[UserInfoRsp](url).map {
      case Right(rsp) =>
        if (rsp.errCode == 0) {
          insertUserInfo(rsp.data)
        } else {
          println(s"get list error: ${rsp.msg}")
        }
      case Left(error) =>
        println(s"get list error: $error")
        window.location.href = "login"
    }
  }

  def insertUserInfo(user:User) = {
    headImg.src = user.headImg
    nickName.innerHTML = user.nickName
    userInfo.onclick = openUserInfo(user.openId)
  }

  def insertShopName(name:String) = {
    shopName.innerHTML = name
  }

  //获取设备列表数据
  def getBoxList(shopId:Long) = {
    val url = AdminRoutes.listBox+"?shopId="+shopId
    Http.getAndParse[ListBoxRsp](url).map {
      case Right(rsp) =>
        if (rsp.errCode == 0) {
          insertBoxList(rsp.data,shopId)
        } else {
          println(s"get list error: ${rsp.msg}")
        }
      case Left(error) =>
        println(s"get list error: $error")
        window.location.href = "login"
    }
  }

  //生成设备列表dom
  def insertBoxList(list:Seq[BoxInfo],shopId:Long) = {
    val tBody =
      list.map{x=>
        tr(*.id:="box"+x.id)(
          td()(x.id),
          td()(x.name),
          td()(x.mac),
          td()(timeFormat(x.createTime)),
          td()(button(*.onclick:=unbindBox(shopId,x.id))("删除"))
        )
      }.render

//    document.getElementById("boxBody").innerHTML=""
    document.getElementById("boxBody").appendChild(tBody)
  }

  //获取观察者数据
  def getObserverList(shopId:Long) = {
    val url = AdminRoutes.listObserver+"?shopId="+shopId
    Http.getAndParse[ListObserverRsp](url).map {
      case Right(rsp) =>
        if (rsp.errCode == 0) {
          insertObserverList(rsp.data,shopId)
        } else {
          println(s"get list error: ${rsp.msg}")
        }
      case Left(error) =>
        println(s"get list error: $error")
        window.location.href = "login"
    }
  }

  //生成观察者dom
  def insertObserverList(list:Seq[Observer],shopId:Long) = {
    val tBody =
      list.map{x=>
        tr(*.id:=s"observer${x.openId}")(
          td()(x.userId),
          td()(x.nickName),
          td()(x.openId),
          td()(img(*.width:="2rem",*.src:=x.headImg)),
          td()(timeFormat(x.createTime)),
          td()(button(*.onclick:=deleteObserver(shopId,x.openId))("删除"))
        )
      }.render

//    document.getElementById("observerBody").innerHTML=""
    document.getElementById("observerBody").appendChild(tBody)
  }


  //添加设备操作
  def addBox(shopId:Long) = {event:MouseEvent =>
    event.preventDefault()
    val name = boxName.value.trim()
    val mac = boxMac.value.trim()
    val url = Route.AdminRoutes.addBox
    val data = BindBoxRst(mac,name,shopId).asJson.noSpaces
    Http.postJsonAndParse[ListBoxRsp](url, data).map {
      case Right(rsp) =>
        if (rsp.errCode == 0) {
          insertBoxList(rsp.data,shopId)
          cancelBtn.click()
        } else {
          println(s"error: ${rsp.msg}")
          JsFunc.alert(s"error: ${rsp.msg}")
        }
      case Left(error) => println(s"get list error: $error")
        window.location.href = "login"
    }
  }

  //解绑设备操作
  def unbindBox(shopId:Long,boxId:Long) = {event:MouseEvent =>
    event.preventDefault()
    val url = Route.AdminRoutes.unbindBox
    val data = UnbindBoxRst(shopId,boxId).asJson.noSpaces
    Http.postJsonAndParse[SuccessResponse](url, data).map {
      case Right(rsp) =>
        if (rsp.errCode == 0) {
          val node = document.getElementById(s"box$boxId")
          document.getElementById("boxBody").removeChild(node)
        } else {
          println(s"error: ${rsp.msg}")
          JsFunc.alert(s"error: ${rsp.msg}")
        }
      case Left(error) => println(s"get list error: $error")
        window.location.href = "login"
    }
  }

  //添加观察者时搜索用户
  def searchUser(shopId:Long) = {event:Event =>
    event.preventDefault()
    val searchKey = observerName.value
    val url = Route.AdminRoutes.searchUser + "?searchKey=" + searchKey
    Http.getAndParse[UserSearchRsp](url).map {
      case Right(rsp) =>
        if (rsp.errCode == 0) {
          val res = rsp.data.take(10)
          insertSearchResult(res,shopId)
        } else {
          println(s"error: ${rsp.msg}")
          JsFunc.alert(s"error: ${rsp.msg}")
        }
      case Left(error) => println(s"get list error: $error")
        window.location.href = "login"
    }
  }
  // 处理用户搜索结果
  def insertSearchResult(data:Seq[User],shopId:Long) = {
    tipsDiv.innerHTML = ""
    val tipsLine = data.map{x =>
      div(*.cls:="tips-line",*.onclick:=selectUser(x,shopId))(
        span()(img(*.src:=x.headImg,*.cls:="search-img")),
        span(*.cls:="search-name")(x.nickName),
        span()(x.openId)
      )
    }.render
    tipsDiv.appendChild(tipsLine)
    tipsDiv.className = "tips-div-show"
  }

  //选择用户
  def selectUser(user:User,shopId:Long) = {event:MouseEvent =>
    event.preventDefault()
    tipsDiv.innerHTML = ""
    observerName.value=""
    val u = div(*.cls:="tips-line")(
      span()(img(*.src:=user.headImg,*.cls:="search-img")),
      span(*.cls:="search-name")(user.nickName),
      span()(user.openId)
    ).render
    tipsDiv.appendChild(u)
    tipsDiv.applyTags("点击确定将添加该用户为观察者")
    obOkBtn.onclick = addObserver(shopId,user)
  }

  //添加观察者请求
  def addObserver(shopId:Long,user:User) = {event:MouseEvent =>
    event.preventDefault()
    val url = Route.AdminRoutes.addObserver
    val data = AddObserverRst(shopId,user.openId).asJson.noSpaces
    Http.postJsonAndParse[SuccessResponse](url, data).map {
      case Right(rsp) =>
        if (rsp.errCode == 0) {
          val now = new scala.scalajs.js.Date().getTime().toLong
          val data = Observer(user.id,user.openId,user.nickName,user.headImg,now)
          insertObserverList(Seq(data),shopId)
          obCancelBtn.click()
        } else {
          println(s"error: ${rsp.msg}")
          JsFunc.alert(s"error: ${rsp.msg}")
        }
      case Left(error) => println(s"get list error: $error")
        window.location.href = "login"
    }
  }

  //删除观察者
  def deleteObserver(shopId:Long,openId:String) = {event:MouseEvent =>
    event.preventDefault()
    val url = Route.AdminRoutes.delObserver
    val data = DeleteObserverRst(shopId,openId).asJson.noSpaces
    Http.postJsonAndParse[SuccessResponse](url, data).map {
      case Right(rsp) =>
        if (rsp.errCode == 0) {
          val node = document.getElementById(s"observer$openId")
          document.getElementById("observerBody").removeChild(node)
        } else {
          println(s"error: ${rsp.msg}")
          JsFunc.alert(s"error: ${rsp.msg}")
        }
      case Left(error) => println(s"get list error: $error")
        window.location.href = "login"
    }
  }


  def openUserInfo(openId:String) = {event:MouseEvent =>
    window.location.href = "userdetail?openId="+openId
  }

  def openCate(url:String) = {event:MouseEvent =>
    event.preventDefault()
    window.location.href = url
  }



}
