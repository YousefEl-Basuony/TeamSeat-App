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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.AirlineSeatReclineNormal
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.SupervisorAccount
import androidx.compose.material.icons.outlined.WorkspacePremium
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stackmates.teamseat.core.data.MockDataProvider
import com.stackmates.teamseat.core.model.SeatStatus
import com.stackmates.teamseat.core.model.Team
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
 * 🔍 المسؤول عن الشاشة: خالد (Khaled) — [KHALED-2]
 * شاشة استعراض تيمات مادة دراسية محددة (Course / Subject Hub Screen)
 * تصميم متميز بمقاييس UI/UX احترافية وعالية الجودة متوافقة مع الهوية البصرية لـ TeamSeat
 */
@Composable
fun CourseHubScreen(
    courseName: String = "مشروع تخرج حاسبات 2026",
    supervisorName: String = "د. محمد كاظم & م. شهد أحمد",
    teamsInCourse: List<Team> = MockDataProvider.sampleTeamsList,
    onTeamClick: (teamId: String) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    var selectedFilter by remember { mutableStateOf("الكل") }

    // حساب إحصائيات المادة بناءً على البيانات الفعلية
    val totalTeams = teamsInCourse.size
    val lockedTeamsCount = teamsInCourse.count { it.isLocked }
    val openTeamsCount = teamsInCourse.count { !it.isLocked }

    // حساب المقاعد الشاغرة الفعلية في المادة
    val totalAvailableSeats = teamsInCourse.filter { !it.isLocked }.sumOf { team ->
        if (team.teamId == MockDataProvider.sampleTeam.teamId) {
            MockDataProvider.sampleSeats.count { it.status == SeatStatus.AVAILABLE }
        } else {
            (team.totalSeats - 2).coerceAtLeast(1)
        }
    }

    // حساب متوسط نسبة اكتمال الفرق
    val averageCompletion = if (totalTeams > 0) {
        val totalPercentages = teamsInCourse.sumOf { team ->
            getTeamCompletionStats(team).third
        }
        totalPercentages / totalTeams
    } else {
        0
    }

    // تصفية الفرق حسب التبويب
    val filteredTeams = when (selectedFilter) {
        "متاحة للتسجيل" -> teamsInCourse.filter { !it.isLocked }
        "مكتملة ومعتمدة" -> teamsInCourse.filter { it.isLocked }
        else -> teamsInCourse
    }

    Scaffold(
        containerColor = WarmCream,
        topBar = {
            CourseHubTopBar(onBackClick = onBackClick)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(top = 10.dp, bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // =================================================================
            // 1. كارت بطل المادة الأكاديمية (Course Information Hero Card)
            // =================================================================
            item {
                Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                    CourseHeroCard(
                        courseName = courseName,
                        supervisorName = supervisorName,
                        totalTeams = totalTeams,
                        availableSeats = totalAvailableSeats
                    )
                }
            }

            // =================================================================
            // 2. بطاقات إحصائيات المادة (Course Key Metrics)
            // =================================================================
            item {
                Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                    CourseMetricsSection(
                        totalTeams = totalTeams,
                        availableSeats = totalAvailableSeats,
                        averageCompletion = averageCompletion,
                        lockedTeams = lockedTeamsCount
                    )
                }
            }

            // =================================================================
            // 3. أزرار تصفية فرق المادة (Course Teams Filter Tabs)
            // =================================================================
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "الفرق المسجلة بالمادة",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = DeepNavy,
                                    fontSize = 18.sp
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = LightAqua.copy(alpha = 0.6f)
                            ) {
                                Text(
                                    text = "${filteredTeams.size} فرق",
                                    modifier = Modifier.padding(horizontal = 9.dp, vertical = 3.dp),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = DeepTeal,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.5.sp
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(listOf("الكل", "متاحة للتسجيل", "مكتملة ومعتمدة")) { tab ->
                            val isSelected = selectedFilter == tab
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = if (isSelected) DeepTeal else Color.White,
                                border = if (isSelected) null else BorderStroke(1.dp, SoftGray.copy(alpha = 0.8f)),
                                shadowElevation = if (isSelected) 3.dp else 1.dp,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .clickable { selectedFilter = tab }
                            ) {
                                Text(
                                    text = tab,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = if (isSelected) Color.White else DeepNavy,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                        fontSize = 13.5.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // =================================================================
            // 4. قائمة كروت الفرق التابعة للمادة (Teams List)
            // =================================================================
            if (filteredTeams.isEmpty()) {
                item {
                    Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
                        EmptyCourseTeamsView(selectedFilter = selectedFilter)
                    }
                }
            } else {
                items(filteredTeams, key = { it.teamId }) { team ->
                    Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                        CourseTeamCard(
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
// مكون الهيدر العلوي المدمج (CourseHubTopBar)
// =============================================================================
@Composable
private fun CourseHubTopBar(
    onBackClick: () -> Unit
) {
    Surface(
        color = WarmCream,
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = Color.White,
                border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.7f)),
                shadowElevation = 2.dp,
                modifier = Modifier.size(42.dp)
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

            Spacer(modifier = Modifier.width(14.dp))

            Column {
                Text(
                    text = "مركز المادة الدراسية",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = DeepNavy,
                        fontSize = 19.sp
                    )
                )
                Text(
                    text = "إشراف الأكاديميين ومتابعة مقاعد وتشكيل الفرق",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = DeepTeal,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp
                    )
                )
            }
        }
    }
}

// =============================================================================
// كارت بطل المادة المميز (CourseHeroCard)
// =============================================================================
@Composable
private fun CourseHeroCard(
    courseName: String,
    supervisorName: String,
    totalTeams: Int,
    availableSeats: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        border = BorderStroke(1.2.dp, DeepTeal.copy(alpha = 0.35f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(22.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // الصف العلوي: أيقونة المادة + شارة المقرر الجامعي
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = LightAqua.copy(alpha = 0.5f),
                    border = BorderStroke(1.dp, DeepTeal.copy(alpha = 0.3f)),
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Outlined.School,
                            contentDescription = null,
                            tint = DeepTeal,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = SoftBeige
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.AutoStories,
                            contentDescription = null,
                            tint = DeepNavy,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "مقرر أكاديمي معتمد",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = DeepNavy,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.5.sp
                            )
                        )
                    }
                }
            }

            // اسم المادة الرئيسي
            Text(
                text = courseName,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = DeepNavy,
                    fontSize = 21.sp,
                    lineHeight = 29.sp
                )
            )

            // نبذة المقرر
            Text(
                text = "مساحة تجمع الطلاب الأكاديمية لاستعراض مقاعد مشاريع التخرج، واختيار الفرق المناسبة بإشراف الكلية.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.DarkGray,
                    fontSize = 13.5.sp,
                    lineHeight = 21.sp
                )
            )

            // بطاقة الهيئة التدريسية المشرفة (Supervising Faculty)
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = WarmCream.copy(alpha = 0.7f),
                border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.6f))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = SoftBeige,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Outlined.SupervisorAccount,
                                contentDescription = null,
                                tint = DeepTeal,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "الهيئة التدريسية والمتابعة:",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color.Gray,
                                fontWeight = FontWeight.Medium,
                                fontSize = 11.5.sp
                            )
                        )
                        Text(
                            text = supervisorName,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = DeepNavy,
                                fontSize = 14.5.sp
                            )
                        )
                    }
                }
            }
        }
    }
}

