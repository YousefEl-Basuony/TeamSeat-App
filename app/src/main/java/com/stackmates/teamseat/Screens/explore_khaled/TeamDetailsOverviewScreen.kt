package com.stackmates.teamseat.Screens.explore_khaled

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.outlined.AirlineSeatReclineNormal
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.HourglassTop
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.SupervisorAccount
import androidx.compose.material.icons.outlined.Terminal
import androidx.compose.material.icons.outlined.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.stackmates.teamseat.core.model.Seat
import com.stackmates.teamseat.core.model.SeatStatus
import com.stackmates.teamseat.core.model.Team
import com.stackmates.teamseat.core.theme.DeepNavy
import com.stackmates.teamseat.core.theme.DeepTeal
import com.stackmates.teamseat.core.theme.LeaderCrownGold
import com.stackmates.teamseat.core.theme.LightAqua
import com.stackmates.teamseat.core.theme.Peach
import com.stackmates.teamseat.core.theme.SeatAvailableBg
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
 * 🔍 المسؤول عن الشاشة: خالد (Khaled) — [KHALED-3]
 * شاشة تفاصيل المشروع الشاملة قبل فتح خريطة الكراسي (Team Details Overview Screen)
 * تصميم متميز بمقاييس UI/UX احترافية وجودة عالية متوافقة مع الهوية البصرية لـ TeamSeat
 */
@Composable
fun TeamDetailsOverviewScreen(
    team: Team = MockDataProvider.sampleTeam,
    seats: List<Seat> = MockDataProvider.sampleSeats,
    onOpenSeatMapClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    // حساب إحصائيات المقاعد المحدثة بناءً على قائمة المقاعد الفعلية
    val totalSeats = if (seats.isNotEmpty()) seats.size else team.totalSeats
    val bookedCount = seats.count { it.status == SeatStatus.BOOKED }
    val availableCount = seats.count { it.status == SeatStatus.AVAILABLE }
    val pendingCount = seats.count { it.status == SeatStatus.PENDING }
    val completionPercent = if (totalSeats > 0) ((bookedCount.toFloat() / totalSeats) * 100).toInt() else 0

    Scaffold(
        containerColor = WarmCream,
        topBar = {
            TeamOverviewTopBar(
                teamCategory = team.category,
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            TeamOverviewBottomCta(
                availableSeatsCount = availableCount,
                isLocked = team.isLocked,
                onOpenSeatMapClick = onOpenSeatMapClick
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(top = 10.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // =================================================================
            // 1. بطاقة بطل المشروع الرئيسية (Project Hero Card)
            // =================================================================
            item {
                Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                    ProjectHeroCard(team = team)
                }
            }

            // =================================================================
            // 2. بطاقة قائد الفريق والهيئة الأكاديمية (Leader & Faculty Mentorship)
            // =================================================================
            item {
                Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                    TeamLeaderAndMentorsCard(team = team)
                }
            }

            // =================================================================
            // 3. بطاقة وصف فكرة المشروع وأهدافه (Project Description Card)
            // =================================================================
            item {
                Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                    ProjectDescriptionCard(description = team.description)
                }
            }

            // =================================================================
            // 4. لوحة ملخص المقاعد الشاغرة والإحصائيات (Seat Availability Metrics)
            // =================================================================
            item {
                Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                    SeatAvailabilityMetricsCard(
                        totalSeats = totalSeats,
                        bookedCount = bookedCount,
                        availableCount = availableCount,
                        pendingCount = pendingCount,
                        completionPercent = completionPercent
                    )
                }
            }

            // =================================================================
            // 5. معاينة خريطة المقاعد السريعة (Compact Seat Grid Preview)
            // =================================================================
            item {
                Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                    SeatMapPreviewSection(seats = seats)
                }
            }
        }
    }
}

// =============================================================================
// مكون الهيدر العلوي المدمج (TeamOverviewTopBar)
// =============================================================================
@Composable
private fun TeamOverviewTopBar(
    teamCategory: String,
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
                    text = "تفاصيل ونظرة عامة على الفريق",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = DeepNavy,
                        fontSize = 19.sp
                    )
                )
                Text(
                    text = teamCategory,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = DeepTeal,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

// =============================================================================
// كارت بطل المشروع الفاخر (ProjectHeroCard)
// =============================================================================
@Composable
private fun ProjectHeroCard(
    team: Team
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
            // الهيدر: شارة المقرر + شارة حالة الفريق
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

                if (team.isLocked) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
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
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "فريق مقفل",
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
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "متاح للحجز",
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

            // أيقونة المشروع واسم المشروع
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = LightAqua.copy(alpha = 0.5f),
                    border = BorderStroke(1.dp, DeepTeal.copy(alpha = 0.3f)),
                    modifier = Modifier.size(54.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Outlined.Terminal,
                            contentDescription = null,
                            tint = DeepTeal,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = team.projectName,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = DeepNavy,
                            fontSize = 21.sp
                        )
                    )
                    Text(
                        text = "معرّف المشروع: #${team.teamId}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                    )
                }
            }
        }
    }
}

