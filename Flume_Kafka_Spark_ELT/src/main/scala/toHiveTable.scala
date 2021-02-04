import PipelineUtils.{extracFunc, getSparkSession, transformFunc}
import org.apache.spark.sql

object toHiveTable extends Serializable with App {
    val spark = getSparkSession
    val dataSource: sql.DataFrame = ELTComponents.extract(spark, extracFunc)
    val transformedSource: sql.DataFrame = ELTComponents.transform(spark, dataSource, transformFunc)

    ELTComponents.Load.toHiveTable(transformedSource,"parquet", "append", "snappy", "chrisy", "fromstream", partition = 1)
}
