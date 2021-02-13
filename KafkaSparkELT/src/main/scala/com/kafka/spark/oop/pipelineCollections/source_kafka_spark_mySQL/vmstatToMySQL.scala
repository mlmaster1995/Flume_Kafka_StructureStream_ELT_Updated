package com.kafka.spark.oop.pipelineCollections.source_kafka_spark_mySQL

import com.kafka.spark.oop.pipelineDev.ApplicationProperties.{kafkaProperties, mySQLProperties}
import com.kafka.spark.oop.pipelineDev.ELTComponents
import com.kafka.spark.oop.pipelineDev.vmstatPipeUtils.{extractFunc, getSparkSession, transformFunc}
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
  ELTComponents.Load.toMysql(transformedSource, mySQLProperties("url"), mySQLProperties("database"), mySQLProperties("table_vmstat"),
    mySQLProperties("username"), mySQLProperties("password"), mySQLProperties("driver"), mySQLProperties("mode"))
}
