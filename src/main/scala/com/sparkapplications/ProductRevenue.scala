package com.sparkapplications

import org.apache.spark.{SparkConf, SparkContext}

import scala.io.Source

/**
  * Created by aditya on 11/15/2017.
  */
object ProductRevenue {

  def main(args: Array[String]): Unit = {

    /*    SETTING UP SPARK CONFIGURATION   */
    val conf = new SparkConf()
    conf.setAppName("Product Revenue")
    conf.setMaster("yarn-client")

    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")

    /*    READING FROM LOCAL FILE SYSTEM   */
    val productList = Source.fromFile("/root/aditya/data/retail_db/products/part-00000").getLines.toList
    val productRdd = sc.parallelize(productList)

    /*    READING FROM HDFS   */
    val orderRdd = sc.textFile("/user/root/retail_db/orders/")
    val orderItemsRdd = sc.textFile("/user/root/retail_db/order_items")

    /*    ACCUMULATORS    */
    val orderCompleted = sc.accumulator(0,"Orders Completed Count")
    val orderNonCompleted = sc.accumulator(0,"Orders in-completed Count")

    /*    FILTERING DATA    */
    val ordersFilteredOnCompleted = orderRdd.filter( order => {

      val isCompleted = order.split(",")(3) == "COMPLETED" || order.split(",")(3) == "CLOSED"

          if(isCompleted)
            orderCompleted += 1
          else
            orderNonCompleted +=1

      isCompleted
    })

    /*    CONVERTING RDD --> (K,V)    */
    // orderMap --> (orderID, orderDate)
    val orderMap = orderRdd.map(order => {
      (order.split(",")(0).toInt,order.split(",")(1))
    })

    // orderItemMap -> (orderID, (productID, order_itemSubTotal))
    val orderItemMap = orderItemsRdd.map(orderItem => {
      (orderItem.split(",")(1).toInt, (orderItem.split(",")(2).toInt, orderItem.split(",")(4).toFloat)) })

    /*    JOINING (K,V)     */
    val ordersJoin = orderMap.join(orderItemMap)
    val ordersLeftJoin = orderMap.leftOuterJoin(orderItemMap)

    // taking orderItemMap as a reference and filtering out the rejected orders from order items
    val t = (24688,("2013-12-25 00:00:00.0",None))
    val ordersRejectedMap = ordersLeftJoin.filter( missingorder =>  missingorder._2._2 == None)

    // ordersJoin(K,V) --> (orderID, (orderDate ,(productID, order_itemSubTotal))) --> (_1, (_2.1, (_2._2._1, _2._2._2)))
    // orderJoinMap(K,V) --> ((orderDate, productID), order_itemSubTotal)
    val orderJoinMap = ordersJoin.map( order => ((order._2._1, order._2._2._1),(order. _2._2._2)))

    /*    REDUCE-BY-KEY      */
    // dailyRevenuePerProductID(K,V) --> ((orderDate, productID), sum(order_itemSubTotal))
    val dailyRevenuePerProductID = orderJoinMap.reduceByKey((total, revenue) => total + revenue)

    /*    AGGREGATE-BY-KEY      */
    /*    aggregateByKey(initialize tuple)(combiner logic)(reducer logic)   */

    // daily revenue of the product and the number of time the product was purchased in a day
    // dailyRevenueABK(K,V) --> ((orderDate, productID), (sum(order_itemSubTotal), count(productID)))
    val dailyRevenueABK = orderJoinMap.aggregateByKey((0.0,0))(
      (i,revenue) => (i._1 + revenue, i._2 + 2),
        (i1,i2) => (i1._1 + i2._1, i1._2 + i2._2)
    )

    /*    JOINING DATA WITHOUT BROADCAST VARIABLES      */

    // productRDDMap(K,V) --> (productID, productName)
    val productRDDMap = productRdd.map( product => (product.split(",")(0).toInt, product.split(",")(2)))
    val dailyRevenuePerProductIDMap = dailyRevenuePerProductID.map( record =>
      (record._1._2, (record._1._1, record._2))
    )

    // dailyRevenuePerProductNameLocal(K,V) --> ((orderDate, productName), sum(order_itemSubTotal))
    val dailyRevenuePerProductNameLocal = productRDDMap.join(dailyRevenuePerProductIDMap)

    /*    PREVIEWING DATA
    dailyRevenuePerProductNameLocal.take(100).foreach(println)*/

    /*    JOINING DATA WITH BROADCAST VARIABLES      */

    val productLocalMap = productList.map( product => (product.split(",")(0).toInt, product.split(",")(2))).toMap
    val broadcastVariable = sc.broadcast(productLocalMap)

    // dailyRevenuePerProductName(K,V) --> ((orderDate, - sum(order_itemSubTotal)) , (orderDate, sum(order_itemSubTotal), productName))
    val dailyRevenuePerProductName = dailyRevenuePerProductID.map( record =>
      {
        ((record._1._1, -record._2) , (record._1._1, record._2,broadcastVariable.value.get(record._1._2).get))
      }
    )

    val dailyRevenuePerProductNameSorted = dailyRevenuePerProductName.sortByKey().map(record => record._2.productIterator.mkString(","))

    /*    PREVIEWING DATA      */
    //dailyRevenuePerProductNameSorted.take(100).foreach(println)

    /*    SAVING DATA HDFS    */
    dailyRevenuePerProductNameSorted.saveAsTextFile("/user/root/dailyRevenuePerProductNameSorted")

  }

}
