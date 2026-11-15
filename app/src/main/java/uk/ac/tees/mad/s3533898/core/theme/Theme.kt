package uk.ac.tees.mad.s3533898.core.theme

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
    primary = Teal40,
    onPrimary = LightSurface,
    primaryContainer = Teal80,
    onPrimaryContainer = TealDark,
    secondary = TealGrey40,
    background = LightBackground,
    onBackground = TealDark,
    surface = LightSurface,
    surfaceVariant = TealGrey80,
    error = DangerRed,
    tertiary = Amber
)

private val DarkColors = darkColorScheme(
    primary = Teal80,
    onPrimary = TealDark,
    primaryContainer = Teal40,
    onPrimaryContainer = Teal80,
    secondary = TealGrey80,
    background = DarkBackground,
    onBackground = TealGrey80,
    surface = DarkSurface,
    surfaceVariant = TealGrey40,
    error = DangerRed,
    tertiary = Amber
)

/**
 * MediMate theme. We deliberately do NOT use Android 12 dynamic colour so the
 * brand identity stays consistent across devices (a documented design decision).
 */
@Composable
fun MediMateTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colors.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }
    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}
