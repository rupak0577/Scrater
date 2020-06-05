package com.scrater.data.source.remote.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ErrorResponse(val message: String)