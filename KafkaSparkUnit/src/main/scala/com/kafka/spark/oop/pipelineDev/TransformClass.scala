/*
Copyright 2021 C.Young

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
  limitations under the License.
*/
package com.kafka.spark.oop.pipelineDev

import com.kafka.spark.oop.pipelineDev.AppInternalProperties.messageDelimiter
import org.apache.spark.sql
import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.functions.{split, udf}
import org.apache.spark.sql.types.{IntegerType, StringType}

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

  // transform vmstat stream into kafka producer input
  def transformVmstatForKafkaWriter(row: Row):String = {
    val rowMap: Map[String, AnyVal] = row.getValuesMap(row.schema.fieldNames)
    s"${rowMap("topic")}|${rowMap("time")}|${rowMap("r")}|${rowMap("b")}|${rowMap("swpd")}|${rowMap("buff")}|${rowMap("cache")}|${rowMap("si")}|" +
      s"${rowMap("so")}|${rowMap("bi")}|${rowMap("bo")}|${rowMap("in_val")}|${rowMap("cs")}|${rowMap("us")}|${rowMap("sy")}|${rowMap("id")}|${rowMap("wa")}|" +
      s"${rowMap("st")}"
  }

  // transform tweet stream into structured stream
  def transformTweetStream (session: SparkSession, source: sql.DataFrame):sql.DataFrame = {
    import session.implicits._
    val filterRow = udf { x: String => x.split(messageDelimiter("delimiterTweet")).filter(_!="END") }
    source
      .select(filterRow('value.cast(StringType)) alias "split_value")
      .select(
        'split_value(0) alias "tweet_time",
        'split_value(1) alias "user_id",
        'split_value(2) alias "full_name",
        'split_value(3) alias "tweet_id",
        'split_value(4) alias "tweet_source",
        'split_value(5) alias "is_truncated",
        'split_value(6) alias "is_rt",
        'split_value(7) alias "tweet_text")
  }

  // transform tweet stream into kafka producer input
  def transformTweetStreamForKafkaWriter (row: Row):String = {
    val rowMap: Map[String, AnyVal] = row.getValuesMap(row.schema.fieldNames)
    s"${rowMap("tweet_time")}|${rowMap("user_id")}|${rowMap("full_name")}|${rowMap("tweet_id")}|${rowMap("tweet_source")}|${rowMap("is_truncated")}|${rowMap("is_rt")}|${rowMap("tweet_text")}"
  }

  // transform covid19 batch data into structured stream
  def transformCovid19BatchData (session: SparkSession, source: sql.DataFrame):sql.DataFrame = {
    import session.implicits._
    source
      .select('timestamp,split('value.cast(StringType),messageDelimiter("delimiterCovid19")) alias "value")
      .select(
        'timestamp,
        'value(15) alias "province",
        'value(0).cast(IntegerType) alias "active_cases",
        'value(1).cast(IntegerType) alias "active_cases_change",
        'value(2).cast(IntegerType) alias "avaccine",
        'value(3).cast(IntegerType) alias "cases",
        'value(4).cast(IntegerType) alias "cumulative_avaccine",
        'value(5).cast(IntegerType) alias "cumulative_cases",
        'value(6).cast(IntegerType) alias "cumulative_cvaccine",
        'value(7).cast(IntegerType) alias "cumulative_deaths",
        'value(8).cast(IntegerType) alias "cumulative_dvaccine",
        'value(9).cast(IntegerType) alias "cumulative_recovered",
        'value(10).cast(IntegerType) alias "cumulative_testing",
        'value(11).cast(IntegerType) alias "cvaccine",
        'value(12) alias "date",
        'value(13).cast(IntegerType) alias "deaths",
        'value(14).cast(IntegerType) alias "dvaccine",
        'value(16).cast(IntegerType) alias "recovered",
        'value(17).cast(IntegerType) alias "testing",
        'value(18) alias "testing_info")
  }

  // transform covid19 batch data into kafka producer input
  def transformCovid19BatchDataForKafkaWriter (row: Row):String = {
    val rowMap: Map[String, AnyVal] = row.getValuesMap(row.schema.fieldNames)
    s"${rowMap("province")}|${rowMap("active_cases")}|${rowMap("active_cases_change")}|${rowMap("avaccine")}|${rowMap("cases")}|${rowMap("cumulative_avaccine")}|${rowMap("cumulative_cases")}" +
      s"|${rowMap("cumulative_cvaccine")}|${rowMap("cumulative_deaths")}|${rowMap("cumulative_dvaccine")}|${rowMap("cumulative_recovered")}|${rowMap("cumulative_testing")}" +
      s"|${rowMap("cvaccine")}|${rowMap("date")}|${rowMap("deaths")}|${rowMap("dvaccine")}|${rowMap("recovered")}|${rowMap("testing")}|${rowMap("testing_info")}"
  }

}
