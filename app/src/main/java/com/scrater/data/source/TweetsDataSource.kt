package com.scrater.data.source

import com.scrater.data.Result
import com.scrater.vo.Tweet
import com.scrater.vo.Tweeter
import kotlinx.coroutines.flow.Flow

interface TweetsDataSource {
    fun fetchTweetsAsFlow(account: String): Flow<List<Tweet>>

    suspend fun fetchTweets(account: String): Result<List<Tweet>>

    suspend fun fetchTweeterDataAsFlow(username: String): Flow<Tweeter>

    suspend fun fetchTweeterData(username: String): Result<Tweeter>

    suspend fun saveTweets(account: String, tweets: List<Tweet>)

    suspend fun saveTweeterData(username: String, data: Tweeter)
}
