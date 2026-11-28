package com.stackmates.teamseat.navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stackmates.teamseat.Screens.explore_khaled.HomeScreen
import com.stackmates.teamseat.Screens.explore_khaled.SearchAndFilterScreen
import com.stackmates.teamseat.Screens.requests_hossam.MyProfileScreen
import com.stackmates.teamseat.Screens.workspace_alaa.NoTeamWorkspaceScreen
import com.stackmates.teamseat.Screens.workspace_alaa.TeamWorkspaceScreen
import com.stackmates.teamseat.core.data.MockDataProvider
import com.stackmates.teamseat.core.model.Seat
import com.stackmates.teamseat.core.model.SeatStatus
import com.stackmates.teamseat.core.model.Team
import com.stackmates.teamseat.core.model.User
import com.stackmates.teamseat.data.local.AppSettings
import com.stackmates.teamseat.data.local.LocalAppSettings
import com.stackmates.teamseat.data.local.SavedAccount
import com.stackmates.teamseat.core.theme.TajawalFontFamily
import com.stackmates.teamseat.core.theme.TeamSeatTheme
import com.stackmates.teamseat.ui.components.TeamSeatBgOffWhite
import com.stackmates.teamseat.ui.components.TeamSeatPrimaryTeal
import com.stackmates.teamseat.ui.components.bounceClick

private val NavBarTopDivider = Color(0xFFE0ECEB)
private val NavActiveMintPill = Color(0xFFE2F1F0)
private val NavInactiveSlate = Color(0xFF64748B)

private data class MainNavTab(
    val label: String,
    val activeIcon: ImageVector,
    val inactiveIcon: ImageVector
)

private val MainNavTabsAr = listOf(
    MainNavTab("الرئيسية", Icons.Rounded.Home, Icons.Outlined.Home),
    MainNavTab("استكشاف", Icons.Rounded.Explore, Icons.Outlined.Explore),
    MainNavTab("فريقي", Icons.Rounded.Groups, Icons.Outlined.Groups),
    MainNavTab("حسابي", Icons.Rounded.Person, Icons.Outlined.Person)
)

private val MainNavTabsEn = listOf(
    MainNavTab("Home", Icons.Rounded.Home, Icons.Outlined.Home),
    MainNavTab("Explore", Icons.Rounded.Explore, Icons.Outlined.Explore),
    MainNavTab("My Team", Icons.Rounded.Groups, Icons.Outlined.Groups),
    MainNavTab("Profile", Icons.Rounded.Person, Icons.Outlined.Person)
)

