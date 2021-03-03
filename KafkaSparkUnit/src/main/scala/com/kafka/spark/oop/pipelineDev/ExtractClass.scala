package com.kafka.spark.oop.pipelineDev

import com.kafka.spark.oop.pipelineDev.ApplicationProperties.kafkaProperties
import com.kafka.spark.oop.pipelineDev.projectUtils.PropType
import org.apache.spark.sql
import org.apache.spark.sql.SparkSession

object ExtractClass extends Serializable {

  // extract data from kafka producer with spark
  def extractFromKafkaProducer(session: SparkSession, brokers:String, topic: String):sql.DataFrame=
    session.readStream.format("kafka").option("kafka.bootstrap.servers", brokers).option("subscribe", topic).load()

}
