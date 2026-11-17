package com.stackmates.teamseat.Screens.requests_hossam

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.stackmates.teamseat.core.components.EmptyScreenTemplate
import com.stackmates.teamseat.core.data.MockDataProvider
import com.stackmates.teamseat.core.model.User
import com.stackmates.teamseat.core.theme.TeamSeatTheme

/**
 * ⚡ المسؤول عن الشاشة: حسام (Hossam) — [HOSSAM-1]
 * شاشة ملفي الشخصي وتعديل مهاراتي (My Profile Screen)
 */
@Composable
fun MyProfileScreen(
    user: User = MockDataProvider.saraStudent,
    onSaveChangesClick: (updatedRole: String, updatedGithub: String) -> Unit = { _, _ -> },
    onLogoutClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    // TODO (حسام): امسح EmptyScreenTemplate وصمم شاشة البروفايل الشخصي للطالب هنا
    EmptyScreenTemplate(
        screenTitle = "ملفي الشخصي",
        ownerName = "حسام (Hossam)",
        onBackClick = onBackClick,
        hints = listOf(
            "صورة رمزية دائرية (Avatar) مع اسم الطالب (${user.name}) والرقم الجامعي (${user.studentId})",
            "عرض التخصص الأساسي (${user.roleTag}) ورابط GitHub مع إمكانية التعديل",
            "عرض حالة الانضمام الحالية (منضم لتيم أو يبحث عن مقعد)",
            "زر حفظ التعديلات + زر تسجيل الخروج"
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun MyProfileScreenPreview() {
    TeamSeatTheme {
        MyProfileScreen()
    }
}
