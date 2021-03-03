package com.kafka.spark.oop.pipelineCollections

import com.kafka.spark.oop.pipelineDev.ELTComponents
import com.kafka.spark.oop.pipelineDev.ExtractClass.extractFromKafkaProducer
import com.kafka.spark.oop.pipelineDev.LoadClass.toConsole
import com.kafka.spark.oop.pipelineDev.TransformClass.transformVmstatStream
import com.kafka.spark.oop.pipelineDev.projectUtils.{PropType, getSparkSession}
import com.kafka.spark.oop.pipelineDev.vmstatPipeUtils.transformFunc
import org.apache.spark.sql
import org.apache.spark.sql.SparkSession

object PipelineCollect extends Serializable {

  trait BaseETLPipeline{
    val spark:SparkSession
    val source:sql.DataFrame
    val transformedSource:sql.DataFrame
    def load:Unit
  }

  // vmstat => flume => kafka producer => spark structured stream => console
  case class VmstatToConsolePipleline(configMap:PropType) extends BaseETLPipeline{
    // build a spark session
    val spark = getSparkSession(configMap)
    // extract data
    val source: sql.DataFrame = extractFromKafkaProducer(spark, configMap("kafka.brokers"), configMap("kafka.topic.vmstat"))
    // transform data
    val transformedSource: sql.DataFrame = transformVmstatStream(spark, source)
    // load data
    def load:Unit = toConsole(transformedSource, mode = configMap("console.write.mode"))
  }

//  // tweet stream => kafka producer => spark structured stream => console
//  case class TweetToConsolePipeline(configMap:PropType) extends BaseETLPipeline{
//    // create a spark session
//    val spark = getSparkSession(configMap)
//    // extract data
//    val source: sql.DataFrame = extractFromKafkaProducer(spark, configMap("kafka.brokers"), configMap("kafka.topic.tweet"))
//    // transform data
//    val transformedSource: sql.DataFrame = ELTComponents.transform(spark, source, transformFunc)
//    // load data
//    def load:Unit = ELTComponents.Load.toConsole(transformedSource, mode = configMap("console.write.mode"))
//  }



}
