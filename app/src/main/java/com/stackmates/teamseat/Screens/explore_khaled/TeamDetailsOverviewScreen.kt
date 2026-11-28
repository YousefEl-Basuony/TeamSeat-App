package com.stackmates.teamseat.Screens.explore_khaled

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.EventSeat
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.HourglassTop
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PersonAddAlt1
import androidx.compose.material.icons.rounded.RocketLaunch
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stackmates.teamseat.core.data.MockDataProvider
import com.stackmates.teamseat.core.model.Seat
import com.stackmates.teamseat.core.model.SeatStatus
import com.stackmates.teamseat.core.model.Team
import com.stackmates.teamseat.core.theme.TajawalFontFamily
import com.stackmates.teamseat.core.theme.TeamSeatTheme
import com.stackmates.teamseat.ui.components.ProjectCategoryIllustration
import com.stackmates.teamseat.ui.components.StaggeredAnimatedItem
import com.stackmates.teamseat.ui.components.TeamSeatBodyMuted
import com.stackmates.teamseat.ui.components.TeamSeatCard
import com.stackmates.teamseat.ui.components.TeamSeatCardBorderTeal
import com.stackmates.teamseat.ui.components.TeamSeatCardBorderWarm
import com.stackmates.teamseat.ui.components.TeamSeatPrimaryTeal
import com.stackmates.teamseat.ui.components.TeamSeatScreenContainer
import com.stackmates.teamseat.ui.components.TeamSeatSoftTealPill
import com.stackmates.teamseat.ui.components.TeamSeatTitleDark
import com.stackmates.teamseat.ui.components.TeamSeatWarmOrange
import com.stackmates.teamseat.ui.components.bounceClick
import com.stackmates.teamseat.ui.components.rememberAnimatedProgress

