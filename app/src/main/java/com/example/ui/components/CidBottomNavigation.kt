package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.outlined.GroupAdd
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ReportProblem
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CidNavyBorder
import com.example.ui.theme.CidNavyDark
import com.example.ui.theme.CidNavySurface
import com.example.ui.theme.CidRedAlert
import com.example.ui.theme.CidTextMuted
import com.example.ui.theme.CidTextSecondary
import com.example.ui.theme.CidYellowBright
import com.example.ui.viewmodel.AppNavDestination

@Composable
fun CidBottomNavigation(
    currentDestination: AppNavDestination,
    isLeaderLoggedIn: Boolean,
    pendingAppsCount: Int,
    onNavigate: (AppNavDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
            .fillMaxWidth()
            .border(width = 0.5.dp, color = CidNavyBorder)
            .navigationBarsPadding(),
        containerColor = CidNavyDark,
        tonalElevation = 8.dp
    ) {
        // Home
        NavigationBarItem(
            selected = currentDestination == AppNavDestination.HOME,
            onClick = { onNavigate(AppNavDestination.HOME) },
            icon = {
                Icon(
                    imageVector = if (currentDestination == AppNavDestination.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                    contentDescription = "Home"
                )
            },
            label = {
                Text(
                    text = "Home",
                    fontSize = 11.sp,
                    fontWeight = if (currentDestination == AppNavDestination.HOME) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CidNavyDark,
                selectedTextColor = CidYellowBright,
                indicatorColor = CidYellowBright,
                unselectedIconColor = CidTextSecondary,
                unselectedTextColor = CidTextMuted
            )
        )

        // Report Problem
        NavigationBarItem(
            selected = currentDestination == AppNavDestination.REPORT_PROBLEM,
            onClick = { onNavigate(AppNavDestination.REPORT_PROBLEM) },
            icon = {
                Icon(
                    imageVector = if (currentDestination == AppNavDestination.REPORT_PROBLEM) Icons.Filled.ReportProblem else Icons.Outlined.ReportProblem,
                    contentDescription = "Report Problem"
                )
            },
            label = {
                Text(
                    text = "Report",
                    fontSize = 11.sp,
                    fontWeight = if (currentDestination == AppNavDestination.REPORT_PROBLEM) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CidNavyDark,
                selectedTextColor = CidYellowBright,
                indicatorColor = CidYellowBright,
                unselectedIconColor = CidTextSecondary,
                unselectedTextColor = CidTextMuted
            )
        )

        // Track Status
        NavigationBarItem(
            selected = currentDestination == AppNavDestination.TRACK_STATUS,
            onClick = { onNavigate(AppNavDestination.TRACK_STATUS) },
            icon = {
                Icon(
                    imageVector = if (currentDestination == AppNavDestination.TRACK_STATUS) Icons.Filled.Search else Icons.Outlined.Search,
                    contentDescription = "Track Status"
                )
            },
            label = {
                Text(
                    text = "Track",
                    fontSize = 11.sp,
                    fontWeight = if (currentDestination == AppNavDestination.TRACK_STATUS) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CidNavyDark,
                selectedTextColor = CidYellowBright,
                indicatorColor = CidYellowBright,
                unselectedIconColor = CidTextSecondary,
                unselectedTextColor = CidTextMuted
            )
        )

        // Join Team
        NavigationBarItem(
            selected = currentDestination == AppNavDestination.JOIN_TEAM,
            onClick = { onNavigate(AppNavDestination.JOIN_TEAM) },
            icon = {
                Icon(
                    imageVector = if (currentDestination == AppNavDestination.JOIN_TEAM) Icons.Filled.GroupAdd else Icons.Outlined.GroupAdd,
                    contentDescription = "Join Team"
                )
            },
            label = {
                Text(
                    text = "Join Team",
                    fontSize = 11.sp,
                    fontWeight = if (currentDestination == AppNavDestination.JOIN_TEAM) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CidNavyDark,
                selectedTextColor = CidYellowBright,
                indicatorColor = CidYellowBright,
                unselectedIconColor = CidTextSecondary,
                unselectedTextColor = CidTextMuted
            )
        )

        // CID Bureau / Admin Portal
        val isBureauSelected = currentDestination == AppNavDestination.LEADER_ADMIN || currentDestination == AppNavDestination.TEAM_DASHBOARD
        NavigationBarItem(
            selected = isBureauSelected,
            onClick = {
                if (isLeaderLoggedIn) {
                    onNavigate(AppNavDestination.LEADER_ADMIN)
                } else {
                    onNavigate(AppNavDestination.TEAM_DASHBOARD)
                }
            },
            icon = {
                BadgedBox(
                    badge = {
                        if (isLeaderLoggedIn && pendingAppsCount > 0) {
                            Badge(containerColor = CidRedAlert) {
                                Text("$pendingAppsCount")
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (isBureauSelected) Icons.Filled.Shield else Icons.Outlined.Shield,
                        contentDescription = "CID Bureau"
                    )
                }
            },
            label = {
                Text(
                    text = if (isLeaderLoggedIn) "Leader HQ" else "CID Bureau",
                    fontSize = 11.sp,
                    fontWeight = if (isBureauSelected) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CidNavyDark,
                selectedTextColor = CidYellowBright,
                indicatorColor = CidYellowBright,
                unselectedIconColor = CidTextSecondary,
                unselectedTextColor = CidTextMuted
            )
        )
    }
}
