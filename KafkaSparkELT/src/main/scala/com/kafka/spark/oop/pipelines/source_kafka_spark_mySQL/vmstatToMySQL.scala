package com.kafka.spark.oop.pipelines.source_kafka_spark_mySQL

import com.kafka.spark.oop.dev.ApplicationProperties.{kafkaProperties, mySQLProperties}
import com.kafka.spark.oop.dev.ELTComponents
import com.kafka.spark.oop.dev.vmstatPipeUtils.{extractFunc, getSparkSession, transformFunc}
import org.apache.spark.sql

// write data to mysql
object vmstatToMySQL extends Serializable {

  // build a session
  val spark = getSparkSession

  // extract data
  val dataSource: sql.DataFrame = ELTComponents.extract(spark,  kafkaProperties("topic_I"), extractFunc)

  // transform data
  val transformedSource: sql.DataFrame = ELTComponents.transform(spark, dataSource, transformFunc)

  // load data
  ELTComponents.Load.toMysql(transformedSource, mySQLProperties("url"), mySQLProperties("database"), mySQLProperties("table"),
    mySQLProperties("username"), mySQLProperties("password"), mySQLProperties("driver"), mySQLProperties("mode"))
}
