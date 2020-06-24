package com.scrater.data

class FakeRateLimiter : RateLimiter<String> {

    private val accounts = mutableMapOf<String, Boolean>()

    override fun shouldFetch(key: String): Boolean {
        return accounts.getOrDefault(key, true)
    }

    override fun reset(key: String) {
        accounts[key] = false
    }

}
