package com.kwk.pos.view

import com.kwk.pos.MainApp
import com.kwk.pos.modal.Table
import scalafx.event.ActionEvent
import scalafx.scene.control.Button
import scalafx.scene.layout.{Pane, TilePane}
import scalafxml.core.macros.sfxml

trait MenuSelectionTrait{
  var table: Table = null
  def getMenuSelectionPane: Pane
}

@sfxml
class MenuSelectionController(
                               private val foodMenuButton: Button,
                               private val menuSelectionTilePane: TilePane
                             ) extends MenuSelectionTrait {

  def handleFoodMenuClick(actionEvent: ActionEvent) = {
//    menuSelectionTilePane.children.clear()
//    menuSelectionTilePane.children.add(MainApp.showMenu(table).getAnchorPane)
    val pane = MainApp.tableOverviewTrait.getRightPane
    pane.children.clear()
    pane.children.add(MainApp.menuTrait.getAnchorPane)
    MainApp.menuTrait.table = table
    println(s"tableeeeeee: ${table}")
  }

  def getMenuSelectionPane: Pane = menuSelectionTilePane
}
