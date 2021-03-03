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
import com.kafka.spark.oop.pipelineCollections.PipelineCollect.VmstatToConsolePipleline
import com.kafka.spark.oop.pipelineDev.projectUtils.{extractProps}

import java.security.InvalidParameterException

object EltPipeline extends Serializable {

  def main(args: Array[String]): Unit = {

   // extract all properties from the .properties file before spark session is created
   val configMap = extractProps(args(0))

    /*
     - elt pipeline: vmstat => flume => kafka producer => spark structured stream => console
     - run "start-vmstats-with-flume.sh" to start to source
     - run "start-spark-kafka-unit.sh" to start to sink
    */
   if(configMap("pipeline.source")==("vmstat") && configMap("pipeline.sink")==("console")) VmstatToConsolePipleline(configMap).load

   else throw new InvalidParameterException("invalid properties in the pipline selection")

  }
}
