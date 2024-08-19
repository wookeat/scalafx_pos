package com.kwk.pos.modal

import javafx.scene.image.Image
import scalafx.beans.property.{ObjectProperty, StringProperty}

trait Product {
//  def id:  StringProperty
  def name: StringProperty
  def price: ObjectProperty[Double]
  def available: ObjectProperty[Boolean]
  def imagePath: ObjectProperty[Image]

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

//  override def hashCode(): Int = this.name.hashCode()
}
