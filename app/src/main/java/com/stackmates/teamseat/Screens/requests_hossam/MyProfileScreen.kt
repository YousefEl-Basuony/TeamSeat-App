package com.stackmates.teamseat.Screens.requests_hossam

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.automirrored.rounded.OpenInNew
import androidx.compose.material.icons.rounded.Android
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.Draw
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Link
import androidx.compose.material.icons.rounded.Memory
import androidx.compose.material.icons.rounded.PriorityHigh
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material.icons.rounded.School
import androidx.compose.material.icons.rounded.Storage
import androidx.compose.material.icons.rounded.AdminPanelSettings
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.SwapHoriz
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material.icons.rounded.VerifiedUser
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stackmates.teamseat.core.data.MockDataProvider
import com.stackmates.teamseat.core.model.User
import com.stackmates.teamseat.data.local.AppSettings
import com.stackmates.teamseat.data.local.SavedAccount
import com.stackmates.teamseat.core.theme.TajawalFontFamily
import com.stackmates.teamseat.core.theme.TeamSeatTheme
import com.stackmates.teamseat.ui.components.StaggeredAnimatedItem
import com.stackmates.teamseat.ui.components.TeamSeatBgOffWhite
import com.stackmates.teamseat.ui.components.TeamSeatBodyMuted
import com.stackmates.teamseat.ui.components.TeamSeatCard
import com.stackmates.teamseat.ui.components.TeamSeatCardBorderTeal
import com.stackmates.teamseat.ui.components.TeamSeatDangerRed
import com.stackmates.teamseat.ui.components.TeamSeatPrimaryTeal
import com.stackmates.teamseat.ui.components.TeamSeatScreenContainer
import com.stackmates.teamseat.ui.components.TeamSeatSoftTealPill
import com.stackmates.teamseat.ui.components.TeamSeatTitleDark
import com.stackmates.teamseat.ui.components.TeamSeatWarmOrange
import com.stackmates.teamseat.ui.components.bounceClick
import kotlinx.coroutines.launch

