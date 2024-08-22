package com.kwk.pos.modal

import scalafx.beans.property.{ObjectProperty, StringProperty}
import scalafx.collections.ObservableBuffer

class Table(_id: String){
  val id = StringProperty(_id)
  var order: Option[Order] = None // Represent the Order for this table
}
