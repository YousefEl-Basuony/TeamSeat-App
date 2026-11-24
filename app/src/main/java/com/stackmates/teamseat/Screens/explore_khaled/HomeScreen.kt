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
import androidx.compose.foundation.layout.systemBars
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
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Tune
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
import com.stackmates.teamseat.core.model.DemoPersona
import com.stackmates.teamseat.core.model.SeatStatus
import com.stackmates.teamseat.core.model.Team
import com.stackmates.teamseat.core.model.User
import com.stackmates.teamseat.core.theme.TeamSeatTheme

private val HomePureWhite = Color(0xFFFFFFFF)
private val HomeAquaSurface = Color(0xFFEDF7F7)
private val HomeAquaBorder = Color(0xFF9AD2D4)
private val HomeSearchBorder = Color(0xFFB8E0E1)
private val HomeUrgentSurface = Color(0xFFFFF6ED)
private val HomeUrgentBorder = Color(0xFFF2B885)
private val HomeUrgentBadgeBg = Color(0xFFFDE68A)
private val HomeUrgentBadgeText = Color(0xFFB45309)
private val HomeLockedSurface = Color(0xFFF1F5F9)
private val HomeLockedBorder = Color(0xFFCBD5E1)
private val HomeCourseTagText = Color(0xFF0A344A)
private val HomeNotifCoral = Color(0xFFE05A47)

