package com.scrater.data

interface RateLimiter<in KEY> {

    fun shouldFetch(key: KEY): Boolean

    fun reset(key: KEY)
}