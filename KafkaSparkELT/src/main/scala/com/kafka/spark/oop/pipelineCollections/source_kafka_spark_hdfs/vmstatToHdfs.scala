package com.kafka.spark.oop.pipelineCollections.source_kafka_spark_hdfs

import com.kafka.spark.oop.pipelineDev.ApplicationProperties.{hdfsProperties, kafkaProperties}
import com.kafka.spark.oop.pipelineDev.ELTComponents
import com.kafka.spark.oop.pipelineDev.vmstatPipeUtils.{extractFunc, getSparkSession, transformFunc}
import org.apache.spark.sql

// write the stream data to hdfs
object vmstatToHdfs extends Serializable {
  // build a spark session
  val spark = getSparkSession

  // extract data
  val dataSource: sql.DataFrame = ELTComponents.extract(spark, kafkaProperties("topic_I"), extractFunc)

  // transform data
  val transformedSource: sql.DataFrame = ELTComponents.transform(spark, dataSource, transformFunc)

  // load data
  ELTComponents.Load.toHDFS(transformedSource, hdfsProperties("hdfsPath"), hdfsProperties("checkpointPath"),
    hdfsProperties("format"), hdfsProperties("mode"), hdfsProperties("compressionType"))
}
