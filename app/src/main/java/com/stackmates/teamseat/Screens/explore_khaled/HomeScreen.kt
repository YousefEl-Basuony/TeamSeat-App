package com.stackmates.teamseat.Screens.explore_khaled

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.outlined.Assignment
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.AirlineSeatReclineNormal
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ElectricBolt
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material.icons.outlined.WorkspacePremium
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stackmates.teamseat.core.data.MockDataProvider
import com.stackmates.teamseat.core.model.DemoPersona
import com.stackmates.teamseat.core.model.SeatStatus
import com.stackmates.teamseat.core.model.Team
import com.stackmates.teamseat.core.model.User
import com.stackmates.teamseat.core.theme.DeepNavy
import com.stackmates.teamseat.core.theme.DeepTeal
import com.stackmates.teamseat.core.theme.LeaderCrownGold
import com.stackmates.teamseat.core.theme.LightAqua
import com.stackmates.teamseat.core.theme.Peach
import com.stackmates.teamseat.core.theme.SeatAvailableBg
import com.stackmates.teamseat.core.theme.SeatAvailableGreen
import com.stackmates.teamseat.core.theme.SeatBookedBg
import com.stackmates.teamseat.core.theme.SeatBookedBlue
import com.stackmates.teamseat.core.theme.SeatLockedBg
import com.stackmates.teamseat.core.theme.SeatLockedGray
import com.stackmates.teamseat.core.theme.SeatPendingAmber
import com.stackmates.teamseat.core.theme.SeatPendingBg
import com.stackmates.teamseat.core.theme.SoftBeige
import com.stackmates.teamseat.core.theme.SoftGray
import com.stackmates.teamseat.core.theme.TeamSeatTheme
import com.stackmates.teamseat.core.theme.Turquoise
import com.stackmates.teamseat.core.theme.WarmCream

/**
 * 🔍 المسؤول عن الشاشة: خالد (Khaled) — [KHALED-4]
 * الشاشة الرئيسية واستكشاف التيمات ومقاعد المشاريع الجامعية (Home / Teams Feed Screen)
 * تصميم متميز بمقاييس UI/UX احترافية وعالية الجودة متوافقة مع الهوية البصرية الرسمية لتطبيق TeamSeat
 */
