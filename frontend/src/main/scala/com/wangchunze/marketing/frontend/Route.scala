package com.neo.sk.feeler3.frontend

/**
  * User: Taoz
  * Date: 1/16/2017
  * Time: 6:49 PM
  */
object Route {


  val baseUrl = "/feeleriii"

  object CounterRoute{
    val counterRoot = baseUrl + "/counter"
    val get = counterRoot + "/get"
    val plus = counterRoot + "/plus"
    val minus = counterRoot + "/minus"
  }

  object UserRoutes{
    val userRoot = baseUrl + "/user"
    val adminRoot = baseUrl + "/admin"
    val login = userRoot + "/login"
    val loginSubmit = userRoot + "/rsf/loginSubmit"
    val listShop = baseUrl + "/shop/shopList"
    def flowRealTimeSummary(id:String,date:String) =
      userRoot+"/getRealtimeSummary?unitId="+id.toLong+"&date="+date
    def flowDetailByHour(id:String,date:String) =
      userRoot+"/getRealtimeDetailByHour?unitId="+id.toLong+"&date="+date
    def flowDetailByDate(id:String,startDate:String,endDate:String) =
      userRoot+"/getRealtimeDetailByDate?unitId="+id.toLong+"&startTime="+startDate+"&endTime="+endDate
    def residentByDate(id:String,date:String) =
      userRoot+"/getDistributionHistory?unitId="+id.toLong+"&date="+date
    def conversionByDate(id:String,startDate:String,endDate:String) =
      userRoot+"/guestConversionRate?unitId="+id.toLong+"&startTime="+startDate+"&endTime="+endDate
    def brand(id:String,date:String) =
     userRoot + "/dailyBrand?unitId="+id.toLong+"&date="+date
    def averageTime(id:String,startDate:String,endDate:String) =
     userRoot + s"/getStartTimeHistory?unitId=${id.toLong}&startTime=$startDate&endTime=$endDate"
    def ratio(id:String,startDate:String,endDate:String) =
     userRoot +s"/newOldRatioHistory?unitId=${id.toLong}&startTime=$startDate&endTime=$endDate"
    def freq(id:String,date:String) =
     userRoot +s"/visitFreqHistory?unitId=${id.toLong}&date=$date"
  }



  object AdminRoutes{
    val adminRoot = baseUrl + "/admin"
    val login = adminRoot + "/login"
    val modifyPwd = adminRoot + "/modifyPwd"
    val listUser = adminRoot +"/listUser"
    val searchUser = adminRoot + "/searchUser"
    val listShop = adminRoot + "/listShop"
    val listUserShop = adminRoot + "/listUserShop"
    val getUserInfo = adminRoot + "/getUserInfo"
    val searchShop = adminRoot + "/searchShop"
    val listBox = adminRoot + "/listBox"
    val listObserver = adminRoot + "/listObserver"
    val addBox = adminRoot + "/bindBox"
    val unbindBox = adminRoot + "/unbindBox"
    val getShopOwner = adminRoot + "/getShopOwner"
    val addObserver = adminRoot + "/addObserver"
    val delObserver = adminRoot + "/deleteObserver"
  }

}
