package com.scalaapplication

/**
  * Created by adity on 10/25/2017.
  */
class CompanionObject_2 {

  private val secret = 2
}


object CompanionObject_2 extends App{

  // can access private member from class
  def calculate (obj: CompanionObject_2) = obj.secret * 2

}

object Driver extends App{

  val obj = new CompanionObject_2
  println(CompanionObject_2.calculate(obj))

}