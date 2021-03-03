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
import com.kafka.spark.oop.pipelineCollections.PipelineCollect.{Covid19ToConsolePipeline, TweetToConsolePipeline, VmstatToConsolePipleline}
import com.kafka.spark.oop.pipelineDev.projectUtils.extractProps

import java.security.InvalidParameterException

object EltPipeline extends Serializable {

  def main(args: Array[String]): Unit = {

   // extract all properties from the .properties file before spark session is created
   val configMap = extractProps(args(0))

    /*
     - elt pipeline: vmstat => flume => kafka producer => spark structured stream => console
     - run "start-vmstats-with-flume.sh" to start to source
    */
    if(configMap("pipeline.source")==("vmstat") && configMap("pipeline.sink")==("console")) VmstatToConsolePipleline(configMap).load
    /*
     - elt pipeline: tweet_stream => flume => kafka producer => spark structured stream => console
     - run "start-tweetStream-to-kafkaProducer.sh" to start to source
    */
    else if(configMap("pipeline.source")==("tweet") && configMap("pipeline.sink")==("console")) TweetToConsolePipeline(configMap).load
    /*
     - elt pipeline: covid_batch_data => kafka producer => spark structured stream => console
     - run DAG "covid19_data_pipeline.py " in Apache Airflow to start to source
    */
    else if(configMap("pipeline.source")==("covid") && configMap("pipeline.sink")==("console")) Covid19ToConsolePipeline(configMap).load
    else throw new InvalidParameterException("invalid properties in the pipline selection")

  }
}
