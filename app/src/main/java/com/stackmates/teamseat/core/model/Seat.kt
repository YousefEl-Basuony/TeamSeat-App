package com.stackmates.teamseat.core.model

/**
 * حالات المقعد في نظام حجز مقاعد التيمات
 */
enum class SeatStatus {
    AVAILABLE,  // متاح للحجز 🟢
    PENDING,    // قيد المراجعة بانتظار موافقة القائد 🟡
    BOOKED,     // محجوز ومعتمد رسمياً باسم الطالب 🔵
    LOCKED      // مقفل (تم إغلاق التيم) 🔒
}

/**
 * يمثل مقعد واحد داخل التيم (نظام السينما / الباص)
 */
data class Seat(
    val id: String,
    val teamId: String,
    val seatNumber: Int,
    val roleRequired: String,       // مثال: "Android Developer", "Backend", "UI/UX"
    val status: SeatStatus = SeatStatus.AVAILABLE,
    val applicantId: String? = null,
    val applicantName: String? = null,
    val applicantNote: String? = null,
    val expiresAt: Long? = null     // تاريخ انتهاء مهلة الـ 48 ساعة
) {
    val seatLabel: String get() = "S$seatNumber"
    val roleTag: String get() = roleRequired
    val assignedStudentName: String? get() = applicantName
    val assignedStudentId: String? get() = applicantId
    val isLeader: Boolean get() = seatNumber == 1
}
