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

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

import scala.io.Source

object projectUtils extends Serializable {
  // property data type
  type PropType = Map[String, String]

  // extract values from .properties file
  def extractProps(filePath:String):PropType=
    Source.fromFile(filePath).getLines().filter(row => ! (row.startsWith("#")) && row.length>0).map(x=>(x.split("=")(0)->x.split("=")(1))).toMap

  // get spark session
  def getSparkSession(configMap:PropType): SparkSession= {
    Logger.getLogger("org").setLevel(Level.OFF)
    Logger.getLogger("akka").setLevel(Level.OFF)
    SparkSession
      .builder()
      .appName(configMap("sparkSession.app.name"))
      .config("spark.mongodb.input.uri", configMap("mongo.InputURI"))
      .config("spark.mongodb.output.uri", configMap("mongo.OutputURI"))
      .master(configMap("sparkSession.mode"))
      .enableHiveSupport()
      .getOrCreate()
  }
}
