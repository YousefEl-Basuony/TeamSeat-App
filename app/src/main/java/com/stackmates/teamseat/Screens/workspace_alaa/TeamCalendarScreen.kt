package com.stackmates.teamseat.Screens.workspace_alaa

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.stackmates.teamseat.core.components.EmptyScreenTemplate
import com.stackmates.teamseat.core.data.MockDataProvider
import com.stackmates.teamseat.core.model.CalendarEvent
import com.stackmates.teamseat.core.theme.TeamSeatTheme

/**
 * 📑 المسؤول عن الشاشة: آلاء (Alaa) — [ALAA-2]
 * جدول تسليمات ومناقشات التيم (Team Calendar Screen)
 */
@Composable
fun TeamCalendarScreen(
    events: List<CalendarEvent> = MockDataProvider.sampleCalendarEvents,
    onAddEventClick: (title: String, date: String, time: String) -> Unit = { _, _, _ -> },
    onBackClick: () -> Unit = {}
) {
    // TODO (آلاء): امسحي EmptyScreenTemplate وصممي جدول مواعيد التيم هنا
    EmptyScreenTemplate(
        screenTitle = "جدول ومواعيد التيم",
        ownerName = "آلاء (Alaa)",
        onBackClick = onBackClick,
        hints = listOf(
            "LazyColumn يعرض مواعيد المناقشات والتسليمات (events)",
            "كل كارت يعرض عنوان الموعد، التاريخ، الوقت، ونوع الحدث",
            "FloatingActionButton (+) لإضافة موعد تسليم أو ميتنج جديد"
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun TeamCalendarScreenPreview() {
    TeamSeatTheme {
        TeamCalendarScreen()
    }
}
