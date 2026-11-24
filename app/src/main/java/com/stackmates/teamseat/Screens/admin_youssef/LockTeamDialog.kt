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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stackmates.teamseat.core.theme.*
import com.stackmates.teamseat.ui.components.StaggeredAnimatedItem
import com.stackmates.teamseat.ui.components.bounceClick
import com.stackmates.teamseat.ui.components.rememberAnimatedProgress

// =============================================================================
// 🎨 باليت الألوان الرسمية للتطبيق (Color.kt)
// =============================================================================
private val OffWhiteBg = WarmCream
private val PrimaryTeal = DeepTeal
private val TitleDark = DeepNavy
private val BodyMuted = DeepNavy.copy(alpha = 0.65f)

private val TealCardFill = Color.White
private val TealCardBorder = SoftGray.copy(alpha = 0.6f)

private val WarmCardFill = SoftBeige.copy(alpha = 0.45f)
private val WarmCardBorder = Peach
private val WarmOrangeAccent = DeepNavy

/**
 * 💻 المسؤول عن الشاشة: يوسف (Youssef - Team Lead & Backend)
 * شاشة اعتماد وقفل التيم نهائياً (LockTeamScreen / LockTeamDialog)
 * - خلفية Off-White (#F8FAFA) وكروت مسطحة ملونة بحواف 1.2.dp بدون كروت متداخلة
 * - أسلوب مصري بسيط وواضح مع حركات bounceClick و rememberAnimatedProgress
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
    val animatedProgress by rememberAnimatedProgress(
        targetProgress = targetProgress,
        durationMillis = 800
    )
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

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Scaffold(
            containerColor = OffWhiteBg,
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = OffWhiteBg),
                    navigationIcon = {
                        Box(
                            modifier = Modifier
                                .padding(start = 12.dp, end = 6.dp)
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(TealCardFill)
                                .border(1.2.dp, TealCardBorder, CircleShape)
                                .bounceClick(onClick = onDismiss),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "رجوع",
                                tint = TitleDark,
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
                                color = TitleDark,
                                fontSize = 18.sp,
                                maxLines = 1,
                                softWrap = false
                            )
                            Text(
                                text = "خطوة نهائية قبل التسليم الأكاديمي",
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.Normal,
                                color = BodyMuted,
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
                    shadowElevation = 6.dp,
                    border = BorderStroke(1.dp, TealCardBorder.copy(alpha = 0.6f))
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
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    if (isConfirmedByLeader) PrimaryTeal else Color(0xFFD6E3E3)
                                )
                                .bounceClick(enabled = isConfirmedByLeader && !isLockedSuccess) {
                                    isLockedSuccess = true
                                    Toast.makeText(
                                        context,
                                        "تمام! التيم اتقفل وجاهز للتسليم",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    onConfirmLock()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = if (isLockedSuccess) Icons.Default.Lock else Icons.Outlined.Lock,
                                    contentDescription = null,
                                    tint = if (isConfirmedByLeader) Color.White else BodyMuted,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = if (isLockedSuccess) "تم قفل التيم بنجاح ✓" else "تأكيد قفل التيم",
                                    fontFamily = TajawalFontFamily,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isConfirmedByLeader) Color.White else BodyMuted,
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
                                .background(TealCardFill)
                                .border(1.2.dp, TealCardBorder, RoundedCornerShape(12.dp))
                                .bounceClick(onClick = onDismiss),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "ارجع راجع المقاعد الأول",
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                color = PrimaryTeal,
                                fontSize = 13.sp,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }
                }
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 12.dp,
                    bottom = 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 1. بطاقة ملخص جاهزية التيم
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = TealCardFill),
                        border = BorderStroke(1.2.dp, TealCardBorder),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = teamName,
                                    style = TextStyle(
                                        fontFamily = TajawalFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        color = TitleDark,
                                        fontSize = 15.sp,
                                        textDirection = TextDirection.Ltr
                                    ),
                                    maxLines = 1,
                                    softWrap = false
                                )

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color.White)
                                        .border(1.dp, TealCardBorder, RoundedCornerShape(10.dp))
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "$bookedSeats من $totalSeats ($animatedPercent%)",
                                        fontFamily = TajawalFontFamily,
                                        fontWeight = FontWeight.SemiBold,
                                        color = PrimaryTeal,
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
                                color = PrimaryTeal,
                                trackColor = Color.White
                            )

                            Text(
                                text = "الإشراف: $supervisorDoctor • $teachingAssistant",
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.Normal,
                                color = BodyMuted,
                                fontSize = 12.5.sp
                            )
                        }
                    }
                }

                // 2. تنبيه القفل النهائي (Warm Highlight Card)
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = WarmCardFill),
                        border = BorderStroke(1.2.dp, WarmCardBorder),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.WarningAmber,
                                contentDescription = null,
                                tint = WarmOrangeAccent,
                                modifier = Modifier.size(22.dp)
                            )
                            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                                Text(
                                    text = "خد بالك قبل ما تقفل",
                                    fontFamily = TajawalFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    color = TitleDark,
                                    fontSize = 15.sp
                                )
                                Text(
                                    text = "بعد ما تقفل التيم، مفيش حد يقدر يبدّل مقعده أو يضيف عضو جديد إلا بعد مراجعة المعيدة.",
                                    fontFamily = TajawalFontFamily,
                                    fontWeight = FontWeight.Normal,
                                    color = BodyMuted,
                                    fontSize = 12.5.sp,
                                    lineHeight = 19.sp
                                )
                            }
                        }
                    }
                }

                // 3. قائمة الأعضاء المعتمدين (ظهور متتابع بدون كروت متداخلة)
                item {
                    Text(
                        text = "الأعضاء اللي هيتسجلوا في الكشف",
                        fontFamily = TajawalFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = TitleDark,
                        fontSize = 15.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                membersList.forEachIndexed { index, (name, roleEn, isLeader) ->
                    item(key = "lock_member_$index") {
                        StaggeredAnimatedItem(index = index) {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isLeader) WarmCardFill else TealCardFill
                                ),
                                border = BorderStroke(
                                    1.2.dp,
                                    if (isLeader) WarmCardBorder else TealCardBorder
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(if (isLeader) WarmOrangeAccent else PrimaryTeal),
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
                                            color = TitleDark,
                                            fontSize = 15.sp,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = roleEn,
                                            style = TextStyle(
                                                fontFamily = TajawalFontFamily,
                                                fontWeight = FontWeight.Medium,
                                                color = BodyMuted,
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
                                                if (isLeader) WarmCardBorder else TealCardBorder,
                                                RoundedCornerShape(8.dp)
                                            )
                                            .padding(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Icon(
                                                imageVector = if (isLeader) Icons.Outlined.VerifiedUser else Icons.Default.CheckCircle,
                                                contentDescription = null,
                                                tint = if (isLeader) WarmOrangeAccent else PrimaryTeal,
                                                modifier = Modifier.size(13.dp)
                                            )
                                            Text(
                                                text = if (isLeader) "قائد الفريق" else "معتمد",
                                                fontFamily = TajawalFontFamily,
                                                fontWeight = FontWeight.SemiBold,
                                                color = if (isLeader) WarmOrangeAccent else PrimaryTeal,
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

                // 4. مربع تأكيد قائد الفريق
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .bounceClick { isConfirmedByLeader = !isConfirmedByLeader },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isConfirmedByLeader) TealCardFill else WarmCardFill
                        ),
                        border = BorderStroke(
                            1.2.dp,
                            if (isConfirmedByLeader) TealCardBorder else WarmCardBorder
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Checkbox(
                                checked = isConfirmedByLeader,
                                onCheckedChange = { isConfirmedByLeader = it },
                                colors = CheckboxDefaults.colors(checkedColor = PrimaryTeal)
                            )
                            Text(
                                text = "تمام، راجعت كل الأسماء ومتأكد إني عايز أقفل التيم.",
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                color = TitleDark,
                                fontSize = 13.sp
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
private fun LockTeamDialogPreview() {
    TeamSeatTheme {
        LockTeamDialog()
    }
}
