#*********************************************************************#
#                  Spark-submit Bash Scritpt				                  #
# - to use this bash script the user needs to redefine the root path  #
# - all the jar files are in the Jars folder in the git repo          #
# - the applicaiton jar file name could be varied and                 #
# "flume_kafka_spark_elt_2.12-3.0.1.jar" is from "sbt package"        #
#*********************************************************************#

#! /usr/bin/bash

clear

root_path= <user-define-root>

spark-submit \
--class $1 \
--packages org.mongodb.spark:mongo-spark-connector_2.12:3.0.1 \
--jars \
${root_path}/spark-sql-kafka-0-10_2.12-3.0.1.jar,\
${root_path}/spark-sql_2.12-3.0.1.jar,\
${root_path}/kafka-clients-2.7.0.jar,\
${root_path}/spark-token-provider-kafka-0-10_2.12-3.0.1.jar,\
${root_path}/commons-pool2-2.6.2.jar,\
${root_path}/mysql-connector-java-8.0.23.jar,\
${root_path}/mongo-spark-connector_2.12-3.0.1.jar \
<jar-file-names-from-sbt>

