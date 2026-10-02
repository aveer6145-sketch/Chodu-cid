package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.TeamApplication
import kotlinx.coroutines.flow.Flow

@Dao
interface TeamApplicationDao {
    @Query("SELECT * FROM team_applications ORDER BY createdAt DESC")
    fun getAllApplications(): Flow<List<TeamApplication>>

    @Query("SELECT * FROM team_applications WHERE applicationId = :appId LIMIT 1")
    fun getApplicationById(appId: String): Flow<TeamApplication?>

    @Query("SELECT * FROM team_applications WHERE applicationId = :appId LIMIT 1")
    suspend fun findApplicationById(appId: String): TeamApplication?

    @Query("SELECT COUNT(*) FROM team_applications WHERE status = :status")
    fun getCountByStatus(status: String): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApplication(application: TeamApplication)

    @Update
    suspend fun updateApplication(application: TeamApplication)

    @Query("DELETE FROM team_applications WHERE applicationId = :appId")
    suspend fun deleteApplicationById(appId: String)
}
