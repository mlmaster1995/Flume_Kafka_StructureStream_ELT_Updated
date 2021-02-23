name := "Flume_Kafka_Spark_ELT"

version := "0.1"

scalaVersion := "2.12.10"

val sparkVersion = "3.0.1"

// https://mvnrepository.com/artifact/org.apache.spark/spark-sql
libraryDependencies += "org.apache.spark" %% "spark-sql" % "3.0.1"

// https://mvnrepository.com/artifact/org.apache.kafka/kafka-clients
libraryDependencies += "org.apache.kafka" % "kafka-clients" % "2.7.0"

// https://mvnrepository.com/artifact/org.apache.spark/spark-sql-kafka-0-10
libraryDependencies += "org.apache.spark" %% "spark-sql-kafka-0-10" % "3.0.1" % "provided"

// https://mvnrepository.com/artifact/org.apache.spark/spark-token-provider-kafka-0-10
libraryDependencies += "org.apache.spark" %% "spark-token-provider-kafka-0-10" % "3.0.1"

// https://mvnrepository.com/artifact/org.apache.commons/commons-pool2
libraryDependencies += "org.apache.commons" % "commons-pool2" % "2.6.2"

// https://mvnrepository.com/artifact/mysql/mysql-connector-java
libraryDependencies += "mysql" % "mysql-connector-java" % "8.0.23"

// https://mvnrepository.com/artifact/com.typesafe/config
libraryDependencies += "com.typesafe" % "config" % "1.4.1"

// https://mvnrepository.com/artifact/org.mongodb.spark/mongo-spark-connector
libraryDependencies += "org.mongodb.spark" %% "mongo-spark-connector" % "3.0.1"

artifactName := { (sv: ScalaVersion, module: ModuleID, artifact: Artifact) =>
  artifact.name + "_" + sv.binary + "-" + sparkVersion + "_" + module.revision + "." + artifact.extension
}