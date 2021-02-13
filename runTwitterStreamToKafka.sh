#*********************************************************************#
#               Twitter Stream Submit Bash Scritpt		      #
# - to use this bash script the user needs to redefine the root path  #
# - all the jar files are in the Jars folder in the git repo          #
# - the applicaiton jar file name could be varied and                 #
#   <jar-file-names-from-sbt> is from "sbt package"                   #
#*********************************************************************#

#! /usr/bin/bash

clear

root_path= "<root path to the jars>"

scala -cp "${root_path}/twitter4j-core-4.0.7.jar:${root_path}/twitter4j-stream-4.0.7.jar:${root_path}/kafka_2.12-2.7.0.jar:${root_path}/kafka-clients-2.7.0.jar:${root_path}/slf4j-api-1.7.25.jar:${root_path}/slf4j-simple-1.7.25.jar" <jar-file-name-from-sbt>.jar

