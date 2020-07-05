package com.scrater.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.scrater.vo.Tweeter
import kotlinx.coroutines.flow.Flow

@Dao
interface TweeterDao {
    @Query("SELECT * from tweeters WHERE username = :username")
    fun loadTweeter(username: String): Flow<Tweeter>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTweeter(tweeter: Tweeter)
}
