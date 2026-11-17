package com.stackmates.teamseat.Screens.seatmap_esraa

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.stackmates.teamseat.core.components.EmptyScreenTemplate
import com.stackmates.teamseat.core.theme.TeamSeatTheme

/**
 * 💺 المسؤول عن الشاشة: إسراء (Esraa) — [ESRAA-2]
 * إنشاء تيم جديد — الخطوة 1: بيانات المشروع وعدد الكراسي
 */
@Composable
fun CreateTeamInfoScreen(
    onNextStepClick: (projectName: String, category: String, description: String, totalSeats: Int) -> Unit = { _, _, _, _ -> },
    onBackClick: () -> Unit = {}
) {
    // TODO (إسراء): امسحي EmptyScreenTemplate وصممي فورم بيانات المشروع هنا
    EmptyScreenTemplate(
        screenTitle = "إنشاء تيم جديد (1/2)",
        ownerName = "إسراء (Esraa)",
        onBackClick = onBackClick,
        hints = listOf(
            "حقل اسم المشروع",
            "حقل اسم المادة أو الفئة (مثلاً: مشروع تخرج / هندسة برمجيات)",
            "حقل نبذة مختصرة عن فكرة المشروع",
            "Slider أو أزرار (+/-) لتحديد عدد المقاعد من 2 إلى 8 مقاعد",
            "زر (التالي: تخصيص المقاعد) يستدعي onNextStepClick(...)"
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun CreateTeamInfoScreenPreview() {
    TeamSeatTheme {
        CreateTeamInfoScreen()
    }
}
