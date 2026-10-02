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
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.data.model.Complaint
import com.example.data.model.TeamMember
import com.example.ui.components.CaseStatusBadge
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
import com.example.ui.viewmodel.AppNavDestination

@Composable
fun TeamMemberDashboardScreen(
    currentMember: TeamMember?,
    approvedMembers: List<TeamMember>,
    complaints: List<Complaint>,
    onSelectMember: (TeamMember) -> Unit,
    onLogoutMember: () -> Unit,
    onUpdateInvestigation: (caseId: String, status: String, notes: String, resolution: String, assignedMember: String?) -> Unit,
    onNavigate: (AppNavDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    var editingCase by remember { mutableStateOf<Complaint?>(null) }
    var noteInput by remember { mutableStateOf("") }
    var resolutionInput by remember { mutableStateOf("") }
    var updatedStatus by remember { mutableStateOf(Complaint.STATUS_INVESTIGATING) }
    var caseFilterTab by remember { mutableIntStateOf(0) } // 0: Assigned to Me, 1: All Active Inquiries

    // If no member is logged in, show Member Verification / Sign-in selector
    if (currentMember == null) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(CidNavyBackground)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CidNavyCard),
                    border = androidx.compose.foundation.BorderStroke(1.2.dp, CidYellowBright.copy(alpha = 0.7f))
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .background(CidYellowBright.copy(alpha = 0.15f), CircleShape)
                                    .border(1.dp, CidYellowBright, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Shield,
                                    contentDescription = "Detective Badge",
                                    tint = CidYellowBright,
                                    modifier = Modifier.size(26.dp)
                                )
                            }

                            Column {
                                Text(
                                    text = "CHODU CID Agent Terminal",
                                    color = CidTextPrimary,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Restricted to Approved Detectives Only",
                                    color = CidYellowBright,
                                    fontSize = 11.5.sp
                                )
                            }
                        }

                        Text(
                            text = "To access assigned cases and submit field investigation reports, select your official approved agent profile below. If you haven't been approved yet, submit an application in 'Join Team' and wait for Aryaveer's approval.",
                            color = CidTextSecondary,
                            fontSize = 12.5.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            item {
                Text(
                    text = "SELECT YOUR APPROVED AGENT DOSSIER",
                    color = CidYellowBright,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }

            if (approvedMembers.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = CidNavyCard),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CidNavyBorder)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Lock,
                                contentDescription = "Locked",
                                tint = CidTextMuted,
                                modifier = Modifier.size(32.dp)
                            )
                            Text(
                                text = "No Approved Members Found",
                                color = CidTextPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Only applicants approved by Leader Aryaveer can access this portal.",
                                color = CidTextSecondary,
                                fontSize = 12.sp
                            )
                            Button(
                                onClick = { onNavigate(AppNavDestination.JOIN_TEAM) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = CidYellowBright,
                                    contentColor = CidNavyDark
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Apply for Membership", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            items(approvedMembers) { member ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectMember(member) },
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
                                    .size(44.dp)
                                    .background(CidPurpleBadge.copy(alpha = 0.2f), CircleShape)
                                    .border(1.dp, CidPurpleBadge, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = member.fullName.take(2).uppercase(),
                                    color = CidPurpleBadge,
                                    fontSize = 15.sp,
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
                                            .background(CidYellowBright.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
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
                            }
                        }

                        Button(
                            onClick = { onSelectMember(member) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CidYellowBright,
                                contentColor = CidNavyDark
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Log In", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
        return
    }

    // When an approved member is logged in
    val myCases = complaints.filter { it.assignedMember == currentMember.fullName }
    val displayedCases = if (caseFilterTab == 0) myCases else complaints.filter { it.status != Complaint.STATUS_RESOLVED }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CidNavyBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(6.dp))

            // Agent Profile Badge Card
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
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .background(CidYellowBright.copy(alpha = 0.2f), CircleShape)
                                .border(1.5.dp, CidYellowBright, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Security,
                                contentDescription = "Active Agent",
                                tint = CidYellowBright,
                                modifier = Modifier.size(26.dp)
                            )
                        }

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Agent ${currentMember.fullName}",
                                    color = CidTextPrimary,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .background(CidYellowBright, RoundedCornerShape(4.dp))
                                        .padding(horizontal = 6.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        text = currentMember.badgeNumber,
                                        color = CidNavyDark,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                }
                            }
                            Text(
                                text = "${currentMember.role} • ${currentMember.course}",
                                color = CidTextSecondary,
                                fontSize = 11.5.sp
                            )
                            Text(
                                text = "Leader: Aryaveer • Squad Member",
                                color = CidTextMuted,
                                fontSize = 10.5.sp
                            )
                        }
                    }

                    IconButton(
                        onClick = onLogoutMember,
                        modifier = Modifier
                            .size(36.dp)
                            .background(CidNavySurface, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ExitToApp,
                            contentDescription = "Switch Profile",
                            tint = CidTextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        // Role Limitations Notice (as requested by user prompt)
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CidNavySurface, RoundedCornerShape(10.dp))
                    .border(0.8.dp, CidNavyBorder, RoundedCornerShape(10.dp))
                    .padding(12.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Field Detective Permissions:",
                        color = CidYellowBright,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "✓ You can inspect assigned cases, submit investigation logs, and mark cases resolved.\n✗ Member approvals, rejections, and removals are strictly restricted to Leader Aryaveer.",
                        color = CidTextSecondary,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // Tabs: My Assigned Cases vs All Active Cases
        item {
            TabRow(
                selectedTabIndex = caseFilterTab,
                containerColor = CidNavyCard,
                contentColor = CidYellowBright,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[caseFilterTab]),
                        color = CidYellowBright,
                        height = 3.dp
                    )
                },
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, CidNavyBorder, RoundedCornerShape(12.dp))
            ) {
                Tab(
                    selected = caseFilterTab == 0,
                    onClick = { caseFilterTab = 0 },
                    text = {
                        Text(
                            text = "Assigned to Me (${myCases.size})",
                            fontSize = 12.5.sp,
                            fontWeight = if (caseFilterTab == 0) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )

                Tab(
                    selected = caseFilterTab == 1,
                    onClick = { caseFilterTab = 1 },
                    text = {
                        Text(
                            text = "All Active Inquiries",
                            fontSize = 12.5.sp,
                            fontWeight = if (caseFilterTab == 1) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }
        }

        if (displayedCases.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(30.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (caseFilterTab == 0) "No cases currently assigned to you. Check 'All Active Inquiries' to take up an open lead!" else "No active cases under investigation.",
                        color = CidTextMuted,
                        fontSize = 12.5.sp
                    )
                }
            }
        }

        items(displayedCases) { complaint ->
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
                        Text(
                            text = complaint.caseId,
                            color = CidYellowBright,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            CaseStatusBadge(status = complaint.status)
                            UrgencyBadge(urgency = complaint.urgency)
                        }
                    }

                    Text(
                        text = complaint.title,
                        color = CidTextPrimary,
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = complaint.description,
                        color = CidTextSecondary,
                        fontSize = 12.5.sp,
                        lineHeight = 18.sp
                    )

                    // Photo Evidence if any
                    if (complaint.imageUri != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .clip(RoundedCornerShape(8.dp))
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(model = complaint.imageUri),
                                contentDescription = "Evidence Photo",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }

                    // Existing Notes
                    if (complaint.investigationNotes.isNotBlank()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(CidNavyDark, RoundedCornerShape(8.dp))
                                .padding(8.dp)
                        ) {
                            Text(
                                text = "Current Notes: ${complaint.investigationNotes}",
                                color = CidBlueInfo,
                                fontSize = 11.5.sp
                            )
                        }
                    }

                    // Member Action Button: Update Investigation Notes
                    Button(
                        onClick = {
                            editingCase = complaint
                            noteInput = complaint.investigationNotes
                            resolutionInput = complaint.resolutionDetails
                            updatedStatus = complaint.status
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CidYellowBright,
                            contentColor = CidNavyDark
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(imageVector = Icons.Filled.Edit, contentDescription = "Update", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Add Notes & Update Progress", fontWeight = FontWeight.Bold, fontSize = 12.5.sp)
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }

    // Modal for Detective to Update Notes & Resolution
    if (editingCase != null) {
        val target = editingCase!!
        AlertDialog(
            onDismissRequest = { editingCase = null },
            containerColor = CidNavyCard,
            title = {
                Text(
                    text = "Update Case: ${target.caseId}",
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
                        Text(target.title, color = CidTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }

                    // Status Choices
                    item {
                        Text("Update Status:", color = CidTextSecondary, fontSize = 11.5.sp)
                        val choices = listOf(Complaint.STATUS_INVESTIGATING, Complaint.STATUS_IN_PROGRESS, Complaint.STATUS_RESOLVED)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            choices.forEach { st ->
                                val isSelected = updatedStatus == st
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) CidYellowBright else CidNavyDark)
                                        .border(1.dp, if (isSelected) CidYellowBright else CidNavyBorder, RoundedCornerShape(8.dp))
                                        .clickable { updatedStatus = st }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (st == Complaint.STATUS_RESOLVED) "Mark Resolved" else st,
                                        color = if (isSelected) CidNavyDark else CidTextSecondary,
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    // Notes
                    item {
                        OutlinedTextField(
                            value = noteInput,
                            onValueChange = { noteInput = it },
                            label = { Text("Investigation Log / Saboot & Bayan") },
                            placeholder = { Text("What did you uncover during the inspection?") },
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
                            value = resolutionInput,
                            onValueChange = { resolutionInput = it },
                            label = { Text("Action Taken for Leader Review") },
                            placeholder = { Text("How was the problem solved?") },
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
                        onUpdateInvestigation(
                            target.caseId,
                            updatedStatus,
                            noteInput,
                            resolutionInput,
                            target.assignedMember ?: currentMember.fullName
                        )
                        editingCase = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CidYellowBright,
                        contentColor = CidNavyDark
                    )
                ) {
                    Text("Submit for Aryaveer's Review", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { editingCase = null },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = CidTextSecondary),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CidNavyBorder)
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}
