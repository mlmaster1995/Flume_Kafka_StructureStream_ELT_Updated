package com.kafka.spark.pipeline.dev

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.spark.sql.{ForeachWriter, Row}

import java.util.Properties

object CustomSinkClasses extends Serializable {

  /*
   - write data to kafka
  */
  class WriteToKafka(val topic: String, val servers: String, val func: Row => String) extends ForeachWriter[Row] {
    // set up kafka properties
    val kafkaProperties = new Properties()
    kafkaProperties.put("bootstrap.servers", servers)
    kafkaProperties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    kafkaProperties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    // set up a global producer
    var producer: KafkaProducer[String, String] = _
    // override ForeachWriter class
    def open(partitionId: Long, epochId: Long) = {producer = new KafkaProducer(kafkaProperties); true}
    def process(row: Row) = producer.send(new ProducerRecord(topic, func(row)))
    def close(errorOrNull: Throwable) = producer.close
  }

}
