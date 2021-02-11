package com.twitter.stream.source

import com.twitter.stream.source.ApplicationProperties.{kafkaProperties, twitterAPIProperties}
import com.twitter.stream.source.TwitterStreamUtils.{concatTweetData, getTweetConfig, getTweetStream, writeToKafkaProducer}
import twitter4j.{StallWarning, Status, StatusDeletionNotice, StatusListener, TwitterStream, TwitterStreamFactory}

object TwitterStreamToKafkaProducer extends Serializable with App{
  // set up twitter api config
  val config = getTweetConfig(twitterAPIProperties("API_key"),twitterAPIProperties("API_secrete_key"),twitterAPIProperties("Access_token"),twitterAPIProperties("Access_token_secret"))
  // get twitterStream instance
  val twitterStream: TwitterStream = getTweetStream(config)
  // set up the tweet status
  twitterStream.addListener(new StatusListener() {
    override def onStatus(status: Status): Unit = writeToKafkaProducer(kafkaProperties("brokers"), kafkaProperties("topic"), concatTweetData(status))
    override def onDeletionNotice(statusDeletionNotice: StatusDeletionNotice): Unit = {}
    override def onTrackLimitationNotice(numberOfLimitedStatuses: Int): Unit = {}
    override def onScrubGeo(userId: Long, upToStatusId: Long): Unit = {}
    override def onStallWarning(warning: StallWarning): Unit = {}
    override def onException(ex: Exception): Unit = {}
  })
  // start to sample english tweets
  twitterStream.sample(twitterAPIProperties("language"))
}
