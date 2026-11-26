package com.stackmates.teamseat.Screens.workspace_alaa

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.FolderOpen
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.OpenInNew
import androidx.compose.material.icons.rounded.School
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
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
import com.stackmates.teamseat.core.model.Team
import com.stackmates.teamseat.core.theme.TajawalFontFamily
import com.stackmates.teamseat.core.theme.TeamSeatTheme
import com.stackmates.teamseat.ui.components.StaggeredAnimatedItem
import com.stackmates.teamseat.ui.components.TeamSeatBodyMuted
import com.stackmates.teamseat.ui.components.TeamSeatCard
import com.stackmates.teamseat.ui.components.TeamSeatCardBorderTeal
import com.stackmates.teamseat.ui.components.TeamSeatPrimaryTeal
import com.stackmates.teamseat.ui.components.TeamSeatScreenContainer
import com.stackmates.teamseat.ui.components.TeamSeatSoftTealPill
import com.stackmates.teamseat.ui.components.TeamSeatTitleDark
import com.stackmates.teamseat.ui.components.TeamSeatWarmOrange
import com.stackmates.teamseat.ui.components.bounceClick
import com.stackmates.teamseat.ui.components.rememberAnimatedProgress

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamWorkspaceScreen(
    team: Team = MockDataProvider.sampleTeam,
    seats: List<Seat> = MockDataProvider.sampleSeats,
    onOpenCalendarClick: () -> Unit = {},
    onOpenTASheetClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val bookedSeats = seats.filter { it.status == SeatStatus.BOOKED }
    val rawProgress = if (team.totalSeats > 0) bookedSeats.size.toFloat() / team.totalSeats else 0f
    val animatedProgress by rememberAnimatedProgress(targetProgress = rawProgress)

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
                                text = "مساحة عمل التيم",
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 19.sp,
                                color = TeamSeatTitleDark,
                                maxLines = 1,
                                softWrap = false
                            )
                            Text(
                                text = team.projectName,
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 12.sp,
                                color = TeamSeatPrimaryTeal,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(top = 6.dp, bottom = 28.dp)
            ) {
                // 1. كارت نسبة اكتمال التيم
                item {
                    StaggeredAnimatedItem(index = 0) {
                        TeamSeatCard(contentPadding = 16.dp) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "جاهزية الفريق",
                                        fontFamily = TajawalFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = TeamSeatTitleDark
                                    )
                                    Text(
                                        text = "${bookedSeats.size} من ${team.totalSeats} مقاعد محجوزة رسمياً",
                                        fontFamily = TajawalFontFamily,
                                        fontSize = 12.5.sp,
                                        color = TeamSeatBodyMuted
                                    )
                                }
                                Surface(
                                    color = TeamSeatSoftTealPill,
                                    shape = RoundedCornerShape(50)
                                ) {
                                    Text(
                                        text = "${(rawProgress * 100).toInt()}%",
                                        fontFamily = TajawalFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = TeamSeatPrimaryTeal,
                                        maxLines = 1,
                                        softWrap = false,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            LinearProgressIndicator(
                                progress = { animatedProgress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(9.dp)
                                    .clip(RoundedCornerShape(50)),
                                color = TeamSeatPrimaryTeal,
                                trackColor = Color(0xFFE0EEEE),
                                strokeCap = StrokeCap.Round
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.School,
                                    contentDescription = null,
                                    tint = TeamSeatPrimaryTeal,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "إشراف: ${team.supervisorDoctor} • ${team.teachingAssistant}",
                                    fontFamily = TajawalFontFamily,
                                    fontSize = 12.sp,
                                    color = TeamSeatBodyMuted,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }

                // 2. أزرار التنقل السريعة (التقويم + كشف المعيد)
                item {
                    StaggeredAnimatedItem(index = 1) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            TeamSeatCard(
                                modifier = Modifier.weight(1f),
                                contentPadding = 14.dp,
                                onClick = onOpenCalendarClick
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(38.dp)
                                            .clip(CircleShape)
                                            .background(TeamSeatSoftTealPill),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.CalendarMonth,
                                            contentDescription = null,
                                            tint = TeamSeatPrimaryTeal,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                    Icon(
                                        imageVector = Icons.Rounded.ChevronLeft,
                                        contentDescription = null,
                                        tint = TeamSeatPrimaryTeal
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "تقويم التيم",
                                    fontFamily = TajawalFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = TeamSeatTitleDark,
                                    maxLines = 1,
                                    softWrap = false
                                )
                                Text(
                                    text = "المواعيد والمناقشات",
                                    fontFamily = TajawalFontFamily,
                                    fontSize = 11.5.sp,
                                    color = TeamSeatBodyMuted,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            TeamSeatCard(
                                modifier = Modifier.weight(1f),
                                isWarmHighlight = true,
                                contentPadding = 14.dp,
                                onClick = onOpenTASheetClick
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(38.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFFFF0E0)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.Description,
                                            contentDescription = null,
                                            tint = TeamSeatWarmOrange,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                    Icon(
                                        imageVector = Icons.Rounded.ChevronLeft,
                                        contentDescription = null,
                                        tint = TeamSeatWarmOrange
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "كشف المعيد",
                                    fontFamily = TajawalFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = TeamSeatTitleDark,
                                    maxLines = 1,
                                    softWrap = false
                                )
                                Text(
                                    text = "تصدير القائمة الرسمية",
                                    fontFamily = TajawalFontFamily,
                                    fontSize = 11.5.sp,
                                    color = TeamSeatBodyMuted,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }

                // 3. روابط المشروع (GitHub & Drive)
                item {
                    StaggeredAnimatedItem(index = 2) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text(
                                text = "روابط المشروع",
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = TeamSeatTitleDark
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                WorkspaceLinkCard(
                                    title = "GitHub Repo",
                                    subtitle = "الكود المصدري",
                                    icon = Icons.Rounded.Code,
                                    modifier = Modifier.weight(1f),
                                    onClick = {
                                        runCatching {
                                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(team.githubRepo)))
                                        }
                                    }
                                )
                                WorkspaceLinkCard(
                                    title = "Google Drive",
                                    subtitle = "الملفات والوثائق",
                                    icon = Icons.Rounded.FolderOpen,
                                    modifier = Modifier.weight(1f),
                                    onClick = {
                                        runCatching {
                                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(team.driveFolder)))
                                        }
                                    }
                                )
                            }
                        }
                    }
                }

                // 4. قائمة الأعضاء المعتمدين
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Groups,
                                contentDescription = null,
                                tint = TeamSeatPrimaryTeal,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "أعضاء الفريق (${bookedSeats.size})",
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = TeamSeatTitleDark
                            )
                        }
                    }
                }

                itemsIndexed(bookedSeats, key = { _, seat -> seat.id }) { index, seat ->
                    StaggeredAnimatedItem(index = index + 3) {
                        TeamSeatCard(contentPadding = 14.dp) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(CircleShape)
                                        .background(TeamSeatSoftTealPill)
                                        .border(1.dp, TeamSeatCardBorderTeal, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = seat.seatLabel,
                                        fontFamily = TajawalFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = TeamSeatPrimaryTeal
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(
                                            text = seat.applicantName ?: "عضو معتمد",
                                            fontFamily = TajawalFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp,
                                            color = TeamSeatTitleDark,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        if (seat.isLeader) {
                                            Surface(
                                                color = Color(0xFFFFF0E0),
                                                shape = RoundedCornerShape(50)
                                            ) {
                                                Text(
                                                    text = "القائد",
                                                    fontFamily = TajawalFontFamily,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 10.5.sp,
                                                    color = TeamSeatWarmOrange,
                                                    maxLines = 1,
                                                    softWrap = false,
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                    }
                                    Text(
                                        text = seat.roleRequired,
                                        fontFamily = TajawalFontFamily,
                                        fontSize = 12.5.sp,
                                        color = TeamSeatBodyMuted
                                    )
                                }
                                Icon(
                                    imageVector = Icons.Rounded.CheckCircle,
                                    contentDescription = "معتمد",
                                    tint = TeamSeatPrimaryTeal,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WorkspaceLinkCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    TeamSeatCard(
        modifier = modifier.bounceClick(onClick = onClick),
        contentPadding = 12.dp,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(TeamSeatSoftTealPill),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = TeamSeatPrimaryTeal,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontFamily = TajawalFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = TeamSeatTitleDark,
                    maxLines = 1,
                    softWrap = false
                )
                Text(
                    text = subtitle,
                    fontFamily = TajawalFontFamily,
                    fontSize = 11.sp,
                    color = TeamSeatBodyMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Icon(
                imageVector = Icons.Rounded.OpenInNew,
                contentDescription = null,
                tint = TeamSeatPrimaryTeal,
                modifier = Modifier.size(15.dp)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TeamWorkspaceScreenPreview() {
    TeamSeatTheme {
        TeamWorkspaceScreen()
    }
}
