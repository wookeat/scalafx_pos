package com.kwk.pos

import com.kwk.pos.modal.{Beverage, Food, Order, OrderItem, Product, Table}
import com.kwk.pos.view.{
  OverviewMenuEditDialogController,
  OverviewTrait,
  PaymentDialogController,
  TableAddBeverageDialogController,
  TableAddOrderDialogController,
  TableEditDialogController,
  TableOrderTrait
}
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.collections.ObservableBuffer
import scalafx.scene.Scene
import scalafx.Includes._
import javafx.{scene => jfxs}
import scalafx.stage.{Modality, Stage}
import scalafxml.core.{FXMLLoader, NoDependencyResolver}

object MainApp extends JFXApp{

  // Food Menu Data
  val menu = new ObservableBuffer[Food]()
  menu += new Food("Kaya Butter Toast", 8.50, true, "../img/food/kaya_butter_toast.jpg")
  menu += new Food("Chicken Chop", 22.00, true, "../img/food/chicken_chop.jpg")
  menu += new Food("Peanut Butter Toast", 10.50, true, "../img/food/peanut_butter_toast.jpg")
  menu += new Food("Half-Boiled Eggs", 4.50, true, "../img/food/half_boiled_eggs.jpg")
  menu += new Food("Nasi Lemak", 13.50, true, "../img/food/nasi_lemak.jpg")
  menu += new Food("Fried Bihun", 15, false, "../img/food/fried_bihun.jpg")
  menu += new Food("Curry Mee", 15, false, "../img/food/curry_mee.jpg")
  menu += new Food("Prawn Mee", 15, false, "../img/food/prawn_mee.jpg")

  // Beverage Menu Data
  val beverageMenu = new ObservableBuffer[Beverage]()
  beverageMenu += new Beverage("Milo", 5.00, true, "../img/beverage/milo.png")
  beverageMenu += new Beverage("Barli", 3.00, true, "../img/beverage/barli.jpg")
  beverageMenu += new Beverage("Teh Tarik", 3.50, true, "../img/beverage/teh_tarik.jpeg")
  beverageMenu += new Beverage("Kopi", 3.50, true, "../img/beverage/kopi.jpg")
  beverageMenu += new Beverage("Cham", 4.00, true, "../img/beverage/cham.jpg")
  beverageMenu += new Beverage("Horlicks", 4.00, true, "../img/beverage/horlick.jpg")

  // Restaurant Table Data
  val table = new ObservableBuffer[Table]()
  table += new Table("1")
  table += new Table("2")
  table += new Table("3")
  table += new Table("4")
  table += new Table("5")
  table += new Table("6")

  // Initialization for Main Stage
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

  // Reference: https://github.com/vigoo/scalafxml
  // "As the controller class is replaced in compile time by a generated one, we cannot directly use it to
  // call the controller of our views. Instead we have to define a public interface for them and then use it
  // as the type given for the getController method of FXMLLoader." - Quoted from reference

  // Trait to obtain the controller from FXML upon loading
  val overviewTrait: OverviewTrait = {
    val resource = getClass.getResource("view/Overview.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val controller = loader.getController[OverviewTrait]
    controller
  }

  val tableOrderTrait: TableOrderTrait = {
    val resource = getClass.getResource("view/TableOverview.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val controller = loader.getController[TableOrderTrait]
    controller
  }

  // Method to display Overview scene
  def showOverview(): Unit = {
    val roots = overviewTrait.getTabPane
    this.overviewTrait.initialize()
    this.roots.setCenter(roots)
  }

  // Method to display Table scene
  def showTableOrder(): Unit = {
    val roots = tableOrderTrait.getAnchorPane
    this.tableOrderTrait.initialize()
    this.roots.setCenter(roots)
  }


  //Dialog to change the quantity or remove order items from the table order
  def showTableEditDialog(orderItem: OrderItem, order: Order): Boolean = {
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
    controller.order= order
    controller.initialize()
    dialog.showAndWait()
    controller.okClicked
  }

  // Dialog to add foods to each table order
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

  // Dialog to add beverages into each table order
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

  // Dialog to edit the food in the Food Menu
  def showFoodMenuEditDialog(food: Food, operation: String): Boolean = {
    val resource = getClass.getResourceAsStream("view/OverviewMenuEditDialog.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)
    loader.load(resource)
    val roots2 = loader.getRoot[jfxs.Parent]
    val controller = loader.getController[OverviewMenuEditDialogController#Controller]

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

  // Dialog to edit the beverage in the Beverage Menu
  def showBeverageMenuEditDialog(beverage: Beverage, operation: String): Boolean = {
    val resource = getClass.getResourceAsStream("view/OverviewMenuEditDialog.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)
    loader.load(resource)
    val roots2 = loader.getRoot[jfxs.Parent]
    val controller = loader.getController[OverviewMenuEditDialogController#Controller]

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

  // Dialog to process payment
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

  showOverview()
}
