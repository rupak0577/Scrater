package com.scrater.data.source.local

import android.content.Context
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import com.scrater.data.Result
import com.scrater.data.source.TweetsDataSource
import com.scrater.data.source.remote.Scraper
import com.scrater.data.source.remote.response.TweetsResponse
import com.scrater.vo.Tweeter
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream

@RunWith(AndroidJUnit4::class)
class TweetsLocalDataSourceTest {
    private lateinit var context: Context
    private lateinit var targetContext: Context
    private lateinit var tweetsDao: TweetsDao
    private lateinit var tweeterDao: TweeterDao
    private lateinit var db: TweetsDatabase
    private lateinit var tweetsLocalDataSource: TweetsDataSource

    @Before
    fun createDb() {
        targetContext = InstrumentationRegistry.getInstrumentation().targetContext
        context = InstrumentationRegistry.getInstrumentation().context
        db = Room.inMemoryDatabaseBuilder(
            targetContext, TweetsDatabase::class.java
        ).build()
        tweetsDao = db.tweetsDao()
        tweeterDao = db.tweeterDao()
        tweetsLocalDataSource = TweetsLocalDataSource(tweetsDao, tweeterDao)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun testDataSourceForTweets() {
        runBlocking {
            val account = "spacex"
            val tweets = Scraper.scrapeTweets(account, parseTweetsResponse("response2").htmlContent)
            tweetsLocalDataSource.saveTweets(account, tweets)

            val dbData = tweetsLocalDataSource.fetchTweets(account)
            val dbFlowData = tweetsLocalDataSource.fetchTweetsAsFlow(account).take(1).toList()[0]
            assertThat(dbData).isInstanceOf(Result.Success::class.java)
            assertThat((dbData as Result.Success).data).isEqualTo(tweets)
            assertThat(dbFlowData).isEqualTo(tweets)
        }
    }

    @Test
    fun testDataSourceForTweeters() {
        runBlocking {
            val username = "spacex"
            val tweeterData = parseProfileResponse(username, "profile_response1")
            tweetsLocalDataSource.saveTweeterData(username, tweeterData)

            val dbFlowData = tweetsLocalDataSource.fetchTweeterDataAsFlow(username).take(1).toList()[0]
            assertThat(dbFlowData).isEqualTo(tweeterData)
        }
    }

    private fun parseProfileResponse(username: String, fileName: String): Tweeter {
        val testInput: InputStream = context.assets.open("$fileName.html")
        return Scraper.scrapeProfile(
            username,
            testInput.bufferedReader().use(BufferedReader::readText)
        )
    }

    private fun parseTweetsResponse(fileName: String): TweetsResponse {
        val testInput: InputStream = context.assets.open("$fileName.json")
        val content = testInput.bufferedReader().use(BufferedReader::readText)
        val adapter = Moshi.Builder().build().adapter(TweetsResponse::class.java)
        return adapter.fromJson(content)!!
    }
}