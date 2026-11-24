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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stackmates.teamseat.core.data.MockDataProvider
import com.stackmates.teamseat.core.model.NotificationItem
import com.stackmates.teamseat.core.model.NotificationType
import com.stackmates.teamseat.core.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * ⚡ المسؤول عن الشاشة: حسام (Hossam) — [HOSSAM-4]
 * شاشة مركز الإشعارات الفورية (Notifications Screen)
 * معاد تصميمها بالكامل وفق هوية ألوان TeamSeat الرسمية (Color.kt)
 * مع خط Cairo ونظام الكروت النظيفة المريحة للعين بدون أي تداخل أو تدرجات باهتة.
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
        "قبول 🎉" -> notificationList.filter { it.type == NotificationType.ACCEPTED }
        "طلبات 📩" -> notificationList.filter { it.type == NotificationType.NEW_REQUEST }
        "اعتماد 🔒" -> notificationList.filter { it.type == NotificationType.TEAM_LOCKED }
        "مرفوض ❌" -> notificationList.filter { it.type == NotificationType.REJECTED }
        else -> notificationList
    }

    val unreadCount = notificationList.count { !it.isRead }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Scaffold(
            containerColor = WarmCream,
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = "مركز الإشعارات",
                                fontFamily = CairoFontFamily,
                                fontWeight = FontWeight.ExtraBold,
                                color = DeepNavy,
                                fontSize = 19.sp,
                                maxLines = 1,
                                softWrap = false
                            )
                            Text(
                                text = "التنبيهات الفورية وحالة طلباتك",
                                fontFamily = CairoFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                color = DeepTeal,
                                fontSize = 12.5.sp,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    },
                    navigationIcon = {
                        Surface(
                            modifier = Modifier
                                .padding(start = 12.dp, end = 6.dp)
                                .size(42.dp),
                            shape = CircleShape,
                            color = Color.White,
                            shadowElevation = 2.dp,
                            border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.6f))
                        ) {
                            IconButton(onClick = onBackClick) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "رجوع",
                                    tint = DeepNavy,
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
                                color = LightAqua.copy(alpha = 0.5f),
                                border = BorderStroke(1.dp, Turquoise.copy(alpha = 0.6f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DoneAll,
                                        contentDescription = "قراءة الكل",
                                        tint = DeepTeal,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "مقروء",
                                        fontFamily = CairoFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        color = DeepTeal,
                                        fontSize = 12.sp,
                                        maxLines = 1,
                                        softWrap = false
                                    )
                                }
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = WarmCream)
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {

                // =========================================================================
                // 0. بانر التحديد كمقروء (Success Banner)
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
                            color = LightAqua.copy(alpha = 0.35f),
                            border = BorderStroke(1.dp, Turquoise.copy(alpha = 0.6f))
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
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(DeepTeal),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Text(
                                    text = "تم تعليم كافة التنبيهات كمقروءة بنجاح ✓",
                                    fontFamily = CairoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepNavy,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }

                // =========================================================================
                // 1. بطاقة ملخص الإشعارات وفلاتر التصفية
                // =========================================================================
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.5f))
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
                                        modifier = Modifier.size(42.dp),
                                        shape = CircleShape,
                                        color = LightAqua.copy(alpha = 0.45f),
                                        border = BorderStroke(1.dp, Turquoise.copy(alpha = 0.4f))
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                imageVector = Icons.Default.NotificationsActive,
                                                contentDescription = null,
                                                tint = DeepTeal,
                                                modifier = Modifier.size(22.dp)
                                            )
                                        }
                                    }

                                    Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                                        Text(
                                            text = "جميع التنبيهات",
                                            fontFamily = CairoFontFamily,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = DeepNavy,
                                            fontSize = 16.5.sp,
                                            maxLines = 1,
                                            softWrap = false
                                        )
                                        Text(
                                            text = if (unreadCount > 0) "لديك $unreadCount تنبيهات غير مقروءة" else "جميع الإشعارات مقروءة",
                                            fontFamily = CairoFontFamily,
                                            fontWeight = FontWeight.Medium,
                                            color = DeepNavy.copy(alpha = 0.65f),
                                            fontSize = 12.sp,
                                            maxLines = 1,
                                            softWrap = false
                                        )
                                    }
                                }

                                if (unreadCount > 0) {
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = Peach.copy(alpha = 0.5f),
                                        border = BorderStroke(1.dp, Peach)
                                    ) {
                                        Text(
                                            text = "$unreadCount جديد",
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                            fontFamily = CairoFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            color = DeepNavy,
                                            fontSize = 11.5.sp,
                                            maxLines = 1,
                                            softWrap = false
                                        )
                                    }
                                }
                            }

                            HorizontalDivider(color = SoftGray.copy(alpha = 0.4f), thickness = 0.8.dp)

                            // أشرطة الفلترة السريعة
                            val filterOptions = listOf("الكل", "قبول 🎉", "طلبات 📩", "اعتماد 🔒", "مرفوض ❌")
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(filterOptions) { filter ->
                                    val isSelected = selectedFilter == filter
                                    Surface(
                                        modifier = Modifier
                                            .height(34.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .clickable { selectedFilter = filter },
                                        shape = RoundedCornerShape(12.dp),
                                        color = if (isSelected) DeepTeal else SoftBeige.copy(alpha = 0.5f),
                                        border = BorderStroke(
                                            width = 1.dp,
                                            color = if (isSelected) DeepTeal else SoftGray.copy(alpha = 0.6f)
                                        )
                                    ) {
                                        Box(
                                            modifier = Modifier.padding(horizontal = 12.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = filter,
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
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.5f))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Surface(
                                    modifier = Modifier.size(64.dp),
                                    shape = CircleShape,
                                    color = LightAqua.copy(alpha = 0.4f)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.NotificationsNone,
                                            contentDescription = null,
                                            tint = DeepTeal,
                                            modifier = Modifier.size(32.dp)
                                        )
                                    }
                                }

                                Text(
                                    text = "لا توجد إشعارات في هذا التصنيف",
                                    fontFamily = CairoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepNavy,
                                    fontSize = 16.5.sp,
                                    textAlign = TextAlign.Center
                                )

                                Text(
                                    text = "جميع التنبيهات الفورية الخاصة بطلبات الحجز والاعتمادات تظهر هنا فور وصولها.",
                                    fontFamily = CairoFontFamily,
                                    color = DeepNavy.copy(alpha = 0.7f),
                                    fontSize = 13.sp,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 20.sp
                                )
                            }
                        }
                    }
                } else {
                    // =========================================================================
                    // 3. بطاقات الإشعارات النظيفة الفاخرة (Clean White Cards)
                    // =========================================================================
                    items(
                        items = filteredList,
                        key = { it.id }
                    ) { item ->
                        val (icon, iconBg, iconTint, tagText, tagBg, tagTextColor) = when (item.type) {
                            NotificationType.ACCEPTED -> NotificationVisual(
                                icon = Icons.Default.CheckCircle,
                                iconBg = LightAqua.copy(alpha = 0.45f),
                                iconTint = DeepTeal,
                                tagText = "تم القبول ✓",
                                tagBg = LightAqua.copy(alpha = 0.4f),
                                tagTextColor = DeepTeal
                            )
                            NotificationType.NEW_REQUEST -> NotificationVisual(
                                icon = Icons.Default.HourglassTop,
                                iconBg = Peach.copy(alpha = 0.45f),
                                iconTint = DeepNavy,
                                tagText = "طلب جديد ⏳",
                                tagBg = Peach.copy(alpha = 0.4f),
                                tagTextColor = DeepNavy
                            )
                            NotificationType.TEAM_LOCKED -> NotificationVisual(
                                icon = Icons.Default.Lock,
                                iconBg = SoftBeige.copy(alpha = 0.6f),
                                iconTint = DeepNavy,
                                tagText = "معتمد ومقفل 🔒",
                                tagBg = SoftBeige.copy(alpha = 0.7f),
                                tagTextColor = DeepNavy
                            )
                            NotificationType.REJECTED -> NotificationVisual(
                                icon = Icons.Default.ErrorOutline,
                                iconBg = Color(0xFFFFEBEE),
                                iconTint = Color(0xFFC62828),
                                tagText = "طلب مرفوض ✕",
                                tagBg = Color(0xFFFFEBEE),
                                tagTextColor = Color(0xFFC62828)
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
                                containerColor = if (!item.isRead) LightAqua.copy(alpha = 0.1f) else Color.White
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
                            border = BorderStroke(
                                width = if (!item.isRead) 1.5.dp else 1.dp,
                                color = if (!item.isRead) Turquoise.copy(alpha = 0.8f) else SoftGray.copy(alpha = 0.5f)
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                // أيقونة الإشعار الدائرية
                                Surface(
                                    modifier = Modifier.size(44.dp),
                                    shape = CircleShape,
                                    color = iconBg,
                                    border = BorderStroke(1.dp, iconTint.copy(alpha = 0.25f))
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = icon,
                                            contentDescription = null,
                                            tint = iconTint,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                }

                                // تفاصيل الإشعار
                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = item.title,
                                            fontFamily = CairoFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            color = DeepNavy,
                                            fontSize = 14.5.sp,
                                            maxLines = 1,
                                            softWrap = false,
                                            modifier = Modifier.weight(1f)
                                        )

                                        Spacer(modifier = Modifier.width(6.dp))

                                        // شارة نوع الإشعار
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = tagBg,
                                            border = BorderStroke(0.6.dp, tagTextColor.copy(alpha = 0.3f))
                                        ) {
                                            Text(
                                                text = tagText,
                                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.5.dp),
                                                fontFamily = CairoFontFamily,
                                                fontWeight = FontWeight.Bold,
                                                color = tagTextColor,
                                                fontSize = 11.sp,
                                                maxLines = 1,
                                                softWrap = false
                                            )
                                        }
                                    }

                                    Text(
                                        text = item.message,
                                        fontFamily = CairoFontFamily,
                                        fontWeight = FontWeight.Normal,
                                        color = DeepNavy.copy(alpha = 0.8f),
                                        fontSize = 12.5.sp,
                                        lineHeight = 18.5.sp
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

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
                                                imageVector = Icons.Default.Schedule,
                                                contentDescription = null,
                                                tint = Turquoise,
                                                modifier = Modifier.size(13.dp)
                                            )
                                            Text(
                                                text = item.timeAgo,
                                                fontFamily = CairoFontFamily,
                                                fontWeight = FontWeight.Medium,
                                                color = DeepNavy.copy(alpha = 0.6f),
                                                fontSize = 11.sp,
                                                maxLines = 1,
                                                softWrap = false
                                            )
                                        }

                                        if (!item.isRead) {
                                            Surface(
                                                shape = CircleShape,
                                                color = Turquoise,
                                                modifier = Modifier.size(7.dp)
                                            ) {}
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // =========================================================================
                // 4. زر مسح كافة الإشعارات (Clear All Action)
                // =========================================================================
                if (notificationList.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedButton(
                            onClick = { showClearConfirmDialog = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp),
                            shape = RoundedCornerShape(14.dp),
                            border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.6f)),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = Color.White,
                                contentColor = DeepNavy
                            )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DeleteOutline,
                                    contentDescription = null,
                                    tint = Color(0xFFC62828),
                                    modifier = Modifier.size(17.dp)
                                )
                                Text(
                                    text = "مسح جميع الإشعارات",
                                    fontFamily = CairoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Color(0xFFC62828),
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
                    containerColor = Color.White,
                    shape = RoundedCornerShape(20.dp),
                    title = {
                        Text(
                            text = "مسح جميع الإشعارات",
                            fontFamily = CairoFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = DeepNavy,
                            fontSize = 17.sp
                        )
                    },
                    text = {
                        Text(
                            text = "هل أنت متأكد من مسح قائمة التنبيهات بالكامل؟ لا يمكن استرجاعها بعد الحذف.",
                            fontFamily = CairoFontFamily,
                            fontWeight = FontWeight.Normal,
                            color = DeepNavy.copy(alpha = 0.75f),
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
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828)),
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
                                color = DeepNavy
                            )
                        }
                    }
                )
            }
        }
    }
}

/**
 * بنية بيانات لنمط كل نوع إشعار
 */
private data class NotificationVisual(
    val icon: ImageVector,
    val iconBg: Color,
    val iconTint: Color,
    val tagText: String,
    val tagBg: Color,
    val tagTextColor: Color
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun NotificationsScreenEnhancedPreview() {
    TeamSeatTheme {
        NotificationsScreen()
    }
}
