
package com.twitter.stream.source

object ApplicationProperties extends Serializable {
  type PropType = Map[String, String]

  val twitterAPIProperties:PropType = Map(
    "API_key" -> "...",
    "API_secrete_key" -> "...",
    "Bear_token" -> "...",
    "Access_token" -> "...",
    "Access_token_secret" -> "...",
  )

  val kafkaProperties: PropType = Map(
    "brokers" -> "localhost:9092",
    "topic" -> "tweet",
  )

}
