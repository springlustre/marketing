package com.neo.sk.feeler3.frontend.business

import com.neo.sk.feeler3.frontend.Component
import com.neo.sk.feeler3.frontend.Route.UserRoutes
import com.neo.sk.feeler3.frontend.business.facede.chartjs2._
import com.neo.sk.feeler3.frontend.utils.{Http, JsFunc, MyUtil}
import com.neo.sk.feeler3.ptcl.ShopProtocol.ShopModel.ShopInfo
import com.neo.sk.feeler3.ptcl.ShopProtocol._
import com.neo.sk.feeler3.ptcl.UserProtocol._
import org.scalajs.dom._
import org.scalajs.dom.html._

import scala.scalajs.js
import scala.scalajs.js.Date
import scalatags.JsDom.short._

object databyWeek extends Component[Div] {
  import io.circe.generic.auto._
  import org.scalajs.dom.html._
  import org.scalajs.dom.raw._

  import concurrent.ExecutionContext.Implicits.global
  val textMap = Map(
    1 -> "1-10min",
    2 ->"10-30min",
    3 -> "30min-1h",
    4 -> "1h-2h",
    5 -> "2h以上"
  )
/**
  * shopList
  * */
  def getShopList = {
    val url = UserRoutes.listShop
    Http.getAndParse[GetShopListRsp](url).map {
      case Right(rsp) =>
        if (rsp.errCode == 0) {
          insertShopList(rsp.data.get.merchantShop)
          insertShopList(rsp.data.get.observedShop)
        } else {
          println(s"get list error: ${rsp.msg}")
        }
      case Left(error) =>
        println(s"get list error: $error")
    }
  }
  def insertShopList(list:Seq[ShopInfo]) = {
    println("shop")
    val tBody =
      list.map{shop=>
        if(shop.groupId.toString==window.localStorage.getItem("shopId")){
          a(*.onclick:=selectShop(shop.groupId,shop.shopName),*.id:=shop.groupId,*.cls:="floor-bar-active")(
            shop.shopName
          )
        }
        else{
          a(*.onclick:=selectShop(shop.groupId,shop.shopName),*.id:=shop.groupId,*.cls:="floor-bar-item")(
            shop.shopName
          )
        }
      }.render
    shopbody.appendChild(tBody)
  }

  def selectShop(id:Long,shopName:String)={event:MouseEvent =>
    val oldId =window.localStorage.getItem("shopId")
    document.getElementById(oldId).setAttribute("class","floor-bar-item")
    document.getElementById(s"$id").setAttribute("class","floor-bar-active")
    window.localStorage.setItem("shopId",id.toString)
    val first = MyUtil.DateFormatter(MyUtil.WeekFormatter(new Date(datePick.value)).head,"YYYY-MM-DD")
    val last = MyUtil.DateFormatter(MyUtil.WeekFormatter(new Date(datePick.value))(1),"YYYY-MM-DD")
    updateFlowDetail(id.toString,first,last)
    updateAverageTime(id.toString,first,last)
    updateResidentTime(id.toString,first+"-"+last)
    updateRatio(id.toString,first,last)
    updateGuestConversion(id.toString,first,last)
  }

  val shopbody = span(*.id:="shopList")().render
  val shopList = div(*.cls:="floor-bar")(
      "店铺列表:",
      shopbody
    ).render
  /**
    * option
    * */
  val datePick = input(*.id := "Date",*.cls:="Wdate",*.height:="auto", *.`type` := "text",*.onclick:="WdatePicker({maxDate:'%y-%M-%d'})",*.readonly:="readonly").render
  val ensure = button(*.onclick:=pick,*.border:="none",*.background:="#fff",*.color:="#3782ff",*.marginRight:="20px")("确认").render
  val yesterday = button(*.cls:="btn btn-primary",*.id:="fore",*.onclick:=yest)("前一周").render
  val tomorrow = button(*.cls:="btn btn-primary",*.id:="next",*.onclick:=tomo)("后一周").render
  val condition = div(*.cls:="options")(
    "时间：",
    datePick,
    ensure,
    yesterday,
    tomorrow
  ).render

  def pick = { event:MouseEvent =>
      val id =window.localStorage.getItem("shopId")
      val date = datePick.value
      val nowdate = new Date().getTime()
      if(new Date(datePick.value).getTime()>nowdate){
        JsFunc.alert("日期不应超过"+MyUtil.DateFormatter( new Date(nowdate), "YYYY-MM-DD" ))
      }
      else {
        val first = MyUtil.DateFormatter(MyUtil.WeekFormatter(new Date(date)).head,"YYYY-MM-DD")
        val last = MyUtil.DateFormatter(MyUtil.WeekFormatter(new Date(date))(1),"YYYY-MM-DD")
        updateFlowDetail(id.toString,first,last)
        updateAverageTime(id.toString,first,last)
        updateResidentTime(id.toString,first+"-"+last)
        updateRatio(id.toString,first,last)
        updateGuestConversion(id.toString,first,last)
      }
  }

