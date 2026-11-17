package com.stackmates.teamseat.navigation

/**
 * أسماء مسارات الشاشات (Navigation Routes)
 * مقسمة بالعدل على الـ 5 مطورين فرونت إند (نُهى، خالد، حسام، إسراء، آلاء)
 * + فولدر الباك إند والربط خاص بك أنت!
 */
object ScreenRoutes {
    const val DEV_HUB = "dev_hub"

    // 🌸 1. نُهى (Auth & Onboarding — 5 ملفات)
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val SETUP_PROFILE = "setup_profile"

    // 🔍 2. خالد (Explore, Courses & Team Details — 5 ملفات)
    const val HOME = "home"
    const val SEARCH = "search"
    const val COURSE_HUB = "course_hub"
    const val TEAM_DETAILS_OVERVIEW = "team_details_overview"

    // ⚡ 3. حسام (Profiles, Requests & Notifications — 5 ملفات)
    const val MY_PROFILE = "my_profile"
    const val MY_APPLICATIONS = "my_applications"
    const val NOTIFICATIONS = "notifications"
    const val REVIEW_APPLICATIONS = "review_applications"

    // 💺 4. إسراء (Seat Map & Team Creation — 5 ملفات)
    const val SEAT_MAP = "seat_map"
    const val CREATE_TEAM_INFO = "create_team_info"
    const val CREATE_TEAM_SEATS = "create_team_seats"
    const val EDIT_TEAM_SEATS = "edit_team_seats"

    // 📑 5. آلاء (Workspace, Chat, Calendar & TA Export — 5 ملفات)
    const val TEAM_WORKSPACE = "team_workspace"
    const val TEAM_GROUP_CHAT = "team_group_chat"
    const val TEAM_CALENDAR = "team_calendar"
    const val TA_SHEET_PREVIEW = "ta_sheet_preview"
}
