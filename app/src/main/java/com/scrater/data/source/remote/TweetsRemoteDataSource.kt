package com.scrater.data.source.remote

import com.scrater.data.Result
import com.scrater.data.source.TweetsDataSource
import com.scrater.data.source.remote.response.ErrorResponse
import com.scrater.data.source.remote.response.TweetsResponse
import com.scrater.vo.Tweet
import com.squareup.moshi.JsonEncodingException
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.Flow

class TweetsRemoteDataSource(
    private val twitterService: TwitterService,
    private val moshi: Moshi
) : TweetsDataSource {

    override fun fetchTweetsAsFlow(account: String): Flow<List<Tweet>> {
        throw NotImplementedError()
    }

    override suspend fun fetchTweets(account: String): Result<List<Tweet>> {
        return when (val response = request(account)) {
            is ApiEmptyResponse -> {
                Result.Error(Exception("Empty response"))
            }
            is ApiSuccessResponse -> {
                Result.Success(Scraper.parseHtml(account, response.body.htmlContent))
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

    private suspend fun request(account: String): ApiResponse<TweetsResponse> {
        return safeApiCall(
            call = {
                ApiResponse.create(twitterService.fetchTweets(account))
            },
            errorMessage = "Error during network call"
        )
    }
}