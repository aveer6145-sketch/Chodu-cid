package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.CidNavyBorder
import com.example.ui.theme.CidNavyCard
import com.example.ui.theme.CidNavyDark
import com.example.ui.theme.CidNavySurface
import com.example.ui.theme.CidRedAlert
import com.example.ui.theme.CidTextMuted
import com.example.ui.theme.CidTextPrimary
import com.example.ui.theme.CidTextSecondary
import com.example.ui.theme.CidYellowBright

@Composable
fun LeaderLoginDialog(
    onDismiss: () -> Unit,
    onLogin: (String, (Boolean) -> Unit) -> Unit
) {
    var pinText by remember { mutableStateOf("") }
    var isPinVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(18.dp),
            color = CidNavyCard,
            border = androidx.compose.foundation.BorderStroke(1.dp, CidYellowBright.copy(alpha = 0.6f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Icon
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(CidYellowBright.copy(alpha = 0.15f), CircleShape)
                        .border(1.dp, CidYellowBright, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Security,
                        contentDescription = "Chief Aryaveer",
                        tint = CidYellowBright,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Aryaveer's Chief Terminal",
                    color = CidTextPrimary,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Restricted Area • CHODU CID High Command",
                    color = CidYellowBright,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Enter Aryaveer's secret access PIN to access the Leader Admin Dashboard.",
                    color = CidTextSecondary,
                    fontSize = 12.5.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(18.dp))

                OutlinedTextField(
                    value = pinText,
                    onValueChange = {
                        if (it.length <= 10) {
                            pinText = it
                            errorMessage = null
                        }
                    },
                    label = { Text("Chief Passcode / PIN") },
                    singleLine = true,
                    visualTransformation = if (isPinVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.NumberPassword,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            if (pinText.isNotBlank()) {
                                isLoading = true
                                onLogin(pinText) { success ->
                                    isLoading = false
                                    if (!success) {
                                        errorMessage = "Incorrect PIN. Access Denied!"
                                    }
                                }
                            }
                        }
                    ),
                    trailingIcon = {
                        IconButton(onClick = { isPinVisible = !isPinVisible }) {
                            Icon(
                                imageVector = if (isPinVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                contentDescription = "Toggle PIN",
                                tint = CidTextMuted
                            )
                        }
                    },
                    isError = errorMessage != null,
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

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = errorMessage ?: "",
                        color = CidRedAlert,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Friendly note for initial access
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                        .background(CidNavySurface, RoundedCornerShape(8.dp))
                        .padding(8.dp)
                ) {
                    Text(
                        text = "Confidential Seed PIN: 7007 (Leader can update PIN in Admin Settings).",
                        color = CidTextMuted,
                        fontSize = 11.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = CidTextSecondary
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CidNavyBorder)
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            if (pinText.isNotBlank()) {
                                isLoading = true
                                onLogin(pinText) { success ->
                                    isLoading = false
                                    if (!success) {
                                        errorMessage = "Incorrect PIN. Access Denied!"
                                    }
                                }
                            } else {
                                errorMessage = "Please enter Chief PIN"
                            }
                        },
                        enabled = !isLoading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CidYellowBright,
                            contentColor = CidNavyDark
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = if (isLoading) "Verifying..." else "Authorize",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
