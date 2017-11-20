package com.sparkapplications

import org.apache.spark.{SparkConf, SparkContext}

import scala.io.Source

/**
  * Created by adity on 11/15/2017.
  */
object ProductRevenue {

  def main(args: Array[String]): Unit = {

    //setting up the spark configuration
    val conf = new SparkConf()
    conf.setAppName("Product Revenue")
    conf.setMaster("yarn-client")

    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")

    // reading data from Local File System to RDD
    val productList = Source.fromFile("/root/aditya/data/retail_db/products/part-00000").getLines
    val productRdd = sc.parallelize(productList.toList)
    productRdd.take(10).foreach(println)

    // reading from HDFS
    val orderRdd = sc.textFile("/user/root/retail_db/orders/")
    val orderItemsRdd = sc.textFile("/user/root/retail_db/order_items")

    // Counting orders completed (COMPLETED & CLOSED)  and order that are not completed
    val orderCompleted = sc.accumulator(0,"Orders Completed Count")
    val orderNonCompleted = sc.accumulator(0,"Orders in-completed Count")

    // filtering orders that are completed
    val ordersFiltered = orderRdd.filter( order => {

      val isCompleted = order.split(",")(3) == "COMPLETED" || order.split(",")(3) == "CLOSED"

          if(isCompleted)
            orderCompleted += 1
          else
            orderNonCompleted +=1

      isCompleted
    })

    // converting to key value pairs

    // orderMap --> (orderID, orderDate) --> (1,2013-07-25 00:00:00.0)
    val orderMap = orderRdd.map(order => {
      (order.split(",")(0).toInt,order.split(",")(1))
    })

    // orderItemMap -> (orderID, (productID, itemSubTotal)) --> (1,(957,299.98))
    val orderItemMap = orderItemsRdd.map(orderItem => {
      (orderItem.split(",")(1).toInt, (orderItem.split(",")(2).toInt, orderItem.split(",")(4).toFloat)) })

    // Joining Order & OrderItems data
    val ordersJoin = orderMap.join(orderItemMap)
    val ordersLeftJoin = orderMap.leftOuterJoin(orderItemMap)

    // taking orderItemMap as a reference and filtering out the rejected orders from order items
    val t = (24688,("2013-12-25 00:00:00.0",None))
    val ordersRejectedMap = ordersLeftJoin.filter( missingorder =>  missingorder._2._2 == None)

    ordersRejectedMap.take(10).foreach(println)

  }

}
