package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CidNavyBorder
import com.example.ui.theme.CidNavyCard
import com.example.ui.theme.CidNavySurface
import com.example.ui.theme.CidTextPrimary
import com.example.ui.theme.CidTextSecondary
import com.example.ui.theme.CidYellowBright

@Composable
fun StatCounterCard(
    title: String,
    count: Int,
    icon: ImageVector,
    accentColor: Color = CidYellowBright,
    modifier: Modifier = Modifier,
    subtitle: String? = null
) {
    Box(
        modifier = modifier
            .background(CidNavyCard, RoundedCornerShape(12.dp))
            .border(1.dp, CidNavyBorder, RoundedCornerShape(12.dp))
            .padding(14.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(accentColor.copy(alpha = 0.15f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = accentColor,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Text(
                    text = count.toString(),
                    color = CidTextPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black
                )
            }

            Column {
                Text(
                    text = title,
                    color = CidTextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        color = accentColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
    }
}
