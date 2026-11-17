package com.stackmates.teamseat.Screens.explore_khaled

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.stackmates.teamseat.core.components.EmptyScreenTemplate
import com.stackmates.teamseat.core.data.MockDataProvider
import com.stackmates.teamseat.core.model.Team
import com.stackmates.teamseat.core.theme.TeamSeatTheme

/**
 * 🔍 المسؤول عن الشاشة: خالد (Khaled) — [KHALED-2]
 * شاشة استعراض تيمات مادة دراسية محددة (Course / Subject Hub Screen)
 */
@Composable
fun CourseHubScreen(
    courseName: String = "مشروع تخرج حاسبات 2026",
    supervisorName: String = "د. محمد كاظم & م. شهد أحمد",
    teamsInCourse: List<Team> = MockDataProvider.sampleTeamsList,
    onTeamClick: (teamId: String) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    // TODO (خالد): امسح EmptyScreenTemplate وصمم شاشة المادة الدراسية وتيماتها هنا
    EmptyScreenTemplate(
        screenTitle = "مادة: $courseName",
        ownerName = "خالد (Khaled)",
        onBackClick = onBackClick,
        hints = listOf(
            "كارت علوي باسم المادة واسم الدكتور والمعيدة وعدد التيمات المسجلة",
            "LazyColumn يعرض كل التيمات المسجلة تحت هذه المادة (teamsInCourse)",
            "الضغط على أي تيم يستدعي onTeamClick(team.teamId)"
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun CourseHubScreenPreview() {
    TeamSeatTheme {
        CourseHubScreen()
    }
}
