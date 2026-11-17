package com.stackmates.teamseat.Screens.explore_khaled

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.stackmates.teamseat.core.components.EmptyScreenTemplate
import com.stackmates.teamseat.core.data.MockDataProvider
import com.stackmates.teamseat.core.model.Seat
import com.stackmates.teamseat.core.model.Team
import com.stackmates.teamseat.core.theme.TeamSeatTheme

/**
 * 🔍 المسؤول عن الشاشة: خالد (Khaled) — [KHALED-3]
 * شاشة تفاصيل المشروع الشاملة قبل فتح خريطة الكراسي (Team Details Overview Screen)
 */
@Composable
fun TeamDetailsOverviewScreen(
    team: Team = MockDataProvider.sampleTeam,
    seats: List<Seat> = MockDataProvider.sampleSeats,
    onOpenSeatMapClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    // TODO (خالد): امسح EmptyScreenTemplate وصمم صفحة تفاصيل التيم ووصف المشروع هنا
    EmptyScreenTemplate(
        screenTitle = "تفاصيل مشروع: ${team.projectName}",
        ownerName = "خالد (Khaled)",
        onBackClick = onBackClick,
        hints = listOf(
            "عرض اسم المشروع، المادة (${team.category})، واسم القائد (${team.leaderName})",
            "عرض وصف فكرة المشروع بالكامل (${team.description})",
            "ملخص سريع لعدد المقاعد المتاحة مقابل المحجوزة",
            "زر عريض وبارز بالأسفل: (افتح خريطة المقاعد واحجز دورك 💺) يستدعي onOpenSeatMapClick()"
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun TeamDetailsOverviewScreenPreview() {
    TeamSeatTheme {
        TeamDetailsOverviewScreen()
    }
}
