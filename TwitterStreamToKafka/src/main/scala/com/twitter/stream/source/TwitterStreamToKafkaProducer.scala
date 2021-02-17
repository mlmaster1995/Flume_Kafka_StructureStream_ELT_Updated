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

import com.twitter.stream.source.ApplicationProperties.{kafkaProperties, twitterAPIProperties}
import com.twitter.stream.source.TwitterStreamUtils.{concatTweetData, getTweetConfig, getTweetStream, writeToKafkaProducer}
import twitter4j.{StallWarning, Status, StatusDeletionNotice, StatusListener, TwitterStream, TwitterStreamFactory}
import java.util.logging.{Level, Logger}

object TwitterStreamToKafkaProducer extends Serializable with App{
  // set up twitter api config
  val config = getTweetConfig(api_key = twitterAPIProperties("API_key"),
                              api_secret_key = twitterAPIProperties("API_secrete_key"),
                              access_token = twitterAPIProperties("Access_token"),
                              access_token_secret = twitterAPIProperties("Access_token_secret"))
  // get twitterStream instance
  val twitterStream: TwitterStream = getTweetStream(config)
  // set up the tweet status
  twitterStream.addListener(new StatusListener() {
    override def onStatus(status: Status): Unit =  writeToKafkaProducer(mode=kafkaProperties("mode"),
                                                                        bootstrap= kafkaProperties("brokers"),
                                                                        ack= kafkaProperties("ack"),
                                                                        retry = kafkaProperties("retries"),
                                                                        linger= kafkaProperties("linger"),
                                                                        batchSize = kafkaProperties("batchSize"),
                                                                        kafkaTopic = kafkaProperties("topic"),
                                                                        message = concatTweetData(status, kafkaProperties("delimiter")))
    override def onDeletionNotice(statusDeletionNotice: StatusDeletionNotice): Unit = {}
    override def onTrackLimitationNotice(numberOfLimitedStatuses: Int): Unit = {}
    override def onScrubGeo(userId: Long, upToStatusId: Long): Unit = {}
    override def onStallWarning(warning: StallWarning): Unit = {}
    override def onException(ex: Exception): Unit = {}
  })
  // start to sample english tweets
  twitterStream.sample(twitterAPIProperties("language").toString)
}