@Composable
fun HomeScreen(
    currentUser: User = MockDataProvider.saraStudent,
    teams: List<Team> = MockDataProvider.sampleTeamsList,
    onTeamClick: (teamId: String) -> Unit = {},
    onCreateTeamClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onMyApplicationsClick: () -> Unit = {},
    onSwitchPersona: () -> Unit = {}
) {
    // تصفية الفرق التي لديها مقاعد شاغرة وعاجلة
    val urgentTeams = teams.filter { !it.isLocked }

    // حساب إجمالي المقاعد المتاحة في الفرق المعروضة
    val totalAvailableSeats = teams.filter { !it.isLocked }.sumOf { team ->
        if (team.teamId == MockDataProvider.sampleTeam.teamId) {
            MockDataProvider.sampleSeats.count { it.status == SeatStatus.AVAILABLE }
        } else {
            (team.totalSeats - 2).coerceAtLeast(1)
        }
    }

    Scaffold(
        containerColor = WarmCream,
        topBar = {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onCreateTeamClick,
                modifier = Modifier.navigationBarsPadding(),
                containerColor = DeepTeal,
                contentColor = Color.White,
                shape = RoundedCornerShape(22.dp),
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 5.dp, pressedElevation = 8.dp),
                icon = {
                    Surface(
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.2f),
                        modifier = Modifier.size(28.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "إنشاء فريق",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                },
                text = {
                    Text(
                        text = "إنشاء فريق جديد",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            letterSpacing = 0.3.sp
                        )
                    )
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(top = 10.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(22.dp)
        ) {
            // =================================================================
            // 0. شريط التبديل التجريبي السريع (Live Demo Persona Switcher)
            // =================================================================
            item {
                Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                    DemoSwitcherBar(
                        currentPersona = if (currentUser.isLeader) DemoPersona.AHMED_LEADER else DemoPersona.SARA_STUDENT,
                        currentUser = currentUser,
                        onTogglePersona = onSwitchPersona
                    )
                }
            }

            // =================================================================
            // 1. الهيدر العلوي المخصص والترحيب + شريط البحث التفاعلي
            // =================================================================
            item {
                PremiumHomeHeader(
                    currentUser = currentUser,
                    unreadNotifCount = MockDataProvider.sampleNotifications.count { !it.isRead }.coerceAtLeast(2),
                    onNotificationsClick = onNotificationsClick,
                    onMyApplicationsClick = onMyApplicationsClick,
                    onSearchClick = onSearchClick
                )
            }

            // =================================================================
            // 2. بطاقة النبض الجامعي والإحصائيات السريعة (University Hub Quick Pulse)
            // =================================================================
            item {
                Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                    UniversityPulseBanner(
                        availableSeats = totalAvailableSeats,
                        activeTeamsCount = teams.size
                    )
                }
            }

            // =================================================================
            // 3. قسم المقاعد العاجلة (Urgent Seats Section - Horizontally Scrollable)
            // =================================================================
            if (urgentTeams.isNotEmpty()) {
                item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        // هيدر القسم المميز
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Peach.copy(alpha = 0.45f),
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Outlined.LocalFireDepartment,
                                            contentDescription = null,
                                            tint = DeepNavy,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "مقاعد عاجلة مطلوبة",
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.ExtraBold,
                                                color = DeepNavy,
                                                fontSize = 18.sp
                                            )
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = Peach
                                        ) {
                                            Text(
                                                text = "URGENT",
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    color = DeepNavy,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    fontSize = 10.sp
                                                )
                                            )
                                        }
                                    }
                                    Text(
                                        text = "فرق تسعى لاكتمال النصاب قبل إغلاق التسجيل النهائي",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = DeepTeal,
                                            fontWeight = FontWeight.Medium,
                                            fontSize = 12.sp
                                        )
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = SoftBeige,
                                border = BorderStroke(1.dp, Peach.copy(alpha = 0.5f))
                            ) {
                                Text(
                                    text = "${urgentTeams.size} فرق عاجلة",
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = DeepNavy,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.5.sp
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // القائمة الأفقية لكروت المقاعد العاجلة
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 20.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(urgentTeams, key = { "urgent_${it.teamId}" }) { team ->
                                PremiumUrgentTeamCard(
                                    team = team,
                                    onClick = { onTeamClick(team.teamId) }
                                )
                            }
                        }
                    }
                }
            }

            // =================================================================
            // 4. قسم استكشاف المشاريع المتاحة (All Open Projects Feed)
            // =================================================================
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "المشاريع المتاحة للتسجيل",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = DeepNavy,
                                    fontSize = 19.sp
                                )
                            )
                        }
                        Text(
                            text = "استكشف تفاصيل وتشكيل كل فريق واحجز دورك المناسب",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.Gray,
                                fontSize = 12.5.sp
                            )
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = LightAqua.copy(alpha = 0.6f),
                        border = BorderStroke(1.dp, DeepTeal.copy(alpha = 0.2f))
                    ) {
                        Text(
                            text = "${teams.size} مشاريع معتمدة",
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = DeepTeal,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        )
                    }
                }
            }

            if (teams.isEmpty()) {
                item {
                    Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                        EmptyTeamsStateView()
                    }
                }
            } else {
                items(teams, key = { it.teamId }) { team ->
                    Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                        PremiumProjectCard(
                            team = team,
                            onClick = { onTeamClick(team.teamId) }
                        )
                    }
                }
            }
        }
    }
}

