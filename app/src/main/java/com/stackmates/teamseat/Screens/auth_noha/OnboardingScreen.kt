package com.stackmates.teamseat.Screens.auth_noha

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.stackmates.teamseat.core.components.EmptyScreenTemplate
import com.stackmates.teamseat.core.theme.TeamSeatTheme

/**
 * 🌸 المسؤول عن الشاشة: نُهى (Noha) — [NOHA-5]
 * شاشة الترحيب (Onboarding Screen - 3 صفحات سلايدر)
 */
@Composable
fun OnboardingScreen(
    onFinishOnboarding: () -> Unit = {}
) {
    // TODO (نُهى): امسحي EmptyScreenTemplate وصممي الـ HorizontalPager هنا
    EmptyScreenTemplate(
        screenTitle = "Onboarding Screen",
        ownerName = "نُهى (Noha)",
        hints = listOf(
            "HorizontalPager مكون من 3 صفحات متتالية",
            "الصفحة 1: تصفح التيمات المتاحة في دفعتك",
            "الصفحة 2: احجز مقعد تخصصك بنظام كراسي السينما",
            "الصفحة 3: اعتمد تيمك وصدر كشف التسليم للمعيدة",
            "مؤشر النقاط (Dots Indicator) + زر (ابدأ الآن) يستدعي onFinishOnboarding()"
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun OnboardingScreenPreview() {
    TeamSeatTheme {
        OnboardingScreen()
    }
}
