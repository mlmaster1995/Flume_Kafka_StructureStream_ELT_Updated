package com.kafka.spark.oop.pipelines.source_kafka_spark_console

import com.kafka.spark.oop.dev.ApplicationProperties.{consoleProperties, kafkaProperties}
import com.kafka.spark.oop.dev.ELTComponents
import com.kafka.spark.oop.dev.vmstatPipeUtils.{extractFunc, getSparkSession, transformFunc}
import org.apache.spark.sql

object vmstatToConsole extends Serializable {

  // build a session
  val spark = getSparkSession

  // extract data
  val dataSource: sql.DataFrame = ELTComponents.extract(spark, kafkaProperties("topic_I"), extractFunc)

  // transform data
  val transformedSource: sql.DataFrame = ELTComponents.transform(spark, dataSource, transformFunc)

  // load data
  ELTComponents.Load.toConsole(transformedSource, mode = consoleProperties("mode"))


}