@Composable
fun MainAppShell(
    currentUser: User,
    teams: List<Team>,
    seats: List<Seat>,
    myTeamSeats: List<Seat> = seats,
    unreadNotificationsCount: Int = 0,
    hasUnreadChat: Boolean = false,
    hasUnreadCalendar: Boolean = false,
    savedAccounts: List<SavedAccount> = emptyList(),
    appSettings: AppSettings = LocalAppSettings.current,
    onUpdateSettings: (AppSettings) -> Unit = {},
    onTeamClick: (String) -> Unit,
    onCreateTeamClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onMyApplicationsClick: () -> Unit,
    onSwitchPersona: () -> Unit,
    onOpenCalendarClick: () -> Unit,
    onOpenTASheetClick: () -> Unit,
    onOpenChatClick: () -> Unit = {},
    onHomeChatClick: () -> Unit = onOpenChatClick,
    onReviewRequestsClick: () -> Unit = {},
    onRemoveMemberClick: (Seat) -> Unit = {},
    onDeleteTeamClick: () -> Unit = {},
    onLeaveTeamClick: () -> Unit = {},
    onUpdateTeamDetailsClick: (String, String, String, String, Boolean) -> Unit = { _, _, _, _, _ -> },
    onOpenDemoCenterClick: () -> Unit = {},
    onOpenSuperAdminClick: () -> Unit = {},
    onSaveProfileChanges: (String, String, String) -> Unit = { _, _, _ -> },
    onUpdateNameClick: (String) -> Unit = {},
    onUpdateAvatarClick: (String, String) -> Unit = { _, _ -> },
    onSwitchToAccountClick: (SavedAccount) -> Unit = {},
    onRemoveSavedAccountClick: (SavedAccount) -> Unit = {},
    onLogoutClick: () -> Unit
) {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    val layoutDirection = if (appSettings.languageCode == "en") LayoutDirection.Ltr else LayoutDirection.Rtl
    val bgColor = if (appSettings.isDarkMode) Color(0xFF0D1719) else TeamSeatBgOffWhite

    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
        Scaffold(
            containerColor = bgColor,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            bottomBar = {
                TeamSeatDockedNavBar(
                    selectedIndex = selectedTab,
                    onSelect = { selectedTab = it },
                    hasTeamAlert = hasUnreadChat || hasUnreadCalendar,
                    isDarkMode = appSettings.isDarkMode,
                    isEnglish = appSettings.languageCode == "en"
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = innerPadding.calculateBottomPadding())
            ) {
                AnimatedContent(
                    targetState = selectedTab,
                    transitionSpec = {
                        val direction = if (targetState > initialState) 1 else -1
                        (fadeIn(tween(220, easing = FastOutSlowInEasing)) +
                            slideInHorizontally(tween(220, easing = FastOutSlowInEasing)) { it / 16 * direction })
                            .togetherWith(
                                fadeOut(tween(180, easing = FastOutSlowInEasing)) +
                                    slideOutHorizontally(tween(200, easing = FastOutSlowInEasing)) { -it / 16 * direction }
                            )
                    },
                    label = "mainTabTransition"
                ) { tabIndex ->
                    when (tabIndex) {
                        0 -> HomeScreen(
                            currentUser = currentUser,
                            teams = teams,
                            unreadNotificationsCount = unreadNotificationsCount,
                            hasUnreadChat = hasUnreadChat,
                            onChatClick = onHomeChatClick,
                            onTeamClick = onTeamClick,
                            onCreateTeamClick = onCreateTeamClick,
                            onSearchClick = { selectedTab = 1 },
                            onNotificationsClick = onNotificationsClick,
                            onMyApplicationsClick = onMyApplicationsClick,
                            onSwitchPersona = { selectedTab = 3 },
                            onExploreClick = { selectedTab = 1 },
                            onMyTeamClick = { selectedTab = 2 },
                            onProfileClick = { selectedTab = 3 },
                            showBottomBar = false
                        )

                        1 -> SearchAndFilterScreen(
                            teams = teams,
                            onTeamClick = { onTeamClick(it) },
                            onBackClick = { selectedTab = 0 }
                        )

                        2 -> {
                            val myTeam = teams.firstOrNull {
                                !currentUser.currentTeamId.isNullOrBlank() && it.teamId == currentUser.currentTeamId
                            } ?: teams.firstOrNull { it.leaderId == currentUser.uid }

                            if (myTeam == null) {
                                NoTeamWorkspaceScreen(
                                    onExploreTeamsClick = { selectedTab = 1 },
                                    onCreateTeamClick = onCreateTeamClick,
                                    onMyApplicationsClick = onMyApplicationsClick,
                                    onBackClick = { selectedTab = 0 }
                                )
                            } else {
                                TeamWorkspaceScreen(
                                    team = myTeam,
                                    seats = myTeamSeats,
                                    isLeader = myTeam.leaderId == currentUser.uid,
                                    pendingRequestsCount = myTeamSeats.count { it.status == SeatStatus.PENDING },
                                    hasUnreadCalendar = hasUnreadCalendar,
                                    onReviewRequestsClick = onReviewRequestsClick,
                                    onRemoveMemberClick = onRemoveMemberClick,
                                    onDeleteTeamClick = onDeleteTeamClick,
                                    onLeaveTeamClick = onLeaveTeamClick,
                                    onUpdateTeamDetailsClick = onUpdateTeamDetailsClick,
                                    onOpenCalendarClick = onOpenCalendarClick,
                                    onOpenTASheetClick = onOpenTASheetClick,
                                    onOpenChatClick = onOpenChatClick,
                                    onBackClick = { selectedTab = 0 }
                                )
                            }
                        }

                        else -> MyProfileScreen(
                            user = currentUser,
                            savedAccounts = savedAccounts,
                            appSettings = appSettings,
                            onUpdateSettings = onUpdateSettings,
                            onSaveChangesClick = { role, github, linkedin ->
                                onSaveProfileChanges(role, github, linkedin)
                            },
                            onUpdateNameClick = onUpdateNameClick,
                            onUpdateAvatarClick = onUpdateAvatarClick,
                            onSwitchToAccountClick = onSwitchToAccountClick,
                            onRemoveSavedAccountClick = onRemoveSavedAccountClick,
                            onLogoutClick = onLogoutClick,
                            onOpenDemoCenterClick = onOpenDemoCenterClick,
                            onOpenSuperAdminClick = onOpenSuperAdminClick,
                            onBackClick = { selectedTab = 0 }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TeamSeatDockedNavBar(
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    hasTeamAlert: Boolean = false,
    isDarkMode: Boolean = false,
    isEnglish: Boolean = false
) {
    val tabs = if (isEnglish) MainNavTabsEn else MainNavTabsAr
    val pillShape = RoundedCornerShape(16.dp)
    val snappyTween = tween<Float>(durationMillis = 220, easing = FastOutSlowInEasing)
    val animatedIndex by animateFloatAsState(
        targetValue = selectedIndex.toFloat(),
        animationSpec = snappyTween,
        label = "slidingPillIndex"
    )

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = if (isDarkMode) Color(0xFF111D20) else Color.White,
        shadowElevation = 0.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            HorizontalDivider(
                thickness = 1.dp,
                color = if (isDarkMode) Color(0xFF28474B) else NavBarTopDivider
            )
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .height(58.dp)
                    .padding(horizontal = 10.dp, vertical = 7.dp)
            ) {
                val tabWidth = maxWidth / tabs.size

                // الكبسولة النعناعية المنزلقة بسرعة واستجابة فورية خلف التبويب النشط
                Box(
                    modifier = Modifier
                        .offset(x = tabWidth * animatedIndex)
                        .width(tabWidth)
                        .fillMaxHeight()
                        .clip(pillShape)
                        .background(if (isDarkMode) Color(0xFF1A3538) else NavActiveMintPill)
                )

                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    tabs.forEachIndexed { index, tab ->
                        val isSelected = selectedIndex == index
                        val iconScale by animateFloatAsState(
                            targetValue = if (isSelected) 1.1f else 1.0f,
                            animationSpec = snappyTween,
                            label = "navIconScale$index"
                        )

                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clip(pillShape)
                                .bounceClick { onSelect(index) },
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            androidx.compose.material3.BadgedBox(
                                badge = {
                                    if (index == 2 && hasTeamAlert) {
                                        androidx.compose.material3.Badge(
                                            containerColor = Color(0xFFEF4444)
                                        )
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = if (isSelected) tab.activeIcon else tab.inactiveIcon,
                                    contentDescription = tab.label,
                                    tint = if (isSelected) TeamSeatPrimaryTeal else NavInactiveSlate,
                                    modifier = Modifier
                                        .size(22.dp)
                                        .graphicsLayer {
                                            scaleX = iconScale
                                            scaleY = iconScale
                                        }
                                )
                            }
                            AnimatedVisibility(
                                visible = isSelected,
                                enter = expandHorizontally(
                                    animationSpec = tween(durationMillis = 220, easing = FastOutSlowInEasing)
                                ) + fadeIn(tween(durationMillis = 220, easing = FastOutSlowInEasing)),
                                exit = shrinkHorizontally(
                                    animationSpec = tween(durationMillis = 180, easing = FastOutSlowInEasing)
                                ) + fadeOut(tween(durationMillis = 140))
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Text(
                                        text = tab.label,
                                        fontFamily = TajawalFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = TeamSeatPrimaryTeal,
                                        maxLines = 1,
                                        softWrap = false
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TeamSeatDockedNavBarPreview() {
    TeamSeatTheme {
        TeamSeatDockedNavBar(selectedIndex = 0, onSelect = {})
    }
}
