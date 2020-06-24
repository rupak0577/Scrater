package com.scrater.data.source

import com.scrater.data.Result
import com.scrater.vo.Tweet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeLocalDataSource : TweetsDataSource {
    private val localTweets = mutableMapOf<String, List<Tweet>>()

    override fun fetchTweetsAsFlow(account: String): Flow<List<Tweet>> {
        return flowOf(
            when (account) {
                "elonmusk" -> localTweets.getOrDefault(account, emptyList())
                "spacex" -> localTweets.getOrDefault(account, emptyList())
                else -> emptyList()
            }
        )
    }

    override suspend fun fetchTweets(account: String): Result<List<Tweet>> {
        return when (account) {
            "elonmusk" -> Result.Success(localTweets.getOrDefault(account, emptyList()))
            "spacex" -> Result.Success(localTweets.getOrDefault(account, emptyList()))
            else -> Result.Success(emptyList())
        }
    }

    override suspend fun saveTweets(account: String, tweets: List<Tweet>) {
        localTweets[account] = tweets
    }
}