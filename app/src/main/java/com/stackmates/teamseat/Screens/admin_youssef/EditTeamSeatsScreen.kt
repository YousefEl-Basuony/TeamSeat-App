package com.stackmates.teamseat.Screens.admin_youssef

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.outlined.AirlineSeatReclineNormal
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.FormatQuote
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.HourglassTop
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.material.icons.outlined.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stackmates.teamseat.core.data.MockDataProvider
import com.stackmates.teamseat.core.model.Seat
import com.stackmates.teamseat.core.model.SeatStatus
import com.stackmates.teamseat.core.theme.DeepNavy
import com.stackmates.teamseat.core.theme.DeepTeal
import com.stackmates.teamseat.core.theme.LightAqua
import com.stackmates.teamseat.core.theme.Peach
import com.stackmates.teamseat.core.theme.SeatAvailableBg
import com.stackmates.teamseat.core.theme.SeatAvailableGreen
import com.stackmates.teamseat.core.theme.SeatBookedBg
import com.stackmates.teamseat.core.theme.SeatBookedBlue
import com.stackmates.teamseat.core.theme.SeatPendingAmber
import com.stackmates.teamseat.core.theme.SeatPendingBg
import com.stackmates.teamseat.core.theme.SoftBeige
import com.stackmates.teamseat.core.theme.SoftGray
import com.stackmates.teamseat.core.theme.TajawalFontFamily
import com.stackmates.teamseat.core.theme.TeamSeatTheme
import com.stackmates.teamseat.core.theme.Turquoise
import com.stackmates.teamseat.core.theme.WarmCream

