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
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.stackmates.teamseat.core.theme.CairoFontFamily
import com.stackmates.teamseat.core.theme.DeepNavy
import com.stackmates.teamseat.core.theme.LightAqua
import com.stackmates.teamseat.core.theme.Peach
import com.stackmates.teamseat.core.theme.SoftBeige
import com.stackmates.teamseat.core.theme.TeamSeatTheme
import com.stackmates.teamseat.core.theme.Turquoise
import com.stackmates.teamseat.ui.components.StaggeredAnimatedItem
import com.stackmates.teamseat.ui.components.bounceClick
import com.stackmates.teamseat.ui.components.rememberAnimatedProgress

// =============================================================================
// 🎨 الألوان المحددة للشاشة (Warm Cream + Deep Teal + Warm Orange)
// =============================================================================
private val ManageBgCream = Color(0xFFFBF7F2)
private val ManageCardBorder = Color(0xFFE5E0D8)
private val ManagePrimaryTeal = Color(0xFF136F78)
private val ManageWarmOrange = Color(0xFFF97316)
private val ManagePendingBg = Color(0xFFFFF7ED)
private val ManageDangerRed = Color(0xFFC62828)

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
 * - تصميم مسطح بالكامل (Flat Hierarchy) بدون كروت متداخلة
 * - حركات تفاعلية ناعمة (bounceClick، Staggered Entrance، AnimatedContent، ModalBottomSheet)
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
            containerColor = ManageBgCream,
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = ManageBgCream),
                    navigationIcon = {
                        Box(
                            modifier = Modifier
                                .padding(start = 12.dp)
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .border(1.dp, ManageCardBorder, CircleShape)
                                .bounceClick(onClick = onBackClick),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "رجوع",
                                tint = DeepNavy,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    },
                    title = {
                        Column(modifier = Modifier.padding(start = 6.dp)) {
                            Text(
                                text = "إدارة مقاعد التيم",
                                fontFamily = CairoFontFamily,
                                fontWeight = FontWeight.ExtraBold,
                                color = DeepNavy,
                                fontSize = 18.sp,
                                maxLines = 1,
                                softWrap = false
                            )
                            Text(
                                text = "رتّب أدوار TeamSeat-App وتابع الأماكن الفاضية",
                                fontFamily = CairoFontFamily,
                                fontWeight = FontWeight.Medium,
                                color = ManagePrimaryTeal,
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
                                    .background(LightAqua.copy(alpha = 0.45f))
                                    .border(1.dp, ManagePrimaryTeal.copy(alpha = 0.35f), RoundedCornerShape(10.dp))
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
                                        tint = ManagePrimaryTeal,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "مكان جديد",
                                        fontFamily = CairoFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        color = ManagePrimaryTeal,
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
                    shadowElevation = 8.dp,
                    border = BorderStroke(1.dp, ManageCardBorder)
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
                                .background(ManagePrimaryTeal)
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
                                    fontFamily = CairoFontFamily,
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
                // 1. بطاقة ملخص اكتمال التيم (كارت مسطح بدون كروت داخلية)
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, ManageCardBorder),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                                        fontFamily = CairoFontFamily,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = DeepNavy,
                                        fontSize = 16.sp,
                                        textDirection = TextDirection.Ltr
                                    ),
                                    maxLines = 1,
                                    softWrap = false
                                )

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(LightAqua.copy(alpha = 0.45f))
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "$approvedCount من $totalCount معتمدين ($completionPercent%)",
                                        fontFamily = CairoFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        color = ManagePrimaryTeal,
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
                                color = ManagePrimaryTeal,
                                trackColor = SoftBeige
                            )

                            Text(
                                text = "دوس على أي مقعد عشان تعدّل دوره أو تأكد طلب الانضمام.",
                                fontFamily = CairoFontFamily,
                                fontWeight = FontWeight.Normal,
                                color = DeepNavy.copy(alpha = 0.7f),
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                // 2. شريط فلاتر الحالة (Single-line Filter Chips with bounceClick)
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
                                    .background(if (isSelected) ManagePrimaryTeal else Color.White)
                                    .border(
                                        width = 1.dp,
                                        color = if (isSelected) ManagePrimaryTeal else ManageCardBorder,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .bounceClick { selectedFilter = tab }
                                    .padding(horizontal = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = tab.label,
                                    fontFamily = CairoFontFamily,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else DeepNavy,
                                    fontSize = 12.5.sp,
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }
                        }
                    }
                }

                // 3. تبديل محتوى المقاعد بانسيابية أفقية عند تغيير الفلتر
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
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                border = BorderStroke(1.dp, ManageCardBorder),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                                        fontFamily = CairoFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        color = DeepNavy,
                                        fontSize = 14.5.sp,
                                        textAlign = TextAlign.Center
                                    )
                                    Text(
                                        text = "جرّب تختار \"الكل\" عشان تشوف باقي مقاعد التيم.",
                                        fontFamily = CairoFontFamily,
                                        color = DeepNavy.copy(alpha = 0.65f),
                                        fontSize = 12.sp,
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

        // =========================================================================
        // 🪄 ModalBottomSheet لإدارة إجراءات العضو وتعديل الدور
        // =========================================================================
        if (activeSeatForSheet != null) {
            val targetSeat = activeSeatForSheet!!
            ModalBottomSheet(
                onDismissRequest = { activeSeatForSheet = null },
                containerColor = Color.White,
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
                                fontFamily = CairoFontFamily,
                                fontWeight = FontWeight.ExtraBold,
                                color = DeepNavy,
                                fontSize = 17.sp
                            )
                            Text(
                                text = targetSeat.memberName ?: "مكان فاضي مستني عضو جديد",
                                fontFamily = CairoFontFamily,
                                color = ManagePrimaryTeal,
                                fontSize = 13.sp
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(SoftBeige)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = targetSeat.code,
                                fontFamily = CairoFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = DeepNavy,
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
                                fontFamily = CairoFontFamily,
                                fontSize = 12.sp
                            )
                        },
                        textStyle = TextStyle(
                            fontFamily = CairoFontFamily,
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
                                .background(ManageWarmOrange)
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
                                fontFamily = CairoFontFamily,
                                fontWeight = FontWeight.Bold,
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
                                .background(ManagePrimaryTeal)
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
                                fontFamily = CairoFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 14.sp,
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
                                    .border(1.dp, ManageDangerRed.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
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
                                    fontFamily = CairoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    color = ManageDangerRed,
                                    fontSize = 14.sp,
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

        // =========================================================================
        // ➕ ModalBottomSheet لإضافة مكان فاضي جديد
        // =========================================================================
        if (showAddSeatSheet) {
            ModalBottomSheet(
                onDismissRequest = { showAddSeatSheet = false },
                containerColor = Color.White,
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
                        fontFamily = CairoFontFamily,
                        fontWeight = FontWeight.ExtraBold,
                        color = DeepNavy,
                        fontSize = 17.sp
                    )

                    OutlinedTextField(
                        value = newRoleDraft,
                        onValueChange = { newRoleDraft = it },
                        singleLine = true,
                        placeholder = {
                            Text(
                                text = "مثال: Backend Developer",
                                fontFamily = CairoFontFamily,
                                fontSize = 12.5.sp
                            )
                        },
                        textStyle = TextStyle(
                            fontFamily = CairoFontFamily,
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
                            .background(ManagePrimaryTeal)
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
                            fontFamily = CairoFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 14.5.sp,
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
 * صف المقعد الواحد (كارت مسطح بالكامل بدون أي Card/Surface متداخل وبضغطة bounceClick)
 */
@Composable
private fun FlatSeatRowCard(
    seat: TeamSeatUiItem,
    onCardClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val badgeLabel = when {
        seat.isLeader -> "قائد الفريق"
        seat.state == ManageSeatState.APPROVED -> "معتمد"
        seat.state == ManageSeatState.PENDING -> "قيد المراجعة"
        else -> "متاح"
    }

    val badgeBg = when {
        seat.isLeader -> Peach.copy(alpha = 0.55f)
        seat.state == ManageSeatState.APPROVED -> LightAqua.copy(alpha = 0.5f)
        seat.state == ManageSeatState.PENDING -> ManagePendingBg
        else -> SoftBeige.copy(alpha = 0.7f)
    }

    val badgeTextColor = when {
        seat.isLeader -> DeepNavy
        seat.state == ManageSeatState.APPROVED -> ManagePrimaryTeal
        seat.state == ManageSeatState.PENDING -> ManageWarmOrange
        else -> DeepNavy.copy(alpha = 0.8f)
    }

    val codeBg = when {
        seat.isLeader -> DeepNavy
        seat.state == ManageSeatState.APPROVED -> ManagePrimaryTeal
        seat.state == ManageSeatState.PENDING -> ManageWarmOrange
        else -> SoftBeige
    }

    val codeTextColor = if (seat.state == ManageSeatState.AVAILABLE) DeepNavy else Color.White

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .bounceClick(onClick = onCardClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, ManageCardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                        .background(codeBg),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = seat.code,
                        fontFamily = CairoFontFamily,
                        fontWeight = FontWeight.ExtraBold,
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
                        fontFamily = CairoFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = if (seat.memberName != null) DeepNavy else DeepNavy.copy(alpha = 0.6f),
                        fontSize = 14.5.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = seat.roleTitleEn,
                        style = TextStyle(
                            fontFamily = CairoFontFamily,
                            fontWeight = FontWeight.Medium,
                            color = ManagePrimaryTeal,
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
                        .padding(horizontal = 10.dp, vertical = 5.dp),
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
                            fontFamily = CairoFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = badgeTextColor,
                            fontSize = 11.sp,
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
                            .background(ManageBgCream)
                            .border(1.dp, ManageCardBorder, RoundedCornerShape(8.dp))
                            .bounceClick(onClick = onEditClick)
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "تعديل الدور",
                            tint = DeepNavy,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = "عدّل الدور",
                            fontFamily = CairoFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = DeepNavy,
                            fontSize = 11.5.sp,
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
                                .border(1.dp, ManageDangerRed.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                                .bounceClick(onClick = onDeleteClick)
                                .padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = "مسح المكان",
                                tint = ManageDangerRed,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = "امسح المكان",
                                fontFamily = CairoFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                color = ManageDangerRed,
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
private fun ManageSeatsScreenPreview() {
    TeamSeatTheme {
        ManageSeatsScreen()
    }
}
