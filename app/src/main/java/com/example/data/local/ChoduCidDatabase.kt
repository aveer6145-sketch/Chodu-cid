package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.AdminConfig
import com.example.data.model.Complaint
import com.example.data.model.TeamApplication
import com.example.data.model.TeamMember
import com.example.util.SecurityUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Complaint::class, TeamApplication::class, TeamMember::class, AdminConfig::class],
    version = 1,
    exportSchema = false
)
abstract class ChoduCidDatabase : RoomDatabase() {

    abstract fun complaintDao(): ComplaintDao
    abstract fun teamApplicationDao(): TeamApplicationDao
    abstract fun teamMemberDao(): TeamMemberDao
    abstract fun adminConfigDao(): AdminConfigDao

    companion object {
        @Volatile
        private var INSTANCE: ChoduCidDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): ChoduCidDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ChoduCidDatabase::class.java,
                    "chodu_cid_database"
                )
                .addCallback(DatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(database)
                    }
                }
            }

            private suspend fun populateInitialData(database: ChoduCidDatabase) {
                val adminConfigDao = database.adminConfigDao()
                val complaintDao = database.complaintDao()
                val appDao = database.teamApplicationDao()
                val memberDao = database.teamMemberDao()

                // Initial Admin Config for Aryaveer
                adminConfigDao.insertOrUpdateConfig(
                    AdminConfig(
                        id = 1,
                        leaderName = "Aryaveer",
                        title = "Chief Bureau Head - CHODU CID",
                        passcodeHash = SecurityUtils.DEFAULT_LEADER_PIN_HASH
                    )
                )

                // Initial Team Members approved by Aryaveer
                val m1 = TeamMember(
                    memberId = "CID-MBR-01",
                    applicationId = "APP-CID-101",
                    fullName = "Rohan Sharma",
                    course = "B.Tech",
                    branch = "Computer Science (CSE)",
                    badgeNumber = "CID-07",
                    role = "Senior Field Investigator",
                    casesAssignedCount = 3,
                    casesResolvedCount = 2
                )
                val m2 = TeamMember(
                    memberId = "CID-MBR-02",
                    applicationId = "APP-CID-102",
                    fullName = "Sneha Patel",
                    course = "B.Tech",
                    branch = "Electronics & Comm (ECE)",
                    badgeNumber = "CID-18",
                    role = "Hostel & Canteen Specialist",
                    casesAssignedCount = 2,
                    casesResolvedCount = 1
                )
                val m3 = TeamMember(
                    memberId = "CID-MBR-03",
                    applicationId = "APP-CID-103",
                    fullName = "Aman Verma",
                    course = "BCA",
                    branch = "Information Technology",
                    badgeNumber = "CID-22",
                    role = "Evidence & Forensics Analyst",
                    casesAssignedCount = 1,
                    casesResolvedCount = 1
                )
                memberDao.insertMember(m1)
                memberDao.insertMember(m2)
                memberDao.insertMember(m3)

                // Corresponding approved applications
                appDao.insertApplication(
                    TeamApplication(
                        applicationId = "APP-CID-101",
                        fullName = "Rohan Sharma",
                        course = "B.Tech",
                        branch = "Computer Science (CSE)",
                        status = TeamApplication.STATUS_APPROVED,
                        reviewedAt = System.currentTimeMillis() - 86400000L * 7,
                        leaderRemarks = "Approved by Aryaveer. Excellent field instincts."
                    )
                )
                appDao.insertApplication(
                    TeamApplication(
                        applicationId = "APP-CID-102",
                        fullName = "Sneha Patel",
                        course = "B.Tech",
                        branch = "Electronics & Comm (ECE)",
                        status = TeamApplication.STATUS_APPROVED,
                        reviewedAt = System.currentTimeMillis() - 86400000L * 5,
                        leaderRemarks = "Approved by Aryaveer. Assigned to Hostel & Canteen division."
                    )
                )

                // 2 Pending applications for Aryaveer to review
                appDao.insertApplication(
                    TeamApplication(
                        applicationId = "APP-CID-201",
                        fullName = "Devendra Rathore",
                        course = "B.Tech",
                        branch = "Mechanical Engineering",
                        status = TeamApplication.STATUS_PENDING,
                        createdAt = System.currentTimeMillis() - 3600000L * 4
                    )
                )
                appDao.insertApplication(
                    TeamApplication(
                        applicationId = "APP-CID-202",
                        fullName = "Ananya Gupta",
                        course = "MBA",
                        branch = "Marketing & HR",
                        status = TeamApplication.STATUS_PENDING,
                        createdAt = System.currentTimeMillis() - 3600000L * 2
                    )
                )

                // Pre-seeded Realistic College Investigation Cases
                complaintDao.insertComplaint(
                    Complaint(
                        caseId = "CID-2026-101",
                        studentName = "Rahul M. (3rd Year)",
                        course = "B.Tech",
                        branch = "CSE",
                        category = "Hostel",
                        title = "Block B RO Water Purifier Malfunction & Contaminated Water",
                        description = "The water filter in Boys Hostel Block B 2nd floor has been producing yellowish foul-smelling water for 4 days. Several students fell sick. Warden ignored the complaints.",
                        status = Complaint.STATUS_RESOLVED,
                        urgency = Complaint.URGENCY_CRITICAL,
                        investigationNotes = "CID Agent Sneha visited the hostel plant room. Found dead pigeon near intake pipe and expired filter cartridge from 2024.",
                        resolutionDetails = "Aryaveer confronted the Estate Maintenance Officer with photographic evidence. Entire filter replaced and tank sanitized within 24 hours. Safe drinking water restored!",
                        assignedMember = "Sneha Patel",
                        createdAt = System.currentTimeMillis() - 86400000L * 3,
                        updatedAt = System.currentTimeMillis() - 86400000L * 1
                    )
                )

                complaintDao.insertComplaint(
                    Complaint(
                        caseId = "CID-2026-102",
                        studentName = "Anonymous / गुप्त खबरी",
                        course = "B.Tech",
                        branch = "Mechanical",
                        category = "Canteen",
                        title = "College Nescafe & Central Mess Charging ₹5 Above MRP on Cold Drinks & Biscuits",
                        description = "Both main canteens are openly selling ₹40 energy drinks for ₹45 and taking cash without receipts, threatening students who question them.",
                        status = Complaint.STATUS_INVESTIGATING,
                        urgency = Complaint.URGENCY_HIGH,
                        investigationNotes = "Undercover sting conducted by Agent Rohan. Recorded sting video of overcharging and lack of GST bills. Report prepared for Proctorial Board.",
                        resolutionDetails = "",
                        assignedMember = "Rohan Sharma",
                        createdAt = System.currentTimeMillis() - 86400000L * 2,
                        updatedAt = System.currentTimeMillis() - 3600000L * 8
                    )
                )

                complaintDao.insertComplaint(
                    Complaint(
                        caseId = "CID-2026-103",
                        studentName = "Kavita S.",
                        course = "BCA",
                        branch = "IT",
                        category = "Classroom",
                        title = "Main Computer Lab 2 Projector Flickering and 12 Keyboards Broken",
                        description = "During DBMS lab practicals, students are unable to see the slides. Half the keyboards have broken spacebars and Enter keys.",
                        status = Complaint.STATUS_IN_PROGRESS,
                        urgency = Complaint.URGENCY_NORMAL,
                        investigationNotes = "Agent Aman inspected Lab 2. Lab technician was informed and spare keyboards have been retrieved from store room.",
                        resolutionDetails = "",
                        assignedMember = "Aman Verma",
                        createdAt = System.currentTimeMillis() - 86400000L * 1,
                        updatedAt = System.currentTimeMillis() - 3600000L * 2
                    )
                )

                complaintDao.insertComplaint(
                    Complaint(
                        caseId = "CID-2026-104",
                        studentName = "Pooja Trivedi",
                        course = "B.Sc",
                        branch = "Biotech",
                        category = "Examination",
                        title = "Admit Cards Withheld for Unjustified Library Fine",
                        description = "Library staff is demanding ₹1200 fine for books that were submitted in December. Refusing to issue exam clearance slip.",
                        status = Complaint.STATUS_NEW,
                        urgency = Complaint.URGENCY_HIGH,
                        investigationNotes = "",
                        resolutionDetails = "",
                        assignedMember = null,
                        createdAt = System.currentTimeMillis() - 3600000L * 5,
                        updatedAt = System.currentTimeMillis() - 3600000L * 5
                    )
                )
            }
        }
    }
}
