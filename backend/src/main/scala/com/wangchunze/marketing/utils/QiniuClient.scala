//package com.wangchunze.marketing.utils
//
//import javax.crypto.spec.SecretKeySpec
//import java.io.File
//import org.apache.http.entity.mime.MultipartEntity
//import org.apache.http.entity.mime.content.StringBody
//import scala.collection.mutable.{ Map => CacheMap }
//import org.apache.http.entity.mime.content.FileBody
//import org.apache.http.client.methods.HttpPost
//import org.apache.http.params.BasicHttpParams
//import org.apache.http.impl.conn.PoolingClientConnectionManager
//import org.apache.http.params.HttpConnectionParams
//import org.apache.http.impl.client.DefaultHttpClient
//import org.apache.http.util.EntityUtils
//import java.io.ByteArrayOutputStream
//import org.apache.commons.codec.binary.Base64
//import scala.util.parsing.json.JSONObject
//import scala.util.parsing.json.JSON
//import org.apache.http.client.methods.HttpGet
//
///**
//  * Created by springlustre on 2017/4/21.
//  */
//
//class QIniuClient(accessKey: String, secretKey: String, upHost: String = "http://up.qbox.me",dnHost:String="http://{bucket}.qiniudn.com/",rsfHost: String = "http://rsf.qbox.me", rsHost: String = "http://rs.qbox.me") {
//  //http 连接参数
//
//  private val httpParams = {
//
//    val httpParams = new BasicHttpParams()
//    // 设置连接超时时间
//
//    HttpConnectionParams.setConnectionTimeout(httpParams, 10000)
//
//    // 设置读取超时时间
//
//    HttpConnectionParams.setSoTimeout(httpParams, 20000)
//
//    httpParams
//  }
//  //http连接池
//
//  private val httpConnManager = {
//
//    val cm = new PoolingClientConnectionManager()
//    cm.setMaxTotal(200)
//    cm.setDefaultMaxPerRoute(20)
//    cm
//  }
//  protected def getHttpClient = {
//    new DefaultHttpClient(httpConnManager, httpParams)
//  }
//
//  protected val cache = CacheMap[String, Tuple2[String, Long]]()
//  //获取缓存的临时token
//
//  protected def tempUploadToken(scope: String) = {
//    val key = "temp_upload_token" + scope
//    val value = cache.getOrElse(key, null)
//    val timestamp = System.currentTimeMillis() / 1000
//    if (value == null || timestamp - value._2 >= 3600l) {
//      val newValue = (uploadToken(scope), timestamp)
//      cache(key) = newValue
//      newValue._1
//    } else {
//      value._1
//    }
//  }
//  /** 上传文件，extra放置自定义变量
//
//    * 必须传token和scope之中一个
//
//    * @author 黄林
//
//    * Upload file.
//
//    */
//  def uploadFile(key: String, file: File, extra: Map[String, String] = Map(), token: String = null, scope: String = null) = {
//    val requestEntity = new MultipartEntity
//    val uploadToken = if (token == null) tempUploadToken(scope) else token
//    Map("token" -> new StringBody(uploadToken), "file" -> new FileBody(file), "key" -> new StringBody(key)) foreach {
//      case (k, v) => requestEntity.addPart(k, v)
//    }
//    val postMethod = new HttpPost(upHost)
//    postMethod.setEntity(requestEntity)
//    val response=getHttpClient.execute(postMethod)
//    if(response.getStatusLine().getStatusCode()!=200)
//      throw new Exception(EntityUtils.toString(response.getEntity()))
//    EntityUtils.toByteArray(response.getEntity())
//  }
//
//  def downloadFile(scope:String,key:String,fop:String=null)={
//    val url=dnHost.replace("{bucket}", scope)+"/"+key +( if(fop!=null&&fop.length>0) fop else "" )
//    val withTokenUrl=downloadUrl(url)
//    val response=getHttpClient.execute(new HttpGet(withTokenUrl))
//    if(response.getStatusLine().getStatusCode()!=200)
//      throw new Exception(EntityUtils.toString(response.getEntity()))
//    EntityUtils.toByteArray(response.getEntity())
//  }
//
//  /** 复制文件
//
//    */
//  def copy(scopeSrc: String, keySrc: String, scopeDest: String,
//    keyDest: String) = {
//    val entryURISrc = scopeSrc + ":" + keySrc
//    val entryURIDest = scopeDest + ":" + keyDest
//    command("copy", entryURISrc, entryURIDest)
//  }
//  /** 移动文件
//
//    */
//  def move(scopeSrc: String, keySrc: String, scopeDest: String,
//    keyDest: String) = {
//    val entryURISrc = scopeSrc + ":" + keySrc
//    val entryURIDest = scopeDest + ":" + keyDest
//    command("move", entryURISrc, entryURIDest)
//  }
//
//  def delete(scope: String, key: String) {
//    val entryURI = scope + ":" + key
//    val url = rsfHost + "/delete/" + EncodeUtils.urlsafeEncode(entryURI)
//    executeUrl(url)
//  }
//
//  /** 列出文件
//
//    * @return marker,List[Map]
//
//    */
//  def list(scope: String, prefix: String = null, marker: String = null, limit: Int = 0) = {
//    val params = Map("bucket=" -> scope, "&marker=" -> marker, "&prefix=" -> prefix, "&limit=" -> limit.toString) filter {
//      case (k, v) => v != null && v.length() > 0
//    }
//    val url = rsfHost + "/list?" + params.toList.map(kv => kv._1 + kv._2).mkString
//    val jsonStr = executeUrl(url)
//    val data = JSON.parseFull(jsonStr).getOrElse(Map.empty).asInstanceOf[Map[String, AnyRef]]
//    (data.getOrElse("marker", null), data.getOrElse("items", Nil))
//  }
//  def stat(scope: String, key: String) = {
//    val entryURI = scope + ":" + key
//    val url = rsfHost + "/stat/" + EncodeUtils.urlsafeEncode(entryURI)
//    executeUrl(url)
//  }
//
//  /** 执行命令
//
//    * @author 黄林
//
//    */
//  protected def command(cmd: String, src: String, dest: String) = {
//    val encodedSrc = EncodeUtils.urlsafeEncode(src)
//    val encodedDest = EncodeUtils.urlsafeEncode(dest)
//    val url = rsHost + "/" + cmd + "/" + encodedSrc + "/" + encodedDest
//    executeUrl(url)
//  }
//  protected def executeUrl(url: String) = {
//    val client = getHttpClient
//    val postMethod = new HttpPost(url)
//    postMethod.setHeader("Authorization", "QBox " + signRequest(postMethod))
//    EntityUtils.toString(client.execute(postMethod).getEntity())
//  }
//
//  /** get Upload token.
//
//    */
//  def uploadToken(scope: String, callbackUrl: String = "", callbackBody: String = "", returnUrl: String = "", returnBody: String = "", asyncOps: String = "", endUser: String = "", expires: Long = 3600l) = {
//    val qiniuExpires = System.currentTimeMillis() / 1000 + expires
//    val parapms = (Map("scope" -> scope, "callbackUrl" -> callbackUrl, "returnUrl" -> returnUrl,
//      "asyncOps" -> asyncOps, "returnBody" -> returnBody) filter
//                   { case (k, v) => v != null && v.length > 0 }) + ("deadline" -> qiniuExpires)
//    signWithData(JSONObject(parapms).toString().getBytes())
//  }
//
//  /** 私有文件下载
//
//    */
//  def downloadUrl(url: String, expires: Long = 3600l) = {
//    val qiniuExpires = System.currentTimeMillis() / 1000 + expires
//    val baseUrl = url + (if (url.contains("?")) "&" else "?") + "e=" + qiniuExpires
//    baseUrl + "&token=" + sign(baseUrl.getBytes())
//  }
//
//  /** 用于传输数据签名数据
//
//    */
//  protected def signWithData(data: Array[Byte]) = {
//    val accessKeyBytes = accessKey.getBytes()
//    val secretKeyBytes = secretKey.getBytes()
//    val policyBase64 = EncodeUtils.urlsafeEncodeBytes(data)
//
//    val mac = javax.crypto.Mac.getInstance("HmacSHA1")
//    val keySpec = new SecretKeySpec(secretKeyBytes, "HmacSHA1")
//    mac.init(keySpec)
//
//    val digest = mac.doFinal(policyBase64)
//    val digestBase64 = EncodeUtils.urlsafeEncodeBytes(digest)
//    val token = new Array[Byte](accessKeyBytes.length + 30 + policyBase64.length)
//
//    System.arraycopy(accessKeyBytes, 0, token, 0, accessKeyBytes.length)
//    token(accessKeyBytes.length) = ':'
//    System.arraycopy(digestBase64, 0, token, accessKeyBytes.length + 1,
//      digestBase64.length)
//    token(accessKeyBytes.length + 29) = ':'
//    System.arraycopy(policyBase64, 0, token, accessKeyBytes.length + 30,
//      policyBase64.length)
//    new String(token)
//  }
//
//  /** 用于非文件传输的签名数据
//
//    *
//
//    */
//  protected def signRequest(post: HttpPost) = {
//    val uri = post.getURI()
//    val path = uri.getRawPath()
//    val query = uri.getRawQuery()
//    val entity = post.getEntity()
//    val secretKeyBytes = secretKey.getBytes()
//    val mac = javax.crypto.Mac.getInstance("HmacSHA1")
//    val keySpec = new SecretKeySpec(secretKeyBytes, "HmacSHA1")
//    mac.init(keySpec)
//    mac.update(path.getBytes())
//    if (query != null && query.length() != 0) {
//      mac.update('?'.toByte)
//      mac.update(query.getBytes())
//    }
//    mac.update('\n'.toByte)
//    if (entity != null) {
//      val ct = entity.getContentType()
//      if (ct != null
//          && ct.getValue() == "application/x-www-form-urlencoded") {
//        val w = new ByteArrayOutputStream()
//        entity.writeTo(w)
//        mac.update(w.toByteArray())
//      }
//    }
//    val digest = mac.doFinal()
//    val digestBase64 = EncodeUtils.urlsafeEncodeBytes(digest)
//    val b = new StringBuffer()
//    b.append(accessKey)
//    b.append(':')
//    b.append(new String(digestBase64))
//    b.toString()
//  }
//
//  /** 用于非公开文件下载的签名数据
//
//    */
//  def sign(data: Array[Byte]) = {
//    val mac = javax.crypto.Mac.getInstance("HmacSHA1")
//    val secretKey = this.secretKey.getBytes()
//    val secretKeySpec = new SecretKeySpec(secretKey, "HmacSHA1")
//    mac.init(secretKeySpec)
//    val encodedSign = EncodeUtils.urlsafeEncodeString(mac.doFinal(data))
//    accessKey + ":" + encodedSign
//  }
//}
//private object EncodeUtils {
//  def urlsafeEncodeBytes(src: Array[Byte]) = {
//    if (src.length % 3 == 0) {
//      encodeBase64Ex(src)
//    } else {
//      val b = encodeBase64Ex(src)
//      if (b.length % 4 == 0) {
//        b
//      } else {
//        val pad = 4 - b.length % 4
//        val b2 = new Array[Byte](b.length + pad)
//        System.arraycopy(b, 0, b2, 0, b.length)
//        b2(b.length) = '='
//        if (pad > 1) {
//          b2(b.length + 1) = '='
//        }
//        b2
//      }
//    }
//  }
//  def urlsafeEncodeString(src: Array[Byte]) = {
//    new String(urlsafeEncodeBytes(src))
//  }
//  def urlsafeEncode(src: String) = {
//    urlsafeEncodeString(src.getBytes())
//  }
//
//  // replace '/' with '_', '+" with '-'
//
//  private def encodeBase64Ex(src: Array[Byte]) = {
//
//    // urlsafe version is not supported in version 1.4 or lower.
//
//    val b64 = Base64.encodeBase64(src)
//
//    for ((b, i) <- b64 zipWithIndex) {
//      if (b == '/') {
//        b64(i) = '_'
//
//      } else if (b == '+') {
//        b64(i) = '-'
//      }
//    }
//    b64
//  }
//}