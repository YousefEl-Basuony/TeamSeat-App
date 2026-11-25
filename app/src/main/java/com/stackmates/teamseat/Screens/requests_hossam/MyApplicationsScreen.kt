package com.stackmates.teamseat.Screens.requests_hossam

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stackmates.teamseat.core.data.MockDataProvider
import com.stackmates.teamseat.core.model.Seat
import com.stackmates.teamseat.core.model.SeatStatus
import com.stackmates.teamseat.core.theme.TajawalFontFamily
import com.stackmates.teamseat.core.theme.TeamSeatTheme
import com.stackmates.teamseat.ui.components.*
import kotlinx.coroutines.launch

/**
 * ⚡ المسؤول عن الشاشة: حسام (Hossam) — [HOSSAM-3]
 * شاشة متابعة طلباتي المعلقة (MyApplicationsScreen)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApplicationsScreen(
    myAppliedSeats: List<Seat> = MockDataProvider.sampleSeats.take(3),
    onCancelApplicationClick: (seatId: String) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val appliedSeats = remember(myAppliedSeats) { mutableStateListOf(*myAppliedSeats.toTypedArray()) }
    var seatToCancel by remember { mutableStateOf<Seat?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val maxRequests = 3
    val currentCount = appliedSeats.size.coerceAtMost(maxRequests)
    val targetProgress = currentCount.toFloat() / maxRequests
    val animatedProgress by rememberAnimatedProgress(targetProgress = targetProgress, durationMillis = 800)

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
                            text = "طلباتي المعلقة",
                            fontFamily = TajawalFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = TeamSeatTitleDark,
                            fontSize = 18.sp,
                            maxLines = 1,
                            softWrap = false
                        )
                        Text(
                            text = "تابع حالة طلباتك ومهلة الـ 48 ساعة",
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
            // 1. بطاقة عداد الطلبات (حد أقصى 3)
            item {
                TeamSeatCard(contentPadding = 16.dp) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "الحد المسموح للطلبات",
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
                                    text = "\u200E$currentCount / $maxRequests\u200E طلبات",
                                    fontFamily = TajawalFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    color = TeamSeatPrimaryTeal,
                                    fontSize = 12.sp,
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
                                .clip(RoundedCornerShape(4.dp)),
                            color = TeamSeatPrimaryTeal,
                            trackColor = Color.White
                        )

                        Text(
                            text = "تقدر تبعت لحد 3 طلبات في نفس الوقت، ولو قائد قبل طلبك الباقي بيتلغي تلقائي.",
                            fontFamily = TajawalFontFamily,
                            fontWeight = FontWeight.Normal,
                            color = TeamSeatBodyMuted,
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            // 2. قائمة الطلبات أو رسالة فارغة
            if (appliedSeats.isEmpty()) {
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
                                    imageVector = Icons.Rounded.AssignmentTurnedIn,
                                    contentDescription = null,
                                    tint = TeamSeatPrimaryTeal,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                            Text(
                                text = "معندكش طلبات معلقة دلوقتي",
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = TeamSeatTitleDark,
                                fontSize = 15.sp,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "لفّ على المشاريع المتاحة واحجز مكانك في التيم اللي يناسبك.",
                                fontFamily = TajawalFontFamily,
                                color = TeamSeatBodyMuted,
                                fontSize = 12.5.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                itemsIndexed(appliedSeats, key = { _, seat -> seat.id }) { index, seat ->
                    val isPending = seat.status == SeatStatus.PENDING
                    val isBooked = seat.status == SeatStatus.BOOKED
                    val accent = if (isPending) TeamSeatWarmOrange else TeamSeatPrimaryTeal
                    val border = if (isPending) TeamSeatCardBorderWarm else TeamSeatCardBorderTeal

                    StaggeredAnimatedItem(index = index) {
                        TeamSeatCard(
                            isWarmHighlight = isPending,
                            contentPadding = 14.dp
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(40.dp)
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(accent),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "S${seat.seatNumber}",
                                                fontFamily = TajawalFontFamily,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White,
                                                fontSize = 13.sp,
                                                maxLines = 1,
                                                softWrap = false
                                            )
                                        }

                                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                            Text(
                                                text = seat.roleRequired,
                                                style = TextStyle(
                                                    fontFamily = TajawalFontFamily,
                                                    fontWeight = FontWeight.Bold,
                                                    color = TeamSeatTitleDark,
                                                    fontSize = 14.5.sp,
                                                    textDirection = TextDirection.Ltr
                                                ),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Text(
                                                text = "كود التيم: ${seat.teamId}",
                                                fontFamily = TajawalFontFamily,
                                                color = TeamSeatBodyMuted,
                                                fontSize = 12.sp,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color.White)
                                            .border(1.dp, border, RoundedCornerShape(8.dp))
                                            .padding(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = when {
                                                isBooked -> "مقبول ✓"
                                                isPending -> "قيد المراجعة"
                                                else -> "مفتوح"
                                            },
                                            fontFamily = TajawalFontFamily,
                                            fontWeight = FontWeight.SemiBold,
                                            color = accent,
                                            fontSize = 11.5.sp,
                                            maxLines = 1,
                                            softWrap = false
                                        )
                                    }
                                }

                                if (!seat.applicantNote.isNullOrBlank()) {
                                    Text(
                                        text = "رسالتك: \"${seat.applicantNote}\"",
                                        fontFamily = TajawalFontFamily,
                                        color = TeamSeatBodyMuted,
                                        fontSize = 12.5.sp,
                                        lineHeight = 18.sp
                                    )
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.Timer,
                                            contentDescription = null,
                                            tint = accent,
                                            modifier = Modifier.size(15.dp)
                                        )
                                        Text(
                                            text = if (isPending) "باقي 36 ساعة" else "تم الرد",
                                            fontFamily = TajawalFontFamily,
                                            fontWeight = FontWeight.SemiBold,
                                            color = accent,
                                            fontSize = 12.sp
                                        )
                                    }

                                    if (!isBooked) {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(Color(0xFFFFF1F2))
                                                .border(1.dp, TeamSeatDangerRed.copy(alpha = 0.35f), RoundedCornerShape(8.dp))
                                                .bounceClick { seatToCancel = seat }
                                                .padding(horizontal = 10.dp, vertical = 5.dp)
                                        ) {
                                            Text(
                                                text = "اسحب الطلب",
                                                fontFamily = TajawalFontFamily,
                                                fontWeight = FontWeight.SemiBold,
                                                color = TeamSeatDangerRed,
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
                }
            }
        }
    }

    if (seatToCancel != null) {
        val target = seatToCancel!!
        ModalBottomSheet(
            onDismissRequest = { seatToCancel = null },
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
                    text = "اسحب طلب الحجز؟",
                    fontFamily = TajawalFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = TeamSeatTitleDark,
                    fontSize = 16.sp
                )
                Text(
                    text = "متأكد إنك عايز تسحب طلبك على مقعد ${target.roleRequired}؟",
                    fontFamily = TajawalFontFamily,
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
                                appliedSeats.removeAll { it.id == target.id }
                                onCancelApplicationClick(target.id)
                                seatToCancel = null
                                scope.launch {
                                    snackbarHostState.showSnackbar("تم سحب الطلب بنجاح")
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "اسحب الطلب",
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
                            .background(Color.White)
                            .border(1.2.dp, TeamSeatCardBorderTeal, RoundedCornerShape(12.dp))
                            .bounceClick { seatToCancel = null },
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun MyApplicationsScreenPreview() {
    TeamSeatTheme {
        MyApplicationsScreen()
    }
}
