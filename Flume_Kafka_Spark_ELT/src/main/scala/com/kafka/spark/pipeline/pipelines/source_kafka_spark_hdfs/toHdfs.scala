package com.kafka.spark.pipeline.pipelines.source_kafka_spark_hdfs

import com.kafka.spark.pipeline.dev.ApplicationProperties.hdfsProperties
import com.kafka.spark.pipeline.dev.ELTComponents
import com.kafka.spark.pipeline.dev.PipelineUtils.{extracFunc, getSparkSession, transformFunc}
import org.apache.spark.sql

// write the stream data to hdfs
object toHdfs extends Serializable {
  // build a spark session
  val spark = getSparkSession

  // extract data
  val dataSource: sql.DataFrame = ELTComponents.extract(spark, extracFunc)

  // transform data
  val transformedSource: sql.DataFrame = ELTComponents.transform(spark, dataSource, transformFunc)

  // load data
  ELTComponents.Load.toHdfs(transformedSource, hdfsProperties("hdfsPath"), hdfsProperties("checkpointPath"),
    hdfsProperties("format"), hdfsProperties("mode"), hdfsProperties("compressionType"))
}