  def yest={event:MouseEvent =>
    val id =window.localStorage.getItem("shopId")
    val timestamp = new Date(datePick.value).getTime()
    val yes = MyUtil.DateFormatter(new Date(timestamp-86400000*7),"YYYY-MM-DD")
    datePick.value = yes
    val first = MyUtil.DateFormatter(MyUtil.WeekFormatter(new Date(yes)).head,"YYYY-MM-DD")
    val last = MyUtil.DateFormatter(MyUtil.WeekFormatter(new Date(yes))(1),"YYYY-MM-DD")
    updateFlowDetail(id.toString,first,last)
    updateAverageTime(id.toString,first,last)
    updateResidentTime(id.toString,first+"-"+last)
    updateRatio(id.toString,first,last)
    updateGuestConversion(id.toString,first,last)
  }
  def tomo={event:MouseEvent =>
    val id =window.localStorage.getItem("shopId")
    val nowdate = new Date().getTime()
    if(new Date(datePick.value).getTime()+86400000*7>nowdate){
      JsFunc.alert("不可超过当前日期")
    }
    else{
      val timestamp = new Date(datePick.value).getTime()+86400000*7
      val to = MyUtil.DateFormatter(new Date(timestamp),"YYYY-MM-DD")
      datePick.value = to
      val first = MyUtil.DateFormatter(MyUtil.WeekFormatter(new Date(to)).head,"YYYY-MM-DD")
      val last = MyUtil.DateFormatter(MyUtil.WeekFormatter(new Date(to))(1),"YYYY-MM-DD")
      updateFlowDetail(id.toString,first,last)
      updateAverageTime(id.toString,first,last)
      updateResidentTime(id.toString,first+"-"+last)
      updateRatio(id.toString,first,last)
      updateGuestConversion(id.toString,first,last)
    }
  }
  /**
    * chart
    * */
  val realTimeChartIn = canvas(*.width := "500").render
  val realTimeChartOut = canvas(*.width := "500").render
  val averageTimeChart = canvas(*.width := "500").render
  val residentTimeChart = canvas(*.width := "500").render
  val residentPeopelChart = canvas(*.width := "500").render
  val ratioChart = canvas(*.width := "500").render
  val conversionChart = canvas(*.width := "500").render
  //给每个表格一个Instance 方便destroy
  import scala.Option
  private var realTimeInstance: Option[Chart] = None
  private var realTimeOutInstance: Option[Chart] = None
  private var residentTimeInstance: Option[Chart] = None
  private var residentPeopleInstance: Option[Chart] = None
  private var averageInstance: Option[Chart] = None
  private var conversionInstance: Option[Chart] = None
  private var ratioInstance: Option[Chart] = None
  //data
  private var LineData = List.empty[TimeDot]
  private var residentTimeData = List.empty[Flag]
  private var conversionData = List.empty[GuestConversionDot]
  private var ratioData = List.empty[RatioDot]
  //draw
  private def drawChart(area: Canvas,data:List[TimeDot],`type`:String,label:String,rank:Int) = {
    import com.neo.sk.feeler3.frontend.business.facede.chartjs2._

    import js.JSConverters._
    println("drawChart")
    val ctx = area.getContext("2d").asInstanceOf[CanvasRenderingContext2D]
    var (xs, ys) = data.sortBy(_.timestamp).map(d => (MyUtil.DateFormatter(new Date(d.timestamp),`type`), d.value.toDouble)).unzip
    if(rank==3) {
      ys = data.sortBy(_.timestamp).map(d => "%1.2f".format(d.value.toDouble / 60).toDouble)
    }
    val dataSet = new LineDataSet(data = ys.toJSArray,label = label)
    val chartData = new ChartData(xs.toJSArray, js.Array(dataSet))
    rank match{
      case 1 =>
        realTimeInstance.foreach(_.destroy())
        realTimeInstance = Some( new Chart(ctx, new ChartConfig("line", chartData,null)))
      case 2 =>
        realTimeOutInstance.foreach(_.destroy())
        realTimeOutInstance = Some( new Chart(ctx, new ChartConfig("line", chartData,null)))
      case 3 =>
        averageInstance.foreach(_.destroy())
        averageInstance = Some( new Chart(ctx, new ChartConfig("line", chartData,null)))
    }
  }

