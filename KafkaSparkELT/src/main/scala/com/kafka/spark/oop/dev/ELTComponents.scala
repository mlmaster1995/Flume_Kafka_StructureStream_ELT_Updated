package com.kafka.spark.pipeline.dev

import com.kafka.spark.pipeline.dev.CustomSinkClasses.WriteToKafka
import com.mongodb.spark.sql.toMongoDataFrameWriterFunctions
import org.apache.spark.sql
import org.apache.spark.sql.{DataFrame, Row, SparkSession}

/*
- ELTComponents Class
- there are three permanent components in each pieline
- each compoent is built based on spark structured stream and accepts user-define-function
*/
object ELTComponents extends Serializable {
  /*
   - Extract
   - extract function is a user-defined function in PipelineUtils
  */
  def extract(session: SparkSession, topic:String, extractFunc: (SparkSession, String) => sql.DataFrame): sql.DataFrame = extractFunc(session, topic)

  /*
   - Transform
   - transform function is a user-defined function in PipelineUtils
  */
  def transform(session: SparkSession, source: sql.DataFrame, transformFunc: (sql.DataFrame, SparkSession) => sql.DataFrame): sql.DataFrame = transformFunc(source, session)

  /*
   - multi-end Load class
   - each function in load class has a specific destination to write the stream to
  */
  object Load {
    // print to console
    def toConsole(source: sql.DataFrame, mode: String): Unit = source.writeStream.format("console").outputMode(mode).start().awaitTermination()

    // write data stream to hdfs storage
    def toHdfs(source: sql.DataFrame, hdfsPath: String, checkpointPath: String, format: String, mode: String, compressionType: String): Unit =
      source.writeStream.format(format).outputMode(mode).option("compression", compressionType).option("path", hdfsPath).option("checkpointLocation", checkpointPath).start().awaitTermination()

    // write data stre0am to hive metastore
    def toHiveMetaStore(source: sql.DataFrame, hiveDataPath: String, checkpiontPath: String, format: String, mode: String): Unit =
      source.writeStream.format(format).outputMode(mode).option("path", hiveDataPath).option("checkpointLocation", checkpiontPath).start().awaitTermination()

    // write data to hiveTable
    def toHiveTable(source: sql.DataFrame, format: String, hiveTableMode: String, compression: String, database: String, table: String, partition: Int = 1): Unit =
      source.writeStream.foreachBatch({ (batchDF: DataFrame, batchId: Long) =>
        batchDF.coalesce(partition).write.format(format).mode(hiveTableMode).option("compression", compression).saveAsTable(s"$database.$table")
      }).outputMode("update").start().awaitTermination()

    // write data stream to kafka
    def toKafka(source: sql.DataFrame, topic: String, servers: String, extract_func: Row => String): Unit =
      source.writeStream.foreach(new WriteToKafka(topic, servers, extract_func)).start().awaitTermination()

    // write data stream to mySQL
    def toMysql(source: sql.DataFrame, url: String, db: String, table: String, user: String, password: String, driver: String, mode: String): Unit =
      source.writeStream.foreachBatch { (batchDF: DataFrame, batchId: Long) =>
        batchDF.write.format("jdbc").mode(mode).option("driver", driver).option("url", url).option("dbtable", s"$db.$table").option("user", user).option("password", password).save()
      }.outputMode("update").start().awaitTermination()

    // write data to mongoDB
    def toMongoD(source: sql.DataFrame): Unit =
      source.writeStream.foreachBatch({ (batchDF: DataFrame, batchId: Long) =>
        batchDF.write.mode("append").mongo()
      }).outputMode("update").start().awaitTermination()

    // write data to Hbase
    ???

  }

}
