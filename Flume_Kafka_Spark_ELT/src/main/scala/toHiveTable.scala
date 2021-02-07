import ApplicationProperties.hiveProperties
import PipelineUtils.{extracFunc, getSparkSession, transformFunc}
import org.apache.spark.sql

object toHiveTable extends Serializable {
    // create a spark session
    val spark = getSparkSession

    // load data
    val dataSource: sql.DataFrame = ELTComponents.extract(spark, extracFunc)

    // transform data
    val transformedSource: sql.DataFrame = ELTComponents.transform(spark, dataSource, transformFunc)

    ELTComponents.Load.toHiveTable(transformedSource, hiveProperties("format"), hiveProperties("mode"),
        hiveProperties("compressionType"), hiveProperties("database"), hiveProperties("table"), hiveProperties("partitions").toInt)
}
