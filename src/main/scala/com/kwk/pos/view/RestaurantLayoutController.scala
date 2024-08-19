package com.kwk.pos.view

import com.kwk.pos.MainApp
import com.kwk.pos.modal.Table
import scalafx.geometry.Pos
import scalafx.scene.Cursor
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout.{GridPane, StackPane}
import scalafx.scene.paint.Color
import scalafx.scene.shape.Circle
import scalafx.scene.text.Text
import scalafxml.core.macros.sfxml
import scalafx.Includes._

trait RestaurantLayoutTrait{
  def getGrid: GridPane
}



@sfxml
class RestaurantLayoutController(
                                  private val grid: GridPane
                                ) extends RestaurantLayoutTrait {
  grid.gridLinesVisible = true

  def getGrid: GridPane = grid

  def initialize(): Unit = {
    for(i <- 0 until  MainApp.table.size()){
      val circle = new Circle {
        radius = 60.0
        cursor = Cursor.Hand
        alignmentInParent = Pos.Center
        fill = Color.Green
        userData = MainApp.table(i)
      }
      circle.onMouseClicked = (e: MouseEvent) => {
        val circle = e.source.asInstanceOf[javafx.scene.shape.Circle]
        val tableData = circle.getUserData.asInstanceOf[Table]

//        MainApp.showTableOverview(tableData)
        MainApp.tableOverviewTrait.table = tableData
        MainApp.showTableOverview()
      }

      val row = i % 2
      val col = i / 2

      val tableNum = new Text(s"Table ${i}")
      tableNum.setStyle("-fx-font-weight: bold")
      val stackPane = new StackPane()

      stackPane.children.addAll(circle, tableNum)

      grid.add(stackPane, row, col)
    }
  }

  def handleClick(): Unit = {

  }

  initialize()
}
