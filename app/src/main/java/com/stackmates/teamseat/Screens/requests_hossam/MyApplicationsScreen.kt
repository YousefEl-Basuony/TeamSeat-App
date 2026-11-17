package com.stackmates.teamseat.Screens.requests_hossam

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.stackmates.teamseat.core.components.EmptyScreenTemplate
import com.stackmates.teamseat.core.data.MockDataProvider
import com.stackmates.teamseat.core.model.Seat
import com.stackmates.teamseat.core.theme.TeamSeatTheme

/**
 * ⚡ المسؤول عن الشاشة: حسام (Hossam) — [HOSSAM-3]
 * شاشة متابعة طلباتي المعلقة (My Sent Applications - حد أقصى 3 طلبات)
 */
@Composable
fun MyApplicationsScreen(
    myAppliedSeats: List<Seat> = MockDataProvider.sampleSeats.take(3),
    onCancelApplicationClick: (seatId: String) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    // TODO (حسام): امسح EmptyScreenTemplate وصمم كروت متابعة طلبات الحجز هنا
    EmptyScreenTemplate(
        screenTitle = "طلباتي وحجوزاتي",
        ownerName = "حسام (Hossam)",
        onBackClick = onBackClick,
        hints = listOf(
            "عداد بالأعلى يوضح الحد الأقصى: الطلبات المعلقة النشطة (${myAppliedSeats.size} / 3)",
            "LazyColumn يعرض المقاعد التي قدم عليها الطالب (myAppliedSeats)",
            "شارة حالة ملونة (قيد المراجعة 🟡 / مقبول 🔵) + الوقت المتبقي من مهلة الـ 48 ساعة",
            "زر (إلغاء وسحب الطلب) يستدعي onCancelApplicationClick(seat.id)"
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun MyApplicationsScreenPreview() {
    TeamSeatTheme {
        MyApplicationsScreen()
    }
}
