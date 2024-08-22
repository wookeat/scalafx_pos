package com.kwk.pos.view

import com.kwk.pos.MainApp
import com.kwk.pos.modal.{Food, Product}
import scalafx.beans.property.{BooleanProperty, ObjectProperty, StringProperty}
import scalafx.event.ActionEvent
import scalafx.scene.control.{Alert, Button, Label, RadioButton, TextField, ToggleGroup}
import scalafx.stage.{FileChooser, Stage}
import scalafxml.core.macros.sfxml
import scalafx.Includes._
import javafx.scene.image.Image
import scalafx.stage.FileChooser.ExtensionFilter

import java.io.File
import java.net.URL
import scala.util.{Failure, Success, Try}

trait FoodMenuEditDialogTrait{
  var dialogStage: Stage = _
  var okClicked: Boolean = false
  var product: Product = _
  var operation: String = _
  def initialize(): Unit
}

@sfxml
class OverviewMenuEditDialogController(
                                    private val foodIdTextField: TextField,
                                    private val foodNameTextField: TextField,
                                    private val foodPriceTextField: TextField,
                                    private val trueRadioButton: RadioButton,
                                    private val falseRadioButton: RadioButton,
                                    private val operationLabel: Label,
                                    private val cancelButton: Button,
                                    private val confirmButton: Button,
                                    private val filePathTextField: TextField
                                  ) extends FoodMenuEditDialogTrait{
  val fileChooser = new FileChooser{
    title = "Open Resource File"
    new ExtensionFilter("Image Files", Seq("*.png", "*.jpg", "*.jpeg"))
  }
  val toggleState = ObjectProperty(false)
  val toggleGroup = new ToggleGroup()
  trueRadioButton.toggleGroup = toggleGroup
  falseRadioButton.toggleGroup = toggleGroup

  def initialize(): Unit = {
    operationLabel.text.value = operation
    foodNameTextField.text.value = product.name.value
    foodPriceTextField.text.value = product.price.value.toString
    filePathTextField.disable = true
    trueRadioButton.setSelected(product.available.value)
    falseRadioButton.setSelected(!product.available.value)
  }

  trueRadioButton.selected.onChange((a,b,c) => {
    toggleState <== ObjectProperty(trueRadioButton.selected.value)
  })


  def convertTextToDouble(textField: TextField): ObjectProperty[Double] = {
    ObjectProperty(textField.text.value.toDouble)
  }

  def handleBrowseFile(actionEvent: ActionEvent): Unit = {
    val selectedFilePath = fileChooser.showOpenDialog(MainApp.stage).toPath
    filePathTextField.text <== StringProperty(selectedFilePath.toString)
  }

  def handleConfirm(actionEvent: ActionEvent): Unit = {
    if(isInputValid()){
      product.name <== foodNameTextField.text
      product.available <== toggleState
      product.price <== convertTextToDouble(foodPriceTextField)
      filePathTextField.text.value match {
        case s if s.isEmpty => s
        case _ =>
          val file = new File(filePathTextField.text.value)
          product.imagePath = ObjectProperty[Image](new Image(file.toURI.toURL.toString))
      }
      okClicked = true
      dialogStage.close()
    }
  }

  def handleCancel(actionEvent: ActionEvent): Unit = {
    dialogStage.close()
  }

  def nullChecking(x: String): Boolean = x == null || x.length == 0


  def isInputValid(): Boolean = {
    var errorMessage = ""

    if(nullChecking(foodNameTextField.text.value)) errorMessage += "Invalid food name!\n"

    if(nullChecking(foodPriceTextField.text.value)) errorMessage += "Invalid food price!\n"
    else{
      val check = Try(foodPriceTextField.text.value.toDouble)
      check match {
        case Failure(exception) =>  errorMessage += "Invalid food price (must be a double)!\n"
        case Success(value) =>
          if(value <= 0){
            errorMessage += "Invalid food price (must be greater than 0)!"
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