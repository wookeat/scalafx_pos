package com.kwk.pos.modal

import javafx.scene.image.Image
import scalafx.beans.property.{ObjectProperty, StringProperty}

class Beverage(_name: String, _price: Double, _available: Boolean, _imagePath: String = "../img/default-image.png") extends Product {
  import com.kwk.pos.util.ImageUtil.pathToImageConverter

  var name = new StringProperty(_name)
  var price = ObjectProperty[Double](_price)
  var sweetness = ObjectProperty[Sweetness](Sweetness.Normal)
  var temperature = ObjectProperty[Temperature](Temperature.Normal)
  var available = ObjectProperty[Boolean](_available)
  var imagePath: ObjectProperty[Image] = _imagePath

  override def equals(obj: Any): Boolean = obj match{
    case beverage: Beverage =>
      this.name.value.equals(beverage.name.value) && beverage.sweetness.value.equals(this.sweetness.value) && beverage.temperature.value.equals(this.temperature.value)
    case _ =>
      false
  }

  override def hashCode(): Int = this.name.value.hashCode + this.sweetness.value.hashCode() + this.temperature.value.hashCode()

}
