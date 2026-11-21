package com.stackmates.teamseat.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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

/**
 * الثيم الموحد لتطبيق TeamSeat (يدعم اتجاه اللغة العربية RTL تلقائياً)
 */
@Composable
fun TeamSeatTheme(
    rtl: Boolean = true,
    content: @Composable () -> Unit
) {
    val direction = if (rtl) LayoutDirection.Rtl else LayoutDirection.Ltr
    CompositionLocalProvider(LocalLayoutDirection provides direction) {
        MaterialTheme(
            colorScheme = LightColorScheme,
            typography = AppTypography,
            content = content
        )
    }
}
