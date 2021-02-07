package com.kafka.spark.pipeline.pipelines.source_kafka_spark_hiveMetaStore

import com.kafka.spark.pipeline.dev.ApplicationProperties.hiveProperties
import com.kafka.spark.pipeline.dev.ELTComponents
import com.kafka.spark.pipeline.dev.vmstatPipeUtils.{extractFunc, getSparkSession, transformFunc}
import org.apache.spark.sql

// write the stream to the metastore
object toHiveMetaStore extends Serializable {
  // build a spark session
  val spark = getSparkSession

  // extract data
  val dataSource: sql.DataFrame = ELTComponents.extract(spark, extractFunc)

  // transform data
  val transformedSource: sql.DataFrame = ELTComponents.transform(spark, dataSource, transformFunc)

  // load data
  ELTComponents.Load.toHiveMetaStore(transformedSource, hiveProperties("warehousePath"), hiveProperties("checkpointPath"),
    hiveProperties("format"), hiveProperties("mode"))

}
