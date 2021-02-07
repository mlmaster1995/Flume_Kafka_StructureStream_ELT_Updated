import PipelineUtils.{extracFunc, extractRowDataForKafkaWriter, getSparkSession, transformFunc}
import ApplicationProperties.{kafkaProperties}
import org.apache.spark.sql

// write data stream back to kafka with a different topic
object toKafka extends Serializable{
  // build a spark session
  val spark = getSparkSession

  // extract data
  val dataSource:sql.DataFrame = ELTComponents.extract(spark, extracFunc)

  // transform data
  val transformedSource:sql.DataFrame = ELTComponents.transform(spark, dataSource, transformFunc)

  // load data
  ELTComponents.Load.toKafka(transformedSource, kafkaProperties("topic_II"), kafkaProperties("brokers"), extract_func = extractRowDataForKafkaWriter)
}
