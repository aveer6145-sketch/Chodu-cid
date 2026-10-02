package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.TeamMember
import kotlinx.coroutines.flow.Flow

@Dao
interface TeamMemberDao {
    @Query("SELECT * FROM team_members WHERE isActive = 1 ORDER BY joinedAt DESC")
    fun getActiveMembers(): Flow<List<TeamMember>>

    @Query("SELECT * FROM team_members WHERE memberId = :memberId LIMIT 1")
    fun getMemberById(memberId: String): Flow<TeamMember?>

    @Query("SELECT * FROM team_members WHERE memberId = :memberId LIMIT 1")
    suspend fun findMemberById(memberId: String): TeamMember?

    @Query("SELECT * FROM team_members WHERE applicationId = :appId LIMIT 1")
    suspend fun findMemberByAppId(appId: String): TeamMember?

    @Query("SELECT * FROM team_members WHERE fullName = :name AND isActive = 1 LIMIT 1")
    suspend fun findMemberByName(name: String): TeamMember?

    @Query("SELECT COUNT(*) FROM team_members WHERE isActive = 1")
    fun getActiveMemberCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMember(member: TeamMember)

    @Update
    suspend fun updateMember(member: TeamMember)

    @Query("UPDATE team_members SET isActive = 0 WHERE memberId = :memberId")
    suspend fun deactivateMember(memberId: String)

    @Query("DELETE FROM team_members WHERE memberId = :memberId")
    suspend fun deleteMember(memberId: String)
}
