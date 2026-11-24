package com.stackmates.teamseat.Screens.admin_youssef

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.HourglassTop
import androidx.compose.material.icons.outlined.PersonAddAlt1
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material.icons.outlined.WorkspacePremium
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
import androidx.compose.ui.text.style.TextAlign
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
private val DangerRed = Color(0xFFC62828)

private enum class ManageSeatState {
    APPROVED,
    PENDING,
    AVAILABLE
}

private data class TeamSeatUiItem(
    val id: String,
    val code: String,
    val memberName: String?,
    val roleTitleEn: String,
    val state: ManageSeatState,
    val isLeader: Boolean = false
)

private enum class SeatFilterTab(val label: String) {
    ALL("الكل"),
    APPROVED("المعتمدة"),
    PENDING("قيد الانتظار"),
    AVAILABLE("المتاحة")
}

/**
 * 💻 المسؤول عن الشاشة: يوسف (Youssef - Team Lead & Backend)
 * شاشة 1 من 4: إدارة وتظبيط مقاعد التيم (ManageSeatsScreen)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageSeatsScreen(
    onSaveSeatsClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val context = LocalContext.current

    val seatItems = remember {
        mutableStateListOf(
            TeamSeatUiItem(
                id = "s1",
                code = "S1",
                memberName = "يوسف محمد البسيوني",
                roleTitleEn = "Team Leader & Backend",
                state = ManageSeatState.APPROVED,
                isLeader = true
            ),
            TeamSeatUiItem(
                id = "s2",
                code = "S2",
                memberName = "خالد محمد السعيد",
                roleTitleEn = "Android Developer",
                state = ManageSeatState.APPROVED
            ),
            TeamSeatUiItem(
                id = "s3",
                code = "S3",
                memberName = "حسام الجوهري",
                roleTitleEn = "Android UI & Navigation",
                state = ManageSeatState.APPROVED
            ),
            TeamSeatUiItem(
                id = "s4",
                code = "S4",
                memberName = "إسراء محمد أحمد",
                roleTitleEn = "UI/UX Designer",
                state = ManageSeatState.APPROVED
            ),
            TeamSeatUiItem(
                id = "s5",
                code = "S5",
                memberName = "نها أحمد محمد",
                roleTitleEn = "Frontend Developer",
                state = ManageSeatState.PENDING
            ),
            TeamSeatUiItem(
                id = "s6",
                code = "S6",
                memberName = null,
                roleTitleEn = "QA & Documentation",
                state = ManageSeatState.AVAILABLE
            )
        )
    }

    var selectedFilter by remember { mutableStateOf(SeatFilterTab.ALL) }
    var activeSeatForSheet by remember { mutableStateOf<TeamSeatUiItem?>(null) }
    var roleDraft by remember { mutableStateOf("") }
    var showAddSeatSheet by remember { mutableStateOf(false) }
    var newRoleDraft by remember { mutableStateOf("") }

    val totalCount = seatItems.size
    val approvedCount = seatItems.count { it.state == ManageSeatState.APPROVED }
    val targetProgress = if (totalCount > 0) approvedCount.toFloat() / totalCount else 0f
    val animatedProgress by rememberAnimatedProgress(
        targetProgress = targetProgress,
        durationMillis = 800
    )
    val completionPercent = (animatedProgress * 100).toInt()

    val filteredSeats = remember(seatItems.toList(), selectedFilter) {
        when (selectedFilter) {
            SeatFilterTab.ALL -> seatItems.toList()
            SeatFilterTab.APPROVED -> seatItems.filter { it.state == ManageSeatState.APPROVED }
            SeatFilterTab.PENDING -> seatItems.filter { it.state == ManageSeatState.PENDING }
            SeatFilterTab.AVAILABLE -> seatItems.filter { it.state == ManageSeatState.AVAILABLE }
        }
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
                                .padding(start = 12.dp)
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(TealCardFill)
                                .border(1.2.dp, TealCardBorder, CircleShape)
                                .bounceClick(onClick = onBackClick),
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
                        Column(modifier = Modifier.padding(start = 6.dp)) {
                            Text(
                                text = "إدارة مقاعد التيم",
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = TitleDark,
                                fontSize = 18.sp,
                                maxLines = 1,
                                softWrap = false
                            )
                            Text(
                                text = "رتّب أدوار TeamSeat-App وتابع الأماكن الفاضية",
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.Normal,
                                color = BodyMuted,
                                fontSize = 12.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    },
                    actions = {
                        if (seatItems.size < 8) {
                            Box(
                                modifier = Modifier
                                    .padding(end = 12.dp)
                                    .height(36.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(TealCardFill)
                                    .border(1.2.dp, TealCardBorder, RoundedCornerShape(10.dp))
                                    .bounceClick {
                                        newRoleDraft = ""
                                        showAddSeatSheet = true
                                    }
                                    .padding(horizontal = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "مكان جديد",
                                        tint = PrimaryTeal,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "مكان جديد",
                                        fontFamily = TajawalFontFamily,
                                        fontWeight = FontWeight.SemiBold,
                                        color = PrimaryTeal,
                                        fontSize = 12.sp,
                                        maxLines = 1,
                                        softWrap = false
                                    )
                                }
                            }
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
                                .background(PrimaryTeal)
                                .bounceClick {
                                    Toast.makeText(
                                        context,
                                        "تم حفظ ومزامنة مقاعد التيم بنجاح!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    onSaveSeatsClick()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Save,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(19.dp)
                                )
                                Text(
                                    text = "حفظ ومزامنة المقاعد",
                                    fontFamily = TajawalFontFamily,
                                    fontWeight = FontWeight.SemiBold,
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
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 12.dp,
                    bottom = 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 1. بطاقة ملخص اكتمال التيم (Tinted Flat Card)
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
                                    text = "TeamSeat-App",
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
                                        text = "$approvedCount من $totalCount معتمدين ($completionPercent%)",
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
                                text = "دوس على أي مقعد عشان تعدّل دوره أو تأكد طلب الانضمام.",
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.Normal,
                                color = BodyMuted,
                                fontSize = 12.5.sp
                            )
                        }
                    }
                }

                // 2. شريط فلاتر الحالة
                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(vertical = 2.dp)
                    ) {
                        items(SeatFilterTab.entries) { tab ->
                            val isSelected = selectedFilter == tab
                            Box(
                                modifier = Modifier
                                    .height(36.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSelected) PrimaryTeal else TealCardFill)
                                    .border(
                                        width = 1.2.dp,
                                        color = if (isSelected) PrimaryTeal else TealCardBorder,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .bounceClick { selectedFilter = tab }
                                    .padding(horizontal = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = tab.label,
                                    fontFamily = TajawalFontFamily,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isSelected) Color.White else TitleDark,
                                    fontSize = 12.sp,
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }
                        }
                    }
                }

                // 3. قائمة المقاعد بحركة انتقال ناعمة
                item {
                    AnimatedContent(
                        targetState = selectedFilter,
                        transitionSpec = {
                            (slideInHorizontally(tween(260)) { it / 6 } + fadeIn(tween(260)))
                                .togetherWith(slideOutHorizontally(tween(200)) { -it / 6 } + fadeOut(tween(200)))
                        },
                        label = "seatFilterContentTransition"
                    ) { _ ->
                        if (filteredSeats.isEmpty()) {
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
                                        .padding(24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = "مفيش مقاعد في الفلتر ده دلوقتي",
                                        fontFamily = TajawalFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        color = TitleDark,
                                        fontSize = 15.sp,
                                        textAlign = TextAlign.Center
                                    )
                                    Text(
                                        text = "جرّب تختار \"الكل\" عشان تشوف باقي مقاعد التيم.",
                                        fontFamily = TajawalFontFamily,
                                        color = BodyMuted,
                                        fontSize = 12.5.sp,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        } else {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                filteredSeats.forEachIndexed { index, seat ->
                                    StaggeredAnimatedItem(index = index) {
                                        FlatSeatRowCard(
                                            seat = seat,
                                            onCardClick = {
                                                if (!seat.isLeader) {
                                                    roleDraft = seat.roleTitleEn
                                                    activeSeatForSheet = seat
                                                } else {
                                                    Toast.makeText(
                                                        context,
                                                        "مقعد قائد الفريق ثابت ومعتمد",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            },
                                            onEditClick = {
                                                roleDraft = seat.roleTitleEn
                                                activeSeatForSheet = seat
                                            },
                                            onDeleteClick = {
                                                seatItems.removeAll { it.id == seat.id }
                                                Toast.makeText(
                                                    context,
                                                    "تم مسح المقعد ${seat.code}",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // ModalBottomSheet لإدارة العضو والدور
        if (activeSeatForSheet != null) {
            val targetSeat = activeSeatForSheet!!
            ModalBottomSheet(
                onDismissRequest = { activeSeatForSheet = null },
                containerColor = OffWhiteBg,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "إدارة المقعد ${targetSeat.code}",
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = TitleDark,
                                fontSize = 16.sp
                            )
                            Text(
                                text = targetSeat.memberName ?: "مكان فاضي مستني عضو جديد",
                                fontFamily = TajawalFontFamily,
                                color = BodyMuted,
                                fontSize = 13.sp
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(TealCardFill)
                                .border(1.2.dp, TealCardBorder, RoundedCornerShape(10.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = targetSeat.code,
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                color = PrimaryTeal,
                                fontSize = 12.sp,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }

                    OutlinedTextField(
                        value = roleDraft,
                        onValueChange = { roleDraft = it },
                        singleLine = true,
                        label = {
                            Text(
                                text = "المسمى الوظيفي المطلوب",
                                fontFamily = TajawalFontFamily,
                                fontSize = 12.sp
                            )
                        },
                        textStyle = TextStyle(
                            fontFamily = TajawalFontFamily,
                            fontSize = 14.sp,
                            textDirection = TextDirection.Ltr
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (targetSeat.state == ManageSeatState.PENDING) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(WarmOrangeAccent)
                                .bounceClick {
                                    val idx = seatItems.indexOfFirst { it.id == targetSeat.id }
                                    if (idx != -1) {
                                        seatItems[idx] = targetSeat.copy(
                                            state = ManageSeatState.APPROVED,
                                            roleTitleEn = roleDraft.trim().ifBlank { targetSeat.roleTitleEn }
                                        )
                                        Toast.makeText(
                                            context,
                                            "مبروك! تم اعتماد ${targetSeat.memberName} في التيم",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    activeSeatForSheet = null
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "تأكيد وقبول العضو في التيم",
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White,
                                fontSize = 14.sp,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(PrimaryTeal)
                                .bounceClick {
                                    val idx = seatItems.indexOfFirst { it.id == targetSeat.id }
                                    if (idx != -1 && roleDraft.isNotBlank()) {
                                        seatItems[idx] = targetSeat.copy(roleTitleEn = roleDraft.trim())
                                        Toast.makeText(
                                            context,
                                            "تم تحديث الدور بنجاح",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    activeSeatForSheet = null
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "حفظ التعديل",
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White,
                                fontSize = 13.5.sp,
                                maxLines = 1,
                                softWrap = false
                            )
                        }

                        if (targetSeat.state == ManageSeatState.AVAILABLE) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(46.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFFFF1F2))
                                    .border(1.2.dp, DangerRed.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                                    .bounceClick {
                                        seatItems.removeAll { it.id == targetSeat.id }
                                        Toast.makeText(
                                            context,
                                            "تم مسح المكان الفاضي ${targetSeat.code}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        activeSeatForSheet = null
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "امسح المكان",
                                    fontFamily = TajawalFontFamily,
                                    fontWeight = FontWeight.SemiBold,
                                    color = DangerRed,
                                    fontSize = 13.5.sp,
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        // ModalBottomSheet لإضافة مكان فاضي جديد
        if (showAddSeatSheet) {
            ModalBottomSheet(
                onDismissRequest = { showAddSeatSheet = false },
                containerColor = OffWhiteBg,
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
                        text = "زود مكان فاضي جديد للتيم",
                        fontFamily = TajawalFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = TitleDark,
                        fontSize = 16.sp
                    )

                    OutlinedTextField(
                        value = newRoleDraft,
                        onValueChange = { newRoleDraft = it },
                        singleLine = true,
                        placeholder = {
                            Text(
                                text = "مثال: Backend Developer",
                                fontFamily = TajawalFontFamily,
                                fontSize = 12.5.sp
                            )
                        },
                        textStyle = TextStyle(
                            fontFamily = TajawalFontFamily,
                            fontSize = 14.sp,
                            textDirection = TextDirection.Ltr
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(PrimaryTeal)
                            .bounceClick {
                                if (newRoleDraft.isNotBlank()) {
                                    val nextNum = seatItems.size + 1
                                    seatItems.add(
                                        TeamSeatUiItem(
                                            id = "s_${System.currentTimeMillis()}",
                                            code = "S$nextNum",
                                            memberName = null,
                                            roleTitleEn = newRoleDraft.trim(),
                                            state = ManageSeatState.AVAILABLE
                                        )
                                    )
                                    Toast.makeText(
                                        context,
                                        "تم إضافة مكان فاضي جديد (S$nextNum)",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                showAddSeatSheet = false
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "إضافة المكان للتيم",
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
}

/**
 * صف المقعد الواحد (كارت ملون خفيف بحواف ملونة بدون كروت متداخلة)
 */
