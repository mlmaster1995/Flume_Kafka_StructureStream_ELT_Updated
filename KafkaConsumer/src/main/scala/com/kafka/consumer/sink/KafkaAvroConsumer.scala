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

import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig
import org.apache.kafka.clients.consumer.{ConsumerRecords, KafkaConsumer}

import java.time.Duration
import java.util
import java.util.Properties

object KafkaAvroConsumer extends Serializable {
  val props = new Properties
  props.put("bootstrap.servers", "localhost:9101")
  props.put("key.deserializer", "io.confluent.kafka.serializers.KafkaAvroDeserializer")
  props.put("value.deserializer", "io.confluent.kafka.serializers.KafkaAvroDeserializer")
  props.put("schema.registry.url", "http://localhost:8081") // Schema Registry url
  props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, "true")

  props.put("group.id", "grp-1")
  props.put("enable.auto.commit", "true")
  props.put("auto.commit.interval.ms", "1000")
  props.put("session.timeout.ms", "15000")
  props.put("max.poll.records", "100")


  val consumer: KafkaConsumer[String, Student] = new KafkaConsumer(props)
  consumer.subscribe(util.Arrays.asList("testAvro"))

  while (true) {
    val consumerRecords: ConsumerRecords[String, Student] = consumer.poll(Duration.ofMillis(1000))
    consumerRecords.forEach(r => println("Student record -> [Id: " + r.value().getId + ", Name: " + r.value().getName + ", email: " + r.value().getEmailAddress + "]"))
  }

}
