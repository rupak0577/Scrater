package com.scrater.data.source.local

import android.content.Context
import androidx.room.Room
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
    private lateinit var targetContext: Context
    private lateinit var tweetsDao: TweetsDao
    private lateinit var db: TweetsDatabase

    @Before
    fun createDb() {
        targetContext = InstrumentationRegistry.getInstrumentation().targetContext
        context = InstrumentationRegistry.getInstrumentation().context
        db = Room.inMemoryDatabaseBuilder(
            targetContext, TweetsDatabase::class.java
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
        var tweets = Scraper.parseHtml("elonmusk", parseTweetsResponse("response1").htmlContent)
        tweetsDao.insertTweets(tweets)

        var tweetsInDb = tweetsDao.loadTweets("elonmusk").take(1).toList()[0]
        assertThat(tweetsInDb.size).isEqualTo(21)
        assertThat(tweetsInDb[8].entries.videos).isEqualTo(1)
        assertThat(tweetsInDb[12].entries.photos.size).isEqualTo(4)

        tweets = Scraper.parseHtml("spacex", parseTweetsResponse("response2").htmlContent)
        tweetsDao.insertTweets(tweets)

        tweetsInDb = tweetsDao.loadTweets("spacex").take(1).toList()[0]
        assertThat(tweetsInDb.size).isEqualTo(20)
        assertThat(tweetsInDb[2].entries.videos).isEqualTo(1)
        assertThat(tweetsInDb[8].entries.photos.size).isEqualTo(4)
    }

    private fun parseTweetsResponse(fileName: String): TweetsResponse {
        val testInput: InputStream = context.assets.open("$fileName.json")
        val content = testInput.bufferedReader().use(BufferedReader::readText)
        val adapter = Moshi.Builder().build().adapter(TweetsResponse::class.java)
        return adapter.fromJson(content)!!
    }
}