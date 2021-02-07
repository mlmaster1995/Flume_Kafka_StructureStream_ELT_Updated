package com.kafka.spark.pipeline.pipelines.source_kafka_spark_Kafka

import com.kafka.spark.pipeline.dev.ApplicationProperties.kafkaProperties
import com.kafka.spark.pipeline.dev.ELTComponents
import com.kafka.spark.pipeline.dev.PipelineUtils.{extracFunc, extractRowDataForKafkaWriter, getSparkSession, transformFunc}
import org.apache.spark.sql

// write data stream back to kafka with a different topic
object toKafka extends Serializable {
  // build a spark session
  val spark = getSparkSession

  // extract data
  val dataSource: sql.DataFrame = ELTComponents.extract(spark, extracFunc)

  // transform data
  val transformedSource: sql.DataFrame = ELTComponents.transform(spark, dataSource, transformFunc)

  // load data
  ELTComponents.Load.toKafka(transformedSource, kafkaProperties("topic_II"), kafkaProperties("brokers"), extract_func = extractRowDataForKafkaWriter)
}
