package com.stackmates.teamseat.Screens.explore_khaled

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.stackmates.teamseat.core.components.EmptyScreenTemplate
import com.stackmates.teamseat.core.data.MockDataProvider
import com.stackmates.teamseat.core.model.Team
import com.stackmates.teamseat.core.theme.TeamSeatTheme

/**
 * 🔍 المسؤول عن الشاشة: خالد (Khaled) — [KHALED-5]
 * شاشة البحث والفلترة المتقدمة (Search & Filter Screen)
 */
@Composable
fun SearchAndFilterScreen(
    teams: List<Team> = MockDataProvider.sampleTeamsList,
    roleFilters: List<String> = listOf("الكل", "Android", "Backend", "UI/UX", "Flutter", "AI"),
    onTeamClick: (teamId: String) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    // TODO (خالد): امسح EmptyScreenTemplate وصمم شاشة البحث والفلترة هنا
    EmptyScreenTemplate(
        screenTitle = "البحث والفلترة",
        ownerName = "خالد (Khaled)",
        onBackClick = onBackClick,
        hints = listOf(
            "شريط بحث بالأعلى للبحث باسم المشروع أو اسم المادة",
            "LazyRow يعرض أزرار الفلترة حسب التخصص (roleFilters)",
            "LazyColumn يعرض نتائج التيمات المطابقة للبحث"
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun SearchAndFilterScreenPreview() {
    TeamSeatTheme {
        SearchAndFilterScreen()
    }
}
