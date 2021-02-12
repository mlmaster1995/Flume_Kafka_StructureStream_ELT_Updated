package com.kafka.spark.oop.pipelines.source_kafka_spark_console

import com.kafka.spark.oop.dev.ApplicationProperties.{consoleProperties, kafkaProperties}
import com.kafka.spark.oop.dev.ELTComponents
import com.kafka.spark.oop.dev.twitterPipeUtils.{extractFunc, getSparkSession, transformFunc}
import org.apache.spark.sql

object TweetToConsole extends Serializable {

  def main(args: Array[String]): Unit = {
    // create a spark session
    val spark = getSparkSession

    // extract data
    val dataSource: sql.DataFrame = ELTComponents.extract(spark, kafkaProperties("topic_III"), extractFunc)

    // transform data
    val transformedSource: sql.DataFrame = ELTComponents.transform(spark, dataSource, transformFunc)

    // load data
    ELTComponents.Load.toConsole(transformedSource, mode = consoleProperties("mode"))

  }

}
