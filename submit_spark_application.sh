#*********************************************************************#
#                  ELT pipeline submit Bash Script  		      #
# - to use this bash script the user needs to redefine the root path  #
# - all the jar files are in the Jars folder in the git repo          #
# - the applicaiton jar file name could be varied and                 #
#    <jar-file-name-from-sbt> is from "sbt package"		      #
# - $1 is the argument for the class name in the spark application    #
#*********************************************************************#

#! /usr/bin/bash

clear

root_path= "<user define root path for jar files>"

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
<jar-file-names-from-sbt>.jar

