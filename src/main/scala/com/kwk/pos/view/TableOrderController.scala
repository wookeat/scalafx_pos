package com.kwk.pos.view

import com.kwk.pos.MainApp
import com.kwk.pos.modal.{Beverage, Food, Order, OrderItem, Table}
import javafx.scene.control.SelectionMode
import scalafx.beans.property.{BooleanProperty, ObjectProperty, StringProperty}
import scalafx.event.ActionEvent
import scalafx.scene.control.{Button, Label, Tab, TableColumn, TableRow, TableView}
import scalafx.Includes._
import scalafx.geometry.{Insets, Pos}
import javafx.{scene => jfxs}
import scalafx.beans.binding.Bindings
import scalafx.scene.Cursor
import scalafx.scene.image.ImageView
import scalafx.scene.layout.{AnchorPane, ColumnConstraints, GridPane, Priority, RowConstraints, StackPane, TilePane, VBox}
import scalafx.scene.shape.Rectangle
import scalafxml.core.macros.sfxml

import java.time.LocalDateTime

trait TableOrderTrait{
  var table: Table = _
  def initialize(): Unit
  def getAnchorPane: AnchorPane
}

@sfxml
class TableOrderController(
                            private val anchorPane: AnchorPane,
                            private val tableNumberLabel: Label,
                            private val tableOrderTable: TableView[OrderItem],
                            private val orderNumberColumn: TableColumn[OrderItem, Integer],
                            private val orderNameColumn: TableColumn[OrderItem, String],
                            private val orderPriceColumn: TableColumn[OrderItem, Double],
                            private val orderQuantityColumn: TableColumn[OrderItem, Integer],
                            private val orderSumColumn: TableColumn[OrderItem, Double],
                            private val totalAmountLabel: Label,
                            private val foodMenuTab: Tab,
                            private val foodMenuAnchorPane: AnchorPane,
                            private val beverageMenuAnchorPane: AnchorPane,
                            private val paymentButton: Button,
                            private val confirmOrderButton: Button
                          ) extends TableOrderTrait{
  def getAnchorPane: AnchorPane = anchorPane
  var order: Order = null
  var paymentStatus: BooleanProperty = null

  def initialize(): Unit = {
    order = table.order match {
      case Some(order) => order   //Retrieve existing order from the table
      case None => Order(LocalDateTime.now())   //Create a new order if the table does not have an existing table
    }

    // To ensure the payment button is active only if there is an order
    paymentStatus = table.order match {
      case Some(order) => BooleanProperty(true)
      case None => BooleanProperty(false)
    }
    paymentButton.disable <== !paymentStatus

    // To ensure the confirm button is active only if there is item in the order
    confirmOrderButton.disable <== BooleanProperty(order.items.isEmpty)
    foodMenuTab.getStyleClass.add("tab-border")
    tableNumberLabel.text = table.id.value
    tableOrderTable.items = order.items

    orderNameColumn.cellValueFactory = { cellData =>
      cellData.value.product match {
        case beverage: Beverage => StringProperty(s"${beverage.name.value} (${beverage.sweetness.value.sweetness}) (${beverage.temperature.value.temperature})")
        case food: Food => food.name
      }
    }
    orderPriceColumn.cellValueFactory = { _.value.product.price }
    orderNumberColumn.cellValueFactory = { cellData =>
      ObjectProperty[Integer](tableOrderTable.items.value.indexOf(cellData.value) + 1)
    }
    orderQuantityColumn.cellValueFactory = { _.value.quantity }
    orderSumColumn.cellValueFactory = { _.value.sum }

    // Initialize row onClick listener to trigger a dialog that allow user to edit the clicked item
    tableOrderTable.setRowFactory( t => {
      val row = new TableRow[OrderItem]()
      row.setOnMouseClicked(e => {
        if(row.getItem != null) {
          MainApp.showTableEditDialog(row.getItem, order)
        }
      })
      row
    })

    totalAmountLabel.text <== StringProperty(f"RM ${order.totalSum.value}%.2f")

    // Ensure the total amount label reflect the changes when items are added or removed
    order.totalSum.onChange((a,b, newValue) => {
      totalAmountLabel.text <== StringProperty(f"RM ${order.totalSum.value}%.2f")
    })

    // Event listener to toggle the confirm order button
    order.items.onChange((a, newValue) => {
      confirmOrderButton.disable <== BooleanProperty(order.items.isEmpty)
    })

    initializeFoodMenu()
    initializeBeverageMenu()

    //
    MainApp.menu.onChange({
      foodMenuAnchorPane.children.clear()
      initializeFoodMenu()
    })

    MainApp.beverageMenu.onChange({
      beverageMenuAnchorPane.children.clear()
      initializeBeverageMenu()
    })
  }

  // Method to initialize the food menu
  def initializeFoodMenu(): Unit = {
    val gridPane = new GridPane(){
      alignmentInParent = Pos.Center
    }
    gridPane.prefHeight <== foodMenuAnchorPane.prefHeight
    gridPane.prefWidth <== foodMenuAnchorPane.prefWidth

    for (_ <- 0 until 3) {
      val column = new ColumnConstraints {
        hgrow = Priority.Always
        percentWidth = 100.0 / 3
      }
      gridPane.columnConstraints += column
    }

    val numRow = (MainApp.menu.size / 3).ceil.toInt
    for (_ <- 0 until numRow) {
      val row = new RowConstraints {
        vgrow = Priority.Always
        percentHeight = 100.0 / 3
      }
      gridPane.rowConstraints += row
    }

    foodMenuAnchorPane.children.add(gridPane)

    for(i <- 0 until MainApp.menu.size){
      val imageView = new ImageView(){
        alignmentInParent = Pos.Center
        fitHeight = 150
        fitWidth = 150
        margin = Insets(10, 0, 0, 0)
      }
      imageView.autosize()
      imageView.imageProperty().bind(MainApp.menu(i).imagePath)

      val foodNameLabel = new Label(){
        alignmentInParent = Pos.Center
      }
      foodNameLabel.text <== MainApp.menu(i).name

      val foodPriceLabel = new Label()
      foodPriceLabel.text <== StringProperty(f"RM ${MainApp.menu(i).price.value}%.2f")

      val vBox = new VBox(){
        prefWidth = 200
        prefHeight = 200
        maxWidth = 200
        maxHeight = 200
        alignment = Pos.Center
        margin = Insets.apply(5,5,5,5)
        styleClass += "box-border"
        cursor = Cursor.Hand
        userData = MainApp.menu(i)
        margin = Insets.apply(15,15,15,15)
      }

      vBox.disable <== BooleanProperty(!MainApp.menu(i).available.value)
      vBox.children.addAll(imageView, foodNameLabel, foodPriceLabel)
      vBox.onMouseClicked = ev => {
        val vbox = ev.source.asInstanceOf[jfxs.layout.VBox]
        vbox.userData match {
          case food: Food => MainApp.showTableAddOrderDialog(food, order)
          case _ => ""
        }
      }

      val row = i % 3
      val col = i / 3

      gridPane.add(vBox, row, col)
    }
  }

  // Method to initialize the beverage menu
  def initializeBeverageMenu(): Unit = {
    val gridPane = new GridPane(){
      alignmentInParent = Pos.Center
    }
    gridPane.prefHeight <== beverageMenuAnchorPane.height
    gridPane.prefWidth <== beverageMenuAnchorPane.width

    for (_ <- 0 until 3) {
      val column = new ColumnConstraints {
        hgrow = Priority.Always
        percentWidth = 100.0 / 3
      }
      gridPane.columnConstraints += column
    }

    val numRow = (MainApp.beverageMenu.size / 3).ceil.toInt
    for (_ <- 0 until numRow) {
      val row = new RowConstraints {
        vgrow = Priority.Always
        percentHeight = 100.0 / 3
      }
      gridPane.rowConstraints += row
    }

    beverageMenuAnchorPane.children.add(gridPane)

    for(i <- 0 until MainApp.beverageMenu.size()){
      val imageView = new ImageView(){
        alignmentInParent = Pos.Center
        fitHeight = 150
        fitWidth = 150
        margin = Insets(10, 0, 0, 0)
      }
      imageView.autosize()
      imageView.imageProperty().bind(MainApp.beverageMenu(i).imagePath)

      val beverageNameLabel = new Label(){
        alignmentInParent = Pos.Center
      }
      beverageNameLabel.text <== MainApp.beverageMenu(i).name

      val beveragePriceLabel = new Label()
      beveragePriceLabel.text <== StringProperty(f"RM ${MainApp.beverageMenu(i).price.value}%.2f")

      val vBox = new VBox(){
        prefWidth = 200
        prefHeight = 200
        maxWidth = 200
        maxHeight = 200
        alignment = Pos.Center
        margin = Insets.apply(5,5,5,5)
        styleClass += "box-border"
        cursor = Cursor.Hand
        userData = MainApp.beverageMenu(i)
        margin = Insets.apply(15,15,15,15)
      }

      vBox.disable <== BooleanProperty(!MainApp.beverageMenu(i).available.value)
      vBox.children.addAll(imageView, beverageNameLabel, beveragePriceLabel)
      vBox.onMouseClicked = ev => {
        val vbox = ev.source.asInstanceOf[jfxs.layout.VBox]
        vbox.userData match {
          case beverage: Beverage => MainApp.showTableAddBeverageDialog(beverage, order)
          case _ => ""
        }
      }

      val row = i % 3
      val col = i / 3

      gridPane.add(vBox, row, col)
    }
  }

  // Event handler to navigate back to main menu
  def handleBack(actionEvent: ActionEvent): Unit = {
    MainApp.showOverview()
  }

  // Event handler to open payment dialog
  def handlePayment(actionEvent: ActionEvent): Unit = {
    MainApp.showPaymentDialog(table)
  }

  // Event handler to register the new Order into the table
  def handleConfirm(actionEvent: ActionEvent): Unit = {
    table.order = Option(order)
    MainApp.showOverview()
  }
}