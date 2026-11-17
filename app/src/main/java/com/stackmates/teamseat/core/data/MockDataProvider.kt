package com.stackmates.teamseat.core.data

import com.stackmates.teamseat.core.model.*

/**
 * بيانات تجريبية جاهزة (Mock Data)
 * يستخدمها فريق الـ UI (نُهى، خالد، إسراء، آلاء) في الـ @Preview
 * لتصميم الشاشات فوراً بدون الحاجة للانتظار أو الاتصال بالإنترنت!
 */
object MockDataProvider {

    // 1. شخصيات العرض التوضيحي (Demo Personas)
    val ahmedLeader = User(
        uid = "user_ahmed_101",
        name = "أحمد محمود (قائد التيم)",
        studentId = "20220145",
        email = "ahmed.leader@cis.edu.eg",
        roleTag = "Android Lead",
        currentTeamId = "team_alpha_2026",
        githubUrl = "https://github.com/ahmed-leader",
        isLeader = true
    )

    val saraStudent = User(
        uid = "user_sara_202",
        name = "سارة علي (متقدمة للحجز)",
        studentId = "20220312",
        email = "sara.ali@cis.edu.eg",
        roleTag = "Android Developer",
        currentTeamId = null, // لم يتم قبولها بعد (تبحث عن مقعد)
        githubUrl = "https://github.com/sara-dev",
        isLeader = false
    )

    // 2. تيم تجريبي أساسي
    val sampleTeam = Team(
        teamId = "team_alpha_2026",
        projectName = "StackMates (RoleSpot)",
        category = "مشروع تخرج حاسبات 2026",
        description = "تطبيق حجز مقاعد تيمات مشاريع التخرج بنظام السينما لمنع العشوائية وتسهيل التسليم للمعيدين.",
        leaderId = ahmedLeader.uid,
        leaderName = ahmedLeader.name,
        totalSeats = 6,
        isLocked = false,
        supervisorDoctor = "د. محمد كاظم",
        teachingAssistant = "م. شهد أحمد عبدالسلام"
    )

    // 3. كراسي التيم التجريبي (بحالات مختلفة للمعاينة في Compose)
    val sampleSeats = listOf(
        Seat(
            id = "seat_1",
            teamId = sampleTeam.teamId,
            seatNumber = 1,
            roleRequired = "Android Developer",
            status = SeatStatus.BOOKED,
            applicantId = "user_omar",
            applicantName = "عمر خالد"
        ),
        Seat(
            id = "seat_2",
            teamId = sampleTeam.teamId,
            seatNumber = 2,
            roleRequired = "Backend Developer (Spring/Firebase)",
            status = SeatStatus.BOOKED,
            applicantId = "user_youssef",
            applicantName = "يوسف أحمد"
        ),
        Seat(
            id = "seat_3",
            teamId = sampleTeam.teamId,
            seatNumber = 3,
            roleRequired = "UI/UX Designer",
            status = SeatStatus.PENDING, // قيد المراجعة
            applicantId = saraStudent.uid,
            applicantName = saraStudent.name,
            applicantNote = "عندي خبرة سابقة في Figma وصممت تطبيقين ونفسي أنضم لتيمكم!",
            expiresAt = System.currentTimeMillis() + (36 * 3600 * 1000L) // باقي 36 ساعة
        ),
        Seat(
            id = "seat_4",
            teamId = sampleTeam.teamId,
            seatNumber = 4,
            roleRequired = "Flutter / Multiplatform",
            status = SeatStatus.AVAILABLE // متاح للحجز 🟢
        ),
        Seat(
            id = "seat_5",
            teamId = sampleTeam.teamId,
            seatNumber = 5,
            roleRequired = "AI / ML Engineer",
            status = SeatStatus.AVAILABLE // متاح للحجز 🟢
        ),
        Seat(
            id = "seat_6",
            teamId = sampleTeam.teamId,
            seatNumber = 6,
            roleRequired = "QA & Tester",
            status = SeatStatus.AVAILABLE // متاح للحجز 🟢
        )
    )

    // 4. قائمة مشاريع للتغذية والبحث (شاشات خالد)
    val sampleTeamsList = listOf(
        sampleTeam,
        Team(
            teamId = "team_beta_2",
            projectName = "Campus Navigation AR",
            category = "هندسة برمجيات (سنة 3)",
            description = "نظام ملاحة داخلي للمباني والسكاشن بالواقع المعزز.",
            leaderId = "user_kareem",
            leaderName = "كريم صبحي",
            totalSeats = 5,
            isLocked = false
        ),
        Team(
            teamId = "team_gamma_3",
            projectName = "E-Clinic Health Vault",
            category = "مشروع تخرج",
            description = "ملف طبي إلكتروني للمرضى مع تحليلات ذكية للأشعة.",
            leaderId = "user_mona",
            leaderName = "منى سعيد",
            totalSeats = 4,
            isLocked = true // تيم مقفل
        )
    )

    // 5. إشعارات تجريبية (لشاشات خالد)
    val sampleNotifications = listOf(
        NotificationItem(
            id = "notif_1",
            title = "تم قبول حجزك رسمياً! 🎉",
            message = "وافق أحمد محمود على طلب انضمامك لمقعد Android Developer في تيم StackMates.",
            timeAgo = "منذ 10 دقائق",
            type = NotificationType.ACCEPTED
        ),
        NotificationItem(
            id = "notif_2",
            title = "طلب حجز جديد معلق ⏳",
            message = "قدمت سارة علي على حجز مقعد UI/UX في تيمك. تنتهي الصلاحية خلال 36 ساعة.",
            timeAgo = "منذ ساعة",
            type = NotificationType.NEW_REQUEST
        ),
        NotificationItem(
            id = "notif_3",
            title = "تنبيه اعتماد التيم 🔒",
            message = "قام قائد التيم بإغلاق المقاعد نهائياً وتصدير كشف المعيدة م. شهد أحمد.",
            timeAgo = "أمس",
            type = NotificationType.TEAM_LOCKED
        )
    )

    // 6. جدول المواعيد (لشاشات آلاء)
    val sampleCalendarEvents = listOf(
        CalendarEvent(
            id = "event_1",
            teamId = sampleTeam.teamId,
            title = "مناقشة البروبوزال مع د. محمد كاظم",
            date = "الأحد، 22 نوفمبر 2026",
            time = "10:30 صباحاً",
            type = "مناقشة دكتور"
        ),
        CalendarEvent(
            id = "event_2",
            teamId = sampleTeam.teamId,
            title = "مراجعة كشف التيم مع م. شهد أحمد",
            date = "الثلاثاء، 24 نوفمبر 2026",
            time = "12:00 ظهراً",
            type = "تسليم سكشن"
        ),
        CalendarEvent(
            id = "event_3",
            teamId = sampleTeam.teamId,
            title = "اجتماع الفريق لتوزيع مهام الـ Sprint 1",
            date = "الجمعة، 27 نوفمبر 2026",
            time = "08:00 مساءً (Google Meet)",
            type = "ميتنج تيم"
        )
    )
}
