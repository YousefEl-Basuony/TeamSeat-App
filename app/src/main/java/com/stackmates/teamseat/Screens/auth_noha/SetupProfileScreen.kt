package com.stackmates.teamseat.Screens.auth_noha

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.stackmates.teamseat.core.components.EmptyScreenTemplate
import com.stackmates.teamseat.core.theme.TeamSeatTheme

/**
 * 🌸 المسؤول عن الشاشة: نُهى (Noha) — [NOHA-4]
 * شاشة استكمال البروفايل واختيار التخصص (Setup Profile Screen)
 */
@Composable
fun SetupProfileScreen(
    availableRoles: List<String> = listOf("Android", "Backend", "UI/UX", "Flutter", "AI / ML", "QA"),
    onSaveProfileClick: (selectedRole: String, githubUrl: String) -> Unit = { _, _ -> },
    onBackClick: () -> Unit = {}
) {
    // TODO (نُهى): امسحي EmptyScreenTemplate وصممي اختيار المهارات هنا
    EmptyScreenTemplate(
        screenTitle = "تحديد التخصص والمهارات",
        ownerName = "نُهى (Noha)",
        onBackClick = onBackClick,
        hints = listOf(
            "عرض المهارات (availableRoles) في شكل FilterChips ملونة ليختار الطالب تخصصه",
            "حقل اختياري لوضع رابط حساب GitHub أو Portfolio",
            "زر (حفظ والبدء) يستدعي onSaveProfileClick(selectedRole, githubUrl)"
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun SetupProfileScreenPreview() {
    TeamSeatTheme {
        SetupProfileScreen()
    }
}
