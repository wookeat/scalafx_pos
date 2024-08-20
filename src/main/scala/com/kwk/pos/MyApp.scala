package com.kwk.pos

import com.kwk.pos.modal.{Beverage, Food, Order, OrderItem, Product, Table}
import com.kwk.pos.view.{FoodMenuEditDialogController, MenuSelectionController, MenuSelectionTrait, MenuTrait, OverviewTrait, PaymentDialogController, RestaurantLayoutTrait, TableAddBeverageDialogController, TableAddOrderDialogController, TableEditDialogController, TableOrderTrait, TableOverviewController, TableOverviewTrait}
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.collections.ObservableBuffer
import scalafx.scene.Scene
import scalafx.Includes._
import javafx.{scene => jfxs}
import scalafx.beans.property.{ObjectProperty, StringProperty}
import scalafx.scene.image.Image
import scalafx.scene.paint.Color
import scalafx.stage.{Modality, Stage, StageStyle}
import scalafxml.core.{FXMLLoader, NoDependencyResolver}

object MainApp extends JFXApp{

  val menu = new ObservableBuffer[Food]()
  menu += new Food("Kaya Butter Toast", 10.50, true, "../img/Kaya-Butter-Toasts.jpg")
  menu += new Food("Wanton Mee", 12.00, true)
  menu += new Food("Fried Rice", 13.00, false)
  menu += new Food("Nasi Lemak", 20, true)
  menu += new Food("Nasi Padang", 18, false)

  val beverageMenu = new ObservableBuffer[Beverage]()
  beverageMenu += new Beverage("Milo", 13.00, true)
  beverageMenu += new Beverage("Barli", 13.00, true)
  beverageMenu += new Beverage("Teh", 13.00, true)
  beverageMenu += new Beverage("Kopi", 13.00, true)
  beverageMenu += new Beverage("Cham", 13.00, true)
  beverageMenu += new Beverage("Cendol", 13.00, true)
  beverageMenu += new Beverage("Ice Kacang", 13.00, true)
  beverageMenu += new Beverage("Horlicks", 13.00, true)
  beverageMenu += new Beverage("Teh C", 13.00, true)
  beverageMenu += new Beverage("Teh O", 13.00, true)

  val table = new ObservableBuffer[Table]()
  table += new Table("1")
  table += new Table("2")
  table += new Table("3")
  table += new Table("4")
  table += new Table("5")
  table += new Table("6")

  val rootResource = getClass.getResource("view/RootLayout.fxml")

  val loader = new FXMLLoader(rootResource, NoDependencyResolver)

  loader.load();

  val roots = loader.getRoot[jfxs.layout.BorderPane]

  stage = new PrimaryStage{
    title = "Point of Sale"
    scene = new Scene{
      root = roots
    }
  }

