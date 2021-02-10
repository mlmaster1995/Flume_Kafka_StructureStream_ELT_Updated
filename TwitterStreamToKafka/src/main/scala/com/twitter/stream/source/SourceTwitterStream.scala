package com.twitter.stream.source

import com.twitter.stream.source.ApplicationProperties.twitterAPIProperties
import twitter4j.{StallWarning, Status, StatusDeletionNotice, StatusListener, TwitterStream, TwitterStreamFactory}

import java.util.Date

object SourceTwitterStream extends Serializable with App{

  // create API connect configuration
  val config = new twitter4j.conf.ConfigurationBuilder()
    .setOAuthConsumerKey(s"${twitterAPIProperties("API_key")}")
    .setOAuthConsumerSecret(s"${twitterAPIProperties("API_secrete_key")}")
    .setOAuthAccessToken(s"${twitterAPIProperties("Access_token")}")
    .setOAuthAccessTokenSecret(s"${twitterAPIProperties("Access_token_secret")}")
    .build
  // get twitterStream instance
  val twitterStream: TwitterStream = new TwitterStreamFactory(config).getInstance()
  // define tweet status concat function
  def concatTweetData(tweetStatus: Status):String ={
    val tweetCreatedDate:Date = tweetStatus.getCreatedAt()
    val tweetID:Long = tweetStatus.getId()
    val tweetText:String = tweetStatus.getText()
    val tweetSource:String = {
      val ptrn = "<a href=\"([\\w\\.\\/\\:]*)\" rel=\"([\\w]*)\">([\\w ]*)</a>".r
      tweetStatus.getSource() match{case ptrn(c0,c1,c2) => s"${c2}"}
    }
    val tweetUserID:Long = tweetStatus.getUser.getId
    val tweetFullName:String = tweetStatus.getUser.getName + "@" + tweetStatus.getUser.getScreenName
    val tweetReplyScreenName:String = tweetStatus.getInReplyToScreenName
    val tweetStatusTruncated:Boolean = tweetStatus.isTruncated
    val tweetIsRT:Boolean = tweetStatus.isRetweeted
    val res = tweetCreatedDate +"|"+ tweetUserID + "|" + tweetFullName + "|" + tweetID + "|"  + tweetSource + "|" + tweetReplyScreenName + "|" + tweetStatusTruncated + "|" + tweetIsRT + "|" + tweetText
    res
  }
  // set up twitter stream
  twitterStream.addListener(new StatusListener() {
    override def onStatus(status: Status): Unit = println(concatTweetData(status))
    override def onDeletionNotice(statusDeletionNotice: StatusDeletionNotice): Unit = {}
    override def onTrackLimitationNotice(numberOfLimitedStatuses: Int): Unit = {}
    override def onScrubGeo(userId: Long, upToStatusId: Long): Unit = {}
    override def onStallWarning(warning: StallWarning): Unit = {}
    override def onException(ex: Exception): Unit = {}
  })
  // start to sample english tweets
  twitterStream.sample("en")
}
