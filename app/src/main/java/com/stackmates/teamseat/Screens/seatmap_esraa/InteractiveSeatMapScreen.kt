package com.stackmates.teamseat.Screens.seatmap_esraa

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.stackmates.teamseat.core.components.EmptyScreenTemplate
import com.stackmates.teamseat.core.data.MockDataProvider
import com.stackmates.teamseat.core.model.Seat
import com.stackmates.teamseat.core.model.Team
import com.stackmates.teamseat.core.theme.TeamSeatTheme

/**
 * 💺 المسؤول عن الشاشة: إسراء (Esraa) — [ESRAA-4]
 * أهم شاشة في التطبيق: خريطة المقاعد التفاعلية بنظام السينما (Interactive Seat Map Screen)
 */
@Composable
fun InteractiveSeatMapScreen(
    team: Team = MockDataProvider.sampleTeam,
    seats: List<Seat> = MockDataProvider.sampleSeats,
    isCurrentUserLeader: Boolean = false,
    onSeatClick: (Seat) -> Unit = {},
    onOpenWorkspaceClick: () -> Unit = {},
    onReviewRequestsClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    // TODO (إسراء): امسحي EmptyScreenTemplate وصممي خريطة الكراسي التفاعلية هنا
    // الألوان جاهزة في ملف Color.kt: SeatAvailableGreen, SeatPendingAmber, SeatBookedBlue, LeaderCrownGold
    EmptyScreenTemplate(
        screenTitle = "خريطة مقاعد: ${team.projectName}",
        ownerName = "إسراء (Esraa)",
        onBackClick = onBackClick,
        hints = listOf(
            "كارت مميز بالأعلى لقائد التيم (${team.leaderName}) مع أيقونة تاج 👑",
            "شبكة عمودين LazyVerticalGrid(GridCells.Fixed(2)) لعرض المقاعد (seats)",
            "كل كرسي يعرض: رقم المقعد، التخصص المطلوب (roleRequired)، ولون الحالة (أخضر متاح / أصفر معلق / أزرق محجوز)",
            "عند الضغط على كرسي متاح: استدعاء onSeatClick(seat) لفتح SeatBookingBottomSheet"
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun InteractiveSeatMapScreenPreview() {
    TeamSeatTheme {
        InteractiveSeatMapScreen()
    }
}
