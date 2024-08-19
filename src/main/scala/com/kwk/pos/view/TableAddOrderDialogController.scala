package com.kwk.pos.view

import scalafx.scene.control.{Label, TextField}
import scalafxml.core.macros.sfxml
import com.kwk.pos.modal.{Product, Table}
import scalafx.beans.property.ObjectProperty
import scalafx.event.ActionEvent
import scalafx.stage.Stage

trait TableAddOrderDialogTrait{
  def initialize(): Unit
  var dialogStage: Stage = _
  var product: Product = _
  var table: Table = _
  var okClicked: Boolean = false
}

@sfxml
class TableAddOrderDialogController(
                             private val productNameLabel: Label,
                             private val quantityTextField: TextField
                             ) extends TableAddOrderDialogTrait{

  val quantity: ObjectProperty[Integer] = ObjectProperty(1)

  def initialize(): Unit = {
    productNameLabel.text.value = product.name.value
    quantityTextField.text = quantity.value.toString
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
    table.addItem(product, quantity.value)
    dialogStage.close()
  }

  def handleCancel(actionEvent: ActionEvent): Unit = {
    dialogStage.close()
  }

  quantityTextField.text.onChange((a,b,newValue) => quantityTextField.text <== quantity.asString())
}