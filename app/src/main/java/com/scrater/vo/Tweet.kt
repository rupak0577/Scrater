package com.scrater.vo

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tweets")
data class Tweet(
    val account: String,
    val position: Int,

    @PrimaryKey
    @ColumnInfo(name = "tweet_id")
    val tweetId: String,
    @ColumnInfo(name = "tweet_url")
    val tweetUrl: String,
    val username: String,
    val avatar: String,
    val time: String,
    @ColumnInfo(name = "html_text")
    val htmlText: String,
    val replies: String,
    val retweets: String,
    val likes: String,
    @ColumnInfo(name = "is_retweet")
    val isRetweet: Boolean,
    @ColumnInfo(name = "is_pinned")
    val isPinned: Boolean,
    @Embedded
    val entries: Entries
)

data class Entries(
    val photos: List<String>,
    val videos: Int
)
