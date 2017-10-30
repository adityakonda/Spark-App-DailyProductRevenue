package com.scalaapplication

/**
  * Created by adity on 10/26/2017.
  */
class Sample {

}

object Sample extends App {

  def saySomething(prefix: String) = (s: String) => {
    prefix + " " + s
  }

  val sayHello = saySomething("Hello")
  println(saySomething("aditya"))
  println(sayHello("asdf"))

}