/**
 * 💻 المسؤول عن الشاشة: يوسف (Youssef - Team Lead & Backend)
 * شاشة إدارة وتعديل مقاعد الفريق — مقاييس بصرية مريحة جداً ومكبرة للقراءة السلسة
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTeamSeatsScreen(
    seats: List<Seat> = MockDataProvider.sampleSeats,
    onSaveSeatsClick: (List<Seat>) -> Unit = {},
    onDeleteSeatClick: (seatId: String) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val editableSeats = remember { mutableStateListOf(*seats.toTypedArray()) }
    var showSavedMessage by remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableStateOf("الكل") }



     val totalCount = editableSeats.size
    val bookedCount = editableSeats.count { it.status == SeatStatus.BOOKED }
    val availableCount = editableSeats.count { it.status == SeatStatus.AVAILABLE }
    val pendingCount = editableSeats.count { it.status == SeatStatus.PENDING }
    val progressPercent = if (totalCount > 0) ((bookedCount.toFloat() / totalCount) * 100).toInt() else 0

    // الفلترة التفاعلية
    val filteredSeats = when (selectedFilter) {
        "المتاحة" -> editableSeats.filter { it.status == SeatStatus.AVAILABLE }
        "المعتمدة" -> editableSeats.filter { it.status == SeatStatus.BOOKED }
        "قيد الانتظار" -> editableSeats.filter { it.status == SeatStatus.PENDING }
        else -> editableSeats
    }

    val rolePresets = listOf(
        "Android Developer",
        "Backend Developer",
        "UI/UX Designer",
        "AI / ML Engineer",
        "Flutter Developer",
        "QA & Testing",
        "DevOps Engineer"
    )

    Scaffold(
        containerColor = WarmCream,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "إدارة مقاعد الفريق",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = DeepNavy,
                                fontSize = 21.sp
                            )
                        )
                        Text(
                            text = "تخصيص الأدوار ومراجعة شواغر المشروع",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = DeepTeal,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            )
                        )
                    }
                },
                navigationIcon = {
                    Surface(
                        modifier = Modifier
                            .padding(start = 12.dp, end = 8.dp)
                            .size(44.dp),
                        shape = CircleShape,
                        color = Color.White,
                        shadowElevation = 2.dp,
                        border = androidx.compose.foundation.BorderStroke(1.dp, SoftGray.copy(alpha = 0.6f))
                    ) {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "رجوع",
                                tint = DeepNavy,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = WarmCream
                )
            )
        },
        bottomBar = {
            Surface(
                color = Color.White,
                shadowElevation = 16.dp,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Button(
                        onClick = {
                            onSaveSeatsClick(editableSeats)
                            showSavedMessage = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                        shape = RoundedCornerShape(28.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (showSavedMessage) "تم حفظ وتحديث المقاعد بنجاح ✓" else "حفظ ومزامنة المقاعد",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 17.sp
                            )
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            // إشعار تأكيد الحفظ
            AnimatedVisibility(
                visible = showSavedMessage,
                enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically() + fadeOut()
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = SeatAvailableBg),
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DeepTeal.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = DeepTeal,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "تم تحديث خريطة مقاعد الفريق بنجاح!",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = DeepNavy,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        )
                    }
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(top = 10.dp, bottom = 28.dp)
            ) {

                // =============================================================
                // 1. كارت ملخص الفريق الاحترافي (Hero Metric Dashboard 2x2 Grid)
                // =============================================================
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SoftGray.copy(alpha = 0.5f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                        ) {
                            // الصف العلوي مع مؤشر الاكتمال الدائري الكبير
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = SoftBeige,
                                        modifier = Modifier.padding(bottom = 6.dp)
                                    ) {
                                        Text(
                                            text = "مشروع تخرج حاسبات 2026",
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                            style = MaterialTheme.typography.labelMedium.copy(
                                                color = DeepTeal,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.5.sp
                                            )
                                        )
                                    }
                                    Text(
                                        text = "StackMates (RoleSpot)",
                                        style = MaterialTheme.typography.titleLarge.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            color = DeepNavy,
                                            fontSize = 20.sp
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "تم اعتماد $bookedCount من أصل $totalCount مقاعد",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = Color.Gray,
                                            fontWeight = FontWeight.Medium,
                                            fontSize = 14.5.sp
                                        )
                                    )
                                }

                                // حلقة التقدم الدائرية بحجم مريح للقراءة
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.size(64.dp)
                                ) {
                                    CircularProgressIndicator(
                                        progress = { if (totalCount > 0) bookedCount.toFloat() / totalCount else 0f },
                                        modifier = Modifier.fillMaxSize(),
                                        color = DeepTeal,
                                        trackColor = SoftBeige,
                                        strokeWidth = 6.dp
                                    )
                                    Text(
                                        text = "$progressPercent%",
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            color = DeepNavy,
                                            fontSize = 14.sp
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))

                            // شبكة الإحصائيات 2x2: مريحة وواسعة جداً للعين بدون ضغط
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    SpaciousMetricCard(
                                        modifier = Modifier.weight(1f),
                                        title = "إجمالي السعة",
                                        value = "$totalCount مقاعد",
                                        icon = Icons.Outlined.Groups,
                                        iconTint = DeepNavy,
                                        iconBg = SoftBeige
                                    )
                                    SpaciousMetricCard(
                                        modifier = Modifier.weight(1f),
                                        title = "أعضاء معتمدين",
                                        value = "$bookedCount طلاب",
                                        icon = Icons.Outlined.VerifiedUser,
                                        iconTint = DeepTeal,
                                        iconBg = SeatBookedBg
                                    )
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    SpaciousMetricCard(
                                        modifier = Modifier.weight(1f),
                                        title = "مقاعد شاغرة",
                                        value = "$availableCount متبقي",
                                        icon = Icons.Outlined.AirlineSeatReclineNormal,
                                        iconTint = Turquoise,
                                        iconBg = SeatAvailableBg
                                    )
                                    SpaciousMetricCard(
                                        modifier = Modifier.weight(1f),
                                        title = "قيد الانتظار",
                                        value = "$pendingCount طلبات",
                                        icon = Icons.Outlined.HourglassTop,
                                        iconTint = SeatPendingAmber,
                                        iconBg = SeatPendingBg
                                    )
                                }
                            }
                        }
                    }
                }

                // =============================================================
                // 2. كبسولات التصفية الأنيقة (Filter Segmented Bar)
                // =============================================================
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        listOf("الكل", "المتاحة", "المعتمدة", "قيد الانتظار").forEach { filter ->
                            val isSelected = selectedFilter == filter
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = if (isSelected) DeepTeal else Color.White,
                                border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, SoftGray.copy(alpha = 0.8f)),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .clickable { selectedFilter = filter }
                            ) {
                                Text(
                                    text = filter,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 9.dp),
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = if (isSelected) Color.White else DeepNavy,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                        fontSize = 14.sp
                                    )
                                )
                            }
                        }
                    }
                }

                // =============================================================
                // 3. قائمة كروت المقاعد المتقنة (Ticket-Style Seat Cards)
                // =============================================================
                items(filteredSeats, key = { it.id }) { seat ->
                    TicketSeatCard(
                        seat = seat,
                        rolePresets = rolePresets,
                        onRoleChange = { newRole ->
                            val index = editableSeats.indexOfFirst { it.id == seat.id }
                            if (index != -1) {
                                editableSeats[index] = seat.copy(roleRequired = newRole)
                            }
                        },
                        onDeleteClick = {
                            editableSeats.removeIf { it.id == seat.id }
                            onDeleteSeatClick(seat.id)
                        }
                    )
                }

                // =============================================================
                // 4. زر إضافة مقعد جديد للتيم
                // =============================================================
                if (editableSeats.size < 8) {
                    item {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(20.dp))
                                .clickable {
                                    val newSeatNumber = (editableSeats.maxOfOrNull { it.seatNumber } ?: 0) + 1
                                    val newSeat = Seat(
                                        id = "seat_custom_${System.currentTimeMillis()}",
                                        teamId = MockDataProvider.sampleTeam.teamId,
                                        seatNumber = newSeatNumber,
                                        roleRequired = "Android Developer",
                                        status = SeatStatus.AVAILABLE
                                    )
                                    editableSeats.add(newSeat)
                                },
                            shape = RoundedCornerShape(20.dp),
                            color = Color.White,
                            border = androidx.compose.foundation.BorderStroke(1.5.dp, DeepTeal.copy(alpha = 0.5f))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 18.dp, horizontal = 22.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = LightAqua,
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.Add,
                                            contentDescription = null,
                                            tint = DeepTeal,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "إضافة مقعد شاغر جديد للفريق (حتى 8 مقاعد)",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = DeepTeal,
                                        fontSize = 15.5.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// =============================================================================
// مكون بطاقة المقعد الفاخرة (TicketSeatCard)
// =============================================================================
@Composable
private fun TicketSeatCard(
    seat: Seat,
    rolePresets: List<String>,
    onRoleChange: (String) -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, SoftGray.copy(alpha = 0.6f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // الهيدر: لوحة رقم المقعد + شارة القائد + شارة الحالة
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // لوحة رقم المقعد المريحة
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = when (seat.status) {
                            SeatStatus.BOOKED -> SeatBookedBg
                            SeatStatus.PENDING -> SeatPendingBg
                            SeatStatus.AVAILABLE -> SeatAvailableBg
                            SeatStatus.LOCKED -> SoftBeige
                        },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = String.format("%02d", seat.seatNumber),
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = when (seat.status) {
                                        SeatStatus.BOOKED -> DeepNavy
                                        SeatStatus.PENDING -> SeatPendingAmber
                                        SeatStatus.AVAILABLE -> DeepTeal
                                        SeatStatus.LOCKED -> Color.Gray
                                    },
                                    fontSize = 18.sp
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "المقعد رقم ${seat.seatNumber}",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = DeepNavy,
                                    fontSize = 16.5.sp
                                )
                            )
                            if (seat.isLeader) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    color = SoftBeige,
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.WorkspacePremium,
                                            contentDescription = null,
                                            tint = DeepNavy,
                                            modifier = Modifier.size(15.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "قائد الفريق",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = DeepNavy,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 11.5.sp
                                            )
                                        )
                                    }
                                }
                            }
                        }
                        Text(
                            text = "الرمز التعريفي: S${seat.seatNumber}",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color.Gray,
                                fontSize = 13.sp
                            )
                        )
                    }
                }

                // كبسولة الحالة المصممة بأيقونة متجهة
                when (seat.status) {
                    SeatStatus.BOOKED -> HighTierBadge(
                        label = "معتمد",
                        icon = Icons.Default.Verified,
                        tint = DeepNavy,
                        bgColor = SeatBookedBg
                    )
                    SeatStatus.PENDING -> HighTierBadge(
                        label = "قيد المراجعة",
                        icon = Icons.Outlined.Schedule,
                        tint = SeatPendingAmber,
                        bgColor = SeatPendingBg
                    )
                    SeatStatus.AVAILABLE -> HighTierBadge(
                        label = "متاح للحجز",
                        icon = Icons.Outlined.CheckCircle,
                        tint = DeepTeal,
                        bgColor = SeatAvailableBg
                    )
                    SeatStatus.LOCKED -> HighTierBadge(
                        label = "مقفل",
                        icon = Icons.Default.Lock,
                        tint = Color.Gray,
                        bgColor = SoftBeige
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // =================================================================
            // تفاصيل المقعد حسب حالته
            // =================================================================
            when (seat.status) {
                SeatStatus.BOOKED -> {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = WarmCream.copy(alpha = 0.8f),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SoftGray.copy(alpha = 0.5f))
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // صورة أو أحرف الطالب المعتمد
                            Surface(
                                shape = CircleShape,
                                color = DeepNavy,
                                modifier = Modifier.size(42.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    val initials = seat.applicantName?.take(2) ?: "ط"
                                    Text(
                                        text = initials,
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = seat.applicantName ?: "طالب معتمد",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = DeepNavy,
                                        fontSize = 15.5.sp
                                    )
                                )
                                Text(
                                    text = "التخصص: ${seat.roleRequired}",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = DeepTeal,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 13.5.sp
                                    )
                                )
                            }

                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "مقعد مثبت",
                                tint = SoftGray,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                SeatStatus.PENDING -> {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = SeatPendingBg.copy(alpha = 0.7f),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Peach.copy(alpha = 0.7f))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "المتقدم: ${seat.applicantName ?: "سارة علي"}",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = DeepNavy,
                                        fontSize = 15.sp
                                    )
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Outlined.HourglassTop,
                                        contentDescription = null,
                                        tint = SeatPendingAmber,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Text(
                                        text = "36 ساعة متبقية",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            color = SeatPendingAmber,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.5.sp
                                        )
                                    )
                                }
                            }

                            if (!seat.applicantNote.isNullOrBlank()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(verticalAlignment = Alignment.Top) {
                                    Icon(
                                        imageVector = Icons.Outlined.FormatQuote,
                                        contentDescription = null,
                                        tint = Color.Gray,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = seat.applicantNote,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = Color.DarkGray,
                                            fontSize = 13.5.sp,
                                            lineHeight = 21.sp
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                SeatStatus.AVAILABLE -> {
                    Column {
                        OutlinedTextField(
                            value = seat.roleRequired,
                            onValueChange = onRoleChange,
                            label = { Text("المسمى أو التخصص المطلوب لهذا المقعد", fontSize = 14.sp) },
                            trailingIcon = {
                                if (!seat.isLeader) {
                                    IconButton(onClick = onDeleteClick) {
                                        Icon(
                                            imageVector = Icons.Outlined.DeleteOutline,
                                            contentDescription = "حذف المقعد",
                                            tint = Color.Red.copy(alpha = 0.75f),
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                }
                            },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            textStyle = MaterialTheme.typography.bodyLarge.copy(
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = DeepNavy,
                                fontSize = 15.5.sp
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = WarmCream.copy(alpha = 0.6f),
                                unfocusedContainerColor = WarmCream.copy(alpha = 0.4f),
                                focusedBorderColor = DeepTeal,
                                unfocusedBorderColor = SoftGray,
                                focusedLabelColor = DeepTeal
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // شريط التخصصات الجاهزة بلمسة واحدة
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(rolePresets) { preset ->
                                val isSelected = seat.roleRequired.equals(preset, ignoreCase = true)
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isSelected) LightAqua else SoftBeige,
                                    border = if (isSelected) androidx.compose.foundation.BorderStroke(1.2.dp, DeepTeal) else null,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .clickable { onRoleChange(preset) }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        if (isSelected) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = null,
                                                tint = DeepTeal,
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                        }
                                        Text(
                                            text = preset,
                                            style = MaterialTheme.typography.labelMedium.copy(
                                                color = if (isSelected) DeepTeal else DeepNavy,
                                                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.SemiBold,
                                                fontSize = 12.5.sp
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                SeatStatus.LOCKED -> {
                    Text(
                        text = "هذا المقعد مقفل ومجمد بعد اعتماد الكشف النهائي.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                    )
                }
            }
        }
    }
}

// =============================================================================
// كارت الإحصائيات الفخم والواسع (SpaciousMetricCard - 2x2 Grid)
// =============================================================================
@Composable
private fun SpaciousMetricCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: ImageVector,
    iconTint: Color,
    iconBg: Color
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = WarmCream,
        border = androidx.compose.foundation.BorderStroke(1.dp, SoftGray.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = iconBg,
                modifier = Modifier.size(42.dp)
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

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = DeepNavy,
                        fontSize = 16.5.sp
                    )
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp
                    )
                )
            }
        }
    }
}

// =============================================================================
// كبسولة الحالة الهندسية الاحترافية (HighTierBadge)
// =============================================================================
@Composable
private fun HighTierBadge(
    label: String,
    icon: ImageVector,
    tint: Color,
    bgColor: Color
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = bgColor
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 11.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(15.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = tint,
                    fontSize = 12.5.sp
                )
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun EditTeamSeatsScreenPreview() {
    TeamSeatTheme {
        EditTeamSeatsScreen()
    }
}
