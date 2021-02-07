import ApplicationProperties.consoleProperties
import PipelineUtils._
import org.apache.spark.sql

object toConsole extends Serializable{

        // build a session
        val spark = getSparkSession

        // extract data
        val dataSource: sql.DataFrame = ELTComponents.extract(spark, extracFunc)

        // transform data
        val transformedSource: sql.DataFrame = ELTComponents.transform(spark, dataSource, transformFunc)

        // load data
        ELTComponents.Load.toConsole(transformedSource, mode = consoleProperties("mode"))


}

