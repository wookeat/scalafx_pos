package com.kwk.pos.view

import com.kwk.pos.MainApp
import com.kwk.pos.modal.{Beverage, Food, Product, Table}
import scalafx.geometry.Pos
import scalafx.scene.{Cursor, Parent}
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout.{AnchorPane, ColumnConstraints, GridPane, Priority, RowConstraints, StackPane}
import scalafx.scene.paint.Color
import scalafx.scene.shape.Circle
import scalafx.scene.text.Text
import scalafxml.core.macros.sfxml
import scalafx.Includes._
import scalafx.beans.property.{ObjectProperty, StringProperty}
import scalafx.event.ActionEvent
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.{Alert, Button, Label, TabPane, TableColumn, TableRow, TableView}
import scalafx.scene.image.ImageView


trait OverviewTrait{
  def getTabPane: TabPane
  def initialize(): Unit
}


// Controller that manages the whole Main menu of the Point of Sale System
@sfxml
class OverviewController(
                        private val tabPane: TabPane,
                        private val tableLayoutAnchorPane: AnchorPane,

                        private val foodIdLabel: Label,
                        private val foodNameLabel: Label,
                        private val foodPriceLabel: Label,
                        private val foodMenuTableView: TableView[Food],
                        private val foodMenuNumberColumn: TableColumn[Food, Integer],
                        private val foodMenuNameColumn: TableColumn[Food, String],
                        private val foodMenuPriceColumn: TableColumn[Food, Double],
                        private val foodMenuAvailableColumn: TableColumn[Food, Boolean],
                        private val removeFoodButton: Button,
                        private val editFoodButton: Button,
                        private val foodImageView: ImageView,


                        private val beverageIdLabel: Label,
                        private val beverageNameLabel: Label,
                        private val beveragePriceLabel: Label,
                        private val beverageMenuTableView: TableView[Beverage],
                        private val beverageMenuNumberColumn: TableColumn[Beverage, Integer],
                        private val beverageMenuNameColumn: TableColumn[Beverage, String],
                        private val beverageMenuPriceColumn: TableColumn[Beverage, Double],
                        private val beverageMenuAvailableColumn: TableColumn[Beverage, Boolean],
                        private val removeBeverageButton: Button,
                        private val editBeverageButton: Button,
                        private val beverageImageView: ImageView,

                        ) extends OverviewTrait{
  def getTabPane: TabPane = tabPane
  removeFoodButton.disable = true
  editFoodButton.disable = true

  removeBeverageButton.disable = true
  editBeverageButton.disable = true


  def initialize(): Unit = {
    initializeTableLayout()

    // Initialize the table view in food menu tab
    foodMenuTableView.items = MainApp.menu
    foodMenuNameColumn.cellValueFactory = { _.value.name }
    foodMenuPriceColumn.cellValueFactory = { _.value.price }
    foodMenuAvailableColumn.cellValueFactory = { _.value.available }
    foodMenuNumberColumn.cellValueFactory = { cellData =>
      ObjectProperty[Integer](foodMenuTableView.items.value.indexOf(cellData.value) + 1)
    }

    // Initialize the table view in beverage menu tab
    beverageMenuTableView.items = MainApp.beverageMenu
    beverageMenuNameColumn.cellValueFactory = { _.value.name }
    beverageMenuPriceColumn.cellValueFactory = { _.value.price }
    beverageMenuAvailableColumn.cellValueFactory = { _.value.available }
    beverageMenuNumberColumn.cellValueFactory = { cellData =>
      ObjectProperty[Integer](beverageMenuTableView.items.value.indexOf(cellData.value) + 1)
    }
  }

  // Method to initialize the table layout in the restaurant by adding the scalafx element/node dynamically
  private def initializeTableLayout(): Unit = {
    val layoutGridPane = new GridPane()
    layoutGridPane.prefWidth <== tableLayoutAnchorPane.width
    layoutGridPane.prefHeight <== tableLayoutAnchorPane.height
    layoutGridPane.hgrow = Priority.Always
    layoutGridPane.vgrow = Priority.Always

    // Ensure the layout to be 3 x 3 grid layout with equal size grid
    for (_ <- 0 until 3) {
      val column = new ColumnConstraints {
        hgrow = Priority.Always
        percentWidth = 100.0 / 3
      }
      layoutGridPane.columnConstraints += column
    }

    for (_ <- 0 until 3) {
      val row = new RowConstraints {
        vgrow = Priority.Always
        percentHeight = 100.0 / 3
      }
      layoutGridPane.rowConstraints += row
    }

    tableLayoutAnchorPane.children.add(layoutGridPane)

    for(i <- 0 until  MainApp.table.size()){
      val circle = new Circle {
        radius = 60.0
        cursor = Cursor.Hand
        alignmentInParent = Pos.Center
        userData = MainApp.table(i)
        // Change the colour of the table based on its order
        if(MainApp.table(i).order.nonEmpty){
          fill = Color.web("#FDC848")
        }else{
          fill = Color.web("#06D6A0")
        }
      }

      // Handle the table onClick Event
      circle.onMouseClicked = (e: MouseEvent) => {
        val circle = e.source.asInstanceOf[javafx.scene.shape.Circle]
        val tableData = circle.getUserData.asInstanceOf[Table]
        MainApp.tableOrderTrait.table = tableData
        MainApp.showTableOrder()
      }

      val row = i % 3
      val col = i / 3

      val tableNum = new Text(s"${i + 1}")
      tableNum.setStyle("-fx-font-weight: bold; -fx-font-family: Gilroy-Bold; -fx-font-size: 24")

      val stackPane = new StackPane() // To stack the table number on top of the circle
      stackPane.children.addAll(circle, tableNum)
      layoutGridPane.add(stackPane, row, col)
    }
  }

  // Listener for table view row selection in food menu tab
  foodMenuTableView.selectionModel.value.selectedItem.onChange(
    (a, b, newValue) => {
      showFoodMenuDetails(Option(newValue))
    }
  )

  // Listener for table view row selection in beverage menu tab
  beverageMenuTableView.selectionModel.value.selectedItem.onChange(
    (a, b, newValue) => {
      showBeverageMenuDetails(Option(newValue))
    }
  )

  // Method to display the food details on click in food menu tab
  private def showFoodMenuDetails(food: Option[Food]): Unit = {
    food match{
      case Some(food) =>
        editFoodButton.disable = false
        removeFoodButton.disable = false
        foodNameLabel.text <== food.name
        foodPriceLabel.text <==  StringProperty(f"RM ${food.price.value}%.2f")
        foodImageView.imageProperty().bind(food.imagePath)

      case None =>
        editFoodButton.disable = true
        removeFoodButton.disable = true
        foodNameLabel.text.unbind()
        foodPriceLabel.text.unbind()
        foodImageView.imageProperty().unbind()
        foodImageView.setImage(null)
        foodNameLabel.text = ""
        foodPriceLabel.text = ""
    }
  }
  showFoodMenuDetails(None)

  // Method to display the beverage details on click in beverage menu tab
  private def showBeverageMenuDetails(beverage: Option[Beverage]): Unit = {
    beverage match{
      case Some(beverage) =>
        editBeverageButton.disable = false
        removeBeverageButton.disable = false
        beverageNameLabel.text <== beverage.name
        beveragePriceLabel.text <== StringProperty(f"RM ${beverage.price.value}%.2f")
        beverageImageView.imageProperty().bind(beverage.imagePath)

      case None =>
        editBeverageButton.disable = true
        removeBeverageButton.disable = true
        beverageNameLabel.text.unbind()
        beveragePriceLabel.text.unbind()
        beverageImageView.imageProperty().unbind()
        beverageImageView.setImage(null)
        beverageNameLabel.text = ""
        beveragePriceLabel.text = ""
    }
  }

  showBeverageMenuDetails(None)

  // Event handler to add new food into Food menu
  def handleAddNewFood(actionEvent: ActionEvent): Unit = {
    val food = new Food("", 0.00, false)
    val okClicked = MainApp.showFoodMenuEditDialog(food, "Add New Food")
    if(okClicked) {
      if(MainApp.menu.indexOf(food) == -1) {
        MainApp.menu += food
      }else{
        val alert = new Alert(Alert.AlertType.Error){
          title = "Error: Add Item Operation"
          headerText = "Please correct invalid fields"
          contentText = "Duplicated Item!"
        }.showAndWait()
      }
    }
  }

  // Event handler to add new beverage into Beverage menu
  def handleAddNewBeverage(actionEvent: ActionEvent): Unit = {
    val beverage = new Beverage("", 0.00, false)
    val okClicked = MainApp.showBeverageMenuEditDialog(beverage, "Add New Beverage")
    if(okClicked) {
      if(MainApp.beverageMenu.indexOf(beverage) == -1) {
        MainApp.beverageMenu += beverage
      }else{
        val alert = new Alert(Alert.AlertType.Error){
          title = "Error: Add Item Operation"
          headerText = "Please correct invalid fields"
          contentText = "Duplicated Item!"
        }.showAndWait()
      }
    }
  }

  // Event handler to edit food details in Food menu
  def handleEditFood(actionEvent: ActionEvent): Unit = {
    val food = foodMenuTableView.selectionModel.value.selectedItem.value
    MainApp.showFoodMenuEditDialog(food, "Edit Food")
  }

  // Event handler to edit beverage details in Beverage menu
  def handleEditBeverage(actionEvent: ActionEvent): Unit = {
    val beverage = beverageMenuTableView.selectionModel.value.selectedItem.value
    MainApp.showBeverageMenuEditDialog(beverage, "Edit Beverage")
  }

  // Event handler to remove food from Food menu
  def handleRemoveFood(actionEvent: ActionEvent): Unit = {
    val index = foodMenuTableView.selectionModel.value.getSelectedIndex
    if(index >= 0) {
      MainApp.menu.remove(index)
    }
  }

  // Event handler to remove beverage from Beverage menu
  def handleRemoveBeverage(actionEvent: ActionEvent): Unit = {
    val index = beverageMenuTableView.selectionModel.value.getSelectedIndex
    if(index >= 0) {
      MainApp.beverageMenu.remove(index)
    }
  }
}