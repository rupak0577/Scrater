package com.scrater.data.source.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import com.scrater.data.source.remote.Scraper
import com.scrater.data.source.remote.response.TweetsResponse
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
class TweetsDaoTest {
    private lateinit var context: Context
    private lateinit var appContext: Context
    private lateinit var tweetsDao: TweetsDao
    private lateinit var db: TweetsDatabase

    @Before
    fun createDb() {
        appContext = ApplicationProvider.getApplicationContext()
        context = InstrumentationRegistry.getInstrumentation().context
        db = Room.inMemoryDatabaseBuilder(
            appContext, TweetsDatabase::class.java
        ).build()
        tweetsDao = db.tweetsDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertAndLoadTweets() = runBlocking {
        var account = "elonmusk"
        var tweets = Scraper.scrapeTweets(account, parseTweetsResponse("response1").htmlContent)
        tweetsDao.insertTweets(tweets)

        var tweetsInDb = tweetsDao.loadTweets(account).take(1).toList()[0]
        assertThat(tweetsInDb).isEqualTo(tweets)

        account = "spacex"
        tweets = Scraper.scrapeTweets(account, parseTweetsResponse("response2").htmlContent)
        tweetsDao.insertTweets(tweets)

        tweetsInDb = tweetsDao.loadTweets(account).take(1).toList()[0]
        assertThat(tweetsInDb).isEqualTo(tweets)
    }

    private fun parseTweetsResponse(fileName: String): TweetsResponse {
        val testInput: InputStream = context.assets.open("$fileName.json")
        val content = testInput.bufferedReader().use(BufferedReader::readText)
        val adapter = Moshi.Builder().build().adapter(TweetsResponse::class.java)
        return adapter.fromJson(content)!!
    }
}