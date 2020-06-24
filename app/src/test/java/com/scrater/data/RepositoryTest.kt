package com.scrater.data

import com.google.common.truth.Truth.assertThat
import com.scrater.data.source.FakeLocalDataSource
import com.scrater.data.source.FakeRemoteDataSource
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class RepositoryTest {
    private lateinit var tweetsRepository: Repository
    private lateinit var twitterRateLimiter: RateLimiter<String>

    @Before
    fun setup() {
        val tweetsLocalDataSource = FakeLocalDataSource()
        val tweetsRemoteDataSource = FakeRemoteDataSource()
        twitterRateLimiter = FakeRateLimiter()
        tweetsRepository = TweetsRepository(tweetsLocalDataSource, tweetsRemoteDataSource, twitterRateLimiter)
    }

    @Test
    fun `load Tweets for account`() = runBlocking {
        val results = tweetsRepository.loadTweets("elonmusk").take(2).toList()

        assertThat(results[0]).isInstanceOf(Result.Loading::class.java)
        assertThat((results[0] as Result.Loading).data).isEmpty()
        assertThat(results[1]).isInstanceOf(Result.Success::class.java)
        assertThat((results[1] as Result.Success).data.size).isEqualTo(21)
    }

    @Test
    fun `load Tweets from db when already fetched`() = runBlocking {
        var results = tweetsRepository.loadTweets("elonmusk").take(2).toList()

        assertThat(results[0]).isInstanceOf(Result.Loading::class.java)
        assertThat((results[0] as Result.Loading).data).isEmpty()
        assertThat(results[1]).isInstanceOf(Result.Success::class.java)
        assertThat((results[1] as Result.Success).data.size).isEqualTo(21)

        twitterRateLimiter.reset("elonmusk")
        results = tweetsRepository.loadTweets("elonmusk").take(2).toList()

        assertThat(results.size).isEqualTo(1)
        assertThat(results[0]).isInstanceOf(Result.Success::class.java)
        assertThat((results[0] as Result.Success).data).isNotEmpty()
    }

    @Test
    fun `show error for invalid account`() = runBlocking {
        val results = tweetsRepository.loadTweets("abc").take(2).toList()

        assertThat(results[0]).isInstanceOf(Result.Loading::class.java)
        assertThat((results[0] as Result.Loading).data).isEmpty()
        assertThat(results[1]).isInstanceOf(Result.Error::class.java)
        assertThat((results[1] as Result.Error).data).isEmpty()
        assertThat((results[1] as Result.Error).exception.message).isEqualTo("This user does not exist.")
    }
}