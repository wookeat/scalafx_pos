package com.kwk.pos.modal

import scalafx.beans.property.ObjectProperty

// Represents as an order item that encapsulates a Food or Beverage item with quantity
class OrderItem(val product: Product, var quantity: ObjectProperty[Integer]) {
  val sum: ObjectProperty[Double] = ObjectProperty[Double](product.price.value * quantity.value)

  // Event listener to identify quantity changes and calculate the new sum of this product
  quantity.onChange{(_, oldValue, newValue) =>
    sum.value = product.price.value * newValue
  }

  // Override this equals method to aggregate the same order item together
  override def equals(obj: Any): Boolean = obj match{
    case orderItem: OrderItem =>
      orderItem.product.equals(this.product)
    case _ =>
      false
  }

  override def hashCode(): Int = this.product.hashCode()
}
