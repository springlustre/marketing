package com.neo.sk.feeler3.frontend

/**
  * Created by springlustre on 2017/3/6.
  */

import com.neo.sk.feeler3.frontend.Route.AdminRoutes
import com.neo.sk.feeler3.frontend.utils.{Http, JsFunc}
import com.neo.sk.feeler3.ptcl.AdminProtocol._
import org.scalajs.dom
import org.scalajs.dom._
import org.scalajs.dom.html._

import scalatags.JsDom.short._

object UserList extends Component[Div]{
  import io.circe.generic.auto._
  import io.circe.syntax._

  import concurrent.ExecutionContext.Implicits.global

  val headBar = new HeadBar()

  val content = div(*.`class`:="content")(
    span(*.`class`:="content-left")(
      ul(*.`class`:="ul-cate")(
        li(*.`class`:="li-cate list-cate-active",*.onclick:=openCate("userlist"))(
          "用户列表"
        ),
        li(*.`class`:="li-cate",*.onclick:=openCate("shoplist"))(
          "店铺列表"
        )
      )
    )
  ).render


  val searchInput = input(*.`type`:="text",
    *.placeholder:="搜索用户")().render

  val firstPage = button(*.cls:="btn-unable")("首页").render
  val lastPage = button(*.cls:="btn-unable")("尾页").render
  val nextPage = button(*.cls:="btn-unable")("下一页").render
  val prePage = button(*.cls:="btn-unable")("上一页").render
  val curPage = span()("").render
  val inputPage = input(*.`type`:="text").render
  val jump = button(*.cls:="btn",*.onclick:=jumpOtherPage(-1))("跳转").render

  val mainBoard = div(*.`class`:="main-board")(
    div(*.`class`:="user-search")(
      searchInput,
      button(*.onclick:=searchUser)("搜索")
    ),
    div(*.`class`:="user-list-board")(
      table(*.`class`:="table-view")(
        thead()(
          tr()(
            th()("id"),
            th()("昵称"),
            th()("openId"),
            th()("头像")
          )
        ),
        tbody(*.id:="shopBody")(

        )
      ),
      div(*.`class`:="page-control-div")(
        firstPage,lastPage,prePage,nextPage,curPage,
        "跳转到",inputPage,"页",jump
      )
    )
  ).render


  override def render()= {
    println("render userList")
    getUserList
    div(
      headBar.render,
      content,
      mainBoard
    ).render
  }

  var total =0

  def jumpOtherPage(p:Int) = {event:MouseEvent =>
    if(p!=0) {
      val page = if (p < 1) {
        try {
          inputPage.value.trim.toInt
        } catch {
          case e: Exception =>
            -1
        }
      } else
        p

      if (page > total) {
        window.alert("不允许超过最大页数")
      } else if (page > 0) {
        val url = AdminRoutes.listUser + s"?page=$page&pageNum=8"
        Http.getAndParse[UserListRsp](url).map {
          case Right(rsp) =>
            if (rsp.errCode == 0) {
              total = rsp.totalPage
              insertUserList(rsp.data, rsp.page)
            } else {
              println(s"get list error: ${rsp.msg}")
            }
          case Left(error) =>
            println(s"get list error: $error")
            window.location.href = "login"
        }
      } else{
        window.alert("必须输入数字！")
      }
    }
  }


  def getUserList = {
    val url = AdminRoutes.listUser+"?page=1&pageNum=8"
    Http.getAndParse[UserListRsp](url).map {
      case Right(rsp) =>
        if (rsp.errCode == 0) {
          total = rsp.totalPage
          insertUserList(rsp.data,rsp.page)
        } else {
          println(s"get list error: ${rsp.msg}")
        }
      case Left(error) =>
        println(s"get list error: $error")
        window.location.href = "login"
    }
  }


  def insertUserList(list:Seq[User],page:Int):Unit = {
    val tBody = list.map{user=>
        tr(*.onclick:=openUserInfo(user.openId))(
          td()(user.id),
          td()(user.nickName),
          td()(user.openId),
          td()(img(*.src:=user.headImg,*.alt:="暂无"))
        )
      }.render
    document.getElementById("shopBody").innerHTML=""
    document.getElementById("shopBody").appendChild(tBody)
    curPage.innerHTML = s"当前第 $page/$total 页"

    if(total>1){
      if(page==total){
        lastPage.onclick = jumpOtherPage(0)
        lastPage.className = "btn-unable"
        nextPage.onclick = jumpOtherPage(0)
        nextPage.className = "btn-unable"
      }else {
        lastPage.onclick = jumpOtherPage(total)
        lastPage.className = "btn"
        nextPage.onclick = jumpOtherPage(page + 1)
        nextPage.className = "btn"
      }

      if(page>1){
        prePage.onclick = jumpOtherPage(page-1)
        prePage.className = "btn"
        firstPage.onclick = jumpOtherPage(1)
        firstPage.className = "btn"
      }else if(page==1){ //第一页
        firstPage.onclick = jumpOtherPage(0)
        prePage.onclick = jumpOtherPage(0)
        prePage.className = "btn-unable"
        firstPage.className = "btn-unable"
      }
    }
  }



  def searchUser = {event:MouseEvent =>
    event.preventDefault()
    val x = searchInput.value
    val url = AdminRoutes.searchUser+"?searchKey="+x
    Http.getAndParse[UserSearchRsp](url).map {
      case Right(rsp) =>
        if (rsp.errCode == 0) {
          insertUserList(rsp.data,-1)
        } else {
          println(s"get list error: ${rsp.msg}")
        }
      case Left(error) =>
        println(s"get list error: $error")
        window.location.href = "login"
    }
  }


  def openCate(url:String) = {event:MouseEvent =>
    event.preventDefault()
    window.location.href = url
  }

  def openUserInfo(openId:String) = {event:MouseEvent =>
    window.location.href = "userdetail?openId="+openId
  }


}
