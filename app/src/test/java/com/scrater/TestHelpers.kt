package com.scrater

import com.scrater.network.TweetsResponse
import com.squareup.moshi.Moshi
import java.io.BufferedReader

class TestHelpers {
    companion object {
        private val responseAdapter by lazy {
            Moshi.Builder().build().adapter(TweetsResponse::class.java)
        }

        fun parseResponse(): TweetsResponse {
            val inputStream = javaClass.classLoader?.getResourceAsStream("response.json")
            val content = inputStream?.bufferedReader()?.use(BufferedReader::readText)
            return responseAdapter.fromJson(content)!!
        }
    }
}