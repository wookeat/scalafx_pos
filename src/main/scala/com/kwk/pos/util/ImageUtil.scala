package com.kwk.pos.util

import javafx.scene.image.Image
import scalafx.beans.property.ObjectProperty

import scala.language.implicitConversions

object ImageUtil{
  implicit def pathToImageConverter(path: String): ObjectProperty[Image] = {
    val url = getClass.getResource(path)
    println(s"${url} url ${path}")
    ObjectProperty(new Image(url.toString))
  }
}