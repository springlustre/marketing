package com.wangchunze.marketing.utils

import com.google.gson.Gson
import com.qiniu.common.{QiniuException, Zone}
import com.qiniu.storage.model.DefaultPutRet
import com.qiniu.storage.{Configuration, UploadManager}
import com.qiniu.util.Auth
import java.io.IOException
import java.util.Base64

import com.qiniu.http.Response
import org.apache.commons.codec.digest.DigestUtils

/**
  * Created by springlustre on 2017/4/21.
  */
object QiniuTest {
  def main(args: Array[String]): Unit = {
    val cfg = new Configuration(Zone.zone1())
    //...其他参数参考类注释
    val uploadManager = new UploadManager(cfg)
    //...生成上传凭证，然后准备上传
    val accessKey = "wangchunze@126.com"
    val secretKey = "wang897618476"
    val bucket = "wangchunze"
//    val accessKey = "TQt-iplt8zbK3LEHMjNYyhh6PzxkbelZFRMl10Mx"
//    val secretKey = "hTIq4H8N5NfCme8gDvZqr6EDmvlIQsRV5L65bVcw"
//    val bucket = "if-pbl"
    //如果是Windows情况下，格式是 D:\\qiniu\\test.png
    val localFilePath = "E:/test.png"
    //默认不指定key的情况下，以文件内容的hash值作为文件名
    val key = "test.png2222222222"
    val auth = Auth.create(accessKey, secretKey)
    val signingStr  = "/refresh\n";

    val access_token = auth.sign(signingStr);
    val upToken = auth.uploadToken(bucket)
    val  decoded = Base64.getDecoder().decode(upToken.split(":").last)
    val a = new String(decoded)
    println(a)
    println(access_token)
    try {
      val response = uploadManager.put(localFilePath.getBytes(), key, upToken)
      //解析上传成功的结果
      val res = response.bodyString()
      println(res)
    } catch {
      case ex:QiniuException =>
      val r = ex.response
      System.err.println(r.toString())
      try {
        System.err.println(r.bodyString())
      } catch {
        case ex2:QiniuException =>
        //ignore
      }
    }
  }

}