// =============================================================================
// قسم إحصائيات المادة (CourseMetricsSection)
// =============================================================================
@Composable
private fun CourseMetricsSection(
    totalTeams: Int,
    availableSeats: Int,
    averageCompletion: Int,
    lockedTeams: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.6f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // الإحصائية 1: إجمالي الفرق
            CourseStatItem(
                modifier = Modifier.weight(1f),
                value = "$totalTeams",
                label = "فرق مسجلة",
                icon = Icons.Outlined.Groups,
                accentColor = DeepNavy,
                containerColor = SoftBeige
            )

            Box(
                modifier = Modifier
                    .height(34.dp)
                    .width(1.dp)
                    .background(SoftGray.copy(alpha = 0.6f))
            )

            // الإحصائية 2: المقاعد الشاغرة
            CourseStatItem(
                modifier = Modifier.weight(1f),
                value = "$availableSeats",
                label = "مقاعد شاغرة",
                icon = Icons.Outlined.AirlineSeatReclineNormal,
                accentColor = DeepTeal,
                containerColor = SeatAvailableBg
            )

            Box(
                modifier = Modifier
                    .height(34.dp)
                    .width(1.dp)
                    .background(SoftGray.copy(alpha = 0.6f))
            )

            // الإحصائية 3: متوسط الاكتمال
            CourseStatItem(
                modifier = Modifier.weight(1f),
                value = "$averageCompletion%",
                label = "متوسط الاكتمال",
                icon = Icons.AutoMirrored.Outlined.TrendingUp,
                accentColor = DeepTeal,
                containerColor = LightAqua.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
private fun CourseStatItem(
    modifier: Modifier = Modifier,
    value: String,
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
                    modifier = Modifier.size(19.dp)
                )
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = value,
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
// كارت فريق المادة المتقن (CourseTeamCard)
// =============================================================================
@Composable
private fun CourseTeamCard(
    team: Team,
    onClick: () -> Unit
) {
    val isLocked = team.isLocked
    val (bookedSeats, totalSeats, completionPercent) = getTeamCompletionStats(team)
    val availableSeatsCount = if (isLocked) 0 else (totalSeats - bookedSeats).coerceAtLeast(0)

    val rolesAvailable = getRolesForTeam(team)

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 210.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.6f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // الهيدر: شارة القفل/الإتاحة + نسبة الاكتمال
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (isLocked) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = SeatLockedBg,
                        border = BorderStroke(0.8.dp, SeatLockedGray)
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
                        shape = RoundedCornerShape(8.dp),
                        color = SeatAvailableBg
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.CheckCircle,
                                contentDescription = null,
                                tint = DeepTeal,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "$availableSeatsCount مقاعد شاغرة",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = DeepTeal,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.5.sp
                                )
                            )
                        }
                    }
                }

                // مؤشر نسبة الاكتمال الفاخر
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isLocked) SoftBeige else LightAqua.copy(alpha = 0.5f)
                ) {
                    Text(
                        text = "$completionPercent% اكتمال",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = if (isLocked) DeepNavy else DeepTeal,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    )
                }
            }

            // اسم المشروع الرئيسي
            Text(
                text = team.projectName,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = DeepNavy,
                    fontSize = 18.sp
                )
            )

            // وصف المشروع
            Text(
                text = team.description,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.DarkGray,
                    fontSize = 13.5.sp,
                    lineHeight = 21.sp
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // شريط تقدم اكتمال الفريق المتقن
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
                        text = "الأعضاء المسجلين: $bookedSeats من أصل $totalSeats مقاعد",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                    )
                    Text(
                        text = if (isLocked) "تم الإغلاق للمعيد" else "متبقي $availableSeatsCount مقاعد",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = if (isLocked) Color.Gray else DeepTeal,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.5.sp
                        )
                    )
                }

                LinearProgressIndicator(
                    progress = { completionPercent / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = if (isLocked) DeepNavy else DeepTeal,
                    trackColor = SoftBeige
                )
            }

            // شرائح التخصصات المطلوبة
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                rolesAvailable.take(3).forEach { role ->
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = WarmCream,
                        border = BorderStroke(0.7.dp, SoftGray.copy(alpha = 0.8f))
                    ) {
                        Text(
                            text = role,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = DeepNavy,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }
                if (rolesAvailable.size > 3) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = SoftBeige
                    ) {
                        Text(
                            text = "+${rolesAvailable.size - 3}",
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = DeepTeal,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
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
                    .background(SoftGray.copy(alpha = 0.4f))
            )

            // الفوتر: القائد + المعيدة + زر الاستعراض
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
                        color = LeaderCrownGold.copy(alpha = 0.35f),
                        modifier = Modifier.size(24.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Outlined.WorkspacePremium,
                                contentDescription = null,
                                tint = DeepNavy,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = team.leaderName.substringBefore(" ("),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = DeepNavy,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = "• ${team.teachingAssistant.substringBefore(" ")}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.Gray,
                            fontSize = 11.5.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (isLocked) "عرض التيم" else "حجز مقعد",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = if (isLocked) Color.Gray else DeepTeal,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.5.sp
                        )
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = if (isLocked) Color.Gray else DeepTeal,
                        modifier = Modifier.size(15.dp)
                    )
                }
            }
        }
    }
}

