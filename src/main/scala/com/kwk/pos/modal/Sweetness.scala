package com.kwk.pos.modal

case class Sweetness(sweetness: String, level: Int)

object Sweetness{
  val NonSweet = Sweetness("Non Sweet", 0)
  val Normal = Sweetness("Normal Sweet", 1)
  val ExtraSweet = Sweetness("Extra Sweet", 2)
}