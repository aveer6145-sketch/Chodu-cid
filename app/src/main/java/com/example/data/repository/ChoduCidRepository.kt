package com.example.data.repository

import com.example.data.local.AdminConfigDao
import com.example.data.local.ComplaintDao
import com.example.data.local.TeamApplicationDao
import com.example.data.local.TeamMemberDao
import com.example.data.model.AdminConfig
import com.example.data.model.Complaint
import com.example.data.model.TeamApplication
import com.example.data.model.TeamMember
import com.example.util.SecurityUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlin.random.Random

class ChoduCidRepository(
    private val complaintDao: ComplaintDao,
    private val teamApplicationDao: TeamApplicationDao,
    private val teamMemberDao: TeamMemberDao,
    private val adminConfigDao: AdminConfigDao
) {
    // Flow observables for UI
    val allComplaints: Flow<List<Complaint>> = complaintDao.getAllComplaints()
    val totalComplaintCount: Flow<Int> = complaintDao.getTotalComplaintCount()
    val resolvedComplaintCount: Flow<Int> = complaintDao.getComplaintCountByStatus(Complaint.STATUS_RESOLVED)
    val investigatingComplaintCount: Flow<Int> = complaintDao.getComplaintCountByStatus(Complaint.STATUS_INVESTIGATING)
    val inProgressComplaintCount: Flow<Int> = complaintDao.getComplaintCountByStatus(Complaint.STATUS_IN_PROGRESS)
    val newComplaintCount: Flow<Int> = complaintDao.getComplaintCountByStatus(Complaint.STATUS_NEW)

    val allApplications: Flow<List<TeamApplication>> = teamApplicationDao.getAllApplications()
    val pendingApplicationCount: Flow<Int> = teamApplicationDao.getCountByStatus(TeamApplication.STATUS_PENDING)

    val activeMembers: Flow<List<TeamMember>> = teamMemberDao.getActiveMembers()
    val activeMemberCount: Flow<Int> = teamMemberDao.getActiveMemberCount()

    val adminConfig: Flow<AdminConfig?> = adminConfigDao.getAdminConfig()

    fun getComplaintByCaseId(caseId: String): Flow<Complaint?> {
        return complaintDao.getComplaintByCaseId(caseId.trim().uppercase())
    }

    fun getApplicationById(appId: String): Flow<TeamApplication?> {
        return teamApplicationDao.getApplicationById(appId.trim().uppercase())
    }

    fun getComplaintsForMember(memberName: String): Flow<List<Complaint>> {
        return complaintDao.getComplaintsForMember(memberName)
    }

    suspend fun submitComplaint(
        studentName: String,
        course: String,
        branch: String,
        category: String,
        title: String,
        description: String,
        imageUri: String? = null,
        urgency: String = Complaint.URGENCY_NORMAL
    ): String = withContext(Dispatchers.IO) {
        val randomNum = Random.nextInt(1000, 9999)
        val caseId = "CID-2026-$randomNum"
        val effectiveName = if (studentName.isBlank()) "Anonymous / गुप्त खबरी" else studentName.trim()

        val complaint = Complaint(
            caseId = caseId,
            studentName = effectiveName,
            course = course.trim(),
            branch = branch.trim(),
            category = category.trim(),
            title = title.trim(),
            description = description.trim(),
            imageUri = imageUri,
            status = Complaint.STATUS_NEW,
            urgency = urgency,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        complaintDao.insertComplaint(complaint)
        caseId
    }

    suspend fun updateComplaintStatus(
        caseId: String,
        status: String,
        notes: String,
        resolution: String,
        assignedMember: String?
    ) = withContext(Dispatchers.IO) {
        val existing = complaintDao.findComplaintByCaseId(caseId) ?: return@withContext
        val updated = existing.copy(
            status = status,
            investigationNotes = notes,
            resolutionDetails = resolution,
            assignedMember = assignedMember,
            updatedAt = System.currentTimeMillis()
        )
        complaintDao.updateComplaint(updated)
    }

    suspend fun deleteComplaint(caseId: String) = withContext(Dispatchers.IO) {
        complaintDao.deleteComplaintById(caseId)
    }

    // Team Applications
    suspend fun submitApplication(
        fullName: String,
        course: String,
        branch: String
    ): String = withContext(Dispatchers.IO) {
        val randomNum = Random.nextInt(1000, 9999)
        val appId = "APP-CID-$randomNum"
        val application = TeamApplication(
            applicationId = appId,
            fullName = fullName.trim(),
            course = course.trim(),
            branch = branch.trim(),
            status = TeamApplication.STATUS_PENDING,
            createdAt = System.currentTimeMillis()
        )
        teamApplicationDao.insertApplication(application)
        appId
    }

    // Aryaveer only: Approve Application
    suspend fun approveApplication(
        appId: String,
        remarks: String?
    ): Boolean = withContext(Dispatchers.IO) {
        val app = teamApplicationDao.findApplicationById(appId) ?: return@withContext false
        val updatedApp = app.copy(
            status = TeamApplication.STATUS_APPROVED,
            reviewedAt = System.currentTimeMillis(),
            leaderRemarks = remarks ?: "Approved by Aryaveer"
        )
        teamApplicationDao.updateApplication(updatedApp)

        // Check if member already exists
        val existingMember = teamMemberDao.findMemberByAppId(appId)
        if (existingMember == null) {
            val memberRandom = Random.nextInt(10, 99)
            val memberId = "CID-MBR-$memberRandom"
            val badge = "CID-${Random.nextInt(30, 99)}"
            val newMember = TeamMember(
                memberId = memberId,
                applicationId = appId,
                fullName = app.fullName,
                course = app.course,
                branch = app.branch,
                badgeNumber = badge,
                role = "Special Investigator",
                joinedAt = System.currentTimeMillis(),
                isActive = true
            )
            teamMemberDao.insertMember(newMember)
        }
        true
    }

    // Aryaveer only: Reject Application
    suspend fun rejectApplication(
        appId: String,
        remarks: String?
    ): Boolean = withContext(Dispatchers.IO) {
        val app = teamApplicationDao.findApplicationById(appId) ?: return@withContext false
        val updatedApp = app.copy(
            status = TeamApplication.STATUS_REJECTED,
            reviewedAt = System.currentTimeMillis(),
            leaderRemarks = remarks ?: "Application not accepted at this time."
        )
        teamApplicationDao.updateApplication(updatedApp)
        true
    }

    // Aryaveer only: Remove Member
    suspend fun removeTeamMember(memberId: String) = withContext(Dispatchers.IO) {
        teamMemberDao.deleteMember(memberId)
    }

    // Leader Authentication
    suspend fun verifyLeaderPasscode(inputPin: String): Boolean = withContext(Dispatchers.IO) {
        val config = adminConfigDao.getAdminConfigDirect()
        val storedHash = config?.passcodeHash ?: SecurityUtils.DEFAULT_LEADER_PIN_HASH
        SecurityUtils.verifyPasscode(inputPin, storedHash)
    }

    suspend fun updateLeaderPasscode(oldPin: String, newPin: String): Boolean = withContext(Dispatchers.IO) {
        val config = adminConfigDao.getAdminConfigDirect()
        val storedHash = config?.passcodeHash ?: SecurityUtils.DEFAULT_LEADER_PIN_HASH
        if (SecurityUtils.verifyPasscode(oldPin, storedHash)) {
            val newHash = SecurityUtils.hashPasscode(newPin)
            val updated = (config ?: AdminConfig(id = 1, passcodeHash = newHash)).copy(
                passcodeHash = newHash,
                lastLoginAt = System.currentTimeMillis()
            )
            adminConfigDao.insertOrUpdateConfig(updated)
            true
        } else {
            false
        }
    }

    suspend fun getMemberByName(name: String): TeamMember? = withContext(Dispatchers.IO) {
        teamMemberDao.findMemberByName(name)
    }
}
