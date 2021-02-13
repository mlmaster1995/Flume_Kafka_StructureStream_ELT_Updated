package com.kafka.spark.oop.pipelineCollections.source_kafka_spark_mongoDB

import com.kafka.spark.oop.pipelineDev.ApplicationProperties.kafkaProperties
import com.kafka.spark.oop.pipelineDev.ELTComponents
import com.kafka.spark.oop.pipelineDev.twitterPipeUtils.{extractFunc, getSparkSession, transformFunc}
import org.apache.spark.sql

object TweetToMongoDB extends Serializable {
  // build a session
  val spark = getSparkSession

  // extract data
  val dataSource: sql.DataFrame = ELTComponents.extract(spark, kafkaProperties("topic_III"), extractFunc)

  // transform data
  val transformedSource: sql.DataFrame = ELTComponents.transform(spark, dataSource, transformFunc)

  // load data
  ELTComponents.Load.toMongoD(transformedSource)

}
