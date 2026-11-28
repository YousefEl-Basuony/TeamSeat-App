package com.stackmates.teamseat.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.stackmates.teamseat.data.local.LocalAppSettings

var appDarkModeEnabled by mutableStateOf(false)

// =============================================================================
// 🎨 النظام البصري الموحد المستخلص من HomeScreen (Visual System Tokens)
// =============================================================================
val TeamSeatBgOffWhite: Color
    get() = if (appDarkModeEnabled) Color(0xFF0D1719) else Color(0xFFF8FAFA)
val TeamSeatWaveMint: Color
    get() = if (appDarkModeEnabled) Color(0xFF15282C) else Color(0xFFD7ECEA)
val TeamSeatPrimaryTeal: Color
    get() = if (appDarkModeEnabled) Color(0xFF4DB6AC) else Color(0xFF136F78)
val TeamSeatTitleDark: Color
    get() = if (appDarkModeEnabled) Color(0xFFE8F4F4) else Color(0xFF0F292E)
val TeamSeatBodyMuted: Color
    get() = if (appDarkModeEnabled) Color(0xFFA8C2C5) else Color(0xFF52606D)

val TeamSeatCardTopTeal: Color
    get() = if (appDarkModeEnabled) Color(0xFF162528) else Color(0xFFF3F9F9)
val TeamSeatCardBorderTeal: Color
    get() = if (appDarkModeEnabled) Color(0xFF2B4B50) else Color(0xFFA3D1CE)

val TeamSeatCardTopWarm: Color
    get() = if (appDarkModeEnabled) Color(0xFF2B2017) else Color(0xFFFFF9F2)
val TeamSeatCardBorderWarm: Color
    get() = if (appDarkModeEnabled) Color(0xFF8C5327) else Color(0xFFF5C293)
val TeamSeatWarmOrange: Color
    get() = if (appDarkModeEnabled) Color(0xFFFF8A3D) else Color(0xFFEA580C)

val TeamSeatSoftTealPill: Color
    get() = if (appDarkModeEnabled) Color(0xFF1B3538) else Color(0xFFE0F2F1)
val TeamSeatDividerColor: Color
    get() = if (appDarkModeEnabled) Color(0xFF263E42) else Color(0xFFE5ECEC)
val TeamSeatDangerRed: Color
    get() = if (appDarkModeEnabled) Color(0xFFEF5350) else Color(0xFFC62828)

/**
 * 🌊 حاوية الشاشات الموحدة (TeamSeatScreenContainer)
 * توفر خلفية #F8FAFA مع الموجتين العضويتين (#D7ECEA) في الزوايا،
 * وتضبط اتجاه RTL ومسافات Scaffold الآمنة تلقائياً.
 */
@Composable
fun TeamSeatScreenContainer(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    snackbarHostState: SnackbarHostState? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    val appSettings = LocalAppSettings.current
    val isDark = appSettings.isDarkMode
    val layoutDirection = if (appSettings.languageCode == "en") LayoutDirection.Ltr else LayoutDirection.Rtl
    val bgColor = if (isDark) Color(0xFF0D1719) else TeamSeatBgOffWhite
    val waveColor = if (isDark) Color(0xFF15282C) else TeamSeatWaveMint

    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(bgColor)
        ) {
            // الموجتان العضويتان في الزاوية العلوية اليسرى والسفلية اليمنى (مع cache للمسارات لتحسين الأداء)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .drawWithCache {
                        val w = size.width
                        val h = size.height

                        val topLeftWave = Path().apply {
                            moveTo(0f, 0f)
                            lineTo(0f, h * 0.24f)
                            cubicTo(
                                w * 0.16f, h * 0.20f,
                                w * 0.42f, h * 0.15f,
                                w * 0.62f, 0f
                            )
                            close()
                        }

                        val bottomRightWave = Path().apply {
                            moveTo(w * 0.35f, h)
                            cubicTo(
                                w * 0.50f, h * 0.88f,
                                w * 0.78f, h * 0.78f,
                                w, h * 0.80f
                            )
                            lineTo(w, h)
                            close()
                        }

                        onDrawBehind {
                            drawPath(path = topLeftWave, color = waveColor)
                            drawPath(path = bottomRightWave, color = waveColor)
                        }
                    }
            )

            Scaffold(
                containerColor = Color.Transparent,
                topBar = topBar,
                bottomBar = bottomBar,
                floatingActionButton = floatingActionButton,
                floatingActionButtonPosition = floatingActionButtonPosition,
                snackbarHost = {
                    if (snackbarHostState != null) {
                        SnackbarHost(hostState = snackbarHostState)
                    }
                }
            ) { innerPadding ->
                val safePadding = PaddingValues(
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding() + 16.dp
                )
                content(safePadding)
            }
        }
    }
}

/**
 * 🃏 كارت TeamSeat الموحد (Flat Tinted Gradient Card — No Nested Cards)
 */
@Composable
fun TeamSeatCard(
    modifier: Modifier = Modifier,
    isWarmHighlight: Boolean = false,
    contentPadding: Dp = 14.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val isDark = LocalAppSettings.current.isDarkMode
    val topColor = when {
        isDark && isWarmHighlight -> Color(0xFF2B2017)
        isDark -> Color(0xFF162528)
        isWarmHighlight -> TeamSeatCardTopWarm
        else -> TeamSeatCardTopTeal
    }
    val bottomColor = if (isDark) Color(0xFF111D20) else Color.White
    val borderColor = when {
        isDark && isWarmHighlight -> Color(0xFF8C5327)
        isDark -> Color(0xFF28474B)
        isWarmHighlight -> TeamSeatCardBorderWarm
        else -> TeamSeatCardBorderTeal
    }

    val baseModifier = modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp))
        .background(
            brush = Brush.verticalGradient(
                colors = listOf(topColor, bottomColor)
            )
        )
        .border(1.5.dp, borderColor, RoundedCornerShape(16.dp))

    val interactiveModifier = if (onClick != null) {
        baseModifier.bounceClick(onClick = onClick)
    } else {
        baseModifier
    }

    Column(
        modifier = interactiveModifier.padding(contentPadding),
        content = content
    )
}
