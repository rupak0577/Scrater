package com.scrater.di

import android.content.Context
import com.scrater.main.MainViewModel
import dagger.BindsInstance
import dagger.Component
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, InterfaceBindingsModule::class])
interface AppComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): AppComponent
    }

    fun mainViewModelFactory(): Provider<MainViewModel>
}