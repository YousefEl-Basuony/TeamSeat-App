package com.stackmates.teamseat.Screens.auth_noha

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.stackmates.teamseat.core.components.EmptyScreenTemplate
import com.stackmates.teamseat.core.theme.TeamSeatTheme

/**
 * 🌸 المسؤول عن الشاشة: نُهى (Noha) — [NOHA-1]
 * شاشة البداية (Splash Screen)
 */
@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit = {}
) {
    // TODO (نُهى): امسحي EmptyScreenTemplate واكتبي كود Compose الخاص بشاشة البداية هنا
    EmptyScreenTemplate(
        screenTitle = "Splash Screen",
        ownerName = "نُهى (Noha)",
        hints = listOf(
            "أيقونة أو لوجو التطبيق في منتصف الشاشة",
            "اسم التطبيق TeamSeat بخط كبير وأنيق",
            "عبارة: احجز مقعدك في تيم مشروعك الجامعي",
            "استدعاء onSplashFinished() بعد ثانيتين أو عند الضغط"
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun SplashScreenPreview() {
    TeamSeatTheme {
        SplashScreen()
    }
}
