package com.kwk.pos.util

import javafx.scene.image.Image
import scalafx.beans.property.ObjectProperty

import scala.language.implicitConversions

object ImageUtil{
  // Implicit function to convert image path to ObjectProperty[Image] for new Food/Beverage object
  implicit def pathToImageConverter(path: String): ObjectProperty[Image] = {
    val url = getClass.getResource(path)
    ObjectProperty(new Image(url.toString))
  }
}