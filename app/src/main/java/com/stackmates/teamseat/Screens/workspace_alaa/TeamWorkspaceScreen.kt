package com.stackmates.teamseat.Screens.workspace_alaa

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.stackmates.teamseat.core.components.EmptyScreenTemplate
import com.stackmates.teamseat.core.data.MockDataProvider
import com.stackmates.teamseat.core.model.Seat
import com.stackmates.teamseat.core.model.Team
import com.stackmates.teamseat.core.theme.TeamSeatTheme

/**
 * 📑 المسؤول عن الشاشة: آلاء (Alaa) — [ALAA-4]
 * مساحة عمل التيم المعتمد (Team Workspace Dashboard)
 */
@Composable
fun TeamWorkspaceScreen(
    team: Team = MockDataProvider.sampleTeam,
    seats: List<Seat> = MockDataProvider.sampleSeats,
    onOpenCalendarClick: () -> Unit = {},
    onOpenTASheetClick: () -> Unit = {},
    onKickMemberClick: (seatId: String, reason: String) -> Unit = { _, _ -> },
    onBackClick: () -> Unit = {}
) {
    // TODO (آلاء): امسحي EmptyScreenTemplate وصممي لوحة تحكم التيم المعتمد هنا
    EmptyScreenTemplate(
        screenTitle = "مساحة عمل: ${team.projectName}",
        ownerName = "آلاء (Alaa)",
        onBackClick = onBackClick,
        hints = listOf(
            "شريط نسبة اكتمال مقاعد التيم (Booked Seats / Total Seats)",
            "كروت روابط التيم السريعة (GitHub Repo & Google Drive)",
            "قائمة الأعضاء المقبولين بأدوارهم مع خيار طرد عضو (قبل قفل التيم)",
            "زر للانتقال لجدول مواعيد التيم + زر لعرض كشف التسليم للمعيدة (onOpenTASheetClick)"
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun TeamWorkspaceScreenPreview() {
    TeamSeatTheme {
        TeamWorkspaceScreen()
    }
}
