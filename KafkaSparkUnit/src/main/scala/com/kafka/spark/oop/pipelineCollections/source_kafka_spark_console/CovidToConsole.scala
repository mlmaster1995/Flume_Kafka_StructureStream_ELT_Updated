package com.kafka.spark.oop.pipelineCollections.source_kafka_spark_console

import com.kafka.spark.oop.pipelineDev.ApplicationProperties.{consoleProperties, kafkaProperties}
import com.kafka.spark.oop.pipelineDev.Covid19PipeUtils.{extractFunc, getSparkSession, transformFunc}
import com.kafka.spark.oop.pipelineDev.ELTComponents
import org.apache.spark.sql

object CovidToConsole extends Serializable {
  // create a spark session
  val spark = getSparkSession

  // extract data
  val dataSource: sql.DataFrame = ELTComponents.extract(spark, kafkaProperties("topicCovid19"), extractFunc)

  // transform data
  val transformedSource: sql.DataFrame = ELTComponents.transform(spark, dataSource, transformFunc)

  // load data
  ELTComponents.Load.toConsole(transformedSource, mode = consoleProperties("modeAppend"))
}
