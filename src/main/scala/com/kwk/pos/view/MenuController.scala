package com.kwk.pos.view

import com.kwk.pos.MainApp
import com.kwk.pos.modal.{Food, Product, Table}
import scalafx.event.ActionEvent
import scalafx.scene.control.{Button, Label, TableColumn, TableView, TextField}
import scalafx.scene.layout.{AnchorPane, Pane}
import scalafxml.core.macros.sfxml
import scalafx.Includes._
import scalafx.beans.property.ObjectProperty

trait MenuTrait{
  var table: Table = _
  def getAnchorPane: Pane
}

@sfxml
class MenuController(
                      private val menuTable: TableView[Food],
                      private val productNameColumn: TableColumn[Food, String],
                      private val productNameLabel: Label,
                      private val productPriceLabel: Label,
                      private val productAvailabilityLabel: Label,
                      private val anchorPane: AnchorPane,
                      private val decreaseButton: Button,
                      private val increaseButton: Button,
                      private val quantityTextField: TextField,
                      private val addItemButton: Button,
                      private val backButton: Button
                    ) extends MenuTrait{
  def getAnchorPane: Pane = anchorPane
  val quantity: ObjectProperty[Integer] = ObjectProperty(1)
  addItemButton.disable = true
  menuTable.items = MainApp.menu
  productNameColumn.cellValueFactory = {_.value.name}
  quantityTextField.text <== quantity.asString()

  private def showMenuDetails(product: Option[Product]): Unit = {
    product match {
      case Some(product) =>
        productNameLabel.text <== product.name
        productPriceLabel.text <== product.price.asString
        productAvailabilityLabel.text <== product.available.asString

      case None =>
        productNameLabel.text.unbind()
        productPriceLabel.text.unbind()
        productAvailabilityLabel.text.unbind()
        productNameLabel.text = ""
        productPriceLabel.text = ""
        productAvailabilityLabel.text = ""
    }
  }

  def handleAddItem(action: ActionEvent): Unit = {
    val selectedItem = menuTable.selectionModel().selectedItem.value
    println(selectedItem)
    table.order.get.addItem(selectedItem, quantity.value)
    quantity.value = 1
  }

  def handleDecrease(actionEvent: ActionEvent): Unit = {
    if(quantity.value > 1){
      quantity.value -= 1
    }else{
      quantity.value = 1
    }
    println("Decrease")
  }

  def handleIncrease(actionEvent: ActionEvent): Unit = {
    quantity.value += 1
    println("Increase")
  }
  
  def handleBack(actionEvent: ActionEvent): Unit = {
    val pane = MainApp.tableOverviewTrait.getRightPane
    pane.children.clear()
    pane.children.add(MainApp.menuSelectionTrait.getMenuSelectionPane)
  }

  quantityTextField.text.onChange(
    (a, b, newValue) => {
      quantityTextField.text <== quantity.asString()
    }
  )

  menuTable.selectionModel.value.selectedItem.onChange(
    (a, b,newValue) => {
      addItemButton.disable = false
      showMenuDetails(Some(newValue))
      quantity.value = 1
    }
  )
}
