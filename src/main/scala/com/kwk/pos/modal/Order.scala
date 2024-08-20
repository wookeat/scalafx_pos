package com.kwk.pos.modal

import scalafx.beans.property.ObjectProperty
import scalafx.collections.ObservableBuffer

import java.time.LocalDateTime

case class Order(time: LocalDateTime){
  val totalSum: ObjectProperty[Double] = ObjectProperty[Double](0.00)
  val items: ObservableBuffer[OrderItem] = ObservableBuffer()

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

  def addItem(product: Product, quantity: Integer): Unit = {
    val newOrderItem = new OrderItem(product, ObjectProperty[Integer](quantity))
    println(newOrderItem.hashCode())
    val index = items.indexOf(newOrderItem)
    println(s"Contains: ${items.contains(newOrderItem)}")
    println(index)
    if(index >= 0){
      items.get(index).quantity.value += newOrderItem.quantity.value
    }else{
      items += newOrderItem
    }

  }
}