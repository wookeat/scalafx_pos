package com.kwk.pos.view

import com.kwk.pos.MainApp
import com.kwk.pos.modal.{OrderItem, Table}
import javafx.beans
import scalafx.beans.property.ObjectProperty
import scalafx.scene.control.{Button, Label, MenuBar, TableColumn, TableRow, TableView}
import scalafx.scene.layout.{BorderPane, GridPane, Pane}
import scalafx.Includes._
import scalafx.beans.Observable
import scalafx.collections.ObservableBuffer.Add
import scalafx.event.ActionEvent
import scalafxml.core.macros.sfxml

trait TableOverviewTrait{
  var table: Table = _
  def initialize(): Unit
  def getBorderPane: BorderPane
  def getRightPane: Pane
}

@sfxml
class TableOverviewController(
                               private val tableNum: Label,
                               private val menuBar: MenuBar,
                               private val tableOverviewGrid: GridPane,
                               private val tableOrderTable: TableView[OrderItem],
                               private val orderNumberColumn: TableColumn[OrderItem, Integer],
                               private val orderNameColumn: TableColumn[OrderItem, String],
                               private val orderPriceColumn: TableColumn[OrderItem, Double],
                               private val orderQuantityColumn: TableColumn[OrderItem, Integer],
                               private val orderSumColumn: TableColumn[OrderItem, Double],
                               private val addItemButton: Button,
                               private val deleteItemButton: Button,
                               private val borderPane: BorderPane,
                               private val rightPane: Pane,
                               private val totalAmountLabel: Label
                             ) extends TableOverviewTrait{

  def initialize(): Unit = {
    tableNum.text = s"Table ${table.id.getValue}"
    tableOrderTable.items = table.order
    println(s"test: ${table} ")
    orderNameColumn.cellValueFactory = { _.value.product.name }

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
    rightPane.children.clear()
    rightPane.children.add(MainApp.menuSelectionTrait.getMenuSelectionPane)
    MainApp.menuSelectionTrait.table = table
  }

  def calculateTotalSum(): Double = {
    val total = table.order.foldRight(0.00)((x,_) => x.sum.value)

    total
  }

  def handleBack(actionEvent: ActionEvent): Unit = {
    MainApp.showOverview()
  }


  def getBorderPane: BorderPane = borderPane

  def getRightPane: Pane = rightPane


}
