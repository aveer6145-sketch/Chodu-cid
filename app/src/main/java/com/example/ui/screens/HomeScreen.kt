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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalPolice
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.Complaint
import com.example.ui.components.CaseStatusBadge
import com.example.ui.components.StatCounterCard
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
import com.example.ui.viewmodel.AppNavDestination

@Composable
fun HomeScreen(
    totalProblemsCount: Int,
    resolvedCasesCount: Int,
    investigatingCount: Int,
    approvedTeamCount: Int,
    recentComplaints: List<Complaint>,
    onNavigate: (AppNavDestination) -> Unit,
    onTrackCaseSearch: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var quickTrackInput by remember { mutableStateOf("") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CidNavyBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(6.dp))
            // Hero Banner Card with Generated CID Art
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, CidNavyBorder, RoundedCornerShape(16.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_cid_hero),
                    contentDescription = "CHODU CID Hero Banner",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f),
                    contentScale = ContentScale.Crop
                )

                // Dark Gradient overlay for contrast
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    CidNavyDark.copy(alpha = 0.85f),
                                    CidNavyDark
                                )
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .background(CidYellowBright, RoundedCornerShape(4.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "OFFICIAL CAMPUS INVESTIGATION BUREAU",
                            color = CidNavyDark,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.8.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "College Ki Har Problem Ka CID!",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }

        // Welcome Box & Purpose
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CidNavyCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, CidNavyBorder)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Policy,
                            contentDescription = "CHODU CID",
                            tint = CidYellowBright,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "Welcome to CHODU CID",
                            color = CidYellowBright,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = "College Ki Har Problem Ka CID! Whether it's mess canteen issues, hostel water breakdown, unfair faculty fines, broken lab equipment, or exam hall blunders — report anonymously or with your name. Our student detective unit investigates with solid evidence!",
                        color = CidTextSecondary,
                        fontSize = 13.sp,
                        lineHeight = 19.sp
                    )
                }
            }
        }

        // Main Leader Section: Aryaveer
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CidNavySurface),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, CidYellowBright.copy(alpha = 0.6f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Leader Avatar / Badge
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, CidYellowBright, CircleShape)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.img_leader_badge),
                                    contentDescription = "Aryaveer - Chief of CID",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }

                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Aryaveer",
                                        color = CidTextPrimary,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Icon(
                                        imageVector = Icons.Filled.VerifiedUser,
                                        contentDescription = "Verified Leader",
                                        tint = CidYellowBright,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Text(
                                    text = "Main Leader & CID Bureau Chief",
                                    color = CidYellowBright,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "Final Authority on Cases & Team Access",
                                    color = CidTextMuted,
                                    fontSize = 10.5.sp
                                )
                            }
                        }
                    }

                    // Leader's Mission Quote
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(CidNavyDark, RoundedCornerShape(10.dp))
                            .border(0.8.dp, CidNavyBorder, RoundedCornerShape(10.dp))
                            .padding(12.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Campaign,
                                contentDescription = "Aryaveer Quote",
                                tint = CidYellowBright,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "\"Koi bhi student pareshaan nahi hoga. Har complaint ki forensic inquiry hogi aur solution nikala jayega! Daya, darwaza todo!\"",
                                color = CidTextSecondary,
                                fontSize = 12.sp,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                            )
                        }
                    }
                }
            }
        }

        // Two Large Action Buttons as requested
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Button 1: Report a Problem
                Button(
                    onClick = { onNavigate(AppNavDestination.REPORT_PROBLEM) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CidYellowBright,
                        contentColor = CidNavyDark
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ReportProblem,
                            contentDescription = "Report Problem",
                            modifier = Modifier.size(22.dp)
                        )
                        Text(
                            text = "1. Report a Problem",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }

                // Button 2: Apply to Join CHODU CID
                Button(
                    onClick = { onNavigate(AppNavDestination.JOIN_TEAM) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CidNavyCard,
                        contentColor = CidYellowBright
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, CidYellowBright)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.GroupAdd,
                            contentDescription = "Apply to Join CHODU CID",
                            modifier = Modifier.size(22.dp)
                        )
                        Text(
                            text = "2. Apply to Join CHODU CID",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Live Statistics Counter (Total reported problems, resolved cases, approved team members)
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "CID BUREAU LIVE METRICS",
                    color = CidYellowBright,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCounterCard(
                        title = "Reported Cases",
                        count = totalProblemsCount,
                        icon = Icons.Filled.ReportProblem,
                        accentColor = CidOrangeWarning,
                        modifier = Modifier.weight(1f),
                        subtitle = "$investigatingCount investigating"
                    )

                    StatCounterCard(
                        title = "Resolved Cases",
                        count = resolvedCasesCount,
                        icon = Icons.Filled.CheckCircle,
                        accentColor = CidGreenSuccess,
                        modifier = Modifier.weight(1f),
                        subtitle = "Issues fixed"
                    )

                    StatCounterCard(
                        title = "Team Members",
                        count = approvedTeamCount,
                        icon = Icons.Filled.Group,
                        accentColor = CidYellowBright,
                        modifier = Modifier.weight(1f),
                        subtitle = "Aryaveer approved"
                    )
                }
            }
        }

        // Quick Case Search Tracker
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = CidNavyCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, CidNavyBorder)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Track Complaint Status",
                        color = CidTextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Enter your Case ID (e.g., CID-2026-101) to view real-time investigation progress.",
                        color = CidTextSecondary,
                        fontSize = 12.sp
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = quickTrackInput,
                            onValueChange = { quickTrackInput = it.uppercase() },
                            placeholder = { Text("Enter Case ID...") },
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
                                if (quickTrackInput.isNotBlank()) {
                                    onTrackCaseSearch(quickTrackInput)
                                    onNavigate(AppNavDestination.TRACK_STATUS)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CidYellowBright,
                                contentColor = CidNavyDark
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Track", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Recent Solved Cases & Live Intel
        item {
            Text(
                text = "RECENT INVESTIGATION DOSSIERS",
                color = CidYellowBright,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }

        items(recentComplaints.take(3)) { complaint ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onTrackCaseSearch(complaint.caseId)
                        onNavigate(AppNavDestination.TRACK_STATUS)
                    },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = CidNavyCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, CidNavyBorder)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = complaint.caseId,
                            color = CidYellowBright,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black
                        )
                        CaseStatusBadge(status = complaint.status)
                    }

                    Text(
                        text = complaint.title,
                        color = CidTextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Category: ${complaint.category} • ${complaint.course}",
                            color = CidTextSecondary,
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
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
