package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.local.ChoduCidDatabase
import com.example.data.repository.ChoduCidRepository
import com.example.ui.components.CidBottomNavigation
import com.example.ui.components.CidTopAppBar
import com.example.ui.components.LeaderLoginDialog
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.JoinTeamScreen
import com.example.ui.screens.LeaderDashboardScreen
import com.example.ui.screens.ReportProblemScreen
import com.example.ui.screens.TeamMemberDashboardScreen
import com.example.ui.screens.TrackCaseScreen
import com.example.ui.theme.ChoduCidTheme
import com.example.ui.theme.CidNavyBackground
import com.example.ui.theme.CidNavyCard
import com.example.ui.theme.CidTextPrimary
import com.example.ui.theme.CidYellowBright
import com.example.ui.viewmodel.AppNavDestination
import com.example.ui.viewmodel.ChoduCidViewModel
import com.example.ui.viewmodel.ChoduCidViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = ChoduCidDatabase.getDatabase(applicationContext, lifecycleScope)
        val repository = ChoduCidRepository(
            complaintDao = database.complaintDao(),
            teamApplicationDao = database.teamApplicationDao(),
            teamMemberDao = database.teamMemberDao(),
            adminConfigDao = database.adminConfigDao()
        )
        val viewModelFactory = ChoduCidViewModelFactory(repository)

        setContent {
            ChoduCidTheme {
                val viewModel: ChoduCidViewModel = viewModel(factory = viewModelFactory)
                ChoduCidApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun ChoduCidApp(viewModel: ChoduCidViewModel) {
    val currentDestination by viewModel.currentDestination.collectAsStateWithLifecycle()
    val allComplaints by viewModel.allComplaints.collectAsStateWithLifecycle()
    val totalProblemsCount by viewModel.totalComplaintsCount.collectAsStateWithLifecycle()
    val resolvedCasesCount by viewModel.resolvedCount.collectAsStateWithLifecycle()
    val investigatingCount by viewModel.investigatingCount.collectAsStateWithLifecycle()
    val newCasesCount by viewModel.newComplaintsCount.collectAsStateWithLifecycle()

    val allApplications by viewModel.allApplications.collectAsStateWithLifecycle()
    val pendingAppsCount by viewModel.pendingApplicationsCount.collectAsStateWithLifecycle()

    val activeMembers by viewModel.activeMembers.collectAsStateWithLifecycle()
    val activeMembersCount by viewModel.activeMembersCount.collectAsStateWithLifecycle()

    val searchedCaseId by viewModel.searchedCaseId.collectAsStateWithLifecycle()
    val trackedCase by viewModel.trackedCase.collectAsStateWithLifecycle()
    val searchedAppId by viewModel.searchedAppId.collectAsStateWithLifecycle()
    val trackedApp by viewModel.trackedApplication.collectAsStateWithLifecycle()
    val isSearching by viewModel.isSearching.collectAsStateWithLifecycle()

    val isLeaderLoggedIn by viewModel.isLeaderAuthenticated.collectAsStateWithLifecycle()
    val currentTeamMember by viewModel.currentTeamMember.collectAsStateWithLifecycle()
    val userFeedbackMessage by viewModel.userFeedbackMessage.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var showLeaderLoginDialog by remember { mutableStateOf(false) }

    // Handle back button on sub-screens
    BackHandler(enabled = currentDestination != AppNavDestination.HOME) {
        viewModel.navigateTo(AppNavDestination.HOME)
    }

    // Feedback Snackbar notification
    LaunchedEffect(userFeedbackMessage) {
        userFeedbackMessage?.let { msg ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(msg)
                viewModel.dismissFeedback()
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = CidNavyBackground,
        topBar = {
            CidTopAppBar(
                isLeaderLoggedIn = isLeaderLoggedIn,
                pendingAppsCount = pendingAppsCount,
                onAdminClick = {
                    if (isLeaderLoggedIn) {
                        viewModel.navigateTo(AppNavDestination.LEADER_ADMIN)
                    } else {
                        showLeaderLoginDialog = true
                    }
                },
                onTrackClick = {
                    viewModel.navigateTo(AppNavDestination.TRACK_STATUS)
                }
            )
        },
        bottomBar = {
            CidBottomNavigation(
                currentDestination = currentDestination,
                isLeaderLoggedIn = isLeaderLoggedIn,
                pendingAppsCount = pendingAppsCount,
                onNavigate = { destination ->
                    if (destination == AppNavDestination.LEADER_ADMIN && !isLeaderLoggedIn) {
                        showLeaderLoginDialog = true
                    } else {
                        viewModel.navigateTo(destination)
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    containerColor = CidNavyCard,
                    contentColor = CidTextPrimary,
                    actionColor = CidYellowBright,
                    snackbarData = data
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(CidNavyBackground)
        ) {
            AnimatedContent(
                targetState = currentDestination,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "ScreenTransition"
            ) { destination ->
                when (destination) {
                    AppNavDestination.HOME -> {
                        HomeScreen(
                            totalProblemsCount = totalProblemsCount,
                            resolvedCasesCount = resolvedCasesCount,
                            investigatingCount = investigatingCount,
                            approvedTeamCount = activeMembersCount,
                            recentComplaints = allComplaints,
                            onNavigate = { dest ->
                                if (dest == AppNavDestination.LEADER_ADMIN && !isLeaderLoggedIn) {
                                    showLeaderLoginDialog = true
                                } else {
                                    viewModel.navigateTo(dest)
                                }
                            },
                            onTrackCaseSearch = { query ->
                                viewModel.searchCaseById(query)
                            }
                        )
                    }

                    AppNavDestination.REPORT_PROBLEM -> {
                        ReportProblemScreen(
                            onSubmitProblem = { name, course, branch, cat, title, desc, img, urgency, onDone ->
                                viewModel.reportProblem(name, course, branch, cat, title, desc, img, urgency, onDone)
                            },
                            onTrackCase = { caseId ->
                                viewModel.searchCaseById(caseId)
                            },
                            onNavigate = { dest ->
                                viewModel.navigateTo(dest)
                            }
                        )
                    }

                    AppNavDestination.TRACK_STATUS -> {
                        TrackCaseScreen(
                            searchedCaseId = searchedCaseId,
                            trackedCase = trackedCase,
                            searchedAppId = searchedAppId,
                            trackedApplication = trackedApp,
                            isSearching = isSearching,
                            onSearchCase = { query ->
                                viewModel.searchCaseById(query)
                            },
                            onSearchApplication = { query ->
                                viewModel.searchApplicationById(query)
                            }
                        )
                    }

                    AppNavDestination.JOIN_TEAM -> {
                        JoinTeamScreen(
                            onApplyMembership = { name, course, branch, onDone ->
                                viewModel.applyForMembership(name, course, branch, onDone)
                            },
                            onTrackApplication = { appId ->
                                viewModel.searchApplicationById(appId)
                            },
                            onNavigate = { dest ->
                                viewModel.navigateTo(dest)
                            }
                        )
                    }

                    AppNavDestination.LEADER_ADMIN -> {
                        if (isLeaderLoggedIn) {
                            LeaderDashboardScreen(
                                complaints = allComplaints,
                                applications = allApplications,
                                members = activeMembers,
                                totalProblemsCount = totalProblemsCount,
                                pendingCasesCount = newCasesCount,
                                investigatingCount = investigatingCount,
                                resolvedCasesCount = resolvedCasesCount,
                                pendingAppsCount = pendingAppsCount,
                                approvedTeamCount = activeMembersCount,
                                onApproveApplication = { appId, remarks ->
                                    viewModel.approveApplication(appId, remarks)
                                },
                                onRejectApplication = { appId, remarks ->
                                    viewModel.rejectApplication(appId, remarks)
                                },
                                onRemoveMember = { memberId ->
                                    viewModel.removeTeamMember(memberId)
                                },
                                onUpdateComplaint = { caseId, status, notes, res, assigned ->
                                    viewModel.updateComplaint(caseId, status, notes, res, assigned)
                                },
                                onDeleteComplaint = { caseId ->
                                    viewModel.deleteComplaint(caseId)
                                },
                                onUpdatePin = { oldPin, newPin, callback ->
                                    viewModel.updateLeaderPin(oldPin, newPin, callback)
                                },
                                onLogout = {
                                    viewModel.logoutLeader()
                                }
                            )
                        } else {
                            // If somehow here without auth, show login prompt
                            LaunchedEffect(Unit) {
                                showLeaderLoginDialog = true
                            }
                            HomeScreen(
                                totalProblemsCount = totalProblemsCount,
                                resolvedCasesCount = resolvedCasesCount,
                                investigatingCount = investigatingCount,
                                approvedTeamCount = activeMembersCount,
                                recentComplaints = allComplaints,
                                onNavigate = { viewModel.navigateTo(it) },
                                onTrackCaseSearch = { viewModel.searchCaseById(it) }
                            )
                        }
                    }

                    AppNavDestination.TEAM_DASHBOARD -> {
                        TeamMemberDashboardScreen(
                            currentMember = currentTeamMember,
                            approvedMembers = activeMembers,
                            complaints = allComplaints,
                            onSelectMember = { member ->
                                viewModel.loginAsMember(member)
                            },
                            onLogoutMember = {
                                viewModel.logoutMember()
                            },
                            onUpdateInvestigation = { caseId, status, notes, res, assigned ->
                                viewModel.updateComplaint(caseId, status, notes, res, assigned)
                            },
                            onNavigate = { dest ->
                                viewModel.navigateTo(dest)
                            }
                        )
                    }
                }
            }
        }
    }

    // Leader PIN Login Dialog
    if (showLeaderLoginDialog) {
        LeaderLoginDialog(
            onDismiss = { showLeaderLoginDialog = false },
            onLogin = { pin, callback ->
                viewModel.authenticateLeader(pin) { success ->
                    callback(success)
                    if (success) {
                        showLeaderLoginDialog = false
                        viewModel.navigateTo(AppNavDestination.LEADER_ADMIN)
                    }
                }
            }
        )
    }
}
