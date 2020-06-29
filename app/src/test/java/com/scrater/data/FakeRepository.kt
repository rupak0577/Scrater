package com.scrater.data

import com.scrater.TestHelpers.Companion.scrapeResponse1
import com.scrater.TestHelpers.Companion.scrapeResponse2
import com.scrater.vo.Tweet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeRepository : Repository {
    private val tweetFlow = MutableStateFlow<Result<List<Tweet>>>(Result.Loading(null))

    override fun loadTweets(account: String): StateFlow<Result<List<Tweet>>> {
        return tweetFlow
    }

    fun mockTweetsLoading() {
        tweetFlow.value = Result.Loading(emptyList())
    }

    fun mockTweetsLoad(account: String) {
        tweetFlow.value =
            when (account) {
                "abc" -> Result.Success(scrapeResponse1("abc"))
                "def" -> Result.Success(scrapeResponse2("def"))
                else -> Result.Error(Exception("This user does not exist."))
            }
    }

    fun mockTweetsError() {
        tweetFlow.value = Result.Error(Exception("This user does not exist."))
    }
}
