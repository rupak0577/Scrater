package com.scrater.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.scrater.vo.Tweet

@Database(entities = [Tweet::class], version = 1)
@TypeConverters(StringListTypeConverter::class)
abstract class TweetsDatabase : RoomDatabase() {
    abstract fun tweetsDao(): TweetsDao
}
