package com.example.sqlmanager.sql

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProfileDao {

    @Query("SELECT * FROM myprofile")
    fun getAll(): List<MyProfile>

    @Insert
    fun insert(profile: MyProfile)
}