package com.scalaapplication

/**
  * Created by adity on 10/25/2017.
  */
class CompanionObject_1(var crustType: String) {

  val test = "apple"

   override def toString = "Crest type is: "+crustType
}

object CompanionObject_1{

  val CRUST_TYPE_THIN = "thin"
  val CRUST_TYPE_THICK = "thick"
  def getFoo = "Foo"

  def main(args: Array[String]): Unit = {

    // accessing Object members
    println(CompanionObject_1.CRUST_TYPE_THIN)
    println(CompanionObject_1.getFoo)

    // accessing class members
    val pizzaObj = new CompanionObject_1(CompanionObject_1.CRUST_TYPE_THICK)
    print(pizzaObj)
  }
}
