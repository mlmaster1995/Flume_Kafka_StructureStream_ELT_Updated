package com.kafka.spark.oop.pipelineCollections.source_kafka_spark_hdfs

import com.kafka.spark.oop.pipelineDev.ApplicationProperties.{hiveProperties, kafkaProperties}
import com.kafka.spark.oop.pipelineDev.ELTComponents
import com.kafka.spark.oop.pipelineDev.twitterPipeUtils.{extractFunc, getSparkSession, transformFunc}
import org.apache.spark.sql

object TweetToHDFS extends Serializable {
  // build a spark session
  val spark = getSparkSession

  // extract data
  val dataSource: sql.DataFrame = ELTComponents.extract(spark, kafkaProperties("topic_III"), extractFunc)

  // transform data
  val transformedSource: sql.DataFrame = ELTComponents.transform(spark, dataSource, transformFunc)

  // load data
  ELTComponents.Load.toHDFS(transformedSource, hiveProperties("hdfsPath"), hiveProperties("checkpointPath"),
    hiveProperties("format"), hiveProperties("mode"), hiveProperties("compressionType"))
}