// =============================================================================
// 🎨 مكون الهيدر العلوي المتميز (PremiumHomeHeader)
// =============================================================================
@Composable
private fun PremiumHomeHeader(
    currentUser: User,
    unreadNotifCount: Int,
    onNotificationsClick: () -> Unit,
    onMyApplicationsClick: () -> Unit,
    onSearchClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // بطاقة هوية الطالب والأزرار التفاعلية
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // صورة الطالب واسمه وتخصصه
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // أفاتار الطالب مع إشارة الحالة النشطة
                Box {
                    Surface(
                        shape = CircleShape,
                        color = Color.White,
                        border = BorderStroke(2.dp, DeepTeal.copy(alpha = 0.5f)),
                        shadowElevation = 3.dp,
                        modifier = Modifier.size(56.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = currentUser.avatarEmoji,
                                fontSize = 28.sp
                            )
                        }
                    }

                    // نقطة الاتصال الأخضر
                    Surface(
                        shape = CircleShape,
                        color = Turquoise,
                        border = BorderStroke(2.dp, WarmCream),
                        modifier = Modifier
                            .size(15.dp)
                            .align(Alignment.BottomEnd)
                    ) {}
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "أهلاً بك، ${currentUser.name.substringBefore(" (")}",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = DeepNavy,
                                fontSize = 20.sp
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = SoftBeige.copy(alpha = 0.8f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.School,
                                contentDescription = null,
                                tint = DeepTeal,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = currentUser.roleTag,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = DeepTeal,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.5.sp
                                )
                            )
                        }
                    }
                }
            }

            // أزرار العمليات السريعة (طلباتي + الإشعارات مع شارة العداد)
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // زر "طلباتي"
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.8f)),
                    shadowElevation = 2.dp,
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onMyApplicationsClick() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 9.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.Assignment,
                            contentDescription = "طلباتي",
                            tint = DeepTeal,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "طلباتي",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = DeepNavy,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        )
                    }
                }

                // زر الإشعارات مع العداد التنبيهي
                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.8f)),
                    shadowElevation = 2.dp,
                    modifier = Modifier.size(46.dp)
                ) {
                    IconButton(onClick = onNotificationsClick) {
                        BadgedBox(
                            badge = {
                                if (unreadNotifCount > 0) {
                                    Badge(
                                        containerColor = Peach,
                                        contentColor = DeepNavy
                                    ) {
                                        Text(
                                            text = "$unreadNotifCount",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp
                                        )
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Notifications,
                                contentDescription = "التنبيهات",
                                tint = DeepNavy,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }
            }
        }

        // عبارة دافئة لاستكشاف المشاريع
        Text(
            text = "اكتشف فرق مشاريع التخرج وشارك في تشكيل فريقك الأكاديمي المثالي بنظام المقاعد المعتمد 🎓",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = DeepNavy.copy(alpha = 0.85f),
                fontWeight = FontWeight.Normal,
                fontSize = 13.5.sp,
                lineHeight = 21.sp
            )
        )

        // شريط البحث المتميز (ينقل فوراً لشاشة البحث والفلترة عند الضغط)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .clickable { onSearchClick() },
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            border = BorderStroke(1.2.dp, SoftGray.copy(alpha = 0.8f)),
            shadowElevation = 3.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = LightAqua.copy(alpha = 0.5f),
                        modifier = Modifier.size(34.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Outlined.Search,
                                contentDescription = "بحث",
                                tint = DeepTeal,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "ابحث عن اسم مشروع، مادة دراسية، أو تخصص...",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SoftBeige,
                    border = BorderStroke(0.8.dp, SoftGray.copy(alpha = 0.6f)),
                    modifier = Modifier.size(34.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Outlined.Tune,
                            contentDescription = "فلترة",
                            tint = DeepTeal,
                            modifier = Modifier.size(17.dp)
                        )
                    }
                }
            }
        }
    }
}

// =============================================================================
// ⚡ بطاقة نبض المنظومة الجامعية (UniversityPulseBanner)
// =============================================================================
@Composable
private fun UniversityPulseBanner(
    availableSeats: Int,
    activeTeamsCount: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // المؤشر 1: مقاعد شاغرة
            PulseMetricItem(
                modifier = Modifier.weight(1f),
                count = "$availableSeats",
                label = "مقاعد شاغرة",
                icon = Icons.Outlined.AirlineSeatReclineNormal,
                accentColor = DeepTeal,
                containerColor = SeatAvailableBg
            )

            Box(
                modifier = Modifier
                    .height(36.dp)
                    .width(1.dp)
                    .background(SoftGray.copy(alpha = 0.6f))
            )

            // المؤشر 2: فرق نشطة
            PulseMetricItem(
                modifier = Modifier.weight(1f),
                count = "$activeTeamsCount",
                label = "فرق مسجلة",
                icon = Icons.Outlined.Groups,
                accentColor = DeepNavy,
                containerColor = SoftBeige
            )

            Box(
                modifier = Modifier
                    .height(36.dp)
                    .width(1.dp)
                    .background(SoftGray.copy(alpha = 0.6f))
            )

            // المؤشر 3: مهلة الاعتماد
            PulseMetricItem(
                modifier = Modifier.weight(1f),
                count = "48 ساعة",
                label = "مهلة الحجز",
                icon = Icons.Outlined.Timer,
                accentColor = SeatPendingAmber,
                containerColor = SeatPendingBg
            )
        }
    }
}

