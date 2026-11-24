package com.stackmates.teamseat.Screens.requests_hossam

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stackmates.teamseat.core.data.MockDataProvider
import com.stackmates.teamseat.core.model.NotificationItem
import com.stackmates.teamseat.core.model.NotificationType
import com.stackmates.teamseat.core.theme.CairoFontFamily
import com.stackmates.teamseat.core.theme.TeamSeatTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// =============================================================================
// 🎨 باليت الألوان المحددة بدقة طبقاً للمواصفات الرسمية المطلوبة:
// (خلفية بيج كريمي ناعم #FBF7F2، كروت بيضاء #FFFFFF،
// أزرار وشارات أخضر تركواز داكن #136F78، لمسات دافئة ناعمة #F5E6D3)
// =============================================================================
private val ScreenBg = Color(0xFFFBF7F2)       // بيج كريمي ناعم
private val CardWhite = Color(0xFFFFFFFF)      // كروت بيضاء ناصعة
private val DeepTealColor = Color(0xFF136F78)  // أخضر تركواز داكن
private val SoftWarmBeige = Color(0xFFF5E6D3)  // لمسات دافئة ناعمة
private val DarkTextColor = Color(0xFF0F292E)  // النص الأساسي
private val BorderMuted = Color(0xFFE5DFD7)    // حدود ناعمة ومريحة

