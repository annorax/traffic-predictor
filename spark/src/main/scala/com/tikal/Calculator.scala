import java.util.logging.{Level, Logger}
import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Created by yitzchakl on 27/02/17.
  */
object Calculator {

  def main(args: Array[String]): Unit = {

    Logger.getLogger("org").setLevel(Level.WARNING)

    val sparkConf = new SparkConf().setAppName("traffic-predictor").setMaster("local[*]")
    val ssc = new StreamingContext(sparkConf, Seconds(2))
    val file = "file:///tmp/log"
    //val lines = ssc.fileStream[LongWritable, Text, TextInputFormat](file).window(Seconds(10), Seconds(2))

    val lines = ssc.textFileStream(file).window(Seconds(10), Seconds(2))

    lines.foreachRDD(rdd => {
      if (rdd != null && rdd.count() > 0) {
        val _rdd = rdd.map(_.toDouble)
        val count = rdd.count()
        val mean = _rdd.mean()
        val devs = rdd.map(score => (score.toDouble - mean) * (score.toDouble - mean))
        val stddev = Math.sqrt(devs.sum / (count - 1))
        println(s"Mean: ${mean} stdev: ${stddev}")
        println("-------------")
      }
    })

    ssc.start()
    ssc.awaitTermination()

    //val output = lines.map{ case(_,x) => x }
  }

}
