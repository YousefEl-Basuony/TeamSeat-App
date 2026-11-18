package com.stackmates.teamseat.Screens.admin_youssef

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.stackmates.teamseat.core.components.EmptyScreenTemplate
import com.stackmates.teamseat.core.data.MockDataProvider
import com.stackmates.teamseat.core.model.Seat
import com.stackmates.teamseat.core.theme.TeamSeatTheme

/**
 * 💻 المسؤول عن الشاشة: يوسف (Youssef - Team Lead & Backend)
 * شاشة تعديل المقاعد الشاغرة وحذفها أو تغيير الأدوار قبل قفل التيم (Edit Team Seats Screen)
 */
@Composable
fun EditTeamSeatsScreen(
    seats: List<Seat> = MockDataProvider.sampleSeats,
    onSaveSeatsClick: (List<Seat>) -> Unit = {},
    onDeleteSeatClick: (seatId: String) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    // TODO (يوسف): صمم واجهة تعديل الأدوار المطلوبة للمقاعد الشاغرة
    EmptyScreenTemplate(
        screenTitle = "إدارة وتعديل مقاعد التيم",
        ownerName = "يوسف (Youssef)",
        onBackClick = onBackClick,
        hints = listOf(
            "عرض قائمة المقاعد غير المحجوزة (AVAILABLE)",
            "إمكانية تعديل التخصص المطلوب للمقعد (مثلاً تغيير من Flutter إلى Android)",
            "أيقونة سلة مهملات لحذف المقعد الفارغ لتقليل عدد التيم",
            "زر (حفظ التعديلات) يستدعي onSaveSeatsClick(seats)"
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun EditTeamSeatsScreenPreview() {
    TeamSeatTheme {
        EditTeamSeatsScreen()
    }
}
