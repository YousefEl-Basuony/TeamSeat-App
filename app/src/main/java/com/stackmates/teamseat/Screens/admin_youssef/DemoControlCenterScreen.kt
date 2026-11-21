package com.stackmates.teamseat.Screens.admin_youssef

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.stackmates.teamseat.core.components.EmptyScreenTemplate
import com.stackmates.teamseat.core.data.MockDataProvider
import com.stackmates.teamseat.core.model.DemoPersona
import com.stackmates.teamseat.core.model.User
import com.stackmates.teamseat.core.theme.TeamSeatTheme

/**
 * 💻 المسؤول عن الشاشة: يوسف (Youssef - Team Lead & Backend)
 * مركز التحكم في العرض التوضيحي السريع للمناقشة (Live Demo Control Center)
 */
@Composable
fun DemoControlCenterScreen(
    currentPersona: DemoPersona = DemoPersona.AHMED_LEADER,
    currentUser: User = MockDataProvider.ahmedLeader,
    onTogglePersonaClick: () -> Unit = {},
    onResetMockDataClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    EmptyScreenTemplate(
        screenTitle = "مركز تحكم المناقشة (Live Demo)",
        ownerName = "يوسف (Youssef)",
        onBackClick = onBackClick,
        hints = listOf(
            "عرض الحساب النشط والتبديل اللحظي"
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun DemoControlCenterScreenPreview() {
    TeamSeatTheme {
        DemoControlCenterScreen()
    }
}
