package com.scrater.data.source

import com.scrater.data.Result
import com.scrater.vo.Tweet
import kotlinx.coroutines.flow.Flow

interface TweetsDataSource {
    fun fetchTweets(account: String): Flow<Result<List<Tweet>>>
}
