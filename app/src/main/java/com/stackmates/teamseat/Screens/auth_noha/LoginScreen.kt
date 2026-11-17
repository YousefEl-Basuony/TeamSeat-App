package com.stackmates.teamseat.Screens.auth_noha

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.stackmates.teamseat.core.components.EmptyScreenTemplate
import com.stackmates.teamseat.core.theme.TeamSeatTheme

/**
 * 🌸 المسؤول عن الشاشة: نُهى (Noha) — [NOHA-2]
 * شاشة تسجيل الدخول (Login Screen)
 */
@Composable
fun LoginScreen(
    isLoading: Boolean = false,
    errorMessage: String? = null,
    onLoginClick: (emailOrStudentId: String, password: String) -> Unit = { _, _ -> },
    onNavigateToRegister: () -> Unit = {}
) {
    // TODO (نُهى): امسحي EmptyScreenTemplate وصممي واجهة تسجيل الدخول هنا
    EmptyScreenTemplate(
        screenTitle = "تسجيل الدخول (Login)",
        ownerName = "نُهى (Noha)",
        hints = listOf(
            "OutlinedTextField لإدخال الإيميل الجامعي أو الرقم الجامعي",
            "OutlinedTextField لكلمة المرور مع أيقونة إظهار/إخفاء الباسورد",
            "Button أساسي لتسجيل الدخول يستدعي onLoginClick(email, password)",
            "TextButton في الأسفل: (ليس لديك حساب؟ سجل الآن) يستدعي onNavigateToRegister()"
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    TeamSeatTheme {
        LoginScreen()
    }
}
