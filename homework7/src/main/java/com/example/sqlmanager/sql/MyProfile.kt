package com.example.sqlmanager.sql

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MyProfile(
    val name: String,
    val phoneNum: String?,
    val photoUrl: String?,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null
)