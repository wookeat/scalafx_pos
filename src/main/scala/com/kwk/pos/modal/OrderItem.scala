package com.kwk.pos.modal

import scalafx.beans.property.ObjectProperty

class OrderItem(val product: Product, var quantity: ObjectProperty[Integer]) {
  val sum: ObjectProperty[Double] = ObjectProperty[Double](product.price.value * quantity.value)

  quantity.onChange{(_, oldValue, newValue) =>
    sum.value = product.price.value * newValue
    println(s"sum: ${sum.value}")
  }

  override def equals(obj: Any): Boolean = obj match{
    case orderItem: OrderItem =>
      orderItem.product.equals(this.product)
    case _ =>
      false
  }

  override def hashCode(): Int = this.product match {
    case beverage: Beverage =>
      beverage.hashCode()
    case food: Food =>
      food.hashCode()
  }
}
