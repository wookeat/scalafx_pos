package com.kwk.pos.view

import com.kwk.pos.MainApp
import com.kwk.pos.modal.{OrderItem, Table}
import scalafx.beans.property.ObjectProperty
import scalafx.event.ActionEvent
import scalafx.scene.control.{Button, TextField}
import scalafx.stage.Stage
import scalafxml.core.macros.sfxml
import scalafx.Includes._
import scalafx.util.StringConverter

import scala.util.Try

trait TableEditDialogTrait{
  var orderItem: OrderItem = _
  var dialogStage: Stage = _
  var okClicked: Boolean = false
}


@sfxml
class TableEditDialogController(
                               private val removeItemButton: Button,
                               private val decreaseButton: Button,
                               private val increaseButton: Button,
                               private val confirmButton: Button,
                               private val quantityTextField: TextField
                               )extends TableEditDialogTrait{

  val quantity: ObjectProperty[Integer] = ObjectProperty(0)

  def initialize(): Unit = {
    quantityTextField.text = orderItem.quantity.value.toString
    quantity.value = orderItem.quantity.value
  }

  def handleIncrease(actionEvent: ActionEvent): Unit = {
    quantity.value += 1
  }

  def handleDecrease(actionEvent: ActionEvent): Unit = {
    if(quantity.value > 1){
      quantity.value -= 1
    }else{
      quantity.value = 1
    }
  }

  def handleConfirm(actionEvent: ActionEvent): Unit = {
    orderItem.quantity <== ObjectProperty[Integer](quantityTextField.text.value.toInt)
    dialogStage.close()
  }

  def handleRemove(actionEvent: ActionEvent): Unit = {
    MainApp.tableOrderTrait.table.order.remove(orderItem)
    dialogStage.close()
  }

//  def isInputValid(): Boolean = {
//
//  }

  val converter = new StringConverter[Integer] {
    override def toString(int: Integer): String = if (int == null) "" else int.toString
    override def fromString(string: String): Integer =
      if (string == null || string.trim.isEmpty) 0
      else string.toInt
  }

//  quantityTextField.text <==> quantity
  quantityTextField.text.onChange((a,b,newValue) => quantityTextField.text <== quantity.asString())
}