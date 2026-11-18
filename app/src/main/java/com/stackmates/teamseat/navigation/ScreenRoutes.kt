package com.stackmates.teamseat.navigation

/**
 * مسارات الشاشات (Navigation Routes)
 * مقسمة بانتظام 4 شاشات لكل عضو في الفريق (6 أفراد × 4 شاشات = 24 شاشة)
 */
object ScreenRoutes {
    const val DEV_HUB = "dev_hub"

    // 💻 1. يوسف (Admin, TA Sheet & Demo)
    const val TA_SHEET_PREVIEW = "ta_sheet_preview"
    const val LOCK_TEAM = "lock_team"
    const val DEMO_CONTROL_CENTER = "demo_control_center"
    const val EDIT_TEAM_SEATS = "edit_team_seats"

    // 🌸 2. نُهى (Auth & Onboarding)
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    const val LOGIN = "login"
    const val REGISTER = "register"

    // 🔍 3. خالد (Explore, Feed & Details)
    const val HOME = "home"
    const val SEARCH = "search"
    const val COURSE_HUB = "course_hub"
    const val TEAM_DETAILS_OVERVIEW = "team_details_overview"

    // ⚡ 4. حسام (Profile, Requests & Notifications)
    const val MY_PROFILE = "my_profile"
    const val STUDENT_PUBLIC_PROFILE = "student_public_profile"
    const val MY_APPLICATIONS = "my_applications"
    const val NOTIFICATIONS = "notifications"

    // 💺 5. إسراء (Seat Map & Team Creation)
    const val SEAT_MAP = "seat_map"
    const val CREATE_TEAM_INFO = "create_team_info"
    const val CREATE_TEAM_SEATS = "create_team_seats"
    const val SEAT_BOOKING = "seat_booking"

    // 📑 6. آلاء (Workspace, Chat, Calendar & Review)
    const val TEAM_WORKSPACE = "team_workspace"
    const val TEAM_GROUP_CHAT = "team_group_chat"
    const val TEAM_CALENDAR = "team_calendar"
    const val REVIEW_APPLICATIONS = "review_applications"
}
