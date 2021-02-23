name := "KafkaConsumer"

version := "0.1"

scalaVersion := "2.12.10"

// https://mvnrepository.com/artifact/org.apache.kafka/kafka
libraryDependencies += "org.apache.kafka" %% "kafka" % "2.7.0"

// https://mvnrepository.com/artifact/org.apache.avro/avro
libraryDependencies += "org.apache.avro" % "avro" % "1.8.2"

// https://mvnrepository.com/artifact/io.confluent/kafka-avro-serializer
libraryDependencies += "io.confluent" % "kafka-avro-serializer" % "5.3.0"

// Avro Schema Generation SBT
// Version must match that of `avro-compiler` in `project/plugins/sbt`
libraryDependencies += "org.apache.avro" % "avro" % "1.10.1"
avroStringType := "String"

// revolvers
resolvers += "confluent" at "https://packages.confluent.io/maven/"

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