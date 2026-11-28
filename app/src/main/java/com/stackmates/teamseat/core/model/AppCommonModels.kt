package com.stackmates.teamseat.core.model

enum class NotificationType {
    ACCEPTED,   // تم القبول في مقعد التيم ✅
    REJECTED,   // تم رفض الطلب ❌
    NEW_REQUEST,// طلب حجز جديد ورد للقائد 📩
    TEAM_LOCKED // تم إغلاق التيم واعتماده 🔒
}

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val timeAgo: String,
    val type: NotificationType,
    val isRead: Boolean = false
)

data class CalendarEvent(
    val id: String,
    val teamId: String,
    val title: String,
    val date: String,
    val time: String,
    val type: String // "مناقشة", "تسليم ديدلاين", "ميتنج"
)

data class TeamChatMessage(
    val id: String = "",
    val teamId: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val text: String = "",
    val timeLabel: String = "",
    val timestampMs: Long = System.currentTimeMillis()
)
