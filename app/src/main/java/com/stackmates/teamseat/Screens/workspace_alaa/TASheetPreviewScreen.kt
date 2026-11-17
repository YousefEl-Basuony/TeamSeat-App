package com.stackmates.teamseat.Screens.workspace_alaa

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.stackmates.teamseat.core.components.EmptyScreenTemplate
import com.stackmates.teamseat.core.data.MockDataProvider
import com.stackmates.teamseat.core.model.Seat
import com.stackmates.teamseat.core.model.Team
import com.stackmates.teamseat.core.theme.TeamSeatTheme

/**
 * 📑 المسؤول عن الشاشة: آلاء (Alaa) — [ALAA-5]
 * شاشة معاينة كشف التسليم الرسمي للمعيدة والدكتور (TA Sheet A4 Preview Screen)
 */
@Composable
fun TASheetPreviewScreen(
    team: Team = MockDataProvider.sampleTeam,
    seats: List<Seat> = MockDataProvider.sampleSeats,
    onLockTeamClick: () -> Unit = {},
    onExportPdfClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    // TODO (آلاء): امسحي EmptyScreenTemplate وصممي ورقة A4 رسمية أنيقة هنا
    EmptyScreenTemplate(
        screenTitle = "كشف اعتماد التيم الرسمي (TA Sheet)",
        ownerName = "آلاء (Alaa)",
        onBackClick = onBackClick,
        hints = listOf(
            "تصميم كارت أبيض يشبه ورقة A4 الرسمية بإطار أنيق",
            "الهيدر: اسم المشروع (${team.projectName}) — المشرف: ${team.supervisorDoctor} — المعيدة: ${team.teachingAssistant}",
            "جدول منظم يعرض: رقم المقعد | اسم الطالب | التخصص (Role)",
            "زر القائد: (اعتماد وإغلاق التيم نهائياً 🔒) يستدعي onLockTeamClick()",
            "زر (تصدير ومشاركة PDF 📄) يستدعي onExportPdfClick()"
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
