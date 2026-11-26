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
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.LockOpen
import androidx.compose.material.icons.rounded.VerifiedUser
import androidx.compose.material.icons.rounded.WarningAmber
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stackmates.teamseat.core.theme.TajawalFontFamily
import com.stackmates.teamseat.core.theme.TeamSeatTheme
import com.stackmates.teamseat.ui.components.*

/**
 * 💻 المسؤول عن الشاشة: يوسف (Youssef - Team Lead & Backend)
 * شاشة اعتماد وقفل التيم نهائياً (LockTeamScreen / LockTeamDialog)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LockTeamDialog(
    teamName: String = "TeamSeat-App",
    totalSeats: Int = 6,
    bookedSeats: Int = 6,
    teachingAssistant: String = "م. شهد أحمد عبدالسلام",
    supervisorDoctor: String = "د. محمد كاظم",
    onConfirmLock: () -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    val context = LocalContext.current
    var isConfirmedByLeader by remember { mutableStateOf(false) }
    var isLockedSuccess by remember { mutableStateOf(false) }

    val targetProgress = if (totalSeats > 0) bookedSeats.toFloat() / totalSeats else 0f
    val animatedProgress by rememberAnimatedProgress(targetProgress = targetProgress, durationMillis = 800)
    val animatedPercent = (animatedProgress * 100).toInt()

    val membersList = remember {
        listOf(
            Triple("يوسف محمد البسيوني", "Team Leader & Backend", true),
            Triple("خالد محمد السعيد", "Android Developer", false),
            Triple("حسام الجوهري", "Android UI & Navigation", false),
            Triple("إسراء محمد أحمد", "UI/UX Designer", false),
            Triple("نها أحمد محمد", "Frontend & State Management", false),
            Triple("آلاء عصام حامد", "QA & Documentation", false)
        )
    }

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
                            .bounceClick(onClick = onDismiss),
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
                            text = "اقفل التيم وجهّز الكشف",
                            fontFamily = TajawalFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = TeamSeatTitleDark,
                            fontSize = 18.sp,
                            maxLines = 1,
                            softWrap = false
                        )
                        Text(
                            text = "خطوة نهائية قبل التسليم الأكاديمي",
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
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .then(
                                if (isConfirmedByLeader) {
                                    Modifier.shadow(
                                        elevation = 8.dp,
                                        shape = RoundedCornerShape(14.dp),
                                        ambientColor = TeamSeatPrimaryTeal,
                                        spotColor = TeamSeatPrimaryTeal
                                    )
                                } else Modifier
                            )
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (isConfirmedByLeader) TeamSeatPrimaryTeal else Color(0xFFD6E3E3))
                            .bounceClick(enabled = isConfirmedByLeader && !isLockedSuccess) {
                                isLockedSuccess = true
                                Toast.makeText(context, "تمام! التيم اتقفل وجاهز للتسليم", Toast.LENGTH_SHORT).show()
                                onConfirmLock()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = if (isLockedSuccess) Icons.Rounded.Lock else Icons.Rounded.LockOpen,
                                contentDescription = null,
                                tint = if (isConfirmedByLeader) Color.White else TeamSeatBodyMuted,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = if (isLockedSuccess) "تم قفل التيم بنجاح ✓" else "تأكيد قفل التيم",
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = if (isConfirmedByLeader) Color.White else TeamSeatBodyMuted,
                                fontSize = 14.sp,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .border(1.2.dp, TeamSeatCardBorderTeal, RoundedCornerShape(12.dp))
                            .bounceClick(onClick = onDismiss),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "ارجع راجع المقاعد الأول",
                            fontFamily = TajawalFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = TeamSeatPrimaryTeal,
                            fontSize = 13.sp,
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
                bottom = safePadding.calculateBottomPadding()
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                TeamSeatCard(contentPadding = 16.dp) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                ProjectCategoryIllustration(
                                    category = teamName,
                                    modifier = Modifier.size(44.dp)
                                )
                                Text(
                                    text = teamName,
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
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(TeamSeatSoftTealPill)
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "$bookedSeats من $totalSeats ($animatedPercent%)",
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
                            text = "الإشراف: $supervisorDoctor • $teachingAssistant",
                            fontFamily = TajawalFontFamily,
                            fontWeight = FontWeight.Normal,
                            color = TeamSeatBodyMuted,
                            fontSize = 12.5.sp
                        )
                    }
                }
            }

            item {
                TeamSeatCard(isWarmHighlight = true, contentPadding = 14.dp) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.WarningAmber,
                            contentDescription = null,
                            tint = TeamSeatWarmOrange,
                            modifier = Modifier.size(22.dp)
                        )
                        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            Text(
                                text = "خد بالك قبل ما تقفل",
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = TeamSeatTitleDark,
                                fontSize = 15.sp
                            )
                            Text(
                                text = "بعد ما تقفل التيم، مفيش حد يقدر يبدّل مقعده أو يضيف عضو جديد إلا بعد مراجعة المعيدة.",
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.Normal,
                                color = TeamSeatBodyMuted,
                                fontSize = 12.5.sp,
                                lineHeight = 19.sp
                            )
                        }
                    }
                }
            }

            item {
                Text(
                    text = "الأعضاء اللي هيتسجلوا في الكشف",
                    fontFamily = TajawalFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = TeamSeatTitleDark,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            membersList.forEachIndexed { index, (name, roleEn, isLeader) ->
                item(key = "lock_member_$index") {
                    StaggeredAnimatedItem(index = index) {
                        TeamSeatCard(
                            isWarmHighlight = isLeader,
                            contentPadding = 14.dp
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isLeader) TeamSeatWarmOrange else TeamSeatPrimaryTeal),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "S${index + 1}",
                                        fontFamily = TajawalFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
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
                                        text = name,
                                        fontFamily = TajawalFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        color = TeamSeatTitleDark,
                                        fontSize = 15.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = roleEn,
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
                                        .border(
                                            1.dp,
                                            if (isLeader) TeamSeatCardBorderWarm else TeamSeatCardBorderTeal,
                                            RoundedCornerShape(8.dp)
                                        )
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (isLeader) Icons.Rounded.VerifiedUser else Icons.Rounded.CheckCircle,
                                            contentDescription = null,
                                            tint = if (isLeader) TeamSeatWarmOrange else TeamSeatPrimaryTeal,
                                            modifier = Modifier.size(13.dp)
                                        )
                                        Text(
                                            text = if (isLeader) "قائد الفريق" else "معتمد",
                                            fontFamily = TajawalFontFamily,
                                            fontWeight = FontWeight.SemiBold,
                                            color = if (isLeader) TeamSeatWarmOrange else TeamSeatPrimaryTeal,
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

            item {
                TeamSeatCard(
                    isWarmHighlight = !isConfirmedByLeader,
                    contentPadding = 12.dp,
                    onClick = { isConfirmedByLeader = !isConfirmedByLeader }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Checkbox(
                            checked = isConfirmedByLeader,
                            onCheckedChange = { isConfirmedByLeader = it },
                            colors = CheckboxDefaults.colors(checkedColor = TeamSeatPrimaryTeal)
                        )
                        Text(
                            text = "تمام، راجعت كل الأسماء ومتأكد إني عايز أقفل التيم.",
                            fontFamily = TajawalFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = TeamSeatTitleDark,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LockTeamDialogPreview() {
    TeamSeatTheme {
        LockTeamDialog()
    }
}
