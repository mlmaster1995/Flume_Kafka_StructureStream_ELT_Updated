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

  val twitterAPIProperties:PropType = Map(
    "API_key" -> "...",
    "API_secrete_key" -> "...",
    "Bear_token" -> "...",
    "Access_token" -> "...",
    "Access_token_secret" -> "...",
  )


  val kafkaProperties: Map[String, String] = Map(
    "delimiter" -> "&&&&",        // delimiter used to concat all tweet info into an string
    "mode" -> "fire-and-forget",  // available modes: fire-and-forget, sync, async
    "brokers" -> "localhost:9101",
    "topic" -> "tweet",
    "sync" -> "0",                // accept acc
    "retries" ->"0",              // retry in async mode
    "linger" -> "300",            // linger time 300ms
    "batchSize" -> "16384"        // batch size default at 16384
  )

}
