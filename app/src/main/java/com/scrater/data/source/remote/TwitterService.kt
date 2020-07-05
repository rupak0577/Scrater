package com.scrater.data.source.remote

import com.scrater.data.source.remote.response.TweetsResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface TwitterService {
    @GET("i/profiles/show/{username}/timeline/tweets?include_available_features=1&include_entities=1&include_new_items_bar=true")
    suspend fun fetchTweets(@Path("username") username: String): Response<TweetsResponse>

    @GET("{username}")
    suspend fun fetchProfile(@Path("username") username: String): Response<ResponseBody>
}
