package com.scrater.di

import android.content.Context
import androidx.room.Room
import com.scrater.data.TwitterRateLimiter
import com.scrater.data.source.local.TweeterDao
import com.scrater.data.source.local.TweetsDao
import com.scrater.data.source.local.TweetsDatabase
import com.scrater.data.source.remote.TwitterService
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class AppModule {
    @Provides
    @Singleton
    fun provideTwitterService(): TwitterService {
        return Retrofit.Builder()
            .baseUrl("https://twitter.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(TwitterService::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(context: Context): TweetsDatabase {
        return Room.databaseBuilder(context, TweetsDatabase::class.java, "TweetsDb").build()
    }

    @Provides
    fun provideTweetDao(db: TweetsDatabase): TweetsDao {
        return db.tweetsDao()
    }

    @Provides
    fun provideTweeterDao(db: TweetsDatabase): TweeterDao {
        return db.tweeterDao()
    }

    @Provides
    @Singleton
    fun provideRateLimiter(): TwitterRateLimiter {
        return TwitterRateLimiter(2, TimeUnit.MINUTES)
    }

    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder().build()
    }
}
