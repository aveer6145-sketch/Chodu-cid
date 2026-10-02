package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.AssignmentInd
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
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
fun JoinTeamScreen(
    onApplyMembership: (fullName: String, course: String, branch: String, onSuccess: (String) -> Unit) -> Unit,
    onTrackApplication: (String) -> Unit,
    onNavigate: (AppNavDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    // Strictly ONLY these 3 required fields as per instructions
    var fullName by remember { mutableStateOf("") }
    var course by remember { mutableStateOf("") }
    var branch by remember { mutableStateOf("") }

    var formError by remember { mutableStateOf<String?>(null) }
    var submittedAppId by remember { mutableStateOf<String?>(null) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CidNavyBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(6.dp))
            // Join Banner
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CidNavyCard),
                border = androidx.compose.foundation.BorderStroke(1.2.dp, CidYellowBright.copy(alpha = 0.6f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(CidYellowBright.copy(alpha = 0.15f), CircleShape)
                                .border(1.dp, CidYellowBright, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.GroupAdd,
                                contentDescription = "Join Our Team",
                                tint = CidYellowBright,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Column {
                            Text(
                                text = "Join Our Team",
                                color = CidTextPrimary,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Become an Official CHODU CID Investigator",
                                color = CidYellowBright,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Text(
                        text = "Are you passionate about student welfare and campus justice? Aryaveer is scouting dedicated detectives to investigate hostel conditions, exam irregularities, canteen pricing, and faculty issues. Submit your application below.",
                        color = CidTextSecondary,
                        fontSize = 12.5.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // Privacy & Zero Bloat Notice
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CidNavySurface, RoundedCornerShape(10.dp))
                    .border(0.8.dp, CidNavyBorder, RoundedCornerShape(10.dp))
                    .padding(12.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Security,
                        contentDescription = "Privacy Shield",
                        tint = CidYellowBright,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Zero Spam Protocol: No email, phone number, or password required. Only your name, course, and branch are needed.",
                        color = CidTextSecondary,
                        fontSize = 11.5.sp
                    )
                }
            }
        }

        // The 3 Required Fields Form
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
                        text = "CHODU CID Membership Application Form",
                        color = CidYellowBright,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    // Field 1: Full Name
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text("1. Full Name *") },
                        placeholder = { Text("e.g. Vikram Malhotra") },
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

                    // Field 2: Course
                    OutlinedTextField(
                        value = course,
                        onValueChange = { course = it },
                        label = { Text("2. Course *") },
                        placeholder = { Text("e.g. B.Tech / BCA / MBA / B.Com") },
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

                    // Field 3: Branch
                    OutlinedTextField(
                        value = branch,
                        onValueChange = { branch = it },
                        label = { Text("3. Branch *") },
                        placeholder = { Text("e.g. Computer Science / Electrical / Finance") },
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

                    // Approval workflow reminder
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(CidNavyDark, RoundedCornerShape(8.dp))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "Note: Aryaveer reviews every applicant personally. Once approved, you will receive an official investigator badge and access to the Team Member Dashboard.",
                            color = CidTextMuted,
                            fontSize = 11.sp,
                            lineHeight = 16.sp
                        )
                    }

                    if (formError != null) {
                        Text(
                            text = formError ?: "",
                            color = CidRedAlert,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Apply for Membership Button
                    Button(
                        onClick = {
                            if (fullName.isBlank()) {
                                formError = "Please enter your Full Name"
                            } else if (course.isBlank()) {
                                formError = "Please enter your Course"
                            } else if (branch.isBlank()) {
                                formError = "Please enter your Branch"
                            } else {
                                formError = null
                                onApplyMembership(fullName, course, branch) { appId ->
                                    submittedAppId = appId
                                    showSuccessDialog = true
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CidYellowBright,
                            contentColor = CidNavyDark
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Policy,
                                contentDescription = "Apply for Membership"
                            )
                            Text(
                                text = "Apply for Membership",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                }
            }
        }

        // Leader Authority Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = CidNavySurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, CidNavyBorder)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .border(1.5.dp, CidYellowBright, CircleShape)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_leader_badge),
                            contentDescription = "Leader Approval",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Column {
                        Text(
                            text = "Approval Process by Aryaveer",
                            color = CidTextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Leader verifies student credentials to maintain CID integrity. Decisions are made within 24 hours.",
                            color = CidTextSecondary,
                            fontSize = 11.5.sp
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Success Dialog on Application Submission
    if (showSuccessDialog && submittedAppId != null) {
        AlertDialog(
            onDismissRequest = {
                showSuccessDialog = false
                submittedAppId?.let { onTrackApplication(it) }
                onNavigate(AppNavDestination.TRACK_STATUS)
            },
            containerColor = CidNavyCard,
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(CidGreenSuccess.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = "Application Sent",
                            tint = CidGreenSuccess,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Text(
                        text = "Application Submitted!",
                        color = CidTextPrimary,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Your application has been sent to Aryaveer. Wait for approval!",
                        color = CidYellowBright,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(CidNavyDark, RoundedCornerShape(8.dp))
                            .border(1.dp, CidYellowBright, RoundedCornerShape(8.dp))
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "APPLICATION TRACKING ID",
                                color = CidTextMuted,
                                fontSize = 10.sp,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = submittedAppId ?: "",
                                color = CidYellowBright,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = "Status: Pending Approval",
                                color = CidOrangeWarning,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Text(
                        text = "As per CID policy, no applicant becomes a member without Aryaveer's personal approval. Check back anytime using your Application ID!",
                        color = CidTextSecondary,
                        fontSize = 12.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val aid = submittedAppId
                        showSuccessDialog = false
                        if (aid != null) {
                            onTrackApplication(aid)
                        }
                        onNavigate(AppNavDestination.TRACK_STATUS)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CidYellowBright,
                        contentColor = CidNavyDark
                    )
                ) {
                    Text("Check Status", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        showSuccessDialog = false
                        onNavigate(AppNavDestination.HOME)
                    },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = CidTextSecondary),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CidNavyBorder)
                ) {
                    Text("Home")
                }
            }
        )
    }
}
