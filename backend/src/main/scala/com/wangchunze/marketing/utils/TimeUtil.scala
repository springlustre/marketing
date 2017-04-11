package com.wangchunze.marketing.utils

import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Calendar

import com.github.nscala_time.time.StaticInterval


/**
  * Created by wangchunze on 2016/3/23.
  */
object TimeUtil {

  def format(timeMs:Long,format:String = "yyyy-MM-dd HH:mm:ss") ={
    val data  = new Date(timeMs)
    val simpleDateFormat = new SimpleDateFormat(format)
    simpleDateFormat.format(data)
  }

  def getMinuteOfNow={
    val data  = new Date(System.currentTimeMillis())
    val format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")
    format.format(data).split("-")(4).toInt
  }

  /**
    * 格式化时间 输入时某天开始的分钟数
    * @param minute
    */
  def formatFromMinute(minute:Long)={
    import com.github.nscala_time.time.Imports._
    val triggerTime = DateTime.now.hour(0).minute(0).second(0).getMillis
    format(triggerTime+minute*60*1000,"HH:mm:ss")
  }

  /**
    * 日期转时间戳
    * @param date 格式：20160518
    */
  def parseDate(date:String)={
    val (year,month,day) = if(date.contains("/")){
      val d = date.trim.replace(" ","").split("/")
      (d(0),d(1),d(2))
    }else if(date.contains("-")){
      val d = date.trim.replace(" ","").split("-")
      (d(0),d(1),d(2))
    }else{
      val d = date.trim
      (d.take(4),d.slice(4,6),d.takeRight(2))
    }
    new SimpleDateFormat("yyyy-MM-dd")
      .parse(year+"-"+month+"-"+day)
      .getTime
  }



  /**
    * 获取之前某天的日期  返回格式 20160518
    */
  def getDateDaysBefore(timeMs:Long,n:Int)={
    val data  = new Date(timeMs-n*3600*24*1000)
    val simpleDateFormat = new SimpleDateFormat("yyyyMMdd")
    simpleDateFormat.format(data)
  }

  /**
    * 获取日期 返回格式 20160519
    * @param date 格式 20160518
    * @param n  n=1 表示前一天
    * @return
    */
  def getDateBeforeNow(date:String,n:Int)={
    val now=parseDate(date)
    getDateDaysBefore(now,n)
  }

  /**
    * 获取一个月的开始时间戳
    * @param now
    */
  def getMonthStart(now:Long)={
    val cal = Calendar.getInstance()
    cal.setTime(new Date(now))
    cal.set(Calendar.DATE,1)
    val df = new SimpleDateFormat("yyyyMMdd")
    parseDate(df.format(cal.getTime)) //本月第一天
  }

  def getMonthEnd(now:Long)={
    val cal =Calendar.getInstance()
    cal.setTime(new Date(now))
    val df = new SimpleDateFormat("yyyyMMdd")
    cal.add(Calendar.MONTH,1)
    cal.set(Calendar.DATE, 1)
    parseDate(df.format(cal.getTime))//本月最后一天
  }


  def getBeginOfDay(now:Long)={
    import com.github.nscala_time.time.Imports._
    DateTime.now.hour(0).minute(0).second(0).getMillis
  }

  def getBeginOfDayOfSec(now:Long)={
    import com.github.nscala_time.time.Imports._
    DateTime.now.hour(0).minute(0).second(0).getMillis / 1000
  }

  def getLastWeek={
    import com.github.nscala_time.time.Imports._
    (StaticInterval.lastWeek().getStartMillis,StaticInterval.lastWeek().getEndMillis)
  }

  def getDay(now:Long)={
    val data  = new Date(now)
    val format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")
    format.format(data).split("-")(2).toInt
  }

  def getLastMonth={
    import com.github.nscala_time.time.Imports._
    (StaticInterval.lastMonth().getStartMillis,StaticInterval.lastMonth().getEndMillis)
  }

  /**
    * 日期转时间戳
    * @param date 格式：2016-09-29
    */
  def date2TimeStamp(date:String)={
    new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(date).getTime
  }



  def main(args: Array[String]) {
    //    println(parseDate("20160518"))
    //    println(formatFromMinute(80))
//    val now = System.currentTimeMillis()
//    println(parseDate("20160302"))
//    println(parseDate("2016-03-02"))
//    println(parseDate("2016/03 /02"))

  }

}
