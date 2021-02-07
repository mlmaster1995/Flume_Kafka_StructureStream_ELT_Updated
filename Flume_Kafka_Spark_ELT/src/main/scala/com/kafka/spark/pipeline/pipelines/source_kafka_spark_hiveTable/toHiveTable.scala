package com.kafka.spark.pipeline.pipelines.source_kafka_spark_hiveTable

import com.kafka.spark.pipeline.dev.ApplicationProperties.hiveProperties
import com.kafka.spark.pipeline.dev.ELTComponents
import com.kafka.spark.pipeline.dev.vmstatPipeUtils.{extractFunc, getSparkSession, transformFunc}
import org.apache.spark.sql

object toHiveTable extends Serializable {
  // create a spark session
  val spark = getSparkSession

  // load data
  val dataSource: sql.DataFrame = ELTComponents.extract(spark, extractFunc)

  // transform data
  val transformedSource: sql.DataFrame = ELTComponents.transform(spark, dataSource, transformFunc)

  ELTComponents.Load.toHiveTable(transformedSource, hiveProperties("format"), hiveProperties("mode"),
    hiveProperties("compressionType"), hiveProperties("database"), hiveProperties("table"), hiveProperties("partitions").toInt)
}
