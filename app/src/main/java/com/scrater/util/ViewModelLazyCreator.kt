package com.scrater.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.createViewModelLazy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * https://stackoverflow.com/a/50681021
 *
 */
@Suppress("UNCHECKED_CAST")
inline fun <reified T : ViewModel> Fragment.fragmentViewModels(
    crossinline creator: () -> T
): Lazy<T> {
    return createViewModelLazy(T::class, storeProducer = {
        viewModelStore
    }, factoryProducer = {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(
                modelClass: Class<T>
            ): T = creator.invoke() as T
        }
    })
}