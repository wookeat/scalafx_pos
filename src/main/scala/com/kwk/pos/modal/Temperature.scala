package com.kwk.pos.modal

case class Temperature(temperature: String, level: Integer)

object Temperature{
  val Cold = Temperature("Cold", 0)
  val Normal = Temperature("Normal Temp.", 1)
  val Hot = Temperature("Hot", 2)
}