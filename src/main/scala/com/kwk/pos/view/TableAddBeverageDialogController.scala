package com.kwk.pos.view

import scalafx.scene.control.{Label, RadioButton, TextField, ToggleGroup}
import scalafxml.core.macros.sfxml
import com.kwk.pos.modal.{Beverage, Product, Sweetness, Table, Temperature}
import scalafx.beans.property.{IntegerProperty, ObjectProperty}
import scalafx.event.ActionEvent
import scalafx.stage.Stage
import scalafx.Includes._

trait TableAddBeverageDialogTrait{
  def initialize(): Unit
  var dialogStage: Stage = _
  var beverage: Beverage = _
  var table: Table = _
  var okClicked: Boolean = false
}

@sfxml
class TableAddBeverageDialogController(
                                     private val productNameLabel: Label,
                                     private val quantityTextField: TextField,
                                     private val nonSweetRadioButton: RadioButton,
                                     private val normalSweetRadioButton: RadioButton,
                                     private val extraSweetRadioButton: RadioButton,
                                     private val coldTempRadioButton: RadioButton,
                                     private val normalTempRadioButton: RadioButton,
                                     private val hotTempRadioButton: RadioButton
                                   ) extends TableAddBeverageDialogTrait{

  val quantity: ObjectProperty[Integer] = ObjectProperty(1)
  val sweetnessToggleState = ObjectProperty(Sweetness.Normal)
  val tempToggleState = ObjectProperty(Temperature.Normal)
  val sweetnessToggleGroup = new ToggleGroup()
  val tempToggleGroup = new ToggleGroup()

  nonSweetRadioButton.toggleGroup = sweetnessToggleGroup
  nonSweetRadioButton.userData = Sweetness.NonSweet
  normalSweetRadioButton.toggleGroup = sweetnessToggleGroup
  normalSweetRadioButton.userData = Sweetness.Normal
  extraSweetRadioButton.toggleGroup = sweetnessToggleGroup
  extraSweetRadioButton.userData = Sweetness.ExtraSweet

  coldTempRadioButton.toggleGroup = tempToggleGroup
  coldTempRadioButton.userData = Temperature.Cold
  normalTempRadioButton.toggleGroup = tempToggleGroup
  normalTempRadioButton.userData = Temperature.Normal
  hotTempRadioButton.toggleGroup = tempToggleGroup
  hotTempRadioButton.userData = Temperature.Hot

  def initialize(): Unit = {
    productNameLabel.text.value = beverage.name.value
    quantityTextField.text = quantity.value.toString
    normalSweetRadioButton.setSelected(true)
    normalTempRadioButton.setSelected(true)
  }

  tempToggleGroup.selectedToggle.onChange((a,b,newValue) => {
    tempToggleState.value = newValue.getUserData.asInstanceOf[Temperature]
  })

  sweetnessToggleGroup.selectedToggle.onChange((a,b,newValue) => {
    sweetnessToggleState.value = newValue.getUserData.asInstanceOf[Sweetness]
  })

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
    val tempBeverage = new Beverage(beverage.name.value, beverage.price.value, beverage.available.value)
    tempBeverage.sweetness <== sweetnessToggleState
    tempBeverage.temperature <== tempToggleState
    table.addItem(tempBeverage, quantity.value)
    println(s"${tempBeverage.sweetness.value.sweetness}")
    dialogStage.close()
  }

  def handleCancel(actionEvent: ActionEvent): Unit = {
    dialogStage.close()
  }

  quantityTextField.text.onChange((a,b,newValue) => quantityTextField.text <== quantity.asString())
}