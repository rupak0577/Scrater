package com.scrater.data.source.remote

/*
 * Copyright 2018 Google LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.squareup.moshi.JsonEncodingException
import java.io.IOException

/**
 * Wrap a suspending API [call] in try/catch. In case an exception is thrown, a [ApiErrorResponse] is
 * created based on the [errorMessage].
 */
suspend fun <T : Any> safeApiCall(call: suspend () -> ApiResponse<T>, errorMessage: String): ApiResponse<T> {
    return try {
        call()
    } catch (e: JsonEncodingException) {
        ApiResponse.create(IOException("Unexpected malformed JSON"))
    } catch (e: Exception) {
        // An exception was thrown when calling the API so we're converting this to an IOException
        ApiResponse.create(IOException(errorMessage, e))
    }
}