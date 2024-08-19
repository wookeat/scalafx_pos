package com.kwk.pos.view

import com.kwk.pos.MainApp
import com.kwk.pos.modal.{Beverage, Food, OrderItem, Table}
import javafx.scene.control.SelectionMode
import scalafx.beans.property.{BooleanProperty, ObjectProperty, StringProperty}
import scalafx.event.ActionEvent
import scalafx.scene.control.{Label, Tab, TableColumn, TableRow, TableView}
import scalafx.Includes._
import scalafx.geometry.{Insets, Pos}
import javafx.{scene => jfxs}
import scalafx.beans.binding.Bindings
import scalafx.scene.Cursor
import scalafx.scene.image.ImageView
import scalafx.scene.layout.{AnchorPane, ColumnConstraints, GridPane, Priority, RowConstraints, StackPane, TilePane, VBox}
import scalafx.scene.shape.Rectangle
import scalafxml.core.macros.sfxml

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
                            private val beverageMenuAnchorPane: AnchorPane
                          ) extends TableOrderTrait{
  def getAnchorPane: AnchorPane = anchorPane

  def initialize(): Unit = {
    foodMenuTab.getStyleClass.add("tab-border")
    tableNumberLabel.text = table.id.value
    tableOrderTable.items = table.order

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

    tableOrderTable.setRowFactory( t => {
      val row = new TableRow[OrderItem]()
      row.setOnMouseClicked(e => {
        if(row.getItem != null) {
          MainApp.showTableEditDialog(row.getItem)
        }
      })
      row
    })

    totalAmountLabel.text <== table.totalSum.asString()

    initializeFoodMenu()
    initializeBeverageMenu()

    MainApp.menu.onChange({
      foodMenuAnchorPane.children.clear()
      println(s"Check anchor pane child: ${foodMenuAnchorPane.children.size()}")
      initializeFoodMenu()
    })
  }

  def initializeFoodMenu(): Unit = {
    val gridPane = new GridPane(){
      alignmentInParent = Pos.Center
//      hgap = 5
//      vgap = 5
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
      }
      imageView.autosize()
      imageView.imageProperty().bind(MainApp.menu(i).imagePath)

      val foodNameLabel = new Label(){
        alignmentInParent = Pos.Center
      }
      foodNameLabel.text <== MainApp.menu(i).name

      val foodPriceLabel = new Label()
      foodPriceLabel.text <== StringProperty(s"RM ${MainApp.menu(i).price.value}")

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
          case food: Food => MainApp.showTableAddOrderDialog(food, table)
          case _ => ""
        }
      }

      val row = i % 3
      val col = i / 3

      gridPane.add(vBox, row, col)
    }
  }

  def initializeBeverageMenu(): Unit = {
    val gridPane = new GridPane(){
      alignmentInParent = Pos.Center
    }
    gridPane.prefHeight <== beverageMenuAnchorPane.height
    gridPane.prefWidth <== beverageMenuAnchorPane.width
//    gridPane.hgrow = Priority.Always
//    gridPane.vgrow = Priority.Always

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
      }
      imageView.autosize()
      imageView.imageProperty().bind(MainApp.beverageMenu(i).imagePath)

      val beverageNameLabel = new Label(){
        alignmentInParent = Pos.Center
      }
      beverageNameLabel.text <== MainApp.beverageMenu(i).name

      val beveragePriceLabel = new Label()
      beveragePriceLabel.text <== StringProperty(s"RM ${MainApp.beverageMenu(i).price.value}")

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
          case beverage: Beverage => MainApp.showTableAddBeverageDialog(beverage, table)
          case _ => ""
        }
      }

      val row = i % 3
      val col = i / 3

      gridPane.add(vBox, row, col)
    }
  }

  def handleBack(actionEvent: ActionEvent): Unit = {
    MainApp.showOverview()
  }
}