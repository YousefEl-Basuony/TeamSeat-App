package com.stackmates.teamseat.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.stackmates.teamseat.core.data.MockDataProvider
import com.stackmates.teamseat.core.model.SeatStatus
import com.stackmates.teamseat.core.utils.PdfExporter
import com.stackmates.teamseat.data.repository.FakeAuthRepository
import com.stackmates.teamseat.data.repository.FakeTeamSeatRepository
import com.stackmates.teamseat.Screens.admin_youssef.*
import com.stackmates.teamseat.Screens.auth_noha.*
import com.stackmates.teamseat.Screens.explore_khaled.*
import com.stackmates.teamseat.Screens.requests_hossam.*
import com.stackmates.teamseat.Screens.seatmap_esraa.*
import com.stackmates.teamseat.Screens.workspace_alaa.*
import kotlinx.coroutines.launch

/**
 * 🔗 شبكة الربط المركزية (AppNavGraph)
 * تربط شاشات الفريق الـ 6 بالتساوي (24 شاشة)
 */
@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = ScreenRoutes.DEV_HUB
) {
    val authRepository = remember { FakeAuthRepository() }
    val teamRepository = remember { FakeTeamSeatRepository() }
    val scope = rememberCoroutineScope()

    val currentUser by authRepository.currentUser.collectAsState()
    val activePersona by authRepository.activePersona.collectAsState()
    val teams by teamRepository.observeTeams().collectAsState(initial = MockDataProvider.sampleTeamsList)
    val seats by teamRepository.observeTeamSeats(MockDataProvider.sampleTeam.teamId)
        .collectAsState(initial = MockDataProvider.sampleSeats)

    NavHost(
        navController = navController,
        startDestination = startDestination
        
    ) {
        // شاشة لوحة التطوير المركزية
        composable(ScreenRoutes.DEV_HUB) {
            DevHubScreen(onNavigate = { route -> navController.navigate(route) })
        }

        // =====================================================================
        // 💻 1. شاشات يوسف (Admin, TA Sheet, Lock & Demo Control)
        // =====================================================================
        composable(ScreenRoutes.TA_SHEET_PREVIEW) {
            TASheetPreviewScreen(
                team = teams.firstOrNull() ?: MockDataProvider.sampleTeam,
                seats = seats,
                onLockTeamClick = { navController.navigate(ScreenRoutes.LOCK_TEAM) },
                onExportPdfClick = { /* استدعاء PdfExporter */ },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(ScreenRoutes.LOCK_TEAM) {
            LockTeamDialog(
                onConfirmLock = {
                    scope.launch {
                        teamRepository.lockTeam(MockDataProvider.sampleTeam.teamId)
                        navController.popBackStack()
                    }
                },
                onDismiss = { navController.popBackStack() }
            )
        }

        composable(ScreenRoutes.DEMO_CONTROL_CENTER) {
            DemoControlCenterScreen(
                currentPersona = activePersona,
                currentUser = currentUser ?: MockDataProvider.ahmedLeader,
                onTogglePersonaClick = { authRepository.switchDemoPersona() },
                onResetMockDataClick = { /* إعادة ضبط البيانات */ },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(ScreenRoutes.EDIT_TEAM_SEATS) {
            ManageSeatsScreen(
                onSaveSeatsClick = { navController.popBackStack() },
                onBackClick = { navController.popBackStack() }
            )
        }

        // =====================================================================
        // 🌸 2. شاشات نُهى (Auth & Onboarding)
        // =====================================================================
        composable(ScreenRoutes.SPLASH) {
            SplashScreen(
                onSplashFinished = { navController.navigate(ScreenRoutes.ONBOARDING) }
            )
        }

        composable(ScreenRoutes.ONBOARDING) {
            OnboardingScreen(
                onFinishOnboarding = { navController.navigate(ScreenRoutes.LOGIN) }
            )
        }

        composable(ScreenRoutes.LOGIN) {
            LoginScreen(
                onLoginClick = { email, password ->
                    scope.launch {
                        authRepository.login(email, password)
                        navController.navigate(ScreenRoutes.HOME)
                    }
                },
                onNavigateToRegister = { navController.navigate(ScreenRoutes.REGISTER) }
            )
        }

        composable(ScreenRoutes.REGISTER) {
            RegisterScreen(
                onRegisterClick = { name, studentId, email, password ->
                    scope.launch {
                        authRepository.register(name, studentId, email, password)
                        navController.navigate(ScreenRoutes.HOME)
                    }
                },
                onBackToLogin = { navController.popBackStack() }
            )
        }

        // =====================================================================
        // 🔍 3. شاشات خالد (Explore, Search, Course Hub & Team Overview)
        // =====================================================================
        composable(ScreenRoutes.HOME) {
            HomeScreen(
                currentUser = currentUser ?: MockDataProvider.saraStudent,
                teams = teams,
                onTeamClick = { navController.navigate(ScreenRoutes.TEAM_DETAILS_OVERVIEW) },
                onCreateTeamClick = { navController.navigate(ScreenRoutes.CREATE_TEAM_INFO) },
                onSearchClick = { navController.navigate(ScreenRoutes.SEARCH) },
                onNotificationsClick = { navController.navigate(ScreenRoutes.NOTIFICATIONS) },
                onMyApplicationsClick = { navController.navigate(ScreenRoutes.MY_APPLICATIONS) },
                onSwitchPersona = { authRepository.switchDemoPersona() }
            )
        }

        composable(ScreenRoutes.SEARCH) {
            SearchAndFilterScreen(
                teams = teams,
                onTeamClick = { navController.navigate(ScreenRoutes.TEAM_DETAILS_OVERVIEW) },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(ScreenRoutes.COURSE_HUB) {
            CourseHubScreen(
                teamsInCourse = teams,
                onTeamClick = { navController.navigate(ScreenRoutes.TEAM_DETAILS_OVERVIEW) },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(ScreenRoutes.TEAM_DETAILS_OVERVIEW) {
            TeamDetailsOverviewScreen(
                team = teams.firstOrNull() ?: MockDataProvider.sampleTeam,
                seats = seats,
                onOpenSeatMapClick = { navController.navigate(ScreenRoutes.SEAT_MAP) },
                onBackClick = { navController.popBackStack() }
            )
        }

        // =====================================================================
        // ⚡ 4. شاشات حسام (Profile, Requests & Notifications)
        // =====================================================================
        composable(ScreenRoutes.MY_PROFILE) {
            MyProfileScreen(
                user = currentUser ?: MockDataProvider.saraStudent,
                onLogoutClick = { navController.navigate(ScreenRoutes.LOGIN) },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(ScreenRoutes.STUDENT_PUBLIC_PROFILE) {
            StudentPublicProfileSheet(
                student = currentUser ?: MockDataProvider.saraStudent,
                onDismiss = { navController.popBackStack() }
            )
        }

        composable(ScreenRoutes.MY_APPLICATIONS) {
            MyApplicationsScreen(
                myAppliedSeats = seats.filter { it.applicantId == currentUser?.uid || it.status == SeatStatus.PENDING },
                onCancelApplicationClick = { seatId ->
                    scope.launch { teamRepository.rejectOrCancelSeat(seatId) }
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(ScreenRoutes.NOTIFICATIONS) {
            NotificationsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        // =====================================================================
        // 💺 5. شاشات إسراء (Seat Map Engine & Creation Flow)
        // =====================================================================
        composable(ScreenRoutes.SEAT_MAP) {
            InteractiveSeatMapScreen(
                team = teams.firstOrNull() ?: MockDataProvider.sampleTeam,
                seats = seats,
                isCurrentUserLeader = currentUser?.isLeader == true,
                onSeatClick = { navController.navigate(ScreenRoutes.SEAT_BOOKING) },
                onOpenWorkspaceClick = { navController.navigate(ScreenRoutes.TEAM_WORKSPACE) },
                onReviewRequestsClick = { navController.navigate(ScreenRoutes.REVIEW_APPLICATIONS) },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(ScreenRoutes.CREATE_TEAM_INFO) {
            CreateTeamInfoScreen(
                onNextStepClick = { _, _, _, _ ->
                    navController.navigate(ScreenRoutes.CREATE_TEAM_SEATS)
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(ScreenRoutes.CREATE_TEAM_SEATS) {
            CreateTeamSeatsScreen(
                onPublishTeamClick = { navController.navigate(ScreenRoutes.SEAT_MAP) },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(ScreenRoutes.SEAT_BOOKING) {
            SeatBookingBottomSheetContent(
                onConfirmBookingClick = { note ->
                    scope.launch {
                        teamRepository.requestSeatBooking("seat_4", currentUser?.uid ?: "user_sara", currentUser?.name ?: "سارة", note)
                        navController.popBackStack()
                    }
                },
                onDismiss = { navController.popBackStack() }
            )
        }

        // =====================================================================
        // 📑 6. شاشات آلاء (Workspace, Chat, Calendar & Review)
        // =====================================================================
        composable(ScreenRoutes.TEAM_WORKSPACE) {
            TeamWorkspaceScreen(
                team = teams.firstOrNull() ?: MockDataProvider.sampleTeam,
                seats = seats,
                onOpenCalendarClick = { navController.navigate(ScreenRoutes.TEAM_CALENDAR) },
                onOpenTASheetClick = { navController.navigate(ScreenRoutes.TA_SHEET_PREVIEW) },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(ScreenRoutes.TEAM_GROUP_CHAT) {
            TeamGroupChatScreen(
                team = teams.firstOrNull() ?: MockDataProvider.sampleTeam,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(ScreenRoutes.TEAM_CALENDAR) {
            TeamCalendarScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(ScreenRoutes.REVIEW_APPLICATIONS) {
            ReviewApplicationsScreen(
                pendingSeats = seats.filter { it.status == SeatStatus.PENDING },
                onAcceptApplicantClick = { seat ->
                    scope.launch {
                        seat.applicantId?.let { applicantId ->
                            teamRepository.acceptApplicantBatch(seat.id, applicantId)
                        }
                    }
                },
                onRejectApplicantClick = { seat ->
                    scope.launch { teamRepository.rejectOrCancelSeat(seat.id) }
                },
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
