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
package com.kafka.consumer.sink

import com.kafka.consumer.sink.ApplicationProperties.{kafkaAvroConsumerConfig, kafkaBasicConsumerConfig, kafkaConsumerMessageProps}
import com.kafka.consumer.sink.KafkaConsumerUtils.setConsumerProps
import org.apache.kafka.clients.consumer.{KafkaConsumer, OffsetAndMetadata, OffsetCommitCallback}
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.errors.WakeupException
import tweet.kafka.avro.Tweet

import java.time.Duration
import java.util

object KafkaConsoleConsumer extends Serializable with App{
  /*
   - flag for avro schema consumer
   - only tweet stream has avro schema, vmstat and covid19 data srouces have no avro schema
  */
  val withAvroSchema:Boolean= false
  val topic = kafkaConsumerMessageProps("tweetTopic")

  // getting consumer instance with proper config
  val consumer = if(withAvroSchema) new KafkaConsumer[String, Tweet](setConsumerProps(kafkaAvroConsumerConfig)) else new KafkaConsumer[String, String](setConsumerProps(kafkaBasicConsumerConfig))
  val consumerTopic = if(withAvroSchema) kafkaConsumerMessageProps("tweetAvroTopic") else topic

  // subscribe the topic
  consumer.subscribe(util.Arrays.asList(consumerTopic))

  // clean exit the consumer
  Runtime.getRuntime.addShutdownHook(
    new Thread(
      () => {
        println("\n!!!!!!!!!!!!!!!safely exit is called...!!!!!!!!!!!!!!!!!!")
        consumer.wakeup()
      }
    )
  )

  // set up a current offset map
  var currentOffset:util.Map[TopicPartition, OffsetAndMetadata] = new util.HashMap()

  // set up async call_back function -> print or "log" once exception is thrown
  val asyncCallBack = new OffsetCommitCallback {
    override def onComplete(offsets: util.Map[TopicPartition, OffsetAndMetadata], exception: Exception): Unit = if(exception!=null) println(s"failed commit offset info:${offsets}")
  }

  // polling messages
  try {
    while (true) {
      val consumerRecords = consumer.poll(Duration.ofMillis(5000))

      consumerRecords.forEach(record => {
        // process messages
        println(s"partitionId: ${record.partition} offset: ${record.offset} key: ${record.key} value: ${record.value}")

        // fill up the offset map
        val topicPartition:TopicPartition = new TopicPartition(record.topic(), record.partition())
        val nextOffset:Long = record.offset()+1L
        val offSetCommit= new OffsetAndMetadata(nextOffset)
        currentOffset.put(topicPartition, offSetCommit)

        // async commit current offset
        consumer.commitAsync(currentOffset, asyncCallBack)
      })
    }
  }
  catch{
    case e: WakeupException => e.printStackTrace()
    case e: Exception => e.printStackTrace()
  }
  finally {
    consumer.close()
  }
}
