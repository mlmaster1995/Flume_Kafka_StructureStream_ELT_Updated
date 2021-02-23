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

object ApplicationProperties extends Serializable {

  type PropType = Map[String, String]

  val sparkProperties: PropType = Map(
    "mode" -> "local",
    "name" -> "eltPipeline"
  )

  val kafkaProperties: PropType = Map(
    "brokers" -> "localhost:9092",
    "topic_I" -> "exec",
    "topic_II" -> "toKafka",
    "topic_III" -> "tweet",
  )

  val mySQLProperties: PropType = Map(
    "url" -> "...",
    "driver" -> "com.mysql.cj.jdbc.Driver",
    "username" -> "...",
    "password" -> "...",
    "database" -> "...",
    "table_vmstat" -> "...",
    "table_tweet" ->"...",
    "mode" -> "..."
  )


  val consoleProperties: PropType = Map(
    "mode" -> "append"
  )

  val hdfsProperties: PropType = Map(
    "hdfsPath" -> "hdfs://localhost:9000/user/ky/data/stream_data/",
    "checkpointPath" -> "hdfs://localhost:9000/user/ky/checkpoint/",
    "format" -> "parquet",
    "mode" -> "append",
    "compressionType" -> "snappy"
  )

  val hiveProperties: PropType = Map(
    "warehousePath" -> "hdfs://localhost:9000/user/hive/warehouse/",
    "checkpointPath" -> "hdfs://localhost:9000/user/ky/checkpoint/",
    "format" -> "parquet",
    "mode" -> "append",
    "compressionType" -> "snappy",
    "database" -> "chrisy",
    "table_vmstat" -> "fromStream",
    "table_tweet" -> "fromTweet",
    "partitions" -> "1"
  )

  val mongodbProperties: PropType = Map(
    "mongoInputURI" -> "mongodb://127.0.0.1/test.fromstream",
    "mongoOutputURI" -> "mongodb://127.0.0.1/test.fromstream"
  )




}
