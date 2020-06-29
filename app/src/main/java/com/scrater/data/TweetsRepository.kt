package com.scrater.data

import com.scrater.data.source.TweetsDataSource
import com.scrater.vo.Tweet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class TweetsRepository @Inject constructor(
    @Named("Local")
    private val tweetsLocalDataSource: TweetsDataSource,
    @Named("Remote")
    private val tweetsRemoteDataSource: TweetsDataSource,
    private val twitterRateLimiter: @JvmSuppressWildcards RateLimiter<String>
) : Repository {

    override fun loadTweets(account: String): Flow<Result<List<Tweet>>> = flow {
        when (val dbValueResult = tweetsLocalDataSource.fetchTweets(account)) {
            is Result.Success -> {
                if (shouldFetch(account, dbValueResult.data)) {
                    emit(Result.Loading(dbValueResult.data))

                    when (val remoteResult = tweetsRemoteDataSource.fetchTweets(account)) {
                        is Result.Success -> {
                            tweetsLocalDataSource.saveTweets(
                                account, remoteResult.data
                            )
                            emitAll(loadFromDb(account).map { Result.Success(it) })
                        }
                        is Result.Error -> {
                            twitterRateLimiter.reset(account)
                            emitAll(loadFromDb(account).map {
                                Result.Error(
                                    remoteResult.exception,
                                    it
                                )
                            })
                        }
                    }
                } else {
                    emitAll(loadFromDb(account).map { Result.Success(it) })
                }
            }
        }
    }

    private fun loadFromDb(account: String): Flow<List<Tweet>> {
        return tweetsLocalDataSource.fetchTweetsAsFlow(account)
    }

    private fun shouldFetch(account: String, dbValue: List<Tweet>?): Boolean {
        return dbValue.isNullOrEmpty() || twitterRateLimiter.shouldFetch(account)
    }
}