package com.kafka.spark.pipeline.pipelines.source_kafka_spark_mySQL

import com.kafka.spark.pipeline.dev.ApplicationProperties.mySQLProperties
import com.kafka.spark.pipeline.dev.ELTComponents
import com.kafka.spark.pipeline.dev.vmstatPipeUtils.{extractFunc, getSparkSession, transformFunc}
import org.apache.spark.sql

// write data to mysql
object vmstatToMySQL extends Serializable {

  // build a session
  val spark = getSparkSession

  // extract data
  val dataSource: sql.DataFrame = ELTComponents.extract(spark, extractFunc)

  // transform data
  val transformedSource: sql.DataFrame = ELTComponents.transform(spark, dataSource, transformFunc)

  // load data
  ELTComponents.Load.toMysql(transformedSource, mySQLProperties("url"), mySQLProperties("database"), mySQLProperties("table"),
    mySQLProperties("username"), mySQLProperties("password"), mySQLProperties("driver"), mySQLProperties("mode"))
}
