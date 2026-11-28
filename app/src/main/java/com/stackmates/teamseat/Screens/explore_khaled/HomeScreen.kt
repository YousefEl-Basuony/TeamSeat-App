package com.stackmates.teamseat.Screens.explore_khaled

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Chat
import androidx.compose.material.icons.automirrored.rounded.FormatListBulleted
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material.icons.rounded.Folder
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stackmates.teamseat.core.data.MockDataProvider
import com.stackmates.teamseat.core.model.SeatStatus
import com.stackmates.teamseat.core.model.Team
import com.stackmates.teamseat.core.model.User
import com.stackmates.teamseat.core.theme.TajawalFontFamily
import com.stackmates.teamseat.core.theme.TeamSeatTheme
import com.stackmates.teamseat.ui.components.ProjectCategoryIllustration
import com.stackmates.teamseat.ui.components.StaggeredAnimatedItem
import com.stackmates.teamseat.ui.components.TeamSeatBgOffWhite
import com.stackmates.teamseat.ui.components.TeamSeatBodyMuted
import com.stackmates.teamseat.ui.components.TeamSeatCard
import com.stackmates.teamseat.ui.components.TeamSeatCardBorderTeal
import com.stackmates.teamseat.ui.components.TeamSeatDividerColor
import com.stackmates.teamseat.ui.components.TeamSeatPrimaryTeal
import com.stackmates.teamseat.ui.components.TeamSeatScreenContainer
import com.stackmates.teamseat.ui.components.TeamSeatSoftTealPill
import com.stackmates.teamseat.ui.components.TeamSeatSurfaceColor
import com.stackmates.teamseat.ui.components.TeamSeatTitleDark
import com.stackmates.teamseat.ui.components.UserAvatarContent
import com.stackmates.teamseat.ui.components.bounceClick

// =============================================================================
// 🎨 باليت الشاشة المستوردة من النظام الموحد (TeamSeatLayout)
// =============================================================================
private val HomeBg: Color get() = TeamSeatBgOffWhite
private val TealPrimary: Color get() = TeamSeatPrimaryTeal
private val TextTitleDark: Color get() = TeamSeatTitleDark
private val TextBodyMuted: Color get() = TeamSeatBodyMuted

private val SearchBorderColor: Color get() = TeamSeatCardBorderTeal
private val StatCardBorderColor: Color get() = TeamSeatDividerColor
private val ProjectCardBorder: Color get() = TeamSeatCardBorderTeal
private val SoftTealPill: Color get() = TeamSeatSoftTealPill
private val IconSquareBg: Color get() = TeamSeatSoftTealPill
private val OnlineGreen = Color(0xFF10B981)
private val BadgeRed = Color(0xFFEF4444)

