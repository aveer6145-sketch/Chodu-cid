package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "team_members")
data class TeamMember(
    @PrimaryKey val memberId: String,
    val applicationId: String,
    val fullName: String,
    val course: String,
    val branch: String,
    val badgeNumber: String,
    val role: String = "Special Investigator",
    val joinedAt: Long = System.currentTimeMillis(),
    val casesAssignedCount: Int = 0,
    val casesResolvedCount: Int = 0,
    val isActive: Boolean = true
)
