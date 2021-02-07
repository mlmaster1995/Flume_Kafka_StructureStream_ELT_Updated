package com.kafka.spark.pipeline.dev

import com.kafka.spark.pipeline.dev.ApplicationProperties.{kafkaProperties, mongodbProperties, sparkProperties}
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql
import org.apache.spark.sql.functions.udf
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.{Row, SparkSession}

trait PipelineUtils extends Serializable{
  // user-define-function to return a SparkSession class
  def getSparkSession:SparkSession
  // user-define-function to specify the extract function based on the source
  def extractFunc(session: SparkSession):sql.DataFrame
  // user-define-function to specify the transform function based on the sink
  def transformFunc (soruce: sql.DataFrame, session: SparkSession):sql.DataFrame
  // user-define-funciton to specify the extract function for kafka sink writer
  def extractRowDataForKafkaWriter (row: Row):String
}

case object vmstatPipeUtils extends Serializable with PipelineUtils {

  def getSparkSession: SparkSession= {
    Logger.getLogger("org").setLevel(Level.OFF)
    Logger.getLogger("akka").setLevel(Level.OFF)
    SparkSession
      .builder()
      .appName(s"${sparkProperties("name")}")
      .config("spark.mongodb.input.uri", mongodbProperties("mongoInputURI"))
      .config("spark.mongodb.output.uri", mongodbProperties("mongoOutputURI"))
      .master(s"${sparkProperties("mode")}")
      .enableHiveSupport()
      .getOrCreate()
  }

  def extractFunc(session: SparkSession):sql.DataFrame =
    session.readStream.format("kafka").option("kafka.bootstrap.servers", kafkaProperties("brokers")).option("subscribe", "exec").load()

  def transformFunc (source: sql.DataFrame, session: SparkSession):sql.DataFrame = {
    import session.implicits._

    val filterRow = udf { x: String => x.split("\\W").filter(y => y.length > 0) }
    source.withWatermark("timestamp", "1 seconds").withColumn("raw_value", 'value.cast(StringType))
      .where(!'raw_value.contains("memory") and !'raw_value.contains("buff")).withColumn("value", filterRow('raw_value))
      .select('topic, 'timestamp alias "time", $"value"(0) alias "r", $"value"(1) alias "b", $"value"(2) alias "swpd", $"value"(3) alias "free", $"value"(4) alias "buff",
        $"value"(5) alias "cache", $"value"(6) alias "si", $"value"(7) alias "so", $"value"(8) alias "bi", $"value"(9) alias "bo", $"value"(10) alias "in_val",
        $"value"(11) alias "cs", $"value"(12) alias "us", $"value"(13) alias "sy", $"value"(14) alias "id", $"value"(15) alias "wa", $"value"(16) alias "st")
  }

  def extractRowDataForKafkaWriter(row: Row):String = {
    val rowMap: Map[String, AnyVal] = row.getValuesMap(row.schema.fieldNames)
    s"${rowMap("topic")}|${rowMap("time")}|${rowMap("r")}|${rowMap("b")}|${rowMap("swpd")}|${rowMap("buff")}|${rowMap("cache")}|${rowMap("si")}|" +
      s"${rowMap("so")}|${rowMap("bi")}|${rowMap("bo")}|${rowMap("in_val")}|${rowMap("cs")}|${rowMap("us")}|${rowMap("sy")}|${rowMap("id")}|${rowMap("wa")}|" +
      s"${rowMap("st")}"
  }

}
