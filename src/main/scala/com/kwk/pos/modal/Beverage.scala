package com.kwk.pos.modal

import javafx.scene.image.Image
import scalafx.beans.property.{ObjectProperty, StringProperty}

// Represent a Beverage object
class Beverage(_name: String, _price: Double, _available: Boolean, _imagePath: String = "../img/default-image.png") extends Product {
  import com.kwk.pos.util.ImageUtil.pathToImageConverter

  var name = new StringProperty(_name)
  var price = ObjectProperty[Double](_price)
  var sweetness = ObjectProperty[Sweetness](Sweetness.Normal) // Sweetness variable for customer to select upon order
  var temperature = ObjectProperty[Temperature](Temperature.Normal) // Temperature variable for customer to select upon order
  var available = ObjectProperty[Boolean](_available)
  var imagePath: ObjectProperty[Image] = _imagePath // Uses ImageUtil to convert the image path automatically to the ObjectProperty

  // Override equals method to compare this food object with another food object based on name, sweetness and temperature.
  // It is used to aggregate same Beverage in the OrderItem class
  override def equals(obj: Any): Boolean = obj match{
    case beverage: Beverage =>
      this.name.value.filterNot(_.isWhitespace).toLowerCase.equals(beverage.name.value.filterNot(_.isWhitespace).toLowerCase) &&
        beverage.sweetness.value.equals(this.sweetness.value) &&
        beverage.temperature.value.equals(this.temperature.value)
    case _ =>
      false
  }

  override def hashCode(): Int = this.name.value.hashCode + this.sweetness.value.hashCode() + this.temperature.value.hashCode()

}
