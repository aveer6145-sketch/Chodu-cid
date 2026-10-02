package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.model.Complaint
import com.example.data.model.TeamApplication
import com.example.data.model.TeamMember
import com.example.data.repository.ChoduCidRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppNavDestination {
    HOME,
    REPORT_PROBLEM,
    TRACK_STATUS,
    JOIN_TEAM,
    LEADER_ADMIN,
    TEAM_DASHBOARD
}

class ChoduCidViewModel(
    private val repository: ChoduCidRepository
) : ViewModel() {

    // Current navigation destination
    private val _currentDestination = MutableStateFlow(AppNavDestination.HOME)
    val currentDestination: StateFlow<AppNavDestination> = _currentDestination.asStateFlow()

    // Data streams from Room
    val allComplaints: StateFlow<List<Complaint>> = repository.allComplaints
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalComplaintsCount: StateFlow<Int> = repository.totalComplaintCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val resolvedCount: StateFlow<Int> = repository.resolvedComplaintCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val investigatingCount: StateFlow<Int> = repository.investigatingComplaintCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val newComplaintsCount: StateFlow<Int> = repository.newComplaintCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val allApplications: StateFlow<List<TeamApplication>> = repository.allApplications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val pendingApplicationsCount: StateFlow<Int> = repository.pendingApplicationCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val activeMembers: StateFlow<List<TeamMember>> = repository.activeMembers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeMembersCount: StateFlow<Int> = repository.activeMemberCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Tracking search
    private val _searchedCaseId = MutableStateFlow("")
    val searchedCaseId: StateFlow<String> = _searchedCaseId.asStateFlow()

    private val _trackedCase = MutableStateFlow<Complaint?>(null)
    val trackedCase: StateFlow<Complaint?> = _trackedCase.asStateFlow()

    private val _searchedAppId = MutableStateFlow("")
    val searchedAppId: StateFlow<String> = _searchedAppId.asStateFlow()

    private val _trackedApplication = MutableStateFlow<TeamApplication?>(null)
    val trackedApplication: StateFlow<TeamApplication?> = _trackedApplication.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    // Leader Admin Auth State
    private val _isLeaderAuthenticated = MutableStateFlow(false)
    val isLeaderAuthenticated: StateFlow<Boolean> = _isLeaderAuthenticated.asStateFlow()

    // Team Member Auth State (Logged in Member)
    private val _currentTeamMember = MutableStateFlow<TeamMember?>(null)
    val currentTeamMember: StateFlow<TeamMember?> = _currentTeamMember.asStateFlow()

    // Filter states for admin & cases
    private val _selectedCategoryFilter = MutableStateFlow<String?>(null)
    val selectedCategoryFilter: StateFlow<String?> = _selectedCategoryFilter.asStateFlow()

    private val _selectedStatusFilter = MutableStateFlow<String?>(null)
    val selectedStatusFilter: StateFlow<String?> = _selectedStatusFilter.asStateFlow()

    // Success dialog / snackbar states
    private val _newRegisteredCaseId = MutableStateFlow<String?>(null)
    val newRegisteredCaseId: StateFlow<String?> = _newRegisteredCaseId.asStateFlow()

    private val _newRegisteredAppId = MutableStateFlow<String?>(null)
    val newRegisteredAppId: StateFlow<String?> = _newRegisteredAppId.asStateFlow()

    private val _userFeedbackMessage = MutableStateFlow<String?>(null)
    val userFeedbackMessage: StateFlow<String?> = _userFeedbackMessage.asStateFlow()

    fun navigateTo(destination: AppNavDestination) {
        _currentDestination.value = destination
    }

    // Submit Problem (Student Reporting)
    fun reportProblem(
        studentName: String,
        course: String,
        branch: String,
        category: String,
        title: String,
        description: String,
        imageUri: String? = null,
        urgency: String = Complaint.URGENCY_NORMAL,
        onSuccess: (String) -> Unit
    ) {
        viewModelScope.launch {
            val caseId = repository.submitComplaint(
                studentName = studentName,
                course = course,
                branch = branch,
                category = category,
                title = title,
                description = description,
                imageUri = imageUri,
                urgency = urgency
            )
            _newRegisteredCaseId.value = caseId
            _userFeedbackMessage.value = "Your case has been registered with CHODU CID! Case ID: $caseId"
            onSuccess(caseId)
        }
    }

    // Submit Membership Application (Join Our Team)
    fun applyForMembership(
        fullName: String,
        course: String,
        branch: String,
        onSuccess: (String) -> Unit
    ) {
        viewModelScope.launch {
            val appId = repository.submitApplication(
                fullName = fullName,
                course = course,
                branch = branch
            )
            _newRegisteredAppId.value = appId
            _userFeedbackMessage.value = "Your application has been sent to Aryaveer. Wait for approval!"
            onSuccess(appId)
        }
    }

    // Track a case
    fun searchCaseById(caseId: String) {
        val query = caseId.trim().uppercase()
        _searchedCaseId.value = query
        viewModelScope.launch {
            _isSearching.value = true
            repository.getComplaintByCaseId(query).collect { complaint ->
                _trackedCase.value = complaint
                _isSearching.value = false
            }
        }
    }

    // Track an application
    fun searchApplicationById(appId: String) {
        val query = appId.trim().uppercase()
        _searchedAppId.value = query
        viewModelScope.launch {
            _isSearching.value = true
            repository.getApplicationById(query).collect { application ->
                _trackedApplication.value = application
                _isSearching.value = false
            }
        }
    }

    // Leader Admin: Passcode Login
    fun authenticateLeader(pin: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = repository.verifyLeaderPasscode(pin)
            if (success) {
                _isLeaderAuthenticated.value = true
                _userFeedbackMessage.value = "Welcome back, Chief Aryaveer!"
            }
            onResult(success)
        }
    }

    fun logoutLeader() {
        _isLeaderAuthenticated.value = false
        _currentDestination.value = AppNavDestination.HOME
    }

    // Leader Admin: Approve / Reject Application
    fun approveApplication(appId: String, remarks: String? = null) {
        viewModelScope.launch {
            repository.approveApplication(appId, remarks)
            _userFeedbackMessage.value = "Application $appId approved! New CID member added."
        }
    }

    fun rejectApplication(appId: String, remarks: String? = null) {
        viewModelScope.launch {
            repository.rejectApplication(appId, remarks)
            _userFeedbackMessage.value = "Application $appId rejected."
        }
    }

    // Leader Admin: Remove Member
    fun removeTeamMember(memberId: String) {
        viewModelScope.launch {
            repository.removeTeamMember(memberId)
            _userFeedbackMessage.value = "Member removed from CHODU CID team."
        }
    }

    // Leader / Team Member: Update Complaint Status and Notes
    fun updateComplaint(
        caseId: String,
        status: String,
        notes: String,
        resolution: String,
        assignedMember: String?
    ) {
        viewModelScope.launch {
            repository.updateComplaintStatus(caseId, status, notes, resolution, assignedMember)
            _userFeedbackMessage.value = "Case $caseId updated successfully!"
        }
    }

    // Leader Admin: Delete Complaint
    fun deleteComplaint(caseId: String) {
        viewModelScope.launch {
            repository.deleteComplaint(caseId)
            _userFeedbackMessage.value = "Case $caseId deleted."
        }
    }

    // Team Member Login / Selection (must be an approved member)
    fun loginAsMember(member: TeamMember) {
        _currentTeamMember.value = member
        _currentDestination.value = AppNavDestination.TEAM_DASHBOARD
        _userFeedbackMessage.value = "Welcome Agent ${member.fullName} [${member.badgeNumber}]"
    }

    fun logoutMember() {
        _currentTeamMember.value = null
        _currentDestination.value = AppNavDestination.HOME
    }

    // Filters
    fun setCategoryFilter(category: String?) {
        _selectedCategoryFilter.value = category
    }

    fun setStatusFilter(status: String?) {
        _selectedStatusFilter.value = status
    }

    fun dismissFeedback() {
        _userFeedbackMessage.value = null
        _newRegisteredCaseId.value = null
        _newRegisteredAppId.value = null
    }

    // Update Leader PIN
    fun updateLeaderPin(oldPin: String, newPin: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val res = repository.updateLeaderPasscode(oldPin, newPin)
            onResult(res)
        }
    }
}

class ChoduCidViewModelFactory(
    private val repository: ChoduCidRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChoduCidViewModel::class.java)) {
            return ChoduCidViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
