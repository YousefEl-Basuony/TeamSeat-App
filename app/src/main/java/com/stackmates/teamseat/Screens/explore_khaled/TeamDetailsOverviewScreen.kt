package com.stackmates.teamseat.Screens.explore_khaled

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.outlined.AirlineSeatReclineNormal
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.SupervisorAccount
import androidx.compose.material.icons.outlined.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stackmates.teamseat.core.data.MockDataProvider
import com.stackmates.teamseat.core.model.Seat
import com.stackmates.teamseat.core.model.SeatStatus
import com.stackmates.teamseat.core.model.Team
import com.stackmates.teamseat.core.theme.TeamSeatTheme

/**
 * 🔍 المسؤول عن الشاشة: خالد (Khaled) — [KHALED-3]
 * شاشة تفاصيل المشروع الشاملة قبل فتح خريطة الكراسي (Team Details Overview Screen)
 */
@Composable
fun TeamDetailsOverviewScreen(
    team: Team = MockDataProvider.sampleTeam,
    seats: List<Seat> = MockDataProvider.sampleSeats,
    onOpenSeatMapClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val totalSeats = if (seats.isNotEmpty()) seats.size else team.totalSeats
    val bookedCount = seats.count { it.status == SeatStatus.BOOKED }
    val availableCount = seats.count { it.status == SeatStatus.AVAILABLE }
    val pendingCount = seats.count { it.status == SeatStatus.PENDING }
    val completionPercent = if (totalSeats > 0) ((bookedCount.toFloat() / totalSeats) * 100).toInt() else 0

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets.systemBars,
        topBar = {
            TeamOverviewTopBar(
                teamCategory = team.category,
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            TeamOverviewBottomCta(
                isLocked = team.isLocked,
                onOpenSeatMapClick = onOpenSeatMapClick
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(top = 8.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. بطاقة عنوان المشروع وحالته
            item {
                Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                    ProjectHeroCard(team = team)
                }
            }

            // 2. قائد الفريق والإشراف الأكاديمي
            item {
                Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                    TeamLeaderAndMentorsCard(team = team)
                }
            }

            // 3. وصف فكرة المشروع
            item {
                Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                    ProjectDescriptionCard(description = team.description)
                }
            }

            // 4. ملخص إحصائيات المقاعد وتشكيل الفريق
            item {
                Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                    SeatAvailabilitySummaryCard(
                        totalSeats = totalSeats,
                        bookedCount = bookedCount,
                        availableCount = availableCount,
                        pendingCount = pendingCount,
                        completionPercent = completionPercent,
                        seats = seats
                    )
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
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = Color.White,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                shadowElevation = 0.dp,
                modifier = Modifier.size(40.dp)
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "رجوع",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(19.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "تفاصيل الفريق",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 19.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = teamCategory,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        fontWeight = FontWeight.Medium,
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
// كارت بطل المشروع الهادئ (ProjectHeroCard)
// =============================================================================
@Composable
private fun ProjectHeroCard(
    team: Team
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = Color.White,
        shadowElevation = 1.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = if (team.isLocked) {
                        MaterialTheme.colorScheme.secondaryContainer
                    } else {
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                    }
                ) {
                    Text(
                        text = if (team.isLocked) "فريق مقفل" else "متاح للحجز",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = if (team.isLocked) {
                                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                            } else {
                                MaterialTheme.colorScheme.primary
                            },
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.5.sp
                        ),
                        maxLines = 1,
                        softWrap = false
                    )
                }

                Text(
                    text = "#${team.teamId}",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        fontSize = 11.5.sp
                    ),
                    maxLines = 1,
                    softWrap = false
                )
            }

            Text(
                text = team.projectName,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 20.sp,
                    lineHeight = 28.sp
                )
            )
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
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = Color.White,
        shadowElevation = 1.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Outlined.WorkspacePremium,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = team.leaderName,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 15.5.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "قائد الفريق",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp
                        )
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MentorMiniCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Outlined.SupervisorAccount,
                    role = "المشرف الأكاديمي",
                    name = team.supervisorDoctor
                )
                MentorMiniCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Outlined.Person,
                    role = "المعيدة المشرفة",
                    name = team.teachingAssistant
                )
            }
        }
    }
}

