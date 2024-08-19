package com.kwk.pos.modal

import scalafx.beans.property.{ObjectProperty, StringProperty}
import scalafx.collections.ObservableBuffer

class Table(idS: String){
  val id = StringProperty(idS)
  val order = ObservableBuffer[OrderItem]()
  val totalSum = ObjectProperty[Double](0.00)

  order.onChange((a, _) => {
    totalSum.value = calculateTotalSum()
    a.foreach(x => {
      x.sum.onChange({
        totalSum.value = calculateTotalSum()
      })
    })
  })

  def calculateTotalSum(): Double = {
    val total = order.foldRight(0.00)((x, acc) => acc + x.sum.value)
    total
  }

  def addItem(product: Product, quantity: Integer): Unit = {
    val newOrderItem = new OrderItem(product, ObjectProperty[Integer](quantity))
    println(newOrderItem.hashCode())
    val index = order.indexOf(newOrderItem)
    println(s"Contains: ${order.contains(newOrderItem)}")
    println(index)
    if(index >= 0){
      order.get(index).quantity.value += newOrderItem.quantity.value
    }else{
      order += newOrderItem
    }

  }

  def deleteItem(orderItem: OrderItem): Unit = {
    println(s"Inside delete: ${orderItem.product.name}")
    val index = order.indexOf(orderItem)
    val targetQuantity = order.get(index).quantity

    if(targetQuantity.value > 1 ){
      targetQuantity.value -= 1
    }else{
      order.remove(index)
    }
  }
}