// =============================================================================
// بطاقة قائد الفريق والإشراف الأكاديمي (TeamLeaderAndMentorsCard)
// =============================================================================
@Composable
private fun TeamLeaderAndMentorsCard(
    team: Team
) {
    Card(
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
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // بيانات قائد الفريق
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = LeaderCrownGold.copy(alpha = 0.35f),
                    border = BorderStroke(1.5.dp, LeaderCrownGold),
                    modifier = Modifier.size(50.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Outlined.WorkspacePremium,
                            contentDescription = null,
                            tint = DeepNavy,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = team.leaderName,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = DeepNavy,
                                fontSize = 16.5.sp
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Text(
                        text = "قائد ومؤسس الفريق 👑",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = DeepTeal,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.5.sp
                        )
                    )
                }
            }

            // خط فاصل خفيف
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(SoftGray.copy(alpha = 0.4f))
            )

            // الإشراف الأكاديمي (الدكتور والمعيدة)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 62.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = WarmCream.copy(alpha = 0.8f),
                    border = BorderStroke(0.8.dp, SoftGray.copy(alpha = 0.6f))
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.SupervisorAccount,
                            contentDescription = null,
                            tint = DeepTeal,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "المشرف الأكاديمي",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color.Gray,
                                    fontSize = 10.5.sp
                                )
                            )
                            Text(
                                text = team.supervisorDoctor,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = DeepNavy,
                                    fontSize = 12.sp
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 62.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = WarmCream.copy(alpha = 0.8f),
                    border = BorderStroke(0.8.dp, SoftGray.copy(alpha = 0.6f))
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = null,
                            tint = DeepTeal,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "المعيدة المشرفة",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color.Gray,
                                    fontSize = 10.5.sp
                                )
                            )
                            Text(
                                text = team.teachingAssistant,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = DeepNavy,
                                    fontSize = 12.sp
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}

// =============================================================================
// بطاقة وصف فكرة المشروع (ProjectDescriptionCard)
// =============================================================================
@Composable
private fun ProjectDescriptionCard(
    description: String
) {
    Card(
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
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = null,
                    tint = DeepTeal,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "عن فكرة المشروع وأهدافه",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = DeepNavy,
                        fontSize = 16.sp
                    )
                )
            }

            Text(
                text = description,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.DarkGray,
                    fontSize = 14.5.sp,
                    lineHeight = 23.sp
                )
            )
        }
    }
}

// =============================================================================
// بطاقة إحصائيات المقاعد الشاملة (SeatAvailabilityMetricsCard)
// =============================================================================
@Composable
private fun SeatAvailabilityMetricsCard(
    totalSeats: Int,
    bookedCount: Int,
    availableCount: Int,
    pendingCount: Int,
    completionPercent: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.6f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // الهيدر مع نسبة الاكتمال الكلية
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "حالة إشغال المقاعد",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = DeepNavy,
                            fontSize = 17.sp
                        )
                    )
                    Text(
                        text = "تم اعتماد $bookedCount من أصل $totalSeats مقاعد رسمياً",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = LightAqua.copy(alpha = 0.6f),
                    border = BorderStroke(1.dp, DeepTeal.copy(alpha = 0.3f))
                ) {
                    Text(
                        text = "$completionPercent% اكتمال",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = DeepTeal,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.5.sp
                        )
                    )
                }
            }

            // شريط التقدم المرئي
            LinearProgressIndicator(
                progress = { completionPercent / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(7.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = DeepTeal,
                trackColor = SoftBeige
            )

            // شبكة المقاييس 2x2
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SeatMetricBox(
                        modifier = Modifier.weight(1f),
                        label = "مقاعد شاغرة",
                        value = "$availableCount متاح",
                        icon = Icons.Outlined.AirlineSeatReclineNormal,
                        iconTint = Turquoise,
                        bgColor = SeatAvailableBg
                    )
                    SeatMetricBox(
                        modifier = Modifier.weight(1f),
                        label = "أعضاء معتمدين",
                        value = "$bookedCount طلاب",
                        icon = Icons.Default.Verified,
                        iconTint = DeepNavy,
                        bgColor = SeatBookedBg
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SeatMetricBox(
                        modifier = Modifier.weight(1f),
                        label = "قيد المراجعة",
                        value = "$pendingCount طلبات",
                        icon = Icons.Outlined.HourglassTop,
                        iconTint = SeatPendingAmber,
                        bgColor = SeatPendingBg
                    )
                    SeatMetricBox(
                        modifier = Modifier.weight(1f),
                        label = "إجمالي السعة",
                        value = "$totalSeats مقاعد",
                        icon = Icons.Outlined.Groups,
                        iconTint = DeepTeal,
                        bgColor = SoftBeige
                    )
                }
            }
        }
    }
}

