package com.stackmates.teamseat.Screens.requests_hossam

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stackmates.teamseat.core.data.MockDataProvider
import com.stackmates.teamseat.core.model.User
import com.stackmates.teamseat.core.theme.DeepNavy
import com.stackmates.teamseat.core.theme.DeepTeal
import com.stackmates.teamseat.core.theme.LeaderCrownGold
import com.stackmates.teamseat.core.theme.LightAqua
import com.stackmates.teamseat.core.theme.Peach
import com.stackmates.teamseat.core.theme.SeatAvailableBg
import com.stackmates.teamseat.core.theme.SoftBeige
import com.stackmates.teamseat.core.theme.SoftGray
import com.stackmates.teamseat.core.theme.TeamSeatTheme
import com.stackmates.teamseat.core.theme.Turquoise
import com.stackmates.teamseat.core.theme.WarmCream
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * ⚡ المسؤول عن الشاشة: حسام (Hossam) — [HOSSAM-1]
 * شاشة ملفي الشخصي وتعديل مهاراتي (My Profile Screen)
 * تصميم متناسق ومستوحى من الهوية البصرية الرسمية لتطبيق TeamSeat
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MyProfileScreen(
    user: User = MockDataProvider.saraStudent,
    onSaveChangesClick: (updatedRole: String, updatedGithub: String) -> Unit = { _, _ -> },
    onLogoutClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    var selectedRole by remember { mutableStateOf(user.roleTag) }
    var githubUrl by remember { mutableStateOf(user.githubUrl) }
    var linkedinUrl by remember { mutableStateOf(user.linkedinUrl) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showSavedBanner by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val availableRoles = remember {
        listOf(
            "Android Developer",
            "Backend Developer",
            "UI/UX Designer",
            "Flutter Developer",
            "AI / ML Engineer",
            "QA & Tester",
            "DevOps Engineer"
        )
    }

    val isTeamMember = !user.currentTeamId.isNullOrEmpty()

    Scaffold(
        containerColor = WarmCream,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "ملفي الشخصي",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = DeepNavy,
                                fontSize = 21.sp
                            )
                        )
                        Text(
                            text = "إدارة بيانات الحساب، الأدوار والروابط",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = DeepTeal,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp
                            )
                        )
                    }
                },
                navigationIcon = {
                    Surface(
                        modifier = Modifier
                            .padding(start = 12.dp, end = 8.dp)
                            .size(42.dp),
                        shape = CircleShape,
                        color = Color.White,
                        shadowElevation = 2.dp,
                        border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.5f))
                    ) {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "رجوع",
                                tint = DeepNavy,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                },
                actions = {
                    Surface(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .size(42.dp),
                        shape = CircleShape,
                        color = Color(0xFFFFF0F0),
                        shadowElevation = 2.dp,
                        border = BorderStroke(1.dp, Color(0xFFFCDDEC))
                    ) {
                        IconButton(onClick = { showLogoutDialog = true }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Logout,
                                contentDescription = "تسجيل الخروج",
                                tint = Color(0xFFD32F2F),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = WarmCream)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // =========================================================================
            // 0. إشعار تأكيد التعديل (Success Banner)
            // =========================================================================
            AnimatedVisibility(
                visible = showSavedBanner,
                enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically() + fadeOut()
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = SeatAvailableBg),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, DeepTeal.copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(DeepTeal),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "تم حفظ وتحديث بيانات ملفك الشخصي بنجاح ✨",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = DeepNavy
                            )
                        )
                    }
                }
            }

            // =========================================================================
            // 1. بطاقة الهوية الأساسية (Avatar & Modern Header Card)
            // =========================================================================
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.4f))
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // شريط ديكوري علوي متدرج
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(84.dp)
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(DeepTeal, Turquoise, LightAqua)
                                )
                            )
                    ) {
                        if (user.isLeader) {
                            Surface(
                                modifier = Modifier
                                    .padding(12.dp)
                                    .align(Alignment.TopEnd),
                                shape = RoundedCornerShape(20.dp),
                                color = LeaderCrownGold,
                                shadowElevation = 2.dp
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = DeepNavy,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "قائد الفريق 👑",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = DeepNavy,
                                            fontSize = 12.sp
                                        )
                                    )
                                }
                            }
                        }
                    }

                    // Avatar مع إطارات ملونة متداخلة
                    Box(
                        modifier = Modifier
                            .offset(y = (-44).dp)
                            .size(96.dp)
                            .clip(CircleShape)
                            .background(WarmCream)
                            .border(
                                width = 3.5.dp,
                                brush = Brush.sweepGradient(listOf(Turquoise, DeepTeal, LightAqua, Turquoise)),
                                shape = CircleShape
                            )
                            .padding(4.dp)
                            .clip(CircleShape)
                            .background(LightAqua.copy(alpha = 0.6f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = user.avatarEmoji,
                            fontSize = 46.sp
                        )
                    }

                    Column(
                        modifier = Modifier
                            .offset(y = (-32).dp)
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = user.name,
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 22.sp,
                                color = DeepNavy
                            )
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // شارة التخصص الحالي
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = LightAqua.copy(alpha = 0.45f),
                            border = BorderStroke(1.dp, DeepTeal.copy(alpha = 0.3f))
                        ) {
                            Text(
                                text = selectedRole,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = DeepTeal
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // معلومات الجامعة والتخصص
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = SoftBeige,
                                modifier = Modifier.weight(1f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Badge,
                                        contentDescription = null,
                                        tint = DeepTeal,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = user.studentId,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = DeepNavy,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = SoftBeige,
                                modifier = Modifier.weight(1.3f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.School,
                                        contentDescription = null,
                                        tint = DeepTeal,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = user.major,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = DeepNavy,
                                            fontWeight = FontWeight.SemiBold
                                        ),
                                        maxLines = 1
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // شارة الموثوقية
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = null,
                                tint = Turquoise,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "حساب طالب جامعي موثق رسمياً",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = DeepNavy.copy(alpha = 0.75f),
                                    fontSize = 12.5.sp
                                )
                            )
                        }
                    }
                }
            }

            // =========================================================================
            // 2. كارت حالة الانضمام للمشروع (Team Membership Status)
            // =========================================================================
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isTeamMember) LightAqua.copy(alpha = 0.4f) else Peach.copy(alpha = 0.3f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                border = BorderStroke(
                    1.dp,
                    if (isTeamMember) DeepTeal.copy(alpha = 0.4f) else Peach
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(if (isTeamMember) DeepTeal else Peach),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isTeamMember) Icons.Outlined.VerifiedUser else Icons.Default.HourglassTop,
                            contentDescription = null,
                            tint = if (isTeamMember) WarmCream else DeepNavy,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (isTeamMember) "عضو معتمد في تيم" else "في مرحلة البحث عن مقعد",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = DeepNavy,
                                    fontSize = 16.5.sp
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(3.dp))

                        Text(
                            text = if (isTeamMember) {
                                "تم اعتماد انضمامك رسمياً في تيم (#${user.currentTeamId})"
                            } else {
                                "لم يتم اعتمادك في أي تيم حتى الآن، يمكنك التقدم لحجز المقاعد المتاحة"
                            },
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = DeepNavy.copy(alpha = 0.8f),
                                lineHeight = 19.sp
                            )
                        )
                    }
                }
            }

            // =========================================================================
            // 3. كارت التخصص والمهارات (Role & Skills Selection)
            // =========================================================================
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.4f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(LightAqua.copy(alpha = 0.5f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Code,
                                contentDescription = null,
                                tint = DeepTeal,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "التخصص الأساسي والرغبة",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = DeepNavy,
                                    fontSize = 17.sp
                                )
                            )
                            Text(
                                text = "حدد دورك التقني الذي تبحث عن مقعد له في مشاريع التخرج",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = DeepNavy.copy(alpha = 0.65f),
                                    fontSize = 12.5.sp
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        availableRoles.forEach { role ->
                            val isSelected = selectedRole == role
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedRole = role },
                                label = {
                                    Text(
                                        text = role,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            fontSize = 14.sp
                                        )
                                    )
                                },
                                leadingIcon = if (isSelected) {
                                    {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                } else null,
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = DeepTeal,
                                    selectedLabelColor = WarmCream,
                                    selectedLeadingIconColor = WarmCream,
                                    containerColor = SoftBeige.copy(alpha = 0.6f),
                                    labelColor = DeepNavy
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = isSelected,
                                    borderColor = SoftGray,
                                    selectedBorderColor = DeepTeal,
                                    borderWidth = 1.dp,
                                    selectedBorderWidth = 1.5.dp
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }
                }
            }

            // =========================================================================
            // 4. كارت الروابط ومعلومات التواصل (GitHub & Professional Links)
            // =========================================================================
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.4f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(LightAqua.copy(alpha = 0.5f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Link,
                                contentDescription = null,
                                tint = DeepTeal,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "روابط الأعمال ومعلومات التواصل",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = DeepNavy,
                                    fontSize = 17.sp
                                )
                            )
                            Text(
                                text = "شارك معرض أعمالك حتى يتأكد قادة التيمات من مهاراتك",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = DeepNavy.copy(alpha = 0.65f),
                                    fontSize = 12.5.sp
                                )
                            )
                        }
                    }

                    // البريد الجامعي (للقراءة فقط)
                    OutlinedTextField(
                        value = user.email,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("البريد الإلكتروني الجامعي (موثق)") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = null,
                                tint = DeepTeal
                            )
                        },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Turquoise
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = WarmCream.copy(alpha = 0.5f),
                            unfocusedContainerColor = WarmCream.copy(alpha = 0.5f),
                            disabledContainerColor = WarmCream.copy(alpha = 0.5f),
                            focusedBorderColor = SoftGray,
                            unfocusedBorderColor = SoftGray,
                            focusedTextColor = DeepNavy,
                            unfocusedTextColor = DeepNavy
                        )
                    )

                    // رابط GitHub (قابل للتعديل)
                    OutlinedTextField(
                        value = githubUrl,
                        onValueChange = { githubUrl = it },
                        label = { Text("رابط حساب GitHub / Portfolio") },
                        placeholder = { Text("https://github.com/username") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Code,
                                contentDescription = null,
                                tint = DeepTeal
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = WarmCream.copy(alpha = 0.4f),
                            unfocusedContainerColor = WarmCream.copy(alpha = 0.3f),
                            focusedBorderColor = DeepTeal,
                            unfocusedBorderColor = SoftGray,
                            focusedTextColor = DeepNavy,
                            unfocusedTextColor = DeepNavy,
                            cursorColor = DeepTeal
                        )
                    )

                    // رابط LinkedIn (قابل للتعديل)
                    OutlinedTextField(
                        value = linkedinUrl,
                        onValueChange = { linkedinUrl = it },
                        label = { Text("رابط حساب LinkedIn") },
                        placeholder = { Text("https://linkedin.com/in/username") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = DeepTeal
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = WarmCream.copy(alpha = 0.4f),
                            unfocusedContainerColor = WarmCream.copy(alpha = 0.3f),
                            focusedBorderColor = DeepTeal,
                            unfocusedBorderColor = SoftGray,
                            focusedTextColor = DeepNavy,
                            unfocusedTextColor = DeepNavy,
                            cursorColor = DeepTeal
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // =========================================================================
            // 5. الأزرار الرئيسية (Save & Logout Buttons)
            // =========================================================================
            Button(
                onClick = {
                    onSaveChangesClick(selectedRole, githubUrl)
                    showSavedBanner = true
                    scope.launch {
                        snackbarHostState.showSnackbar("تم حفظ تعديلات الملف الشخصي بنجاح! ✨")
                        delay(3000)
                        showSavedBanner = false
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(27.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DeepTeal,
                    contentColor = WarmCream
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "حفظ التعديلات والمزامنة",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = WarmCream,
                        fontSize = 16.5.sp
                    )
                )
            }

            OutlinedButton(
                onClick = { showLogoutDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFFD32F2F)
                ),
                border = BorderStroke(1.2.dp, Color(0xFFE53935).copy(alpha = 0.5f))
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = null,
                    modifier = Modifier.size(19.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "تسجيل الخروج",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFD32F2F),
                        fontSize = 15.5.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // =========================================================================
    // 6. نافذة تأكيد تسجيل الخروج (Logout Confirmation Dialog)
    // =========================================================================
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = {
                Text(
                    text = "تسجيل الخروج 🔒",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = DeepNavy
                    )
                )
            },
            text = {
                Text(
                    text = "هل أنت متأكد من رغبتك في تسجيل الخروج من حسابك في TeamSeat؟",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = DeepNavy.copy(alpha = 0.8f)
                    )
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        onLogoutClick()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "نعم، تسجيل الخروج",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text(
                        text = "إلغاء",
                        color = DeepNavy,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },
            containerColor = WarmCream,
            shape = RoundedCornerShape(20.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MyProfileScreenPreview() {
    TeamSeatTheme {
        MyProfileScreen(
            user = MockDataProvider.saraStudent
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MyProfileScreenLeaderPreview() {
    TeamSeatTheme {
        MyProfileScreen(
            user = MockDataProvider.ahmedLeader
        )
    }
}
