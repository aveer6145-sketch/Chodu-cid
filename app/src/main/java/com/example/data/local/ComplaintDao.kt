package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.Complaint
import kotlinx.coroutines.flow.Flow

@Dao
interface ComplaintDao {
    @Query("SELECT * FROM complaints ORDER BY createdAt DESC")
    fun getAllComplaints(): Flow<List<Complaint>>

    @Query("SELECT * FROM complaints WHERE caseId = :caseId LIMIT 1")
    fun getComplaintByCaseId(caseId: String): Flow<Complaint?>

    @Query("SELECT * FROM complaints WHERE caseId = :caseId LIMIT 1")
    suspend fun findComplaintByCaseId(caseId: String): Complaint?

    @Query("SELECT * FROM complaints WHERE assignedMember = :memberName ORDER BY createdAt DESC")
    fun getComplaintsForMember(memberName: String): Flow<List<Complaint>>

    @Query("SELECT COUNT(*) FROM complaints")
    fun getTotalComplaintCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM complaints WHERE status = :status")
    fun getComplaintCountByStatus(status: String): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComplaint(complaint: Complaint)

    @Update
    suspend fun updateComplaint(complaint: Complaint)

    @Query("DELETE FROM complaints WHERE caseId = :caseId")
    suspend fun deleteComplaintById(caseId: String)
}
