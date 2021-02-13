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

package com.kafka.spark.oop.pipelineCollections.source_kafka_spark_hiveTable

import com.kafka.spark.oop.pipelineDev.ApplicationProperties.{hiveProperties, kafkaProperties}
import com.kafka.spark.oop.pipelineDev.ELTComponents
import com.kafka.spark.oop.pipelineDev.twitterPipeUtils.{extractFunc, getSparkSession, transformFunc}
import org.apache.spark.sql

object TweetToHiveTable extends Serializable {
  // create a spark session
  val spark = getSparkSession

  // load data
  val dataSource: sql.DataFrame = ELTComponents.extract(spark, kafkaProperties("topic_III"), extractFunc)

  // transform data
  val transformedSource: sql.DataFrame = ELTComponents.transform(spark, dataSource, transformFunc)

  ELTComponents.Load.toHiveTable(transformedSource, hiveProperties("format"), hiveProperties("mode"),
    hiveProperties("compressionType"), hiveProperties("database"), hiveProperties("table_tweet"), hiveProperties("partitions").toInt)
}
