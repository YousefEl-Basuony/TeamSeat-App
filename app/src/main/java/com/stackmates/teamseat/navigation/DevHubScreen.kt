package com.stackmates.teamseat.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * 🛠️ لوحة معاينة الفريق الكاملة (Developer Hub)
 * تعرض شاشات الـ 5 مطورين UI (نُهى، خالد، حسام، إسراء، آلاء) لتجربة أي شاشة بضغطة زر!
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DevHubScreen(
    onNavigate: (String) -> Unit
) {
    val sections = listOf(
        "🌸 1. شاشات نُهى (Auth & Onboarding)" to listOf(
            "Splash Screen" to ScreenRoutes.SPLASH,
            "Onboarding Screen" to ScreenRoutes.ONBOARDING,
            "Login Screen" to ScreenRoutes.LOGIN,
            "Register Screen" to ScreenRoutes.REGISTER,
            "Setup Profile Screen" to ScreenRoutes.SETUP_PROFILE
        ),
        "🔍 2. شاشات خالد (Explore & Course Hub)" to listOf(
            "Home / Teams Feed (+ Demo Switcher)" to ScreenRoutes.HOME,
            "Search & Filter Screen" to ScreenRoutes.SEARCH,
            "Course Hub Screen (صفحة المادة)" to ScreenRoutes.COURSE_HUB,
            "Team Details Overview Screen" to ScreenRoutes.TEAM_DETAILS_OVERVIEW
        ),
        "⚡ 3. شاشات حسام (Profiles, Requests & Notifications)" to listOf(
            "My Profile Screen" to ScreenRoutes.MY_PROFILE,
            "My Sent Applications (طلباتي)" to ScreenRoutes.MY_APPLICATIONS,
            "Notifications Center (الإشعارات)" to ScreenRoutes.NOTIFICATIONS,
            "Leader Review Applications (القبول والرفض)" to ScreenRoutes.REVIEW_APPLICATIONS
        ),
        "💺 4. شاشات إسراء (Seat Map & Team Creation)" to listOf(
            "Interactive Seat Map (خريطة السينما)" to ScreenRoutes.SEAT_MAP,
            "Create Team (Step 1: Info)" to ScreenRoutes.CREATE_TEAM_INFO,
            "Create Team (Step 2: Seats)" to ScreenRoutes.CREATE_TEAM_SEATS,
            "Edit Team Seats" to ScreenRoutes.EDIT_TEAM_SEATS
        ),
        "📑 5. شاشات آلاء (Workspace, Chat & TA Sheet)" to listOf(
            "Team Workspace Dashboard" to ScreenRoutes.TEAM_WORKSPACE,
            "In-Team Group Chat (شات التيم)" to ScreenRoutes.TEAM_GROUP_CHAT,
            "Team Calendar (المواعيد والتسليمات)" to ScreenRoutes.TEAM_CALENDAR,
            "TA Sheet A4 Preview (كشف المعيدة)" to ScreenRoutes.TA_SHEET_PREVIEW
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("💺 TeamSeat — لوحة التيم (6 أعضاء)") }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            sections.forEach { (memberTitle, screens) ->
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = memberTitle,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            screens.forEach { (label, route) ->
                                FilledTonalButton(
                                    onClick = { onNavigate(route) },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(text = label)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