  private def drawbarChart(area: Canvas,data:List[Flag],`type`:String,rank:Int) = {

    import js.JSConverters._
    println("drawbarChart")
    val ctx = area.getContext("2d").asInstanceOf[CanvasRenderingContext2D]
    val (xs, ys) = data.sortBy(_.flag).map(d => (textMap(d.flag), d.value.toInt)).unzip
    val dataSet = new BarDataSet(data = ys.toJSArray,label=`type`)
    val chartData = new ChartData(xs.toJSArray, js.Array(dataSet))
    rank match{
      case 1 =>
        residentTimeInstance.foreach(_.destroy())
        residentTimeInstance = Some(  new Chart(ctx, new ChartConfig("horizontalBar", chartData,null)))
      case 2 =>
        residentPeopleInstance.foreach(_.destroy())
        residentPeopleInstance = Some(  new Chart(ctx, new ChartConfig("horizontalBar", chartData,null)))
    }
  }

  private def drawRatioChart(area: Canvas,data:List[RatioDot],name:Array[String]) = {
    import js.JSConverters._
    val ctx = area.getContext("2d").asInstanceOf[CanvasRenderingContext2D]
    val xs = data.map(d=> MyUtil.DateFormatter(new Date(d.timestamp),"MM-DD"))
    val ys1 = data.map(d=>{
      if(d.newCount==0)
        "0"
      else
        "%1.2f".format(d.newCount*100/(d.newCount+d.oldCount).toDouble)
    })
    val ys2 = data.map(d=>{
      if(d.oldCount==0)
        "0"
      else
        "%1.2f".format(d.oldCount*100/(d.newCount+d.oldCount).toDouble)
    })
    val dataSet1 = new DoubleBarData(data = ys1.toJSArray,label = name.head,backgroundColor = "#3D86FF",borderColor = "#3D86FF")
    val dataSet2 = new DoubleBarData(data = ys2.toJSArray,label = name(1),backgroundColor = "#4AEC54",borderColor = "#4AEC54")
    val chartData = new ChartData(xs.toJSArray, js.Array(dataSet1,dataSet2))
    ratioInstance.foreach(_.destroy())
    ratioInstance = Some(new Chart(ctx, new ChartConfig("bar", chartData,null)))
  }

  private def drawDoubleBarChart(area: Canvas,data:List[GuestConversionDot],name:Array[String]) = {
    import js.JSConverters._
    val ctx = area.getContext("2d").asInstanceOf[CanvasRenderingContext2D]
    val xs = data.map(d=> MyUtil.DateFormatter(new Date(d.timestamp),"MM-DD"))
    val ys1 = data.map(d=>{
                if(d.custIn==0)
                    "0"
                else
                  "%1.2f".format(d.custIn*100/(d.custIn+d.custOut).toDouble)
    })
    val ys2 = data.map(d=>{
          if(d.custOut==0)
            "0"
          else
            "%1.2f".format(d.custOut*100/(d.custIn+d.custOut).toDouble)
    })
    val dataSet1 = new DoubleBarData(data = ys1.toJSArray,label = name.head,backgroundColor = "#3D86FF",borderColor = "#3D86FF")
    val dataSet2 = new DoubleBarData(data = ys2.toJSArray,label = name(1),backgroundColor = "#4AEC54",borderColor = "#4AEC54")
    val chartData = new ChartData(xs.toJSArray, js.Array(dataSet1,dataSet2))
    conversionInstance.foreach(_.destroy())
    conversionInstance = Some( new Chart(ctx, new ChartConfig("bar", chartData,null)))
  }

  private def updateFlowDetail(id: String,startDate:String,endDate:String) = {
    val url = UserRoutes.flowDetailByDate(id,startDate,endDate)
    Http.getAndParse[FlowDetailRsp](url).foreach {
      case Right(rsp) =>
        if (rsp.errCode == 0) {
          rsp.data.filter(_.behavior == "in").foreach( d =>{
            LineData=d.dots
          })
          drawChart(realTimeChartIn,LineData,"MM-DD","顾客 进店",1)
          rsp.data.filter(_.behavior == "out").foreach( d =>{
            LineData=d.dots
          })
          drawChart(realTimeChartOut,LineData,"MM-DD","顾客 穿行",2)
        } else {
          JsFunc.alert(s"some error: ${rsp.msg}")
        }
      case Left(err) =>
        JsFunc.alert(s"internal error: ${err.getMessage}")
    }
  }

