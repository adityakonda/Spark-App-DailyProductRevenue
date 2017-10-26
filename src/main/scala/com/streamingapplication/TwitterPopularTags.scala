package com.streamingapplication

import org.apache.spark.SparkConf
import org.apache.spark.streaming.twitter.TwitterUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}


/**
  * Created by adity on 10/25/2017.
  */
object TwitterPopularTags {

  def main(args: Array[String]): Unit = {


    if (args.length < 4){
      System.err.println("Usage: TwitterPopularTags should have minimum of 4 parameters " +
        "<Consumer Key (API Key)> " +
        "<Consumer Secret (API Secret)>" +
        " <Access Token> " +
        "<Access Token Secret>")
      System.exit(1)
    }

    val Array(consumerKey,consumerSecret,accessToken,accessTokenSecret) = args.take(4)
    val filters = args.takeRight(args.length - 4)

    System.setProperty("twitter4j.oauth.consumerKey", consumerKey)
    System.setProperty("twitter4j.oauth.consumerSecret", consumerSecret)
    System.setProperty("twitter4j.oauth.accessToken", accessToken)
    System.setProperty("twitter4j.oauth.accessTokenSecret", accessTokenSecret)


    // Configuring Spark Context and Spark Streaming Context
    val sparkConf = new SparkConf()
    sparkConf.setMaster("local")
    sparkConf.setAppName("Twitter Streaming")
    val ssc = new StreamingContext(sparkConf,Seconds(2))
    val stream = TwitterUtils.createStream(ssc,None,filters)

/*
    val hashTags = stream.flatMap(status => status.getText.split(" ").filter( `))
*/
  }
}
