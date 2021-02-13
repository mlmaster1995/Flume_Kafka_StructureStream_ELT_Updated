package com.kafka.spark.oop.pipelineCollections.source_kafka_spark_hiveTable

import com.kafka.spark.oop.pipelineDev.ApplicationProperties.{hiveProperties, kafkaProperties}
import com.kafka.spark.oop.pipelineDev.ELTComponents
import com.kafka.spark.oop.pipelineDev.vmstatPipeUtils.{extractFunc, getSparkSession, transformFunc}
import org.apache.spark.sql

object vmstatToHiveTable extends Serializable {
  // create a spark session
  val spark = getSparkSession

  // load data
  val dataSource: sql.DataFrame = ELTComponents.extract(spark,  kafkaProperties("topic_I"), extractFunc)

  // transform data
  val transformedSource: sql.DataFrame = ELTComponents.transform(spark, dataSource, transformFunc)

  ELTComponents.Load.toHiveTable(transformedSource, hiveProperties("format"), hiveProperties("mode"),
    hiveProperties("compressionType"), hiveProperties("database"), hiveProperties("table"), hiveProperties("partitions").toInt)
}
