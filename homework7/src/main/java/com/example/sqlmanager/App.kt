package com.example.sqlmanager

import android.app.Application
import androidx.room.Room
import com.example.sqlmanager.sql.AppDatabase

class App: Application() {
    companion object {
        var database: AppDatabase? = null
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "db").build()
    }
}