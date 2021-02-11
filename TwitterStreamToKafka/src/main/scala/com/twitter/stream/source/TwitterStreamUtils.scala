package com.twitter.stream.source

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import twitter4j.{Status, TwitterStream, TwitterStreamFactory}
import twitter4j.conf.Configuration

import java.util.{Date, Properties}


object TwitterStreamUtils extends Serializable{

  // get tweet config
  def getTweetConfig(api_key:String, api_secret_key:String, access_token:String, access_token_secret:String):Configuration =
    new twitter4j.conf.ConfigurationBuilder().setOAuthConsumerKey(api_key).setOAuthConsumerSecret(api_secret_key)
      .setOAuthAccessToken(access_token).setOAuthAccessTokenSecret(access_token_secret).build

  // get tweet stream instance
  def getTweetStream(config:Configuration):TwitterStream = new TwitterStreamFactory(config).getInstance()

  // define tweet status concat function
  def concatTweetData(tweetStatus: Status):String ={
    val tweetCreatedDate:Date = tweetStatus.getCreatedAt()
    val tweetID:Long = tweetStatus.getId()
    val tweetText:String = tweetStatus.getText()
    val tweetSource:String = {
      val ptrn = "<a href=\"([\\w\\.\\/\\:]*)\" rel=\"([\\w]*)\">([\\w ]*)</a>".r
      tweetStatus.getSource() match{case ptrn(c0,c1,c2) => s"${c2}"}
    }
    val tweetUserID:Long = tweetStatus.getUser.getId
    val tweetFullName:String = tweetStatus.getUser.getName + "@" + tweetStatus.getUser.getScreenName
    val tweetReplyScreenName:String = tweetStatus.getInReplyToScreenName
    val tweetStatusTruncated:Boolean = tweetStatus.isTruncated
    val tweetIsRT:Boolean = tweetStatus.isRetweeted
    val res = tweetCreatedDate +"|"+ tweetUserID + "|" + tweetFullName.trim + "|" + tweetID + "|"  + tweetSource.trim +
      "|" + tweetReplyScreenName.trim + "|" + tweetStatusTruncated + "|" + tweetIsRT + "|" + tweetText.trim
    res
  }

  // set up kafka producer writer
  def writeToKafkaProducer (bootstrapServers:String, kafkaTopic:String, message:String) = {
    val properties = new Properties()
    properties.put("bootstrap.servers", bootstrapServers)
    properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    val producer = new KafkaProducer[String, String](properties)
    producer.send(new ProducerRecord(kafkaTopic, message))
  }
}





