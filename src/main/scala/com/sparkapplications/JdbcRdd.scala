package com.sparkapplications

import java.sql.DriverManager

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.JdbcRDD
import com.mysql.jdbc.Driver

/**
  * Created by adity on 10/25/2017.
  */
object JdbcRdd {

  def main(args: Array[String]): Unit = {

    //JDBC connection & it's crenditials
    val url = "jdbc:mysql://localhost:3306/sakila"
    val username = "root"
    val password = "password"

    //JDBC driver connection
    Class.forName("com.mysql.jdbc.Driver").newInstance()

    val conf = new SparkConf()
    conf.setMaster("local")
    conf.setAppName("JDBC Rdd")
    val sc = new SparkContext(conf)

    val myRdd = new JdbcRDD(sc, () => DriverManager.getConnection(url,username,password),
      "SELECT * FROM sakila.actor limit ?,?",10,20,1,
      r => r.getString("first_name") + "," +r.getString("last_name"))

    myRdd.foreach(println)

  }

}
