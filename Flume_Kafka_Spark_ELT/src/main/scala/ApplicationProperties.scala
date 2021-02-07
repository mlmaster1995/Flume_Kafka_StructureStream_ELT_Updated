object ApplicationProperties extends Serializable {

  type PropType = Map[String, String]

  val sparkProperties: PropType = Map(
    "mode"-> "local",
    "name"-> "eltPipeline"
  )

    val kafkaProperties: PropType = Map(
    "brokers"-> "localhost:9092",
    "topic_I" -> "exec",
    "topic_II" -> "toKafka",
  )

  val mySQLProperties:PropType =Map(
    "url"-> "...",
    "driver"->"com.mysql.cj.jdbc.Driver",
    "username" -> "...",
    "password" -> "...",
    "database" -> "...",
    "table" -> "...",
    "mode" -> "..."
  )

  val consoleProperties:PropType = Map(
    "mode"->"append"
  )

  val hdfsProperties:PropType =Map(
    "hdfsPath"->"data/stream_data/",
    "checkpointPath"->"checkpoint/",
    "format" -> "parquet",
    "mode"->"append",
    "compressionType"->"snappy"
  )

  val hiveProperties:PropType=Map(
    "warehousePath"->"user/hive/warehouse",
    "checkpointPath"->"checkpoint/",
    "format" -> "parquet",
    "mode"->"append",
    "compressionType" -> "snappy",
    "database" ->"chrisy",
    "table"->"fromStream",
    "partitions"->"1"
  )

  val mongodbProperties:PropType=Map(
    "mongoInputURI"->"mongodb://127.0.0.1/test.fromstream",
    "mongoOutputURI" ->"mongodb://127.0.0.1/test.fromstream"
  )


}
