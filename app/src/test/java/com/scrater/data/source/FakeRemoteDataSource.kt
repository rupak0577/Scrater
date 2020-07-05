package com.scrater.data.source

import com.scrater.TestHelpers
import com.scrater.data.Result
import com.scrater.data.source.remote.Scraper
import com.scrater.vo.Tweet
import com.scrater.vo.Tweeter
import kotlinx.coroutines.flow.Flow

class FakeRemoteDataSource : TweetsDataSource {

    override fun fetchTweetsAsFlow(account: String): Flow<List<Tweet>> {
        throw NotImplementedError()
    }

    override suspend fun fetchTweets(account: String): Result<List<Tweet>> {
        return when (account) {
            "elonmusk" -> Result.Success(
                Scraper.scrapeTweets(
                    account,
                    TestHelpers.parseResponse1().htmlContent
                )
            )
            "spacex" -> Result.Success(
                Scraper.scrapeTweets(
                    account,
                    TestHelpers.parseResponse2().htmlContent
                )
            )
            else -> Result.Error(Exception("This user does not exist."))
        }
    }

    override suspend fun fetchTweeterDataAsFlow(username: String): Flow<Tweeter> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchTweeterData(username: String): Result<Tweeter> {
        TODO("Not yet implemented")
    }

    override suspend fun saveTweets(account: String, tweets: List<Tweet>) {
        throw NotImplementedError()
    }

    override suspend fun saveTweeterData(username: String, data: Tweeter) {
        TODO("Not yet implemented")
    }
}