import PipelineUtils.{extracFunc, getSparkSession, transformFunc}
import com.mongodb.ReadPreference.primaryPreferred
import org.apache.spark.sql

object toMongoDB extends Serializable with App{

    // build a session
    val spark = getSparkSession

    // extract data
    val dataSource: sql.DataFrame = ELTComponents.extract(spark, extracFunc)

    // transform data
    val transformedSource: sql.DataFrame = ELTComponents.transform(spark, dataSource, transformFunc)

    // load data
    ELTComponents.Load.toMongoD(transformedSource)

}

