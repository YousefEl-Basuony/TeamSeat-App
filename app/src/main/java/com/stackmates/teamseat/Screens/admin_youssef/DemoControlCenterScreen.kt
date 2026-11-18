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
    // TODO (يوسف): صمم لوحة تحكم سريعة للتبديل اللحظي بين حساب القائد والطالبة وقت المناقشة
    EmptyScreenTemplate(
        screenTitle = "مركز تحكم المناقشة (Live Demo)",
        ownerName = "يوسف (Youssef)",
        onBackClick = onBackClick,
        hints = listOf(
            "عرض الحساب النشط حالياً: ${currentUser.name} (${currentUser.roleTag})",
            "زر بارز للتبديل الفوري بين (أحمد - ليدر التيم) و(سارة - طالبة متقدمة)",
            "زر لإعادة ضبط مقاعد التيم الافتراضية (Reset Mock Seats)",
            "إحصائيات سريعة: عدد المقاعد المتاحة والمحجوزة والمعلقة"
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