/**
 * 🔍 المسؤول عن الشاشة: خالد (Khaled) — [KHALED-4]
 * الشاشة الرئيسية واستكشاف التيمات ومقاعد المشاريع الجامعية (Home / Teams Feed Screen)
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
    var selectedQuickFilter by remember { mutableStateOf("الكل") }
    val quickFilters = listOf("الكل", "متاح الآن", "مكتمل")

    val urgentTeams = teams.filter { !it.isLocked }
    val filteredProjects = when (selectedQuickFilter) {
        "متاح الآن" -> teams.filter { !it.isLocked }
        "مكتمل" -> teams.filter { it.isLocked }
        else -> teams
    }

    Scaffold(
        containerColor = HomePureWhite,
        contentWindowInsets = WindowInsets.systemBars,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onCreateTeamClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp),
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 2.dp,
                    pressedElevation = 4.dp
                ),
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "إنشاء فريق",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                },
                text = {
                    Text(
                        text = "إنشاء فريق",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
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
            contentPadding = PaddingValues(top = 12.dp, bottom = 92.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // 1. صف الترحيب الأحادي النظيف
            item {
                CalmHomeGreetingRow(
                    currentUser = currentUser,
                    unreadNotifCount = MockDataProvider.sampleNotifications.count { !it.isRead }.coerceAtLeast(2),
                    onNotificationsClick = onNotificationsClick,
                    onMyApplicationsClick = onMyApplicationsClick
                )
            }

            // 2. شريط البحث + فلاتر سريعة
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                        CalmSearchBarTrigger(onSearchClick = onSearchClick)
                    }

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(quickFilters) { filter ->
                            val isSelected = selectedQuickFilter == filter
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = if (isSelected) MaterialTheme.colorScheme.primary else HomeAquaSurface,
                                border = if (isSelected) null else BorderStroke(1.dp, HomeSearchBorder),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50))
                                    .clickable { selectedQuickFilter = filter }
                            ) {
                                Text(
                                    text = filter,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
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

            // 3. قسم المقاعد العاجلة (أفقي)
            if (urgentTeams.isNotEmpty() && selectedQuickFilter != "مكتمل") {
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
                                text = "مقاعد عاجلة",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontSize = 17.sp
                                )
                            )

                            Text(
                                text = "${urgentTeams.size} فرق",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.5.sp
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 20.dp),
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            items(urgentTeams, key = { "urgent_${it.teamId}" }) { team ->
                                CalmUrgentTeamCard(
                                    team = team,
                                    onClick = { onTeamClick(team.teamId) }
                                )
                            }
                        }
                    }
                }
            }

            // 4. قسم جميع المشاريع (عمودي)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "المشاريع المتاحة",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 17.sp
                        )
                    )

                    Text(
                        text = "${filteredProjects.size} مشاريع",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.5.sp
                        )
                    )
                }
            }

            if (filteredProjects.isEmpty()) {
                item {
                    Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                        EmptyTeamsStateView()
                    }
                }
            } else {
                items(filteredProjects, key = { it.teamId }) { team ->
                    Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                        CalmProjectCard(
                            team = team,
                            onClick = { onTeamClick(team.teamId) }
                        )
                    }
                }
            }

            // 5. شريحة التبديل التجريبي المدمجة وغير المزعجة
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    DemoSwitcherBar(
                        currentPersona = if (currentUser.isLeader) DemoPersona.AHMED_LEADER else DemoPersona.SARA_STUDENT,
                        currentUser = currentUser,
                        onTogglePersona = onSwitchPersona
                    )
                }
            }
        }
    }
}

// =============================================================================
// صف الترحيب الأحادي النظيف (CalmHomeGreetingRow)
// =============================================================================
@Composable
private fun CalmHomeGreetingRow(
    currentUser: User,
    unreadNotifCount: Int,
    onNotificationsClick: () -> Unit,
    onMyApplicationsClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Surface(
                shape = CircleShape,
                color = HomeAquaSurface,
                border = BorderStroke(1.2.dp, HomeAquaBorder),
                shadowElevation = 1.dp,
                modifier = Modifier.size(44.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = currentUser.avatarEmoji,
                        fontSize = 21.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = "أهلاً، ${currentUser.name.substringBefore(" (").substringBefore(" ")}",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 19.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = currentUser.roleTag,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(50),
                color = HomeAquaSurface,
                border = BorderStroke(1.dp, HomeSearchBorder),
                shadowElevation = 1.dp,
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .clickable { onMyApplicationsClick() }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.Assignment,
                        contentDescription = "طلباتي",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "طلباتي",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.5.sp
                        ),
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }

            Surface(
                shape = CircleShape,
                color = HomeAquaSurface,
                border = BorderStroke(1.dp, HomeSearchBorder),
                shadowElevation = 1.dp,
                modifier = Modifier.size(40.dp)
            ) {
                IconButton(onClick = onNotificationsClick) {
                    BadgedBox(
                        badge = {
                            if (unreadNotifCount > 0) {
                                Badge(
                                    containerColor = HomeNotifCoral,
                                    contentColor = HomePureWhite
                                ) {
                                    Text(
                                        text = "$unreadNotifCount",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 9.5.sp
                                    )
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = "التنبيهات",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(19.dp)
                        )
                    }
                }
            }
        }
    }
}

// =============================================================================
// شريط البحث النظيف (CalmSearchBarTrigger)
// =============================================================================
@Composable
private fun CalmSearchBarTrigger(
    onSearchClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onSearchClick() },
        shape = RoundedCornerShape(16.dp),
        color = HomeAquaSurface,
        border = BorderStroke(1.dp, HomeSearchBorder),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = "بحث",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "ابحث عن مشروع، مادة، أو تخصص...",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        fontSize = 13.5.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Icon(
                imageVector = Icons.Outlined.Tune,
                contentDescription = "فلترة",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

// =============================================================================
// كارت المقاعد العاجلة الهادئ (CalmUrgentTeamCard - Horizontal)
// =============================================================================
@Composable
private fun CalmUrgentTeamCard(
    team: Team,
    onClick: () -> Unit
) {
    val availableSeatsCount = if (team.teamId == MockDataProvider.sampleTeam.teamId) {
        MockDataProvider.sampleSeats.count { it.status == SeatStatus.AVAILABLE }
    } else {
        (team.totalSeats - 2).coerceAtLeast(1)
    }
    val bookedSeatsCount = team.totalSeats - availableSeatsCount
    val capacityPercent = if (team.totalSeats > 0) (bookedSeatsCount.toFloat() / team.totalSeats) else 0.5f

    Card(
        onClick = onClick,
        modifier = Modifier.width(280.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = HomeUrgentSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.2.dp, HomeUrgentBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 1. الصف العلوي: شارة المادة + شارة المقاعد المتبقية
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = HomePureWhite,
                    border = BorderStroke(1.dp, HomeUrgentBorder.copy(alpha = 0.7f)),
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    Text(
                        text = team.category.substringBefore(" ("),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = HomeCourseTagText,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Surface(
                    shape = RoundedCornerShape(50),
                    color = HomeUrgentBadgeBg
                ) {
                    Text(
                        text = "متبقي $availableSeatsCount",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = HomeUrgentBadgeText,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        ),
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }

            // 2. الوسط: العنوان + شريط التقدم الرفيع
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = team.projectName,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 16.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "الأعضاء",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                fontSize = 11.5.sp
                            )
                        )
                        Text(
                            text = "$bookedSeatsCount/${team.totalSeats}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 11.5.sp
                            )
                        )
                    }

                    LinearProgressIndicator(
                        progress = { capacityPercent },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    )
                }
            }

            // 3. الصف السفلي: اسم القائد + رابط الحجز
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
                        text = team.leaderName.substringBefore(" ("),
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f),
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "احجز",
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
// كارت المشروع الهوائي (CalmProjectCard - Vertical)
// =============================================================================
@Composable
private fun CalmProjectCard(
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
    val bookedSeatsCount = team.totalSeats - availableSeatsCount
    val capacityPercent = if (team.totalSeats > 0) (bookedSeatsCount.toFloat() / team.totalSeats) else 0.5f

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isLocked) HomeLockedSurface else HomeAquaSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isLocked) 0.dp else 2.dp),
        border = BorderStroke(1.2.dp, if (isLocked) HomeLockedBorder else HomeAquaBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 1. الصف العلوي: أيقونة أكاديمية + اسم المادة، وشارة المقاعد
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = HomePureWhite,
                    border = BorderStroke(1.dp, if (isLocked) HomeLockedBorder else HomeSearchBorder),
                    modifier = Modifier.weight(1f, fill = false)
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
                            text = team.category,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = HomeCourseTagText,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 11.sp
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Surface(
                    shape = RoundedCornerShape(50),
                    color = if (isLocked) {
                        HomePureWhite
                    } else {
                        MaterialTheme.colorScheme.primary
                    }
                ) {
                    Text(
                        text = if (isLocked) "مكتمل" else "متاح $availableSeatsCount مقاعد",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = if (isLocked) {
                                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                            } else {
                                HomePureWhite
                            },
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        ),
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }

            // 2. العنوان والوصف المختصر
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

            // 3. شريط تقدم رفيع
            LinearProgressIndicator(
                progress = { capacityPercent },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = if (isLocked) {
                    MaterialTheme.colorScheme.outline
                } else {
                    MaterialTheme.colorScheme.primary
                },
                trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            )

            // 4. فوتر نظيف: القائد وعدد الأعضاء + رابط التفاصيل
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
                        text = "${team.leaderName.substringBefore(" (")} • $bookedSeatsCount/${team.totalSeats} أعضاء",
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
                        text = "التفاصيل",
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
// حالة عدم وجود مشاريع متاحة (Empty State View)
// =============================================================================
@Composable
private fun EmptyTeamsStateView() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = HomeAquaSurface,
        border = BorderStroke(1.dp, HomeAquaBorder),
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
                text = "لا توجد مشاريع مطابقة حالياً",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp
                )
            )
            Text(
                text = "جرّب تغيير الفلتر أو إنشاء فريق جديد.",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
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
