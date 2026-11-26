package com.stackmates.teamseat.Screens.admin_youssef

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
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
import com.stackmates.teamseat.core.utils.PdfExporter
import com.stackmates.teamseat.ui.components.*
import java.io.File

private data class RosterMember(
    val seatNumber: String,
    val name: String,
    val roleEn: String,
    val isLeader: Boolean = false,
    val isApproved: Boolean = true
)

/**
 * 💻 المسؤول عن الشاشة: يوسف (Youssef - Team Lead & Backend)
 * شاشة كشف التسليم الرسمي والاعتماد الأكاديمي (TASheetPreviewScreen)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TASheetPreviewScreen(
    team: Team = MockDataProvider.sampleTeam,
    seats: List<Seat> = MockDataProvider.sampleSeats,
    onLockTeamClick: () -> Unit = {},
    onExportPdfClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val context = LocalContext.current
    var isExporting by remember { mutableStateOf(false) }
    var exportedFile by remember { mutableStateOf<File?>(null) }
    var showExportSuccessSheet by remember { mutableStateOf(false) }

    val rosterMembers = remember {
        listOf(
            RosterMember("S1", "يوسف محمد البسيوني", "Team Leader & Backend (Firebase)", isLeader = true),
            RosterMember("S2", "خالد محمد السعيد", "Android Developer (Kotlin / Compose)"),
            RosterMember("S3", "حسام الجوهري", "Android Developer (UI & Navigation)"),
            RosterMember("S4", "إسراء محمد أحمد أبو المعاطى", "UI/UX Designer"),
            RosterMember("S5", "نها أحمد محمد رجب", "Frontend & State Management"),
            RosterMember("S6", "آلاء عصام حامد عبدالقادر", "QA & Documentation")
        )
    }

    val pdfSeats = remember(rosterMembers) {
        rosterMembers.mapIndexed { index, m ->
            Seat(
                id = "seat_${index + 1}",
                teamId = team.teamId,
                seatNumber = index + 1,
                roleRequired = m.roleEn,
                status = SeatStatus.BOOKED,
                applicantName = m.name
            )
        }
    }

    val totalCount = rosterMembers.size
    val bookedCount = rosterMembers.count { it.isApproved }
    val isComplete = bookedCount == totalCount
    val animatedProgress by rememberAnimatedProgress(
        targetProgress = if (totalCount > 0) bookedCount.toFloat() / totalCount else 0f,
        durationMillis = 800
    )
    val animatedPercent = (animatedProgress * 100).toInt()

    TeamSeatScreenContainer(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
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
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "كشف التسليم الرسمي",
                            fontFamily = TajawalFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = TeamSeatTitleDark,
                            fontSize = 18.sp,
                            maxLines = 1,
                            softWrap = false
                        )
                        Text(
                            text = "اعتماد وتوثيق مقاعد الفريق",
                            fontFamily = TajawalFontFamily,
                            fontWeight = FontWeight.Medium,
                            color = TeamSeatBodyMuted,
                            fontSize = 12.sp,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                },
                actions = {
                    Box(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(1.dp, TeamSeatCardBorderTeal, CircleShape)
                            .bounceClick {
                                val result = PdfExporter.exportTeamSheetPdf(context, team, pdfSeats)
                                result.onSuccess { file ->
                                    exportedFile = file
                                    showExportSuccessSheet = true
                                    onExportPdfClick()
                                }.onFailure { e ->
                                    Toast.makeText(context, "تعذر تصدير الكشف: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.FileDownload,
                            contentDescription = "تحميل PDF",
                            tint = TeamSeatPrimaryTeal,
                            modifier = Modifier.size(20.dp)
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(TeamSeatTitleDark)
                            .bounceClick(enabled = !isExporting) {
                                isExporting = true
                                val result = PdfExporter.exportTeamSheetPdf(context, team, pdfSeats)
                                isExporting = false
                                result.onSuccess { file ->
                                    exportedFile = file
                                    showExportSuccessSheet = true
                                    onExportPdfClick()
                                }.onFailure { e ->
                                    Toast.makeText(context, "تعذر تصدير الملف: ${e.message}", Toast.LENGTH_LONG).show()
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (isExporting) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.FileDownload,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = "تصدير كشف PDF",
                                    fontFamily = TajawalFontFamily,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp,
                                    color = Color.White,
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .shadow(
                                elevation = 8.dp,
                                shape = RoundedCornerShape(14.dp),
                                ambientColor = TeamSeatPrimaryTeal,
                                spotColor = TeamSeatPrimaryTeal
                            )
                            .clip(RoundedCornerShape(14.dp))
                            .background(TeamSeatPrimaryTeal)
                            .bounceClick(onClick = onLockTeamClick),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = if (team.isLocked) Icons.Rounded.Lock else Icons.Rounded.LockOpen,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = if (team.isLocked) "معتمد ومقفل" else "اعتماد وقفل التيم",
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                color = Color.White,
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
            item {
                TeamSeatCard(contentPadding = 16.dp) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
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
                                ProjectCategoryIllustration(
                                    category = "TeamSeat-App Android",
                                    modifier = Modifier.size(46.dp)
                                )

                                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                    Text(
                                        text = "TeamSeat-App",
                                        style = TextStyle(
                                            fontFamily = TajawalFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            color = TeamSeatTitleDark,
                                            fontSize = 15.sp,
                                            textDirection = TextDirection.Ltr
                                        ),
                                        maxLines = 1,
                                        softWrap = false
                                    )
                                    Text(
                                        text = "مشروع مادة تطوير تطبيقات الهواتف - الفرقة الثالثة",
                                        fontFamily = TajawalFontFamily,
                                        fontWeight = FontWeight.Normal,
                                        color = TeamSeatBodyMuted,
                                        fontSize = 12.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color.White)
                                    .border(1.dp, TeamSeatCardBorderTeal, RoundedCornerShape(10.dp))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isComplete) Icons.Rounded.CheckCircle else Icons.Rounded.HourglassTop,
                                        contentDescription = null,
                                        tint = TeamSeatPrimaryTeal,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Text(
                                        text = if (isComplete) "جاهز للاعتماد" else "قيد الاستكمال",
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

                        HorizontalDivider(color = TeamSeatDividerColor, thickness = 0.8.dp)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "اكتمال المقاعد المعتمدة:",
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.Medium,
                                color = TeamSeatBodyMuted,
                                fontSize = 12.sp
                            )
                            Text(
                                text = "$bookedCount من $totalCount مقاعد ($animatedPercent%)",
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = TeamSeatPrimaryTeal,
                                fontSize = 12.sp,
                                maxLines = 1,
                                softWrap = false
                            )
                        }

                        LinearProgressIndicator(
                            progress = { animatedProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(7.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = TeamSeatPrimaryTeal,
                            trackColor = Color.White
                        )
                    }
                }
            }

            item {
                Text(
                    text = "هيئة الإشراف والتقييم الأكاديمي",
                    fontFamily = TajawalFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = TeamSeatTitleDark,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            item {
                SupervisionFlatCard(
                    name = "د. محمد كاظم",
                    subtitle = "أستاذ المقرر ورئيس لجنة التقييم",
                    badgeText = "أستاذ المقرر",
                    icon = Icons.Rounded.VerifiedUser,
                    isWarmHighlight = false
                )
            }

            item {
                SupervisionFlatCard(
                    name = "م. شهد أحمد عبدالسلام",
                    subtitle = "المعيدة المشرفة على المشروع",
                    badgeText = "مسؤولة التسليم",
                    icon = Icons.Rounded.Person,
                    isWarmHighlight = true
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "كشف مقاعد الفريق المعتمدة",
                        fontFamily = TajawalFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = TeamSeatTitleDark,
                        fontSize = 15.sp
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(TeamSeatSoftTealPill)
                            .padding(horizontal = 10.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "6 مقاعد معتمدة",
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

            rosterMembers.forEachIndexed { index, member ->
                item(key = "roster_${member.seatNumber}") {
                    StaggeredAnimatedItem(index = index) {
                        RosterMemberFlatCard(member = member)
                    }
                }
            }
        }
    }

    if (showExportSuccessSheet && exportedFile != null) {
        ModalBottomSheet(
            onDismissRequest = { showExportSuccessSheet = false },
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
                    text = "تم تصدير كشف التسليم الرسمي بنجاح ✓",
                    fontFamily = TajawalFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = TeamSeatTitleDark,
                    fontSize = 16.sp
                )
                Text(
                    text = "تم حفظ ملف PDF الرسمي للفريق (${exportedFile?.name}) وأصبح جاهزاً للمراجعة والتسليم.",
                    fontFamily = TajawalFontFamily,
                    fontWeight = FontWeight.Normal,
                    color = TeamSeatBodyMuted,
                    fontSize = 13.sp,
                    lineHeight = 20.sp
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(TeamSeatPrimaryTeal)
                        .bounceClick { showExportSuccessSheet = false },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "حسناً",
                        fontFamily = TajawalFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        fontSize = 14.sp,
                        maxLines = 1,
                        softWrap = false
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun SupervisionFlatCard(
    name: String,
    subtitle: String,
    badgeText: String,
    icon: ImageVector,
    isWarmHighlight: Boolean
) {
    val border = if (isWarmHighlight) TeamSeatCardBorderWarm else TeamSeatCardBorderTeal
    val accent = if (isWarmHighlight) TeamSeatWarmOrange else TeamSeatPrimaryTeal

    TeamSeatCard(isWarmHighlight = isWarmHighlight, contentPadding = 14.dp) {
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
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(1.dp, border, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = accent,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = name,
                        fontFamily = TajawalFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = TeamSeatTitleDark,
                        fontSize = 15.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = subtitle,
                        fontFamily = TajawalFontFamily,
                        fontWeight = FontWeight.Normal,
                        color = TeamSeatBodyMuted,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .border(1.dp, border, RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = badgeText,
                    fontFamily = TajawalFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = accent,
                    fontSize = 12.sp,
                    maxLines = 1,
                    softWrap = false
                )
            }
        }
    }
}

@Composable
private fun RosterMemberFlatCard(member: RosterMember) {
    val isLeader = member.isLeader
    val border = if (isLeader) TeamSeatCardBorderWarm else TeamSeatCardBorderTeal
    val accent = if (isLeader) TeamSeatWarmOrange else TeamSeatPrimaryTeal

    TeamSeatCard(isWarmHighlight = isLeader, contentPadding = 14.dp) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isLeader) TeamSeatWarmOrange else TeamSeatPrimaryTeal),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = member.seatNumber,
                    fontFamily = TajawalFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 13.sp,
                    maxLines = 1,
                    softWrap = false
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = member.name,
                    fontFamily = TajawalFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = TeamSeatTitleDark,
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = member.roleEn,
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
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = null,
                        tint = accent,
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text = if (isLeader) "قائد الفريق" else "معتمد",
                        fontFamily = TajawalFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = accent,
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
private fun TASheetPreviewScreenPreview() {
    TeamSeatTheme {
        TASheetPreviewScreen()
    }
}
