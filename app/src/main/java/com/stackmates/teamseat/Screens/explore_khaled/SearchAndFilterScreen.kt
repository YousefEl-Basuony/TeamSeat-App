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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.RestartAlt
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.SearchOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
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
 * 🔍 المسؤول عن الشاشة: خالد (Khaled) — [KHALED-2]
 * شاشة البحث والفلترة المتقدمة لمشاريع وتيمات الجامعة (SearchAndFilterScreen)
 * تستخدم TeamSeatScreenContainer (بالموجتين العضويتين) و TeamSeatCard.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAndFilterScreen(
    teams: List<Team> = MockDataProvider.sampleTeamsList,
    roleFilters: List<String> = listOf("الكل", "Android", "Backend", "UI/UX", "Flutter", "AI"),
    onTeamClick: (teamId: String) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf(roleFilters.firstOrNull() ?: "الكل") }
    val focusManager = LocalFocusManager.current

    val filteredTeams = remember(teams, searchQuery, selectedRole) {
        val queryTrimmed = searchQuery.trim()
        teams.filter { team ->
            val matchesQuery = queryTrimmed.isEmpty() ||
                team.projectName.contains(queryTrimmed, ignoreCase = true) ||
                team.category.contains(queryTrimmed, ignoreCase = true) ||
                team.description.contains(queryTrimmed, ignoreCase = true) ||
                team.leaderName.contains(queryTrimmed, ignoreCase = true)

            val matchesRole = if (selectedRole == "الكل") {
                true
            } else {
                team.projectName.contains(selectedRole, ignoreCase = true) ||
                    team.description.contains(selectedRole, ignoreCase = true) ||
                    team.category.contains(selectedRole, ignoreCase = true) ||
                    (team.teamId == MockDataProvider.sampleTeam.teamId &&
                        MockDataProvider.sampleSeats.any {
                            it.roleRequired.contains(selectedRole, ignoreCase = true)
                        })
            }

            matchesQuery && matchesRole
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
                            text = "دوّر على تيمك",
                            fontFamily = TajawalFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = TeamSeatTitleDark,
                            fontSize = 18.sp,
                            maxLines = 1,
                            softWrap = false
                        )
                        Text(
                            text = "ابحث بالمشروع أو المادة أو التخصص اللي بتحبه",
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
            // 1. حقل البحث
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    placeholder = {
                        Text(
                            text = "اكتب اسم مشروع، مادة، أو تخصص...",
                            fontFamily = TajawalFontFamily,
                            color = TeamSeatBodyMuted.copy(alpha = 0.8f),
                            fontSize = 13.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Search,
                            contentDescription = "بحث",
                            tint = TeamSeatPrimaryTeal,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(
                                    imageVector = Icons.Rounded.Close,
                                    contentDescription = "مسح البحث",
                                    tint = TeamSeatBodyMuted,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    },
                    textStyle = TextStyle(
                        fontFamily = TajawalFontFamily,
                        fontSize = 14.sp,
                        color = TeamSeatTitleDark
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = TeamSeatPrimaryTeal,
                        unfocusedBorderColor = Color(0xFFD9E6E6)
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() })
                )
            }

            // 2. شرائح فلاتر التخصصات
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 2.dp)
                ) {
                    items(roleFilters) { role ->
                        val isSelected = selectedRole == role
                        Box(
                            modifier = Modifier
                                .height(36.dp)
                                .wrapContentWidth()
                                .clip(RoundedCornerShape(18.dp))
                                .background(if (isSelected) TeamSeatPrimaryTeal else Color.White)
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) TeamSeatPrimaryTeal else Color(0xFFD9E6E6),
                                    shape = RoundedCornerShape(18.dp)
                                )
                                .bounceClick { selectedRole = role }
                                .padding(horizontal = 14.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = role,
                                style = TextStyle(
                                    fontFamily = TajawalFontFamily,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isSelected) Color.White else TeamSeatTitleDark,
                                    fontSize = 12.sp,
                                    textDirection = if (role == "الكل") TextDirection.Rtl else TextDirection.Ltr
                                ),
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }
                }
            }

            // 3. شريط عدد النتائج
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "النتائج المطابقة",
                        fontFamily = TajawalFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = TeamSeatTitleDark,
                        fontSize = 15.sp
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(TeamSeatSoftTealPill)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${filteredTeams.size} تيمات",
                            fontFamily = TajawalFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = TeamSeatPrimaryTeal,
                            fontSize = 12.sp,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }
            }

            // 4. قائمة النتائج أو حالة عدم وجود نتائج
            if (filteredTeams.isEmpty()) {
                item {
                    TeamSeatCard(contentPadding = 28.dp) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(TeamSeatSoftTealPill),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.SearchOff,
                                    contentDescription = null,
                                    tint = TeamSeatPrimaryTeal,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                            Text(
                                text = "ملقيناش تيم بالمواصفات دي",
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = TeamSeatTitleDark,
                                fontSize = 15.sp,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "جرّب تغيّر كلمة البحث أو اختار \"الكل\" عشان تشوف باقي الفرق.",
                                fontFamily = TajawalFontFamily,
                                color = TeamSeatBodyMuted,
                                fontSize = 12.5.sp,
                                textAlign = TextAlign.Center
                            )
                            Box(
                                modifier = Modifier
                                    .height(40.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(TeamSeatPrimaryTeal)
                                    .bounceClick {
                                        searchQuery = ""
                                        selectedRole = "الكل"
                                    }
                                    .padding(horizontal = 16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.RestartAlt,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "رجّع كل التيمات",
                                        fontFamily = TajawalFontFamily,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.White,
                                        fontSize = 12.5.sp,
                                        maxLines = 1,
                                        softWrap = false
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                itemsIndexed(filteredTeams, key = { _, item -> item.teamId }) { index, team ->
                    StaggeredAnimatedItem(index = index) {
                        SearchTeamResultCard(
                            team = team,
                            onClick = { onTeamClick(team.teamId) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchTeamResultCard(
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
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White)
                        .border(1.dp, TeamSeatCardBorderTeal.copy(alpha = 0.7f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = team.category.substringBefore(" ("),
                        fontFamily = TajawalFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = TeamSeatPrimaryTeal,
                        fontSize = 11.sp,
                        maxLines = 1,
                        softWrap = false
                    )
                }

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
                        text = "عرض التفاصيل ←",
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
private fun SearchAndFilterScreenPreview() {
    TeamSeatTheme {
        SearchAndFilterScreen()
    }
}
