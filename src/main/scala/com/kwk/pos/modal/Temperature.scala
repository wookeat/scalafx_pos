package com.kwk.pos.modal

case class Temperature(temperature: String, level: Integer)

//Companion Object for Temperature Levels of Beverage
object Temperature{
  val Cold = Temperature("Cold", 0)
  val Normal = Temperature("N. Temp.", 1)
  val Hot = Temperature("Hot", 2)
}