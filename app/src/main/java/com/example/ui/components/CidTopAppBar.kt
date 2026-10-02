package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CidNavyBorder
import com.example.ui.theme.CidNavyDark
import com.example.ui.theme.CidNavySurface
import com.example.ui.theme.CidRedAlert
import com.example.ui.theme.CidTextPrimary
import com.example.ui.theme.CidTextSecondary
import com.example.ui.theme.CidYellowBright

@Composable
fun CidTopAppBar(
    isLeaderLoggedIn: Boolean,
    pendingAppsCount: Int,
    onAdminClick: () -> Unit,
    onTrackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(CidNavyDark)
            .statusBarsPadding()
            .border(width = 0.5.dp, color = CidNavyBorder)
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Logo & Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(CidYellowBright)
                        .border(1.5.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "CHODU CID Magnifier",
                        tint = CidNavyDark,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "CHODU ",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "CID",
                            color = CidYellowBright,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                    }
                    Text(
                        text = "College Ki Har Problem Ka CID!",
                        color = CidTextSecondary,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Action Icons
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Quick Case Track shortcut
                IconButton(
                    onClick = onTrackClick,
                    modifier = Modifier.size(38.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search Case",
                        tint = CidTextSecondary
                    )
                }

                // Leader / Admin Access Button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            if (isLeaderLoggedIn) CidYellowBright.copy(alpha = 0.2f)
                            else CidNavySurface
                        )
                        .border(
                            1.dp,
                            if (isLeaderLoggedIn) CidYellowBright else CidNavyBorder,
                            RoundedCornerShape(20.dp)
                        )
                        .clickable { onAdminClick() }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        if (isLeaderLoggedIn) {
                            Icon(
                                imageVector = Icons.Filled.Shield,
                                contentDescription = "Aryaveer Chief",
                                tint = CidYellowBright,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "ARYAVEER",
                                color = CidYellowBright,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            if (pendingAppsCount > 0) {
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
                        } else {
                            Icon(
                                imageVector = Icons.Filled.Lock,
                                contentDescription = "Admin Login",
                                tint = CidTextSecondary,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "LEADER",
                                color = CidTextSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}
