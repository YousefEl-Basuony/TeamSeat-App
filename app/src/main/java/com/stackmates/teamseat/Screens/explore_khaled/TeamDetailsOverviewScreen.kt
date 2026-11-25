package com.stackmates.teamseat.Screens.explore_khaled

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.EventSeat
import androidx.compose.material.icons.rounded.HourglassTop
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PersonAddAlt1
import androidx.compose.material.icons.rounded.SupervisorAccount
import androidx.compose.material.icons.rounded.WorkspacePremium
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.stackmates.teamseat.core.model.Seat
import com.stackmates.teamseat.core.model.SeatStatus
import com.stackmates.teamseat.core.model.Team
import com.stackmates.teamseat.core.theme.TajawalFontFamily
import com.stackmates.teamseat.core.theme.TeamSeatTheme
import com.stackmates.teamseat.ui.components.*

/**
 * 🔍 المسؤول عن الشاشة: خالد (Khaled) — [KHALED-4]
 * شاشة تفاصيل المشروع قبل فتح خريطة الكراسي (TeamDetailsOverviewScreen)
 * تستخدم TeamSeatScreenContainer (بالموجتين العضويتين) و TeamSeatCard.
 */
@OptIn(ExperimentalMaterial3Api::class)
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

    val targetProgress = if (totalSeats > 0) bookedCount.toFloat() / totalSeats else 0f
    val animatedProgress by rememberAnimatedProgress(targetProgress = targetProgress, durationMillis = 800)
    val animatedPercent = (animatedProgress * 100).toInt()

    val isArabicFirst = team.projectName.firstOrNull()?.let {
        Character.getDirectionality(it) == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC ||
            Character.getDirectionality(it) == Character.DIRECTIONALITY_RIGHT_TO_LEFT
    } == true

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
                            text = "تفاصيل المشروع",
                            fontFamily = TajawalFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = TeamSeatTitleDark,
                            fontSize = 18.sp,
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
            Surface(
                color = Color.White,
                shadowElevation = 0.dp,
                border = BorderStroke(1.dp, TeamSeatDividerColor)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (team.isLocked) Color(0xFF94A3B8) else TeamSeatPrimaryTeal)
                            .bounceClick(enabled = !team.isLocked, onClick = onOpenSeatMapClick),
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
                                modifier = Modifier.size(19.dp)
                            )
                            Text(
                                text = if (team.isLocked) "التيم مقفول ومكتمل" else "افتح خريطة المقاعد واحجز مكانك ←",
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 14.sp,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
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
                bottom = safePadding.calculateBottomPadding()
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 1. بطاقة المشروع الرئيسية + الرسم التوضيحي
            item {
                TeamSeatCard(contentPadding = 16.dp) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            ProjectCategoryIllustration(category = "${team.projectName} ${team.category}")

                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = team.projectName,
                                    style = TextStyle(
                                        fontFamily = TajawalFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        color = TeamSeatTitleDark,
                                        fontSize = 17.sp,
                                        textDirection = if (isArabicFirst) TextDirection.Rtl else TextDirection.Ltr
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Person,
                                        contentDescription = null,
                                        tint = TeamSeatPrimaryTeal,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Text(
                                        text = "قائد التيم: ${team.leaderName.substringBefore(" (")}",
                                        fontFamily = TajawalFontFamily,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TeamSeatBodyMuted,
                                        fontSize = 12.5.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }

                        Text(
                            text = team.description,
                            fontFamily = TajawalFontFamily,
                            fontWeight = FontWeight.Normal,
                            color = TeamSeatBodyMuted,
                            fontSize = 13.sp,
                            lineHeight = 20.sp
                        )

                        HorizontalDivider(color = TeamSeatDividerColor, thickness = 0.8.dp)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "اكتمال المقاعد ($animatedPercent%)",
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = TeamSeatTitleDark,
                                fontSize = 12.5.sp
                            )
                            Text(
                                text = "\u200E$bookedCount / $totalSeats\u200E مقاعد",
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = TeamSeatPrimaryTeal,
                                fontSize = 12.5.sp
                            )
                        }

                        LinearProgressIndicator(
                            progress = { animatedProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = TeamSeatPrimaryTeal,
                            trackColor = Color.White
                        )
                    }
                }
            }

            // 2. بطاقة الإشراف الأكاديمي وملخص الحالات
            item {
                TeamSeatCard(isWarmHighlight = true, contentPadding = 14.dp) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.SupervisorAccount,
                                contentDescription = null,
                                tint = TeamSeatWarmOrange,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "${team.supervisorDoctor} • ${team.teachingAssistant}",
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                color = TeamSeatTitleDark,
                                fontSize = 12.5.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White)
                                .border(1.dp, TeamSeatCardBorderWarm, RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
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

            // 3. قائمة المقاعد والتخصصات في التيم
            item {
                Text(
                    text = "توزيع مقاعد الفريق",
                    fontFamily = TajawalFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = TeamSeatTitleDark,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            itemsIndexed(seats, key = { _, seat -> seat.id }) { index, seat ->
                val isWarm = seat.status == SeatStatus.PENDING || index == 0
                val badgeText = when {
                    index == 0 -> "قائد التيم"
                    seat.status == SeatStatus.BOOKED -> "محجوز"
                    seat.status == SeatStatus.PENDING -> "قيد المراجعة"
                    else -> "متاح للحجز"
                }
                val accent = if (isWarm) TeamSeatWarmOrange else TeamSeatPrimaryTeal
                val border = if (isWarm) TeamSeatCardBorderWarm else TeamSeatCardBorderTeal

                StaggeredAnimatedItem(index = index) {
                    TeamSeatCard(isWarmHighlight = isWarm, contentPadding = 12.dp) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (seat.status == SeatStatus.AVAILABLE) Color.White else accent)
                                    .then(
                                        if (seat.status == SeatStatus.AVAILABLE) {
                                            Modifier.border(1.dp, border, RoundedCornerShape(10.dp))
                                        } else Modifier
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "S${seat.seatNumber}",
                                    fontFamily = TajawalFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    color = if (seat.status == SeatStatus.AVAILABLE) TeamSeatTitleDark else Color.White,
                                    fontSize = 12.5.sp,
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }

                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Text(
                                    text = seat.applicantName ?: "مكان فاضي مستنيك",
                                    fontFamily = TajawalFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    color = if (seat.applicantName != null) TeamSeatTitleDark else TeamSeatBodyMuted,
                                    fontSize = 14.sp,
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

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.White)
                                    .border(1.dp, border, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = when {
                                            index == 0 -> Icons.Rounded.WorkspacePremium
                                            seat.status == SeatStatus.BOOKED -> Icons.Rounded.CheckCircle
                                            seat.status == SeatStatus.PENDING -> Icons.Rounded.HourglassTop
                                            else -> Icons.Rounded.PersonAddAlt1
                                        },
                                        contentDescription = null,
                                        tint = accent,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Text(
                                        text = badgeText,
                                        fontFamily = TajawalFontFamily,
                                        fontWeight = FontWeight.SemiBold,
                                        color = accent,
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
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TeamDetailsOverviewScreenPreview() {
    TeamSeatTheme {
        TeamDetailsOverviewScreen()
    }
}
