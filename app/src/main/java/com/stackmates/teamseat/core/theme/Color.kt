package com.stackmates.teamseat.core.theme

import androidx.compose.ui.graphics.Color

// =========================================================================
// 🎨 TeamSeat Official Color Palette (من الهوية البصرية المعتمدة للتطبيق)
// =========================================================================

// الألوان الداكنة والأساسية
val DeepNavy = Color(0xFF0A344A)      // العناوين الرئيسية والخلفيات الداكنة
val DeepTeal = Color(0xFF1B6F7D)      // الأزرار الأساسية وشريط التنقل Navbar
val Turquoise = Color(0xFF469D9F)     // الأيقونات والعناصر النشطة
val LightAqua = Color(0xFFA9D5D4)     // الخلفيات الثانوية

// الألوان الفاتحة والخلفيات
val WarmCream = Color(0xFFFCF2EA)     // الخلفية الرئيسية للتطبيق
val SoftBeige = Color(0xFFF4E2DD)     // الكروت وحقول الإدخال Input Fields
val Peach = Color(0xFFF4C195)         // لمسات جمالية وأزرار ثانوية
val SoftGray = Color(0xFFD3CFD1)      // الحدود والعناصر غير النشطة

// ألوان متوافقة مع Material 3 Theme Names
val BrandPrimary = DeepTeal
val BrandSecondary = Turquoise
val BrandBackground = WarmCream
val BrandSurface = SoftBeige

// =========================================================================
// 💺 ألوان حالات مقاعد التيم الموحدة (Interactive Seat Map)
// =========================================================================
val SeatAvailableGreen = Turquoise      // 🟢 متاح للحجز
val SeatAvailableBg = LightAqua

val SeatPendingAmber = Peach            // 🟡 قيد المراجعة (48 ساعة)
val SeatPendingBg = WarmCream

val SeatBookedBlue = DeepNavy           // 🔵 محجوز ومعتمد
val SeatBookedBg = SoftGray

val SeatLockedGray = SoftGray           // 🔒 التيم مقفل نهائياً
val SeatLockedBg = SoftBeige

val LeaderCrownGold = Peach             // 👑 لون تاج قائد التيم
