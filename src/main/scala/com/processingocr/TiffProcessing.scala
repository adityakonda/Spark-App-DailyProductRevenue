package com.processingocr

import java.io.ByteArrayInputStream
import javax.imageio.ImageIO

import net.sourceforge.tess4j.Tesseract
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by adity on 12/13/2017.
  */
object TiffProcessing {

  def main(args: Array[String]): Unit = {

    /*    SETTING UP SPARK CONFIGURATION   */
    val conf = new SparkConf()
    conf.setAppName("Processing Tiff Files")
    conf.setMaster("yarn-client")
    conf.setExecutorEnv("TESSDATA_PREFIX","/usr/local/share/tessdata/")

    val sc = new SparkContext(conf)
    val path = args(0)
    val files = sc.binaryFiles(path)
    val data = files.mapPartitions((f) => {
      val tess = new Tesseract
      f.map(x => (x._1, tess.doOCR(ImageIO.read(new ByteArrayInputStream(x._2.toArray())))))
    })
    val r = data.first()
    println(r._2)
    data.saveAsTextFile("res")

  }
}
