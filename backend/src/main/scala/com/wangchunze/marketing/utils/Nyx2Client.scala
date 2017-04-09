//package com.neo.sk.feeler3.utils
//
//import com.neo.sk.feeler3.common.AppSettings
//import com.neo.sk.feeler3.model._
//import org.slf4j.LoggerFactory
//import io.circe.{Decoder, Encoder, Json}
//import io.circe.generic.auto._
//import io.circe.syntax._
//import io.circe._
//import io.circe.parser._
//import cats.syntax.either._
//import com.neo.sk.feeler3.protocols.Nyx2JsonProtocol
//import com.neo.sk.feeler3.utils.CirceSupport._
//
//import scala.collection.mutable
//import scala.collection.mutable.ArrayBuffer
//import scala.concurrent.ExecutionContext.Implicits.global
//
///**
//  * Created by hong on 2017/1/5.
//  */
//object Nyx2Client extends HttpUtil with Nyx2JsonProtocol{
//  val log = LoggerFactory.getLogger(getClass);
//
//  val baseUrl = AppSettings.nyx2Protocol + "://" +AppSettings.nyx2Host + ":" + AppSettings.nyx2Port
//  //  val domainUrl = AppSettings.rogersProtocol + "://" + AppSettings.rogersDomain
//  val appId = AppSettings.nyx2AppId
//  val secureKey = AppSettings.nyx2SecureKey
//
//  def main(args: Array[String]) {
////    historySummary("1100001","all","in","20170301","20170316")
//    addBox("64517E3AE91A","1202-12A-4",65,2100001)
//    //    dailyDistribution("1100001","20170306","person")
//  }
//
//  /**
//    * 添加区
//    * */
//  def addDistrict(
//                   districtName:String
//                 ) = {
//    val url = baseUrl + s"/nyx2/api/member/addDistrict"
//    val methodName = s"addDistrict"
//
//    val sn = System.nanoTime().toString
//    val timestamp = System.currentTimeMillis()
//    val nonce = SecureUtil.nonceStr(6)
//    val data = Json.obj(
//      (s"districtName",districtName.asJson)
//    ).noSpaces
//    val signature = SecureUtil.generateSignature(List(appId, sn, timestamp.toString, nonce, data), secureKey)
//
//    val postData = Json.obj(
//      ("appId",appId.asJson),
//      ("sn",sn.asJson),
//      ("timestamp",timestamp.toString.asJson),
//      ("nonce",nonce.asJson),
//      ("data",data.asJson),
//      ("signature",signature.asJson)
//    ).noSpaces
//
//    postJsonRequestSend(methodName,url,List(),postData).map{
//      case Right(jsonString) =>
//        decode[AddDistrictResult](jsonString) match {
//          case Right(result) =>
//            if(result.errCode == 0L){
//              log.info(s"success")
//              Right(result.unitId.getOrElse(0L))
//            }else{
//              log.info(s"$methodName failed...$jsonString")
//              Left(jsonString)
//            }
//          case Left(e) =>
//            log.info(s":$methodName failed...error:${e.getMessage}..")
//            Left(jsonString)
//        }
//      case Left(t) =>
//        log.error(s"$methodName error:${t.getMessage}",t)
//        Left(t.getMessage)
//    }
//  }
//
//  /**
//    * 添加组
//    * */
//  def addGroup(
//                groupName:String,
//                durationLength:Long,
//                districtId:Long
//              ) = {
//    val url = baseUrl + s"/nyx2/api/member/addGroup"
//    val methodName = s"addGroup"
//
//    val sn = System.nanoTime().toString
//    val timestamp = System.currentTimeMillis()
//    val nonce = SecureUtil.nonceStr(6)
//    val data = Json.obj(
//      (s"groupName",groupName.asJson),
//      (s"durationLength",durationLength.asJson),
//      (s"districtId",districtId.asJson)
//    ).noSpaces
//    val signature = SecureUtil.generateSignature(List(appId, sn, timestamp.toString, nonce, data), secureKey)
//
//    val r = SecureUtil.checkSignature(List(appId, sn, timestamp.toString, nonce, data),signature,secureKey)
//    println(s"r=$r")
//
//    val postData = Json.obj(
//      ("appId",appId.asJson),
//      ("sn",sn.asJson),
//      ("timestamp",timestamp.toString.asJson),
//      ("nonce",nonce.asJson),
//      ("data",data.asJson),
//      ("signature",signature.asJson)
//    ).noSpaces
//
//    postJsonRequestSend(methodName,url,List(),postData).map{
//      case Right(jsonString) =>
//        decode[AddGroupResult](jsonString) match {
//          case Right(result) =>
//            if(result.errCode == 0L){
//              log.info(s"success")
//              Right(result.unitId.getOrElse(0L))
//            }else{
//              log.info(s"$methodName failed...$jsonString")
//              Left(jsonString)
//            }
//          case Left(e) =>
//            log.info(s":$methodName failed...error:${e.getMessage}..")
//            Left(jsonString)
//        }
//      case Left(t) =>
//        log.error(s"$methodName error:${t.getMessage}",t)
//        Left(t.getMessage)
//    }
//  }
//
//  /**
//    * 添加盒子
//    * */
//  def addBox(
//              boxMac:String,
//              boxName:String,
//              rssiSet:Long,
//              groupId:Long
//            ) = {
//    val url = baseUrl + s"/nyx2/api/member/addBox"
//    val methodName = s"addBox"
//
//    val sn = System.nanoTime().toString
//    val timestamp = System.currentTimeMillis()
//    val nonce = SecureUtil.nonceStr(6)
//    val data = Json.obj(
//      (s"boxMac",boxMac.asJson),
//      (s"boxName",boxName.asJson),
//      (s"rssiSet",rssiSet.asJson),
//      (s"groupId",groupId.asJson)
//    ).noSpaces
//    val signature = SecureUtil.generateSignature(List(appId, sn, timestamp.toString, nonce, data), secureKey)
//
//    val postData = Json.obj(
//      ("appId",appId.asJson),
//      ("sn",sn.asJson),
//      ("timestamp",timestamp.toString.asJson),
//      ("nonce",nonce.asJson),
//      ("data",data.asJson),
//      ("signature",signature.asJson)
//    ).noSpaces
//
//    postJsonRequestSend(methodName,url,List(),postData).map{
//      case Right(jsonString) =>
//        decode[CommonResult](jsonString) match {
//          case Right(result) =>
//            if(result.errCode == 0L){
//              log.info(s"success")
//              Right(result.errCode)
//            }else{
//              log.info(s"$methodName failed...$jsonString")
//              Left(jsonString)
//            }
//          case Left(e) =>
//            log.info(s":$methodName failed...error:${e.getMessage}..")
//            Left(jsonString)
//        }
//      case Left(t) =>
//        log.error(s"$methodName error:${t.getMessage}",t)
//        Left(t.getMessage)
//    }
//  }
//
//  /**
//    * 修改盒子从属关系
//    * */
//  def editBoxAffiliation(
//                          unitId:String,
//                          affiliationId	:String
//                        ) = {
//    val url = baseUrl + s"/nyx2/api/member/editBoxAffiliation"
//    val methodName = s"editBoxAffiliation"
//
//    val sn = System.nanoTime().toString
//    val timestamp = System.currentTimeMillis()
//    val nonce = SecureUtil.nonceStr(6)
//    val data = Json.obj(
//      (s"unitId",unitId.asJson),
//      (s"affiliationId",affiliationId.asJson)
//    ).noSpaces
//    val signature = SecureUtil.generateSignature(List(appId, sn, timestamp.toString, nonce, data), secureKey)
//
//    val postData = Json.obj(
//      ("appId",appId.asJson),
//      ("sn",sn.asJson),
//      ("timestamp",timestamp.toString.asJson),
//      ("nonce",nonce.asJson),
//      ("data",data.asJson),
//      ("signature",signature.asJson)
//    ).noSpaces
//
//    postJsonRequestSend(methodName,url,List(),postData).map{
//      case Right(jsonString) =>
//        decode[CommonResult](jsonString) match {
//          case Right(result) =>
//            if(result.errCode == 0L){
//              log.info(s"success")
//              Right(result.errCode)
//            }else{
//              log.info(s"$methodName failed...$jsonString")
//              Left(jsonString)
//            }
//          case Left(e) =>
//            log.info(s":$methodName failed...error:${e.getMessage}..")
//            Left(jsonString)
//        }
//      case Left(t) =>
//        log.error(s"$methodName error:${t.getMessage}",t)
//        Left(t.getMessage)
//    }
//  }
//
//  /**
//    * 删除盒子
//    * */
//  def deleteBox(
//                 unitId:String
//               ) = {
//    val url = baseUrl + s"/nyx2/api/member/deleteBox"
//    val methodName = s"deleteBox"
//
//    val sn = System.nanoTime().toString
//    val timestamp = System.currentTimeMillis()
//    val nonce = SecureUtil.nonceStr(6)
//    val data = Json.obj(
//      (s"unitId",unitId.asJson)
//    ).noSpaces
//    val signature = SecureUtil.generateSignature(List(appId, sn, timestamp.toString, nonce, data), secureKey)
//
//    val postData = Json.obj(
//      ("appId",appId.asJson),
//      ("sn",sn.asJson),
//      ("timestamp",timestamp.toString.asJson),
//      ("nonce",nonce.asJson),
//      ("data",data.asJson),
//      ("signature",signature.asJson)
//    ).noSpaces
//
//    postJsonRequestSend(methodName,url,List(),postData).map{
//      case Right(jsonString) =>
//        decode[CommonResult](jsonString) match {
//          case Right(result) =>
//            if(result.errCode == 0L){
//              log.info(s"success")
//              Right(result.errCode)
//            }else{
//              log.info(s"$methodName failed...$jsonString")
//              Left(jsonString)
//            }
//          case Left(e) =>
//            log.info(s":$methodName failed...error:${e.getMessage}..")
//            Left(jsonString)
//        }
//      case Left(t) =>
//        log.error(s"$methodName error:${t.getMessage}",t)
//        Left(t.getMessage)
//    }
//  }
//
//  /**
//    * 修改盒子名称
//    * */
//  def editBoxName(
//                   unitId:String,
//                   name	:String
//                 ) = {
//    val url = baseUrl + s"/nyx2/api/member/editBoxName"
//    val methodName = s"editBoxName"
//
//    val sn = System.nanoTime().toString
//    val timestamp = System.currentTimeMillis()
//    val nonce = SecureUtil.nonceStr(6)
//    val data = Json.obj(
//      (s"unitId",unitId.asJson),
//      (s"name",name.asJson)
//    ).noSpaces
//    val signature = SecureUtil.generateSignature(List(appId, sn, timestamp.toString, nonce, data), secureKey)
//
//    val postData = Json.obj(
//      ("appId",appId.asJson),
//      ("sn",sn.asJson),
//      ("timestamp",timestamp.toString.asJson),
//      ("nonce",nonce.asJson),
//      ("data",data.asJson),
//      ("signature",signature.asJson)
//    ).noSpaces
//
//    postJsonRequestSend(methodName,url,List(),postData).map{
//      case Right(jsonString) =>
//        decode[CommonResult](jsonString) match {
//          case Right(result) =>
//            if(result.errCode == 0L){
//              log.info(s"success")
//              Right(result.errCode)
//            }else{
//              log.info(s"$methodName failed...$jsonString")
//              Left(jsonString)
//            }
//          case Left(e) =>
//            log.info(s":$methodName failed...error:${e.getMessage}..")
//            Left(jsonString)
//        }
//      case Left(t) =>
//        log.error(s"$methodName error:${t.getMessage}",t)
//        Left(t.getMessage)
//    }
//  }
//
//  /**
//    * 修改盒子名称
//    * */
//  def editGroupName(
//                     unitId:String,
//                     name	:String
//                   ) = {
//    val url = baseUrl + s"/nyx2/api/member/editGroupName"
//    val methodName = s"editGroupName"
//
//    val sn = System.nanoTime().toString
//    val timestamp = System.currentTimeMillis()
//    val nonce = SecureUtil.nonceStr(6)
//    val data = Json.obj(
//      (s"unitId",unitId.asJson),
//      (s"name",name.asJson)
//    ).noSpaces
//    val signature = SecureUtil.generateSignature(List(appId, sn, timestamp.toString, nonce, data), secureKey)
//
//    val postData = Json.obj(
//      ("appId",appId.asJson),
//      ("sn",sn.asJson),
//      ("timestamp",timestamp.toString.asJson),
//      ("nonce",nonce.asJson),
//      ("data",data.asJson),
//      ("signature",signature.asJson)
//    ).noSpaces
//
//    postJsonRequestSend(methodName,url,List(),postData).map{
//      case Right(jsonString) =>
//        decode[CommonResult](jsonString) match {
//          case Right(result) =>
//            if(result.errCode == 0L){
//              log.info(s"success")
//              Right(result.errCode)
//            }else{
//              log.info(s"$methodName failed...$jsonString")
//              Left(jsonString)
//            }
//          case Left(e) =>
//            log.info(s":$methodName failed...error:${e.getMessage}..")
//            Left(jsonString)
//        }
//      case Left(t) =>
//        log.error(s"$methodName error:${t.getMessage}",t)
//        Left(t.getMessage)
//    }
//  }
//
//  /**
//    *获取所有区级别统计单元信息
//    **/
//
//  def getAllDistricts() = {
//    val url = baseUrl + s"/nyx2/api/member/getAllDistricts"
//    val methodName = s"getAllDistricts"
//
//    val sn = System.nanoTime().toString
//    val timestamp = System.currentTimeMillis()
//    val nonce = SecureUtil.nonceStr(6)
//    val signature = SecureUtil.generateSignature(List(appId, sn, timestamp.toString, nonce), secureKey)
//
//    val postData = Json.obj(
//      ("appId",appId.asJson),
//      ("sn",sn.asJson),
//      ("timestamp",timestamp.toString.asJson),
//      ("nonce",nonce.asJson),
//      ("signature",signature.asJson)
//    ).noSpaces
//
//    postJsonRequestSend(methodName,url,List(),postData).map{
//      case Right(jsonString) =>
//        decode[AllDistricts](jsonString) match {
//          case Right(allDistricts) =>
//            if(allDistricts.errCode == 0L){
//              log.info(s"success")
//              Right(allDistricts.member)
//            }else{
//              log.info(s"$methodName failed...$jsonString")
//              Left(jsonString)
//            }
//
//          case Left(e) =>
//            log.info(s"parse error:${e.getMessage}..")
//            Left(jsonString)
//        }
//      case Left(t) =>
//        log.error(s"$methodName error:${t.getMessage}",t)
//        Left(t.getMessage)
//    }
//  }
//
//  /**
//    *通过区编号获取所有属于它的组级别统计单元的信息
//    * */
//  def getGroupInfoList(unitId:String) = {
//    val url = baseUrl + s"/nyx2/api/member/getGroupInfoList"
//    val methodName = s"getGroupInfoList"
//
//    val sn = System.nanoTime().toString
//    val timestamp = System.currentTimeMillis()
//    val nonce = SecureUtil.nonceStr(6)
//    val data = UnitId(unitId).asJson.noSpaces
//    val signature = SecureUtil.generateSignature(List(appId, sn, timestamp.toString, nonce, data), secureKey)
//
//    val postData = Json.obj(
//      ("appId",appId.asJson),
//      ("sn",sn.asJson),
//      ("timestamp",timestamp.toString.asJson),
//      ("nonce",nonce.asJson),
//      ("data",data.asJson),
//      ("signature",signature.asJson)
//    ).noSpaces
//
//    postJsonRequestSend(methodName,url,List(),postData).map{
//      case Right(jsonString) =>
//        decode[GroupInfoList](jsonString) match {
//          case Right(groupInfoList) =>
//            if(groupInfoList.errCode == 0L){
//              log.info(s"success")
//              Right(groupInfoList.member)
//            }else{
//              log.info(s"$methodName failed...$jsonString")
//              Left(jsonString)
//            }
//          case Left(e) =>
//            log.info(s":$methodName failed...error:${e.getMessage}..")
//            Left(jsonString)
//        }
//      case Left(t) =>
//        log.error(s"$methodName error:${t.getMessage}",t)
//        Left(t.getMessage)
//    }
//  }
//
//
//  /**
//    * realtimeSummary
//    * 获取各统计项当前实时数据的接口
//    * */
//  def realtimeSummary(unitId:String,date:String) = {
//    val url = baseUrl + s"/nyx2/api/flow/realtimeSummary"
//    val methodName = s"realtimeSummary"
//
//    val sn = System.nanoTime().toString
//    val timestamp = System.currentTimeMillis()
//    val nonce = SecureUtil.nonceStr(6)
//    val data = Json.obj(
//      ("unitId",unitId.asJson),
//      ("date",date.asJson)
//    ).noSpaces
//    val signature = SecureUtil.generateSignature(List(appId, sn, timestamp.toString, nonce, data), secureKey)
//
//    val postData = Json.obj(
//      ("appId",appId.asJson),
//      ("sn",sn.asJson),
//      ("timestamp",timestamp.toString.asJson),
//      ("nonce",nonce.asJson),
//      ("data",data.asJson),
//      ("signature",signature.asJson)
//    ).noSpaces
//
//    postJsonRequestSend(methodName,url,List(),postData).map{
//      case Right(jsonString) =>
//        decode[RealtimeSummaryResult](jsonString) match {
//          case Right(result) =>
//            if(result.errCode == 0L){
//              log.info(s"success")
//              Right(result.info)
//            }else{
//              log.info(s"$methodName failed...$jsonString")
//              Left(jsonString)
//            }
//          case Left(e) =>
//            log.info(s":$methodName failed...error:${e.getMessage}..")
//            Left(jsonString)
//        }
//      case Left(t) =>
//        log.error(s"$methodName error:${t.getMessage}",t)
//        Left(t.getMessage)
//    }
//  }
//
//  /**
//    * 实时客流图表信息
//    * */
//  def realtimeDetail(
//                      unitId:String,
//                      userType:String,  //all为全部，customer为顾客，staff为员工
//                      behavior:String,  //in为进店，out为穿行
//                      date:String     //yyyyMMdd
//                    ) = {
//    val url = baseUrl + s"/nyx2/api/flow/detail"
//    val methodName = s"realtimeDetail"
//
//    val sn = System.nanoTime().toString
//    val timestamp = System.currentTimeMillis()
//    val nonce = SecureUtil.nonceStr(6)
//    val data = RealtimeDetailPostData(unitId,userType,behavior,date).asJson.noSpaces
//    val signature = SecureUtil.generateSignature(List(appId, sn, timestamp.toString, nonce, data), secureKey)
//
//    val postData = Json.obj(
//      ("appId",appId.asJson),
//      ("sn",sn.asJson),
//      ("timestamp",timestamp.toString.asJson),
//      ("nonce",nonce.asJson),
//      ("data",data.asJson),
//      ("signature",signature.asJson)
//    ).noSpaces
//
//    postJsonRequestSend(methodName,url,List(),postData).map{
//      case Right(jsonString) =>
//        decode[RealtimeDetailResult](jsonString) match {
//          case Right(result) =>
//            if(result.errCode == 0L){
//              log.info(s"success")
//              Right(result)
//            }else{
//              log.info(s"$methodName failed...$jsonString")
//              Left(jsonString)
//            }
//          case Left(e) =>
//            log.info(s":$methodName failed...error:${e.getMessage}..")
//            Left(jsonString)
//        }
//      case Left(t) =>
//        log.error(s"$methodName error:${t.getMessage}",t)
//        Left(t.getMessage)
//    }
//  }
//
//  /**
//    * 实时客流图表信息 历史记录
//    * history/overview
//    * */
//  def historySummary(
//                      unitId:String,
//                      userType:String,  //all为全部，customer为顾客，staff为员工
//                      behavior:String,  //in为进店，out为穿行
//                      startTime:String,     //yyyyMMdd
//                      endTime:String
//                    ) = {
//    val url = baseUrl + s"/nyx2/api/flow/history/overview"
//    val methodName = s"historySummary"
//
//    val sn = System.nanoTime().toString
//    val timestamp = System.currentTimeMillis()
//    val nonce = SecureUtil.nonceStr(6)
//    val data = HistorySummaryPostData(unitId,userType,behavior,startTime,endTime).asJson.noSpaces
//    val signature = SecureUtil.generateSignature(List(appId, sn, timestamp.toString, nonce, data), secureKey)
//
//    val postData = Json.obj(
//      ("appId",appId.asJson),
//      ("sn",sn.asJson),
//      ("timestamp",timestamp.toString.asJson),
//      ("nonce",nonce.asJson),
//      ("data",data.asJson),
//      ("signature",signature.asJson)
//    ).noSpaces
//
//    postJsonRequestSend(methodName,url,List(),postData).map{
//      case Right(jsonString) =>
//        decode[HistorySummaryResult](jsonString) match {
//          case Right(result) =>
//            if(result.errCode == 0L){
//              log.info(s"success")
//              println(s"s=${dealCorrectDots(Some(result.dots),startTime,endTime)}")
//              Right(dealCorrectDots(Some(result.dots),startTime,endTime))
//            }else{
//              log.info(s"$methodName failed...$jsonString")
//              Left(jsonString)
//            }
//          case Left(e) =>
//            log.info(s":$methodName failed...error:${e.getMessage}..")
//            Left(jsonString)
//        }
//      case Left(t) =>
//        log.error(s"$methodName error:${t.getMessage}",t)
//        Left(t.getMessage)
//    }
//  }
//
//  /**
//    * 获取某个区下所有分组当前人数信息的接口
//    * groupDetail
//    * */
//  def groupDetail(
//                   unitId:String
//                 ) = {
//    val url = baseUrl + s"/nyx2/api/flow/groupDetail"
//    val methodName = s"groupDetail"
//
//    val sn = System.nanoTime().toString
//    val timestamp = System.currentTimeMillis()
//    val nonce = SecureUtil.nonceStr(6)
//    val data = UnitId(unitId).asJson.noSpaces
//    val signature = SecureUtil.generateSignature(List(appId, sn, timestamp.toString, nonce, data), secureKey)
//
//    val postData = Json.obj(
//      ("appId",appId.asJson),
//      ("sn",sn.asJson),
//      ("timestamp",timestamp.toString.asJson),
//      ("nonce",nonce.asJson),
//      ("data",data.asJson),
//      ("signature",signature.asJson)
//    ).noSpaces
//
//    postJsonRequestSend(methodName,url,List(),postData).map{
//      case Right(jsonString) =>
//        decode[GroupCustomInResultNyx2](jsonString) match {
//          case Right(result) =>
//            if(result.errCode == 0L){
//              log.info(s"success")
//              Right(result.dots)
//            }else{
//              log.info(s"$methodName failed...$jsonString")
//              Left(jsonString)
//            }
//          case Left(e) =>
//            log.info(s":$methodName failed...error:${e.getMessage}..")
//            Left(jsonString)
//        }
//      case Left(t) =>
//        log.error(s"$methodName error:${t.getMessage}",t)
//        Left(t.getMessage)
//    }
//  }
//
//
//  /**
//    * 获取某一统计单元指定日期区间内平均驻留时长信息的接口
//    * stayTime/history/overview
//    * */
//  def stayTimeHistory(
//                       unitId:String,
//                       startTime:String,     //yyyyMMdd
//                       endTime:String
//
//                     ) = {
//    val url = baseUrl + s"/nyx2/api/stayTime/history/overview"
//    val methodName = s"stayTimeHistory"
//
//    val sn = System.nanoTime().toString
//    val timestamp = System.currentTimeMillis()
//    val nonce = SecureUtil.nonceStr(6)
//    val data = Json.obj(
//      (s"unitId",unitId.asJson),
//      (s"startTime",startTime.asJson),
//      (s"endTime",endTime.asJson)
//    ).noSpaces
//    val signature = SecureUtil.generateSignature(List(appId, sn, timestamp.toString, nonce, data), secureKey)
//
//    val postData = Json.obj(
//      ("appId",appId.asJson),
//      ("sn",sn.asJson),
//      ("timestamp",timestamp.toString.asJson),
//      ("nonce",nonce.asJson),
//      ("data",data.asJson),
//      ("signature",signature.asJson)
//    ).noSpaces
//
//    postJsonRequestSend(methodName,url,List(),postData).map{
//      case Right(jsonString) =>
//        decode[stayTimeHistoryResultNyx2](jsonString) match {
//          case Right(result) =>
//            if(result.errCode == 0L){
//              log.info(s"success")
//              //              println(s"ss=${dealCorrectDots(result.dots,startTime,endTime)}")
//              Right(dealCorrectDots(result.dots,startTime,endTime))
//            }else{
//              log.info(s"$methodName failed...$jsonString")
//              Left(jsonString)
//            }
//          case Left(e) =>
//            log.info(s":$methodName failed...error:${e.getMessage}..")
//            Left(jsonString)
//        }
//      case Left(t) =>
//        log.error(s"$methodName error:${t.getMessage}",t)
//        Left(t.getMessage)
//    }
//  }
//
//  /**
//    * 补齐数据
//    * */
//  def dealCorrectDots(data:Option[List[RealtimeDetailDots]],startTime:String,endTime:String) ={
//    val result = new ArrayBuffer[RealtimeDetailDots]()
//    val d = data.getOrElse(Nil)
//    var start = TimeUtil.parseDate(startTime)
//    val end = TimeUtil.parseDate(endTime)
//    val today = TimeUtil.parseDate(TimeUtil.format(System.currentTimeMillis(),"yyyyMMdd"))
//    while(start <= end  ){
//      val timeString = TimeUtil.format(start,"yyyyMMdd")
//      val s = d.filter(_.timestamp.toString == timeString)
//      if(s.length > 0)
//        result.append(RealtimeDetailDots(TimeUtil.parseDate(s(0).timestamp.toString),s(0).value))
//      else
//        result.append((RealtimeDetailDots(TimeUtil.parseDate(timeString).toLong,0)))
//
//      start += 3600*24*1000
//    }
//    result.toList.sortBy(_.timestamp)
//  }
//
//
//  /**
//    * 获取某一统计单元指定日期驻留时长分布信息的接口
//    * PersonTime
//    * */
//  def dailyDistribution(
//                         unitId:String,
//                         date:String,
//                         status:String  // person,personTime
//                       ) = {
//    val url = baseUrl + s"/nyx2/api/stayTime/history/dailyDistribution"
//    val methodName = s"dailyDistribution"
//
//    val sn = System.nanoTime().toString
//    val timestamp = System.currentTimeMillis()
//    val nonce = SecureUtil.nonceStr(6)
//    val data = Json.obj(
//      (s"unitId",unitId.asJson),
//      (s"date",date.asJson),
//      (s"status",status.asJson)
//    ).noSpaces
//    val signature = SecureUtil.generateSignature(List(appId, sn, timestamp.toString, nonce, data), secureKey)
//
//    val postData = Json.obj(
//      ("appId",appId.asJson),
//      ("sn",sn.asJson),
//      ("timestamp",timestamp.toString.asJson),
//      ("nonce",nonce.asJson),
//      ("data",data.asJson),
//      ("signature",signature.asJson)
//    ).noSpaces
//
//    postJsonRequestSend(methodName,url,List(),postData).map{
//      case Right(jsonString) =>
//        decode[DistributionResultNyx2](jsonString) match {
//          case Right(result) =>
//            if(result.errCode == 0L){
//              log.info(s"success")
//              println(s"${dealCountDistribution(result.dots)}")
//              Right(dealCountDistribution(result.dots))
//            }else{
//              log.info(s"$methodName failed...$jsonString")
//              Left(jsonString)
//            }
//          case Left(e) =>
//            log.info(s":$methodName failed...error:${e.getMessage}..")
//            Left(jsonString)
//        }
//      case Left(t) =>
//        log.error(s"$methodName error:${t.getMessage}",t)
//        Left(t.getMessage)
//    }
//  }
//
//  /**
//    * 补齐数据
//    * */
//  def dealCountDistribution(data:Option[List[Distribution]]) ={
//    var cout10 = 0L
//    var cout30 = 0L
//    var cout60 = 0L
//    var cout120 = 0L
//    var coutMax = 0L
//    val d = data.getOrElse(Nil)
//    d.map{  r =>
//      if(r.kind > 120)
//        coutMax += r.value
//      else if(r.kind > 60)
//        cout120 += r.value
//      else if(r.kind > 30)
//        cout60 += r.value
//      else if(r.kind > 10)
//        cout30 += r.value
//      else
//        cout10 += r.value
//    }
//
//    val result = new ArrayBuffer[DistributionInfo]()
//    result.append(DistributionInfo(1,cout10))
//    result.append(DistributionInfo(2,cout30))
//    result.append(DistributionInfo(3,cout60))
//    result.append(DistributionInfo(4,cout120))
//    result.append(DistributionInfo(5,coutMax))
//    result.toList
//  }
//
//
//  /**
//    * 获取某月制定统计单元到访频次信息的接口
//    *
//    * */
//  def visitFreqHistory(
//                        unitId:String,
//                        date:String      //yyyyMM
//                      ) = {
//    val url = baseUrl + s"/nyx2/api/visitFreq/history"
//    val methodName = s"visitFreqHistory"
//
//    val sn = System.nanoTime().toString
//    val timestamp = System.currentTimeMillis()
//    val nonce = SecureUtil.nonceStr(6)
//    val data = Json.obj(
//      (s"unitId",unitId.asJson),
//      (s"date",date.asJson)
//    ).noSpaces
//    val signature = SecureUtil.generateSignature(List(appId, sn, timestamp.toString, nonce, data), secureKey)
//
//    val postData = Json.obj(
//      ("appId",appId.asJson),
//      ("sn",sn.asJson),
//      ("timestamp",timestamp.toString.asJson),
//      ("nonce",nonce.asJson),
//      ("data",data.asJson),
//      ("signature",signature.asJson)
//    ).noSpaces
//
//    postJsonRequestSend(methodName,url,List(),postData).map{
//      case Right(jsonString) =>
//        decode[visitFreqResultNyx2](jsonString) match {
//          case Right(result) =>
//            if(result.errCode == 0L){
//              log.info(s"success")
//              println(s"${result.dots}")
//              Right(result.dots.get)
//            }else{
//              log.info(s"$methodName failed...$jsonString")
//              Left(jsonString)
//            }
//          case Left(e) =>
//            log.info(s":$methodName failed...error:${e.getMessage}..")
//            Left(jsonString)
//        }
//      case Left(t) =>
//        log.error(s"$methodName error:${t.getMessage}",t)
//        Left(t.getMessage)
//    }
//  }
//
//  /**1.8
//    * 获取指定某一天新老顾客详细数据的接口
//    *
//    * */
//  def newOldRatioDailyDetail(
//                              unitId:String,
//                              date:String      //yyyyMMdd
//                            ) = {
//    val url = baseUrl + s"/nyx2/api/newOldRatio/realtime/dailyDetail"
//    val methodName = s"newOldRatioDailyDetail"
//
//    val sn = System.nanoTime().toString
//    val timestamp = System.currentTimeMillis()
//    val nonce = SecureUtil.nonceStr(6)
//    val data = Json.obj(
//      (s"unitId",unitId.asJson),
//      (s"date",date.asJson)
//    ).noSpaces
//    val signature = SecureUtil.generateSignature(List(appId, sn, timestamp.toString, nonce, data), secureKey)
//
//    val postData = Json.obj(
//      ("appId",appId.asJson),
//      ("sn",sn.asJson),
//      ("timestamp",timestamp.toString.asJson),
//      ("nonce",nonce.asJson),
//      ("data",data.asJson),
//      ("signature",signature.asJson)
//    ).noSpaces
//
//    postJsonRequestSend(methodName,url,List(),postData).map{
//      case Right(jsonString) =>
//        decode[newOldRatioDotResultNyx2](jsonString) match {
//          case Right(result) =>
//            if(result.errCode == 0L){
//              log.info(s"success")
//              println(s"${result.dots}")
//              Right(result.dots.get)
//            }else{
//              log.info(s"$methodName failed...$jsonString")
//              Left(jsonString)
//            }
//          case Left(e) =>
//            log.info(s":$methodName failed...error:${e.getMessage}..")
//            Left(jsonString)
//        }
//      case Left(t) =>
//        log.error(s"$methodName error:${t.getMessage}",t)
//        Left(t.getMessage)
//    }
//  }
//
//  /**1.9
//    * 获取指定时间区间内新老顾客详细数据的接口
//    *
//    * */
//  def newOldRatioHistoryOverview(
//                                  unitId:String,
//                                  startTime:String,      //yyyyMM
//                                  endTime:String
//                                ) = {
//    val url = baseUrl + s"/nyx2/api/newOldRatio/history/overview"
//    val methodName = s"newOldRatioHistoryOverview"
//
//    val sn = System.nanoTime().toString
//    val timestamp = System.currentTimeMillis()
//    val nonce = SecureUtil.nonceStr(6)
//    val data = Json.obj(
//      (s"unitId",unitId.asJson),
//      (s"startTime",startTime.asJson),
//      (s"endTime",endTime.asJson)
//    ).noSpaces
//    val signature = SecureUtil.generateSignature(List(appId, sn, timestamp.toString, nonce, data), secureKey)
//
//    val postData = Json.obj(
//      ("appId",appId.asJson),
//      ("sn",sn.asJson),
//      ("timestamp",timestamp.toString.asJson),
//      ("nonce",nonce.asJson),
//      ("data",data.asJson),
//      ("signature",signature.asJson)
//    ).noSpaces
//
//    postJsonRequestSend(methodName,url,List(),postData).map{
//      case Right(jsonString) =>
//        decode[newOldRatioDotResultNyx2](jsonString) match {
//          case Right(result) =>
//            if(result.errCode == 0L){
//              log.info(s"success")
//              //              println(s"${dealCorrectNewOldRatioDot(result.dots,startTime,endTime)}")
//              Right(dealCorrectNewOldRatioDot(result.dots,startTime,endTime))
//            }else{
//              log.info(s"$methodName failed...$jsonString")
//              Left(jsonString)
//            }
//          case Left(e) =>
//            log.info(s":$methodName failed...error:${e.getMessage}..")
//            Left(jsonString)
//        }
//      case Left(t) =>
//        log.error(s"$methodName error:${t.getMessage}",t)
//        Left(t.getMessage)
//    }
//  }
//  /**
//    * 补齐数据
//    * */
//  def dealCorrectNewOldRatioDot(data:Option[List[newOldRatioDot]],startTime:String,endTime:String) ={
//    val result = new ArrayBuffer[newOldRatioDot]()
//    val d = data.getOrElse(Nil)
//    var start = TimeUtil.parseDate(startTime)
//    val end = TimeUtil.parseDate(endTime)
//    val today = TimeUtil.parseDate(TimeUtil.format(System.currentTimeMillis(),"yyyyMMdd"))
//    while(start <= end  ){
//      val timeString = TimeUtil.format(start,"yyyyMMdd")
//      val s = d.filter(_.timestamp.toString == timeString)
//      if(s.length > 0)
//        result.append(newOldRatioDot(TimeUtil.parseDate(s(0).timestamp.toString),s(0).oldCount,s(0).newCount))
//      else
//        result.append((newOldRatioDot(TimeUtil.parseDate(timeString).toLong,0,0)))
//
//      start += 3600*24*1000
//    }
//    result.toList.sortBy(_.timestamp)
//  }
//
//
//  /**1.10、品牌统计信息
//    * 获取某一统计单元指定日期品牌统计信息的接口
//    *
//    * */
//  def dailyBrand(
//                  unitId:String,
//                  date:String   //指定日期，形如yyyyMMdd（查询单天）或yyyyMM（查询单月，或最近三十天）或yyyyMMddyyyyMMdd（查询一周，为当周周一与周日的日期）
//                ) = {
//    val url = baseUrl + s"/nyx2/api/brand/dailyBrand"
//    val methodName = s"dailyBrand"
//
//    val sn = System.nanoTime().toString
//    val timestamp = System.currentTimeMillis()
//    val nonce = SecureUtil.nonceStr(6)
//    val data = Json.obj(
//      (s"unitId",unitId.asJson),
//      (s"date",date.asJson)
//    ).noSpaces
//    val signature = SecureUtil.generateSignature(List(appId, sn, timestamp.toString, nonce, data), secureKey)
//
//    val postData = Json.obj(
//      ("appId",appId.asJson),
//      ("sn",sn.asJson),
//      ("timestamp",timestamp.toString.asJson),
//      ("nonce",nonce.asJson),
//      ("data",data.asJson),
//      ("signature",signature.asJson)
//    ).noSpaces
//
//    postJsonRequestSend(methodName,url,List(),postData).map{
//      case Right(jsonString) =>
//        decode[BrandInfoResultNyx2](jsonString) match {
//          case Right(result) =>
//            if(result.errCode == 0L){
//              log.info(s"success")
//              println(s"${result.dots}")
//              Right(result.dots.get)
//            }else{
//              log.info(s"$methodName failed...$jsonString")
//              Left(jsonString)
//            }
//          case Left(e) =>
//            log.info(s":$methodName failed...error:${e.getMessage}..")
//            Left(jsonString)
//        }
//      case Left(t) =>
//        log.error(s"$methodName error:${t.getMessage}",t)
//        Left(t.getMessage)
//    }
//  }
//}
//