@Composable
private fun PulseMetricItem(
    modifier: Modifier = Modifier,
    count: String,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color,
    containerColor: Color
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = containerColor,
            modifier = Modifier.size(36.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = count,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = DeepNavy,
                    fontSize = 15.sp
                )
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = Color.Gray,
                    fontSize = 11.sp
                )
            )
        }
    }
}

// =============================================================================
// 🔥 كارت المقاعد العاجلة المتميز (PremiumUrgentTeamCard - Horizontal)
// =============================================================================
@Composable
private fun PremiumUrgentTeamCard(
    team: Team,
    onClick: () -> Unit
) {
    // حساب المقاعد الشاغرة بدقة
    val availableSeatsCount = if (team.teamId == MockDataProvider.sampleTeam.teamId) {
        MockDataProvider.sampleSeats.count { it.status == SeatStatus.AVAILABLE }
    } else {
        (team.totalSeats - 2).coerceAtLeast(1)
    }

    val bookedSeatsCount = team.totalSeats - availableSeatsCount
    val capacityPercent = if (team.totalSeats > 0) (bookedSeatsCount.toFloat() / team.totalSeats) else 0.5f

    val requiredSpecialties = if (team.teamId == MockDataProvider.sampleTeam.teamId) {
        MockDataProvider.sampleSeats
            .filter { it.status == SeatStatus.AVAILABLE }
            .map { it.roleRequired.substringBefore(" (") }
    } else {
        listOf("Backend", "UI/UX", "AI/ML")
    }

    Card(
        onClick = onClick,
        modifier = Modifier
            .width(300.dp)
            .heightIn(min = 238.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        border = BorderStroke(1.5.dp, Peach)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // شريط الحالة العلوي: شارة عاجل نارية + اسم المادة
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = SeatPendingBg,
                    border = BorderStroke(1.dp, Peach)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.LocalFireDepartment,
                            contentDescription = null,
                            tint = DeepNavy,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "مطلوب عاجلاً",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = DeepNavy,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.5.sp
                            )
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SoftBeige
                ) {
                    Text(
                        text = team.category.substringBefore(" ("),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = DeepTeal,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // اسم المشروع الرئيسي
            Text(
                text = team.projectName,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = DeepNavy,
                    fontSize = 17.sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // مؤشر امتلاء المقاعد مع شريط تقدم أنيق
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "اكتمال الفريق: $bookedSeatsCount/${team.totalSeats} أعضاء",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.DarkGray,
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp
                        )
                    )
                    Text(
                        text = "متبقي $availableSeatsCount فقط",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = DeepTeal,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.5.sp
                        )
                    )
                }

                LinearProgressIndicator(
                    progress = { capacityPercent },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = DeepTeal,
                    trackColor = SoftBeige
                )
            }

            // التخصصات المطلوبة كـ Chips جذابة
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                requiredSpecialties.take(3).forEach { spec ->
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = WarmCream,
                        border = BorderStroke(0.8.dp, SoftGray.copy(alpha = 0.8f))
                    ) {
                        Text(
                            text = spec,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = DeepNavy,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // خط فاصل ناعم
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(SoftGray.copy(alpha = 0.5f))
            )

            // الفوتر: اسم القائد وزر الحجز التفاعلي
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = LeaderCrownGold.copy(alpha = 0.4f),
                        modifier = Modifier.size(24.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Outlined.WorkspacePremium,
                                contentDescription = null,
                                tint = DeepNavy,
                                modifier = Modifier.size(15.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = team.leaderName.substringBefore(" ("),
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = DeepNavy,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = DeepTeal
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "احجز مقعدك",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.5.sp
                            )
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
            }
        }
    }
}

