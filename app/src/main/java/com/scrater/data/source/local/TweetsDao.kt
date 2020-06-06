package com.scrater.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.scrater.vo.Tweet
import kotlinx.coroutines.flow.Flow

@Dao
interface TweetsDao {
    @Query("SELECT * from tweets WHERE account = :account")
    fun loadTweets(account: String): Flow<List<Tweet>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTweets(tweets: List<Tweet>)
}
