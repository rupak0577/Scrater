package com.scrater.data

import com.scrater.vo.Tweet
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun loadTweets(account: String): Flow<Result<List<Tweet>>>
}