package com.kafka.spark.oop.pipelineCollections.source_kafka_spark_Kafka

import com.kafka.spark.oop.pipelineDev.ApplicationProperties.kafkaProperties
import com.kafka.spark.oop.pipelineDev.ELTComponents
import com.kafka.spark.oop.pipelineDev.vmstatPipeUtils.{extractFunc, extractRowDataForKafkaWriter, getSparkSession, transformFunc}
import org.apache.spark.sql

// write data stream back to kafka with a different topic
object vmstatToKafka extends Serializable {
  // build a spark session
  val spark = getSparkSession

  // extract data
  val dataSource: sql.DataFrame = ELTComponents.extract(spark,  kafkaProperties("topic_I"), extractFunc)

  // transform data
  val transformedSource: sql.DataFrame = ELTComponents.transform(spark, dataSource, transformFunc)

  // load data
  ELTComponents.Load.toKafka(transformedSource, kafkaProperties("topic_II"), kafkaProperties("brokers"), extract_func = extractRowDataForKafkaWriter)
}
