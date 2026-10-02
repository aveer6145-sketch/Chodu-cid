package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Complaint
import com.example.data.model.TeamApplication
import com.example.ui.theme.CidBlueInfo
import com.example.ui.theme.CidGreenSuccess
import com.example.ui.theme.CidNavyDark
import com.example.ui.theme.CidOrangeWarning
import com.example.ui.theme.CidRedAlert
import com.example.ui.theme.CidYellowBright

@Composable
fun CaseStatusBadge(
    status: String,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, borderColor) = when (status) {
        Complaint.STATUS_NEW -> Triple(
            CidYellowBright.copy(alpha = 0.18f),
            CidYellowBright,
            CidYellowBright.copy(alpha = 0.5f)
        )
        Complaint.STATUS_INVESTIGATING -> Triple(
            CidBlueInfo.copy(alpha = 0.18f),
            CidBlueInfo,
            CidBlueInfo.copy(alpha = 0.5f)
        )
        Complaint.STATUS_IN_PROGRESS -> Triple(
            CidOrangeWarning.copy(alpha = 0.18f),
            CidOrangeWarning,
            CidOrangeWarning.copy(alpha = 0.5f)
        )
        Complaint.STATUS_RESOLVED -> Triple(
            CidGreenSuccess.copy(alpha = 0.18f),
            CidGreenSuccess,
            CidGreenSuccess.copy(alpha = 0.5f)
        )
        Complaint.STATUS_REJECTED -> Triple(
            CidRedAlert.copy(alpha = 0.18f),
            CidRedAlert,
            CidRedAlert.copy(alpha = 0.5f)
        )
        TeamApplication.STATUS_PENDING -> Triple(
            CidYellowBright.copy(alpha = 0.18f),
            CidYellowBright,
            CidYellowBright.copy(alpha = 0.5f)
        )
        TeamApplication.STATUS_APPROVED -> Triple(
            CidGreenSuccess.copy(alpha = 0.18f),
            CidGreenSuccess,
            CidGreenSuccess.copy(alpha = 0.5f)
        )
        else -> Triple(
            Color.Gray.copy(alpha = 0.2f),
            Color.LightGray,
            Color.Gray.copy(alpha = 0.5f)
        )
    }

    Box(
        modifier = modifier
            .background(bgColor, RoundedCornerShape(6.dp))
            .border(1.dp, borderColor, RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = status.uppercase(),
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun UrgencyBadge(
    urgency: String,
    modifier: Modifier = Modifier
) {
    val (color, label) = when (urgency) {
        Complaint.URGENCY_CRITICAL -> Pair(CidRedAlert, "CRITICAL ALERT")
        Complaint.URGENCY_HIGH -> Pair(CidOrangeWarning, "HIGH PRIORITY")
        else -> Pair(CidBlueInfo, "NORMAL")
    }

    Box(
        modifier = modifier
            .background(color.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
            .border(0.8.dp, color.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = label,
            color = color,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
