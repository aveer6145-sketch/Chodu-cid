package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.data.model.Complaint
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportProblemScreen(
    onSubmitProblem: (
        studentName: String,
        course: String,
        branch: String,
        category: String,
        title: String,
        description: String,
        imageUri: String?,
        urgency: String,
        onSuccess: (String) -> Unit
    ) -> Unit,
    onTrackCase: (String) -> Unit,
    onNavigate: (AppNavDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    var studentName by remember { mutableStateOf("") }
    var isAnonymous by remember { mutableStateOf(false) }
    var course by remember { mutableStateOf("") }
    var branch by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(Complaint.CATEGORIES.first()) }
    var isCategoryDropdownExpanded by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var urgency by remember { mutableStateOf(Complaint.URGENCY_NORMAL) }

    var formError by remember { mutableStateOf<String?>(null) }
    var registeredCaseId by remember { mutableStateOf<String?>(null) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    // Photo picker launcher (Android Photo Picker - no dangerous permission needed)
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
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
            // Screen Header
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = CidNavyCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, CidYellowBright.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .background(CidYellowBright.copy(alpha = 0.15f), CircleShape)
                            .border(1.dp, CidYellowBright, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ReportProblem,
                            contentDescription = "Report Problem",
                            tint = CidYellowBright,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "Register Student Complaint",
                            color = CidTextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "No login required • Free, secure & confidential",
                            color = CidTextSecondary,
                            fontSize = 11.5.sp
                        )
                    }
                }
            }
        }

        // Student Info Section
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = CidNavyCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, CidNavyBorder)
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
                        Text(
                            text = "1. Student Details",
                            color = CidYellowBright,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )

                        // Anonymous toggle chip
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (isAnonymous) CidYellowBright else CidNavySurface)
                                .border(1.dp, if (isAnonymous) CidYellowBright else CidNavyBorder, RoundedCornerShape(20.dp))
                                .clickable {
                                    isAnonymous = !isAnonymous
                                    if (isAnonymous) studentName = "Anonymous / गुप्त खबरी"
                                    else studentName = ""
                                }
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = if (isAnonymous) "✓ Anonymous (Secret)" else "+ Stay Anonymous",
                                color = if (isAnonymous) CidNavyDark else CidTextSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    // Student Name (Optional)
                    OutlinedTextField(
                        value = studentName,
                        onValueChange = {
                            studentName = it
                            if (isAnonymous && it.isNotBlank() && it != "Anonymous / गुप्त खबरी") {
                                isAnonymous = false
                            }
                        },
                        label = { Text("Student Name (Optional)") },
                        placeholder = { Text("e.g., Aman Singh or leave empty") },
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

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Course
                        OutlinedTextField(
                            value = course,
                            onValueChange = { course = it },
                            label = { Text("Course *") },
                            placeholder = { Text("e.g. B.Tech / BCA") },
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

                        // Branch
                        OutlinedTextField(
                            value = branch,
                            onValueChange = { branch = it },
                            label = { Text("Branch *") },
                            placeholder = { Text("e.g. CSE / ECE / Civil") },
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
                    }
                }
            }
        }

        // Problem Category & Urgency
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = CidNavyCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, CidNavyBorder)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "2. Category & Urgency",
                        color = CidYellowBright,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )

                    // Dropdown for Category
                    ExposedDropdownMenuBox(
                        expanded = isCategoryDropdownExpanded,
                        onExpandedChange = { isCategoryDropdownExpanded = !isCategoryDropdownExpanded }
                    ) {
                        OutlinedTextField(
                            value = category,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Problem Category *") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCategoryDropdownExpanded) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CidYellowBright,
                                unfocusedBorderColor = CidNavyBorder,
                                focusedTextColor = CidTextPrimary,
                                unfocusedTextColor = CidTextPrimary,
                                focusedContainerColor = CidNavyDark,
                                unfocusedContainerColor = CidNavyDark
                            ),
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = isCategoryDropdownExpanded,
                            onDismissRequest = { isCategoryDropdownExpanded = false },
                            modifier = Modifier.background(CidNavyCard)
                        ) {
                            Complaint.CATEGORIES.forEach { cat ->
                                DropdownMenuItem(
                                    text = { Text(cat, color = CidTextPrimary) },
                                    onClick = {
                                        category = cat
                                        isCategoryDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    // Urgency Selector
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "Priority Level:",
                            color = CidTextSecondary,
                            fontSize = 12.sp
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(Complaint.URGENCY_NORMAL, Complaint.URGENCY_HIGH, Complaint.URGENCY_CRITICAL).forEach { level ->
                                val isSelected = urgency == level
                                val activeColor = when (level) {
                                    Complaint.URGENCY_CRITICAL -> CidRedAlert
                                    Complaint.URGENCY_HIGH -> CidOrangeWarning
                                    else -> CidYellowBright
                                }

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) activeColor.copy(alpha = 0.2f) else CidNavyDark)
                                        .border(1.dp, if (isSelected) activeColor else CidNavyBorder, RoundedCornerShape(8.dp))
                                        .clickable { urgency = level }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = level,
                                        color = if (isSelected) activeColor else CidTextSecondary,
                                        fontSize = 11.5.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Problem Details
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = CidNavyCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, CidNavyBorder)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "3. Problem Description & Evidence",
                        color = CidYellowBright,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )

                    // Title
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Problem Title *") },
                        placeholder = { Text("e.g. Canteen selling expired snacks") },
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

                    // Description
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Detailed Problem Description *") },
                        placeholder = { Text("Provide details: location, date, time, faculty/staff involved, who was affected, etc.") },
                        minLines = 4,
                        maxLines = 8,
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

                    // Optional Image Upload / Attachment
                    Text(
                        text = "Optional Photographic Evidence:",
                        color = CidTextSecondary,
                        fontSize = 12.sp
                    )

                    if (selectedImageUri != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .border(1.dp, CidYellowBright, RoundedCornerShape(10.dp))
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(model = selectedImageUri),
                                contentDescription = "Attached evidence",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )

                            IconButton(
                                onClick = { selectedImageUri = null },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(6.dp)
                                    .size(30.dp)
                                    .background(CidNavyDark.copy(alpha = 0.8f), CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = "Remove photo",
                                    tint = CidRedAlert,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    } else {
                        OutlinedButton(
                            onClick = { photoPickerLauncher.launch("image/*") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CidNavyBorder)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.AddPhotoAlternate,
                                    contentDescription = "Attach Evidence Photo",
                                    tint = CidYellowBright
                                )
                                Text(
                                    text = "Attach Evidence Photo (Optional)",
                                    color = CidTextSecondary,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Error message if any
        if (formError != null) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CidRedAlert.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                        .border(1.dp, CidRedAlert, RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        text = formError ?: "",
                        color = CidRedAlert,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Submit Button
        item {
            Button(
                onClick = {
                    if (course.isBlank()) {
                        formError = "Please enter your Course (e.g. B.Tech, BCA)"
                    } else if (branch.isBlank()) {
                        formError = "Please enter your Branch (e.g. CSE, ECE)"
                    } else if (title.isBlank()) {
                        formError = "Please enter a Problem Title"
                    } else if (description.isBlank()) {
                        formError = "Please provide detailed description of the problem"
                    } else {
                        formError = null
                        val effectiveName = if (isAnonymous || studentName.isBlank()) "Anonymous / गुप्त खबरी" else studentName
                        onSubmitProblem(
                            effectiveName,
                            course,
                            branch,
                            category,
                            title,
                            description,
                            selectedImageUri?.toString(),
                            urgency
                        ) { caseId ->
                            registeredCaseId = caseId
                            showSuccessDialog = true
                        }
                    }
                },
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
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.ReportProblem,
                        contentDescription = "Submit Problem",
                        tint = CidNavyDark
                    )
                    Text(
                        text = "Submit Problem to CHODU CID",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Success Dialog on Problem Registration
    if (showSuccessDialog && registeredCaseId != null) {
        AlertDialog(
            onDismissRequest = {
                showSuccessDialog = false
                registeredCaseId?.let { onTrackCase(it) }
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
                            contentDescription = "Success",
                            tint = CidGreenSuccess,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Text(
                        text = "Case Registered!",
                        color = CidTextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Your case has been registered with CHODU CID!",
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
                                text = "OFFICIAL CASE ID",
                                color = CidTextMuted,
                                fontSize = 10.sp,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = registeredCaseId ?: "",
                                color = CidYellowBright,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }

                    Text(
                        text = "Save this Case ID! Aryaveer and the CHODU CID squad have been notified. You can track investigation notes and resolution progress anytime in the Track tab.",
                        color = CidTextSecondary,
                        fontSize = 12.5.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val cid = registeredCaseId
                        showSuccessDialog = false
                        if (cid != null) {
                            onTrackCase(cid)
                        }
                        onNavigate(AppNavDestination.TRACK_STATUS)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CidYellowBright,
                        contentColor = CidNavyDark
                    )
                ) {
                    Text("Track Status Now", fontWeight = FontWeight.Bold)
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
                    Text("Go Home")
                }
            }
        )
    }
}
