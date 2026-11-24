package com.stackmates.teamseat.Screens.requests_hossam

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MarkEmailRead
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stackmates.teamseat.core.data.MockDataProvider
import com.stackmates.teamseat.core.model.NotificationItem
import com.stackmates.teamseat.core.model.NotificationType
import com.stackmates.teamseat.core.theme.DeepNavy
import com.stackmates.teamseat.core.theme.DeepTeal
import com.stackmates.teamseat.core.theme.LightAqua
import com.stackmates.teamseat.core.theme.Peach
import com.stackmates.teamseat.core.theme.SeatAvailableBg
import com.stackmates.teamseat.core.theme.SoftBeige
import com.stackmates.teamseat.core.theme.SoftGray
import com.stackmates.teamseat.core.theme.TeamSeatTheme
import com.stackmates.teamseat.core.theme.Turquoise
import com.stackmates.teamseat.core.theme.WarmCream
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * ⚡ المسؤول عن الشاشة: حسام (Hossam) — [HOSSAM-4]
 * شاشة مركز الإشعارات الفورية (Notifications Screen)
 * تصميم متسق مع هوية TeamSeat الرسمية وألوانها المتناسقة
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

    Scaffold(
        containerColor = WarmCream,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "مركز الإشعارات",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = DeepNavy,
                                fontSize = 21.sp
                            )
                        )
                        Text(
                            text = "التنبيهات الفورية وحالة طلباتك",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = DeepTeal,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp
                            )
                        )
                    }
                },
                navigationIcon = {
                    Surface(
                        modifier = Modifier
                            .padding(start = 12.dp, end = 8.dp)
                            .size(42.dp),
                        shape = CircleShape,
                        color = Color.White,
                        shadowElevation = 2.dp,
                        border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.5f))
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
                                        delay(3000)
                                        showMarkedAllBanner = false
                                    }
                                },
                            shape = RoundedCornerShape(18.dp),
                            color = LightAqua.copy(alpha = 0.5f),
                            border = BorderStroke(1.dp, DeepTeal.copy(alpha = 0.3f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MarkEmailRead,
                                    contentDescription = "قراءة الكل",
                                    tint = DeepTeal,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "مقروء",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = DeepTeal
                                    )
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
            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            // =========================================================================
            // 0. إشعار تم التحديد كمقروء (Banner)
            // =========================================================================
            item {
                AnimatedVisibility(
                    visible = showMarkedAllBanner,
                    enter = slideInVertically() + fadeIn(),
                    exit = slideOutVertically() + fadeOut()
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = SeatAvailableBg),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, DeepTeal.copy(alpha = 0.4f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(30.dp)
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
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "تم تعليم كافة التنبيهات كمقروءة بنجاح ✨",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = DeepNavy
                                )
                            )
                        }
                    }
                }
            }

            // =========================================================================
            // 1. ملخص الإشعارات وأشرطة الفلترة (Summary & Filter Chips Bar)
            // =========================================================================
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.4f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(LightAqua.copy(alpha = 0.5f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.NotificationsActive,
                                        contentDescription = null,
                                        tint = DeepTeal,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "جميع التنبيهات",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            color = DeepNavy,
                                            fontSize = 17.sp
                                        )
                                    )
                                    Text(
                                        text = if (unreadCount > 0) "لديك $unreadCount تنبيهات غير مقروءة" else "جميع الإشعارات مقروءة",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = DeepNavy.copy(alpha = 0.65f),
                                            fontSize = 12.sp
                                        )
                                    )
                                }
                            }

                            if (unreadCount > 0) {
                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = Peach,
                                    border = BorderStroke(1.dp, DeepNavy.copy(alpha = 0.15f))
                                ) {
                                    Text(
                                        text = "$unreadCount جديد",
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = DeepNavy,
                                            fontSize = 12.sp
                                        )
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // أشرطة الفلترة السريعة
                        val filterOptions = listOf("الكل", "قبول 🎉", "طلبات 📩", "اعتماد 🔒", "مرفوض ❌")
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(filterOptions) { filter ->
                                val isSelected = selectedFilter == filter
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { selectedFilter = filter },
                                    label = {
                                        Text(
                                            text = filter,
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                                fontSize = 13.5.sp
                                            )
                                        )
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = DeepTeal,
                                        selectedLabelColor = WarmCream,
                                        containerColor = SoftBeige.copy(alpha = 0.6f),
                                        labelColor = DeepNavy
                                    ),
                                    border = FilterChipDefaults.filterChipBorder(
                                        enabled = true,
                                        selected = isSelected,
                                        borderColor = SoftGray,
                                        selectedBorderColor = DeepTeal,
                                        borderWidth = 1.dp
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                )
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
                            .padding(vertical = 20.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.4f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(72.dp)
                                    .clip(CircleShape)
                                    .background(LightAqua.copy(alpha = 0.4f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.NotificationsNone,
                                    contentDescription = null,
                                    tint = DeepTeal,
                                    modifier = Modifier.size(38.dp)
                                )
                            }

                            Text(
                                text = "لا توجد إشعارات حالياً",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = DeepNavy,
                                    fontSize = 20.sp
                                ),
                                textAlign = TextAlign.Center
                            )

                            Text(
                                text = "جميع التنبيهات الفورية الخاصة بطلبات الحجز واعتمدات الفرق ستظهر لك هنا فور وصولها.",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = DeepNavy.copy(alpha = 0.7f),
                                    lineHeight = 22.sp
                                ),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                // =========================================================================
                // 3. قائمة الإشعارات (Notification Cards List)
                // =========================================================================
                items(
                    items = filteredList,
                    key = { it.id }
                ) { item ->
                    val style = when (item.type) {
                        NotificationType.ACCEPTED -> NotificationIconStyle(
                            Icons.Default.CheckCircle,
                            LightAqua.copy(alpha = 0.6f),
                            DeepTeal,
                            "تم القبول 🎉",
                            SeatAvailableBg
                        )
                        NotificationType.NEW_REQUEST -> NotificationIconStyle(
                            Icons.Default.HourglassTop,
                            Peach.copy(alpha = 0.5f),
                            Color(0xFFD84315),
                            "طلب حجز جديد 📩",
                            Peach.copy(alpha = 0.4f)
                        )
                        NotificationType.TEAM_LOCKED -> NotificationIconStyle(
                            Icons.Default.Lock,
                            LightAqua.copy(alpha = 0.5f),
                            DeepNavy,
                            "اعتماد التيم 🔒",
                            LightAqua.copy(alpha = 0.5f)
                        )
                        NotificationType.REJECTED -> NotificationIconStyle(
                            Icons.Default.ErrorOutline,
                            Color(0xFFFFF0F0),
                            Color(0xFFD32F2F),
                            "طلب مرفوض ❌",
                            Color(0xFFFCDDEC)
                        )
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val index = notificationList.indexOfFirst { it.id == item.id }
                                if (index != -1) {
                                    notificationList[index] = item.copy(isRead = true)
                                }
                            },
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (!item.isRead) LightAqua.copy(alpha = 0.15f) else Color.White
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        border = BorderStroke(
                            width = if (!item.isRead) 1.5.dp else 1.dp,
                            color = if (!item.isRead) DeepTeal.copy(alpha = 0.5f) else SoftGray.copy(alpha = 0.4f)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            // أيكونة نوع الإشعار
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(style.iconBg),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = style.icon,
                                    contentDescription = null,
                                    tint = style.iconTint,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            // المحتوى التفصيلي
                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = item.title,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            color = DeepNavy,
                                            fontSize = 16.5.sp
                                        ),
                                        modifier = Modifier.weight(1f)
                                    )

                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = style.tagBg
                                    ) {
                                        Text(
                                            text = style.typeTag,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = DeepNavy,
                                                fontSize = 11.5.sp
                                            )
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = item.message,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = DeepNavy.copy(alpha = 0.82f),
                                        fontSize = 14.sp,
                                        lineHeight = 21.sp
                                    )
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Schedule,
                                            contentDescription = null,
                                            tint = Turquoise,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = item.timeAgo,
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = DeepNavy.copy(alpha = 0.6f),
                                                fontSize = 12.sp
                                            )
                                        )
                                    }

                                    if (!item.isRead) {
                                        Surface(
                                            shape = CircleShape,
                                            color = Turquoise,
                                            modifier = Modifier.size(8.dp)
                                        ) {}
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // =========================================================================
            // 4. زر تفريغ الإشعارات (Clear All Notifications Button)
            // =========================================================================
            if (notificationList.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = {
                            notificationList.clear()
                            scope.launch {
                                snackbarHostState.showSnackbar("تم مسح قائمة الإشعارات بالكامل 🗑️")
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFFD32F2F)
                        ),
                        border = BorderStroke(1.dp, Color(0xFFE53935).copy(alpha = 0.4f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Cancel,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "مسح جميع الإشعارات",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFD32F2F),
                                fontSize = 14.5.sp
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

private data class NotificationIconStyle(
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val iconBg: Color,
    val iconTint: Color,
    val typeTag: String,
    val tagBg: Color
)

@Preview(showBackground = true)
@Composable
fun NotificationsScreenPreview() {
    TeamSeatTheme {
        NotificationsScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationsScreenEmptyPreview() {
    TeamSeatTheme {
        NotificationsScreen(
            notifications = emptyList()
        )
    }
}
