package com.kafka.spark.oop.pipelineDev

import org.apache.spark.sql
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.udf
import org.apache.spark.sql.types.StringType

object TransformClass extends Serializable {

  // transform vmstat stream into structured stream
  def transformVmstatStream (session: SparkSession, source: sql.DataFrame):sql.DataFrame = {
    import session.implicits._

    val filterRow = udf { x: String => x.split("\\W").filter(y => y.length > 0) }
    source
      .withWatermark("timestamp", "1 seconds")
      .withColumn("raw_value", 'value.cast(StringType))
      .where(!'raw_value.contains("memory") and !'raw_value.contains("buff")).withColumn("value", filterRow('raw_value))
      .select(
        'topic,
        'timestamp alias "time",
        $"value"(0) alias "r",
        $"value"(1) alias "b",
        $"value"(2) alias "swpd",
        $"value"(3) alias "free",
        $"value"(4) alias "buff",
        $"value"(5) alias "cache",
        $"value"(6) alias "si",
        $"value"(7) alias "so",
        $"value"(8) alias "bi",
        $"value"(9) alias "bo",
        $"value"(10) alias "in_val",
        $"value"(11) alias "cs",
        $"value"(12) alias "us",
        $"value"(13) alias "sy",
        $"value"(14) alias "id",
        $"value"(15) alias "wa",
        $"value"(16) alias "st")
  }
}
