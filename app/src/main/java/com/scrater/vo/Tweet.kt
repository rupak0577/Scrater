package com.scrater.vo

data class Tweet(
    val tweetId: String,
    val tweetUrl: String,
    val username: String,
    val time: Long,
    val htmlText: String,
    val replies: String,
    val retweets: String,
    val likes: String,
    val isRetweet: Boolean,
    val isPinned: Boolean,
    val entries: Entries
)

data class Entries(
    val photos: List<String>,
    val videos: Int
)
