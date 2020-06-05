package com.scrater.data.source.remote

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import java.io.BufferedReader

class MockServerDispatcher : Dispatcher() {
    val path = "/i/profiles/show/%s/timeline/tweets?include_available_features=1&include_entities=1&include_new_items_bar=true"

    override fun dispatch(request: RecordedRequest): MockResponse {
        when (request.path) {
            path.format("elonmusk") ->
                return MockResponse().setResponseCode(200)
                    .setBody(enqueueResponse("response1.json"))
            path.format("spacex") ->
                return MockResponse().setResponseCode(200)
                    .setBody(enqueueResponse("response2.json"))
            path.format("abc") ->
                return MockResponse().setResponseCode(404)
                    .setBody(enqueueResponse("error_response.json"))
        }
        return MockResponse().setResponseCode(200).setBody("some html string")
    }

    private fun enqueueResponse(
        filename: String
    ): String {
        val inputStream = javaClass.classLoader?.getResourceAsStream(filename)
        return inputStream?.bufferedReader()?.use(BufferedReader::readText)!!
    }
}