/**
 * ⚡ المسؤول عن الشاشة: حسام (Hossam) — [HOSSAM-4]
 * شاشة مركز الإشعارات الفورية (Notifications Screen)
 * تم ضبط المقاسات، والمسافات، والمحاذاة بدقة متناهية:
 * 1. شريط فلتر أفقي بـ LazyRow قابل للتمرير وبدون أي إيموجي مزدحم وبـ wrapContentWidth.
 * 2. إلغاء الإطار المزدوج تماماً واعتماد كارت موحد بـ padding(16.dp).
 * 3. رأس كارت منظم: أيقونة (42.dp) على اليمين، عنوان (15.sp) بدون تكرار إيموجي، وشارة (26.dp) على اليسار.
 * 4. محاذاة نص الإشعار والوقت بمسافة (54.dp) من يمين الكارت ليبدأ أسفل العنوان مباشرة.
 * 5. إيقاع بصري موحد: هامش جانبي 16.dp، ومسافة بين الكروت 12.dp.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    notifications: List<NotificationItem> = MockDataProvider.sampleNotifications,
    onBackClick: () -> Unit = {}
) {
    val notificationList = remember { mutableStateListOf(*notifications.toTypedArray()) }
    var selectedFilter by remember { mutableStateOf("الكل") }
    var showMarkedAllBanner by remember { mutableStateOf(false) }
    var showClearConfirmDialog by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val filteredList = when (selectedFilter) {
        "قبول" -> notificationList.filter { it.type == NotificationType.ACCEPTED }
        "طلبات" -> notificationList.filter { it.type == NotificationType.NEW_REQUEST }
        "اعتماد" -> notificationList.filter { it.type == NotificationType.TEAM_LOCKED }
        "مرفوض" -> notificationList.filter { it.type == NotificationType.REJECTED }
        else -> notificationList
    }

    val unreadCount = notificationList.count { !it.isRead }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Scaffold(
            containerColor = ScreenBg,
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = "مركز الإشعارات",
                                fontFamily = CairoFontFamily,
                                fontWeight = FontWeight.ExtraBold,
                                color = DarkTextColor,
                                fontSize = 18.5.sp,
                                maxLines = 1,
                                softWrap = false
                            )
                            Text(
                                text = "التنبيهات الفورية وحالة طلباتك",
                                fontFamily = CairoFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                color = DeepTealColor,
                                fontSize = 12.sp,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    },
                    navigationIcon = {
                        Surface(
                            modifier = Modifier
                                .padding(start = 12.dp, end = 6.dp)
                                .size(40.dp),
                            shape = CircleShape,
                            color = CardWhite,
                            shadowElevation = 1.5.dp,
                            border = BorderStroke(1.dp, BorderMuted)
                        ) {
                            IconButton(onClick = onBackClick) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "رجوع",
                                    tint = DarkTextColor,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    },
                    actions = {
                        if (unreadCount > 0) {
                            Surface(
                                modifier = Modifier
                                    .padding(end = 12.dp)
                                    .height(36.dp)
                                    .clip(RoundedCornerShape(18.dp))
                                    .clickable {
                                        for (i in notificationList.indices) {
                                            notificationList[i] = notificationList[i].copy(isRead = true)
                                        }
                                        showMarkedAllBanner = true
                                        scope.launch {
                                            snackbarHostState.showSnackbar("تم تعليم جميع الإشعارات كمقروءة ✓")
                                            delay(2500)
                                            showMarkedAllBanner = false
                                        }
                                    },
                                shape = RoundedCornerShape(18.dp),
                                color = CardWhite,
                                shadowElevation = 1.dp,
                                border = BorderStroke(1.dp, DeepTealColor.copy(alpha = 0.4f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DoneAll,
                                        contentDescription = "مقروء",
                                        tint = DeepTealColor,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "مقروء ✓✓",
                                        fontFamily = CairoFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        color = DeepTealColor,
                                        fontSize = 11.5.sp,
                                        maxLines = 1,
                                        softWrap = false
                                    )
                                }
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = ScreenBg)
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                // =========================================================================
                // 0. بانر التحديد كمقروء (Success Feedback Banner)
                // =========================================================================
                item {
                    AnimatedVisibility(
                        visible = showMarkedAllBanner,
                        enter = slideInVertically() + fadeIn(),
                        exit = slideOutVertically() + fadeOut()
                    ) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            color = SoftWarmBeige.copy(alpha = 0.6f),
                            border = BorderStroke(1.dp, DeepTealColor.copy(alpha = 0.4f))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(26.dp)
                                        .clip(CircleShape)
                                        .background(DeepTealColor),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(15.dp)
                                    )
                                }
                                Text(
                                    text = "تم تعليم كافة التنبيهات كمقروءة بنجاح ✓",
                                    fontFamily = CairoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    color = DarkTextColor,
                                    fontSize = 12.5.sp
                                )
                            }
                        }
                    }
                }

                // =========================================================================
                // 1. بطاقة الملخص وفلاتر التصفية الخمسة
                // =========================================================================
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = CardWhite),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
                        border = BorderStroke(1.dp, BorderMuted)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Surface(
                                        modifier = Modifier.size(40.dp),
                                        shape = CircleShape,
                                        color = SoftWarmBeige.copy(alpha = 0.65f),
                                        border = BorderStroke(1.dp, BorderMuted)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                imageVector = Icons.Default.NotificationsActive,
                                                contentDescription = null,
                                                tint = DeepTealColor,
                                                modifier = Modifier.size(21.dp)
                                            )
                                        }
                                    }

                                    Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                                        Text(
                                            text = "جميع التنبيهات",
                                            fontFamily = CairoFontFamily,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = DarkTextColor,
                                            fontSize = 16.sp,
                                            maxLines = 1,
                                            softWrap = false
                                        )
                                        Text(
                                            text = if (unreadCount > 0) "لديك $unreadCount تنبيهات غير مقروءة" else "جميع الإشعارات مقروءة",
                                            fontFamily = CairoFontFamily,
                                            fontWeight = FontWeight.Medium,
                                            color = DarkTextColor.copy(alpha = 0.65f),
                                            fontSize = 12.sp,
                                            maxLines = 1,
                                            softWrap = false
                                        )
                                    }
                                }

                                if (unreadCount > 0) {
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = SoftWarmBeige,
                                        border = BorderStroke(1.dp, BorderMuted)
                                    ) {
                                        Text(
                                            text = "$unreadCount جديد",
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                            fontFamily = CairoFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            color = DarkTextColor,
                                            fontSize = 11.5.sp,
                                            maxLines = 1,
                                            softWrap = false
                                        )
                                    }
                                }
                            }

                            HorizontalDivider(color = BorderMuted.copy(alpha = 0.7f), thickness = 0.8.dp)

                            // شريط الفلتر الأفقي بخمس شرائح واضحة بدون إيموجي وبدون أي انقصاص
                            val filterOptions = listOf("الكل", "قبول", "طلبات", "اعتماد", "مرفوض")
                            LazyRow(
                                modifier = Modifier.fillMaxWidth(),
                                contentPadding = PaddingValues(horizontal = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(filterOptions) { filter ->
                                    val isSelected = selectedFilter == filter
                                    Surface(
                                        modifier = Modifier
                                            .height(36.dp)
                                            .wrapContentWidth()
                                            .clip(RoundedCornerShape(12.dp))
                                            .clickable { selectedFilter = filter },
                                        shape = RoundedCornerShape(12.dp),
                                        color = if (isSelected) DeepTealColor else ScreenBg,
                                        border = BorderStroke(
                                            width = 1.dp,
                                            color = if (isSelected) DeepTealColor else BorderMuted
                                        )
                                    ) {
                                        Box(
                                            modifier = Modifier.padding(horizontal = 14.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = filter,
                                                fontFamily = CairoFontFamily,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                                color = if (isSelected) Color.White else DarkTextColor,
                                                fontSize = 12.5.sp,
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

                // =========================================================================
                // 2. حالة عدم وجود إشعارات (Empty State)
                // =========================================================================
                if (filteredList.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = CardWhite),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
                            border = BorderStroke(1.dp, BorderMuted)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Surface(
                                    modifier = Modifier.size(60.dp),
                                    shape = CircleShape,
                                    color = SoftWarmBeige.copy(alpha = 0.5f)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.NotificationsNone,
                                            contentDescription = null,
                                            tint = DeepTealColor,
                                            modifier = Modifier.size(30.dp)
                                        )
                                    }
                                }

                                Text(
                                    text = "لا توجد إشعارات في هذا التصنيف",
                                    fontFamily = CairoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    color = DarkTextColor,
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Center
                                )

                                Text(
                                    text = "جميع التنبيهات الفورية الخاصة بطلبات الحجز والاعتمادات تظهر هنا فور وصولها.",
                                    fontFamily = CairoFontFamily,
                                    color = DarkTextColor.copy(alpha = 0.65f),
                                    fontSize = 12.5.sp,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 19.sp
                                )
                            }
                        }
                    }
                } else {
                    // =========================================================================
                    // 3. بطاقات الإشعارات المنظمة (بدون أي إطار مزدوج وبمحاذاة 54.dp متناسقة)
                    // =========================================================================
                    items(
                        items = filteredList,
                        key = { it.id }
                    ) { item ->
                        val visual = when (item.type) {
                            NotificationType.ACCEPTED -> NotificationVisual(
                                icon = Icons.Default.CheckCircle,
                                iconBg = DeepTealColor.copy(alpha = 0.12f),
                                iconTint = DeepTealColor,
                                tagText = "تم القبول ✓",
                                tagBg = DeepTealColor.copy(alpha = 0.1f),
                                tagBorder = DeepTealColor.copy(alpha = 0.35f),
                                tagTextColor = DeepTealColor
                            )
                            NotificationType.NEW_REQUEST -> NotificationVisual(
                                icon = Icons.Default.HourglassTop,
                                iconBg = SoftWarmBeige,
                                iconTint = Color(0xFFD97706),
                                tagText = "طلب جديد",
                                tagBg = SoftWarmBeige,
                                tagBorder = Color(0xFFD97706).copy(alpha = 0.35f),
                                tagTextColor = Color(0xFFB45309)
                            )
                            NotificationType.TEAM_LOCKED -> NotificationVisual(
                                icon = Icons.Default.Lock,
                                iconBg = DeepTealColor.copy(alpha = 0.12f),
                                iconTint = DeepTealColor,
                                tagText = "معتمد ومقفل",
                                tagBg = DeepTealColor.copy(alpha = 0.1f),
                                tagBorder = DeepTealColor.copy(alpha = 0.35f),
                                tagTextColor = DeepTealColor
                            )
                            NotificationType.REJECTED -> NotificationVisual(
                                icon = Icons.Default.ErrorOutline,
                                iconBg = Color(0xFFFFECEC),
                                iconTint = Color(0xFFDC2626),
                                tagText = "مرفوض ✕",
                                tagBg = Color(0xFFFFECEC),
                                tagBorder = Color(0xFFDC2626).copy(alpha = 0.35f),
                                tagTextColor = Color(0xFFDC2626)
                            )
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .clickable {
                                    val index = notificationList.indexOfFirst { it.id == item.id }
                                    if (index != -1) {
                                        notificationList[index] = item.copy(isRead = true)
                                    }
                                },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (!item.isRead) DeepTealColor.copy(alpha = 0.03f) else CardWhite
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                            border = BorderStroke(
                                width = if (!item.isRead) 1.5.dp else 1.dp,
                                color = if (!item.isRead) DeepTealColor else BorderMuted
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // 1. رأس كارت الإشعار: أيقونة (42.dp) + عنوان (15.sp) + شارة (26.dp)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        modifier = Modifier.weight(1f),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        // الأيقونة الدائرية (42.dp) على أقصى اليمين
                                        Surface(
                                            modifier = Modifier.size(42.dp),
                                            shape = CircleShape,
                                            color = visual.iconBg,
                                            border = BorderStroke(1.dp, visual.iconTint.copy(alpha = 0.25f))
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Icon(
                                                    imageVector = visual.icon,
                                                    contentDescription = null,
                                                    tint = visual.iconTint,
                                                    modifier = Modifier.size(22.dp)
                                                )
                                            }
                                        }

                                        // العنوان الرئيسي بدون أي إيموجي مكرر
                                        Text(
                                            text = item.title,
                                            fontFamily = CairoFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            color = DarkTextColor,
                                            fontSize = 15.sp,
                                            maxLines = 1,
                                            softWrap = false
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(8.dp))

                                    // شارة الحالة على أقصى اليسار بارتفاع 26.dp في سطر واحد
                                    Surface(
                                        modifier = Modifier.height(26.dp),
                                        shape = RoundedCornerShape(8.dp),
                                        color = visual.tagBg,
                                        border = BorderStroke(0.8.dp, visual.tagBorder)
                                    ) {
                                        Box(
                                            modifier = Modifier.padding(horizontal = 9.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = visual.tagText,
                                                fontFamily = CairoFontFamily,
                                                fontWeight = FontWeight.Bold,
                                                color = visual.tagTextColor,
                                                fontSize = 11.sp,
                                                maxLines = 1,
                                                softWrap = false
                                            )
                                        }
                                    }
                                }

                                // 2. نص الإشعار بمحاذاة 54.dp ليبدأ أسفل العنوان مباشرة
                                Text(
                                    text = item.message,
                                    modifier = Modifier.padding(start = 54.dp),
                                    fontFamily = CairoFontFamily,
                                    fontWeight = FontWeight.Normal,
                                    color = DarkTextColor.copy(alpha = 0.85f),
                                    fontSize = 13.sp,
                                    lineHeight = 19.5.sp
                                )

                                // 3. أسفل الكارت: الوقت + نقطة الإشعار غير المقروء (8.dp)
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 54.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Schedule,
                                            contentDescription = null,
                                            tint = DeepTealColor,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Text(
                                            text = item.timeAgo,
                                            fontFamily = CairoFontFamily,
                                            fontWeight = FontWeight.Medium,
                                            color = DarkTextColor.copy(alpha = 0.6f),
                                            fontSize = 11.5.sp,
                                            maxLines = 1,
                                            softWrap = false
                                        )
                                    }

                                    if (!item.isRead) {
                                        Surface(
                                            modifier = Modifier.size(8.dp),
                                            shape = CircleShape,
                                            color = DeepTealColor
                                        ) {}
                                    }
                                }
                            }
                        }
                    }
                }

                // =========================================================================
                // 4. زر مسح جميع الإشعارات بالكامل
                // =========================================================================
                if (notificationList.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedButton(
                            onClick = { showClearConfirmDialog = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(14.dp),
                            border = BorderStroke(1.dp, Color(0xFFEF5350).copy(alpha = 0.6f)),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = CardWhite,
                                contentColor = Color(0xFFD32F2F)
                            )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DeleteOutline,
                                    contentDescription = null,
                                    tint = Color(0xFFD32F2F),
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = "مسح جميع الإشعارات",
                                    fontFamily = CairoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.5.sp,
                                    color = Color(0xFFD32F2F),
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }

            // نافذة تأكيد مسح الإشعارات
            if (showClearConfirmDialog) {
                AlertDialog(
                    onDismissRequest = { showClearConfirmDialog = false },
                    containerColor = CardWhite,
                    shape = RoundedCornerShape(20.dp),
                    title = {
                        Text(
                            text = "مسح جميع الإشعارات",
                            fontFamily = CairoFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = DarkTextColor,
                            fontSize = 17.sp
                        )
                    },
                    text = {
                        Text(
                            text = "هل أنت متأكد من مسح قائمة التنبيهات بالكامل؟ لن تتمكن من استرجاعها بعد الحذف.",
                            fontFamily = CairoFontFamily,
                            fontWeight = FontWeight.Normal,
                            color = DarkTextColor.copy(alpha = 0.75f),
                            fontSize = 13.5.sp
                        )
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                showClearConfirmDialog = false
                                notificationList.clear()
                                scope.launch {
                                    snackbarHostState.showSnackbar("تم مسح قائمة الإشعارات بالكامل 🗑️")
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = "مسح الكل",
                                fontFamily = CairoFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showClearConfirmDialog = false }) {
                            Text(
                                text = "إلغاء",
                                fontFamily = CairoFontFamily,
                                fontWeight = FontWeight.Medium,
                                color = DarkTextColor
                            )
                        }
                    }
                )
            }
        }
    }
}

/**
 * بنية بيانات لنمط وشارة كل نوع إشعار
 */
private data class NotificationVisual(
    val icon: ImageVector,
    val iconBg: Color,
    val iconTint: Color,
    val tagText: String,
    val tagBg: Color,
    val tagBorder: Color,
    val tagTextColor: Color
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun NotificationsScreenPrecisionPreview() {
    TeamSeatTheme {
        NotificationsScreen()
    }
}
