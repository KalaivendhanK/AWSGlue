import com.amazonaws.services.glue.GlueContext
import com.amazonaws.services.glue.util.GlueArgParser
import com.amazonaws.services.glue.util.Job
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.Dataset
import org.apache.spark.sql.Row
import org.apache.spark.sql.SaveMode
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.from_json
import org.apache.spark.sql.streaming.Trigger

import scala.collection.JavaConverters._
import java.util.Calendar

object AWSGlueTest1 {
    def main(sysArgs: Array[String]):Unit = {
      val sparkConf = new SparkConf().setAppName("SOME APP NAME").setMaster("local[*]")
      val spark: SparkContext = new SparkContext(sparkConf)
      val glueContext: GlueContext = new GlueContext(spark)
      val sparkSession: SparkSession = glueContext.getSparkSession
//      val sparkSession: SparkSession = SparkSession.builder().master("local").getOrCreate()
      import sparkSession.implicits._
      // @params: [JOB_NAME]
      val args = GlueArgParser.getResolvedOptions(sysArgs, Seq("JOB_NAME").toArray)
      Job.init(args("JOB_NAME"), glueContext, args.asJava)

      val staticData = sparkSession.read          // read() returns type DataFrameReader
        .format("json")
        .option("header", "true")
        .load("s3://yext-integration-history-entities/json/")  // load() returns a DataFrame

      staticData.show()
//      val datasource0 = sparkSession.readStream   // readstream() returns type DataStreamReader
//        .format("kinesis")
//        .option("streamName", "stream-join-demo")
//        .option("endpointUrl", "https://kinesis.us-east-1.amazonaws.com")
//        .option("startingPosition", "TRIM_HORIZON")
//        .load                                     // load() returns a DataFrame
//
//      val selectfields1 = datasource0.select(from_json($"data".cast("string"), glueContext.getCatalogSchemaAsSparkSchema("stream-demos", "stream-join-demo2")) as "data").select("data.*")
//
//      val datasink2: Unit = selectfields1.writeStream.foreachBatch { (dataFrame: Dataset[Row], batchId: Long) => {   //foreachBatch() returns type DataStreamWriter
//        val joined = dataFrame.join(staticData, "product_id")
//        val year: Int = Calendar.getInstance().get(Calendar.YEAR)
//        val month :Int = Calendar.getInstance().get(Calendar.MONTH) + 1
//        val day: Int = Calendar.getInstance().get(Calendar.DATE)
//        val hour: Int = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
//
//        if (dataFrame.count() > 0) {
//          joined.write                           // joined.write returns type DataFrameWriter
//            .mode(SaveMode.Append)
//            .format("parquet")
//            .option("quote", " ")
//            .save("s3://awsexamplebucket-streaming-demo2/output/" + "/year=" + "%04d".format(year) + "/month=" + "%02d".format(month) + "/day=" + "%02d".format(day) + "/hour=" + "%02d".format(hour) + "/")
//        }
//      }
//      }  // end foreachBatch()
//        .trigger(Trigger.ProcessingTime("100 seconds"))
//        .option("checkpointLocation", "s3://awsexamplebucket-streaming-demo2/checkpoint/")
//        .start().awaitTermination()              // start() returns type StreamingQuery
      Job.commit()
    }
}
