package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "admin_config")
data class AdminConfig(
    @PrimaryKey val id: Int = 1,
    val leaderName: String = "Aryaveer",
    val title: String = "Chief of CHODU CID",
    val passcodeHash: String, // SHA-256 hash of leader passcode
    val sessionToken: String? = null,
    val lastLoginAt: Long = 0L
)
