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
        projectName = "TeamSeat-App",
        category = "مشروع مادة الأندرويد 2026",
        description = "تطبيق حجز مقاعد تيمات مشاريع التخرج بنظام السينما لمنع العشوائية وتسهيل التسليم للمعيدين.",
        leaderId = "user_youssef_101",
        leaderName = "يوسف البسيوني",
        totalSeats = 6,
        isLocked = false,
        supervisorDoctor = "د. محمد كاظم",
        teachingAssistant = "م. شهد أحمد عبدالسلام"
    )

    // 3. كراسي التيم التجريبي (4 محجوزة + 2 متاحة)
    val sampleSeats = listOf(
        Seat(
            id = "seat_1",
            teamId = sampleTeam.teamId,
            seatNumber = 1,
            roleRequired = "Team Leader & Backend",
            status = SeatStatus.BOOKED,
            applicantId = "user_youssef",
            applicantName = "يوسف محمد البسيوني"
        ),
        Seat(
            id = "seat_2",
            teamId = sampleTeam.teamId,
            seatNumber = 2,
            roleRequired = "Android Developer",
            status = SeatStatus.BOOKED,
            applicantId = "user_khaled",
            applicantName = "خالد محمد السعيد"
        ),
        Seat(
            id = "seat_3",
            teamId = sampleTeam.teamId,
            seatNumber = 3,
            roleRequired = "Android UI & Navigation",
            status = SeatStatus.BOOKED,
            applicantId = "user_hossam",
            applicantName = "حسام الجوهري"
        ),
        Seat(
            id = "seat_4",
            teamId = sampleTeam.teamId,
            seatNumber = 4,
            roleRequired = "UI/UX Designer",
            status = SeatStatus.BOOKED,
            applicantId = "user_esraa",
            applicantName = "إسراء محمد أحمد"
        ),
        Seat(
            id = "seat_5",
            teamId = sampleTeam.teamId,
            seatNumber = 5,
            roleRequired = "Frontend Developer",
            status = SeatStatus.AVAILABLE
        ),
        Seat(
            id = "seat_6",
            teamId = sampleTeam.teamId,
            seatNumber = 6,
            roleRequired = "QA & Documentation",
            status = SeatStatus.AVAILABLE
        )
    )

    // 4. قائمة مشاريع للتغذية والبحث (شاشات خالد)
    val sampleTeamsList = listOf(
        sampleTeam,
        Team(
            teamId = "team_beta_2",
            projectName = "صنايعي (Sanayee)",
            category = "تطبيق خدمات وملاحة 2026",
            description = "نظام ملاحة وتتبع لحظي لربط الفنيين والحرفيين بالعملاء داخل المدينة.",
            leaderId = "user_khaled",
            leaderName = "خالد السعيد",
            totalSeats = 5,
            isLocked = false
        ),
        Team(
            teamId = "team_gamma_3",
            projectName = "E-Clinic Health Vault",
            category = "مشروع تخرج",
            description = "ملف طبي إلكتروني للمرضى مع تحليلات ذكية للأشعة.",
            leaderId = "user_alaa",
            leaderName = "آلاء عبدالقادر",
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
