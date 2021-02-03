object ApplicationProperties extends Serializable {

  val sparkProperties:Map[String, String] = Map(
    "mode"-> "local",
    "name"-> "eltPipeline"
  )

  val kafkaBrokers:String = "localhost:9092"

  val mySQLProperties:Map[String, String] =Map(
    "url"-> "jdbc:mysql://localhost:3306",
    "driver"->"com.mysql.cj.jdbc.Driver",
    "username" -> "root",
    "password" -> "!Jh_860526*",
    "database" -> "chrisy",
    "table" -> "fromStream",
    "mode" -> "append"
  )

  val hiveProperties:Map[String,String]=Map(
    "table"->"fromStream",
    "database"->"chrisy",
    "externalPath"->"data/stream_data/",
    "fileFormat"->"parquet",
    "mode" -> "append"
  )



}