@Composable
private fun FlatSeatRowCard(
    seat: TeamSeatUiItem,
    onCardClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val isHighlighted = seat.state == ManageSeatState.PENDING || seat.isLeader
    val cardFill = if (isHighlighted) WarmCardFill else TealCardFill
    val cardBorder = if (isHighlighted) WarmCardBorder else TealCardBorder

    val badgeLabel = when {
        seat.isLeader -> "قائد الفريق"
        seat.state == ManageSeatState.APPROVED -> "معتمد"
        seat.state == ManageSeatState.PENDING -> "قيد المراجعة"
        else -> "متاح"
    }

    val badgeBg = if (isHighlighted) Color.White else Color.White
    val badgeBorder = if (isHighlighted) WarmCardBorder else TealCardBorder
    val badgeTextColor = when {
        seat.isLeader -> TitleDark
        seat.state == ManageSeatState.APPROVED -> PrimaryTeal
        seat.state == ManageSeatState.PENDING -> WarmOrangeAccent
        else -> BodyMuted
    }

    val codeBg = when {
        seat.isLeader -> TitleDark
        seat.state == ManageSeatState.APPROVED -> PrimaryTeal
        seat.state == ManageSeatState.PENDING -> WarmOrangeAccent
        else -> Color.White
    }
    val codeTextColor = if (seat.state == ManageSeatState.AVAILABLE) TitleDark else Color.White

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .bounceClick(onClick = onCardClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardFill),
        border = BorderStroke(1.2.dp, cardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(codeBg)
                        .then(
                            if (seat.state == ManageSeatState.AVAILABLE) {
                                Modifier.border(1.dp, TealCardBorder, RoundedCornerShape(12.dp))
                            } else Modifier
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = seat.code,
                        fontFamily = TajawalFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = codeTextColor,
                        fontSize = 14.sp,
                        maxLines = 1,
                        softWrap = false
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = seat.memberName ?: "مكان فاضي",
                        fontFamily = TajawalFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = if (seat.memberName != null) TitleDark else BodyMuted,
                        fontSize = 15.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = seat.roleTitleEn,
                        style = TextStyle(
                            fontFamily = TajawalFontFamily,
                            fontWeight = FontWeight.Medium,
                            color = BodyMuted,
                            fontSize = 12.5.sp,
                            textDirection = TextDirection.Ltr
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(badgeBg)
                        .border(1.dp, badgeBorder, RoundedCornerShape(10.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = when {
                                seat.isLeader -> Icons.Outlined.WorkspacePremium
                                seat.state == ManageSeatState.APPROVED -> Icons.Default.CheckCircle
                                seat.state == ManageSeatState.PENDING -> Icons.Outlined.HourglassTop
                                else -> Icons.Outlined.PersonAddAlt1
                            },
                            contentDescription = null,
                            tint = badgeTextColor,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = badgeLabel,
                            fontFamily = TajawalFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = badgeTextColor,
                            fontSize = 12.sp,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }
            }

            if (!seat.isLeader) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White)
                            .border(1.dp, TealCardBorder, RoundedCornerShape(8.dp))
                            .bounceClick(onClick = onEditClick)
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "تعديل الدور",
                            tint = PrimaryTeal,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = "عدّل الدور",
                            fontFamily = TajawalFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = TitleDark,
                            fontSize = 12.sp,
                            maxLines = 1,
                            softWrap = false
                        )
                    }

                    if (seat.state == ManageSeatState.AVAILABLE) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFFFF1F2))
                                .border(1.dp, DangerRed.copy(alpha = 0.35f), RoundedCornerShape(8.dp))
                                .bounceClick(onClick = onDeleteClick)
                                .padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = "مسح المكان",
                                tint = DangerRed,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = "امسح المكان",
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                color = DangerRed,
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ManageSeatsScreenPreview() {
    TeamSeatTheme {
        ManageSeatsScreen()
    }
}
