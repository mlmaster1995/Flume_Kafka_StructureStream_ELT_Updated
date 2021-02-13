package com.kafka.spark.oop.pipelineCollections.source_kafka_spark_hiveTable

import com.kafka.spark.oop.pipelineDev.ApplicationProperties.{hiveProperties, kafkaProperties}
import com.kafka.spark.oop.pipelineDev.ELTComponents
import com.kafka.spark.oop.pipelineDev.twitterPipeUtils.{extractFunc, getSparkSession, transformFunc}
import org.apache.spark.sql

object TweetToHiveTable extends Serializable {
  // create a spark session
  val spark = getSparkSession

  // load data
  val dataSource: sql.DataFrame = ELTComponents.extract(spark, kafkaProperties("topic_III"), extractFunc)

  // transform data
  val transformedSource: sql.DataFrame = ELTComponents.transform(spark, dataSource, transformFunc)

  ELTComponents.Load.toHiveTable(transformedSource, hiveProperties("format"), hiveProperties("mode"),
    hiveProperties("compressionType"), hiveProperties("database"), hiveProperties("table_tweet"), hiveProperties("partitions").toInt)
}
