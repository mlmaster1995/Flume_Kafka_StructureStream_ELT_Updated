/*
Copyright 2021 C.Young

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
  limitations under the License.
*/

package com.kafka.spark.oop.pipelineDev

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.spark.sql.{ForeachWriter, Row}

import java.util.Properties

object CustomSinkClasses extends Serializable {
  /*
   - write data to kafka
  */
  class WriteToBasicKafkaProducer(val topic: String, val servers: String, val func: Row => String) extends ForeachWriter[Row] {
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
