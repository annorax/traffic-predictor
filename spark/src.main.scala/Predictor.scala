import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Created by yitzchakl on 27/02/17.
  */
class Predictor {

  val sparkConf = new SparkConf().setAppName("traffic-predictor")
  val ssc = new StreamingContext(sparkConf, Seconds(30))
  val lines = ssc.fileStream[LongWritable, Text, TextInputFormat]("/").window(Seconds(10), Seconds(2))

  val output = lines.map{ case(_,x) => x }
}
