package com.scrater.data.source.remote

import com.google.common.truth.Truth.assertThat
import com.scrater.data.Result
import com.squareup.moshi.Moshi
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class TweetsRemoteDataSourceTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var twitterService: TwitterService
    private lateinit var remoteDataSource: TweetsRemoteDataSource

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.dispatcher = MockServerDispatcher()
        mockWebServer.start()
        twitterService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(TwitterService::class.java)
        remoteDataSource = TweetsRemoteDataSource(twitterService, Moshi.Builder().build())
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `given account, when asked for tweets, return tweets for valid user`() = runBlocking {
        var tweetsResult = remoteDataSource.fetchTweets("elonmusk")
        assertThat(tweetsResult).isInstanceOf(Result.Success::class.java)
        assertThat((tweetsResult as Result.Success).data.size).isEqualTo(21)

        tweetsResult = remoteDataSource.fetchTweets("spacex")
        assertThat(tweetsResult).isInstanceOf(Result.Success::class.java)
        assertThat((tweetsResult as Result.Success).data.size).isEqualTo(20)
    }

    @Test
    fun `given non-existent account, when asked for tweets, return error`() = runBlocking {
        val tweetsResult = remoteDataSource.fetchTweets("abc")
        assertThat(tweetsResult).isInstanceOf(Result.Error::class.java)
        assertThat((tweetsResult as Result.Error).exception.message).isEqualTo("This user does not exist.")
    }

    @Test
    fun `given valid account, when asked for tweets, return unexpected response error`() = runBlocking {
        val tweetsResult = remoteDataSource.fetchTweets("xyz")
        assertThat(tweetsResult).isInstanceOf(Result.Error::class.java)
        assertThat((tweetsResult as Result.Error).exception.message).contains("Unexpected malformed JSON")
    }

    @Test
    fun `given valid username, when asked for profile, return profile data`() = runBlocking {
        val tweeterResult = remoteDataSource.fetchTweeterData("spacex")
        assertThat(tweeterResult).isInstanceOf(Result.Success::class.java)
        assertThat((tweeterResult as Result.Success).data.name).isEqualTo("SpaceX")
        assertThat((tweeterResult as Result.Success).data.website).isEqualTo("spacex.com")
    }
}