package com.kwk.pos.modal

import javafx.scene.image.Image
import scalafx.beans.property.{ObjectProperty, StringProperty}

// Represent a Food object
class Food(_name: String, _price: Double, _available: Boolean, _imagePath: String = "../img/default-image.png") extends Product {
  import com.kwk.pos.util.ImageUtil.pathToImageConverter

  var name = new StringProperty(_name)
  var price = ObjectProperty[Double](_price)
  var available = ObjectProperty[Boolean](_available)
  var imagePath: ObjectProperty[Image] = _imagePath // Uses ImageUtil to convert the image path automatically to the ObjectProperty

  // Override equals method to compare this food object with another food object based on name. It is used to aggregate same Food
  // in the OrderItem class
  override def equals(obj: Any): Boolean = obj match{
    case food: Food =>
      this.name.value.filterNot(_.isWhitespace).toLowerCase.equals(food.name.value.filterNot(_.isWhitespace).toLowerCase)
    case _ =>
      false
  }

  override def hashCode(): Int = this.name.hashCode()
}
