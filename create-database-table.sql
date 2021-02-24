/* create a database */
CREATE DATABASE IF NOT EXISTS chrisy;

/* create a table for vmstate data source */
DROP TABLE IF EXITS chrisy.fromStream;
CREATE TABLE IF NOT EXISTS chrisy.fromStream (
    row_id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    topic VARCHAR(10), time TIMESTAMP,
    r VARCHAR(10),
    b VARCHAR(10),
    swpd VARCHAR(10),
    free VARCHAR(10),
    buff VARCHAR(10),
    cache VARCHAR(10),
    si VARCHAR(10),
    so VARCHAR(10),
    bi VARCHAR(10),
    bo VARCHAR(10),
    in_val VARCHAR(10),
    cs VARCHAR(10),
    us VARCHAR(10),
    sy VARCHAR(10),
    id VARCHAR(10),
    wa VARCHAR(10),
    st VARCHAR(10)
    );

/*create database and table for twitter stream source*/
DROP TABLE IF EXISTS chrisy.fromTweet;
CREATE TABLE IF NOT EXISTS chrisy.fromTweet (
    row_id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    tweet_time VARCHAR(50),
    user_id VARCHAR(50),
    full_name VARCHAR(100),
    tweet_id VARCHAR(50),
    tweet_source VARCHAR(50),
    is_truncated VARCHAR(5),
    is_rt VARCHAR(5),
    tweet_text TEXT);
