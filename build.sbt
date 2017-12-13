name := "Spark_Applications"

version := "1.0"

scalaVersion := "2.10.5"

/*libraryDependencies += "org.apache.spark" % "spark-core_2.10" % "2.2.0"*/

// https://mvnrepository.com/artifact/org.apache.spark/spark-core
libraryDependencies += "org.apache.spark" %% "spark-core" % "1.6.0"

// https://mvnrepository.com/artifact/mysql/mysql-connector-java
libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.6"

libraryDependencies += "org.apache.spark" % "spark-streaming-twitter_2.10" % "1.6.3"

// https://mvnrepository.com/artifact/org.apache.spark/spark-streaming
libraryDependencies += "org.apache.spark" %% "spark-streaming" % "1.6.0" //% "provided"

// https://mvnrepository.com/artifact/org.apache.spark/spark-hive
libraryDependencies += "org.apache.spark" %% "spark-hive" % "1.6.0"

// https://mvnrepository.com/artifact/org.apache.spark/spark-sql
libraryDependencies += "org.apache.spark" %% "spark-sql" % "1.6.0"

// Tess Version
libraryDependencies ++= Seq(
  "net.sourceforge.tess4j" % "tess4j" % "3.4.2"
)