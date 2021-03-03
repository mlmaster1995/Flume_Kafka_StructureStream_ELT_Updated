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
package com.kafka.spark.oop.pipelineDev

import com.kafka.spark.oop.pipelineDev.CustomSinkClasses.WriteToBasicKafkaProducer
import com.mongodb.spark.sql.toMongoDataFrameWriterFunctions
import org.apache.spark.sql
import org.apache.spark.sql.{DataFrame, Row}

object LoadClass extends Serializable {
  // print to console
  def toConsole(source: sql.DataFrame, mode: String): Unit = source.writeStream.format("console").outputMode(mode).start().awaitTermination()

  // write data stream to hdfs storage
  def toHDFS(source: sql.DataFrame, hdfsPath: String, checkpointPath: String, format: String, mode: String, compressionType: String): Unit =
    source.writeStream.format(format).outputMode(mode).option("compression", compressionType).option("path", hdfsPath).option("checkpointLocation", checkpointPath).start().awaitTermination()

  // write data to hiveTable
  def toHiveTable(source: sql.DataFrame, format: String, hiveTableMode: String, compression: String, database: String, table: String, partition: Int = 1): Unit =
    source.writeStream.foreachBatch({ (batchDF: DataFrame, batchId: Long) =>
      batchDF.coalesce(partition).write.format(format).mode(hiveTableMode).option("compression", compression).saveAsTable(s"$database.$table")
    }).outputMode("update").start().awaitTermination()

  // write data stream to kafka
  def toKafka(source: sql.DataFrame, topic: String, servers: String, extract_func: Row => String): Unit =
    source.writeStream.foreach(new WriteToBasicKafkaProducer(topic, servers, extract_func)).start().awaitTermination()

  // write data stream to mySQL
  def toMysql(source: sql.DataFrame, url: String, db: String, table: String, user: String, password: String, driver: String, mode: String): Unit =
    source.writeStream.foreachBatch { (batchDF: DataFrame, batchId: Long) =>
      batchDF.write.format("jdbc").mode(mode).option("driver", driver).option("url", url).option("dbtable", s"$db.$table").option("user", user).option("password", password).save()
    }.outputMode("update").start().awaitTermination()

  // write data to mongoDB
  def toMongoDB(source: sql.DataFrame): Unit =
    source.writeStream.foreachBatch({ (batchDF: DataFrame, batchId: Long) =>
      batchDF.write.mode("append").mongo()
    }).outputMode("update").start().awaitTermination()

}
