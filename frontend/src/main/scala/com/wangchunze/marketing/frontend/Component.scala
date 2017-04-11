package com.wangchunze.marketing.frontend

import org.scalajs.dom.Element

/**
  * User: Taoz
  * Date: 12/26/2016
  * Time: 1:36 PM
  */
trait Component[T <: Element] {

  def render(): T


}
