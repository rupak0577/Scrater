package com.scrater.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.scrater.vo.Tweet
import com.scrater.vo.Tweeter

@Database(entities = [Tweet::class, Tweeter::class], version = 1)
@TypeConverters(StringListTypeConverter::class)
abstract class TweetsDatabase : RoomDatabase() {
    abstract fun tweetsDao(): TweetsDao
    abstract fun tweeterDao(): TweeterDao
}
