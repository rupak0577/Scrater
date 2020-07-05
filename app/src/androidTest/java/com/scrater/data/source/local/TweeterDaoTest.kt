package com.scrater.data.source.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import com.scrater.data.source.remote.Scraper
import com.scrater.vo.Tweeter
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
class TweeterDaoTest {
    private lateinit var context: Context
    private lateinit var appContext: Context
    private lateinit var tweeterDao: TweeterDao
    private lateinit var db: TweetsDatabase

    @Before
    fun createDb() {
        appContext = ApplicationProvider.getApplicationContext()
        context = InstrumentationRegistry.getInstrumentation().context
        db = Room.inMemoryDatabaseBuilder(
            appContext, TweetsDatabase::class.java
        ).build()
        tweeterDao = db.tweeterDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertAndLoadTweeter() = runBlocking {
        var username = "spacex"
        var tweeterData = parseProfileResponse(username, "profile_response1")
        tweeterDao.insertTweeter(tweeterData)

        var dbData = tweeterDao.loadTweeter(username).take(1).toList()[0]
        assertThat(dbData).isEqualTo(tweeterData)

        username = "kotlin"
        tweeterData = parseProfileResponse(username, "profile_response2")
        tweeterDao.insertTweeter(tweeterData)

        dbData = tweeterDao.loadTweeter(username).take(1).toList()[0]
        assertThat(dbData).isEqualTo(tweeterData)
    }

    private fun parseProfileResponse(username: String, fileName: String): Tweeter {
        val testInput: InputStream = context.assets.open("$fileName.html")
        return Scraper.scrapeProfile(
            username,
            testInput.bufferedReader().use(BufferedReader::readText)
        )
    }
}