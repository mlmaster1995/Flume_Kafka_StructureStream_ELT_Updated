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

import com.kafka.spark.oop.pipelineDev.ExtractClass.extractFromKafkaProducer
import com.kafka.spark.oop.pipelineDev.LoadClass.{toConsole, toHDFS, toHiveTable, toKafka, toMongoDB, toMysql}
import com.kafka.spark.oop.pipelineDev.TransformClass.{transformCovid19BatchData, transformTweetStream, transformTweetStreamForKafkaWriter, transformVmstatForKafkaWriter, transformVmstatStream}
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

  // sink to console
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
    def load:Unit = toConsole(transformedSource, mode = configMap("console.write.mode"))
  }
  // covid19_batch_data => kafka producer => spark structured stream => console
  case class Covid19ToConsolePipeline(configMap:PropType) extends BaseETLPipeline{
    val spark = getSparkSession(configMap)
    val source: sql.DataFrame = extractFromKafkaProducer(spark, configMap("kafka.brokers"), configMap("kafka.topic.covid19"))
    val transformedSource: sql.DataFrame = transformCovid19BatchData(spark, source)
    def load:Unit = toConsole(transformedSource, mode = configMap("console.write.mode"))
  }

  // sink to HDFS
  // vmstat => flume => kafka producer => spark structured stream => hdfs
  case class VmstatToHDFSPipleline(configMap:PropType) extends BaseETLPipeline{
    val spark = getSparkSession(configMap)
    val source: sql.DataFrame = extractFromKafkaProducer(spark, configMap("kafka.brokers"), configMap("kafka.topic.vmstat"))
    val transformedSource: sql.DataFrame = transformVmstatStream(spark, source)
    def load:Unit = toHDFS(transformedSource, configMap("hdfs.path.vmstat"), configMap("hdfs.checkpoint.path.vmstat"), configMap("hdfs.write.format"), configMap("hdfs.write.mode"),
      configMap("hdfs.compression.type"))
  }
  // tweet stream => kafka producer => spark structured stream => hdfs
  case class TweetToHDFSPipeline(configMap:PropType) extends BaseETLPipeline{
    val spark = getSparkSession(configMap)
    val source: sql.DataFrame = extractFromKafkaProducer(spark, configMap("kafka.brokers"), configMap("kafka.topic.tweet"))
    val transformedSource: sql.DataFrame = transformTweetStream(spark, source)
    def load:Unit = toHDFS(transformedSource, configMap("hdfs.path.tweet"), configMap("hdfs.checkpoint.path.tweet"), configMap("hdfs.write.format"), configMap("hdfs.write.mode"),
      configMap("hdfs.compression.type"))
  }
  // covid19_batch_data => kafka producer => spark structured stream => console
  case class Covid19ToHDFSPipeline(configMap:PropType) extends BaseETLPipeline{
    val spark = getSparkSession(configMap)
    val source: sql.DataFrame = extractFromKafkaProducer(spark, configMap("kafka.brokers"), configMap("kafka.topic.covid19"))
    val transformedSource: sql.DataFrame = transformCovid19BatchData(spark, source)
    def load:Unit = toHDFS(transformedSource, configMap("hdfs.path.covid19"), configMap("hdfs.checkpoint.path.covid19"), configMap("hdfs.write.format"), configMap("hdfs.write.mode"),
      configMap("hdfs.compression.type"))
  }


  // sink to HiveTable
  // vmstat => flume => kafka producer => spark structured stream => hiveTable
  case class VmstatToHiveTablePipleline(configMap:PropType) extends BaseETLPipeline{
    val spark = getSparkSession(configMap)
    val source: sql.DataFrame = extractFromKafkaProducer(spark, configMap("kafka.brokers"), configMap("kafka.topic.vmstat"))
    val transformedSource: sql.DataFrame = transformVmstatStream(spark, source)
    def load:Unit =toHiveTable(transformedSource, configMap("hive.write.format"), configMap("hive.write.mode"), configMap("hive.compression.type"), configMap("hive.metastore.database"),
      configMap("hive.metastore.table.vmstat"), configMap("hive.partitions.number").toInt)
  }
  // tweet stream => kafka producer => spark structured stream => hiveTable
  case class TweetToHiveTablePipeline(configMap:PropType) extends BaseETLPipeline{
    val spark = getSparkSession(configMap)
    val source: sql.DataFrame = extractFromKafkaProducer(spark, configMap("kafka.brokers"), configMap("kafka.topic.tweet"))
    val transformedSource: sql.DataFrame = transformTweetStream(spark, source)
    def load:Unit =toHiveTable(transformedSource, configMap("hive.write.format"), configMap("hive.write.mode"), configMap("hive.compression.type"), configMap("hive.metastore.database"),
      configMap("hive.metastore.table.tweet"), configMap("hive.partitions.number").toInt)
  }


  // sink to kafka producer
  // vmstat => flume => kafka producer => spark structured stream => kafka producer
  case class VmstatToKafkaPipleline(configMap:PropType) extends BaseETLPipeline{
    val spark = getSparkSession(configMap)
    val source: sql.DataFrame = extractFromKafkaProducer(spark, configMap("kafka.brokers"), configMap("kafka.topic.vmstat"))
    val transformedSource: sql.DataFrame = transformVmstatStream(spark, source)
    def load:Unit = toKafka(transformedSource, configMap("kafka.topic.kafkaProducer"), configMap("kafka.brokers"), extract_func = transformVmstatForKafkaWriter)
  }
  // tweet stream => kafka producer => spark structured stream => kafka producer
  case class TweetToKafkaPipeline(configMap:PropType) extends BaseETLPipeline{
    val spark = getSparkSession(configMap)
    val source: sql.DataFrame = extractFromKafkaProducer(spark, configMap("kafka.brokers"), configMap("kafka.topic.tweet"))
    val transformedSource: sql.DataFrame = transformTweetStream(spark, source)
    def load:Unit = toKafka(transformedSource, configMap("kafka.topic.kafkaProducer"), configMap("kafka.brokers"), extract_func = transformTweetStreamForKafkaWriter)
  }

  // sink to mysql
  // vmstat => flume => kafka producer => spark structured stream => mysql
  case class VmstatToMySQLPipleline(configMap:PropType) extends BaseETLPipeline{
    val spark = getSparkSession(configMap)
    val source: sql.DataFrame = extractFromKafkaProducer(spark, configMap("kafka.brokers"), configMap("kafka.topic.vmstat"))
    val transformedSource: sql.DataFrame = transformVmstatStream(spark, source)
    def load:Unit = toMysql(transformedSource, configMap("mysql.url"), configMap("mysql.database"), configMap("mysql.table.vmstat"),
      configMap("mysql.username"), configMap("mysql.password"), configMap("mysql.driver"), configMap("mysql.write.mode"))
  }
  // tweet stream => kafka producer => spark structured stream => mysql
  case class TweetToMySQLPipeline(configMap:PropType) extends BaseETLPipeline{
    val spark = getSparkSession(configMap)
    val source: sql.DataFrame = extractFromKafkaProducer(spark, configMap("kafka.brokers"), configMap("kafka.topic.tweet"))
    val transformedSource: sql.DataFrame = transformTweetStream(spark, source)
    def load:Unit = toMysql(transformedSource, configMap("mysql.url"), configMap("mysql.database"), configMap("mysql.table.tweet"),
      configMap("mysql.username"), configMap("mysql.password"), configMap("mysql.driver"), configMap("mysql.write.mode"))
  }

  // sink to mongoDB
  // vmstat => flume => kafka producer => spark structured stream => mongoDB
  case class VmstatToMongoDBPipleline(configMap:PropType) extends BaseETLPipeline{
    val spark = getSparkSession(configMap)
    val source: sql.DataFrame = extractFromKafkaProducer(spark, configMap("kafka.brokers"), configMap("kafka.topic.vmstat"))
    val transformedSource: sql.DataFrame = transformVmstatStream(spark, source)
    def load:Unit = toMongoDB(transformedSource)
  }
  // tweet stream => kafka producer => spark structured stream => mongoDB
  case class TweetToMongoDBPipeline(configMap:PropType) extends BaseETLPipeline{
    val spark = getSparkSession(configMap)
    val source: sql.DataFrame = extractFromKafkaProducer(spark, configMap("kafka.brokers"), configMap("kafka.topic.tweet"))
    val transformedSource: sql.DataFrame = transformTweetStream(spark, source)
    def load:Unit = toMongoDB(transformedSource)
  }




}
