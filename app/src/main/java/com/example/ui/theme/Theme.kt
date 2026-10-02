package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val CidColorScheme = darkColorScheme(
    primary = CidYellowBright,
    onPrimary = CidNavyDark,
    primaryContainer = CidYellowContainer,
    onPrimaryContainer = CidYellowLight,
    secondary = CidGold,
    onSecondary = CidNavyDark,
    secondaryContainer = CidNavySurface,
    onSecondaryContainer = CidYellowLight,
    tertiary = CidBlueInfo,
    onTertiary = CidNavyDark,
    background = CidNavyBackground,
    onBackground = CidTextPrimary,
    surface = CidNavyCard,
    onSurface = CidTextPrimary,
    surfaceVariant = CidNavySurface,
    onSurfaceVariant = CidTextSecondary,
    outline = CidNavyBorder,
    outlineVariant = CidNavyBorder.copy(alpha = 0.5f),
    error = CidRedAlert,
    onError = CidTextPrimary
)

@Composable
fun ChoduCidTheme(
    darkTheme: Boolean = true, // Investigation dark theme as requested
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            window?.let {
                it.statusBarColor = CidNavyDark.toArgb()
                it.navigationBarColor = CidNavyDark.toArgb()
                val controller = WindowCompat.getInsetsController(it, view)
                controller.isAppearanceLightStatusBars = false
                controller.isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = CidColorScheme,
        typography = Typography,
        content = content
    )
}
