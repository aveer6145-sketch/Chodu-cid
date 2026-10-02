package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.AdminConfig
import kotlinx.coroutines.flow.Flow

@Dao
interface AdminConfigDao {
    @Query("SELECT * FROM admin_config WHERE id = 1 LIMIT 1")
    fun getAdminConfig(): Flow<AdminConfig?>

    @Query("SELECT * FROM admin_config WHERE id = 1 LIMIT 1")
    suspend fun getAdminConfigDirect(): AdminConfig?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateConfig(config: AdminConfig)
}
