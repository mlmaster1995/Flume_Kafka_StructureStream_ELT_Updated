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
