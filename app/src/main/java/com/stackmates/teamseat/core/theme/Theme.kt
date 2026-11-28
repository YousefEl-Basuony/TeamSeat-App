package com.stackmates.teamseat.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection

private val LightColorScheme = lightColorScheme(
    primary = DeepTeal,
    onPrimary = WarmCream,
    primaryContainer = LightAqua,
    onPrimaryContainer = DeepNavy,
    secondary = Turquoise,
    onSecondary = WarmCream,
    secondaryContainer = SoftBeige,
    onSecondaryContainer = DeepNavy,
    tertiary = Peach,
    onTertiary = DeepNavy,
    background = WarmCream,
    onBackground = DeepNavy,
    surface = SoftBeige,
    onSurface = DeepNavy,
    surfaceVariant = WarmCream,
    onSurfaceVariant = DeepNavy,
    outline = SoftGray
)

private val DarkColorScheme = darkColorScheme(
    primary = Turquoise,
    onPrimary = Color(0xFF062023),
    primaryContainer = DeepTeal,
    onPrimaryContainer = Color(0xFFE0F2F1),
    secondary = LightAqua,
    onSecondary = Color(0xFF062023),
    secondaryContainer = Color(0xFF1A2C30),
    onSecondaryContainer = Color(0xFFE6F1F1),
    tertiary = Peach,
    onTertiary = Color(0xFF1E140B),
    background = Color(0xFF0D1719),
    onBackground = Color(0xFFE6F1F1),
    surface = Color(0xFF142226),
    onSurface = Color(0xFFE6F1F1),
    surfaceVariant = Color(0xFF1A2C30),
    onSurfaceVariant = Color(0xFFB8CECE),
    outline = Color(0xFF2D484D)
)

/**
 * الثيم الموحد لتطبيق TeamSeat (يدعم الوضع الداكن واتجاه اللغة العربية/الإنجليزية)
 */
@Composable
fun TeamSeatTheme(
    darkTheme: Boolean = false,
    rtl: Boolean = true,
    content: @Composable () -> Unit
) {
    val direction = if (rtl) LayoutDirection.Rtl else LayoutDirection.Ltr
    CompositionLocalProvider(LocalLayoutDirection provides direction) {
        MaterialTheme(
            colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
            typography = AppTypography,
            content = content
        )
    }
}
