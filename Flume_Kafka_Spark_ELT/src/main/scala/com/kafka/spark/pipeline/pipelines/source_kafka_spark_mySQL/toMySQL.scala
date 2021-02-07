package com.kafka.spark.pipeline.pipelines.source_kafka_spark_mySQL

import com.kafka.spark.pipeline.dev.ApplicationProperties.mySQLProperties
import com.kafka.spark.pipeline.dev.ELTComponents
import com.kafka.spark.pipeline.dev.PipelineUtils.{extracFunc, getSparkSession, transformFunc}
import org.apache.spark.sql

// write data to mysql
object toMySQL extends Serializable {

  // build a session
  val spark = getSparkSession

  // extract data
  val dataSource: sql.DataFrame = ELTComponents.extract(spark, extracFunc)

  // transform data
  val transformedSource: sql.DataFrame = ELTComponents.transform(spark, dataSource, transformFunc)

  // load data
  ELTComponents.Load.toMysql(transformedSource, mySQLProperties("url"), mySQLProperties("database"), mySQLProperties("table"),
    mySQLProperties("username"), mySQLProperties("password"), mySQLProperties("driver"), mySQLProperties("mode"))
}
