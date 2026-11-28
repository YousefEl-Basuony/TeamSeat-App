package com.stackmates.teamseat.Screens.workspace_alaa

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.HourglassTop
import androidx.compose.material.icons.rounded.PersonSearch
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stackmates.teamseat.core.data.MockDataProvider
import com.stackmates.teamseat.core.model.Seat
import com.stackmates.teamseat.core.model.SeatStatus
import com.stackmates.teamseat.core.theme.TajawalFontFamily
import com.stackmates.teamseat.core.theme.TeamSeatTheme
import com.stackmates.teamseat.ui.components.StaggeredAnimatedItem
import com.stackmates.teamseat.ui.components.TeamSeatBodyMuted
import com.stackmates.teamseat.ui.components.TeamSeatCard
import com.stackmates.teamseat.ui.components.TeamSeatCardBorderWarm
import com.stackmates.teamseat.ui.components.TeamSeatDangerRed
import com.stackmates.teamseat.ui.components.TeamSeatPrimaryTeal
import com.stackmates.teamseat.ui.components.TeamSeatScreenContainer
import com.stackmates.teamseat.ui.components.TeamSeatSoftTealPill
import com.stackmates.teamseat.ui.components.TeamSeatTitleDark
import com.stackmates.teamseat.ui.components.TeamSeatWarmOrange
import com.stackmates.teamseat.ui.components.bounceClick

private enum class LocalDecision { PENDING, ACCEPTED, REJECTED }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewApplicationsScreen(
    pendingSeats: List<Seat> = MockDataProvider.sampleSeats.filter { it.status == SeatStatus.PENDING },
    onViewProfileClick: (applicantId: String) -> Unit = {},
    onAcceptApplicantClick: (Seat) -> Unit = {},
    onRejectApplicantClick: (Seat) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val decisions = remember { mutableStateMapOf<String, LocalDecision>() }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        TeamSeatScreenContainer(
            topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent
                    ),
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                contentDescription = "رجوع",
                                tint = TeamSeatTitleDark
                            )
                        }
                    },
                    title = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "مراجعة طلبات الانضمام",
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = TeamSeatTitleDark,
                                maxLines = 1,
                                softWrap = false
                            )
                            Text(
                                text = "${pendingSeats.size} طلبات بانتظار قرار القائد",
                                fontFamily = TajawalFontFamily,
                                fontSize = 12.sp,
                                color = TeamSeatPrimaryTeal
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            if (pendingSeats.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    TeamSeatCard(contentPadding = 24.dp) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.PersonSearch,
                                contentDescription = null,
                                tint = TeamSeatPrimaryTeal,
                                modifier = Modifier.size(44.dp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "مفيش طلبات معلقة حالياً",
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = TeamSeatTitleDark
                            )
                            Text(
                                text = "أول ما حد يقدم على مقعد متاح هيظهر هنا فوراً.",
                                fontFamily = TajawalFontFamily,
                                fontSize = 13.sp,
                                color = TeamSeatBodyMuted
                            )
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(top = 8.dp, bottom = 28.dp)
                ) {
                    itemsIndexed(pendingSeats, key = { _, seat -> seat.id }) { index, seat ->
                        val decision = decisions[seat.id] ?: LocalDecision.PENDING
                        StaggeredAnimatedItem(index = index) {
                            ApplicantReviewCard(
                                seat = seat,
                                decision = decision,
                                onViewProfile = {
                                    seat.applicantId?.let { onViewProfileClick(it) }
                                },
                                onAccept = {
                                    decisions[seat.id] = LocalDecision.ACCEPTED
                                    onAcceptApplicantClick(seat)
                                },
                                onReject = {
                                    decisions[seat.id] = LocalDecision.REJECTED
                                    onRejectApplicantClick(seat)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ApplicantReviewCard(
    seat: Seat,
    decision: LocalDecision,
    onViewProfile: () -> Unit,
    onAccept: () -> Unit,
    onReject: () -> Unit
) {
    val applicantName = seat.applicantName ?: "طالب متقدم"
    TeamSeatCard(
        isWarmHighlight = decision == LocalDecision.PENDING,
        contentPadding = 16.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFF0E0))
                    .border(1.dp, TeamSeatCardBorderWarm, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = applicantName.take(1),
                    fontFamily = TajawalFontFamily,
                    color = TeamSeatWarmOrange,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = applicantName,
                    fontFamily = TajawalFontFamily,
                    color = TeamSeatTitleDark,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${seat.roleRequired} • مقعد ${seat.seatLabel}",
                    fontFamily = TajawalFontFamily,
                    color = TeamSeatPrimaryTeal,
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Surface(
                color = when (decision) {
                    LocalDecision.PENDING -> Color(0xFFFFF0E0)
                    LocalDecision.ACCEPTED -> TeamSeatSoftTealPill
                    LocalDecision.REJECTED -> Color(0xFFFDECEC)
                },
                shape = RoundedCornerShape(50)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = when (decision) {
                            LocalDecision.PENDING -> Icons.Rounded.HourglassTop
                            LocalDecision.ACCEPTED -> Icons.Rounded.Check
                            LocalDecision.REJECTED -> Icons.Rounded.Close
                        },
                        contentDescription = null,
                        tint = when (decision) {
                            LocalDecision.PENDING -> TeamSeatWarmOrange
                            LocalDecision.ACCEPTED -> TeamSeatPrimaryTeal
                            LocalDecision.REJECTED -> TeamSeatDangerRed
                        },
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text = when (decision) {
                            LocalDecision.PENDING -> "قيد المراجعة"
                            LocalDecision.ACCEPTED -> "تم القبول"
                            LocalDecision.REJECTED -> "تم الرفض"
                        },
                        fontFamily = TajawalFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = when (decision) {
                            LocalDecision.PENDING -> TeamSeatWarmOrange
                            LocalDecision.ACCEPTED -> TeamSeatPrimaryTeal
                            LocalDecision.REJECTED -> TeamSeatDangerRed
                        },
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }
        }

        if (!seat.applicantNote.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(TeamSeatSoftTealPill.copy(alpha = 0.45f))
                    .padding(12.dp)
            ) {
                Text(
                    text = seat.applicantNote,
                    fontFamily = TajawalFontFamily,
                    color = TeamSeatTitleDark,
                    fontSize = 13.5.sp,
                    lineHeight = 20.sp
                )
            }
        }

        if (!seat.applicantId.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedButton(
                onClick = onViewProfile,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.PersonSearch,
                    contentDescription = null,
                    tint = TeamSeatPrimaryTeal,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "عرض بروفايل الطالب",
                    fontFamily = TajawalFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = TeamSeatPrimaryTeal,
                    fontSize = 12.5.sp
                )
            }
        }

        if (decision == LocalDecision.PENDING) {
            Spacer(modifier = Modifier.height(14.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onAccept,
                    colors = ButtonDefaults.buttonColors(containerColor = TeamSeatPrimaryTeal),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .bounceClick(onClick = onAccept)
                ) {
                    Icon(Icons.Rounded.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "قبول",
                        fontFamily = TajawalFontFamily,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        softWrap = false
                    )
                }
                OutlinedButton(
                    onClick = onReject,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TeamSeatDangerRed),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .bounceClick(onClick = onReject)
                ) {
                    Icon(Icons.Rounded.Close, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "رفض",
                        fontFamily = TajawalFontFamily,
                        fontWeight = FontWeight.Bold,
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
fun ReviewApplicationsScreenPreview() {
    TeamSeatTheme {
        ReviewApplicationsScreen()
    }
}
