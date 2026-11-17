package com.stackmates.teamseat.Screens.workspace_alaa

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.stackmates.teamseat.core.components.EmptyScreenTemplate
import com.stackmates.teamseat.core.data.MockDataProvider
import com.stackmates.teamseat.core.model.Team
import com.stackmates.teamseat.core.theme.TeamSeatTheme

/**
 * 📑 المسؤول عن الشاشة: آلاء (Alaa) — [ALAA-3]
 * شاشة المحادثة الجماعية الداخلية لأعضاء التيم المعتمدين (In-Team Group Chat Screen)
 */
@Composable
fun TeamGroupChatScreen(
    team: Team = MockDataProvider.sampleTeam,
    onSendMessageClick: (messageText: String) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    // TODO (آلاء): امسحي EmptyScreenTemplate وصممي شاشة شات التيم الداخلي هنا
    EmptyScreenTemplate(
        screenTitle = "شات تيم: ${team.projectName}",
        ownerName = "آلاء (Alaa)",
        onBackClick = onBackClick,
        hints = listOf(
            "LazyColumn يعرض فقاعات الرسائل (Chat Bubbles) مع اسم العضو وتخصصه والوقت",
            "تمييز رسائل المستخدم الحالي بلون مختلف على اليمين ورسائل باقي التيم على اليسار",
            "شريط سفلي يحتوي على TextField لكتابة رسالة + زر إرسال أيقونة Send"
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun TeamGroupChatScreenPreview() {
    TeamSeatTheme {
        TeamGroupChatScreen()
    }
}
