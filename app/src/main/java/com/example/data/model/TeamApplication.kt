package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "team_applications")
data class TeamApplication(
    @PrimaryKey val applicationId: String,
    val fullName: String,
    val course: String,
    val branch: String,
    val status: String = STATUS_PENDING, // Pending Approval, Approved, Rejected
    val createdAt: Long = System.currentTimeMillis(),
    val reviewedAt: Long? = null,
    val leaderRemarks: String? = null
) {
    companion object {
        const val STATUS_PENDING = "Pending Approval"
        const val STATUS_APPROVED = "Approved"
        const val STATUS_REJECTED = "Rejected"
    }
}
