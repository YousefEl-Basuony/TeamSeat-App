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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.vector.ImageVector
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

private val CoursePureWhite = Color(0xFFFFFFFF)
private val CourseAquaSurface = Color(0xFFEDF7F7)
private val CourseAquaBorder = Color(0xFF9AD2D4)
private val CourseInputBorder = Color(0xFFB8E0E1)
private val CourseLockedSurface = Color(0xFFF1F5F9)
private val CourseLockedBorder = Color(0xFFCBD5E1)
private val CourseTagText = Color(0xFF0A344A)

/**
 * 🔍 المسؤول عن الشاشة: خالد (Khaled) — [KHALED-2]
 * شاشة مركز المادة الدراسية (Academic Course Hub Dashboard)
 */
@Composable
fun CourseHubScreen(
    courseName: String = "مشروع تخرج حاسبات 2026",
    supervisorName: String = "د. محمد كاظم & م. شهد أحمد",
    teamsInCourse: List<Team> = MockDataProvider.sampleTeamsList,
    onTeamClick: (teamId: String) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf("الكل") }
    val segmentTabs = listOf("الكل", "متاح للانضمام", "مكتمل")

    val totalTeams = teamsInCourse.size
    val totalAvailableSeats = teamsInCourse.filter { !it.isLocked }.sumOf { team ->
        if (team.teamId == MockDataProvider.sampleTeam.teamId) {
            MockDataProvider.sampleSeats.count { it.status == SeatStatus.AVAILABLE }
        } else {
            (team.totalSeats - 2).coerceAtLeast(1)
        }
    }

    val averageCompletion = if (totalTeams > 0) {
        teamsInCourse.sumOf { getTeamCompletionStats(it).third } / totalTeams
    } else {
        0
    }

    val filteredTeams = when (selectedTab) {
        "متاح للانضمام" -> teamsInCourse.filter { !it.isLocked }
        "مكتمل" -> teamsInCourse.filter { it.isLocked }
        else -> teamsInCourse
    }

    Scaffold(
        containerColor = CoursePureWhite,
        contentWindowInsets = WindowInsets.systemBars,
        topBar = {
            CourseHubTopBar(onBackClick = onBackClick)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(top = 8.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // 1. بطاقة المادة الدراسية والهيئة التدريسية
            item {
                Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                    CourseAcademicHeroCard(
                        courseName = courseName,
                        supervisorName = supervisorName
                    )
                }
            }

            // 2. شريط ملخص إحصائيات المادة
            item {
                Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                    CourseSummaryStrip(
                        totalTeams = totalTeams,
                        availableSeats = totalAvailableSeats,
                        averageCompletion = averageCompletion
                    )
                }
            }

            // 3. مبدّل التبويبات المقطع (Segmented View Switcher)
            item {
                Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                    CourseSegmentedSwitcher(
                        tabs = segmentTabs,
                        selectedTab = selectedTab,
                        onTabSelected = { selectedTab = it }
                    )
                }
            }

            // 4. قائمة كروت الفرق المركّزة على نسبة الاكتمال
            if (filteredTeams.isEmpty()) {
                item {
                    Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
                        EmptyCourseTeamsView(selectedFilter = selectedTab)
                    }
                }
            } else {
                items(filteredTeams, key = { it.teamId }) { team ->
                    Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                        CourseCompletionTeamCard(
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
// 1. مكون الهيدر العلوي المدمج (CourseHubTopBar)
// =============================================================================
@Composable
private fun CourseHubTopBar(
    onBackClick: () -> Unit
) {
    Surface(
        color = CoursePureWhite,
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
                color = CourseAquaSurface,
                border = BorderStroke(1.dp, CourseInputBorder),
                shadowElevation = 1.dp,
                modifier = Modifier.size(40.dp)
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "رجوع",
                        tint = CourseTagText,
                        modifier = Modifier.size(19.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "مركز المادة الدراسية",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = CourseTagText,
                    fontSize = 19.sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// =============================================================================
// 2. بطاقة المادة الدراسية والهيئة التدريسية (CourseAcademicHeroCard)
// =============================================================================
@Composable
private fun CourseAcademicHeroCard(
    courseName: String,
    supervisorName: String
) {
    val instructors = supervisorName
        .split("&", "،", ",")
        .map { it.trim() }
        .filter { it.isNotEmpty() }

    val primaryInstructor = instructors.getOrNull(0) ?: supervisorName
    val assistantInstructor = instructors.getOrNull(1)

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = CourseAquaSurface,
        shadowElevation = 2.dp,
        border = BorderStroke(1.2.dp, CourseAquaBorder)
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
                    color = CoursePureWhite,
                    border = BorderStroke(1.dp, CourseInputBorder)
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
                                color = CourseTagText,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 11.sp
                            ),
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(50),
                    color = MaterialTheme.colorScheme.primary
                ) {
                    Text(
                        text = "CS-401",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = CoursePureWhite,
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
                    fontWeight = FontWeight.ExtraBold,
                    color = CourseTagText,
                    fontSize = 20.sp,
                    lineHeight = 28.sp
                )
            )

            // شريط الهيئة التدريسية (عمودان متجاوران)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                InstructorMiniCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Outlined.SupervisorAccount,
                    role = "أستاذ المادة",
                    name = primaryInstructor
                )

                if (assistantInstructor != null) {
                    InstructorMiniCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Outlined.Person,
                        role = "المعيدة",
                        name = assistantInstructor
                    )
                }
            }
        }
    }
}

