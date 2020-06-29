package com.scrater

import android.app.Application
import com.scrater.di.AppComponent
import com.scrater.di.DaggerAppComponent

class ScraterApplication : Application() {

    lateinit var appComponent: AppComponent
        private set

    companion object {
        private lateinit var INSTANCE: ScraterApplication

        fun get(): ScraterApplication = INSTANCE
    }

    override fun onCreate() {
        super.onCreate()

        INSTANCE = this
        appComponent = DaggerAppComponent.factory().create(applicationContext)
    }
}
