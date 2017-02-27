package com.tikal


import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.sql.SQLContext
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Created by doron on 27/02/2017.
  */
object Predictor {

  def main(args: Array[String]): Unit = {

    Logger.getLogger("org").setLevel(Level.WARN)

    val sparkConf = new SparkConf().setAppName("traffic-predictor").setMaster("local[*]")
    val ssc = new StreamingContext(sparkConf, Seconds(2))
    val file = "file:///tmp/log"
    //val lines = ssc.fileStream[LongWritable, Text, TextInputFormat](file).window(Seconds(10), Seconds(2))

    // Load training data
    val sqlContext = new SQLContext(ssc.sparkContext)
    import sqlContext.implicits._

    val training = ssc.sparkContext.textFile("/tmp/training-data").map(row => row.split(" "))
        .map(row => (row(0).toDouble, row(1).toDouble, row(2).toInt))
      .toDF("numberOfRequests", "averageResponse", "label")

    val assembler = new VectorAssembler()
      .setInputCols(Array("numberOfRequests", "averageResponse"))
      .setOutputCol("features")


    val lr = new LogisticRegression()
      .setMaxIter(10)
      .setRegParam(0.3)
      .setElasticNetParam(0.8)

    val output = assembler.transform(training)

    // Fit the mode
    val lrModel = lr.fit(output)

    val lines = ssc.textFileStream(file).window(Seconds(10), Seconds(2))

    lines.foreachRDD(rdd => {
      if (rdd != null && rdd.count() > 0) {
        val _rdd = rdd.map(_.toDouble)
        val numberOfRequests = rdd.count()
        val count = rdd.count()
        val averageResponseTime = _rdd.mean()
        val coefficients = lrModel.coefficients
        val logit = coefficients(0)*numberOfRequests + coefficients(1) * averageResponseTime
        println(s"Requests: ${numberOfRequests} average response: ${averageResponseTime}, logit: ${logit}")
        if (logit > 2) {
          write("1")
        } else {
          write("0")
        }

        println("-------------")

      }
    })


    ssc.start()
    ssc.awaitTermination()
  }

  def write(status:String):Unit = {
    import java.io._
    val pw = new PrintWriter(new File("../status"))
    pw.write(status)
    pw.close
  }
}
