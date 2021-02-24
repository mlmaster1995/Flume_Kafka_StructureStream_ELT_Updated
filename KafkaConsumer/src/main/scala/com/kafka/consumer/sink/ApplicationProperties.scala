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

object ApplicationProperties extends Serializable {

  type PropType = Map[String, String]

  // props for basic kafka consumer
  val kafkaBasicConsumerConfig: PropType = Map(
    "bootstrap.servers" -> "localhost:9101",
    "key.deserializer" -> "org.apache.kafka.common.serialization.StringDeserializer",
    "value.deserializer" -> "org.apache.kafka.common.serialization.StringDeserializer",
    "group.id"-> "grp-1",
    "enable.auto.commit" -> "true",
    "auto.commit.interval.ms" -> "1000",
    "session.timeout.ms"-> "15000",
    "max.poll.records" -> "100",
  )

  // props for avro kafka consumer
  val kafkaAvroConsumerConfig: PropType = Map(
    "schema.registry.url" -> "http://localhost:8081",
    "bootstrap.servers" -> "localhost:9101",
    "key.deserializer"-> "io.confluent.kafka.serializers.KafkaAvroDeserializer",
    "value.deserializer"-> "io.confluent.kafka.serializers.KafkaAvroDeserializer",
    "specific.avro.reader" -> "true",
    "group.id"-> "grp-1",
    "enable.auto.commit" -> "true",
    "auto.commit.interval.ms" -> "1000",
    "session.timeout.ms"-> "15000",
    "max.poll.records" -> "100",
  )

  val kafkaConsumerMessageProps: PropType = Map(
    "tweetTopic"->"tweet",
    "tweetAvroTopic" ->"tweetAvro",
  )

}
