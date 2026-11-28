package com.stackmates.teamseat.core.model

/**
 * بيانات الطالب المسجل في التطبيق
 */
data class User(
    val uid: String,
    val name: String,
    val studentId: String,          // الرقم الجامعي
    val email: String,
    val roleTag: String,            // التخصص الأساسي: Android, Backend, UI/UX...
    val currentTeamId: String? = null, // التيم الذي تم قبوله فيه رسمياً (إذا وُجد)
    val githubUrl: String = "",
    val linkedinUrl: String = "",
    val isLeader: Boolean = false,
    val avatarEmoji: String = if (isLeader) "👨‍💻" else "👩‍💻",
    val major: String = "علوم حاسب (Computer Science)",
    val isBanned: Boolean = false,
    val isAdmin: Boolean = false,
    val photoBase64: String = ""
)

/**
 * شخصيات العرض التوضيحي السريع للمناقشة (Live Demo)
 */
enum class DemoPersona {
    AHMED_LEADER,   // أحمد - قائد التيم
    SARA_STUDENT    // سارة - طالبة تبحث عن تيم
}
