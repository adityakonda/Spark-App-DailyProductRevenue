package com.sparkapplications

import org.apache.spark.{SparkConf, SparkContext}

import scala.io.Source

/**
  * Created by adity on 11/15/2017.
  */
object ProductRevenue {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf()
    conf.setAppName("Product Revenue")
    conf.setMaster("yarn-client")

    val sc = new SparkContext(conf)

    val productList = Source.fromFile("/root/aditya/data/retail_db/products/part-00000").getLines
    val productRdd = sc.parallelize(productList.toList)
    productRdd.take(10).foreach(println)
    
  }

}
