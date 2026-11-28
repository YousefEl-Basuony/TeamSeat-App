package com.stackmates.teamseat.navigation

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.stackmates.teamseat.R
import com.stackmates.teamseat.core.data.MockDataProvider
import com.stackmates.teamseat.core.model.NotificationType
import com.stackmates.teamseat.core.model.SeatStatus
import com.stackmates.teamseat.core.utils.PdfExporter
import com.stackmates.teamseat.data.local.SavedAccountManager
import com.stackmates.teamseat.data.repository.AuthRepository
import com.stackmates.teamseat.data.repository.FirebaseAuthRepository
import com.stackmates.teamseat.data.repository.FirebaseTeamSeatRepository
import com.stackmates.teamseat.data.repository.TeamSeatRepository
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
    startDestination: String = ScreenRoutes.SPLASH
) {
    val authRepository: AuthRepository = remember { FirebaseAuthRepository() }
    val teamRepository: TeamSeatRepository = remember { FirebaseTeamSeatRepository() }
    val scope = rememberCoroutineScope()
    val appContext = LocalContext.current
    val savedAccountManager = remember(appContext) { SavedAccountManager(appContext) }
    var savedAccounts by remember { mutableStateOf(savedAccountManager.getSavedAccounts()) }
    var selectedLoginEmail by remember { mutableStateOf("") }

    val googleSwitchLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { activityResult ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(activityResult.data)
        try {
            val googleAcc = task.getResult(ApiException::class.java)
            val idToken = googleAcc.idToken
            if (!idToken.isNullOrBlank()) {
                scope.launch {
                    val res = authRepository.signInWithGoogle(idToken)
                    res.onSuccess { user ->
                        Toast.makeText(appContext, "تم التبديل إلى ${user.name}", Toast.LENGTH_SHORT).show()
                    }.onFailure { err ->
                        Toast.makeText(
                            appContext,
                            err.localizedMessage ?: "تعذر التبديل بحساب Google",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        } catch (_: ApiException) {
            Toast.makeText(appContext, "تم إلغاء تبديل الحساب", Toast.LENGTH_SHORT).show()
        }
    }

    val currentUser by authRepository.currentUser.collectAsState()
    val activePersona by authRepository.activePersona.collectAsState()
    val teams by teamRepository.observeTeams().collectAsState(initial = emptyList())
    val userNotifications by teamRepository.observeUserNotifications(
        currentUser?.uid.orEmpty()
    ).collectAsState(initial = emptyList())

    LaunchedEffect(currentUser) {
        currentUser?.let {
            savedAccountManager.saveAccount(it)
            savedAccounts = savedAccountManager.getSavedAccounts()
        }
    }

    var selectedTeamId by remember { mutableStateOf("") }
    var selectedSeatId by remember { mutableStateOf("") }
    var draftTeamName by remember { mutableStateOf("") }
    var draftTeamCourse by remember { mutableStateOf("") }
    var draftTeamDesc by remember { mutableStateOf("") }
    var draftTeamSeatCount by remember { mutableIntStateOf(6) }

    val selectedTeam = remember(teams, selectedTeamId, currentUser) {
        teams.firstOrNull { it.teamId == selectedTeamId }
            ?: teams.firstOrNull { it.teamId == currentUser?.currentTeamId }
            ?: teams.firstOrNull()
            ?: MockDataProvider.sampleTeam
    }
    val seats by teamRepository.observeTeamSeats(
        if (teams.isEmpty()) "" else selectedTeam.teamId
    ).collectAsState(initial = emptyList())

    val myTeam = remember(teams, currentUser) {
        teams.firstOrNull { !currentUser?.currentTeamId.isNullOrBlank() && it.teamId == currentUser?.currentTeamId }
            ?: teams.firstOrNull { it.leaderId == currentUser?.uid }
    }
    val myTeamSeats by teamRepository.observeTeamSeats(myTeam?.teamId.orEmpty())
        .collectAsState(initial = emptyList())

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
            val context = LocalContext.current
            TASheetPreviewScreen(
                team = selectedTeam,
                seats = seats,
                onLockTeamClick = { navController.navigate(ScreenRoutes.LOCK_TEAM) },
                onExportPdfClick = {
                    val result = PdfExporter.exportTeamSheetPdf(context, selectedTeam, seats)
                    result.onSuccess { file ->
                        Toast.makeText(
                            context,
                            "تم تصدير الكشف بنجاح: ${file.name}",
                            Toast.LENGTH_LONG
                        ).show()
                    }.onFailure {
                        Toast.makeText(
                            context,
                            "تعذر تصدير الكشف: ${it.localizedMessage}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
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
                onSplashFinished = {
                    val hasSavedSession = FirebaseAuth.getInstance().currentUser != null || currentUser != null
                    val destination = when {
                        hasSavedSession && currentUser != null && currentUser!!.studentId.isBlank() ->
                            ScreenRoutes.COMPLETE_PROFILE
                        hasSavedSession -> ScreenRoutes.HOME
                        else -> ScreenRoutes.ONBOARDING
                    }
                    navController.navigate(destination) {
                        popUpTo(ScreenRoutes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(ScreenRoutes.ONBOARDING) {
            OnboardingScreen(
                onFinishOnboarding = {
                    navController.navigate(ScreenRoutes.LOGIN) {
                        popUpTo(ScreenRoutes.ONBOARDING) { inclusive = true }
                    }
                }
            )
        }

        composable(ScreenRoutes.LOGIN) {
            val context = LocalContext.current
            var authError by remember { mutableStateOf<String?>(null) }
            var isLoading by remember { mutableStateOf(false) }

            val googleSignInClient = remember(context) {
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(context.getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()
                GoogleSignIn.getClient(context, gso)
            }

            val googleLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult()
            ) { activityResult ->
                val task = GoogleSignIn.getSignedInAccountFromIntent(activityResult.data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    val idToken = account.idToken
                    if (idToken.isNullOrBlank()) {
                        authError = "تعذر الحصول على رمز Google ID Token"
                    } else {
                        scope.launch {
                            isLoading = true
                            authError = null
                            val result = authRepository.signInWithGoogle(idToken)
                            isLoading = false
                            if (result.isSuccess) {
                                val signedInUser = result.getOrNull()
                                if (signedInUser != null && signedInUser.studentId.isBlank()) {
                                    navController.navigate(ScreenRoutes.COMPLETE_PROFILE) {
                                        popUpTo(ScreenRoutes.LOGIN) { inclusive = true }
                                    }
                                } else {
                                    navController.navigate(ScreenRoutes.HOME) {
                                        popUpTo(ScreenRoutes.LOGIN) { inclusive = true }
                                    }
                                }
                            } else {
                                authError = result.exceptionOrNull()?.localizedMessage
                                    ?: "فشل تسجيل الدخول بحساب Google"
                            }
                        }
                    }
                } catch (e: ApiException) {
                    authError = "تم إلغاء تسجيل الدخول بحساب Google (${e.statusCode})"
                }
            }

            LoginScreen(
                initialEmail = selectedLoginEmail,
                onLoginClick = { email, password ->
                    scope.launch {
                        isLoading = true
                        authError = null
                        val result = authRepository.login(email, password)
                        isLoading = false
                        if (result.isSuccess) {
                            result.getOrNull()?.let { signedInUser ->
                                savedAccountManager.saveAccount(signedInUser, password)
                                savedAccounts = savedAccountManager.getSavedAccounts()
                            }
                            navController.navigate(ScreenRoutes.HOME) {
                                popUpTo(ScreenRoutes.LOGIN) { inclusive = true }
                            }
                        } else {
                            authError = result.exceptionOrNull()?.localizedMessage
                                ?: "فشل تسجيل الدخول، تأكد من البيانات"
                        }
                    }
                },
                onNavigateToRegister = { navController.navigate(ScreenRoutes.REGISTER) },
                onGoogleSignInClick = {
                    authError = null
                    googleSignInClient.signOut().addOnCompleteListener {
                        googleLauncher.launch(googleSignInClient.signInIntent)
                    }
                },
                errorMessage = authError,
                isLoading = isLoading
            )
        }

        composable(ScreenRoutes.REGISTER) {
            var authError by remember { mutableStateOf<String?>(null) }
            var isLoading by remember { mutableStateOf(false) }
            RegisterScreen(
                onRegisterCompleteClick = { name, studentId, email, password, role, major ->
                    scope.launch {
                        isLoading = true
                        authError = null
                        val result = authRepository.register(name, studentId, email, password, role, major)
                        isLoading = false
                        if (result.isSuccess) {
                            result.getOrNull()?.let { registeredUser ->
                                savedAccountManager.saveAccount(registeredUser, password)
                                savedAccounts = savedAccountManager.getSavedAccounts()
                            }
                            navController.navigate(ScreenRoutes.HOME) {
                                popUpTo(ScreenRoutes.LOGIN) { inclusive = true }
                            }
                        } else {
                            authError = result.exceptionOrNull()?.localizedMessage
                                ?: "تعذر إنشاء الحساب، حاول مرة أخرى"
                        }
                    }
                },
                onBackToLogin = { navController.popBackStack() },
                errorMessage = authError,
                isLoading = isLoading
            )
        }

        composable(ScreenRoutes.COMPLETE_PROFILE) {
            var profileError by remember { mutableStateOf<String?>(null) }
            var isLoading by remember { mutableStateOf(false) }
            CompleteProfileScreen(
                initialName = currentUser?.name.orEmpty(),
                onSaveProfileClick = { name, studentId, role ->
                    scope.launch {
                        isLoading = true
                        profileError = null
                        val result = authRepository.completeProfile(name, studentId, role)
                        isLoading = false
                        if (result.isSuccess) {
                            navController.navigate(ScreenRoutes.HOME) {
                                popUpTo(0) { inclusive = true }
                            }
                        } else {
                            profileError = result.exceptionOrNull()?.localizedMessage
                                ?: "تعذر حفظ البيانات، حاول مرة أخرى"
                        }
                    }
                },
                errorMessage = profileError,
                isLoading = isLoading
            )
        }

        // =====================================================================
        // 🔍 3. شاشات خالد (Explore, Search, Course Hub & Team Overview)
        // =====================================================================
        composable(ScreenRoutes.HOME) {
            MainAppShell(
                currentUser = currentUser ?: MockDataProvider.saraStudent,
                teams = teams,
                seats = seats,
                myTeamSeats = myTeamSeats,
                unreadNotificationsCount = userNotifications.count { !it.isRead },
                savedAccounts = savedAccounts,
                onTeamClick = { teamId ->
                    selectedTeamId = teamId
                    navController.navigate(ScreenRoutes.TEAM_DETAILS_OVERVIEW)
                },
                onCreateTeamClick = { navController.navigate(ScreenRoutes.CREATE_TEAM_INFO) },
                onNotificationsClick = { navController.navigate(ScreenRoutes.NOTIFICATIONS) },
                onMyApplicationsClick = { navController.navigate(ScreenRoutes.MY_APPLICATIONS) },
                onSwitchPersona = { authRepository.switchDemoPersona() },
                onOpenCalendarClick = { navController.navigate(ScreenRoutes.TEAM_CALENDAR) },
                onOpenTASheetClick = { navController.navigate(ScreenRoutes.TA_SHEET_PREVIEW) },
                onOpenChatClick = { navController.navigate(ScreenRoutes.TEAM_GROUP_CHAT) },
                onReviewRequestsClick = {
                    myTeam?.teamId?.let { selectedTeamId = it }
                    navController.navigate(ScreenRoutes.REVIEW_APPLICATIONS)
                },
                onRemoveMemberClick = { seat ->
                    scope.launch {
                        val result = teamRepository.removeMemberFromTeam(seat.id)
                        result.onSuccess {
                            Toast.makeText(appContext, "تم إزالة العضو من الفريق", Toast.LENGTH_SHORT).show()
                        }.onFailure { err ->
                            Toast.makeText(
                                appContext,
                                err.localizedMessage ?: "تعذر إزالة العضو",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                },
                onDeleteTeamClick = {
                    myTeam?.let { teamToDelete ->
                        scope.launch {
                            val result = teamRepository.deleteTeam(teamToDelete.teamId)
                            result.onSuccess {
                                if (selectedTeamId == teamToDelete.teamId) {
                                    selectedTeamId = ""
                                }
                                Toast.makeText(appContext, "تم حذف التيم بنجاح", Toast.LENGTH_SHORT).show()
                            }.onFailure { err ->
                                Toast.makeText(
                                    appContext,
                                    err.localizedMessage ?: "تعذر حذف التيم",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                },
                onOpenDemoCenterClick = { navController.navigate(ScreenRoutes.DEMO_CONTROL_CENTER) },
                onSaveProfileChanges = { role, github, linkedin ->
                    scope.launch { authRepository.updateProfile(role, github, linkedin) }
                },
                onSwitchToAccountClick = { account ->
                    if (account.savedPassword.isNotBlank()) {
                        scope.launch {
                            val res = authRepository.login(account.email, account.savedPassword)
                            res.onSuccess {
                                Toast.makeText(appContext, "تم التبديل إلى ${account.name}", Toast.LENGTH_SHORT).show()
                            }.onFailure {
                                selectedLoginEmail = account.email
                                authRepository.logout()
                                Toast.makeText(appContext, "أدخل كلمة المرور لإكمال التبديل", Toast.LENGTH_SHORT).show()
                                navController.navigate(ScreenRoutes.LOGIN) {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        }
                    } else {
                        val gsoBuilder = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(appContext.getString(R.string.default_web_client_id))
                            .requestEmail()
                        if (account.email.isNotBlank()) {
                            gsoBuilder.setAccountName(account.email)
                        }
                        val switchClient = GoogleSignIn.getClient(appContext, gsoBuilder.build())
                        switchClient.silentSignIn()
                            .addOnSuccessListener { googleAcc ->
                                val idToken = googleAcc.idToken
                                if (!idToken.isNullOrBlank()) {
                                    scope.launch {
                                        val res = authRepository.signInWithGoogle(idToken)
                                        res.onSuccess { user ->
                                            Toast.makeText(appContext, "تم التبديل إلى ${user.name}", Toast.LENGTH_SHORT).show()
                                        }.onFailure {
                                            googleSwitchLauncher.launch(switchClient.signInIntent)
                                        }
                                    }
                                } else {
                                    googleSwitchLauncher.launch(switchClient.signInIntent)
                                }
                            }
                            .addOnFailureListener {
                                googleSwitchLauncher.launch(switchClient.signInIntent)
                            }
                    }
                },
                onRemoveSavedAccountClick = { account ->
                    savedAccountManager.removeAccount(account.uid)
                    savedAccounts = savedAccountManager.getSavedAccounts()
                    Toast.makeText(appContext, "تم حذف الحساب من القائمة", Toast.LENGTH_SHORT).show()
                },
                onLogoutClick = {
                    selectedLoginEmail = ""
                    authRepository.logout()
                    navController.navigate(ScreenRoutes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(ScreenRoutes.SEARCH) {
            SearchAndFilterScreen(
                teams = teams,
                onTeamClick = { teamId ->
                    selectedTeamId = teamId
                    navController.navigate(ScreenRoutes.TEAM_DETAILS_OVERVIEW)
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(ScreenRoutes.COURSE_HUB) {
            CourseHubScreen(
                teamsInCourse = teams,
                onTeamClick = { teamId ->
                    selectedTeamId = teamId
                    navController.navigate(ScreenRoutes.TEAM_DETAILS_OVERVIEW)
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(ScreenRoutes.TEAM_DETAILS_OVERVIEW) {
            TeamDetailsOverviewScreen(
                team = selectedTeam,
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
                onSaveChangesClick = { role, github, linkedin ->
                    scope.launch { authRepository.updateProfile(role, github, linkedin) }
                },
                onLogoutClick = {
                    authRepository.logout()
                    navController.navigate(ScreenRoutes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                },
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
            val myAppliedSeats by teamRepository.observeApplicantSeats(currentUser?.uid.orEmpty())
                .collectAsState(initial = emptyList())
            MyApplicationsScreen(
                myAppliedSeats = myAppliedSeats,
                onCancelApplicationClick = { seatId ->
                    scope.launch { teamRepository.rejectOrCancelSeat(seatId) }
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(ScreenRoutes.NOTIFICATIONS) {
            NotificationsScreen(
                notifications = userNotifications,
                onNotificationClick = { item ->
                    if (item.type == NotificationType.NEW_REQUEST) {
                        val leaderTeamId = teams.firstOrNull { it.leaderId == currentUser?.uid }?.teamId
                            ?: currentUser?.currentTeamId.orEmpty()
                        if (leaderTeamId.isNotBlank()) {
                            selectedTeamId = leaderTeamId
                        }
                        navController.navigate(ScreenRoutes.REVIEW_APPLICATIONS)
                    }
                },
                onMarkAllReadClick = {
                    currentUser?.uid?.let { uid ->
                        scope.launch { teamRepository.markAllNotificationsRead(uid) }
                    }
                },
                onClearAllClick = {
                    currentUser?.uid?.let { uid ->
                        scope.launch { teamRepository.clearUserNotifications(uid) }
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        // =====================================================================
        // 💺 5. شاشات إسراء (Seat Map Engine & Creation Flow)
        // =====================================================================
        composable(ScreenRoutes.SEAT_MAP) {
            InteractiveSeatMapScreen(
                team = selectedTeam,
                seats = seats,
                isCurrentUserLeader = currentUser?.uid == selectedTeam.leaderId || currentUser?.isLeader == true,
                onSeatClick = { clickedSeatLabel ->
                    val targetSeat = seats.firstOrNull {
                        it.seatLabel == clickedSeatLabel || it.id == clickedSeatLabel
                    }
                    selectedSeatId = targetSeat?.id ?: seats.firstOrNull {
                        it.status == SeatStatus.AVAILABLE
                    }?.id ?: "seat_4"
                    navController.navigate(ScreenRoutes.SEAT_BOOKING)
                },
                onOpenWorkspaceClick = { navController.navigate(ScreenRoutes.TEAM_WORKSPACE) },
                onReviewRequestsClick = { navController.navigate(ScreenRoutes.REVIEW_APPLICATIONS) },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(ScreenRoutes.CREATE_TEAM_INFO) {
            CreateTeamInfoScreen(
                onNextStepClick = { name, course, description, seatCount ->
                    draftTeamName = name
                    draftTeamCourse = course
                    draftTeamDesc = description
                    draftTeamSeatCount = seatCount
                    navController.navigate(ScreenRoutes.CREATE_TEAM_SEATS)
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(ScreenRoutes.CREATE_TEAM_SEATS) {
            val context = LocalContext.current
            CreateTeamSeatsScreen(
                seatCount = draftTeamSeatCount,
                onBack = { navController.popBackStack() },
                onPublish = { roles ->
                    scope.launch {
                        val result = teamRepository.createTeam(
                            projectName = draftTeamName,
                            category = draftTeamCourse,
                            description = draftTeamDesc,
                            leaderId = currentUser?.uid ?: "user_leader",
                            leaderName = currentUser?.name ?: "قائد الفريق",
                            seatRoles = roles
                        )
                        result.onSuccess { created ->
                            selectedTeamId = created.teamId
                            navController.navigate(ScreenRoutes.SEAT_MAP) {
                                popUpTo(ScreenRoutes.HOME)
                            }
                        }.onFailure { err ->
                            Toast.makeText(
                                context,
                                err.localizedMessage ?: "تعذر إنشاء الفريق",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            )
        }

        composable(ScreenRoutes.SEAT_BOOKING) {
            val context = LocalContext.current
            SeatBookingBottomSheetContent(
                onConfirmBookingClick = { note ->
                    scope.launch {
                        val result = teamRepository.requestSeatBooking(
                            seatId = selectedSeatId,
                            studentId = currentUser?.uid ?: "user_sara",
                            studentName = currentUser?.name ?: "سارة",
                            note = note
                        )
                        result.onFailure { err ->
                            Toast.makeText(
                                context,
                                err.localizedMessage ?: "تعذر حجز المقعد",
                                Toast.LENGTH_LONG
                            ).show()
                        }
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
                team = selectedTeam,
                seats = seats,
                onOpenCalendarClick = { navController.navigate(ScreenRoutes.TEAM_CALENDAR) },
                onOpenTASheetClick = { navController.navigate(ScreenRoutes.TA_SHEET_PREVIEW) },
                onOpenChatClick = { navController.navigate(ScreenRoutes.TEAM_GROUP_CHAT) },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(ScreenRoutes.TEAM_GROUP_CHAT) {
            val activeTeam = teams.firstOrNull { it.teamId == currentUser?.currentTeamId }
                ?: teams.firstOrNull()
                ?: MockDataProvider.sampleTeam
            val liveMessages by teamRepository.observeTeamMessages(activeTeam.teamId)
                .collectAsState(initial = emptyList())
            TeamGroupChatScreen(
                team = activeTeam,
                liveMessages = liveMessages,
                currentUserId = currentUser?.uid.orEmpty(),
                onSendMessageClick = { messageText ->
                    scope.launch {
                        teamRepository.sendTeamMessage(
                            teamId = activeTeam.teamId,
                            senderId = currentUser?.uid ?: "user_anon",
                            senderName = currentUser?.name ?: "عضو",
                            text = messageText
                        )
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(ScreenRoutes.TEAM_CALENDAR) {
            val activeTeam = teams.firstOrNull { it.teamId == currentUser?.currentTeamId }
                ?: teams.firstOrNull()
                ?: MockDataProvider.sampleTeam
            val liveEvents by teamRepository.observeTeamEvents(activeTeam.teamId)
                .collectAsState(initial = MockDataProvider.sampleCalendarEvents)
            TeamCalendarScreen(
                events = liveEvents,
                onAddEventClick = { title, date, time ->
                    scope.launch {
                        teamRepository.addTeamEvent(
                            teamId = activeTeam.teamId,
                            title = title,
                            date = date,
                            time = time
                        )
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(ScreenRoutes.REVIEW_APPLICATIONS) {
            val leaderTeamId = selectedTeamId.ifBlank {
                teams.firstOrNull { it.leaderId == currentUser?.uid }?.teamId
                    ?: currentUser?.currentTeamId.orEmpty()
            }
            val leaderSeats by teamRepository.observeTeamSeats(leaderTeamId)
                .collectAsState(initial = emptyList())
            val pendingSeats = remember(leaderSeats) {
                leaderSeats.filter { it.status == SeatStatus.PENDING }
            }
            ReviewApplicationsScreen(
                pendingSeats = pendingSeats,
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
