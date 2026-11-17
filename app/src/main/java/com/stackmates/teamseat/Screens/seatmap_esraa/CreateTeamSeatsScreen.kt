package com.stackmates.teamseat.Screens.seatmap_esraa

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.stackmates.teamseat.core.components.EmptyScreenTemplate
import com.stackmates.teamseat.core.theme.TeamSeatTheme

/**
 * 💺 المسؤول عن الشاشة: إسراء (Esraa) — [ESRAA-3]
 * إنشاء تيم جديد — الخطوة 2: تحديد تخصص كل مقعد (Seat Blueprint)
 */
@Composable
fun CreateTeamSeatsScreen(
    totalSeatsCount: Int = 5,
    availableRoles: List<String> = listOf("Android", "Backend", "UI/UX", "Flutter", "AI / ML", "QA"),
    onPublishTeamClick: (seatRoles: List<String>) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    // TODO (إسراء): امسحي EmptyScreenTemplate وصممي تخصيص الكراسي هنا
    EmptyScreenTemplate(
        screenTitle = "تخصيص أدوار المقاعد (2/2)",
        ownerName = "إسراء (Esraa)",
        onBackClick = onBackClick,
        hints = listOf(
            "عرض قائمة بعدد المقاعد المختارة ($totalSeatsCount مقاعد)",
            "أمام كل مقعد DropdownMenu أو Chips لاختيار التخصص المطلوب لهذا الكرسي",
            "زر (نشر التيم وفتح الحجز 🚀) يستدعي onPublishTeamClick(seatRoles)"
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun CreateTeamSeatsScreenPreview() {
    TeamSeatTheme {
        CreateTeamSeatsScreen()
    }
}
