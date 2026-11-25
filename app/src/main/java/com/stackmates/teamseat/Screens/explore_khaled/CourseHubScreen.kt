package com.stackmates.teamseat.Screens.explore_khaled

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.School
import androidx.compose.material.icons.rounded.SupervisorAccount
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stackmates.teamseat.core.data.MockDataProvider
import com.stackmates.teamseat.core.model.SeatStatus
import com.stackmates.teamseat.core.model.Team
import com.stackmates.teamseat.core.theme.TajawalFontFamily
import com.stackmates.teamseat.core.theme.TeamSeatTheme
import com.stackmates.teamseat.ui.components.*

/**
 * 🔍 المسؤول عن الشاشة: خالد (Khaled) — [KHALED-3]
 * شاشة مركز المادة الدراسية وتيماتها ونسب الاكتمال (CourseHubScreen)
 * تستخدم TeamSeatScreenContainer (بالموجتين العضويتين) و TeamSeatCard.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseHubScreen(
    courseName: String = "مشروع مادة الأندرويد 2026",
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

    val filteredTeams = remember(teamsInCourse, selectedTab) {
        when (selectedTab) {
            "متاح للانضمام" -> teamsInCourse.filter { !it.isLocked }
            "مكتمل" -> teamsInCourse.filter { it.isLocked }
            else -> teamsInCourse
        }
    }

    TeamSeatScreenContainer(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                navigationIcon = {
                    Box(
                        modifier = Modifier
                            .padding(start = 12.dp, end = 6.dp)
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
                    Column {
                        Text(
                            text = "فرق المادة الدراسية",
                            fontFamily = TajawalFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = TeamSeatTitleDark,
                            fontSize = 18.sp,
                            maxLines = 1,
                            softWrap = false
                        )
                        Text(
                            text = courseName,
                            fontFamily = TajawalFontFamily,
                            fontWeight = FontWeight.Normal,
                            color = TeamSeatBodyMuted,
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
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
                bottom = safePadding.calculateBottomPadding()
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 1. بطاقة ملخص المادة والإشراف الأكاديمي
            item {
                TeamSeatCard(contentPadding = 16.dp) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(TeamSeatPrimaryTeal),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.School,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Text(
                                    text = courseName,
                                    fontFamily = TajawalFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    color = TeamSeatTitleDark,
                                    fontSize = 15.5.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.SupervisorAccount,
                                        contentDescription = null,
                                        tint = TeamSeatPrimaryTeal,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Text(
                                        text = supervisorName,
                                        fontFamily = TajawalFontFamily,
                                        fontWeight = FontWeight.Medium,
                                        color = TeamSeatBodyMuted,
                                        fontSize = 12.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }

                        HorizontalDivider(color = TeamSeatDividerColor, thickness = 0.8.dp)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            CourseStatMini(label = "إجمالي الفرق", value = "$totalTeams فرق")
                            CourseStatMini(label = "الأماكن الفاضية", value = "$totalAvailableSeats مقاعد")
                            CourseStatMini(label = "المكتملة", value = "${teamsInCourse.count { it.isLocked }} تيم")
                        }
                    }
                }
            }

            // 2. تبويبات التصفية
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 2.dp)
                ) {
                    items(segmentTabs) { tab ->
                        val isSelected = selectedTab == tab
                        Box(
                            modifier = Modifier
                                .height(36.dp)
                                .wrapContentWidth()
                                .clip(RoundedCornerShape(18.dp))
                                .background(if (isSelected) TeamSeatPrimaryTeal else Color.White)
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) TeamSeatPrimaryTeal else TeamSeatCardBorderTeal,
                                    shape = RoundedCornerShape(18.dp)
                                )
                                .bounceClick { selectedTab = tab }
                                .padding(horizontal = 14.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = tab,
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isSelected) Color.White else TeamSeatTitleDark,
                                fontSize = 12.sp,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }
                }
            }

            // 3. قائمة فرق المادة
            itemsIndexed(filteredTeams, key = { _, t -> t.teamId }) { index, team ->
                StaggeredAnimatedItem(index = index) {
                    CourseTeamCard(
                        team = team,
                        onClick = { onTeamClick(team.teamId) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CourseStatMini(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontFamily = TajawalFontFamily,
            fontWeight = FontWeight.Bold,
            color = TeamSeatPrimaryTeal,
            fontSize = 14.sp,
            maxLines = 1,
            softWrap = false
        )
        Text(
            text = label,
            fontFamily = TajawalFontFamily,
            fontWeight = FontWeight.Normal,
            color = TeamSeatBodyMuted,
            fontSize = 11.5.sp,
            maxLines = 1,
            softWrap = false
        )
    }
}

@Composable
private fun CourseTeamCard(
    team: Team,
    onClick: () -> Unit
) {
    val availableSeats = if (team.isLocked) {
        0
    } else if (team.teamId == MockDataProvider.sampleTeam.teamId) {
        MockDataProvider.sampleSeats.count { it.status == SeatStatus.AVAILABLE }
    } else {
        (team.totalSeats - 2).coerceAtLeast(1)
    }
    val bookedSeats = (team.totalSeats - availableSeats).coerceAtLeast(1)
    val progress = if (team.totalSeats > 0) bookedSeats.toFloat() / team.totalSeats else 0.5f

    val isArabicFirst = team.projectName.firstOrNull()?.let {
        Character.getDirectionality(it) == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC ||
            Character.getDirectionality(it) == Character.DIRECTIONALITY_RIGHT_TO_LEFT
    } == true

    TeamSeatCard(
        isWarmHighlight = team.isLocked,
        contentPadding = 14.dp,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = team.projectName,
                        style = TextStyle(
                            fontFamily = TajawalFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = TeamSeatTitleDark,
                            fontSize = 15.5.sp,
                            textDirection = if (isArabicFirst) TextDirection.Rtl else TextDirection.Ltr
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    Icon(
                        imageVector = Icons.Rounded.ChevronLeft,
                        contentDescription = null,
                        tint = TeamSeatTitleDark,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Text(
                    text = team.description,
                    fontFamily = TajawalFontFamily,
                    fontWeight = FontWeight.Normal,
                    color = TeamSeatBodyMuted,
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.weight(1f, fill = false)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Person,
                            contentDescription = null,
                            tint = TeamSeatPrimaryTeal,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = team.leaderName.substringBefore(" ("),
                            fontFamily = TajawalFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = TeamSeatTitleDark,
                            fontSize = 11.5.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .width(42.dp)
                            .height(5.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = TeamSeatPrimaryTeal,
                        trackColor = Color.White
                    )

                    Text(
                        text = "\u200E$bookedSeats / ${team.totalSeats}\u200E أعضاء",
                        fontFamily = TajawalFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = TeamSeatPrimaryTeal,
                        fontSize = 11.sp,
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(TeamSeatSoftTealPill)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Groups,
                            contentDescription = null,
                            tint = TeamSeatPrimaryTeal,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = if (team.isLocked) "مكتمل" else "متاح $availableSeats مقاعد",
                            fontFamily = TajawalFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = TeamSeatPrimaryTeal,
                            fontSize = 11.sp,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                ProjectCategoryIllustration(
                    category = "${team.projectName} ${team.category}",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .height(34.dp)
                        .clip(RoundedCornerShape(17.dp))
                        .background(TeamSeatPrimaryTeal)
                        .padding(horizontal = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "ادخل التيم ←",
                        fontFamily = TajawalFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        fontSize = 12.sp,
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CourseHubScreenPreview() {
    TeamSeatTheme {
        CourseHubScreen()
    }
}
