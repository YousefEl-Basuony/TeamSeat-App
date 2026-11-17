package com.stackmates.teamseat.Screens.auth_noha

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.stackmates.teamseat.core.components.EmptyScreenTemplate
import com.stackmates.teamseat.core.theme.TeamSeatTheme

/**
 * 🌸 المسؤول عن الشاشة: نُهى (Noha) — [NOHA-3]
 * شاشة إنشاء حساب جديد (Register Screen)
 */
@Composable
fun RegisterScreen(
    isLoading: Boolean = false,
    onRegisterClick: (name: String, studentId: String, email: String, password: String) -> Unit = { _, _, _, _ -> },
    onBackToLogin: () -> Unit = {}
) {
    // TODO (نُهى): امسحي EmptyScreenTemplate وصممي فورم إنشاء الحساب هنا
    EmptyScreenTemplate(
        screenTitle = "إنشاء حساب طالب جديد",
        ownerName = "نُهى (Noha)",
        onBackClick = onBackToLogin,
        hints = listOf(
            "حقل الاسم الثلاثي للطالب",
            "حقل الرقم الجامعي (Student ID)",
            "حقل البريد الإلكتروني الأكاديمي",
            "حقل كلمة المرور وتأكيد كلمة المرور",
            "زر (متابعة) يستدعي onRegisterClick(...)"
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun RegisterScreenPreview() {
    TeamSeatTheme {
        RegisterScreen()
    }
}
