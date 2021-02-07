import ApplicationProperties.hiveProperties
import PipelineUtils.{extracFunc, getSparkSession, transformFunc}
import org.apache.spark.sql

// write the stream to the metastore
object toHiveMetaStore extends Serializable{
  // build a spark session
  val spark = getSparkSession

  // extract data
  val dataSource:sql.DataFrame = ELTComponents.extract(spark, extracFunc)

  // transform data
  val transformedSource:sql.DataFrame = ELTComponents.transform(spark, dataSource, transformFunc)

  // load data
  ELTComponents.Load.toHiveMetaStore(transformedSource, hiveProperties("warehousePath"), hiveProperties("checkpointPath"),
    hiveProperties("format"), hiveProperties("mode"))

}
