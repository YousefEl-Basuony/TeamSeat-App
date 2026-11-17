package com.stackmates.teamseat.Screens.requests_hossam

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.stackmates.teamseat.core.components.EmptyScreenTemplate
import com.stackmates.teamseat.core.data.MockDataProvider
import com.stackmates.teamseat.core.model.NotificationItem
import com.stackmates.teamseat.core.theme.TeamSeatTheme

/**
 * ⚡ المسؤول عن الشاشة: حسام (Hossam) — [HOSSAM-4]
 * شاشة مركز الإشعارات الفورية (Notifications Screen)
 */
@Composable
fun NotificationsScreen(
    notifications: List<NotificationItem> = MockDataProvider.sampleNotifications,
    onBackClick: () -> Unit = {}
) {
    // TODO (حسام): امسح EmptyScreenTemplate وصمم قائمة الإشعارات هنا
    EmptyScreenTemplate(
        screenTitle = "مركز الإشعارات",
        ownerName = "حسام (Hossam)",
        onBackClick = onBackClick,
        hints = listOf(
            "LazyColumn يعرض عناصر notifications",
            "تلوين الأيقونة حسب النوع: قبول أخضر ✅، طلب معلق أصفر ⏳، إغلاق التيم أزرق 🔒",
            "عرض العنوان والرسالة والوقت المنقضي (timeAgo)"
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun NotificationsScreenPreview() {
    TeamSeatTheme {
        NotificationsScreen()
    }
}
