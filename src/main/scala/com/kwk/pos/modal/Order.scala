package com.kwk.pos.modal

import scalafx.beans.property.ObjectProperty
import scalafx.collections.ObservableBuffer
import java.time.LocalDateTime

// Representation of an Order object for each table that contains buffer to hold all the order item for that table
case class Order(time: LocalDateTime){
  val totalSum: ObjectProperty[Double] = ObjectProperty[Double](0.00)
  val items: ObservableBuffer[OrderItem] = ObservableBuffer()

  // Calculate the total sum of all the order items in the buffer
  items.onChange((a, _) => {
    totalSum.value = calculateTotalSum()
    a.foreach(x => {
      x.sum.onChange({
        totalSum.value = calculateTotalSum()
      })
    })
  })

  def calculateTotalSum(): Double = {
    val total = items.foldRight(0.00)((x, acc) => acc + x.sum.value)
    total
  }

  // Method to add item into the buffer
  def addItem(product: Product, quantity: Integer): Unit = {
    val newOrderItem = new OrderItem(product, ObjectProperty[Integer](quantity))
    println(newOrderItem.hashCode())
    val exist = items.find(x => x.equals(newOrderItem))

    exist match {
      case Some(orderItem: OrderItem) => orderItem.quantity <== ObjectProperty[Integer](orderItem.quantity.value + quantity)
      case None => items += newOrderItem
    }

//    if(index >= 0){
//      items.get(index).quantity = ObjectProperty(items.ge newOrderItem.quantity.value
//    }else{
//      items += newOrderItem
//    }
  }

  def removeItem(orderItem: OrderItem): Unit = {
    items.remove(orderItem)
  }
}