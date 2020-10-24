package com.example.cfthomework

import android.app.Application
import android.content.Context

class App: Application() {

    companion object {
        var appContext: Context? = null
            private set(value){
                field = value
            }
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }
}