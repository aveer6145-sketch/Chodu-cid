package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Complaint
import com.example.data.model.TeamApplication
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
import com.example.ui.theme.CidRedAlert
import com.example.ui.theme.CidTextMuted
import com.example.ui.theme.CidTextPrimary
import com.example.ui.theme.CidTextSecondary
import com.example.ui.theme.CidYellowBright
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TrackCaseScreen(
    searchedCaseId: String,
    trackedCase: Complaint?,
    searchedAppId: String,
    trackedApplication: TeamApplication?,
    isSearching: Boolean,
    onSearchCase: (String) -> Unit,
    onSearchApplication: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Case, 1: Membership Application
    var inputQuery by remember { mutableStateOf("") }

    LaunchedEffect(searchedCaseId, searchedAppId) {
        if (searchedCaseId.isNotBlank() && selectedTab == 0) {
            inputQuery = searchedCaseId
        } else if (searchedAppId.isNotBlank() && selectedTab == 1) {
            inputQuery = searchedAppId
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CidNavyBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(6.dp))

            // Switch Tabs
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = CidNavyCard,
                contentColor = CidYellowBright,
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
                Tab(
                    selected = selectedTab == 0,
                    onClick = {
                        selectedTab = 0
                        inputQuery = searchedCaseId
                    },
                    text = {
                        Text(
                            text = "Track Problem Case",
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 12.5.sp
                        )
                    }
                )

                Tab(
                    selected = selectedTab == 1,
                    onClick = {
                        selectedTab = 1
                        inputQuery = searchedAppId
                    },
                    text = {
                        Text(
                            text = "Track Application",
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 12.5.sp
                        )
                    }
                )
            }
        }

        // Search Input Box
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
                        text = if (selectedTab == 0) "Enter Investigation Case ID" else "Enter Membership Application ID",
                        color = CidYellowBright,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = inputQuery,
                            onValueChange = { inputQuery = it.uppercase() },
                            placeholder = {
                                Text(
                                    if (selectedTab == 0) "e.g. CID-2026-101" else "e.g. APP-CID-201",
                                    color = CidTextMuted
                                )
                            },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CidYellowBright,
                                unfocusedBorderColor = CidNavyBorder,
                                focusedTextColor = CidTextPrimary,
                                unfocusedTextColor = CidTextPrimary,
                                focusedContainerColor = CidNavyDark,
                                unfocusedContainerColor = CidNavyDark
                            ),
                            modifier = Modifier.weight(1f)
                        )

                        Button(
                            onClick = {
                                if (selectedTab == 0) onSearchCase(inputQuery)
                                else onSearchApplication(inputQuery)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CidYellowBright,
                                contentColor = CidNavyDark
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = "Search"
                            )
                        }
                    }
                }
            }
        }

        // Loading
        if (isSearching) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(30.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = CidYellowBright)
                }
            }
        }

        // CASE RESULT
        if (selectedTab == 0) {
            if (trackedCase != null) {
                item {
                    val dateFormatted = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
                        .format(Date(trackedCase.createdAt))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = CidNavyCard),
                        border = androidx.compose.foundation.BorderStroke(1.2.dp, CidYellowBright.copy(alpha = 0.8f))
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            // Header: Case ID and Status Badge
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "CASE FILE",
                                        color = CidTextMuted,
                                        fontSize = 10.sp,
                                        letterSpacing = 1.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = trackedCase.caseId,
                                        color = CidYellowBright,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                }

                                CaseStatusBadge(status = trackedCase.status)
                            }

                            UrgencyBadge(urgency = trackedCase.urgency)

                            // Title & Description
                            Text(
                                text = trackedCase.title,
                                color = CidTextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = trackedCase.description,
                                color = CidTextSecondary,
                                fontSize = 13.sp,
                                lineHeight = 19.sp
                            )

                            // Metadata Grid
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(CidNavyDark, RoundedCornerShape(10.dp))
                                    .padding(12.dp)
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Category:", color = CidTextMuted, fontSize = 12.sp)
                                        Text(trackedCase.category, color = CidTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                                    }
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Course & Branch:", color = CidTextMuted, fontSize = 12.sp)
                                        Text("${trackedCase.course} - ${trackedCase.branch}", color = CidTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                                    }
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Reporter:", color = CidTextMuted, fontSize = 12.sp)
                                        Text(trackedCase.studentName, color = CidTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                                    }
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Filed On:", color = CidTextMuted, fontSize = 12.sp)
                                        Text(dateFormatted, color = CidTextSecondary, fontSize = 11.5.sp)
                                    }
                                    if (trackedCase.assignedMember != null) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text("Investigator Assigned:", color = CidYellowBright, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                            Text(trackedCase.assignedMember, color = CidYellowBright, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }

                            // Investigation Notes Section
                            if (trackedCase.investigationNotes.isNotBlank()) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(CidNavySurface, RoundedCornerShape(10.dp))
                                        .border(1.dp, CidBlueInfo.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                                        .padding(12.dp)
                                ) {
                                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.Policy,
                                                contentDescription = "Investigation Notes",
                                                tint = CidBlueInfo,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Text(
                                                text = "CID Investigation Log & Bayan:",
                                                color = CidBlueInfo,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        Text(
                                            text = trackedCase.investigationNotes,
                                            color = CidTextPrimary,
                                            fontSize = 12.5.sp,
                                            lineHeight = 18.sp
                                        )
                                    }
                                }
                            }

                            // Official Resolution Section
                            if (trackedCase.resolutionDetails.isNotBlank()) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(CidGreenSuccess.copy(alpha = 0.12f), RoundedCornerShape(10.dp))
                                        .border(1.dp, CidGreenSuccess.copy(alpha = 0.6f), RoundedCornerShape(10.dp))
                                        .padding(12.dp)
                                ) {
                                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.CheckCircle,
                                                contentDescription = "Resolution Details",
                                                tint = CidGreenSuccess,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Text(
                                                text = "Final Action Taken & Resolution:",
                                                color = CidGreenSuccess,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        Text(
                                            text = trackedCase.resolutionDetails,
                                            color = CidTextPrimary,
                                            fontSize = 12.5.sp,
                                            lineHeight = 18.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (searchedCaseId.isNotBlank() && !isSearching) {
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
                                imageVector = Icons.Filled.Info,
                                contentDescription = "Not found",
                                tint = CidOrangeWarning,
                                modifier = Modifier.size(36.dp)
                            )
                            Text(
                                text = "No Case Found For \"$searchedCaseId\"",
                                color = CidTextPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Please verify your Case ID format (e.g., CID-2026-101) or check the home page for recent cases.",
                                color = CidTextSecondary,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }

        // APPLICATION RESULT
        if (selectedTab == 1) {
            if (trackedApplication != null) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = CidNavyCard),
                        border = androidx.compose.foundation.BorderStroke(1.2.dp, CidYellowBright.copy(alpha = 0.8f))
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "MEMBERSHIP APPLICATION",
                                        color = CidTextMuted,
                                        fontSize = 10.sp,
                                        letterSpacing = 1.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = trackedApplication.applicationId,
                                        color = CidYellowBright,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                }

                                CaseStatusBadge(status = trackedApplication.status)
                            }

                            // Applicant Details
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(CidNavyDark, RoundedCornerShape(10.dp))
                                    .padding(12.dp)
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Applicant Name:", color = CidTextMuted, fontSize = 12.sp)
                                        Text(trackedApplication.fullName, color = CidTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                    }
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Course:", color = CidTextMuted, fontSize = 12.sp)
                                        Text(trackedApplication.course, color = CidTextPrimary, fontSize = 12.sp)
                                    }
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Branch:", color = CidTextMuted, fontSize = 12.sp)
                                        Text(trackedApplication.branch, color = CidTextPrimary, fontSize = 12.sp)
                                    }
                                }
                            }

                            // Status Explanations
                            when (trackedApplication.status) {
                                TeamApplication.STATUS_PENDING -> {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(CidYellowBright.copy(alpha = 0.12f), RoundedCornerShape(8.dp))
                                            .border(1.dp, CidYellowBright.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                            .padding(12.dp)
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.HourglassTop,
                                                contentDescription = "Pending",
                                                tint = CidYellowBright
                                            )
                                            Text(
                                                text = "Your application has been sent to Aryaveer. Wait for approval!",
                                                color = CidYellowBright,
                                                fontSize = 12.5.sp,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                    }
                                }
                                TeamApplication.STATUS_APPROVED -> {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(CidGreenSuccess.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                                            .border(1.dp, CidGreenSuccess, RoundedCornerShape(8.dp))
                                            .padding(12.dp)
                                    ) {
                                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                            Row(
                                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Filled.CheckCircle,
                                                    contentDescription = "Approved",
                                                    tint = CidGreenSuccess
                                                )
                                                Text(
                                                    text = "Congratulations! Approved by Aryaveer.",
                                                    color = CidGreenSuccess,
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                            Text(
                                                text = "You are now an official CHODU CID investigator! Go to the 'CID Bureau' tab to log in with your name and access investigator cases.",
                                                color = CidTextPrimary,
                                                fontSize = 12.sp
                                            )
                                            if (trackedApplication.leaderRemarks != null) {
                                                Text(
                                                    text = "Aryaveer's Note: ${trackedApplication.leaderRemarks}",
                                                    color = CidYellowBright,
                                                    fontSize = 11.5.sp
                                                )
                                            }
                                        }
                                    }
                                }
                                TeamApplication.STATUS_REJECTED -> {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(CidRedAlert.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                                            .border(1.dp, CidRedAlert, RoundedCornerShape(8.dp))
                                            .padding(12.dp)
                                    ) {
                                        Text(
                                            text = "Application not approved: ${trackedApplication.leaderRemarks ?: "Candidate did not meet current bureau quota."}",
                                            color = CidRedAlert,
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (searchedAppId.isNotBlank() && !isSearching) {
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
                                imageVector = Icons.Filled.Info,
                                contentDescription = "Not found",
                                tint = CidOrangeWarning,
                                modifier = Modifier.size(36.dp)
                            )
                            Text(
                                text = "No Application Found For \"$searchedAppId\"",
                                color = CidTextPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Check the Application ID given upon submitting (e.g., APP-CID-101).",
                                color = CidTextSecondary,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
