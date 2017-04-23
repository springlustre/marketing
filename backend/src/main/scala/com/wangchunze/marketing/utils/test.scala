package com.wangchunze.marketing.utils

/**
  * Created by springlustre on 2017/4/23.
  */
object test {

  def main(args: Array[String]): Unit = {
    val file = "diary.jpg"
    println(file+"+++++++++")
    val arr = file.split("\\.")
    println(arr.toList)
  }
}