@Composable
private fun InstructorMiniCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    role: String,
    name: String
) {
    Surface(
        modifier = modifier.heightIn(min = 54.dp),
        shape = RoundedCornerShape(12.dp),
        color = CoursePureWhite,
        border = BorderStroke(1.dp, CourseInputBorder)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 9.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = CourseAquaSurface,
                modifier = Modifier.size(30.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = role,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.65f),
                        fontSize = 10.5.sp
                    ),
                    maxLines = 1
                )
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = CourseTagText,
                        fontWeight = FontWeight.Bold,
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
// 3. شريط ملخص إحصائيات المادة (CourseSummaryStrip)
// =============================================================================
@Composable
private fun CourseSummaryStrip(
    totalTeams: Int,
    availableSeats: Int,
    averageCompletion: Int
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = CourseAquaSurface,
        border = BorderStroke(1.dp, CourseInputBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SummaryMetricCell(
                modifier = Modifier.weight(1f),
                value = "$totalTeams فرق",
                label = "إجمالي الفرق"
            )

            Box(
                modifier = Modifier
                    .height(26.dp)
                    .width(1.dp)
                    .background(CourseInputBorder)
            )

            SummaryMetricCell(
                modifier = Modifier.weight(1f),
                value = "$availableSeats مقاعد",
                label = "مقاعد متاحة",
                valueColor = MaterialTheme.colorScheme.primary
            )

            Box(
                modifier = Modifier
                    .height(26.dp)
                    .width(1.dp)
                    .background(CourseInputBorder)
            )

            SummaryMetricCell(
                modifier = Modifier.weight(1f),
                value = "$averageCompletion%",
                label = "متوسط الاكتمال"
            )
        }
    }
}

@Composable
private fun SummaryMetricCell(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    valueColor: Color = CourseTagText
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.ExtraBold,
                color = valueColor,
                fontSize = 14.sp
            ),
            maxLines = 1
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                fontSize = 11.sp
            ),
            maxLines = 1
        )
    }
}

