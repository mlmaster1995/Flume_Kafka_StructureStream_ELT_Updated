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

package com.twitter.stream.source

object ApplicationProperties extends Serializable {
  type PropType = Map[String, String]

  // props from twitter API account
  val twitterAPIProps:PropType = Map(
    "API_key" -> "...",
    "API_secrete_key" -> "...",
    "Bear_token" -> "...",
    "Access_token" -> "...",
    "Access_token_secret" -> "...",
  )



  // basic props for kafka producer config, more props could be added for more configs
  var kafkaBasicProducerConfig: PropType = Map(
    "bootstrap.servers" -> "localhost:9101",
    "key.serializer" -> "org.apache.kafka.common.serialization.StringSerializer",
    "value.serializer" -> "org.apache.kafka.common.serialization.StringSerializer",
    "acks" -> "1",                                                                    // !!!ack: forget-and-fire & 0, sync & 1, async & 1
    "retries" ->"1",
    "linger.ms" -> "1",
    "batchSize" -> "16384"
  )


  // props for kafka producer with avro schema
  val kafkaAvroProducerConfig:PropType =Map(
    "bootstrap.servers" -> "localhost:9101",
    "schema.registry.url" -> "http://localhost:8081",
    "key.serializer" -> "io.confluent.kafka.serializers.KafkaAvroSerializer",
    "value.serializer"-> "io.confluent.kafka.serializers.KafkaAvroSerializer",
    "acks" -> "1",                                                                    // !!!ack: 0 & mode= forget-and-fire, 1 & mode = sync,  1 & mode = async
    "retries" ->"1",
    "linger.ms" -> "1",
    "batchSize" -> "16384"
  )

  // other props for kafka producer
  val kafkaProducerMessageProps:PropType =Map(
    "delimiter" -> "&&&&",                                                            // delimiter used to concat all tweet info into an string
    "mode" -> "async",                                                                // !!!mode: forget-and-fire & ack=0, sync & ack=1, async & ack=1
    "tweetTopic"->"tweet",
    "tweetAvroTopic" ->"tweetAvro"
  )

}