/**
 * 🔍 المسؤول عن الشاشة: خالد (Khaled) — [KHALED-1]
 * الشاشة الرئيسية لاستكشاف الفرق والمشاريع (HomeScreen)
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
    onSwitchPersona: () -> Unit = {},
    onExploreClick: () -> Unit = onSearchClick,
    onMyTeamClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onChatClick: () -> Unit = {},
    hasUnreadChat: Boolean = false,
    unreadNotificationsCount: Int? = null,
    showBottomBar: Boolean = true
) {
    var selectedFilter by remember { mutableStateOf("الكل") }
    var selectedNavIndex by remember { mutableIntStateOf(0) }

    val unreadNotifCount = unreadNotificationsCount ?: 0
    val currentTeamName = remember(teams, currentUser) {
        teams.firstOrNull { !currentUser.currentTeamId.isNullOrBlank() && it.teamId == currentUser.currentTeamId }?.projectName
            ?: teams.firstOrNull { it.leaderId == currentUser.uid }?.projectName
    }

    val filteredTeams = remember(teams, selectedFilter) {
        when (selectedFilter) {
            "متاح الآن" -> teams.filter { !it.isLocked && !it.isCompleted }
            "مكتمل" -> teams.filter { it.isLocked && !it.isCompleted }
            "تم تنفيذه" -> teams.filter { it.isCompleted }
            else -> teams
        }
    }

    TeamSeatScreenContainer(
        topBar = {
            HomeTopHeaderRow(
                currentUser = currentUser,
                currentTeamName = currentTeamName,
                unreadCount = unreadNotifCount,
                hasUnreadChat = hasUnreadChat,
                onChatClick = onChatClick,
                onNotificationsClick = onNotificationsClick,
                onMyApplicationsClick = onMyApplicationsClick,
                onAvatarClick = onSwitchPersona
            )
        },
        floatingActionButton = {
            val fabTransition = rememberInfiniteTransition(label = "fabFloat")
            val fabFloatY by fabTransition.animateFloat(
                initialValue = -2.5f,
                targetValue = 2.5f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 1900, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "fabFloatY"
            )
            val rayAlpha by fabTransition.animateFloat(
                initialValue = 0.45f,
                targetValue = 1.0f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "rayAlpha"
            )
            Row(
                modifier = Modifier.graphicsLayer { translationY = fabFloatY.dp.toPx() },
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .height(48.dp)
                        .shadow(
                            elevation = 10.dp,
                            shape = RoundedCornerShape(24.dp),
                            ambientColor = TealPrimary,
                            spotColor = TealPrimary
                        )
                        .clip(RoundedCornerShape(24.dp))
                        .background(TealPrimary)
                        .bounceClick(onClick = onCreateTeamClick)
                        .padding(horizontal = 22.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = "إنشاء فريق",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "إنشاء فريق",
                            fontFamily = TajawalFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 14.5.sp,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }

                Canvas(
                    modifier = Modifier
                        .size(22.dp)
                        .offset(y = (-6).dp)
                        .graphicsLayer { alpha = rayAlpha }
                ) {
                    drawLine(
                        color = TealPrimary,
                        start = Offset(size.width * 0.78f, size.height * 0.78f),
                        end = Offset(size.width * 0.35f, size.height * 0.22f),
                        strokeWidth = 2.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                    drawLine(
                        color = TealPrimary,
                        start = Offset(size.width * 0.72f, size.height * 0.92f),
                        end = Offset(size.width * 0.12f, size.height * 0.85f),
                        strokeWidth = 2.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = {
            if (showBottomBar) {
                HomeBottomNavigationBar(
                    selectedIndex = selectedNavIndex,
                    onSelect = { index ->
                        selectedNavIndex = index
                        when (index) {
                            1 -> onExploreClick()
                            2 -> onMyTeamClick()
                            3 -> onProfileClick()
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding()),
            contentPadding = PaddingValues(
                top = 8.dp,
                bottom = 84.dp
            ),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
                    // 1. شريط البحث
                    item {
                        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                            HomeSearchBar(onClick = onSearchClick)
                        }
                    }

                    // 2. شرائح الفلترة (الكل، متاح الآن، مكتمل، تم تنفيذه)
                    item {
                        val filters = listOf("الكل", "متاح الآن", "مكتمل", "تم تنفيذه")
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(filters) { filter ->
                                HomeFilterChip(
                                    label = filter,
                                    isSelected = selectedFilter == filter,
                                    onClick = { selectedFilter = filter }
                                )
                            }
                        }
                    }

                    // 3. بطاقتا الإحصائيات السريعة مع عداد متحرك
                    item {
                        val availableProjectsCount by animateIntAsState(
                            targetValue = teams.size,
                            animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
                            label = "stat1"
                        )
                        val needMembersCount by animateIntAsState(
                            targetValue = teams.count { !it.isLocked },
                            animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
                            label = "stat2"
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            QuickStatCard(
                                modifier = Modifier.weight(1f),
                                number = availableProjectsCount.toString(),
                                label = "مشروع متاح",
                                icon = Icons.Rounded.Folder,
                                onClick = onSearchClick
                            )
                            QuickStatCard(
                                modifier = Modifier.weight(1f),
                                number = needMembersCount.toString(),
                                label = "تيمات محتاجة أعضاء",
                                icon = Icons.Rounded.Groups,
                                showInnerWave = true,
                                onClick = { selectedFilter = "متاح الآن" }
                            )
                        }
                    }

                    // 4. عنوان القسم: مشاريع مقترحة ليك
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.AutoAwesome,
                                    contentDescription = null,
                                    tint = TealPrimary,
                                    modifier = Modifier.size(19.dp)
                                )
                                Text(
                                    text = "مشاريع مقترحة ليك",
                                    fontFamily = TajawalFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    color = TextTitleDark,
                                    fontSize = 16.sp,
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }

                            Text(
                                text = "شوف الكل ←",
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable(onClick = onSearchClick)
                                    .padding(horizontal = 6.dp, vertical = 4.dp),
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = TealPrimary,
                                fontSize = 13.sp,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }

                    // 5. قائمة كروت المشاريع برسوم Duotone متعددة الطبقات
                    if (filteredTeams.isEmpty()) {
                        item {
                            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color.White)
                                        .border(1.2.dp, StatCardBorderColor, RoundedCornerShape(16.dp))
                                        .padding(28.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "مفيش مشاريع في الفلتر ده دلوقتي",
                                        fontFamily = TajawalFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        color = TextTitleDark,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    } else {
                        itemsIndexed(filteredTeams, key = { _, team -> team.teamId }) { index, team ->
                            StaggeredAnimatedItem(index = index) {
                                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                                    RecommendedProjectCard(
                                        team = team,
                                        onClick = { onTeamClick(team.teamId) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

/**
 * 1. الشريط العلوي (Top Header Row)
 */
