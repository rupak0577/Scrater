package com.scrater.main

import androidx.lifecycle.*
import com.scrater.DispatcherProvider
import com.scrater.data.Repository
import com.scrater.data.Result
import com.scrater.vo.Tweet
import kotlinx.coroutines.flow.collect

class MainViewModel(
    private val tweetsRepository: Repository,
    private val dispatchers: DispatcherProvider
) : ViewModel() {
    private val _tweets = MutableLiveData<String>()

    val tweets: LiveData<Result<List<Tweet>>> = _tweets.switchMap { account ->
        liveData(context = viewModelScope.coroutineContext + dispatchers.io()) {
            tweetsRepository.loadTweets(account).collect {
                emit(it)
            }
        }
    }

    fun setAccount(account: String) {
        _tweets.value = account
    }
}