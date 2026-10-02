package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "complaints")
data class Complaint(
    @PrimaryKey val caseId: String,
    val studentName: String = "", // Optional or "Anonymous / गुप्त"
    val course: String,
    val branch: String,
    val category: String, // College, Faculty, Classroom, Examination, Hostel, Canteen, Fees, Infrastructure, Other
    val title: String,
    val description: String,
    val imageUri: String? = null,
    val status: String = STATUS_NEW, // New, Under Investigation, In Progress, Resolved, Rejected
    val urgency: String = URGENCY_NORMAL, // Normal, High, Critical
    val investigationNotes: String = "",
    val resolutionDetails: String = "",
    val assignedMember: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    companion object {
        const val STATUS_NEW = "New"
        const val STATUS_INVESTIGATING = "Under Investigation"
        const val STATUS_IN_PROGRESS = "In Progress"
        const val STATUS_RESOLVED = "Resolved"
        const val STATUS_REJECTED = "Rejected"

        const val URGENCY_NORMAL = "Normal"
        const val URGENCY_HIGH = "High"
        const val URGENCY_CRITICAL = "Critical"

        val CATEGORIES = listOf(
            "College",
            "Faculty",
            "Classroom",
            "Examination",
            "Hostel",
            "Canteen",
            "Fees",
            "Infrastructure",
            "Other"
        )
    }
}
