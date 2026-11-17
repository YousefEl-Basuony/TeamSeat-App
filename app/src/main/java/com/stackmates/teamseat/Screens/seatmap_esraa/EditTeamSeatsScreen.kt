package com.stackmates.teamseat.Screens.seatmap_esraa

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.stackmates.teamseat.core.components.EmptyScreenTemplate
import com.stackmates.teamseat.core.data.MockDataProvider
import com.stackmates.teamseat.core.model.Seat
import com.stackmates.teamseat.core.theme.TeamSeatTheme

/**
 * 💺 المسؤول عن الشاشة: إسراء (Esraa)
 * تعديل مقاعد التيم الفارغة قبل إغلاق التيم
 */
@Composable
fun EditTeamSeatsScreen(
    seats: List<Seat> = MockDataProvider.sampleSeats,
    onSaveSeatsClick: (List<Seat>) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    // TODO (إسراء): امسحي EmptyScreenTemplate وصممي شاشة تعديل مسميات المقاعد الفارغة
    EmptyScreenTemplate(
        screenTitle = "تعديل مقاعد التيم",
        ownerName = "إسراء (Esraa)",
        onBackClick = onBackClick,
        hints = listOf(
            "عرض مقاعد التيم مع إمكانية تعديل التخصص أو حذف المقعد طالما حالته AVAILABLE",
            "زر (حفظ التعديلات) يستدعي onSaveSeatsClick(...)"
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