  private def updateAverageTime(id: String,startDate:String,endDate:String) = {
    val url = UserRoutes.averageTime(id,startDate,endDate)
    Http.getAndParse[AverageRsp](url).foreach {
      case Right(rsp) =>
        if (rsp.errCode == 0) {
          LineData = rsp.data
          drawChart(averageTimeChart,LineData,"MM-DD","平均时长(分)",3)
        } else {
          JsFunc.alert(s"some error: ${rsp.msg}")
        }
      case Left(err) =>
        JsFunc.alert(s"internal error: ${err.getMessage}")
    }
  }


  private def updateResidentTime(id: String,date:String) = {
    val url = UserRoutes.residentByDate(id,date)
    Http.getAndParse[ResidentTimeRsp](url).foreach {
      case Right(rsp) =>
        if (rsp.errCode == 0) {
          rsp.data.filter(_.status == "personTime").foreach( d =>{
            residentTimeData=d.dots
          })
          drawbarChart(residentTimeChart,residentTimeData,"人次",1)
          rsp.data.filter(_.status == "person").foreach( d =>{
            residentTimeData=d.dots
          })
          drawbarChart(residentPeopelChart,residentTimeData,"人",2)
        } else {
          JsFunc.alert(s"some error: ${rsp.msg}")
        }
      case Left(err) =>
        JsFunc.alert(s"internal error: ${err.getMessage}")
    }
  }

  private def updateRatio(id: String,startDate:String,endDate:String) = {
    val url = UserRoutes.ratio(id,startDate,endDate)
    Http.getAndParse[RatioRsp](url).foreach {
      case Right(rsp) =>
        if (rsp.errCode == 0) {
          ratioData = rsp.data
          drawRatioChart(ratioChart,ratioData,Array[String]("新顾客比","老顾客比"))
        } else {
          JsFunc.alert(s"some error: ${rsp.msg}")
        }
      case Left(err) =>
        JsFunc.alert(s"internal error: ${err.getMessage}")
    }
  }

  private def updateGuestConversion(id: String,startDate:String,endDate:String) = {
    val url = UserRoutes.conversionByDate(id,startDate,endDate)
    Http.getAndParse[ConversionRsp](url).foreach {
      case Right(rsp) =>
        if (rsp.errCode == 0) {
          conversionData = rsp.data
          drawDoubleBarChart(conversionChart,conversionData,Array[String]("进入比率","穿行比率"))
        } else {
          JsFunc.alert(s"some error: ${rsp.msg}")
        }
      case Left(err) =>
        JsFunc.alert(s"internal error: ${err.getMessage}")
    }
  }

  private def todayDate = MyUtil.DateFormatter(new Date(),"YYYY-MM-DD")

  override def render() = {
    datePick.value = todayDate
    if(window.localStorage.getItem("shopId")==null){
      window.localStorage.setItem("shopId","2100001")
    }
    while(shopbody.hasChildNodes()){
      shopbody.removeChild(shopbody.firstChild)
    }
    getShopList
    val first = MyUtil.DateFormatter(MyUtil.WeekFormatter(new Date(todayDate)).head,"YYYY-MM-DD")
    val last = MyUtil.DateFormatter(MyUtil.WeekFormatter(new Date(todayDate))(1),"YYYY-MM-DD")
    updateFlowDetail(window.localStorage.getItem("shopId").toString,first,last)
    updateAverageTime(window.localStorage.getItem("shopId").toString,first,last)
    updateResidentTime(window.localStorage.getItem("shopId").toString,first+"-"+last)
    updateRatio(window.localStorage.getItem("shopId").toString,first,last)
    updateGuestConversion(window.localStorage.getItem("shopId").toString,first,last)
    div(*.cls:="passenger-flow-page")(
      div(*.cls:="top-panel")(
        shopList,
        condition
      ),
      div(*.cls:="col-back")(
      h1(*.cls:="t1")("实时客流图表"),
      h4(*.cls:="subtitle")("30分钟/每次"),
      div(*.cls:="in")("进店"),
      realTimeChartIn,
      div(*.cls:="out")("穿行"),
      realTimeChartOut,
      h1(*.cls:="t1")("平均驻留时长"),
      averageTimeChart,
      h1(*.cls:="t1")("驻留时长分布"),
      div(*.cls:="in")("按次统计"),
      residentTimeChart,
      div(*.cls:="in")("按人统计"),
      residentPeopelChart,
      h1(*.cls:="t1")("新老顾客比"),
      ratioChart,
      h1(*.cls:="t1")("人客转换率"),
      conversionChart
      )
    ).render
  }
}