/**
 * ⚡ المسؤول عن الشاشة: حسام (Hossam) — [HOSSAM-1]
 * شاشة ملفي الشخصي وتعديل المهارات (MyProfileScreen)
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MyProfileScreen(
    user: User = MockDataProvider.saraStudent,
    savedAccounts: List<SavedAccount> = emptyList(),
    appSettings: AppSettings = AppSettings(),
    onUpdateSettings: (AppSettings) -> Unit = {},
    onSaveChangesClick: (updatedRole: String, updatedGithub: String, updatedLinkedin: String) -> Unit = { _, _, _ -> },
    onSwitchToAccountClick: (SavedAccount) -> Unit = {},
    onRemoveSavedAccountClick: (SavedAccount) -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onOpenDemoCenterClick: () -> Unit = {},
    onOpenSuperAdminClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val clipboard = LocalClipboardManager.current
    val isEn = appSettings.languageCode == "en"
    val isSuperAdmin = user.email.equals("youssefmohamedelbasouny@gmail.com", ignoreCase = true)
    var selectedRole by remember(user.uid, user.roleTag) { mutableStateOf(user.roleTag) }
    var githubUrl by remember(user.uid, user.githubUrl) { mutableStateOf(user.githubUrl) }
    var linkedinUrl by remember(user.uid, user.linkedinUrl) { mutableStateOf(user.linkedinUrl) }
    var editingRole by remember { mutableStateOf(false) }
    var editingGithub by remember { mutableStateOf(false) }
    var editingLinkedin by remember { mutableStateOf(false) }
    var showLogoutSheet by remember { mutableStateOf(false) }
    var showAccountSwitcherSheet by remember { mutableStateOf(false) }
    var showSettingsSheet by remember { mutableStateOf(false) }
    var showAdminPasscodeDialog by remember { mutableStateOf(false) }
    var adminPasscodeInput by remember { mutableStateOf("") }
    var adminPasscodeError by remember { mutableStateOf<String?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val availableRoles: List<Pair<String, ImageVector>> = remember {
        listOf(
            "Android Developer" to Icons.Rounded.Android,
            "Backend Developer" to Icons.Rounded.Storage,
            "UI/UX Designer" to Icons.Rounded.Draw,
            "Flutter Developer" to Icons.Rounded.Code,
            "AI / ML Engineer" to Icons.Rounded.Memory,
            "QA & Tester" to Icons.Rounded.Verified
        )
    }

    val isTeamMember = !user.currentTeamId.isNullOrEmpty()

    TeamSeatScreenContainer(
        snackbarHostState = snackbarHostState,
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent),
                navigationIcon = {
                    Box(
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(1.dp, TeamSeatCardBorderTeal, CircleShape)
                            .bounceClick(onClick = onBackClick),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "رجوع",
                            tint = TeamSeatTitleDark,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = if (isEn) "My Profile & Skills" else "حسابي ومهاراتي",
                            fontFamily = TajawalFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = TeamSeatTitleDark,
                            fontSize = 19.sp,
                            maxLines = 1,
                            softWrap = false
                        )
                        Text(
                            text = if (isEn) "Set your role & links so teams find you easily" else "ظبّط تخصصك وروابطك عشان التيمات تلاقيك بسهولة",
                            fontFamily = TajawalFontFamily,
                            fontWeight = FontWeight.Normal,
                            color = TeamSeatBodyMuted,
                            fontSize = 11.5.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                },
                actions = {
                    Row(
                        modifier = Modifier.padding(end = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .border(1.dp, TeamSeatCardBorderTeal, CircleShape)
                                .bounceClick { showSettingsSheet = true },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Settings,
                                contentDescription = if (isEn) "Settings" else "الإعدادات",
                                tint = TeamSeatTitleDark,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .border(1.dp, TeamSeatCardBorderTeal, CircleShape)
                                .bounceClick { showAccountSwitcherSheet = true },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.SwapHoriz,
                                contentDescription = if (isEn) "Switch Account" else "تبديل الحساب",
                                tint = TeamSeatPrimaryTeal,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .border(1.dp, TeamSeatWarmOrange.copy(alpha = 0.45f), CircleShape)
                                .bounceClick { showLogoutSheet = true },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.Logout,
                                contentDescription = if (isEn) "Logout" else "تسجيل خروج",
                                tint = TeamSeatWarmOrange,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            )
        }
    ) { safePadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = safePadding.calculateTopPadding() + 8.dp,
                bottom = 26.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 1. بطاقة البروفايل الرئيسية
            item {
                StaggeredAnimatedItem(index = 0) {
                    TeamSeatCard(contentPadding = 16.dp) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            // الأفاتار الدائري المتوهج
                            Box(modifier = Modifier.size(76.dp)) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape)
                                        .background(
                                            Brush.radialGradient(
                                                listOf(Color(0xFFE0F2F1), Color.White)
                                            )
                                        )
                                        .border(
                                            width = 2.5.dp,
                                            brush = Brush.linearGradient(
                                                listOf(Color(0xFF4DB6AC), TeamSeatPrimaryTeal)
                                            ),
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = user.avatarEmoji.ifBlank { "👩‍💻" },
                                        fontSize = 36.sp
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .size(22.dp)
                                        .align(Alignment.BottomStart)
                                        .clip(CircleShape)
                                        .background(TeamSeatPrimaryTeal)
                                        .border(2.dp, Color.White, CircleShape)
                                        .clickable { editingRole = !editingRole },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Edit,
                                        contentDescription = "تعديل",
                                        tint = Color.White,
                                        modifier = Modifier.size(12.dp)
                                    )
                                }
                            }

                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                // شارة الحالة الخضراء
                                Surface(
                                    color = Color(0xFF3E9E8B),
                                    shape = RoundedCornerShape(50)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.CheckCircle,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(13.dp)
                                        )
                                        Text(
                                            text = if (isTeamMember) "منضم لتيم" else "متاح للتيم",
                                            fontFamily = TajawalFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp,
                                            color = Color.White,
                                            maxLines = 1,
                                            softWrap = false
                                        )
                                    }
                                }

                                // الاسم + أيقونة النسخ
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = user.name.substringBefore(" (").ifBlank { "طالب TeamSeat" },
                                        fontFamily = TajawalFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        color = TeamSeatTitleDark,
                                        fontSize = 17.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.weight(1f, fill = false)
                                    )
                                    Icon(
                                        imageVector = Icons.Rounded.ContentCopy,
                                        contentDescription = "نسخ الرقم الجامعي",
                                        tint = TeamSeatBodyMuted,
                                        modifier = Modifier
                                            .size(16.dp)
                                            .clickable {
                                                if (user.studentId.isNotBlank()) {
                                                    clipboard.setText(AnnotatedString(user.studentId))
                                                    scope.launch {
                                                        snackbarHostState.showSnackbar("تم نسخ الرقم الجامعي")
                                                    }
                                                }
                                            }
                                    )
                                }

                                Text(
                                    text = "${user.studentId.ifBlank { "بدون رقم جامعي" }} • ${user.major.substringBefore(" (")}",
                                    fontFamily = TajawalFontFamily,
                                    fontWeight = FontWeight.Medium,
                                    color = TeamSeatBodyMuted,
                                    fontSize = 12.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                // شارة التخصص
                                Surface(
                                    color = Color(0xFFF3F9F9),
                                    shape = RoundedCornerShape(10.dp),
                                    border = BorderStroke(1.dp, TeamSeatCardBorderTeal)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.Android,
                                            contentDescription = null,
                                            tint = Color(0xFF3E9E8B),
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Text(
                                            text = selectedRole,
                                            style = TextStyle(
                                                fontFamily = TajawalFontFamily,
                                                fontWeight = FontWeight.Bold,
                                                color = TeamSeatTitleDark,
                                                fontSize = 11.5.sp,
                                                textDirection = TextDirection.Ltr
                                            ),
                                            maxLines = 1,
                                            softWrap = false
                                        )
                                    }
                                }

                                // أيقونتا GitHub و LinkedIn جنباً إلى جنب عند الحفظ
                                if (githubUrl.isNotBlank() || linkedinUrl.isNotBlank()) {
                                    Row(
                                        modifier = Modifier.padding(top = 2.dp),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        if (githubUrl.isNotBlank()) {
                                            Row(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(Color(0xFF0F172A))
                                                    .bounceClick {
                                                        val url = if (githubUrl.startsWith("http")) githubUrl else "https://$githubUrl"
                                                        runCatching {
                                                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                                                        }
                                                    }
                                                    .padding(horizontal = 9.dp, vertical = 4.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Rounded.Code,
                                                    contentDescription = "GitHub",
                                                    tint = Color.White,
                                                    modifier = Modifier.size(13.dp)
                                                )
                                                Text(
                                                    text = "GitHub",
                                                    fontFamily = TajawalFontFamily,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.White,
                                                    fontSize = 11.sp,
                                                    maxLines = 1,
                                                    softWrap = false
                                                )
                                            }
                                        }

                                        if (linkedinUrl.isNotBlank()) {
                                            Row(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(Color(0xFF0A66C2))
                                                    .bounceClick {
                                                        val url = if (linkedinUrl.startsWith("http")) linkedinUrl else "https://$linkedinUrl"
                                                        runCatching {
                                                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                                                        }
                                                    }
                                                    .padding(horizontal = 9.dp, vertical = 4.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                                            ) {
                                                Text(
                                                    text = "in",
                                                    fontFamily = TajawalFontFamily,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    color = Color.White,
                                                    fontSize = 11.5.sp,
                                                    maxLines = 1,
                                                    softWrap = false
                                                )
                                                Text(
                                                    text = "LinkedIn",
                                                    fontFamily = TajawalFontFamily,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.White,
                                                    fontSize = 11.sp,
                                                    maxLines = 1,
                                                    softWrap = false
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 2. كارت حالة الانضمام للتيم مع الساعة الرملية
            item {
                StaggeredAnimatedItem(index = 1) {
                    TeamSeatCard(isWarmHighlight = !isTeamMember, contentPadding = 14.dp) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(if (isTeamMember) TeamSeatPrimaryTeal else TeamSeatWarmOrange),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isTeamMember) Icons.Rounded.VerifiedUser else Icons.Rounded.PriorityHigh,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Text(
                                    text = if (isTeamMember) "منضم لتيم رسمياً ✓" else "لسه بدوّر على تيم",
                                    fontFamily = TajawalFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isTeamMember) TeamSeatPrimaryTeal else TeamSeatWarmOrange,
                                    fontSize = 15.sp
                                )
                                Text(
                                    text = if (isTeamMember) {
                                        "مقعدك متسجل في (${user.currentTeamId})"
                                    } else {
                                        "ممكن تبعت لحد 3 طلبات حجز في نفس الوقت"
                                    },
                                    fontFamily = TajawalFontFamily,
                                    fontWeight = FontWeight.Normal,
                                    color = TeamSeatBodyMuted,
                                    fontSize = 12.sp
                                )
                            }

                            if (!isTeamMember) {
                                HourglassIllustration(modifier = Modifier.size(44.dp))
                            }
                        }
                    }
                }
            }

            // 3. كارت التخصص الأساسي (مختصر مع زر تعديل)
            item {
                StaggeredAnimatedItem(index = 2) {
                    TeamSeatCard(contentPadding = 16.dp) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.School,
                                        contentDescription = null,
                                        tint = TeamSeatPrimaryTeal,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text(
                                        text = "تخصصك الأساسي",
                                        fontFamily = TajawalFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        color = TeamSeatTitleDark,
                                        fontSize = 15.sp
                                    )
                                }

                                Surface(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50))
                                        .bounceClick { editingRole = !editingRole },
                                    shape = RoundedCornerShape(50),
                                    color = Color.White,
                                    border = BorderStroke(1.dp, TeamSeatCardBorderTeal)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (editingRole) Icons.Rounded.Check else Icons.Rounded.Edit,
                                            contentDescription = null,
                                            tint = TeamSeatPrimaryTeal,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Text(
                                            text = if (editingRole) "تم" else "تعديل",
                                            fontFamily = TajawalFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            color = TeamSeatTitleDark,
                                            fontSize = 11.5.sp,
                                            maxLines = 1,
                                            softWrap = false
                                        )
                                    }
                                }
                            }

                            if (!editingRole) {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Surface(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(50))
                                            .bounceClick { editingRole = true },
                                        shape = RoundedCornerShape(50),
                                        color = Color.White,
                                        border = BorderStroke(1.dp, TeamSeatCardBorderTeal),
                                        shadowElevation = 2.dp
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(
                                                start = 6.dp,
                                                end = 18.dp,
                                                top = 6.dp,
                                                bottom = 6.dp
                                            ),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(28.dp)
                                                    .clip(CircleShape)
                                                    .background(TeamSeatPrimaryTeal),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Rounded.Code,
                                                    contentDescription = null,
                                                    tint = Color.White,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                            Text(
                                                text = selectedRole,
                                                style = TextStyle(
                                                    fontFamily = TajawalFontFamily,
                                                    fontWeight = FontWeight.Bold,
                                                    color = TeamSeatTitleDark,
                                                    fontSize = 13.sp,
                                                    textDirection = TextDirection.Ltr
                                                ),
                                                maxLines = 1,
                                                softWrap = false
                                            )
                                        }
                                    }
                                }
                            }

                            AnimatedVisibility(
                                visible = editingRole,
                                enter = expandVertically() + fadeIn(),
                                exit = shrinkVertically() + fadeOut()
                            ) {
                                FlowRow(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    availableRoles.forEach { (role, roleIcon) ->
                                        val isSelected = selectedRole == role
                                        Surface(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(50))
                                                .bounceClick {
                                                    selectedRole = role
                                                    editingRole = false
                                                },
                                            shape = RoundedCornerShape(50),
                                            color = if (isSelected) TeamSeatPrimaryTeal else Color.White,
                                            border = BorderStroke(
                                                width = 1.dp,
                                                color = if (isSelected) TeamSeatPrimaryTeal else TeamSeatCardBorderTeal
                                            ),
                                            shadowElevation = if (isSelected) 3.dp else 0.dp
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                Icon(
                                                    imageVector = if (isSelected) Icons.Rounded.CheckCircle else roleIcon,
                                                    contentDescription = null,
                                                    tint = if (isSelected) Color.White else TeamSeatPrimaryTeal,
                                                    modifier = Modifier.size(15.dp)
                                                )
                                                Text(
                                                    text = role,
                                                    style = TextStyle(
                                                        fontFamily = TajawalFontFamily,
                                                        fontWeight = FontWeight.Bold,
                                                        color = if (isSelected) Color.White else TeamSeatTitleDark,
                                                        fontSize = 12.sp,
                                                        textDirection = TextDirection.Ltr
                                                    ),
                                                    maxLines = 1,
                                                    softWrap = false
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 4. كارت روابط الشغل والتواصل (GitHub + LinkedIn)
            item {
                StaggeredAnimatedItem(index = 3) {
                    TeamSeatCard(contentPadding = 16.dp) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Link,
                                    contentDescription = null,
                                    tint = TeamSeatPrimaryTeal,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = "روابط الشغل والتواصل",
                                    fontFamily = TajawalFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    color = TeamSeatTitleDark,
                                    fontSize = 15.sp
                                )
                            }

                            // صف GitHub
                            ProfileLinkRowCard(
                                title = "رابط GitHub",
                                subtitle = githubUrl.ifBlank { "مستودع الكود الخاص بي" },
                                logo = { ProfileGitHubLogo(modifier = Modifier.size(34.dp)) },
                                onRowClick = { editingGithub = !editingGithub },
                                onOpenClick = {
                                    if (githubUrl.isNotBlank()) {
                                        val url = if (githubUrl.startsWith("http")) githubUrl else "https://$githubUrl"
                                        runCatching {
                                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                                        }
                                    } else {
                                        editingGithub = true
                                    }
                                }
                            )
                            AnimatedVisibility(
                                visible = editingGithub,
                                enter = expandVertically() + fadeIn(),
                                exit = shrinkVertically() + fadeOut()
                            ) {
                                OutlinedTextField(
                                    value = githubUrl,
                                    onValueChange = { githubUrl = it },
                                    singleLine = true,
                                    placeholder = {
                                        Text(
                                            text = "https://github.com/username",
                                            fontFamily = TajawalFontFamily,
                                            fontSize = 12.5.sp
                                        )
                                    },
                                    textStyle = TextStyle(
                                        fontFamily = TajawalFontFamily,
                                        color = TeamSeatTitleDark,
                                        fontSize = 13.sp,
                                        textDirection = TextDirection.Ltr
                                    ),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = Color.White,
                                        unfocusedContainerColor = Color.White,
                                        focusedBorderColor = TeamSeatPrimaryTeal,
                                        unfocusedBorderColor = TeamSeatCardBorderTeal
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }

                            // صف LinkedIn
                            ProfileLinkRowCard(
                                title = "رابط LinkedIn",
                                subtitle = linkedinUrl.ifBlank { "صفحتي على لينكدان" },
                                logo = { ProfileLinkedInLogo(modifier = Modifier.size(34.dp)) },
                                onRowClick = { editingLinkedin = !editingLinkedin },
                                onOpenClick = {
                                    if (linkedinUrl.isNotBlank()) {
                                        val url = if (linkedinUrl.startsWith("http")) linkedinUrl else "https://$linkedinUrl"
                                        runCatching {
                                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                                        }
                                    } else {
                                        editingLinkedin = true
                                    }
                                }
                            )
                            AnimatedVisibility(
                                visible = editingLinkedin,
                                enter = expandVertically() + fadeIn(),
                                exit = shrinkVertically() + fadeOut()
                            ) {
                                OutlinedTextField(
                                    value = linkedinUrl,
                                    onValueChange = { linkedinUrl = it },
                                    singleLine = true,
                                    placeholder = {
                                        Text(
                                            text = "https://linkedin.com/in/username",
                                            fontFamily = TajawalFontFamily,
                                            fontSize = 12.5.sp
                                        )
                                    },
                                    textStyle = TextStyle(
                                        fontFamily = TajawalFontFamily,
                                        color = TeamSeatTitleDark,
                                        fontSize = 13.sp,
                                        textDirection = TextDirection.Ltr
                                    ),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = Color.White,
                                        unfocusedContainerColor = Color.White,
                                        focusedBorderColor = TeamSeatPrimaryTeal,
                                        unfocusedBorderColor = TeamSeatCardBorderTeal
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }

            // 5. زر احفظ التعديلات
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                        .height(52.dp)
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(50),
                            ambientColor = TeamSeatPrimaryTeal,
                            spotColor = TeamSeatPrimaryTeal
                        )
                        .clip(RoundedCornerShape(50))
                        .background(
                            Brush.horizontalGradient(
                                listOf(TeamSeatPrimaryTeal, Color(0xFF0D4E55))
                            )
                        )
                        .bounceClick {
                            editingGithub = false
                            editingLinkedin = false
                            onSaveChangesClick(selectedRole, githubUrl, linkedinUrl)
                            scope.launch {
                                snackbarHostState.showSnackbar("تمام! حفظنا تعديلات حسابك")
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Save,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(19.dp)
                        )
                        Text(
                            text = "احفظ التعديلات",
                            fontFamily = TajawalFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 15.sp,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }
            }
        }
    }

    if (showAccountSwitcherSheet) {
        ModalBottomSheet(
            onDismissRequest = { showAccountSwitcherSheet = false },
            containerColor = TeamSeatBgOffWhite,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "تبديل أو إضافة حساب",
                    fontFamily = TajawalFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = TeamSeatTitleDark,
                    fontSize = 17.sp
                )
                val accountsToDisplay = if (savedAccounts.isNotEmpty()) {
                    savedAccounts
                } else {
                    listOf(
                        SavedAccount(
                            uid = user.uid,
                            name = user.name.ifBlank { "الحساب الحالي" },
                            email = user.email.ifBlank { selectedRole },
                            roleTag = selectedRole,
                            avatarEmoji = user.avatarEmoji.ifBlank { "👩‍💻" }
                        )
                    )
                }
                accountsToDisplay.forEach { account ->
                    val isCurrentAccount = account.uid == user.uid ||
                        (user.email.isNotBlank() && account.email.equals(user.email, ignoreCase = true))
                    TeamSeatCard(contentPadding = 12.dp) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(text = account.avatarEmoji.ifBlank { "👩‍💻" }, fontSize = 24.sp)
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = account.name.ifBlank { "حساب محفوظ" },
                                    fontFamily = TajawalFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    color = TeamSeatTitleDark,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = account.email.ifBlank { account.roleTag },
                                    fontFamily = TajawalFontFamily,
                                    color = TeamSeatBodyMuted,
                                    fontSize = 12.sp
                                )
                            }
                            if (isCurrentAccount) {
                                Surface(color = TeamSeatSoftTealPill, shape = RoundedCornerShape(50)) {
                                    Text(
                                        text = "نشط الآن",
                                        fontFamily = TajawalFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        color = TeamSeatPrimaryTeal,
                                        fontSize = 11.sp,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }
                            } else {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    OutlinedButton(
                                        onClick = {
                                            showAccountSwitcherSheet = false
                                            onSwitchToAccountClick(account)
                                        },
                                        shape = RoundedCornerShape(50),
                                        border = BorderStroke(1.dp, TeamSeatPrimaryTeal),
                                        contentPadding = androidx.compose.foundation.layout.PaddingValues(
                                            horizontal = 12.dp,
                                            vertical = 4.dp
                                        )
                                    ) {
                                        Text(
                                            text = "تبديل",
                                            fontFamily = TajawalFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            color = TeamSeatPrimaryTeal,
                                            fontSize = 11.sp
                                        )
                                    }
                                    androidx.compose.material3.IconButton(
                                        onClick = { onRemoveSavedAccountClick(account) },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.Delete,
                                            contentDescription = "حذف الحساب المحفوظ",
                                            tint = TeamSeatDangerRed,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                Button(
                    onClick = {
                        showAccountSwitcherSheet = false
                        onLogoutClick()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TeamSeatPrimaryTeal)
                ) {
                    Text(
                        text = if (isEn) "+ Sign in with another account (Google / Email)" else "+ تسجيل الدخول بحساب آخر (Google / Email)",
                        fontFamily = TajawalFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 1,
                        softWrap = false
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
            }
        }
    }

    if (showSettingsSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSettingsSheet = false },
            containerColor = if (appSettings.isDarkMode) Color(0xFF111D20) else TeamSeatBgOffWhite,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = if (isEn) "Settings & About" else "الإعدادات وحول التطبيق",
                    fontFamily = TajawalFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = if (appSettings.isDarkMode) Color(0xFFE6F1F1) else TeamSeatTitleDark,
                    fontSize = 18.sp
                )

                TeamSeatCard(contentPadding = 12.dp) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.NotificationsActive,
                            contentDescription = null,
                            tint = TeamSeatPrimaryTeal
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isEn) "Notifications" else "الإشعارات",
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = if (appSettings.isDarkMode) Color(0xFFE6F1F1) else TeamSeatTitleDark,
                                fontSize = 14.sp
                            )
                            Text(
                                text = if (isEn) "Alerts for join requests & updates" else "تنبيهات طلبات الانضمام وتحديثات الفريق",
                                fontFamily = TajawalFontFamily,
                                color = TeamSeatBodyMuted,
                                fontSize = 11.5.sp
                            )
                        }
                        Switch(
                            checked = appSettings.notificationsEnabled,
                            onCheckedChange = { enabled ->
                                onUpdateSettings(appSettings.copy(notificationsEnabled = enabled))
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = TeamSeatPrimaryTeal
                            )
                        )
                    }
                }

                TeamSeatCard(contentPadding = 12.dp) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.DarkMode,
                            contentDescription = null,
                            tint = TeamSeatPrimaryTeal
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isEn) "Dark Mode" else "الوضع الداكن (Dark Mode)",
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = if (appSettings.isDarkMode) Color(0xFFE6F1F1) else TeamSeatTitleDark,
                                fontSize = 14.sp
                            )
                            Text(
                                text = if (isEn) "Switch between light and dark theme" else "تبديل مظهر التطبيق بين الفاتح والداكن",
                                fontFamily = TajawalFontFamily,
                                color = TeamSeatBodyMuted,
                                fontSize = 11.5.sp
                            )
                        }
                        Switch(
                            checked = appSettings.isDarkMode,
                            onCheckedChange = { dark ->
                                onUpdateSettings(appSettings.copy(isDarkMode = dark))
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = TeamSeatPrimaryTeal
                            )
                        )
                    }
                }

                TeamSeatCard(contentPadding = 12.dp) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Language,
                            contentDescription = null,
                            tint = TeamSeatPrimaryTeal
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isEn) "App Language" else "لغة التطبيق",
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = if (appSettings.isDarkMode) Color(0xFFE6F1F1) else TeamSeatTitleDark,
                                fontSize = 14.sp
                            )
                            Text(
                                text = if (isEn) "English (EN) / العربية (AR)" else "العربية (AR) / English (EN)",
                                fontFamily = TajawalFontFamily,
                                color = TeamSeatBodyMuted,
                                fontSize = 11.5.sp
                            )
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Surface(
                                color = if (!isEn) TeamSeatPrimaryTeal else TeamSeatSoftTealPill,
                                shape = RoundedCornerShape(50),
                                modifier = Modifier.bounceClick {
                                    onUpdateSettings(appSettings.copy(languageCode = "ar"))
                                }
                            ) {
                                Text(
                                    text = "عربي",
                                    fontFamily = TajawalFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    color = if (!isEn) Color.White else TeamSeatPrimaryTeal,
                                    fontSize = 11.5.sp,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                            Surface(
                                color = if (isEn) TeamSeatPrimaryTeal else TeamSeatSoftTealPill,
                                shape = RoundedCornerShape(50),
                                modifier = Modifier.bounceClick {
                                    onUpdateSettings(appSettings.copy(languageCode = "en"))
                                }
                            ) {
                                Text(
                                    text = "EN",
                                    fontFamily = TajawalFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isEn) Color.White else TeamSeatPrimaryTeal,
                                    fontSize = 11.5.sp,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }

                if (isSuperAdmin) {
                    TeamSeatCard(isWarmHighlight = true, contentPadding = 12.dp) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .bounceClick {
                                    showSettingsSheet = false
                                    adminPasscodeInput = ""
                                    adminPasscodeError = null
                                    showAdminPasscodeDialog = true
                                },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.AdminPanelSettings,
                                contentDescription = null,
                                tint = TeamSeatWarmOrange
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (isEn) "Super Admin Panel" else "لوحة تحكم المسؤول (Admin)",
                                    fontFamily = TajawalFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    color = if (appSettings.isDarkMode) Color(0xFFE6F1F1) else TeamSeatTitleDark,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = if (isEn) "Manage users & teams" else "إدارة المستخدمين وحظرهم والتحكم في التيمات",
                                    fontFamily = TajawalFontFamily,
                                    color = TeamSeatBodyMuted,
                                    fontSize = 11.5.sp
                                )
                            }
                        }
                    }
                }

                TeamSeatCard(isWarmHighlight = true, contentPadding = 14.dp) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Info,
                                contentDescription = null,
                                tint = TeamSeatWarmOrange
                            )
                            Text(
                                text = if (isEn) "About TeamSeat & StackMates Team" else "عن تطبيق TeamSeat وفريق العمل",
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = if (appSettings.isDarkMode) Color(0xFFE6F1F1) else TeamSeatTitleDark,
                                fontSize = 15.sp
                            )
                        }
                        Text(
                            text = if (isEn) {
                                "TeamSeat is a smart seat-booking platform for graduation project teams.\nBuilt with passion by StackMates Team: Youssef El-Basuony (Team Leader), Noha, Khaled, Hossam, Esraa & Alaa."
                            } else {
                                "تطبيق TeamSeat لإدارة وتشكيل فرق مشاريع التخرج بنظام حجز المقاعد الذكي.\nتم التطوير بواسطة فريق StackMates: يوسف البسيوني (Team Leader)، نُهى، خالد، حسام، إسراء، وآلاء."
                            },
                            fontFamily = TajawalFontFamily,
                            color = if (appSettings.isDarkMode) Color(0xFFB8CECE) else TeamSeatBodyMuted,
                            fontSize = 12.5.sp,
                            lineHeight = 19.sp
                        )
                        Button(
                            onClick = {
                                runCatching {
                                    context.startActivity(
                                        Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse("https://www.linkedin.com/in/yousef-el-basuony/")
                                        )
                                    )
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = TeamSeatPrimaryTeal)
                        ) {
                            Text(
                                text = if (isEn) "Contact Support on LinkedIn" else "للتواصل مع الدعم (LinkedIn)",
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }

    if (showAdminPasscodeDialog) {
        AlertDialog(
            onDismissRequest = { showAdminPasscodeDialog = false },
            title = {
                Text(
                    text = if (isEn) "Admin Verification" else "تأكيد دخول المسؤول",
                    fontFamily = TajawalFontFamily,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = if (isEn) "Enter Admin password to continue" else "أدخل كلمة مرور المسؤول للمتابعة",
                        fontFamily = TajawalFontFamily,
                        fontSize = 13.sp,
                        color = TeamSeatBodyMuted
                    )
                    OutlinedTextField(
                        value = adminPasscodeInput,
                        onValueChange = {
                            adminPasscodeInput = it
                            adminPasscodeError = null
                        },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        label = {
                            Text(
                                text = if (isEn) "Password" else "كلمة المرور",
                                fontFamily = TajawalFontFamily
                            )
                        },
                        isError = adminPasscodeError != null
                    )
                    if (adminPasscodeError != null) {
                        Text(
                            text = adminPasscodeError.orEmpty(),
                            fontFamily = TajawalFontFamily,
                            color = TeamSeatDangerRed,
                            fontSize = 12.sp
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val digest = java.security.MessageDigest.getInstance("SHA-256")
                            .digest(adminPasscodeInput.toByteArray(Charsets.UTF_8))
                            .joinToString("") { "%02x".format(it) }
                        if (digest == "de22ca1b8761d7e31493b7795d0f2212ccc8e69321660824eef0fce4c4e9e458") {
                            showAdminPasscodeDialog = false
                            onOpenSuperAdminClick()
                        } else {
                            adminPasscodeError = if (isEn) "Incorrect password" else "كلمة المرور غير صحيحة"
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = TeamSeatPrimaryTeal)
                ) {
                    Text(
                        text = if (isEn) "Enter" else "دخول",
                        fontFamily = TajawalFontFamily,
                        color = Color.White
                    )
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showAdminPasscodeDialog = false }) {
                    Text(
                        text = if (isEn) "Cancel" else "إلغاء",
                        fontFamily = TajawalFontFamily
                    )
                }
            }
        )
    }

    if (showLogoutSheet) {
        ModalBottomSheet(
            onDismissRequest = { showLogoutSheet = false },
            containerColor = TeamSeatBgOffWhite,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "تسجيل الخروج",
                    fontFamily = TajawalFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = TeamSeatTitleDark,
                    fontSize = 16.sp
                )
                Text(
                    text = "متأكد إنك عايز تخرج من حسابك دلوقتي؟",
                    fontFamily = TajawalFontFamily,
                    fontWeight = FontWeight.Normal,
                    color = TeamSeatBodyMuted,
                    fontSize = 13.sp
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = { showLogoutSheet = false },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "لأ خليك",
                            fontFamily = TajawalFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = TeamSeatTitleDark,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                    Button(
                        onClick = {
                            showLogoutSheet = false
                            onLogoutClick()
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = TeamSeatDangerRed)
                    ) {
                        Text(
                            text = "أيوة اخرج",
                            fontFamily = TajawalFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
            }
        }
    }
}

@Composable
private fun ProfileLinkRowCard(
    title: String,
    subtitle: String,
    logo: @Composable () -> Unit,
    onRowClick: () -> Unit,
    onOpenClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onRowClick),
        shape = RoundedCornerShape(14.dp),
        color = Color(0xFFF6FAFA),
        border = BorderStroke(1.dp, Color(0xFFDCEAEA))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            logo()
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontFamily = TajawalFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.5.sp,
                    color = TeamSeatTitleDark
                )
                Text(
                    text = subtitle,
                    fontFamily = TajawalFontFamily,
                    fontSize = 11.5.sp,
                    color = TeamSeatBodyMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(1.dp, TeamSeatCardBorderTeal, CircleShape)
                    .clickable(onClick = onOpenClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.OpenInNew,
                    contentDescription = "فتح الرابط",
                    tint = TeamSeatPrimaryTeal,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
private fun ProfileGitHubLogo(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        drawCircle(color = Color(0xFF181717))
        val head = Path().apply {
            moveTo(w * 0.28f, h * 0.60f)
            cubicTo(w * 0.24f, h * 0.42f, w * 0.30f, h * 0.30f, w * 0.34f, h * 0.26f)
            lineTo(w * 0.38f, h * 0.35f)
            lineTo(w * 0.62f, h * 0.35f)
            lineTo(w * 0.66f, h * 0.26f)
            cubicTo(w * 0.70f, h * 0.30f, w * 0.76f, h * 0.42f, w * 0.72f, h * 0.60f)
            cubicTo(w * 0.68f, h * 0.70f, w * 0.60f, h * 0.73f, w * 0.56f, h * 0.75f)
            lineTo(w * 0.56f, h * 0.90f)
            lineTo(w * 0.44f, h * 0.90f)
            lineTo(w * 0.44f, h * 0.75f)
            cubicTo(w * 0.40f, h * 0.73f, w * 0.32f, h * 0.70f, w * 0.28f, h * 0.60f)
            close()
        }
        drawPath(path = head, color = Color.White)
    }
}

@Composable
private fun ProfileLinkedInLogo(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(Color(0xFF0A66C2)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "in",
            fontFamily = TajawalFontFamily,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White,
            fontSize = 15.sp
        )
    }
}

@Composable
private fun HourglassIllustration(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val wood = Color(0xFF78350F)
        val glass = Color(0xFFFFEDD5)
        val sand = Color(0xFFF59E0B)

        // الإطار الخشبي العلوي والسفلي
        drawRoundRect(
            color = wood,
            topLeft = Offset(w * 0.18f, h * 0.08f),
            size = Size(w * 0.64f, h * 0.10f),
            cornerRadius = CornerRadius(4f, 4f)
        )
        drawRoundRect(
            color = wood,
            topLeft = Offset(w * 0.18f, h * 0.82f),
            size = Size(w * 0.64f, h * 0.10f),
            cornerRadius = CornerRadius(4f, 4f)
        )

        // الزجاج
        val topBulb = Path().apply {
            moveTo(w * 0.26f, h * 0.18f)
            lineTo(w * 0.74f, h * 0.18f)
            lineTo(w * 0.54f, h * 0.48f)
            lineTo(w * 0.46f, h * 0.48f)
            close()
        }
        drawPath(topBulb, color = glass)

        val bottomBulb = Path().apply {
            moveTo(w * 0.46f, h * 0.52f)
            lineTo(w * 0.54f, h * 0.52f)
            lineTo(w * 0.74f, h * 0.82f)
            lineTo(w * 0.26f, h * 0.82f)
            close()
        }
        drawPath(bottomBulb, color = glass)

        // الرمل الذهبي بالأسفل
        val bottomSand = Path().apply {
            moveTo(w * 0.50f, h * 0.63f)
            lineTo(w * 0.70f, h * 0.82f)
            lineTo(w * 0.30f, h * 0.82f)
            close()
        }
        drawPath(bottomSand, color = sand)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MyProfileScreenPreview() {
    TeamSeatTheme {
        MyProfileScreen()
    }
}
