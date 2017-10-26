package com.scalaapplication

/**
  * Created by adity on 10/25/2017.
  */
class Functions_1 {

  // 1st method of declaring function
  def add1(a: Int, b: Int) = a + b

  // 2nd method of declaring function
  def add2(a: Int, b: Int): Int = a + b

  // 3nd method of declaring function
  def add3(a: Int, b: Int): Int = {a + b}


  /* Setting default values for method parameters */

  def makeConnection(timeout: Int = 500, protocol: String = "http"): Any = {
    println("timeout = %d, protocol = %s".format(timeout, protocol))
  }

}

object MyDriver extends App{

  val obj = new Functions_1

  println("Calling add(): "+obj.add3(2,3))

  obj.makeConnection()
  obj.makeConnection(200,"asdf")
}