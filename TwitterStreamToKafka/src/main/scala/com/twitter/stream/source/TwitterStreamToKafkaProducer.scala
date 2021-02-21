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

import com.twitter.stream.source.ApplicationProperties.{kafkaProps, twitterAPIProperties}
import com.twitter.stream.source.TwitterStreamUtils.{concatTweetData, getTweetConfig, getTweetStream, schemaTweetData, writeToKafkaProducer, writeToKafkaProducerAvro}
import twitter4j.{StallWarning, Status, StatusDeletionNotice, StatusListener, TwitterStream}

object TwitterStreamToKafkaProducer extends Serializable with App{
  // set up twitter api config
  val config = getTweetConfig(api_key = twitterAPIProperties("API_key"),
                              api_secret_key = twitterAPIProperties("API_secrete_key"),
                              access_token = twitterAPIProperties("Access_token"),
                              access_token_secret = twitterAPIProperties("Access_token_secret"))
  // get twitterStream instance
  val twitterStream: TwitterStream = getTweetStream(config)
  // with schema or not
  val withAvro:Boolean = true
  /*
   - set up the tweet status
   - "writeToKafkaProducer" is to produce messages in the plain string
   - "writeToKafkaProducerAvro" is to produce messages with certain schema via Schema Registry
  */
  twitterStream.addListener(new StatusListener() {

    override def onStatus(status: Status): Unit =  {
      if(!withAvro)
        writeToKafkaProducer(mode=kafkaProps("mode"), bootstrap= kafkaProps("brokers"), ack= kafkaProps("ack"), retry = kafkaProps("retries"), linger= kafkaProps("linger"),
          batchSize = kafkaProps("batchSize"), kafkaTopic = kafkaProps("topic"), message = concatTweetData(status, kafkaProps("delimiter")))
      else
        writeToKafkaProducerAvro(mode=kafkaProps("mode"), schemaRegistryURL= kafkaProps("schemaRegistryURL"),bootstrap= kafkaProps("brokers"), ack= kafkaProps("ack"),
          retry = kafkaProps("retries"), linger= kafkaProps("linger"), batchSize = kafkaProps("batchSize"), kafkaTopic = kafkaProps("topic"), message = schemaTweetData(status))
    }

    override def onDeletionNotice(statusDeletionNotice: StatusDeletionNotice): Unit = {}
    override def onTrackLimitationNotice(numberOfLimitedStatuses: Int): Unit = {}
    override def onScrubGeo(userId: Long, upToStatusId: Long): Unit = {}
    override def onStallWarning(warning: StallWarning): Unit = {}
    override def onException(ex: Exception): Unit = {}
  })
  // start to sample english tweets
  twitterStream.sample(twitterAPIProperties("language"))
}
