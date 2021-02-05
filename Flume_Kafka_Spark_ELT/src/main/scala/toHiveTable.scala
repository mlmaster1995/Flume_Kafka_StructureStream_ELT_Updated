import PipelineUtils.{extracFunc, getSparkSession, transformFunc}
import org.apache.spark.sql

object toHiveTable extends Serializable {
    // create a spark session
    val spark = getSparkSession

    // load data
    val dataSource: sql.DataFrame = ELTComponents.extract(spark, extracFunc)

    // transform data
    val transformedSource: sql.DataFrame = ELTComponents.transform(spark, dataSource, transformFunc)

    ELTComponents.Load.toHiveTable(transformedSource,"parquet", "append", "snappy", "chrisy", "fromstream", partition = 1)
}