/**
 * 🔍 المسؤول عن الشاشة: خالد (Khaled) — [KHALED-4]
 * شاشة تفاصيل المشروع قبل فتح خريطة الكراسي (TeamDetailsOverviewScreen)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamDetailsOverviewScreen(
    team: Team = MockDataProvider.sampleTeam,
    seats: List<Seat> = MockDataProvider.sampleSeats,
    canViewMemberNames: Boolean = false,
    onOpenSeatMapClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val totalSeats = if (seats.isNotEmpty()) seats.size else team.totalSeats
    val bookedCount = seats.count { it.status == SeatStatus.BOOKED }
    val availableCount = seats.count { it.status == SeatStatus.AVAILABLE }
    val pendingCount = seats.count { it.status == SeatStatus.PENDING }
    val rawProgress = if (totalSeats > 0) bookedCount.toFloat() / totalSeats else 0f
    val animatedProgress by rememberAnimatedProgress(targetProgress = rawProgress)

    TeamSeatScreenContainer(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent),
                navigationIcon = {
                    Box(
                        modifier = Modifier
                            .padding(start = 12.dp)
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
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "تفاصيل المشروع",
                            fontFamily = TajawalFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = TeamSeatTitleDark,
                            fontSize = 19.sp,
                            maxLines = 1,
                            softWrap = false
                        )
                        Text(
                            text = team.category,
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
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(50),
                            ambientColor = TeamSeatPrimaryTeal,
                            spotColor = TeamSeatPrimaryTeal
                        )
                        .clip(RoundedCornerShape(50))
                        .background(
                            Brush.horizontalGradient(
                                listOf(TeamSeatPrimaryTeal, Color(0xFF0D4E55))
                            )
                        )
                        .bounceClick(onClick = onOpenSeatMapClick),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = if (team.isLocked) Icons.Rounded.Lock else Icons.Rounded.EventSeat,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = if (team.isLocked) "التيم مقفول ومكتمل" else "افتح خريطة المقاعد واحجز مكانك",
                            fontFamily = TajawalFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 15.sp,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }
            }
        }
    ) { safePadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = safePadding.calculateTopPadding() + 8.dp,
                bottom = safePadding.calculateBottomPadding() + 14.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 1. بطاقة المشروع الرئيسية
            item {
                StaggeredAnimatedItem(index = 0) {
                    TeamSeatCard(contentPadding = 16.dp) {
                        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(14.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                // رسمة التخصص + شارة الصاروخ
                                Box(modifier = Modifier.size(86.dp)) {
                                    ProjectCategoryIllustration(
                                        category = team.category,
                                        modifier = Modifier.size(82.dp)
                                    )
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .align(Alignment.BottomEnd)
                                            .clip(CircleShape)
                                            .background(Color(0xFF0F4C54))
                                            .border(2.dp, Color.White, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.RocketLaunch,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }

                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Surface(
                                        color = TeamSeatSoftTealPill,
                                        shape = RoundedCornerShape(50)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Rounded.Groups,
                                                contentDescription = null,
                                                tint = TeamSeatPrimaryTeal,
                                                modifier = Modifier.size(15.dp)
                                            )
                                            Text(
                                                text = team.projectName,
                                                style = TextStyle(
                                                    fontFamily = TajawalFontFamily,
                                                    fontWeight = FontWeight.Bold,
                                                    color = TeamSeatTitleDark,
                                                    fontSize = 12.5.sp,
                                                    textDirection = TextDirection.Ltr
                                                ),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.Person,
                                            contentDescription = null,
                                            tint = TeamSeatTitleDark,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text(
                                            text = if (canViewMemberNames) "قائد التيم: ${team.leaderName}" else "قائد التيم: محجوز",
                                            fontFamily = TajawalFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            color = TeamSeatTitleDark,
                                            fontSize = 13.5.sp,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }

                                    Text(
                                        text = team.description,
                                        fontFamily = TajawalFontFamily,
                                        fontWeight = FontWeight.Normal,
                                        color = TeamSeatBodyMuted,
                                        fontSize = 12.sp,
                                        lineHeight = 18.sp
                                    )
                                }
                            }

                            // شريط اكتمال المقاعد
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "اكتمال المقاعد (${(rawProgress * 100).toInt()}%)",
                                        fontFamily = TajawalFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        color = TeamSeatTitleDark,
                                        fontSize = 13.sp
                                    )
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.Groups,
                                            contentDescription = null,
                                            tint = TeamSeatPrimaryTeal,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text(
                                            text = "\u200E$bookedCount / $totalSeats\u200E مقاعد",
                                            fontFamily = TajawalFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            color = TeamSeatPrimaryTeal,
                                            fontSize = 12.5.sp,
                                            maxLines = 1,
                                            softWrap = false
                                        )
                                    }
                                }
                                LinearProgressIndicator(
                                    progress = { animatedProgress },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(8.dp)
                                        .clip(RoundedCornerShape(50)),
                                    color = TeamSeatPrimaryTeal,
                                    trackColor = Color(0xFFE0EEEE),
                                    strokeCap = StrokeCap.Round
                                )
                            }
                        }
                    }
                }
            }

            // 2. شريط الإشراف وحالة المقاعد المتاحة
            item {
                StaggeredAnimatedItem(index = 1) {
                    TeamSeatCard(isWarmHighlight = true, contentPadding = 12.dp) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Groups,
                                contentDescription = null,
                                tint = TeamSeatWarmOrange,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${team.supervisorDoctor} • ${team.teachingAssistant}",
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.Medium,
                                color = TeamSeatTitleDark,
                                fontSize = 12.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            VerticalDivider(
                                modifier = Modifier.fillMaxHeight(),
                                thickness = 1.dp,
                                color = TeamSeatCardBorderWarm
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                color = Color(0xFFFFECD9),
                                shape = RoundedCornerShape(50),
                                border = BorderStroke(1.dp, TeamSeatCardBorderWarm)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Schedule,
                                        contentDescription = null,
                                        tint = TeamSeatWarmOrange,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Text(
                                        text = "متاح $availableCount • معلق $pendingCount",
                                        fontFamily = TajawalFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        color = TeamSeatWarmOrange,
                                        fontSize = 11.sp,
                                        maxLines = 1,
                                        softWrap = false
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 3. عنوان توزيع مقاعد الفريق
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Groups,
                        contentDescription = null,
                        tint = TeamSeatPrimaryTeal,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "توزيع مقاعد الفريق",
                        fontFamily = TajawalFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = TeamSeatTitleDark,
                        fontSize = 16.sp
                    )
                }
            }

            // 4. كروت المقاعد
            itemsIndexed(seats, key = { _, seat -> seat.id }) { index, seat ->
                StaggeredAnimatedItem(index = index + 2) {
                    TeamOverviewSeatCard(
                        seat = seat,
                        canViewMemberNames = canViewMemberNames,
                        onClick = onOpenSeatMapClick
                    )
                }
            }
        }
    }
}

@Composable
private fun TeamOverviewSeatCard(
    seat: Seat,
    canViewMemberNames: Boolean = false,
    onClick: () -> Unit
) {
    val isBooked = seat.status == SeatStatus.BOOKED
    val isPending = seat.status == SeatStatus.PENDING
    val isAvailable = seat.status == SeatStatus.AVAILABLE

    TeamSeatCard(
        isWarmHighlight = isPending,
        contentPadding = 12.dp,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // دائرة رقم المقعد S1, S2...
            Box(modifier = Modifier.size(44.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(
                            when {
                                isBooked -> TeamSeatPrimaryTeal
                                isPending -> Color(0xFFFFF0E0)
                                else -> TeamSeatSoftTealPill
                            }
                        )
                        .border(
                            width = 1.dp,
                            color = when {
                                isBooked -> TeamSeatPrimaryTeal
                                isPending -> TeamSeatCardBorderWarm
                                else -> TeamSeatCardBorderTeal
                            },
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = seat.seatLabel,
                        fontFamily = TajawalFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = when {
                            isBooked -> Color.White
                            isPending -> TeamSeatWarmOrange
                            else -> TeamSeatPrimaryTeal
                        },
                        fontSize = 13.5.sp,
                        maxLines = 1,
                        softWrap = false
                    )
                }
                if (isBooked) {
                    Box(
                        modifier = Modifier
                            .size(11.dp)
                            .align(Alignment.BottomStart)
                            .clip(CircleShape)
                            .background(Color(0xFF22C55E))
                            .border(1.5.dp, Color.White, CircleShape)
                    )
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = when {
                        isAvailable -> "مكان فاضي مستنيك"
                        !canViewMemberNames && isPending -> "مقعد قيد المراجعة"
                        !canViewMemberNames -> "مقعد محجوز"
                        !seat.applicantName.isNullOrBlank() -> seat.applicantName
                        else -> "عضو معتمد"
                    },
                    fontFamily = TajawalFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = TeamSeatTitleDark,
                    fontSize = 14.5.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = seat.roleRequired,
                    style = TextStyle(
                        fontFamily = TajawalFontFamily,
                        fontWeight = FontWeight.Medium,
                        color = TeamSeatBodyMuted,
                        fontSize = 12.sp,
                        textDirection = TextDirection.Ltr
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // شارة الحالة على اليسار
            when {
                seat.isLeader -> {
                    Surface(
                        color = Color(0xFFFFF0E0),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, TeamSeatCardBorderWarm)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.EmojiEvents,
                                contentDescription = null,
                                tint = TeamSeatWarmOrange,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "قائد التيم",
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = TeamSeatWarmOrange,
                                fontSize = 11.5.sp,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }
                }
                isBooked -> {
                    Surface(
                        color = Color.White,
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, TeamSeatCardBorderTeal)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.CheckCircle,
                                contentDescription = null,
                                tint = TeamSeatPrimaryTeal,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "محجوز",
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = TeamSeatPrimaryTeal,
                                fontSize = 11.5.sp,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }
                }
                isPending -> {
                    Surface(
                        color = Color(0xFFFFF0E0),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, TeamSeatCardBorderWarm)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.HourglassTop,
                                contentDescription = null,
                                tint = TeamSeatWarmOrange,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "قيد المراجعة",
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = TeamSeatWarmOrange,
                                fontSize = 11.5.sp,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }
                }
                else -> {
                    Surface(
                        color = Color.White,
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, TeamSeatCardBorderTeal)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.PersonAddAlt1,
                                contentDescription = null,
                                tint = TeamSeatPrimaryTeal,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "متاح للحجز",
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = TeamSeatPrimaryTeal,
                                fontSize = 11.5.sp,
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TeamDetailsOverviewScreenPreview() {
    TeamSeatTheme {
        TeamDetailsOverviewScreen()
    }
}
