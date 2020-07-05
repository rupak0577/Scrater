package com.scrater.data.source.remote

import com.scrater.data.Result
import com.scrater.data.source.TweetsDataSource
import com.scrater.data.source.remote.response.ErrorResponse
import com.scrater.data.source.remote.response.TweetsResponse
import com.scrater.vo.Tweet
import com.scrater.vo.Tweeter
import com.squareup.moshi.JsonEncodingException
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import javax.inject.Inject

class TweetsRemoteDataSource @Inject constructor(
    private val twitterService: TwitterService,
    private val moshi: Moshi
) : TweetsDataSource {

    override fun fetchTweetsAsFlow(account: String): Flow<List<Tweet>> {
        throw NotImplementedError()
    }

    override suspend fun fetchTweets(account: String): Result<List<Tweet>> {
        return when (val response = requestTweets(account)) {
            is ApiEmptyResponse -> {
                Result.Error(Exception("Empty response"))
            }
            is ApiSuccessResponse -> {
                Result.Success(Scraper.scrapeTweets(account, response.body.htmlContent))
            }
            is ApiErrorResponse -> {
                val error = try {
                    moshi.adapter(ErrorResponse::class.java).fromJson(response.errorMessage)
                } catch (e: JsonEncodingException) {
                    null
                }
                Result.Error(Exception(error?.message ?: response.errorMessage))
            }
        }
    }

    override suspend fun fetchTweeterDataAsFlow(username: String): Flow<Tweeter> {
        throw NotImplementedError()
    }

    override suspend fun fetchTweeterData(username: String): Result<Tweeter> {
        return when (val response = requestProfile(username)) {
            is ApiEmptyResponse -> {
                Result.Error(Exception("Empty response"))
            }
            is ApiSuccessResponse -> {
                Result.Success(Scraper.scrapeProfile(username, response.body.string()))
            }
            is ApiErrorResponse -> {
                Result.Error(Exception(response.errorMessage))
            }
        }
    }

    override suspend fun saveTweets(account: String, tweets: List<Tweet>) {
        throw NotImplementedError()
    }

    override suspend fun saveTweeterData(username: String, data: Tweeter) {
        throw NotImplementedError()
    }

    private suspend fun requestTweets(account: String): ApiResponse<TweetsResponse> {
        return safeApiCall(
            call = {
                ApiResponse.create(twitterService.fetchTweets(account))
            },
            errorMessage = "Error during network call"
        )
    }

    private suspend fun requestProfile(username: String): ApiResponse<ResponseBody> {
        return safeApiCall(
            call = {
                ApiResponse.create(twitterService.fetchProfile(username))
            },
            errorMessage = "Error during network call"
        )
    }
}