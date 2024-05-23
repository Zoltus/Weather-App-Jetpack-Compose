package fi.sulku.weatherapp.ui.theme

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

private val DarkColorScheme = darkColorScheme(
    primary = primaryDark, // Buttons and top bar
    secondary = indicator, // Loading indicator
    background = backgroundDark, // Background
    surface = surfaceDark, // Cards, sheets, menus
    surfaceVariant = surfaceVariantDark, // Textfields
    onPrimary = onPrimaryDark, // text, radio buttons
)

private val LightColorScheme = lightColorScheme(
    primary = primaryLight,
    secondary = indicator,
    background = backgroundLight,
    surface = surfaceLight
    /* Testers
    onPrimary = Color(0xFF07EC12), // text, radioation button
    primaryContainer = Color(0xFF311B92),
    onPrimaryContainer = Color(0xFF4527A0),
    inversePrimary = Color(0xFF512DA8),
    onSecondary = Color(0xFF1BFFC9), //text and icons on top of primary?
    secondaryContainer = Color(0xFF673AB7),
    onSecondaryContainer = Color(0xFF9575CD),
    tertiary = Color(0xFFB39DDB),
    onTertiary = Color(0xFFD1C4E9),
    tertiaryContainer = Color(0xFFEDE7F6),
    onTertiaryContainer = Color(0xFFBB23E6),
    onBackground = Color(0xFF43007E),
    surfaceVariant = Color(0xFFFF88E4),
    onSurfaceVariant = Color(0xFF81C784),
    surfaceTint = Color(0xFFC8E6C9),
    inverseSurface = Color(0xFFE8F5E9),
    inverseOnSurface = Color(0xFFBF360C),
    onError = Color(0xFFE64A19),
    errorContainer = Color(0xFFFF5722),
    onErrorContainer = Color(0xFFFF7043),
    outline = Color(0xFFFF8A65),
    outlineVariant = Color(0xFFFFAB91),
    scrim = Color(0xFFFFCCBC),
    surfaceBright = Color(0xFFFFE0B2),
    surfaceContainer = Color(0xFFFFEBEE),
    surfaceContainerHigh = Color(0xFFD32F2F),
    surfaceContainerHighest = Color(0xFFC62828),
    surfaceContainerLow = Color(0xFFB71C1C),
    surfaceContainerLowest = Color(0xFF880E4F),
    surfaceDim = Color(0xFFAD1457)*/
)

@Composable
fun WeatherAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}