@Composable
private fun SeatMetricBox(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    bgColor: Color
) {
    Surface(
        modifier = modifier.heightIn(min = 66.dp),
        shape = RoundedCornerShape(14.dp),
        color = bgColor.copy(alpha = 0.7f),
        border = BorderStroke(0.8.dp, SoftGray.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = DeepNavy,
                        fontSize = 13.5.sp
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
}

// =============================================================================
// قسم المعاينة السريعة للمقاعد (SeatMapPreviewSection)
// =============================================================================
@Composable
private fun SeatMapPreviewSection(
    seats: List<Seat>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.6f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // الهيدر ودليل الألوان
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "تشكيل المقاعد والتخصصات",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = DeepNavy,
                        fontSize = 17.sp
                    )
                )

                // مفتاح الألوان المصغر
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MiniLegend(label = "متاح", color = Turquoise)
                    MiniLegend(label = "معتمد", color = DeepNavy)
                    MiniLegend(label = "انتظار", color = SeatPendingAmber)
                }
            }

            // قائمة المقاعد كبطاقات تذاكر مصغرة
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                seats.forEach { seat ->
                    MiniSeatTicket(seat = seat)
                }
            }
        }
    }
}

@Composable
private fun MiniLegend(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(color, CircleShape)
        )
        Spacer(modifier = Modifier.width(3.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 10.sp,
                color = Color.Gray
            )
        )
    }
}

@Composable
private fun MiniSeatTicket(
    seat: Seat
) {
    val statusBg = when (seat.status) {
        SeatStatus.BOOKED -> SeatBookedBg
        SeatStatus.AVAILABLE -> SeatAvailableBg
        SeatStatus.PENDING -> SeatPendingBg
        SeatStatus.LOCKED -> SoftBeige
    }

    val statusColor = when (seat.status) {
        SeatStatus.BOOKED -> DeepNavy
        SeatStatus.AVAILABLE -> DeepTeal
        SeatStatus.PENDING -> SeatPendingAmber
        SeatStatus.LOCKED -> Color.Gray
    }

    val statusLabel = when (seat.status) {
        SeatStatus.BOOKED -> if (seat.assignedStudentName != null) "معتمد: ${seat.assignedStudentName?.substringBefore(" ")}" else "معتمد"
        SeatStatus.AVAILABLE -> "شاغر للحجز 🟢"
        SeatStatus.PENDING -> "قيد المراجعة ⏳"
        SeatStatus.LOCKED -> "مقفل 🔒"
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 52.dp),
        shape = RoundedCornerShape(12.dp),
        color = statusBg.copy(alpha = 0.5f),
        border = BorderStroke(0.8.dp, SoftGray.copy(alpha = 0.6f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 9.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = statusBg,
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = String.format("%02d", seat.seatNumber),
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = statusColor,
                                fontSize = 12.sp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = seat.roleRequired,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = DeepNavy,
                                fontSize = 13.5.sp
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (seat.isLeader) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "👑",
                                fontSize = 12.sp
                            )
                        }
                    }
                    Text(
                        text = statusLabel,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = statusColor,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.5.sp
                        )
                    )
                }
            }
        }
    }
}

// =============================================================================
// الزر العريض الثابت بالأسفل (TeamOverviewBottomCta)
// =============================================================================
@Composable
private fun TeamOverviewBottomCta(
    availableSeatsCount: Int,
    isLocked: Boolean,
    onOpenSeatMapClick: () -> Unit
) {
    Surface(
        color = Color.White,
        shadowElevation = 16.dp,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            Button(
                onClick = onOpenSeatMapClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                shape = RoundedCornerShape(28.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp, pressedElevation = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.AirlineSeatReclineNormal,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = if (isLocked) "استعراض خريطة المقاعد المعتمدة" else "افتح خريطة المقاعد واحجز دورك 💺",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 16.sp
                    )
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

// =============================================================================
// Preview
// =============================================================================
@Preview(showBackground = true)
@Composable
private fun TeamDetailsOverviewScreenPreview() {
    TeamSeatTheme {
        TeamDetailsOverviewScreen()
    }
}
