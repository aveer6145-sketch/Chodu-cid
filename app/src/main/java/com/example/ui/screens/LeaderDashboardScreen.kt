package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PersonRemove
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.R
import com.example.data.model.Complaint
import com.example.data.model.TeamApplication
import com.example.data.model.TeamMember
import com.example.ui.components.CaseStatusBadge
import com.example.ui.components.StatCounterCard
import com.example.ui.components.UrgencyBadge
import com.example.ui.theme.CidBlueInfo
import com.example.ui.theme.CidGreenSuccess
import com.example.ui.theme.CidNavyBackground
import com.example.ui.theme.CidNavyBorder
import com.example.ui.theme.CidNavyCard
import com.example.ui.theme.CidNavyDark
import com.example.ui.theme.CidNavySurface
import com.example.ui.theme.CidOrangeWarning
import com.example.ui.theme.CidPurpleBadge
import com.example.ui.theme.CidRedAlert
import com.example.ui.theme.CidTextMuted
import com.example.ui.theme.CidTextPrimary
import com.example.ui.theme.CidTextSecondary
import com.example.ui.theme.CidYellowBright
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderDashboardScreen(
    complaints: List<Complaint>,
    applications: List<TeamApplication>,
    members: List<TeamMember>,
    totalProblemsCount: Int,
    pendingCasesCount: Int,
    investigatingCount: Int,
    resolvedCasesCount: Int,
    pendingAppsCount: Int,
    approvedTeamCount: Int,
    onApproveApplication: (String, String?) -> Unit,
    onRejectApplication: (String, String?) -> Unit,
    onRemoveMember: (String) -> Unit,
    onUpdateComplaint: (caseId: String, status: String, notes: String, resolution: String, assignedMember: String?) -> Unit,
    onDeleteComplaint: (String) -> Unit,
    onUpdatePin: (oldPin: String, newPin: String, (Boolean) -> Unit) -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    // 0: Overview, 1: Problems, 2: Applications, 3: Team, 4: Security

    // Filters for problems
    var categoryFilter by remember { mutableStateOf<String?>(null) }
    var statusFilter by remember { mutableStateOf<String?>(null) }

    // Dialog state for editing a complaint
    var editingComplaint by remember { mutableStateOf<Complaint?>(null) }
    var editStatus by remember { mutableStateOf("") }
    var editNotes by remember { mutableStateOf("") }
    var editResolution by remember { mutableStateOf("") }
    var editAssignedMember by remember { mutableStateOf<String?>(null) }
    var isMemberDropdownOpen by remember { mutableStateOf(false) }

    // Dialog for PIN change
    var showPinChangeDialog by remember { mutableStateOf(false) }
    var oldPinInput by remember { mutableStateOf("") }
    var newPinInput by remember { mutableStateOf("") }
    var pinChangeStatus by remember { mutableStateOf<String?>(null) }

    val tabTitles = listOf("Dashboard", "Cases", "Applications", "Team", "Security")

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CidNavyBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(6.dp))

            // Chief Aryaveer Header Bar
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CidNavyCard),
                border = androidx.compose.foundation.BorderStroke(1.2.dp, CidYellowBright)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .border(1.5.dp, CidYellowBright, CircleShape)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_leader_badge),
                                contentDescription = "Aryaveer Chief",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Aryaveer",
                                    color = CidTextPrimary,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Black
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Box(
                                    modifier = Modifier
                                        .background(CidYellowBright, RoundedCornerShape(4.dp))
                                        .padding(horizontal = 6.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        text = "CHIEF LEADER",
                                        color = CidNavyDark,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                }
                            }
                            Text(
                                text = "Admin Control Room • Supreme Authority",
                                color = CidTextSecondary,
                                fontSize = 11.5.sp
                            )
                        }
                    }

                    // Logout Button
                    IconButton(
                        onClick = onLogout,
                        modifier = Modifier
                            .size(38.dp)
                            .background(CidNavySurface, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ExitToApp,
                            contentDescription = "Lock & Logout",
                            tint = CidRedAlert,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        // Horizontal Tab Bar with notification badges
        item {
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                containerColor = CidNavyCard,
                contentColor = CidYellowBright,
                edgePadding = 0.dp,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = CidYellowBright,
                        height = 3.dp
                    )
                },
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, CidNavyBorder, RoundedCornerShape(12.dp))
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = title,
                                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 12.5.sp
                                )
                                if (index == 2 && pendingAppsCount > 0) {
                                    Box(
                                        modifier = Modifier
                                            .size(16.dp)
                                            .background(CidRedAlert, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "$pendingAppsCount",
                                            color = Color.White,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }

        // ================= TAB 0: OVERVIEW & STATISTICS =================
        if (selectedTab == 0) {
            item {
                Text(
                    text = "DASHBOARD STATISTICS",
                    color = CidYellowBright,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            // Grid of 6 statistics requested in prompt:
            // 1. Total reported cases, 2. Pending cases, 3. Cases under investigation,
            // 4. Resolved cases, 5. Pending membership applications, 6. Total approved team members
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatCounterCard(
                            title = "Total Complaints",
                            count = totalProblemsCount,
                            icon = Icons.Filled.ReportProblem,
                            accentColor = CidYellowBright,
                            modifier = Modifier.weight(1f)
                        )
                        StatCounterCard(
                            title = "New Cases",
                            count = pendingCasesCount,
                            icon = Icons.Filled.HourglassEmpty,
                            accentColor = CidOrangeWarning,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatCounterCard(
                            title = "Under Investigation",
                            count = investigatingCount,
                            icon = Icons.Filled.Security,
                            accentColor = CidBlueInfo,
                            modifier = Modifier.weight(1f)
                        )
                        StatCounterCard(
                            title = "Resolved Cases",
                            count = resolvedCasesCount,
                            icon = Icons.Filled.CheckCircle,
                            accentColor = CidGreenSuccess,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatCounterCard(
                            title = "Pending Applications",
                            count = pendingAppsCount,
                            icon = Icons.Filled.Notifications,
                            accentColor = if (pendingAppsCount > 0) CidRedAlert else CidTextSecondary,
                            subtitle = if (pendingAppsCount > 0) "Needs your review!" else "All cleared",
                            modifier = Modifier.weight(1f)
                        )
                        StatCounterCard(
                            title = "Approved Members",
                            count = approvedTeamCount,
                            icon = Icons.Filled.Group,
                            accentColor = CidPurpleBadge,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Quick Actions Box
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = CidNavyCard),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CidNavyBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Aryaveer's Quick Action Center",
                            color = CidTextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { selectedTab = 2 },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (pendingAppsCount > 0) CidYellowBright else CidNavySurface,
                                    contentColor = if (pendingAppsCount > 0) CidNavyDark else CidTextPrimary
                                ),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Review Apps ($pendingAppsCount)", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = { selectedTab = 1 },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = CidNavySurface,
                                    contentColor = CidYellowBright
                                ),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Manage Cases ($totalProblemsCount)", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // ================= TAB 1: PROBLEM MANAGEMENT =================
        if (selectedTab == 1) {
            // Filters row
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "FILTER CASES BY CATEGORY & STATUS",
                        color = CidYellowBright,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )

                    // Categories chip row
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        item {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(if (categoryFilter == null) CidYellowBright else CidNavyCard)
                                    .border(1.dp, if (categoryFilter == null) CidYellowBright else CidNavyBorder, RoundedCornerShape(16.dp))
                                    .clickable { categoryFilter = null }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "All Categories",
                                    color = if (categoryFilter == null) CidNavyDark else CidTextSecondary,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        items(Complaint.CATEGORIES) { cat ->
                            val isSelected = categoryFilter == cat
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(if (isSelected) CidYellowBright else CidNavyCard)
                                    .border(1.dp, if (isSelected) CidYellowBright else CidNavyBorder, RoundedCornerShape(16.dp))
                                    .clickable { categoryFilter = if (isSelected) null else cat }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = cat,
                                    color = if (isSelected) CidNavyDark else CidTextSecondary,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    // Status filter row
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        val statuses = listOf(
                            null,
                            Complaint.STATUS_NEW,
                            Complaint.STATUS_INVESTIGATING,
                            Complaint.STATUS_IN_PROGRESS,
                            Complaint.STATUS_RESOLVED,
                            Complaint.STATUS_REJECTED
                        )

                        items(statuses) { st ->
                            val isSelected = statusFilter == st
                            val label = st ?: "All Statuses"
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(if (isSelected) CidBlueInfo else CidNavySurface)
                                    .border(1.dp, if (isSelected) CidBlueInfo else CidNavyBorder, RoundedCornerShape(16.dp))
                                    .clickable { statusFilter = if (isSelected) null else st }
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = label,
                                    color = if (isSelected) CidNavyDark else CidTextSecondary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }

            val filteredComplaints = complaints.filter { c ->
                (categoryFilter == null || c.category == categoryFilter) &&
                (statusFilter == null || c.status == statusFilter)
            }

            if (filteredComplaints.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No complaints found for selected filters.", color = CidTextMuted, fontSize = 13.sp)
                    }
                }
            }

            items(filteredComplaints) { complaint ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = CidNavyCard),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CidNavyBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = complaint.caseId,
                                    color = CidYellowBright,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Black
                                )
                                Text(
                                    text = "${complaint.category} • ${complaint.course} (${complaint.branch})",
                                    color = CidTextMuted,
                                    fontSize = 11.sp
                                )
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                CaseStatusBadge(status = complaint.status)
                                UrgencyBadge(urgency = complaint.urgency)
                            }
                        }

                        Text(
                            text = complaint.title,
                            color = CidTextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = complaint.description,
                            color = CidTextSecondary,
                            fontSize = 12.5.sp,
                            lineHeight = 18.sp
                        )

                        // Show image if attached
                        if (complaint.imageUri != null) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(140.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .border(1.dp, CidNavyBorder, RoundedCornerShape(8.dp))
                            ) {
                                Image(
                                    painter = rememberAsyncImagePainter(model = complaint.imageUri),
                                    contentDescription = "Evidence Photo",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }

                        // Reporter & Assigned
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "By: ${complaint.studentName}",
                                color = CidTextMuted,
                                fontSize = 11.5.sp
                            )
                            if (complaint.assignedMember != null) {
                                Text(
                                    text = "Agent: ${complaint.assignedMember}",
                                    color = CidBlueInfo,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        // Investigation notes or resolution preview
                        if (complaint.investigationNotes.isNotBlank()) {
                            Text(
                                text = "CID Log: ${complaint.investigationNotes}",
                                color = CidBlueInfo,
                                fontSize = 11.5.sp
                            )
                        }

                        // Action Buttons: Edit/Investigate & Delete
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    editingComplaint = complaint
                                    editStatus = complaint.status
                                    editNotes = complaint.investigationNotes
                                    editResolution = complaint.resolutionDetails
                                    editAssignedMember = complaint.assignedMember
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = CidNavySurface,
                                    contentColor = CidYellowBright
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(imageVector = Icons.Filled.Edit, contentDescription = "Manage", modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Update Dossier", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }

                            OutlinedButton(
                                onClick = { onDeleteComplaint(complaint.caseId) },
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = CidRedAlert),
                                border = androidx.compose.foundation.BorderStroke(1.dp, CidRedAlert.copy(alpha = 0.5f)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete", modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }
        }

        // ================= TAB 2: MEMBERSHIP REQUESTS (APPLICATIONS) =================
        if (selectedTab == 2) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "MEMBERSHIP APPLICATIONS FOR ARYAVEER",
                        color = CidYellowBright,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )

                    if (pendingAppsCount > 0) {
                        Box(
                            modifier = Modifier
                                .background(CidRedAlert, RoundedCornerShape(12.dp))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "$pendingAppsCount Pending",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            if (applications.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(30.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No membership applications submitted yet.", color = CidTextMuted, fontSize = 13.sp)
                    }
                }
            }

            items(applications) { app ->
                val dateStr = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
                    .format(Date(app.createdAt))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = CidNavyCard),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (app.status == TeamApplication.STATUS_PENDING) CidYellowBright.copy(alpha = 0.8f) else CidNavyBorder
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = app.applicationId,
                                    color = CidYellowBright,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black
                                )
                                Text(
                                    text = "Applied: $dateStr",
                                    color = CidTextMuted,
                                    fontSize = 10.5.sp
                                )
                            }
                            CaseStatusBadge(status = app.status)
                        }

                        // Applicant Info Card
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(CidNavyDark, RoundedCornerShape(8.dp))
                                .padding(10.dp)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    text = "Applicant: ${app.fullName}",
                                    color = CidTextPrimary,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Course: ${app.course}",
                                    color = CidTextSecondary,
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = "Branch: ${app.branch}",
                                    color = CidTextSecondary,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        if (app.leaderRemarks != null) {
                            Text(
                                text = "Leader Remark: ${app.leaderRemarks}",
                                color = CidYellowBright,
                                fontSize = 11.5.sp
                            )
                        }

                        // Approve and Reject buttons (Only Aryaveer can approve/reject)
                        if (app.status == TeamApplication.STATUS_PENDING) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Button(
                                    onClick = {
                                        onApproveApplication(app.applicationId, "Approved by Aryaveer - Official Detective")
                                    },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = CidGreenSuccess,
                                        contentColor = CidNavyDark
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(imageVector = Icons.Filled.Check, contentDescription = "Approve", modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Approve Member", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }

                                Button(
                                    onClick = {
                                        onRejectApplication(app.applicationId, "Application rejected by Chief Aryaveer.")
                                    },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = CidRedAlert,
                                        contentColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(imageVector = Icons.Filled.Close, contentDescription = "Reject", modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Reject", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }

        // ================= TAB 3: TEAM MANAGEMENT =================
        if (selectedTab == 3) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "OFFICIAL APPROVED CHODU CID AGENTS",
                        color = CidYellowBright,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )

                    Text(
                        text = "$approvedTeamCount Active Detectives",
                        color = CidTextSecondary,
                        fontSize = 11.sp
                    )
                }
            }

            if (members.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(30.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No approved team members yet. Approve applicants to build your squad!", color = CidTextMuted, fontSize = 13.sp)
                    }
                }
            }

            items(members) { member ->
                val joinedDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                    .format(Date(member.joinedAt))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = CidNavyCard),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CidNavyBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .background(CidPurpleBadge.copy(alpha = 0.2f), CircleShape)
                                    .border(1.dp, CidPurpleBadge, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = member.fullName.take(2).uppercase(),
                                    color = CidPurpleBadge,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }

                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = member.fullName,
                                        color = CidTextPrimary,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .background(CidYellowBright.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                                            .padding(horizontal = 6.dp, vertical = 1.dp)
                                    ) {
                                        Text(
                                            text = member.badgeNumber,
                                            color = CidYellowBright,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Text(
                                    text = "${member.role} • ${member.course} (${member.branch})",
                                    color = CidTextSecondary,
                                    fontSize = 11.5.sp
                                )

                                Text(
                                    text = "Joined: $joinedDate • Solved: ${member.casesResolvedCount}",
                                    color = CidTextMuted,
                                    fontSize = 10.5.sp
                                )
                            }
                        }

                        // Remove Member Button (Aryaveer only)
                        IconButton(
                            onClick = { onRemoveMember(member.memberId) },
                            modifier = Modifier
                                .size(36.dp)
                                .background(CidNavyDark, CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.PersonRemove,
                                contentDescription = "Remove Member",
                                tint = CidRedAlert,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }

        // ================= TAB 4: SECURITY SETTINGS =================
        if (selectedTab == 4) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = CidNavyCard),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CidNavyBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(
                            text = "Aryaveer's Security Protocol",
                            color = CidYellowBright,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Manage your Chief access PIN. Credentials are encrypted via SHA-256 and stored in secure offline local storage.",
                            color = CidTextSecondary,
                            fontSize = 12.sp
                        )

                        OutlinedTextField(
                            value = oldPinInput,
                            onValueChange = { oldPinInput = it },
                            label = { Text("Current Chief PIN") },
                            placeholder = { Text("e.g. 7007") },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CidYellowBright,
                                unfocusedBorderColor = CidNavyBorder,
                                focusedTextColor = CidTextPrimary,
                                unfocusedTextColor = CidTextPrimary,
                                focusedContainerColor = CidNavyDark,
                                unfocusedContainerColor = CidNavyDark
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = newPinInput,
                            onValueChange = { newPinInput = it },
                            label = { Text("New Chief PIN (4-8 digits)") },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CidYellowBright,
                                unfocusedBorderColor = CidNavyBorder,
                                focusedTextColor = CidTextPrimary,
                                unfocusedTextColor = CidTextPrimary,
                                focusedContainerColor = CidNavyDark,
                                unfocusedContainerColor = CidNavyDark
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        if (pinChangeStatus != null) {
                            Text(
                                text = pinChangeStatus ?: "",
                                color = if (pinChangeStatus?.contains("Success") == true) CidGreenSuccess else CidRedAlert,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Button(
                            onClick = {
                                if (oldPinInput.isNotBlank() && newPinInput.isNotBlank()) {
                                    onUpdatePin(oldPinInput, newPinInput) { success ->
                                        if (success) {
                                            pinChangeStatus = "Success! Chief PIN updated."
                                            oldPinInput = ""
                                            newPinInput = ""
                                        } else {
                                            pinChangeStatus = "Incorrect Current PIN. Failed to update."
                                        }
                                    }
                                } else {
                                    pinChangeStatus = "Please fill both PIN fields"
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CidYellowBright,
                                contentColor = CidNavyDark
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(imageVector = Icons.Filled.LockReset, contentDescription = "Update PIN")
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Update Chief PIN", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }

    // Modal Dialog to Update Complaint Dossier
    if (editingComplaint != null) {
        val targetComplaint = editingComplaint!!

        AlertDialog(
            onDismissRequest = { editingComplaint = null },
            containerColor = CidNavyCard,
            title = {
                Text(
                    text = "Update Dossier: ${targetComplaint.caseId}",
                    color = CidYellowBright,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        Text(
                            text = targetComplaint.title,
                            color = CidTextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    // Status Dropdown
                    item {
                        val statusOptions = listOf(
                            Complaint.STATUS_NEW,
                            Complaint.STATUS_INVESTIGATING,
                            Complaint.STATUS_IN_PROGRESS,
                            Complaint.STATUS_RESOLVED,
                            Complaint.STATUS_REJECTED
                        )

                        Text("Case Status:", color = CidTextSecondary, fontSize = 11.5.sp)
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(statusOptions) { st ->
                                val isSelected = editStatus == st
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) CidYellowBright else CidNavyDark)
                                        .border(1.dp, if (isSelected) CidYellowBright else CidNavyBorder, RoundedCornerShape(8.dp))
                                        .clickable { editStatus = st }
                                        .padding(horizontal = 8.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = st,
                                        color = if (isSelected) CidNavyDark else CidTextSecondary,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }

                    // Assign Team Member
                    item {
                        Text("Assign Agent:", color = CidTextSecondary, fontSize = 11.5.sp)
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            item {
                                val isSelected = editAssignedMember == null
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) CidBlueInfo else CidNavyDark)
                                        .clickable { editAssignedMember = null }
                                        .padding(horizontal = 8.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = "Unassigned",
                                        color = if (isSelected) CidNavyDark else CidTextSecondary,
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            items(members) { m ->
                                val isSelected = editAssignedMember == m.fullName
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) CidBlueInfo else CidNavyDark)
                                        .clickable { editAssignedMember = m.fullName }
                                        .padding(horizontal = 8.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = m.fullName,
                                        color = if (isSelected) CidNavyDark else CidTextSecondary,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }

                    // Investigation Notes
                    item {
                        OutlinedTextField(
                            value = editNotes,
                            onValueChange = { editNotes = it },
                            label = { Text("CID Investigation Notes / Bayan") },
                            placeholder = { Text("What did the CID inquiry reveal?") },
                            minLines = 3,
                            maxLines = 5,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CidYellowBright,
                                unfocusedBorderColor = CidNavyBorder,
                                focusedTextColor = CidTextPrimary,
                                unfocusedTextColor = CidTextPrimary,
                                focusedContainerColor = CidNavyDark,
                                unfocusedContainerColor = CidNavyDark
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Resolution Details
                    item {
                        OutlinedTextField(
                            value = editResolution,
                            onValueChange = { editResolution = it },
                            label = { Text("Official Action Taken & Resolution") },
                            placeholder = { Text("How was the problem resolved with college authorities?") },
                            minLines = 2,
                            maxLines = 4,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CidYellowBright,
                                unfocusedBorderColor = CidNavyBorder,
                                focusedTextColor = CidTextPrimary,
                                unfocusedTextColor = CidTextPrimary,
                                focusedContainerColor = CidNavyDark,
                                unfocusedContainerColor = CidNavyDark
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onUpdateComplaint(
                            targetComplaint.caseId,
                            editStatus,
                            editNotes,
                            editResolution,
                            editAssignedMember
                        )
                        editingComplaint = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CidYellowBright,
                        contentColor = CidNavyDark
                    )
                ) {
                    Text("Save Changes", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { editingComplaint = null },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = CidTextSecondary),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CidNavyBorder)
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}
