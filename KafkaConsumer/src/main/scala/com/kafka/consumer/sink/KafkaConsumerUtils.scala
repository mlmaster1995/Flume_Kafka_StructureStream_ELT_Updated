package com.kafka.consumer.sink

import com.kafka.consumer.sink.ApplicationProperties.PropType
import java.util.Properties

object KafkaConsumerUtils extends Serializable{

  // set up consumer props
  def setConsumerProps(consumerProps:PropType):Properties ={
    val props = new Properties()
    consumerProps.foreach(prop=> props.put(prop._1, prop._2))
    props
  }


}
