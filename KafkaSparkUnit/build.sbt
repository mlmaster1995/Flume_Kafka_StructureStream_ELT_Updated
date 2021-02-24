
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

// merge strategy SBT assemble
assemblyMergeStrategy in assembly := {
  case PathList("jackson-annotations-2.10.5.jar", xs @ _*) => MergeStrategy.last
  case PathList("jackson-core-2.10.5.jar", xs @ _*) => MergeStrategy.last
  case PathList("jackson-databind-2.10.5.1.jar", xs @ _*) => MergeStrategy.last
  case PathList("jackson-dataformat-csv-2.10.5.jar", xs @ _*) => MergeStrategy.last
  case PathList("jackson-datatype-jdk8-2.10.5.jar", xs @ _*) => MergeStrategy.last
  case PathList("jackson-module-paranamer-2.10.5.jar", xs @ _*) => MergeStrategy.last

  case PathList("netty-buffer-4.1.48.Final.jar", xs @ _*) => MergeStrategy.last
  case PathList("netty-codec-4.1.48.Final.jar", xs @ _*) => MergeStrategy.last
  case PathList("netty-common-4.1.48.Final.jar", xs @ _*) => MergeStrategy.last
  case PathList("netty-handler-4.1.48.Final.jar", xs @ _*) => MergeStrategy.last
  case PathList("netty-resolver-4.1.48.Final.jar", xs @ _*) => MergeStrategy.last
  case PathList("netty-transport-native-epoll-4.1.48.Final.jar", xs @ _*) => MergeStrategy.last
  case PathList("netty-transport-native-unix-common-4.1.48.Final.jar", xs @ _*) => MergeStrategy.last
  case PathList("netty-transport-4.1.48.Final.jar", xs @ _*) => MergeStrategy.last

  case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
  case _ => MergeStrategy.first
}