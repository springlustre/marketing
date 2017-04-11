package com.wangchunze.marketing

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.util.Timeout
import org.slf4j.LoggerFactory
import com.wangchunze.marketing.common.AppSettings._
import com.wangchunze.marketing.http.HttpService
import scala.concurrent.duration._
import scala.util.{Failure, Success}
/**
  * Created by ZYQ on 2017/1/4.
  *
  */
object Boot extends HttpService  {

  override implicit val system = ActorSystem("marketing")
  override implicit val executor = system.dispatchers.lookup("akka.actor.my-blocking-dispatcher")
  override implicit val materializer = ActorMaterializer()

  override val log = LoggerFactory.getLogger(this.getClass)

  override val timeout = Timeout(1 minutes) // for actor asks


  def main(args: Array[String]) {
    log.info("Starting.")
    val binding = Http().bindAndHandle(routes, httpInterface, httpPort)
    binding.onComplete {
      case Success(b) ⇒
        val localAddress = b.localAddress
        log.info(s"Server is listening on ${localAddress.getHostName}:${localAddress.getPort}")
      case Failure(e) ⇒
        log.error(s"Binding failed with ${e.getMessage}")
        system.terminate()
        System.exit(-1)
    }
  }

}
