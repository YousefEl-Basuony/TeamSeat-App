package com.stackmates.teamseat.Screens.explore_khaled

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.SupervisorAccount
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stackmates.teamseat.core.data.MockDataProvider
import com.stackmates.teamseat.core.model.SeatStatus
import com.stackmates.teamseat.core.model.Team
import com.stackmates.teamseat.core.theme.TeamSeatTheme

/**
 * 🔍 المسؤول عن الشاشة: خالد (Khaled) — [KHALED-2]
 * شاشة استعراض تيمات مادة دراسية محددة (Course / Subject Hub Screen)
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

    val totalTeams = teamsInCourse.size
    val totalAvailableSeats = teamsInCourse.filter { !it.isLocked }.sumOf { team ->
        if (team.teamId == MockDataProvider.sampleTeam.teamId) {
            MockDataProvider.sampleSeats.count { it.status == SeatStatus.AVAILABLE }
        } else {
            (team.totalSeats - 2).coerceAtLeast(1)
        }
    }

    val filteredTeams = when (selectedFilter) {
        "متاحة للتسجيل" -> teamsInCourse.filter { !it.isLocked }
        "مكتملة ومعتمدة" -> teamsInCourse.filter { it.isLocked }
        else -> teamsInCourse
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets.systemBars,
        topBar = {
            CourseHubTopBar(onBackClick = onBackClick)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(top = 6.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // 1. بطاقة المادة الدراسية والمشرفين
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

            // 2. تصفية فرق المادة
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "الفرق المسجلة",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 17.sp
                            )
                        )
                        Text(
                            text = "${filteredTeams.size} فرق",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                fontWeight = FontWeight.Medium,
                                fontSize = 12.5.sp
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(listOf("الكل", "متاحة للتسجيل", "مكتملة ومعتمدة")) { tab ->
                            val isSelected = selectedFilter == tab
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.White,
                                border = if (isSelected) null else BorderStroke(
                                    1.dp,
                                    MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                                ),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50))
                                    .clickable { selectedFilter = tab }
                            ) {
                                Text(
                                    text = tab,
                                    modifier = Modifier.padding(horizontal = 15.dp, vertical = 8.dp),
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        fontSize = 13.sp
                                    ),
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }
                        }
                    }
                }
            }

            // 3. قائمة كروت الفرق
            if (filteredTeams.isEmpty()) {
                item {
                    Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
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

            Text(
                text = "مركز المادة الدراسية",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 19.sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// =============================================================================
// كارت بطل المادة الهادئ (CourseHeroCard)
// =============================================================================
@Composable
private fun CourseHeroCard(
    courseName: String,
    supervisorName: String,
    totalTeams: Int,
    availableSeats: Int
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
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.School,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "مقرر أكاديمي معتمد",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 11.5.sp
                            ),
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(50),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = "$availableSeats مقاعد متاحة",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        ),
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }

            Text(
                text = courseName,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 20.sp,
                    lineHeight = 28.sp
                )
            )

            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
            )

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
                        imageVector = Icons.Outlined.SupervisorAccount,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(17.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = supervisorName,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f),
                            fontSize = 13.5.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "$totalTeams فرق",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp
                    ),
                    maxLines = 1,
                    softWrap = false
                )
            }
        }
    }
}

// =============================================================================
// كارت فريق المادة الهادئ (CourseTeamCard)
// =============================================================================
@Composable
private fun CourseTeamCard(
    team: Team,
    onClick: () -> Unit
) {
    val isLocked = team.isLocked
    val (bookedSeats, totalSeats, completionPercent) = getTeamCompletionStats(team)
    val availableSeatsCount = if (isLocked) 0 else (totalSeats - bookedSeats).coerceAtLeast(0)

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = if (isLocked) {
                        MaterialTheme.colorScheme.secondaryContainer
                    } else {
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                    }
                ) {
                    Text(
                        text = if (isLocked) "مكتمل ومعتمد" else "متاح $availableSeatsCount مقاعد",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = if (isLocked) {
                                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                            } else {
                                MaterialTheme.colorScheme.primary
                            },
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        ),
                        maxLines = 1,
                        softWrap = false
                    )
                }

                Text(
                    text = "$completionPercent%",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp
                    ),
                    maxLines = 1,
                    softWrap = false
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = team.projectName,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 16.5.sp
                    )
                )
                Text(
                    text = team.description,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f),
                        fontSize = 13.sp,
                        lineHeight = 19.sp
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            LinearProgressIndicator(
                progress = { completionPercent / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = if (isLocked) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            )

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
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.65f),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "${team.leaderName.substringBefore(" (")} • $bookedSeats/$totalSeats أعضاء",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (isLocked) "التفاصيل" else "حجز مقعد",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.5.sp
                        ),
                        maxLines = 1,
                        softWrap = false
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(14.dp)
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
        shape = RoundedCornerShape(18.dp),
        color = Color.White,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        shadowElevation = 0.dp
    ) {
        Column(
            modifier = Modifier.padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                modifier = Modifier.size(52.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Outlined.Groups,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            Text(
                text = "لا توجد فرق مطابقة حالياً",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp
                )
            )

            Text(
                text = if (selectedFilter == "الكل") {
                    "لم يتم تسجيل أي فريق بعد في هذه المادة الدراسية."
                } else {
                    "لا توجد فرق تندرج تحت تصنيف ($selectedFilter) حالياً."
                },
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    fontSize = 13.sp,
                    lineHeight = 19.sp
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

// =============================================================================
// دوال مساعدة لحساب نسبة الاكتمال
// =============================================================================
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
