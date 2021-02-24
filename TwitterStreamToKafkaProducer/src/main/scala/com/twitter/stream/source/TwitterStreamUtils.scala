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
import com.twitter.stream.source.ApplicationProperties.PropType
import org.apache.kafka.clients.producer.{Callback, KafkaProducer, ProducerRecord, RecordMetadata}
import tweet.kafka.avro.Tweet
import twitter4j.{Status, TwitterStream, TwitterStreamFactory}
import twitter4j.conf.Configuration

import java.security.InvalidParameterException
import java.util.{Date, Properties}
import scala.collection.immutable.Queue

object TwitterStreamUtils extends Serializable{

  // set up producer props
  def setProducerProps(consumerProps:PropType):Properties ={
    val props = new Properties()
    consumerProps.foreach(prop=> props.put(prop._1, prop._2))
    props
  }

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

  // extract tweet data
  def extractTweetData(tweetStatus:Status):Map[String, Any]={
    val tweetCreatedDate:Date = tweetStatus.getCreatedAt()
    val tweetID:Long = tweetStatus.getId()
    val tweetText:String = tweetStatus.getText()
    val tweetSource:String = {val ptrn = "<a href=\"([\\w\\.\\/\\:]*)\" rel=\"([\\w]*)\">([\\w ]*)</a>".r;tweetStatus.getSource() match{case ptrn(c0,c1,c2) => s"${c2}"}}
    val tweetUserID:Long = tweetStatus.getUser.getId
    val tweetFullName:String = tweetStatus.getUser.getName + "@" + tweetStatus.getUser.getScreenName
    val tweetStatusTruncated:Boolean = tweetStatus.isTruncated
    val tweetIsRT:Boolean = tweetStatus.isRetweeted
    Map(
      "tweetCreatedDate" -> tweetCreatedDate,
      "tweetID" -> tweetID,
      "tweetText" -> tweetText,
      "tweetSource" -> tweetSource,
      "tweetUserID" -> tweetUserID,
      "tweetFullName" -> tweetFullName,
      "tweetStatusTruncated" -> tweetStatusTruncated,
      "tweetIsRT" -> tweetIsRT
    )
  }

  // concat tweet data with specified delimiter
  // "tweetdate"<delimiter>"userID"<delimiter>"fullName"<delimiter>"tweetID"<delimiter>"tweetSource"<delimiter>"isTruncated"<delimiter>"isRT"<delimiter>"tweet"
  def concatTweetData(tweetStatus: Status, concatDelimiter: String):String ={
    val tweetData = extractTweetData(tweetStatus)
    concatWithDelimiter(concatDelimiter, Queue(tweetData("tweetCreatedDate"), tweetData("tweetUserID"), tweetData("tweetFullName"), tweetData("tweetID"), tweetData("tweetSource"),
      tweetData("tweetStatusTruncated"), tweetData("tweetIsRT"), tweetData("tweetText")))
  }

  // fill the tweet data into the Tweet Schema defined in "tweetSchema.avsc"
  def putTweetDataIntoSchema(tweetStatus:Status):Tweet={
    val tweetData = extractTweetData(tweetStatus)
    new Tweet(s"${tweetData("tweetCreatedDate")}", tweetData("tweetUserID").asInstanceOf[Long], tweetData("tweetFullName").toString, tweetData("tweetID").asInstanceOf[Long],
      tweetData("tweetSource").toString, tweetData("tweetStatusTruncated").asInstanceOf[Boolean], tweetData("tweetIsRT").asInstanceOf[Boolean], tweetData("tweetText").toString)
  }

  // kafka callback anonymous class
  val kafkaCallBack = new Callback {
    override def onCompletion(metadata: RecordMetadata, exception: Exception): Unit =
      if (exception != null) println(exception)
      else println("*******record published to [partition:" + metadata.partition + ",offset:" + metadata.offset + "]*******")
  }

  // set up kafka producer writer with the schema in the Avro format
  def writeToAvroKafkaProducer(producerProps:PropType, mode: String, topic:String, message:Tweet):Unit = {
    // check consistency between mode and acks
    if(mode=="forget-and-fire" && producerProps("acks")!="0") throw new InvalidParameterException("forget-and-fire mode must be with acks at 0")
    else if(mode=="sync" && producerProps("acks")!="1") throw new InvalidParameterException("sync mode must be with acks at 1")
    else if(mode=="async" && producerProps("acks")!="1") throw new InvalidParameterException("async mode must be with acks at 1")

    // set up producer config
    val properties = setProducerProps(producerProps)

    // get producer instance
    val producer = new KafkaProducer[String, Tweet](properties)

    // send messages to brokers
    if(mode == "forget-and-fire") {
      // forget-and-fire mode
      try {
        producer.send(new ProducerRecord(topic, topic+util.Random.nextInt(100), message))
      }
      catch {
        case _: Throwable => println("fails to write to kafka producer...")
      }
      finally {
        producer.close()
        println("*******Tweets are sent*******")
      }
    }
    else if (mode == "sync"){
      // sync mode
      try {
        val ack = producer.send(new ProducerRecord(topic, topic+util.Random.nextInt(100), message)).get()
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
    else if (mode == "async") {
      // async mode
      try {
        producer.send(new ProducerRecord(topic, topic+ util.Random.nextInt(100), message), kafkaCallBack)
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

  // set up kafka producer writer without the schema in the plain string format
  def writeToKafkaProducer (producerProps:PropType, mode: String, topic:String, message:String):Unit = {
    // check consistency between mode and acks
    if(mode=="forget-and-fire" && producerProps("acks")!="0") throw new InvalidParameterException("forget-and-fire mode must be with acks at 0")
    else if(mode=="sync" && producerProps("acks")!="1") throw new InvalidParameterException("sync mode must be with acks at 1")
    else if(mode=="async" && producerProps("acks")!="1") throw new InvalidParameterException("async mode must be with acks at 1")

    // set up producer config
    val properties = setProducerProps(producerProps)

    // get producer instance
    val producer = new KafkaProducer[String, String](properties)

    // send message to brokers
    if (mode == "forget-and-fire") {
      // forget-and-fire mode
      try {
        producer.send(new ProducerRecord(topic, topic+util.Random.nextInt(100), message))
      }
      catch {
        case _: Throwable => println("fails to write to kafka producer...")
      }
      finally {
        producer.close()
        println("*******Tweets are sent*******")
      }
    }
    else if (mode == "sync"){
      // sync mode
      try {
        val ack = producer.send(new ProducerRecord(topic, topic + util.Random.nextInt(100), message)).get()
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
    else if (mode == "async") {
      // async mode
      try {
        producer.send(new ProducerRecord(topic, topic + util.Random.nextInt(100), message), kafkaCallBack)
      }
      catch {
        case _: Throwable => println("fails to write to kafka producer...")
      }
      finally {
        producer.close()
        println("*******Tweets are sent*******")
      }
    }
    else println("!!!!!!!!no messages write to kafka!!!!!!!!")
  }


}