@Composable
private fun HomeTopHeaderRow(
    currentUser: User,
    currentTeamName: String? = null,
    unreadCount: Int,
    hasUnreadChat: Boolean = false,
    onChatClick: () -> Unit = {},
    onNotificationsClick: () -> Unit,
    onMyApplicationsClick: () -> Unit,
    onAvatarClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
                .clickable(onClick = onAvatarClick)
        ) {
            Box(modifier = Modifier.size(44.dp)) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(TeamSeatSurfaceColor)
                        .border(1.5.dp, TealPrimary.copy(alpha = 0.35f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    UserAvatarContent(
                        avatarEmoji = currentUser.avatarEmoji,
                        photoBase64 = currentUser.photoBase64,
                        emojiFontSizeSp = 22f
                    )
                }

                val onlinePulse by rememberInfiniteTransition(label = "onlineDot").animateFloat(
                    initialValue = 1.0f,
                    targetValue = 1.24f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "onlinePulse"
                )
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .align(Alignment.BottomEnd)
                        .graphicsLayer {
                            scaleX = onlinePulse
                            scaleY = onlinePulse
                        }
                        .clip(CircleShape)
                        .background(OnlineGreen)
                        .border(2.dp, HomeBg, CircleShape)
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                val firstName = currentUser.name.substringBefore(" ").ifBlank { "سارة" }
                Text(
                    text = "أهلاً، $firstName 👋",
                    fontFamily = TajawalFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = TextTitleDark,
                    fontSize = 17.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = currentUser.roleTag.ifBlank { "Android Developer" },
                    style = TextStyle(
                        fontFamily = TajawalFontFamily,
                        fontWeight = FontWeight.Medium,
                        color = TextBodyMuted,
                        fontSize = 12.sp,
                        textDirection = TextDirection.Ltr
                    ),
                    maxLines = 1,
                    softWrap = false
                )
                if (!currentTeamName.isNullOrBlank()) {
                    Surface(
                        color = SoftTealPill,
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            text = "فريق: $currentTeamName",
                            fontFamily = TajawalFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = TealPrimary,
                            fontSize = 10.5.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .height(36.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(TeamSeatSurfaceColor)
                    .border(1.dp, SearchBorderColor, RoundedCornerShape(18.dp))
                    .bounceClick(onClick = onMyApplicationsClick)
                    .padding(horizontal = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.FormatListBulleted,
                        contentDescription = "طلباتي",
                        tint = TealPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "طلباتي",
                        fontFamily = TajawalFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = TextTitleDark,
                        fontSize = 12.sp,
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(TeamSeatSurfaceColor)
                    .border(1.dp, SearchBorderColor, CircleShape)
                    .bounceClick(onClick = onChatClick),
                contentAlignment = Alignment.Center
            ) {
                BadgedBox(
                    badge = {
                        if (hasUnreadChat) {
                            Badge(containerColor = BadgeRed)
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.Chat,
                        contentDescription = "شات الفريق",
                        tint = TealPrimary,
                        modifier = Modifier.size(19.dp)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(TeamSeatSurfaceColor)
                    .border(1.dp, SearchBorderColor, CircleShape)
                    .bounceClick(onClick = onNotificationsClick),
                contentAlignment = Alignment.Center
            ) {
                BadgedBox(
                    badge = {
                        if (unreadCount > 0) {
                            Badge(
                                containerColor = BadgeRed,
                                contentColor = Color.White
                            ) {
                                Text(
                                    text = "$unreadCount",
                                    fontSize = 9.5.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Notifications,
                        contentDescription = "الإشعارات",
                        tint = TextTitleDark,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

/**
 * 2. شريط البحث الدائري النظيف
 */
@Composable
private fun HomeSearchBar(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(TeamSeatSurfaceColor)
            .border(1.dp, SearchBorderColor, RoundedCornerShape(14.dp))
            .bounceClick(onClick = onClick)
            .padding(horizontal = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = "بحث",
                    tint = TextBodyMuted,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "ابحث عن مشروع، مادة، أو تخصص...",
                    fontFamily = TajawalFontFamily,
                    fontWeight = FontWeight.Normal,
                    color = TextBodyMuted.copy(alpha = 0.85f),
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Icon(
                imageVector = Icons.Rounded.Tune,
                contentDescription = "فلترة",
                tint = TealPrimary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

/**
 * شريحة فلتر واحدة
 */
@Composable
private fun HomeFilterChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bgColor by animateColorAsState(
        targetValue = if (isSelected) TealPrimary else TeamSeatSurfaceColor,
        animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
        label = "chipBg"
    )
    val chipScale by animateFloatAsState(
        targetValue = if (isSelected) 1.04f else 1.0f,
        animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
        label = "chipScale"
    )
    Box(
        modifier = Modifier
            .height(36.dp)
            .wrapContentWidth()
            .graphicsLayer {
                scaleX = chipScale
                scaleY = chipScale
            }
            .clip(RoundedCornerShape(18.dp))
            .background(bgColor)
            .border(
                width = 1.dp,
                color = if (isSelected) TealPrimary else SearchBorderColor,
                shape = RoundedCornerShape(18.dp)
            )
            .bounceClick(onClick = onClick)
            .padding(horizontal = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (!isSelected && label == "متاح الآن") {
                Box(
                    modifier = Modifier
                        .size(7.dp)
                        .clip(CircleShape)
                        .background(OnlineGreen)
                )
            } else if (!isSelected && label == "مكتمل") {
                Box(
                    modifier = Modifier
                        .size(15.dp)
                        .clip(CircleShape)
                        .background(TealPrimary.copy(alpha = 0.14f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = null,
                        tint = TealPrimary,
                        modifier = Modifier.size(10.dp)
                    )
                }
            }

            Text(
                text = label,
                fontFamily = TajawalFontFamily,
                fontWeight = FontWeight.SemiBold,
                color = if (isSelected) Color.White else TextTitleDark,
                fontSize = 12.sp,
                maxLines = 1,
                softWrap = false
            )
        }
    }
}

/**
 * 3. بطاقة الإحصائية السريعة بسهم RTL متجه لليسار (<)
 */
@Composable
private fun QuickStatCard(
    modifier: Modifier = Modifier,
    number: String,
    label: String,
    icon: ImageVector,
    showInnerWave: Boolean = false,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(1.dp, StatCardBorderColor, RoundedCornerShape(16.dp))
            .bounceClick(onClick = onClick)
    ) {
        if (showInnerWave) {
            Canvas(modifier = Modifier.matchParentSize()) {
                val w = size.width
                val h = size.height
                val innerWave = Path().apply {
                    moveTo(0f, 0f)
                    lineTo(w * 0.28f, 0f)
                    cubicTo(
                        w * 0.36f, h * 0.35f,
                        w * 0.24f, h * 0.72f,
                        w * 0.34f, h
                    )
                    lineTo(0f, h)
                    close()
                }
                drawPath(path = innerWave, color = Color(0xFFE8F5F4))
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = TealPrimary,
                        modifier = Modifier.size(21.dp)
                    )
                    Text(
                        text = number,
                        fontFamily = TajawalFontFamily,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextTitleDark,
                        fontSize = 18.sp,
                        maxLines = 1,
                        softWrap = false
                    )
                }
                Text(
                    text = label,
                    fontFamily = TajawalFontFamily,
                    fontWeight = FontWeight.Medium,
                    color = TextBodyMuted,
                    fontSize = 12.sp,
                    maxLines = 1,
                    softWrap = false
                )
            }

            Box(
                modifier = Modifier
                    .size(26.dp)
                    .clip(CircleShape)
                    .background(IconSquareBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.ChevronLeft,
                    contentDescription = null,
                    tint = TealPrimary,
                    modifier = Modifier.size(17.dp)
                )
            }
        }
    }
}

/**
 * 5. كارت المشروع المقترح مع رسم ProjectCategoryIllustration متعدد الطبقات وسهم RTL
 */
@Composable
private fun RecommendedProjectCard(
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

    TeamSeatCard(
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // العمود الأيمن: التصنيف + العنوان بسهم < + الوصف + القائد وشريط التقدم
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White)
                        .border(1.dp, ProjectCardBorder.copy(alpha = 0.7f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = team.category.substringBefore(" (").ifBlank { "مشروع مادة الأندرويد 2026" },
                        fontFamily = TajawalFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = TealPrimary,
                        fontSize = 11.sp,
                        maxLines = 1,
                        softWrap = false
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    val isArabicFirst = team.projectName.firstOrNull()?.let {
                        Character.getDirectionality(it) == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC ||
                            Character.getDirectionality(it) == Character.DIRECTIONALITY_RIGHT_TO_LEFT
                    } == true

                    Text(
                        text = team.projectName,
                        style = TextStyle(
                            fontFamily = TajawalFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = TextTitleDark,
                            fontSize = 16.sp,
                            textDirection = if (isArabicFirst) TextDirection.Rtl else TextDirection.Ltr
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    Icon(
                        imageVector = Icons.Rounded.ChevronLeft,
                        contentDescription = null,
                        tint = TextTitleDark,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Text(
                    text = team.description,
                    fontFamily = TajawalFontFamily,
                    fontWeight = FontWeight.Normal,
                    color = TextBodyMuted,
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(2.dp))

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
                            tint = TealPrimary,
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = team.leaderName.substringBefore(" ("),
                            fontFamily = TajawalFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = TextTitleDark,
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
                        color = TealPrimary,
                        trackColor = Color.White
                    )

                    Text(
                        text = "\u200E$bookedSeats / ${team.totalSeats}\u200E أعضاء",
                        fontFamily = TajawalFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = TealPrimary,
                        fontSize = 11.sp,
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // العمود الأيسر: شارة المقاعد + رسم ProjectCategoryIllustration + زر التفاصيل
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(SoftTealPill)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Groups,
                            contentDescription = null,
                            tint = TealPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = if (team.isLocked) "مكتمل" else "متاح $availableSeats مقاعد",
                            fontFamily = TajawalFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = TealPrimary,
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
                        .background(TealPrimary)
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

/**
 * 6. شريط التنقل السفلي من 4 عناصر (الرئيسية، استكشاف، فريقي، حسابي)
 */
@Composable
private fun HomeBottomNavigationBar(
    selectedIndex: Int,
    onSelect: (Int) -> Unit
) {
    val navItems = listOf(
        "الرئيسية" to Icons.Rounded.Home,
        "استكشاف" to Icons.Rounded.Search,
        "فريقي" to Icons.Rounded.Groups,
        "حسابي" to Icons.Rounded.Person
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        HorizontalDivider(
            thickness = 1.dp,
            color = Color(0xFFE5ECEC)
        )
        NavigationBar(
            containerColor = Color.White,
            tonalElevation = 0.dp
        ) {
        navItems.forEachIndexed { index, (title, icon) ->
            val selected = selectedIndex == index
            NavigationBarItem(
                selected = selected,
                onClick = { onSelect(index) },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = {
                    Text(
                        text = title,
                        fontFamily = TajawalFontFamily,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 11.5.sp,
                        maxLines = 1,
                        softWrap = false
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = TealPrimary,
                    selectedTextColor = TealPrimary,
                    indicatorColor = SoftTealPill,
                    unselectedIconColor = TextBodyMuted,
                    unselectedTextColor = TextBodyMuted
                )
            )
        }
    }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenReferencePreview() {
    TeamSeatTheme {
        HomeScreen()
    }
}
