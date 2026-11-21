package com.stackmates.teamseat.Screens.explore_khaled

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.stackmates.teamseat.core.components.EmptyScreenTemplate
import com.stackmates.teamseat.core.data.MockDataProvider
import com.stackmates.teamseat.core.model.Team
import com.stackmates.teamseat.core.model.User
import com.stackmates.teamseat.core.theme.TeamSeatTheme

/**
 * 🔍 المسؤول عن الشاشة: خالد (Khaled) — [KHALED-4]
 * الشاشة الرئيسية واستكشاف التيمات (Home / Teams Feed Screen)
 */
@Composable
fun HomeScreen(
    currentUser: User = MockDataProvider.saraStudent,
    teams: List<Team> = MockDataProvider.sampleTeamsList,
    onTeamClick: (teamId: String) -> Unit = {},
    onCreateTeamClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onMyApplicationsClick: () -> Unit = {},
    onSwitchPersona: () -> Unit = {}
) {
    // TODO (خالد): امسح EmptyScreenTemplate وصمم الشاشة الرئيسية هنا باستخدام البيانات الجاهزة فوق
    EmptyScreenTemplate(
        screenTitle = "",
        ownerName = "خالد (Khaled)",
        hints = listOf(
            "وضع DemoSwitcherBar في الأعلى لعرض المستخدم الحالي والتبديل السريع",
            "قسم علوي للكراسي العاجلة المطلوبة (Urgent Seats)",
            "LazyColumn يعرض كروت التيمات (teams): اسم المشروع، المادة، واسم القائد",
            "الضغط على أي كارت تيم يستدعي onTeamClick(team.teamId)",
            "FloatingActionButton (+) لإنشاء تيم جديد يستدعي onCreateTeamClick()"
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    TeamSeatTheme {
        HomeScreen()
    }
}
