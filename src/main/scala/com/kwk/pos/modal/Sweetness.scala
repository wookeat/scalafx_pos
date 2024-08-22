package com.kwk.pos.modal

case class Sweetness(sweetness: String, level: Int)

//Companion Object for Sweetness Levels of Beverage
object Sweetness{
  val NonSweet = Sweetness("Non Sweet", 0)
  val Normal = Sweetness("N. Sweet", 1)
  val ExtraSweet = Sweetness("Ex. Sweet", 2)
}