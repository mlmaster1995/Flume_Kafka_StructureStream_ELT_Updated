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
package com.kafka.consumer.sink

import com.kafka.consumer.sink.ApplicationProperties.{kafkaAvroConsumerConfig, kafkaBasicConsumerConfig, kafkaConsumerMessageProps}
import com.kafka.consumer.sink.KafkaConsumerUtils.setConsumerProps
import org.apache.kafka.clients.consumer.KafkaConsumer
import tweet.kafka.avro.Tweet

import java.time.Duration
import java.util

object KafkaConsoleConsumer extends Serializable with App{
  // flag for avro schema consumer
  val withAvroSchema:Boolean= true

  // getting consumer instance with proper config
  val consumer = if(withAvroSchema) new KafkaConsumer[String, Tweet](setConsumerProps(kafkaAvroConsumerConfig)) else new KafkaConsumer[String, String](setConsumerProps(kafkaBasicConsumerConfig))
  val consumerTopic = if(withAvroSchema) kafkaConsumerMessageProps("tweetAvroTopic") else kafkaConsumerMessageProps("tweetTopic")
  consumer.subscribe(util.Arrays.asList(consumerTopic))

  // polling messages
  while(true){
    val consumerRecords = consumer.poll(Duration.ofMillis(1000))
    consumerRecords.forEach(record => println(s"partitionId: ${record.partition} offset: ${record.offset} key: ${record.key} value: ${record.value}"))
  }
}
