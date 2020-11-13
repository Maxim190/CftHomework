package com.example.sqlmanager.sql

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MyProfile::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getProfileDao(): ProfileDao
}