// =============================================================================
// حالة عدم وجود فرق في المادة (EmptyCourseTeamsView)
// =============================================================================
@Composable
private fun EmptyCourseTeamsView(
    selectedFilter: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.6f)),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
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
                text = "لا توجد فرق مطابقة حالياً",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = DeepNavy,
                    fontSize = 17.sp
                )
            )

            Text(
                text = if (selectedFilter == "الكل") {
                    "لم يتم تسجيل أي فريق بعد في هذه المادة الدراسية. يمكنك أن تكون أول من يبادر بتأسيس فريق جديد!"
                } else {
                    "لا توجد فرق تندرج تحت تصنيف ($selectedFilter) في هذه المادة حالياً."
                },
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color.Gray,
                    fontSize = 13.sp,
                    lineHeight = 19.sp
                ),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

// =============================================================================
// دوال مساعدة لحساب نسبة الاكتمال والأدوار
// =============================================================================
/**
 * ترجع ثلاثية: (المقاعد المحجوزة، إجمالي السعة، نسبة الاكتمال المئوية)
 */
private fun getTeamCompletionStats(team: Team): Triple<Int, Int, Int> {
    val totalSeats = team.totalSeats
    val bookedSeats = if (team.teamId == MockDataProvider.sampleTeam.teamId) {
        MockDataProvider.sampleSeats.count { it.status == SeatStatus.BOOKED }
    } else if (team.isLocked) {
        totalSeats
    } else {
        (totalSeats - 2).coerceAtLeast(1)
    }

    val percentage = if (totalSeats > 0) {
        ((bookedSeats.toFloat() / totalSeats) * 100).toInt()
    } else {
        0
    }

    return Triple(bookedSeats, totalSeats, percentage)
}

private fun getRolesForTeam(team: Team): List<String> {
    return if (team.teamId == MockDataProvider.sampleTeam.teamId) {
        MockDataProvider.sampleSeats.map { it.roleRequired.substringBefore(" (") }.distinct()
    } else {
        when (team.teamId) {
            "team_beta_2" -> listOf("Flutter", "UI/UX", "Backend", "AI")
            "team_gamma_3" -> listOf("Android", "Backend", "AI")
            else -> listOf("Android", "Backend", "UI/UX", "Flutter")
        }
    }
}

// =============================================================================
// Preview
// =============================================================================
@Preview(showBackground = true)
@Composable
private fun CourseHubScreenPreview() {
    TeamSeatTheme {
        CourseHubScreen()
    }
}
