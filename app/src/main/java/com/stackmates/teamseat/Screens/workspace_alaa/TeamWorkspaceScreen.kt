package com.stackmates.teamseat.Screens.workspace_alaa

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.Chat
import androidx.compose.material.icons.automirrored.rounded.OpenInNew
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.Link
import androidx.compose.material.icons.rounded.PersonRemove
import androidx.compose.material.icons.rounded.School
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.stackmates.teamseat.ui.components.TeamSeatDangerRed
import com.stackmates.teamseat.ui.components.TeamSeatDividerColor
import com.stackmates.teamseat.ui.components.TeamSeatPrimaryTeal
import com.stackmates.teamseat.ui.components.TeamSeatScreenContainer
import com.stackmates.teamseat.ui.components.TeamSeatSoftTealPill
import com.stackmates.teamseat.ui.components.TeamSeatTitleDark
import com.stackmates.teamseat.ui.components.TeamSeatWarmOrange
import com.stackmates.teamseat.ui.components.rememberAnimatedProgress

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamWorkspaceScreen(
    team: Team = MockDataProvider.sampleTeam,
    seats: List<Seat> = MockDataProvider.sampleSeats,
    isLeader: Boolean = false,
    pendingRequestsCount: Int = 0,
    onReviewRequestsClick: () -> Unit = {},
    onRemoveMemberClick: (Seat) -> Unit = {},
    onDeleteTeamClick: () -> Unit = {},
    onOpenCalendarClick: () -> Unit = {},
    onOpenTASheetClick: () -> Unit = {},
    onOpenChatClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val context = LocalContext.current
    var memberToRemove by remember { mutableStateOf<Seat?>(null) }
    var showDeleteTeamDialog by remember { mutableStateOf(false) }
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
                                fontSize = 12.5.sp,
                                color = TeamSeatPrimaryTeal,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    },
                    actions = {
                        if (isLeader) {
                            IconButton(onClick = { showDeleteTeamDialog = true }) {
                                Icon(
                                    imageVector = Icons.Rounded.Delete,
                                    contentDescription = "حذف التيم",
                                    tint = TeamSeatDangerRed
                                )
                            }
                        }
                        IconButton(onClick = onOpenChatClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.Chat,
                                contentDescription = "شات الفريق",
                                tint = TeamSeatPrimaryTeal
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
                // 1. كارت جاهزية الفريق الدائري + الشريط السفلي للإشراف
                item {
                    StaggeredAnimatedItem(index = 0) {
                        TeamSeatCard(contentPadding = 0.dp) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(IntrinsicSize.Min)
                                        .padding(horizontal = 16.dp, vertical = 14.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // الجزء الأيمن: العنوان والشريط الأفقي
                                    Column(
                                        modifier = Modifier.weight(1f),
                                        verticalArrangement = Arrangement.spacedBy(6.dp)
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
                                                text = "جاهزية الفريق",
                                                fontFamily = TajawalFontFamily,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 16.sp,
                                                color = TeamSeatTitleDark
                                            )
                                        }
                                        Text(
                                            text = "${bookedSeats.size} من ${team.totalSeats} مقاعد محجوزة رسمياً",
                                            fontFamily = TajawalFontFamily,
                                            fontSize = 12.5.sp,
                                            color = TeamSeatBodyMuted
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
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

                                    Spacer(modifier = Modifier.width(14.dp))
                                    VerticalDivider(
                                        modifier = Modifier.fillMaxHeight(),
                                        thickness = 1.dp,
                                        color = TeamSeatDividerColor
                                    )
                                    Spacer(modifier = Modifier.width(14.dp))

                                    // الجزء الأيسر: الحلقة الدائرية للنسبة المئوية
                                    CircularReadinessRing(
                                        progress = animatedProgress,
                                        percentText = "${(rawProgress * 100).toInt()}%"
                                    )
                                }

                                HorizontalDivider(thickness = 1.dp, color = TeamSeatDividerColor)

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color(0xFFF6FAFA))
                                        .padding(horizontal = 16.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.School,
                                        contentDescription = null,
                                        tint = TeamSeatPrimaryTeal,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
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
                }

                if (isLeader) {
                    item {
                        TeamSeatCard(
                            isWarmHighlight = pendingRequestsCount > 0,
                            contentPadding = 14.dp,
                            onClick = onReviewRequestsClick
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (pendingRequestsCount > 0) Color(0xFFFFF0E0) else TeamSeatSoftTealPill
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Groups,
                                        contentDescription = null,
                                        tint = if (pendingRequestsCount > 0) TeamSeatWarmOrange else TeamSeatPrimaryTeal,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "طلبات الانضمام للفريق",
                                        fontFamily = TajawalFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.5.sp,
                                        color = TeamSeatTitleDark
                                    )
                                    Text(
                                        text = if (pendingRequestsCount > 0) {
                                            "عندك $pendingRequestsCount طلبات معلقة — اضغط للقبول أو الرفض"
                                        } else {
                                            "راجع طلبات الانضمام الواردة لفريقك"
                                        },
                                        fontFamily = TajawalFontFamily,
                                        fontSize = 12.sp,
                                        color = if (pendingRequestsCount > 0) TeamSeatWarmOrange else TeamSeatBodyMuted
                                    )
                                }
                                Icon(
                                    imageVector = Icons.Rounded.ChevronLeft,
                                    contentDescription = null,
                                    tint = if (pendingRequestsCount > 0) TeamSeatWarmOrange else TeamSeatPrimaryTeal
                                )
                            }
                        }
                    }
                }

                // 2. بطاقتا التقويم وكشف المعيد
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
                                Box(modifier = Modifier.fillMaxWidth()) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .align(Alignment.Center)
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
                                        tint = TeamSeatPrimaryTeal,
                                        modifier = Modifier.align(Alignment.CenterEnd)
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "تقويم التيم",
                                    fontFamily = TajawalFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.5.sp,
                                    color = TeamSeatTitleDark,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth(),
                                    maxLines = 1,
                                    softWrap = false
                                )
                                Text(
                                    text = "المواعيد والمناقشات",
                                    fontFamily = TajawalFontFamily,
                                    fontSize = 11.5.sp,
                                    color = TeamSeatBodyMuted,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth(),
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
                                Box(modifier = Modifier.fillMaxWidth()) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .align(Alignment.Center)
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
                                        tint = TeamSeatWarmOrange,
                                        modifier = Modifier.align(Alignment.CenterEnd)
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "كشف المعيد",
                                    fontFamily = TajawalFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.5.sp,
                                    color = TeamSeatTitleDark,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth(),
                                    maxLines = 1,
                                    softWrap = false
                                )
                                Text(
                                    text = "تصدير القائمة الرسمية",
                                    fontFamily = TajawalFontFamily,
                                    fontSize = 11.5.sp,
                                    color = TeamSeatBodyMuted,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth(),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }

                // 3. روابط المشروع (GitHub + Google Drive بأيقوناتهم المميزة)
                item {
                    StaggeredAnimatedItem(index = 2) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Link,
                                    contentDescription = null,
                                    tint = TeamSeatPrimaryTeal,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = "روابط المشروع",
                                    fontFamily = TajawalFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = TeamSeatTitleDark
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                WorkspaceBrandLinkCard(
                                    title = "GitHub Repo",
                                    subtitle = "الكود المصدري",
                                    modifier = Modifier.weight(1f),
                                    logo = { GitHubLogoIcon(modifier = Modifier.size(28.dp)) },
                                    onClick = {
                                        runCatching {
                                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(team.githubRepo)))
                                        }
                                    }
                                )
                                WorkspaceBrandLinkCard(
                                    title = "Google Drive",
                                    subtitle = "الملفات والوثائق",
                                    modifier = Modifier.weight(1f),
                                    logo = { GoogleDriveLogoIcon(modifier = Modifier.size(28.dp)) },
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

                itemsIndexed(bookedSeats, key = { _, seat -> seat.id }) { index, seat ->
                    StaggeredAnimatedItem(index = index + 3) {
                        TeamSeatCard(contentPadding = 12.dp) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .background(TeamSeatSoftTealPill)
                                        .border(1.dp, TeamSeatCardBorderTeal, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = seat.seatLabel,
                                        fontFamily = TajawalFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.5.sp,
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
                                        fontSize = 12.sp,
                                        color = TeamSeatBodyMuted
                                    )
                                }
                                if (isLeader && !seat.isLeader && seat.applicantId != team.leaderId) {
                                    IconButton(
                                        onClick = { memberToRemove = seat },
                                        modifier = Modifier.size(34.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.PersonRemove,
                                            contentDescription = "إزالة العضو",
                                            tint = TeamSeatDangerRed,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                }
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(TeamSeatPrimaryTeal),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Check,
                                        contentDescription = "معتمد",
                                        tint = Color.White,
                                        modifier = Modifier.size(15.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        val targetMember = memberToRemove
        if (targetMember != null) {
            AlertDialog(
                onDismissRequest = { memberToRemove = null },
                title = {
                    Text(
                        text = "إزالة عضو من الفريق",
                        fontFamily = TajawalFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = TeamSeatTitleDark
                    )
                },
                text = {
                    Text(
                        text = "هل أنت متأكد من إزالة ${targetMember.applicantName ?: "هذا العضو"} من مقعد (${targetMember.roleRequired})؟ سيصبح المقعد متاحاً للحجز من جديد.",
                        fontFamily = TajawalFontFamily,
                        color = TeamSeatBodyMuted
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            onRemoveMemberClick(targetMember)
                            memberToRemove = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = TeamSeatDangerRed)
                    ) {
                        Text(
                            text = "إزالة",
                            fontFamily = TajawalFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                },
                dismissButton = {
                    OutlinedButton(onClick = { memberToRemove = null }) {
                        Text(
                            text = "إلغاء",
                            fontFamily = TajawalFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = TeamSeatTitleDark
                        )
                    }
                }
            )
        }

        if (showDeleteTeamDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteTeamDialog = false },
                title = {
                    Text(
                        text = "حذف التيم نهائياً",
                        fontFamily = TajawalFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = TeamSeatTitleDark
                    )
                },
                text = {
                    Text(
                        text = "هل أنت متأكد من حذف تيم ${team.projectName}؟ سيتم حذف كل المقاعد وإخراج جميع الأعضاء ولا يمكن التراجع عن ذلك.",
                        fontFamily = TajawalFontFamily,
                        color = TeamSeatBodyMuted
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showDeleteTeamDialog = false
                            onDeleteTeamClick()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = TeamSeatDangerRed)
                    ) {
                        Text(
                            text = "حذف التيم",
                            fontFamily = TajawalFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                },
                dismissButton = {
                    OutlinedButton(onClick = { showDeleteTeamDialog = false }) {
                        Text(
                            text = "إلغاء",
                            fontFamily = TajawalFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = TeamSeatTitleDark
                        )
                    }
                }
            )
        }
    }
}

@Composable
private fun CircularReadinessRing(
    progress: Float,
    percentText: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.size(70.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokePx = 6.dp.toPx()
            val inset = strokePx / 2f
            val arcSize = Size(size.width - strokePx, size.height - strokePx)
            drawArc(
                color = Color(0xFFE0EEEE),
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = Offset(inset, inset),
                size = arcSize,
                style = Stroke(width = strokePx, cap = StrokeCap.Round)
            )
            drawArc(
                color = TeamSeatPrimaryTeal,
                startAngle = -90f,
                sweepAngle = 360f * progress.coerceIn(0f, 1f),
                useCenter = false,
                topLeft = Offset(inset, inset),
                size = arcSize,
                style = Stroke(width = strokePx, cap = StrokeCap.Round)
            )
        }
        Text(
            text = percentText,
            fontFamily = TajawalFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = TeamSeatPrimaryTeal,
            maxLines = 1,
            softWrap = false
        )
    }
}

@Composable
private fun WorkspaceBrandLinkCard(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    logo: @Composable () -> Unit,
    onClick: () -> Unit
) {
    TeamSeatCard(
        modifier = modifier,
        contentPadding = 12.dp,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            logo()
            Spacer(modifier = Modifier.width(10.dp))
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
                imageVector = Icons.AutoMirrored.Rounded.OpenInNew,
                contentDescription = null,
                tint = TeamSeatPrimaryTeal,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
private fun GitHubLogoIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        drawCircle(color = Color(0xFF181717))
        // أذنا ورأس Octocat المبسط
        val head = Path().apply {
            moveTo(w * 0.28f, h * 0.60f)
            cubicTo(w * 0.24f, h * 0.42f, w * 0.30f, h * 0.30f, w * 0.34f, h * 0.26f)
            lineTo(w * 0.38f, h * 0.35f)
            lineTo(w * 0.62f, h * 0.35f)
            lineTo(w * 0.66f, h * 0.26f)
            cubicTo(w * 0.70f, h * 0.30f, w * 0.76f, h * 0.42f, w * 0.72f, h * 0.60f)
            cubicTo(w * 0.68f, h * 0.70f, w * 0.60f, h * 0.73f, w * 0.56f, h * 0.75f)
            lineTo(w * 0.56f, h * 0.90f)
            lineTo(w * 0.44f, h * 0.90f)
            lineTo(w * 0.44f, h * 0.75f)
            cubicTo(w * 0.40f, h * 0.73f, w * 0.32f, h * 0.70f, w * 0.28f, h * 0.60f)
            close()
        }
        drawPath(path = head, color = Color.White)
    }
}

@Composable
private fun GoogleDriveLogoIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        // الضلع الأصفر (يمين)
        val yellowRibbon = Path().apply {
            moveTo(w * 0.36f, h * 0.14f)
            lineTo(w * 0.64f, h * 0.14f)
            lineTo(w * 0.90f, h * 0.62f)
            lineTo(w * 0.64f, h * 0.62f)
            close()
        }
        drawPath(path = yellowRibbon, color = Color(0xFFFBBC05))

        // الضلع الأخضر (يسار)
        val greenRibbon = Path().apply {
            moveTo(w * 0.36f, h * 0.14f)
            lineTo(w * 0.50f, h * 0.38f)
            lineTo(w * 0.24f, h * 0.84f)
            lineTo(w * 0.10f, h * 0.62f)
            close()
        }
        drawPath(path = greenRibbon, color = Color(0xFF34A853))

        // الضلع الأزرق (أسفل)
        val blueRibbon = Path().apply {
            moveTo(w * 0.32f, h * 0.62f)
            lineTo(w * 0.90f, h * 0.62f)
            lineTo(w * 0.76f, h * 0.84f)
            lineTo(w * 0.24f, h * 0.84f)
            close()
        }
        drawPath(path = blueRibbon, color = Color(0xFF4285F4))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoTeamWorkspaceScreen(
    onExploreTeamsClick: () -> Unit = {},
    onCreateTeamClick: () -> Unit = {},
    onMyApplicationsClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
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
                        Text(
                            text = "فريقي",
                            fontFamily = TajawalFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 19.sp,
                            color = TeamSeatTitleDark
                        )
                    }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                TeamSeatCard(contentPadding = 24.dp) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(TeamSeatSoftTealPill),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Groups,
                                contentDescription = null,
                                tint = TeamSeatPrimaryTeal,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        Text(
                            text = "أنت لسه مش في أي تيم حالياً",
                            fontFamily = TajawalFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = TeamSeatTitleDark,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "لو بعتت طلب انضمام لليدر، استنى موافقته وتقدر تتابع طلبك من «طلباتي»، أو استكشف التيمات أو أنشئ تيم جديد.",
                            fontFamily = TajawalFontFamily,
                            fontSize = 13.sp,
                            color = TeamSeatBodyMuted,
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Button(
                            onClick = onMyApplicationsClick,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = TeamSeatPrimaryTeal)
                        ) {
                            Text(
                                text = "متابعة طلباتي المعلقة",
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedButton(
                                onClick = onExploreTeamsClick,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Text(
                                    text = "استكشاف",
                                    fontFamily = TajawalFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    color = TeamSeatTitleDark
                                )
                            }
                            OutlinedButton(
                                onClick = onCreateTeamClick,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Text(
                                    text = "إنشاء تيم",
                                    fontFamily = TajawalFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    color = TeamSeatPrimaryTeal
                                )
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
fun TeamWorkspaceScreenPreview() {
    TeamSeatTheme {
        TeamWorkspaceScreen()
    }
}
