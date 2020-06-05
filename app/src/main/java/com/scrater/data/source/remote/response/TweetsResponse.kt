package com.scrater.data.source.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class TweetsResponse(
    @field:Json(name = "has_more_items")
    val hasMoreItems: Boolean,
    @field:Json(name = "items_html")
    val htmlContent: String
)