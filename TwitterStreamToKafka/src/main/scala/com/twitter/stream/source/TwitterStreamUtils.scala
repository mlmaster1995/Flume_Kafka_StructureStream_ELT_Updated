

package com.twitter.stream.source

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}


import twitter4j.{Status, TwitterStream, TwitterStreamFactory}
import twitter4j.conf.Configuration

import java.util.{Date, Properties}
import scala.collection.immutable.Queue


object TwitterStreamUtils extends Serializable{
  // concat strings with certain delimiter
  def concatWithDelimiter(del:String, stringQ:Queue[Any]):String = {
    if(stringQ.isEmpty) "END"
    else stringQ.dequeue._1 + del + concatWithDelimiter(del, stringQ.dequeue._2)
  }

  // get tweet config
  def getTweetConfig(api_key:String, api_secret_key:String, access_token:String, access_token_secret:String):Configuration =
    new twitter4j.conf.ConfigurationBuilder().setOAuthConsumerKey(api_key).setOAuthConsumerSecret(api_secret_key)
      .setOAuthAccessToken(access_token).setOAuthAccessTokenSecret(access_token_secret).build

  // get tweet stream instance
  def getTweetStream(config:Configuration):TwitterStream = new TwitterStreamFactory(config).getInstance()

  // extract tweet status and concat with specified delimiter
  // "tweetdate"<delimiter>"userID"<delimiter>"fullName"<delimiter>"tweetID"<delimiter>"tweetSource"<delimiter>"isTruncated"<delimiter>"isRT"<delimiter>"tweet"
  def concatTweetData(tweetStatus: Status, concatDelimiter: String):String ={
    val tweetCreatedDate:Date = tweetStatus.getCreatedAt()
    val tweetID:Long = tweetStatus.getId()
    val tweetText:String = tweetStatus.getText()
    val tweetSource:String = {val ptrn = "<a href=\"([\\w\\.\\/\\:]*)\" rel=\"([\\w]*)\">([\\w ]*)</a>".r;tweetStatus.getSource() match{case ptrn(c0,c1,c2) => s"${c2}"}}
    val tweetUserID:Long = tweetStatus.getUser.getId
    val tweetFullName:String = tweetStatus.getUser.getName + "@" + tweetStatus.getUser.getScreenName
    val tweetStatusTruncated:Boolean = tweetStatus.isTruncated
    val tweetIsRT:Boolean = tweetStatus.isRetweeted
    concatWithDelimiter(concatDelimiter, Queue(tweetCreatedDate, tweetUserID, tweetFullName.trim,tweetID,tweetSource.trim,tweetStatusTruncated, tweetIsRT,tweetText.trim))
  }

  // set up kafka producer writer
  def writeToKafkaProducer (bootstrapServers:String, kafkaTopic:String, message:String) = {
    val properties = new Properties()
    properties.put("bootstrap.servers", bootstrapServers)
    properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    properties.put("linger.ms", "3000")
    properties.put("batch.size", "500")
    val producer = new KafkaProducer[String, String](properties)
    producer.send(new ProducerRecord(kafkaTopic, message))
  }

}





