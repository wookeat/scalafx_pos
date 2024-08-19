package com.kwk.pos.modal

import javafx.scene.image.Image
import scalafx.beans.property.{ObjectProperty, StringProperty}

class Food(_name: String, _price: Double, _available: Boolean, _imagePath: String = "../img/default-image.png") extends Product {
  import com.kwk.pos.util.ImageUtil.pathToImageConverter

  var name = new StringProperty(_name)
  var price = ObjectProperty[Double](_price)
  var available = ObjectProperty[Boolean](_available)
  var imagePath: ObjectProperty[Image] = _imagePath

  override def equals(obj: Any): Boolean = obj match{
    case food: Food =>
      this.name.value.equals(food.name.value)
    case _ =>
      false
  }

  override def hashCode(): Int = this.name.hashCode()
}
