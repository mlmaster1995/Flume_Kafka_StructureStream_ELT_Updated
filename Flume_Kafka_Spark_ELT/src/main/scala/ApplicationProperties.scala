object ApplicationProperties extends Serializable {

  val sparkProperties:Map[String, String] = Map(
    "mode"-> "local",
    "name"-> "eltPipeline"
  )

  val kafkaBrokers:String = "localhost:9092"

  val mySQLProperties:Map[String, String] =Map(
    "url"-> "...",
    "driver"->"com.mysql.cj.jdbc.Driver",
    "username" -> "...",
    "password" -> "...",
    "database" -> "...",
    "table" -> "...",
    "mode" -> "..."
  )



}
