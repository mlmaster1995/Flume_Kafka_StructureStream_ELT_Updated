## Kafka Spark Stream ETL Pipeline Collection OOP
![Hex.pm](https://img.shields.io/hexpm/l/plug?logo=Apache&logoColor=%23ff0000&style=flat-square)

### Table of Contents
* [About the Project](#about-the-project)
* [What's New](#what's-new)
* [Pipelines](#pipelines)  
* [Built With](#built-with)
* [Project Content](#project-content)
* [Structure Data Samples](#structure-data-samples)
* [Contact](#contact)

### About The Project
This project is updated from [Stream Data Pipeline with Flume Kafka StructureStream](https://github.com/mlmaster1995/Flume_Kafka_StructureStream_ELT) 
conducted in 2020, but this work is developed with the latest stable version of spark and kafka. The whole project is tested in the centOS7 VM configured with all required technology.
The whole project is developed as Object Oriented Project containing two real-time stream data sources and seven different sinks. User could overwrite the
user-define-function to reuse any ETL pipeline to acquire the real-time data.

### What's New
* This project is conducted with newer version of Kafka and Spark in Scala 2.12;
* Adds Twitter Stream source which could be ingested into Kafka producer with "sync", "async", "fire-and-forget" modes;
* Adds Avro Schema to the tweet stream source working with Confluent Schema Registry;  
* This project adds three more sinks including mySQL, HiveTable, MongoDB on top of previous version, so totally 7 data sinks;
* This is an Object-Oriented Project, so each pipeline could be reused by over-writing user-define-functions for any other soruces and new sinks;

### Pipelines
* Pipeline Structure:

![Big Data Flow Charts](https://user-images.githubusercontent.com/55723894/107840061-9ac6ee00-6d7d-11eb-932f-3b63d6522b5b.jpeg)

* Pipeline List:


    |    Sources   |                  Pipelines                   |                               Sinks                                 |
    | ------------ | -------------------------------------------- | ------------------------------------------------------------------- |
    |    vmstat    |   flume => kafka => spark structured stream  |   console, hdfs, hive metastore, hive table, kafka, mongoDB, mySQL  |
    | tweet stream |   kafka => spark structured stream           |   console, hdfs, hive metastore, hive table, kafka, mongoDB, mySQL  |
    | tweet stream |   kafka + Schema Registry                    |                  confluent kakaf-avro-consumer                      |

### Built With*
* [Scala 2.12.0](https://www.scala-lang.org/download/2.12.10.html)
* [Apache Spark 3.0.1](https://spark.apache.org/docs/2.1.1/)
* [Apache Flume 1.9.0](https://flume.apache.org/releases/1.5.2.html)
* [Apache Kafka 2.7.0](https://kafka.apache.org/0102/documentation.html)
* [Apache Hadoop 2.7.7](https://hadoop.apache.org/)
* [Confluent Schema Registry (Community Platform 6.1.0)](https://github.com/confluentinc/schema-registry)  
* [Twitter4j 4.0.7](http://twitter4j.org/en/index.html)
* [MongoDB 4.2](https://www.mongodb.com/)
* [MySQL 8.0.x](https://www.mysql.com/)

### Project Content

    ├── ApplicaitonJars                           # folder for all jars used for the project
    ├── kafka_flume_mysql_commands.txt            # all the commands for mysql, flume and kafka
    ├── KafkaSparkELT                             # Kafka Spark Data Pipeline Application Folder
    ├── TwitterStreamToKafka                      # Twitter Stream to Kafka Application Folder
    ├── runTwitterStreamToKafkaFatJars.sh         # A bash script file to submit the "TwitterStreamToKafka" application;
    ├── submit_spark_applicaiton.sh               # A bash script file to submit the "KafkaSparkELT" application; 

### Structure Data Samples
**NOTE**: Sensitive Data Is Hidden Or Modified In The Following Samples. 

* Pipeline: vmstat -> flume -> kafka -> spark structured streaming -> mySQL

 
    | row_id | topic | time                | r    | b    | swpd | free   | buff | cache   | si   | so   | bi   | bo   | in_val | cs   | us   | sy   | id   | wa   | st   |
    |--------|-------|---------------------|------|------|------|--------|------|---------|------|------|------|------|--------|------|------|------|------|------|------|
    |      1 | exec  | 2021-02-02 10:43:02 | 1    | 2    | 3    | 4      | 5    | 6       | 7    | 8    | 9    | 10   | 11     | 12   | 13   | 14   | 15   | 16   | 17   |
    |      2 | exec  | 2021-02-02 10:56:47 | 0    | 0    | 8    | 301620 | 1144 | 8950572 | 0    | 0    | 0    | 35   | 1706   | 1672 | 6    | 2    | 92   | 0    | 0    |
    |      3 | exec  | 2021-02-02 10:56:47 | 0    | 0    | 8    | 301176 | 1144 | 8950576 | 0    | 0    | 0    | 0    | 1469   | 1540 | 4    | 2    | 95   | 0    | 0    |
    |      4 | exec  | 2021-02-02 10:56:47 | 1    | 0    | 8    | 247564 | 1144 | 8950612 | 0    | 0    | 0    | 0    | 3564   | 3661 | 15   | 4    | 81   | 0    | 0    |
    |      5 | exec  | 2021-02-02 10:56:50 | 2    | 0    | 8    | 170608 | 1144 | 8919396 | 0    | 0    | 0    | 0    | 5363   | 4051 | 35   | 5    | 60   | 0    | 0    |
   

* Pipeline: tweet stream -> kafka -> spark structured streaming -> mySQL


    | row_id | tweet_time                   | user_id  | full_name           | tweet_id  | tweet_source        | is_truncated | is_rt | tweet_text                         |
    |--------|------------------------------|----------|---------------------|-----------|---------------------|--------------|-------|------------------------------------|
    |      1 | Fri Feb 12 20:04:55 EST 2021 |   ...    |      ...            |   ...     | Twitter for iPhone  | false        | false | just ordered ... 🥰 ...       ...  |
    |      2 | Fri Feb 12 20:04:55 EST 2021 |   ...    | chrisy 🌼@pptyaacy  |   ...     | Twitter for Android | false        | false | @bluexjjkyu okeyyy,           ...  |
    |      3 | Fri Feb 12 20:04:55 EST 2021 |   ...    |      ...            |   ...     | Twitter for iPhone  | false        | false | RT @uhprome: I really         ...  |
    |      4 | Fri Feb 12 20:04:55 EST 2021 |   ...    |      ...            |  ...      | Twitter for iPhone  | false        | false | RT @thesecret: Every          ...  |
    |      5 | Fri Feb 12 20:04:55 EST 2021 |   ...    |      ...            |   ...     | Twitter for iPhone  | false        | false | RT @ferbIatin: the            ...  |

* Pipeline: tweet stream -> kafka -> spark structred streaming -> mongoDB
  

      {
        "_id" : ObjectId("60271b6f6a142c2014fdc296"),
        "tweet_time" : "Fri Feb 12 19:20:53 EST 2021",
        "user_id" : "...",
        "full_name" : "...",
        "tweet_id" : "...",
        "tweet_source" : "Twitter for iPhone",
        "is_truncated" : "false",
        "is_rt" : "false",
        "tweet_text" : "First Time She Put Dat Pussy On Me I Put Her In A Benz 🤞🏽"
      }

* Pipeline: tweet stream -> kafka + Schema Registry -> Kafka Avro Consumer 
    
     
     {"tweetdate":"Sat Feb 20 19:23:25 EST 2021","userID":{"long":...},"fullName":{"string":"Aphrodi\uD83D\uD..."},"tweetID":{"long":...},"tweetSource":{"string":"Twitter for iPhone"},"isTruncated":{"boolean":false},"isRT":{"boolean":false},"tweet":{"string":"RT @deeptrusts: I want someo ..."}}
     
     {"tweetdate":"Sat Feb 20 19:23:25 EST 2021","userID":{"long":...},"fullName":{"string":"Ro ♒\uD83D\uDC96..."},"tweetID":{"long":...},"tweetSource":{"string":"Twitter for iPhone"},"isTruncated":{"boolean":false},"isRT":{"boolean":false},"tweet":{"string":"RT @feelxpain: i fucking fac ..."}}
     
     {"tweetdate":"Sat Feb 20 19:23:25 EST 2021","userID":{"long":...},"fullName":{"string":"nico._.macedo@ni..."},"tweetID":{"long":...},"tweetSource":{"string":"Twitter for Android"},"isTruncated":{"boolean":false},"isRT":{"boolean":false},"tweet":{"string":"@mukti_alin NFR lbinoBateon ..."}}



### Contact
* C. Young: kyang3@lakeheadu.ca
