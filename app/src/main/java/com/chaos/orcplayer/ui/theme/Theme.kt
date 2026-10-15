package com.chaos.orcplayer.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColors = lightColorScheme(
    primary = android.graphics.Color.parseColor("#D84B2A"),
    onPrimary = android.graphics.Color.parseColor("#FFFFFF"),
    secondary = android.graphics.Color.parseColor("#6B5B4E"),
    tertiary = android.graphics.Color.parseColor("#8BA399"),
    background = android.graphics.Color.parseColor("#FAF7F2"),
    onBackground = android.graphics.Color.parseColor("#2C2C2C"),
    surface = android.graphics.Color.parseColor("#FFFFFF"),
    error = android.graphics.Color.parseColor("#CF6679"),
)

private val DarkColors = darkColorScheme(
    primary = android.graphics.Color.parseColor("#FF8C69"),
    onPrimary = android.graphics.Color.parseColor("#1A1A1A"),
    secondary = android.graphics.Color.parseColor("#B8A696"),
    tertiary = android.graphics.Color.parseColor("#9EC5B0"),
    background = android.graphics.Color.parseColor("#1A1A1A"),
    onBackground = android.graphics.Color.parseColor("#E8E8E8"),
    surface = android.graphics.Color.parseColor("#2A2A2A"),
    error = android.graphics.Color.parseColor("#F28B82"),
)

@Composable
fun OrcPlayerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) DarkColors else LightColors
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = android.graphics.Color.TRANSPARENT
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }
    MaterialTheme(colorScheme = colors, typography = AppTypography, content = content)
}
