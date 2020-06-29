package com.scrater.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.scrater.CoroutineTestRule
import com.scrater.TestHelpers.Companion.scrapeResponse1
import com.scrater.data.FakeRepository
import com.scrater.data.Result
import com.scrater.getOrAwaitValue
import com.scrater.observeForTesting
import com.scrater.vo.Tweet
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MainViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineTestRule = CoroutineTestRule()

    private lateinit var repository: FakeRepository
    private lateinit var mainViewModel: MainViewModel

    @Before
    fun setup() {
        repository = FakeRepository()
        mainViewModel = MainViewModel(repository, coroutineTestRule.testDispatcherProvider)
    }

    @Test
    fun `given account, when fetch successful, load tweets`() =
        coroutineTestRule.testDispatcher.runBlockingTest {
            repository.mockTweetsLoading()
            mainViewModel.setAccount("abc")

            mainViewModel.tweets.observeForTesting {
                assertThat((mainViewModel.tweets.getOrAwaitValue() as Result.Loading<List<Tweet>>).data)
                    .isEqualTo(emptyList<Tweet>())

                repository.mockTweetsLoad("abc")

                assertThat((mainViewModel.tweets.getOrAwaitValue() as Result.Success<List<Tweet>>).data)
                    .isEqualTo(scrapeResponse1("abc"))
            }
        }

    @Test
    fun `given account, when fetch unsuccessful, load error`() =
        coroutineTestRule.testDispatcher.runBlockingTest {
            repository.mockTweetsLoading()
            mainViewModel.setAccount("abc")

            mainViewModel.tweets.observeForTesting {
                assertThat((mainViewModel.tweets.getOrAwaitValue() as Result.Loading<List<Tweet>>).data)
                    .isEqualTo(emptyList<Tweet>())

                repository.mockTweetsError()

                assertThat((mainViewModel.tweets.getOrAwaitValue() as Result.Error<List<Tweet>>).exception)
                    .hasMessageThat().isEqualTo("This user does not exist.")
            }
        }
}