package com.scrater.di

import com.scrater.DefaultDispatcherProvider
import com.scrater.DispatcherProvider
import com.scrater.data.RateLimiter
import com.scrater.data.Repository
import com.scrater.data.TweetsRepository
import com.scrater.data.TwitterRateLimiter
import com.scrater.data.source.TweetsDataSource
import com.scrater.data.source.local.TweetsLocalDataSource
import com.scrater.data.source.remote.TweetsRemoteDataSource
import dagger.Binds
import dagger.Module
import javax.inject.Named

@Module
abstract class InterfaceBindingsModule {
    @Binds
    @Named("Local")
    abstract fun provideLocalDataSource(tweetsLocalDataSource: TweetsLocalDataSource): TweetsDataSource

    @Binds
    @Named("Remote")
    abstract fun provideRemoteDataSource(tweetsRemoteDataSource: TweetsRemoteDataSource): TweetsDataSource

    @Binds
    abstract fun provideRateLimiter(twitterRateLimiter: TwitterRateLimiter): RateLimiter<String>

    @Binds
    abstract fun provideRepository(tweetsRepository: TweetsRepository): Repository

    @Binds
    abstract fun provideDispatcherProvider(defaultDispatcherProvider: DefaultDispatcherProvider): DispatcherProvider
}