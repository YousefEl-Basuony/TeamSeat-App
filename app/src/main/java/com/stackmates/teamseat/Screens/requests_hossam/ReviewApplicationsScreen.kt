package com.stackmates.teamseat.Screens.requests_hossam

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.stackmates.teamseat.core.components.EmptyScreenTemplate
import com.stackmates.teamseat.core.data.MockDataProvider
import com.stackmates.teamseat.core.model.Seat
import com.stackmates.teamseat.core.model.SeatStatus
import com.stackmates.teamseat.core.theme.TeamSeatTheme

/**
 * ⚡ المسؤول عن الشاشة: حسام (Hossam) — [HOSSAM-5]
 * شاشة القائد لمراجعة وقبول أو رفض طلبات الانضمام للمقاعد (Review Pending Applications)
 */
@Composable
fun ReviewApplicationsScreen(
    pendingSeats: List<Seat> = MockDataProvider.sampleSeats.filter { it.status == SeatStatus.PENDING },
    onAcceptApplicantClick: (Seat) -> Unit = {},
    onRejectApplicantClick: (Seat) -> Unit = {},
    onViewApplicantProfile: (applicantId: String) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    // TODO (حسام): امسح EmptyScreenTemplate وصمم كروت مراجعة المتقدمين للقائد هنا
    EmptyScreenTemplate(
        screenTitle = "مراجعة طلبات المقاعد (للقائد)",
        ownerName = "حسام (Hossam)",
        onBackClick = onBackClick,
        hints = listOf(
            "عرض قائمة المقاعد المعلقة (pendingSeats)",
            "في كل كارت: اسم الطالب المتقدم، رقم وتخصص المقعد، ورسالة التقديم (applicantNote)",
            "زر لمعاينة بروفايل الطالب (يفتح StudentPublicProfileSheet)",
            "زر أخضر (قبول وتثبيت المقعد ✅) + زر أحمر (رفض ❌)"
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun ReviewApplicationsScreenPreview() {
    TeamSeatTheme {
        ReviewApplicationsScreen()
    }
}
