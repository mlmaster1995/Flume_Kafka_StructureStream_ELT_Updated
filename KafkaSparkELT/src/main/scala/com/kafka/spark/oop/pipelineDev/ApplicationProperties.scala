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
