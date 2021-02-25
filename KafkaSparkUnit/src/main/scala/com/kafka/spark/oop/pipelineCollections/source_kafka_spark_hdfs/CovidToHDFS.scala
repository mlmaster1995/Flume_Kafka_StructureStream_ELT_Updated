package com.kafka.spark.oop.pipelineCollections.source_kafka_spark_hdfs

import com.kafka.spark.oop.pipelineDev.ApplicationProperties.{hdfsProperties, kafkaProperties}
import com.kafka.spark.oop.pipelineDev.Covid19PipeUtils.{extractFunc, getSparkSession, transformFunc}
import com.kafka.spark.oop.pipelineDev.ELTComponents
import org.apache.spark.sql

object CovidToHDFS extends Serializable {
  // build a spark session
  val spark = getSparkSession

  // extract data
  val dataSource: sql.DataFrame = ELTComponents.extract(spark, kafkaProperties("topicCovid19"), extractFunc)

  // transform data
  val transformedSource: sql.DataFrame = ELTComponents.transform(spark, dataSource, transformFunc)

  // load data
  ELTComponents.Load.toHDFS(transformedSource, hdfsProperties("hdfsPath"), hdfsProperties("checkpointPath"),
    hdfsProperties("format"), hdfsProperties("mode"), hdfsProperties("compressionType"))
}