  val restaurantLayoutTrait: RestaurantLayoutTrait = {
    val resource = getClass.getResource("view/RestaurantLayout.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val controller = loader.getController[RestaurantLayoutTrait]
    controller
  }

  val tableOverviewTrait: TableOverviewTrait = {
    val resource = getClass.getResource("view/TableOverview1.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val controller = loader.getController[TableOverviewTrait]
    controller
  }

  val menuSelectionTrait: MenuSelectionTrait = {
    val resource = getClass.getResource("view/MenuSelection.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val controller = loader.getController[MenuSelectionTrait]
    controller
  }

  val menuTrait: MenuTrait = {
    val resource = getClass.getResource("view/Menu.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val controller = loader.getController[MenuTrait]
    controller
  }

  val overviewTrait: OverviewTrait = {
    val resource = getClass.getResource("view/Overview.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val controller = loader.getController[OverviewTrait]
    controller
  }

  val tableOrderTrait: TableOrderTrait = {
    val resource = getClass.getResource("view/TableOverview2.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val controller = loader.getController[TableOrderTrait]
    controller
  }

  def showLayout() = {
    val roots = restaurantLayoutTrait.getGrid
    this.roots.setCenter(roots)
  }

  def showTableOverview()={
    val roots = tableOverviewTrait.getBorderPane
    this.tableOverviewTrait.initialize()
    this.roots.setCenter(roots)
  }

  def showOverview(): Unit = {
    val roots = overviewTrait.getTabPane
    this.overviewTrait.initialize()
    this.roots.setCenter(roots)
  }


  //****************Check this*************************
  def showTableOrder(): Unit = {
    val roots = tableOrderTrait.getAnchorPane
    this.tableOrderTrait.initialize()
    this.roots.setCenter(roots)
  }


  //Dialog

  def showTableEditDialog(orderItem: OrderItem): Boolean = {
    val resource = getClass.getResourceAsStream("view/TableEditDialog.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)
    loader.load(resource)
    val roots2 = loader.getRoot[jfxs.Parent]
    val controller = loader.getController[TableEditDialogController#Controller]

    val dialog = new Stage(){
      initModality(Modality.ApplicationModal)
      initOwner(stage)
      scene = new Scene{
        stylesheets += getClass.getResource("css/test.css").toString
        root = roots2
      }
    }
    controller.dialogStage = dialog
    controller.orderItem = orderItem
    controller.initialize()
    dialog.showAndWait()
    controller.okClicked
  }

//  def showTableAddOrderDialog(product: Product, table: Table): Boolean = {
//    val resource = getClass.getResourceAsStream("view/TableAddOrderDialog.fxml")
//    val loader = new FXMLLoader(null, NoDependencyResolver)
//    loader.load(resource)
//    val roots2 = loader.getRoot[jfxs.Parent]
//    val controller = loader.getController[TableAddOrderDialogController#Controller]
//
//    val dialog = new Stage(){
//      initModality(Modality.ApplicationModal)
//      initOwner(stage)
//      scene = new Scene{
//        stylesheets += getClass.getResource("css/test.css").toString
//        root = roots2
//      }
//    }
//    controller.dialogStage = dialog
//    controller.product = product
//    controller.table = table
//    controller.initialize()
//    dialog.showAndWait()
//    controller.okClicked
//  }

  def showTableAddOrderDialog(product: Product, order: Order): Boolean = {
    val resource = getClass.getResourceAsStream("view/TableAddOrderDialog.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)
    loader.load(resource)
    val roots2 = loader.getRoot[jfxs.Parent]
    val controller = loader.getController[TableAddOrderDialogController#Controller]

    val dialog = new Stage(){
      initModality(Modality.ApplicationModal)
      initOwner(stage)
      scene = new Scene{
        stylesheets += getClass.getResource("css/test.css").toString
        root = roots2
      }
    }
    controller.dialogStage = dialog
    controller.product = product
    controller.order = order
    controller.initialize()
    dialog.showAndWait()
    controller.okClicked
  }

//  def showTableAddBeverageDialog(beverage: Beverage, table: Table): Boolean = {
//    val resource = getClass.getResourceAsStream("view/TableAddBeverageDialog.fxml")
//    val loader = new FXMLLoader(null, NoDependencyResolver)
//    loader.load(resource)
//    val roots2 = loader.getRoot[jfxs.Parent]
//    val controller = loader.getController[TableAddBeverageDialogController#Controller]
//
//    val dialog = new Stage(){
//      initModality(Modality.ApplicationModal)
//      initOwner(stage)
//      scene = new Scene{
//        root = roots2
//      }
//    }
//    controller.dialogStage = dialog
//    controller.beverage = beverage
//    controller.table = table
//    controller.initialize()
//    dialog.showAndWait()
//    controller.okClicked
//  }

  def showTableAddBeverageDialog(beverage: Beverage, order: Order): Boolean = {
    val resource = getClass.getResourceAsStream("view/TableAddBeverageDialog.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)
    loader.load(resource)
    val roots2 = loader.getRoot[jfxs.Parent]
    val controller = loader.getController[TableAddBeverageDialogController#Controller]

    val dialog = new Stage(){
      initModality(Modality.ApplicationModal)
      initOwner(stage)
      scene = new Scene{
        stylesheets += getClass.getResource("css/test.css").toString
        root = roots2
      }
    }
    controller.dialogStage = dialog
    controller.beverage = beverage
    controller.order = order
    controller.initialize()
    dialog.showAndWait()
    controller.okClicked
  }

  def showFoodMenuEditDialog(food: Food, operation: String): Boolean = {
    val resource = getClass.getResourceAsStream("view/FoodMenuEditDialog.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)
    loader.load(resource)
    val roots2 = loader.getRoot[jfxs.Parent]
    val controller = loader.getController[FoodMenuEditDialogController#Controller]

    val dialog = new Stage(){
      initModality(Modality.ApplicationModal)
      initOwner(stage)
      scene = new Scene{
        stylesheets += getClass.getResource("css/test.css").toString
        root = roots2
      }
    }
    controller.dialogStage = dialog
    controller.product = food
    controller.operation = operation
    controller.initialize()
    dialog.showAndWait()
    controller.okClicked
  }

  def showBeverageMenuEditDialog(beverage: Beverage, operation: String): Boolean = {
    val resource = getClass.getResourceAsStream("view/FoodMenuEditDialog.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)
    loader.load(resource)
    val roots2 = loader.getRoot[jfxs.Parent]
    val controller = loader.getController[FoodMenuEditDialogController#Controller]

    val dialog = new Stage(){
      initModality(Modality.ApplicationModal)
      initOwner(stage)
      scene = new Scene{
        stylesheets += getClass.getResource("css/test.css").toString
        root = roots2
      }
    }
    controller.dialogStage = dialog
    controller.product = beverage
    controller.operation = operation
    controller.initialize()
    dialog.showAndWait()
    controller.okClicked
  }

  def showPaymentDialog(table: Table): Boolean = {
    val resource = getClass.getResourceAsStream("view/PaymentDialog.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)
    loader.load(resource)
    val roots2 = loader.getRoot[jfxs.Parent]
    val controller = loader.getController[PaymentDialogController#Controller]

    val dialog = new Stage(){
      initModality(Modality.ApplicationModal)
      initOwner(stage)
      scene = new Scene{
        stylesheets += getClass.getResource("css/test.css").toString
        root = roots2
      }
    }
    controller.dialogStage = dialog
    controller.table = table
    controller.initialize()
    dialog.showAndWait()
    controller.okClicked
  }

  // Helper function


  //  def showLayout() = {
  //    val resource = getClass.getResource("view/RestaurantLayout.fxml")
  //    val loader = new FXMLLoader(resource, NoDependencyResolver)
  //    loader.load()
  //    val controller = loader.getController[RestaurantLayoutTrait]
  //
  //    val roots = loader.getRoot[jfxs.layout.AnchorPane]
  //    this.roots.setCenter(roots)
  //    controller
  //  }
  //
  //  def showTableOverview(table: Table) = {
  //    val resource = getClass.getResource("view/TableOverview1.fxml")
  //    val loader = new FXMLLoader(resource, NoDependencyResolver)
  //    loader.load()
  //    val controller = loader.getController[TableOverviewTrait]
  //    val roots = loader.getRoot[jfxs.layout.BorderPane]
  //
  //    controller.table = table
  //    controller.initialize()
  //
  //    this.roots.setCenter(roots)
  //    controller
  //  }
  //
  //  def showMenuSelection(table: Table) = {
  //    val resource = getClass.getResource("view/MenuSelection.fxml")
  //    val loader = new FXMLLoader(resource, NoDependencyResolver)
  //    loader.load()
  //    val controller = loader.getController[MenuSelectionTrait]
  //    controller.table = table
  //    controller
  //  }
  //
  //  def showMenu(table: Table) = {
  //    val resource = getClass.getResource("view/Menu.fxml")
  //    val loader = new FXMLLoader(resource, NoDependencyResolver)
  //    loader.load()
  //    val controller = loader.getController[MenuTrait]
  //    controller.table = table
  //    controller
  //  }
  //  initialize()
  //  showLayout()

  showOverview()
}
