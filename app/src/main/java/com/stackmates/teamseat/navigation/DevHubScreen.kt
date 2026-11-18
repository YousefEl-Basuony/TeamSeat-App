package com.stackmates.teamseat.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * 🛠️ لوحة تحكم الفريق (Developer Hub)
 * تعرض شاشات الـ 6 أفراد بالتساوي (4 شاشات لكل عضو = 24 شاشة)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DevHubScreen(
    onNavigate: (String) -> Unit
) {
    val sections = listOf(
        "💻 1. شاشاتك أنت (يوسف - Leader, TA Sheet & Demo)" to listOf(
            "كشف المعيدة الرسمي (TA Sheet A4)" to ScreenRoutes.TA_SHEET_PREVIEW,
            "نافذة قفل واعتماد التيم (Lock Dialog)" to ScreenRoutes.LOCK_TEAM,
            "مركز تحكم المناقشة (Live Demo Center)" to ScreenRoutes.DEMO_CONTROL_CENTER,
            "تعديل مقاعد التيم (Edit Seats)" to ScreenRoutes.EDIT_TEAM_SEATS
        ),
        "🌸 2. شاشات نُهى (Auth & Onboarding)" to listOf(
            "Splash Screen" to ScreenRoutes.SPLASH,
            "Onboarding Screen (السلايدر)" to ScreenRoutes.ONBOARDING,
            "Login Screen (تسجيل الدخول)" to ScreenRoutes.LOGIN,
            "Register Screen (إنشاء حساب)" to ScreenRoutes.REGISTER
        ),
        "🔍 3. شاشات خالد (Explore & Course Hub)" to listOf(
            "Home / Teams Feed (الرئيسية)" to ScreenRoutes.HOME,
            "Search & Filter Screen (البحث والفلترة)" to ScreenRoutes.SEARCH,
            "Course Hub Screen (صفحة المادة)" to ScreenRoutes.COURSE_HUB,
            "Team Details Overview (تفاصيل المشروع)" to ScreenRoutes.TEAM_DETAILS_OVERVIEW
        ),
        "⚡ 4. شاشات حسام (Profile, Requests & Notifications)" to listOf(
            "My Profile Screen (ملفي الشخصي)" to ScreenRoutes.MY_PROFILE,
            "Student Public Profile (معاينة بروفايل طالب)" to ScreenRoutes.STUDENT_PUBLIC_PROFILE,
            "My Applications (طلباتي المعلقة)" to ScreenRoutes.MY_APPLICATIONS,
            "Notifications Center (مركز الإشعارات)" to ScreenRoutes.NOTIFICATIONS
        ),
        "💺 5. شاشات إسراء (Seat Map & Team Creation)" to listOf(
            "Interactive Seat Map (خريطة السينما)" to ScreenRoutes.SEAT_MAP,
            "Create Team Step 1 (بيانات المشروع)" to ScreenRoutes.CREATE_TEAM_INFO,
            "Create Team Step 2 (تخصيص الكراسي)" to ScreenRoutes.CREATE_TEAM_SEATS,
            "Seat Booking Sheet (حجز مقعد)" to ScreenRoutes.SEAT_BOOKING
        ),
        "📑 6. شاشات آلاء (Workspace, Chat & Review)" to listOf(
            "Team Workspace (مساحة عمل التيم)" to ScreenRoutes.TEAM_WORKSPACE,
            "In-Team Group Chat (شات التيم)" to ScreenRoutes.TEAM_GROUP_CHAT,
            "Team Calendar (جدول المواعيد)" to ScreenRoutes.TEAM_CALENDAR,
            "Review Applications (مراجعة وقبول الطلبات)" to ScreenRoutes.REVIEW_APPLICATIONS
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("💺 TeamSeat — لوحة الـ 6 أعضاء (24 شاشة)") }
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
