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

import org.apache.kafka.clients.producer.{Callback, KafkaProducer, ProducerRecord, RecordMetadata}
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
    new twitter4j.conf.ConfigurationBuilder().setDebugEnabled(true).setOAuthConsumerKey(api_key).setOAuthConsumerSecret(api_secret_key)
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

  // kafka callback anonymous class
  val kafkaCallBack = new Callback {
    override def onCompletion(metadata: RecordMetadata, exception: Exception): Unit =
      if (exception != null) println(exception)
      else println("*******record published to [partition:" + metadata.partition + ",offset:" + metadata.offset + "]*******")
  }

  // set up kafka producer writer
  def writeToKafkaProducer (mode: String,
                            bootstrap:String,
                            ack:String,
                            retry:String,
                            linger:String,
                            batchSize:String,
                            kafkaTopic:String,
                            message:String) = {

    val properties = new Properties()
    properties.put("bootstrap.servers", bootstrap)
    properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    properties.put("acks", ack)
    properties.put("retries", retry)
    properties.put("linger.ms", linger)
    properties.put("batch.size", batchSize)
    val producer = new KafkaProducer[String, String](properties)

    if(mode == "forget-and-fire" && ack == "0") {
      // forget-and-fire mode
      try {
        producer.send(new ProducerRecord(kafkaTopic, "tweet"+util.Random.nextInt(3), message))
      }
      catch {
        case _: Throwable => println("fails to write to kafka producer...")
      }
      finally {
        producer.close()
        println("*******Tweets are sent*******")
      }
    }
    else if (mode == "sync" && ack == "1"){
      // sync mode
      try {
        val ack = producer.send(new ProducerRecord(kafkaTopic, "tweet"+util.Random.nextInt(3), message)).get()
        println("*******record published to [partition: " + ack.partition + ",offset: " + ack.offset + "]*******")
      }
      catch {
        case _: Throwable => println("fails to write to kafka producer...")
      }
      finally {
        producer.close()
        println("*******Tweets are sent*******")
      }
    }
    else if (mode == "async" && ack == "1") {
      // async mode
      try {
        producer.send(new ProducerRecord(kafkaTopic, "tweet"+ util.Random.nextInt(3), message), kafkaCallBack)
      }
      catch {
        case _: Throwable => println("fails to write to kafka producer...")
      }
      finally {
        producer.close()
        println("*******Tweets are sent*******")
      }
    }
    else
      println("!!!!!!!!no messages write to kafka!!!!!!!!")

  }
}





