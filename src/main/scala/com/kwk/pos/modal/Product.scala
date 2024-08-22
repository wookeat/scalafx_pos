package com.kwk.pos.modal

import javafx.scene.image.Image
import scalafx.beans.property.{ObjectProperty, StringProperty}

// Parent class for Food and Beverage
trait Product {
  def name: StringProperty
  def price: ObjectProperty[Double]
  def available: ObjectProperty[Boolean]
  def imagePath: ObjectProperty[Image]

  // Override equals method to allow the Food and Beverage implements their own
  // customized equals comparison method respectively
  override def equals(obj: Any): Boolean = obj match{
    case product: Product =>
      product match {
        case beverage: Beverage =>
          beverage.equals(this)
        case food: Food =>
          food.equals(this)
        case _ =>
          false
      }

    case _ =>
      false
  }

  override def hashCode(): Int = this match {
    case beverage: Beverage =>
      beverage.hashCode()
    case food: Food =>
      food.hashCode()
  }
}
