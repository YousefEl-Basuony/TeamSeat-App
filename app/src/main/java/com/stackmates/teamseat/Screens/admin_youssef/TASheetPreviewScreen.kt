package com.stackmates.teamseat.Screens.admin_youssef

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.stackmates.teamseat.core.components.EmptyScreenTemplate
import com.stackmates.teamseat.core.data.MockDataProvider
import com.stackmates.teamseat.core.model.Seat
import com.stackmates.teamseat.core.model.Team
import com.stackmates.teamseat.core.theme.TeamSeatTheme

/**
 * 💻 المسؤول عن الشاشة: يوسف (Youssef - Team Lead & Backend)
 * شاشة كشف التسليم الرسمي للمعيدة م. شهد أحمد والدكتور د. محمد كاظم (A4 Sheet Layout)
 */
@Composable
fun TASheetPreviewScreen(
    team: Team = MockDataProvider.sampleTeam,
    seats: List<Seat> = MockDataProvider.sampleSeats,
    onLockTeamClick: () -> Unit = {},
    onExportPdfClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    EmptyScreenTemplate(
        screenTitle = "كشف اعتماد التيم الرسمي (TA Sheet)",
        ownerName = "يوسف (Youssef)",
        onBackClick = onBackClick,
        hints = listOf(
            "كشف تسليم A4 رسمي"
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun TASheetPreviewScreenPreview() {
    TeamSeatTheme {
        TASheetPreviewScreen()
    }
}
