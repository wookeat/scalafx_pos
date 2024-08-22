package com.kwk.pos.view

import com.kwk.pos.MainApp
import com.kwk.pos.modal.{OrderItem, Table}
import scalafx.beans.property.StringProperty
import scalafx.event.ActionEvent
import scalafx.scene.control.{Alert, Button, Label, TableColumn, TableView, TextField}
import scalafx.scene.layout.StackPane
import scalafx.scene.shape.Rectangle
import scalafx.stage.Stage
import scalafxml.core.macros.sfxml

import scala.util.{Failure, Success, Try}

trait PaymentDialogTrait{
  var table: Table = _
  var dialogStage: Stage = _
  var okClicked = false
}

@sfxml
class PaymentDialogController(
                               private val paymentTableView: TableView[OrderItem],
                               private val productNameTableColumn: TableColumn[OrderItem, String],
                               private val quantityTableColumn: TableColumn[OrderItem, Integer],
                               private val sumTableColumn: TableColumn[OrderItem, Double],
                               private val productTotalLabel: Label,
                               private val taxLabel: Label,
                               private val balanceLabel: Label,
                               private val totalLabel: Label,
                               private val chargedLabel: Label,
                               private val oneRinggitStackPane: StackPane,
                               private val fiveRinggitStackPane: StackPane,
                               private val tenRinggitStackPane: StackPane,
                               private val twentyRinggitStackPane: StackPane,
                               private val fiftyRinggitStackPane: StackPane,
                               private val hundredRinggitStackPane: StackPane,
                               private val customAmountTextField: TextField,
                               private val confirmButton: Button,
                               private val cancelButton: Button
                             ) extends PaymentDialogTrait{

  val stackPaneValues = List(
    (1.0, oneRinggitStackPane),
    (5.0, fiveRinggitStackPane),
    (10.0, tenRinggitStackPane),
    (20.0, twentyRinggitStackPane),
    (50.0, fiftyRinggitStackPane),
    (100.0, hundredRinggitStackPane)
  )

  def initialize(): Unit = {
    paymentTableView.items = table.order.get.items

    productNameTableColumn.cellValueFactory = { _.value.product.name }
    quantityTableColumn.cellValueFactory = { _.value.quantity }
    sumTableColumn.cellValueFactory = { _.value.sum }

    val productTotal = table.order.get.totalSum.value
    val tax = productTotal * 0.1
    val balance = productTotal + tax
    productTotalLabel.text <== StringProperty(f"RM $productTotal%.2f")
    taxLabel.text <== StringProperty(f"RM $tax%.2f")
    balanceLabel.text <== StringProperty(f"RM $balance%.2f")
    totalLabel.text <== StringProperty(f"$balance%.2f")

    // Method to ensure the fixed ringgit button that has value less than the payable amount are disabled
    stackPaneValues.foreach { case (value, button) =>
      button.disable = balance > value
    }
  }

  stackPaneValues.foreach{
    case (value, stackPane) =>
      stackPane.onMouseClicked = ev => {
        chargedLabel.text <== StringProperty(f"${value}%.2f")
        customAmountTextField.text.value = f"${value}%.2f"
      }
  }

  customAmountTextField.text.onChange((a, b, newValue) => {
    chargedLabel.text <== StringProperty(newValue)
  })

  def handleCancel(actionEvent: ActionEvent): Unit = {
    dialogStage.close()
  }

  def handleConfirm(actionEvent: ActionEvent): Unit = {
    if(isInputValid()){
      table.order = None
      dialogStage.close()
      MainApp.showOverview()
    }
  }

  def nullChecking(x: String): Boolean = x == null || x.length == 0

  def isInputValid(): Boolean = {
    var errorMessage = ""

    if(nullChecking(chargedLabel.text.value)) errorMessage += "Invalid Payment Amount!\n"
    else{
      val check = Try(chargedLabel.text.value.toDouble)
      check match {
        case Failure(exception) =>  errorMessage += "Invalid Payment Amount (must be a number)!\n"
        case Success(value) =>
          if(value <= 0 || chargedLabel.text.value.toDouble < totalLabel.text.value.toDouble){
            errorMessage += "Invalid Payment Amount!"
          }
      }
    }

    if(errorMessage.length() == 0){
      true
    }else{
      val alert = new Alert(Alert.AlertType.Error){
        initOwner(dialogStage)
        title = "Invalid Fields"
        headerText = "Please correct invalid fields"
        contentText = errorMessage
      }.showAndWait()
      false
    }
  }
}