// =============================================================================
// 4. مبدّل التبويبات المقطع (CourseSegmentedSwitcher)
// =============================================================================
@Composable
private fun CourseSegmentedSwitcher(
    tabs: List<String>,
    selectedTab: String,
    onTabSelected: (String) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = CourseAquaSurface,
        border = BorderStroke(1.dp, CourseInputBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            tabs.forEach { tab ->
                val isSelected = tab == selectedTab
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { onTabSelected(tab) },
                    shape = RoundedCornerShape(10.dp),
                    color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
                ) {
                    Box(
                        modifier = Modifier.padding(vertical = 9.dp, horizontal = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tab,
                            style = MaterialTheme.typography.labelLarge.copy(
                                color = if (isSelected) CoursePureWhite else CourseTagText,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 12.5.sp
                            ),
                            maxLines = 1,
                            softWrap = false,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

// =============================================================================
// 5. كارت الفريق المركّز على نسبة الاكتمال (CourseCompletionTeamCard)
// =============================================================================
@Composable
private fun CourseCompletionTeamCard(
    team: Team,
    onClick: () -> Unit
) {
    val isLocked = team.isLocked
    val (bookedSeats, totalSeats, completionPercent) = getTeamCompletionStats(team)
    val availableSeatsCount = if (isLocked) 0 else (totalSeats - bookedSeats).coerceAtLeast(0)
    val openRolesSummary = getOpenRolesSummary(team)

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isLocked) CourseLockedSurface else CourseAquaSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isLocked) 0.dp else 2.dp),
        border = BorderStroke(1.2.dp, if (isLocked) CourseLockedBorder else CourseAquaBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // الهيدر: اسم المشروع + شارة الحالة
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = team.projectName,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = CourseTagText,
                        fontSize = 16.5.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Surface(
                    shape = RoundedCornerShape(50),
                    color = if (isLocked) CoursePureWhite else MaterialTheme.colorScheme.primary
                ) {
                    Text(
                        text = if (isLocked) "مكتمل" else "متبقي $availableSeatsCount مقاعد",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = if (isLocked) {
                                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                            } else {
                                CoursePureWhite
                            },
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        ),
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }

            // السطر الفرعي: قائد الفريق
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "قائد الفريق: ${team.leaderName.substringBefore(" (")}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f),
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.5.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // قسم نسبة اكتمال الفريق البارز
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = CoursePureWhite,
                border = BorderStroke(1.dp, if (isLocked) CourseLockedBorder else CourseInputBorder)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "نسبة اكتمال الفريق",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f),
                                fontWeight = FontWeight.Medium,
                                fontSize = 12.sp
                            )
                        )
                        Text(
                            text = "$completionPercent% • $bookedSeats/$totalSeats أعضاء",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = if (isLocked) CourseTagText else MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 12.5.sp
                            ),
                            maxLines = 1,
                            softWrap = false
                        )
                    }

                    LinearProgressIndicator(
                        progress = { completionPercent / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = if (isLocked) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    )
                }
            }

            // الفوتر: ملخص الأدوار المطلوبة + رابط عرض التفاصيل
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (isLocked) "الفريق مكتمل الأعضاء" else "الأدوار: $openRolesSummary",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f),
                        fontSize = 12.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "عرض التفاصيل",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = if (isLocked) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.primary,
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
                        tint = if (isLocked) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.primary,
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
        color = CourseAquaSurface,
        border = BorderStroke(1.dp, CourseAquaBorder),
        shadowElevation = 0.dp
    ) {
        Column(
            modifier = Modifier.padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = CoursePureWhite,
                border = BorderStroke(1.dp, CourseInputBorder),
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
                    color = CourseTagText,
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
// دوال مساعدة لحساب نسبة الاكتمال والأدوار
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

private fun getOpenRolesSummary(team: Team): String {
    val roles = if (team.teamId == MockDataProvider.sampleTeam.teamId) {
        MockDataProvider.sampleSeats
            .filter { it.status == SeatStatus.AVAILABLE }
            .map { it.roleRequired.substringBefore(" (") }
            .distinct()
    } else {
        when (team.teamId) {
            "team_beta_2" -> listOf("Flutter", "UI/UX", "Backend")
            "team_gamma_3" -> listOf("Android", "Backend", "AI")
            else -> listOf("Android", "Backend", "UI/UX")
        }
    }
    return roles.joinToString("، ").ifEmpty { "متنوعة" }
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
