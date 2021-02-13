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

object TwitterStreamToKafkaProducer extends Serializable with App{
  // set up twitter api config
  val config = getTweetConfig(twitterAPIProperties("API_key"),twitterAPIProperties("API_secrete_key"),twitterAPIProperties("Access_token"),twitterAPIProperties("Access_token_secret"))
  // get twitterStream instance
  val twitterStream: TwitterStream = getTweetStream(config)
  // set up delimiter to concat all tweet info
  val deli = "&&&&"
  // set up the tweet status
  twitterStream.addListener(new StatusListener() {
    override def onStatus(status: Status): Unit =  writeToKafkaProducer(kafkaProperties("brokers"), kafkaProperties("topic"), concatTweetData(status, deli))
    override def onDeletionNotice(statusDeletionNotice: StatusDeletionNotice): Unit = {}
    override def onTrackLimitationNotice(numberOfLimitedStatuses: Int): Unit = {}
    override def onScrubGeo(userId: Long, upToStatusId: Long): Unit = {}
    override def onStallWarning(warning: StallWarning): Unit = {}
    override def onException(ex: Exception): Unit = {}
  })
  // start to sample english tweets
  twitterStream.sample(twitterAPIProperties("language"))
}
