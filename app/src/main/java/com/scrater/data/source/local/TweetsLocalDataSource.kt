package com.scrater.data.source.local

import com.scrater.data.Result
import com.scrater.data.source.TweetsDataSource
import com.scrater.vo.Tweet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class TweetsLocalDataSource @Inject constructor(private val tweetsDao: TweetsDao) : TweetsDataSource {

    override fun fetchTweetsAsFlow(account: String): Flow<List<Tweet>> {
        return tweetsDao.loadTweets(account)
    }

    override suspend fun fetchTweets(account: String): Result<List<Tweet>> {
        return Result.Success(tweetsDao.loadTweets(account).first())
    }

    override suspend fun saveTweets(account: String, tweets: List<Tweet>) {
        tweetsDao.insertTweets(tweets)
    }
}