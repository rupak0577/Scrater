package com.scrater.data.source.remote

import com.scrater.data.Result
import com.scrater.data.source.TweetsDataSource
import com.scrater.data.source.remote.response.ErrorResponse
import com.scrater.network.Scraper
import com.scrater.network.TweetsResponse
import com.scrater.vo.Tweet
import com.squareup.moshi.JsonEncodingException
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TweetsRemoteDataSource(
    private val twitterService: TwitterService,
    private val moshi: Moshi
) : TweetsDataSource {

    override fun fetchTweets(username: String): Flow<Result<List<Tweet>>> = flow {
        emit(
            when (val response = request(username)) {
                is ApiEmptyResponse -> {
                    Result.Error(Exception("Empty response"))
                }
                is ApiSuccessResponse -> {
                    Result.Success(Scraper.parseHtml(response.body.htmlContent))
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
        )
    }

    private suspend fun request(username: String): ApiResponse<TweetsResponse> {
        return safeApiCall(
            call = {
                ApiResponse.create(twitterService.fetchTweets(username))
            },
            errorMessage = "Error during network call"
        )
    }
}