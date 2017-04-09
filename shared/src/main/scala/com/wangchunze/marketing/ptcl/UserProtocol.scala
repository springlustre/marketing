package com.neo.sk.feeler3.ptcl

object UserProtocol {
//rsp
  sealed trait RestResponse{
    val errCode: Int
    val msg: String
  }

  case class SuccessResponse(
    msg: String = "ok",
    errCode: Int = 0
  )

  case class ErrorResponse(errCode: Int, msg: String) extends RestResponse

  case class FlowSummaryRsp(
                             data: FlowSummaryInfo,
                             errCode: Int,
                             msg: String
                           )
  case class FlowDetailRsp(
                             data: Seq[FlowDetailInfo],
                             errCode: Int,
                             msg: String
                           )
  case class ResidentTimeRsp(
                            data: Seq[ResidentInfo],
                            errCode: Int,
                            msg: String
                          )
  case class ConversionRsp(
                              data: List[GuestConversionDot],
                              errCode: Int,
                              msg: String
                            )
  case class BrandRsp(
                            data: List[BrandDot],
                            errCode: Int,
                            msg: String
                          )
  case class AverageRsp(
                       data: List[TimeDot],
                       errCode: Int,
                       msg: String
                     )
  case class RatioRsp(
                         data: List[RatioDot],
                         errCode: Int,
                         msg: String
                       )
  case class FreqRsp(
                            data: List[Freq],
                            errCode: Int,
                            msg: String
                          )
//dot
  sealed trait Dot

  case class TimeDot(timestamp: Long, value: Long) extends Dot
  case class Flag(flag: Int, value: Long) extends Dot
  case class Freq(kind: Int, value: Long) extends Dot
  case class GuestConversionDot(
                                 timestamp:Long,
                                 custIn:Long,
                                 custOut:Long
                               ) extends Dot
  case class BrandDot(
                       brandId:Long,
                       brandName:String,
                       value:Long
                     ) extends Dot
  case class RatioDot(
                       timestamp:Long,
                       newCount:Long,
                       oldCount:Long
                     ) extends Dot

//info
  case class FlowSummaryInfo(
                              custIn: Int,
                              oldCust: Int,
                              newCust: Int,
                              nowIn: Int,
                              residentTime: Int,
                              totalFlow:Int
                            )
  case class FlowDetailInfo(
                           unitId:String,
                           userType:String,
                           behavior:String,
                           dots: List[TimeDot],
                           amount: Int //当前人数
                           )
  case class ResidentInfo(
                           status:String,
                           dots:List[Flag]
                         )


}
