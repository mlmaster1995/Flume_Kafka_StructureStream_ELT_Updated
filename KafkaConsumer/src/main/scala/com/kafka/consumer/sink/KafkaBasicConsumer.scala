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

import org.apache.kafka.clients.consumer.{ConsumerRecords, KafkaConsumer}
import java.time.Duration
import java.util
import java.util.Properties

object KafkaBasicConsumer extends Serializable {
  val props = new Properties
  props.put("bootstrap.servers", "localhost:9101")
  props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
  props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")

  props.put("group.id", "grp-1")
  props.put("enable.auto.commit", "true")
  props.put("auto.commit.interval.ms", "1000")
  props.put("session.timeout.ms", "15000")
  props.put("max.poll.records", "100")

  val consumer: KafkaConsumer[String, String] = new KafkaConsumer(props)
  consumer.subscribe(util.Arrays.asList("test"))

  while (true) {
    val consumerRecords: ConsumerRecords[String, String] = consumer.poll(Duration.ofMillis(1000))
    consumerRecords.forEach(record => println("=============== partition Id= " + record.partition + "  offset = " + record.offset + " value = " + record.value + "================="))
  }
}