@Composable
private fun MentorMiniCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    role: String,
    name: String
) {
    Surface(
        modifier = modifier.heightIn(min = 56.dp),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = role,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        fontSize = 10.5.sp
                    )
                )
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
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
// بطاقة وصف فكرة المشروع (ProjectDescriptionCard)
// =============================================================================
@Composable
private fun ProjectDescriptionCard(
    description: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = Color.White,
        shadowElevation = 1.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "عن فكرة المشروع",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 15.5.sp
                )
            )

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f),
                    fontSize = 14.sp,
                    lineHeight = 22.sp
                )
            )
        }
    }
}

// =============================================================================
// بطاقة ملخص المقاعد وتشكيل الفريق (SeatAvailabilitySummaryCard)
// =============================================================================
@Composable
private fun SeatAvailabilitySummaryCard(
    totalSeats: Int,
    bookedCount: Int,
    availableCount: Int,
    pendingCount: Int,
    completionPercent: Int,
    seats: List<Seat>
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = Color.White,
        shadowElevation = 1.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "إشغال المقاعد",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 16.sp
                    )
                )

                Text(
                    text = "$completionPercent% ($bookedCount/$totalSeats)",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.5.sp
                    )
                )
            }

            LinearProgressIndicator(
                progress = { completionPercent / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            )

            // إحصائيات مختصرة في صف متوازن
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SeatStatPill(
                    modifier = Modifier.weight(1f),
                    label = "متاح",
                    value = "$availableCount",
                    tintColor = MaterialTheme.colorScheme.primary
                )
                SeatStatPill(
                    modifier = Modifier.weight(1f),
                    label = "معتمد",
                    value = "$bookedCount",
                    tintColor = MaterialTheme.colorScheme.onSurface
                )
                SeatStatPill(
                    modifier = Modifier.weight(1f),
                    label = "انتظار",
                    value = "$pendingCount",
                    tintColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                seats.forEach { seat ->
                    MiniSeatRow(seat = seat)
                }
            }
        }
    }
}

@Composable
private fun SeatStatPill(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    tintColor: Color
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.06f)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = tintColor,
                    fontSize = 15.sp
                )
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    fontSize = 11.sp
                )
            )
        }
    }
}

@Composable
private fun MiniSeatRow(
    seat: Seat
) {
    val statusText = when (seat.status) {
        SeatStatus.BOOKED -> if (seat.assignedStudentName != null) "معتمد: ${seat.assignedStudentName?.substringBefore(" ")}" else "معتمد"
        SeatStatus.AVAILABLE -> "متاح للحجز"
        SeatStatus.PENDING -> "قيد المراجعة"
        SeatStatus.LOCKED -> "مقفل"
    }

    val badgeBg = when (seat.status) {
        SeatStatus.AVAILABLE -> MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
        SeatStatus.PENDING -> MaterialTheme.colorScheme.tertiary.copy(alpha = 0.25f)
        else -> MaterialTheme.colorScheme.secondaryContainer
    }

    val badgeText = when (seat.status) {
        SeatStatus.AVAILABLE -> MaterialTheme.colorScheme.primary
        SeatStatus.PENDING -> MaterialTheme.colorScheme.onSurface
        else -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f)
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.04f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = seat.roleRequired,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 13.5.sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Surface(
                shape = RoundedCornerShape(50),
                color = badgeBg
            ) {
                Text(
                    text = statusText,
                    modifier = Modifier.padding(horizontal = 9.dp, vertical = 3.dp),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = badgeText,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 11.sp
                    ),
                    maxLines = 1,
                    softWrap = false
                )
            }
        }
    }
}

// =============================================================================
// الزر العريض الثابت بالأسفل (TeamOverviewBottomCta)
// =============================================================================
@Composable
private fun TeamOverviewBottomCta(
    isLocked: Boolean,
    onOpenSeatMapClick: () -> Unit
) {
    Surface(
        color = Color.White,
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {
            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp)
            ) {
                Button(
                    onClick = onOpenSeatMapClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 1.dp, pressedElevation = 3.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AirlineSeatReclineNormal,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isLocked) "استعراض خريطة المقاعد" else "افتح خريطة المقاعد واحجز مقعدك",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 14.5.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
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
