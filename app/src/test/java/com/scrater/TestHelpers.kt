package com.scrater

import com.scrater.data.source.remote.Scraper
import com.scrater.data.source.remote.response.TweetsResponse
import com.scrater.vo.Tweet
import com.squareup.moshi.Moshi
import java.io.BufferedReader

class TestHelpers {
    companion object {
        private val responseAdapter by lazy {
            Moshi.Builder().build().adapter(TweetsResponse::class.java)
        }

        fun parseResponse1(): TweetsResponse {
            val inputStream = javaClass.classLoader?.getResourceAsStream("response1.json")
            val content = inputStream?.bufferedReader()?.use(BufferedReader::readText)
            return responseAdapter.fromJson(content)!!
        }

        fun parseResponse2(): TweetsResponse {
            val inputStream = javaClass.classLoader?.getResourceAsStream("response2.json")
            val content = inputStream?.bufferedReader()?.use(BufferedReader::readText)
            return responseAdapter.fromJson(content)!!
        }

        fun readProfileResponse(file: Int): String {
            val inputStream = when (file) {
                1 -> javaClass.classLoader?.getResourceAsStream("profile_response1.html")
                2 -> javaClass.classLoader?.getResourceAsStream("profile_response2.html")
                else -> null
            }
            return inputStream?.bufferedReader()?.use(BufferedReader::readText)!!
        }

        fun scrapeResponse1(account: String): List<Tweet> {
            return Scraper.scrapeTweets(account, parseResponse1().htmlContent)
        }

        fun scrapeResponse2(account: String): List<Tweet> {
            return Scraper.scrapeTweets(account, parseResponse2().htmlContent)
        }
    }
}