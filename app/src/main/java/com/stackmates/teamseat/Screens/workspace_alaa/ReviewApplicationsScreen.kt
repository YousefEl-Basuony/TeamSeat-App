package com.stackmates.teamseat.Screens.workspace_alaa

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.stackmates.teamseat.core.components.EmptyScreenTemplate
import com.stackmates.teamseat.core.data.MockDataProvider
import com.stackmates.teamseat.core.model.Seat
import com.stackmates.teamseat.core.model.SeatStatus
import com.stackmates.teamseat.core.theme.TeamSeatTheme

/**
 * 📑 المسؤول عن الشاشة: آلاء (Alaa)
 * شاشة القائد لمراجعة وقبول أو رفض طلبات الانضمام للمقاعد (Review Pending Applications)
 */
@Composable
fun ReviewApplicationsScreen(
    pendingSeats: List<Seat> = MockDataProvider.sampleSeats.filter { it.status == SeatStatus.PENDING },
    onAcceptApplicantClick: (Seat) -> Unit = {},
    onRejectApplicantClick: (Seat) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    // TODO (آلاء): صممي كروت مراجعة المتقدمين للقائد هنا مع زري القبول والرفض
    EmptyScreenTemplate(
        screenTitle = "مراجعة طلبات المقاعد (للقائد)",
        ownerName = "آلاء (Alaa)",
        onBackClick = onBackClick,
        hints = listOf(
            "عرض قائمة المقاعد المعلقة (pendingSeats)",
            "في كل كارت: اسم الطالب المتقدم، رقم وتخصص المقعد، ورسالة التقديم (applicantNote)",
            "زر أخضر (قبول وتثبيت المقعد ✅) يستدعي onAcceptApplicantClick(seat)",
            "زر أحمر (رفض ❌) يستدعي onRejectApplicantClick(seat)"
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
