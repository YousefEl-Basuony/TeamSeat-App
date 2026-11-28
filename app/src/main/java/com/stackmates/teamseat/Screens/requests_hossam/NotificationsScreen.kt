package com.stackmates.teamseat.Screens.requests_hossam

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stackmates.teamseat.core.data.MockDataProvider
import com.stackmates.teamseat.core.model.NotificationItem
import com.stackmates.teamseat.core.model.NotificationType
import com.stackmates.teamseat.core.theme.TajawalFontFamily
import com.stackmates.teamseat.core.theme.TeamSeatTheme
import com.stackmates.teamseat.ui.components.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * ⚡ المسؤول عن الشاشة: حسام (Hossam) — [HOSSAM-4]
 * شاشة مركز الإشعارات الفورية (NotificationsScreen)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    notifications: List<NotificationItem> = MockDataProvider.sampleNotifications,
    onNotificationClick: (NotificationItem) -> Unit = {},
    onMarkAllReadClick: () -> Unit = {},
    onClearAllClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val notificationList = remember(notifications) { mutableStateListOf(*notifications.toTypedArray()) }
    var selectedFilter by remember { mutableStateOf("الكل") }
    var showMarkedAllBanner by remember { mutableStateOf(false) }
    var showClearSheet by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val filteredList = when (selectedFilter) {
        "قبول" -> notificationList.filter { it.type == NotificationType.ACCEPTED }
        "طلبات" -> notificationList.filter { it.type == NotificationType.NEW_REQUEST }
        "اعتماد" -> notificationList.filter { it.type == NotificationType.TEAM_LOCKED }
        "مرفوض" -> notificationList.filter { it.type == NotificationType.REJECTED }
        else -> notificationList
    }

    val unreadCount = notificationList.count { !it.isRead }

    TeamSeatScreenContainer(
        snackbarHostState = snackbarHostState,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                navigationIcon = {
                    Box(
                        modifier = Modifier
                            .padding(start = 12.dp, end = 6.dp)
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(TeamSeatSurfaceColor)
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
                            text = "الإشعارات",
                            fontFamily = TajawalFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = TeamSeatTitleDark,
                            fontSize = 18.sp,
                            maxLines = 1,
                            softWrap = false
                        )
                        Text(
                            text = "تابع أخبار طلباتك والتيم أول بأول",
                            fontFamily = TajawalFontFamily,
                            fontWeight = FontWeight.Normal,
                            color = TeamSeatBodyMuted,
                            fontSize = 12.sp,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                },
                actions = {
                    if (unreadCount > 0) {
                        Box(
                            modifier = Modifier
                                .padding(end = 12.dp)
                                .height(36.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .background(TeamSeatSurfaceColor)
                                .border(1.dp, TeamSeatCardBorderTeal, RoundedCornerShape(18.dp))
                                .bounceClick {
                                    for (i in notificationList.indices) {
                                        notificationList[i] = notificationList[i].copy(isRead = true)
                                    }
                                    onMarkAllReadClick()
                                    showMarkedAllBanner = true
                                    scope.launch {
                                        snackbarHostState.showSnackbar("تم تعليم كل الإشعارات كمقروءة ✓")
                                        delay(2500)
                                        showMarkedAllBanner = false
                                    }
                                }
                                .padding(horizontal = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.DoneAll,
                                    contentDescription = "مقروء",
                                    tint = TeamSeatPrimaryTeal,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "مقروء ✓✓",
                                    fontFamily = TajawalFontFamily,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TeamSeatPrimaryTeal,
                                    fontSize = 12.sp,
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }
                        }
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
            item {
                AnimatedVisibility(
                    visible = showMarkedAllBanner,
                    enter = slideInVertically() + fadeIn(),
                    exit = slideOutVertically() + fadeOut()
                ) {
                    TeamSeatCard(contentPadding = 12.dp) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(26.dp)
                                    .clip(CircleShape)
                                    .background(TeamSeatPrimaryTeal),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                            Text(
                                text = "تمام! كل الإشعارات اتعلم عليها كمقروءة",
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = TeamSeatTitleDark,
                                fontSize = 12.5.sp
                            )
                        }
                    }
                }
            }

            item {
                TeamSeatCard(contentPadding = 16.dp) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(TeamSeatSurfaceColor)
                                        .border(1.dp, TeamSeatCardBorderTeal, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.NotificationsActive,
                                        contentDescription = null,
                                        tint = TeamSeatPrimaryTeal,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }

                                Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                                    Text(
                                        text = "كل التنبيهات",
                                        fontFamily = TajawalFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        color = TeamSeatTitleDark,
                                        fontSize = 15.sp,
                                        maxLines = 1,
                                        softWrap = false
                                    )
                                    Text(
                                        text = if (unreadCount > 0) "عندك $unreadCount إشعارات جديدة" else "قريت كل الإشعارات",
                                        fontFamily = TajawalFontFamily,
                                        fontWeight = FontWeight.Normal,
                                        color = TeamSeatBodyMuted,
                                        fontSize = 12.sp,
                                        maxLines = 1,
                                        softWrap = false
                                    )
                                }
                            }

                            if (unreadCount > 0) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(TeamSeatSurfaceColor)
                                        .border(1.dp, TeamSeatCardBorderWarm, RoundedCornerShape(10.dp))
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "$unreadCount جديد",
                                        fontFamily = TajawalFontFamily,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TeamSeatWarmOrange,
                                        fontSize = 12.sp,
                                        maxLines = 1,
                                        softWrap = false
                                    )
                                }
                            }
                        }

                        HorizontalDivider(color = TeamSeatDividerColor, thickness = 0.8.dp)

                        val filterOptions = listOf("الكل", "قبول", "طلبات", "اعتماد", "مرفوض")
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(horizontal = 2.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(filterOptions) { filter ->
                                val isSelected = selectedFilter == filter
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
                                        .bounceClick { selectedFilter = filter }
                                        .padding(horizontal = 14.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = filter,
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
                }
            }

            if (filteredList.isEmpty()) {
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
                                    imageVector = Icons.Rounded.NotificationsNone,
                                    contentDescription = null,
                                    tint = TeamSeatPrimaryTeal,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                            Text(
                                text = "مفيش إشعارات هنا دلوقتي",
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = TeamSeatTitleDark,
                                fontSize = 15.sp,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "أي رد جديد على طلباتك هيوصلك هنا على طول.",
                                fontFamily = TajawalFontFamily,
                                color = TeamSeatBodyMuted,
                                fontSize = 12.5.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                items(
                    items = filteredList,
                    key = { it.id }
                ) { item ->
                    val isWarm = item.type == NotificationType.NEW_REQUEST || item.type == NotificationType.REJECTED
                    val visual = when (item.type) {
                        NotificationType.ACCEPTED -> NotificationVisual(
                            icon = Icons.Rounded.CheckCircle,
                            tint = TeamSeatPrimaryTeal,
                            tagText = "تم القبول ✓",
                            border = TeamSeatCardBorderTeal
                        )
                        NotificationType.NEW_REQUEST -> NotificationVisual(
                            icon = Icons.Rounded.HourglassTop,
                            tint = TeamSeatWarmOrange,
                            tagText = "طلب جديد",
                            border = TeamSeatCardBorderWarm
                        )
                        NotificationType.TEAM_LOCKED -> NotificationVisual(
                            icon = Icons.Rounded.Lock,
                            tint = TeamSeatPrimaryTeal,
                            tagText = "معتمد ومقفل",
                            border = TeamSeatCardBorderTeal
                        )
                        NotificationType.REJECTED -> NotificationVisual(
                            icon = Icons.Rounded.ErrorOutline,
                            tint = TeamSeatDangerRed,
                            tagText = "مرفوض ✕",
                            border = TeamSeatCardBorderWarm
                        )
                    }

                    TeamSeatCard(
                        isWarmHighlight = isWarm || !item.isRead,
                        contentPadding = 16.dp,
                        onClick = {
                            val index = notificationList.indexOfFirst { it.id == item.id }
                            if (index != -1) {
                                notificationList[index] = item.copy(isRead = true)
                            }
                            onNotificationClick(item)
                        }
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(42.dp)
                                            .clip(CircleShape)
                                            .background(TeamSeatSurfaceColor)
                                            .border(1.dp, visual.border, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = visual.icon,
                                            contentDescription = null,
                                            tint = visual.tint,
                                            modifier = Modifier.size(21.dp)
                                        )
                                    }

                                    Text(
                                        text = item.title,
                                        fontFamily = TajawalFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        color = TeamSeatTitleDark,
                                        fontSize = 15.sp,
                                        maxLines = 1,
                                        softWrap = false
                                    )
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                Box(
                                    modifier = Modifier
                                        .height(26.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(TeamSeatSurfaceColor)
                                        .border(1.dp, visual.border, RoundedCornerShape(8.dp))
                                        .padding(horizontal = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = visual.tagText,
                                        fontFamily = TajawalFontFamily,
                                        fontWeight = FontWeight.SemiBold,
                                        color = visual.tint,
                                        fontSize = 12.sp,
                                        maxLines = 1,
                                        softWrap = false
                                    )
                                }
                            }

                            Text(
                                text = item.message,
                                modifier = Modifier.padding(start = 54.dp),
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.Normal,
                                color = TeamSeatBodyMuted,
                                fontSize = 13.sp,
                                lineHeight = 19.5.sp
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 54.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Schedule,
                                        contentDescription = null,
                                        tint = TeamSeatPrimaryTeal,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = item.timeAgo,
                                        fontFamily = TajawalFontFamily,
                                        fontWeight = FontWeight.Medium,
                                        color = TeamSeatBodyMuted,
                                        fontSize = 12.sp,
                                        maxLines = 1,
                                        softWrap = false
                                    )
                                }

                                if (!item.isRead) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(TeamSeatPrimaryTeal)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (notificationList.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(TeamSeatSurfaceColor)
                            .border(1.2.dp, TeamSeatDangerRed.copy(alpha = 0.45f), RoundedCornerShape(14.dp))
                            .bounceClick { showClearSheet = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.DeleteOutline,
                                contentDescription = null,
                                tint = TeamSeatDangerRed,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "امسح كل الإشعارات",
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                color = TeamSeatDangerRed,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }
                }
            }
        }
    }

    if (showClearSheet) {
        ModalBottomSheet(
            onDismissRequest = { showClearSheet = false },
            containerColor = TeamSeatBgOffWhite,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "امسح كل الإشعارات؟",
                    fontFamily = TajawalFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = TeamSeatTitleDark,
                    fontSize = 16.sp
                )
                Text(
                    text = "لو مسحتهم مش هتقدر ترجعهم تاني، متأكد؟",
                    fontFamily = TajawalFontFamily,
                    fontWeight = FontWeight.Normal,
                    color = TeamSeatBodyMuted,
                    fontSize = 13.sp
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(TeamSeatDangerRed)
                            .bounceClick {
                                showClearSheet = false
                                notificationList.clear()
                                onClearAllClick()
                                scope.launch {
                                    snackbarHostState.showSnackbar("تم مسح كل الإشعارات")
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "امسح الكل",
                            fontFamily = TajawalFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                            fontSize = 13.sp,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(TeamSeatSurfaceColor)
                            .border(1.2.dp, TeamSeatCardBorderTeal, RoundedCornerShape(12.dp))
                            .bounceClick { showClearSheet = false },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "إلغاء",
                            fontFamily = TajawalFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = TeamSeatTitleDark,
                            fontSize = 13.sp,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

private data class NotificationVisual(
    val icon: ImageVector,
    val tint: Color,
    val tagText: String,
    val border: Color
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun NotificationsScreenPreview() {
    TeamSeatTheme {
        NotificationsScreen()
    }
}
