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
package com.kafka.spark.oop.pipelineCollections

import com.kafka.spark.oop.pipelineDev.ELTComponents
import com.kafka.spark.oop.pipelineDev.ExtractClass.extractFromKafkaProducer
import com.kafka.spark.oop.pipelineDev.LoadClass.toConsole
import com.kafka.spark.oop.pipelineDev.TransformClass.{transformCovid19BatchData, transformTweetStream, transformVmstatStream}
import com.kafka.spark.oop.pipelineDev.projectUtils.{PropType, getSparkSession}
import org.apache.spark.sql
import org.apache.spark.sql.SparkSession

object PipelineCollect extends Serializable {
  // root pipeline for all pipelines
  trait BaseETLPipeline{
    val spark:SparkSession
    val source:sql.DataFrame
    val transformedSource:sql.DataFrame
    def load:Unit
  }
  // vmstat => flume => kafka producer => spark structured stream => console
  case class VmstatToConsolePipleline(configMap:PropType) extends BaseETLPipeline{
    val spark = getSparkSession(configMap)
    val source: sql.DataFrame = extractFromKafkaProducer(spark, configMap("kafka.brokers"), configMap("kafka.topic.vmstat"))
    val transformedSource: sql.DataFrame = transformVmstatStream(spark, source)
    def load:Unit = toConsole(transformedSource, mode = configMap("console.write.mode"))
  }
  // tweet stream => kafka producer => spark structured stream => console
  case class TweetToConsolePipeline(configMap:PropType) extends BaseETLPipeline{
    val spark = getSparkSession(configMap)
    val source: sql.DataFrame = extractFromKafkaProducer(spark, configMap("kafka.brokers"), configMap("kafka.topic.tweet"))
    val transformedSource: sql.DataFrame = transformTweetStream(spark, source)
    def load:Unit = ELTComponents.Load.toConsole(transformedSource, mode = configMap("console.write.mode"))
  }
  // covid19_batch_data => kafka producer => spark structured stream => console
  case class Covid19ToConsolePipeline(configMap:PropType) extends BaseETLPipeline{
    val spark = getSparkSession(configMap)
    val source: sql.DataFrame = extractFromKafkaProducer(spark, configMap("kafka.brokers"), configMap("kafka.topic.covid19"))
    val transformedSource: sql.DataFrame = transformCovid19BatchData(spark, source)
    def load:Unit = ELTComponents.Load.toConsole(transformedSource, mode = configMap("console.write.mode"))
  }



}