// =============================================================================
// 📂 كارت استعراض المشروع المفتوح المتقن (PremiumProjectCard - Vertical)
// =============================================================================
@Composable
private fun PremiumProjectCard(
    team: Team,
    onClick: () -> Unit
) {
    val isLocked = team.isLocked
    val availableSeatsCount = if (team.teamId == MockDataProvider.sampleTeam.teamId) {
        MockDataProvider.sampleSeats.count { it.status == SeatStatus.AVAILABLE }
    } else if (isLocked) {
        0
    } else {
        (team.totalSeats - 2).coerceAtLeast(1)
    }

    val rolesAvailable = if (team.teamId == MockDataProvider.sampleTeam.teamId) {
        MockDataProvider.sampleSeats.map { it.roleRequired.substringBefore(" (") }.distinct()
    } else {
        listOf("Android Lead", "Backend / Cloud", "UI/UX", "Data Science")
    }

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.6f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(13.dp)
        ) {
            // الهيدر: الفئة / المادة الدراسية + شارة حالة المقاعد
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = SoftBeige
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.School,
                            contentDescription = null,
                            tint = DeepTeal,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = team.category,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = DeepTeal,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        )
                    }
                }

                if (isLocked) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = SeatLockedBg,
                        border = BorderStroke(1.dp, SeatLockedGray)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "فريق مقفل ومعتمد",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color.Gray,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.5.sp
                                )
                            )
                        }
                    }
                } else {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = SeatAvailableBg,
                        border = BorderStroke(0.8.dp, DeepTeal.copy(alpha = 0.3f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.CheckCircle,
                                contentDescription = null,
                                tint = DeepTeal,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "$availableSeatsCount مقاعد متاحة",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = DeepTeal,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            )
                        }
                    }
                }
            }

            // عنوان المشروع
            Text(
                text = team.projectName,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = DeepNavy,
                    fontSize = 19.sp
                )
            )

            // وصف الفكرة بوضوح وتوازن مريح
            Text(
                text = team.description,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.DarkGray,
                    fontSize = 14.sp,
                    lineHeight = 22.sp
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // شرائح التخصصات المطلوبة مع شارة التعداد الإضافي
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                rolesAvailable.take(3).forEach { role ->
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = WarmCream,
                        border = BorderStroke(0.8.dp, SoftGray.copy(alpha = 0.8f))
                    ) {
                        Text(
                            text = role,
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = DeepNavy,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }
                if (rolesAvailable.size > 3) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = SoftBeige
                    ) {
                        Text(
                            text = "+${rolesAvailable.size - 3}",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = DeepTeal,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        )
                    }
                }
            }

            // إشراف الأستاذ والمعيد
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = WarmCream.copy(alpha = 0.6f)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.WorkspacePremium,
                        contentDescription = null,
                        tint = DeepTeal,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "إشراف: ${team.supervisorDoctor} • ${team.teachingAssistant}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.DarkGray,
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // خط فاصل ناعم
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(SoftGray.copy(alpha = 0.4f))
            )

            // الفوتر: القائد + إجمالي السعة + زر التصفح
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = null,
                        tint = DeepTeal,
                        modifier = Modifier.size(17.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = team.leaderName.substringBefore(" ("),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = DeepNavy,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.5.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Icon(
                        imageVector = Icons.Outlined.Groups,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(17.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "السعة: ${team.totalSeats} مقاعد",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.Gray,
                            fontSize = 12.5.sp
                        )
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (isLocked) "عرض التفاصيل" else "تصفح المقاعد",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = if (isLocked) Color.Gray else DeepTeal,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = if (isLocked) Color.Gray else DeepTeal,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

// =============================================================================
// حالة عدم وجود مشاريع متاحة (Empty State View)
// =============================================================================
@Composable
private fun EmptyTeamsStateView() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.6f)),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(36.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = SoftBeige,
                modifier = Modifier.size(64.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Outlined.Groups,
                        contentDescription = null,
                        tint = DeepTeal,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
            Text(
                text = "لا توجد مشاريع متاحة حالياً",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = DeepNavy,
                    fontSize = 17.sp
                )
            )
            Text(
                text = "كن أول من يبادر بإنشاء فريق جامعي جديد ودعوة زملائك للانضمام!",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color.Gray,
                    fontSize = 13.sp
                )
            )
        }
    }
}

// =============================================================================
// Preview
// =============================================================================
@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    TeamSeatTheme {
        HomeScreen()
    }
}
