package com.kafka.spark.oop.pipelines.source_kafka_spark_hiveMetaStore

import com.kafka.spark.oop.dev.ApplicationProperties.{hiveProperties, kafkaProperties}
import com.kafka.spark.oop.dev.ELTComponents
import com.kafka.spark.oop.dev.vmstatPipeUtils.{extractFunc, getSparkSession, transformFunc}
import org.apache.spark.sql

// write the stream to the metastore
object vmstatToHiveMetaStore extends Serializable {
  // build a spark session
  val spark = getSparkSession

  // extract data
  val dataSource: sql.DataFrame = ELTComponents.extract(spark,  kafkaProperties("topic_I"), extractFunc)

  // transform data
  val transformedSource: sql.DataFrame = ELTComponents.transform(spark, dataSource, transformFunc)

  // load data
  ELTComponents.Load.toHiveMetaStore(transformedSource, hiveProperties("warehousePath"), hiveProperties("checkpointPath"),
    hiveProperties("format"), hiveProperties("mode"))

}
