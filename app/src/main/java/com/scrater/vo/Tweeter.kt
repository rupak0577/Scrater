package com.scrater.vo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tweeters")
data class Tweeter(
    @PrimaryKey
    val username: String,
    val name: String,
    val bio: String,
    val website: String,
    val avatar: String,
    val banner: String?
)