package com.stackmates.teamseat.Screens.requests_hossam

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.stackmates.teamseat.core.model.Seat
import com.stackmates.teamseat.core.model.SeatStatus
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
 * ⚡ المسؤول عن الشاشة: حسام (Hossam) — [HOSSAM-3]
 * شاشة متابعة طلباتي المعلقة (My Sent Applications - حد أقصى 3 طلبات)
 * تصميم أنيق ومتوافق تماماً مع الهوية البصرية الرسمية لتطبيق TeamSeat
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApplicationsScreen(
    myAppliedSeats: List<Seat> = MockDataProvider.sampleSeats.take(3),
    onCancelApplicationClick: (seatId: String) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    var seatToCancel by remember { mutableStateOf<Seat?>(null) }
    var showSuccessCancelBanner by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        containerColor = WarmCream,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "طلباتي وحجوزاتي",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = DeepNavy,
                                fontSize = 21.sp
                            )
                        )
                        Text(
                            text = "متابعة الطلبات المعلقة ومواعيد انتهائها",
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = WarmCream)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // =========================================================================
            // 0. إشعار سحب الطلب (Cancel Success Banner)
            // =========================================================================
            item {
                AnimatedVisibility(
                    visible = showSuccessCancelBanner,
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
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(DeepTeal),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "تم سحب وإلغاء طلبك بنجاح! 🔄",
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
            // 1. كارت السعة والعداد (Active Applications Capacity Card)
            // =========================================================================
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.4f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
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
                                        imageVector = Icons.Default.Schedule,
                                        contentDescription = null,
                                        tint = DeepTeal,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "الطلبات المعلقة النشطة",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            color = DeepNavy,
                                            fontSize = 17.sp
                                        )
                                    )
                                    Text(
                                        text = "حد أقصى 3 طلبات متزامنة لمنع العشوائية",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = DeepNavy.copy(alpha = 0.65f),
                                            fontSize = 12.sp
                                        )
                                    )
                                }
                            }

                            // شارة العداد
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = if (myAppliedSeats.size >= 3) Peach else LightAqua,
                                border = BorderStroke(
                                    1.dp,
                                    if (myAppliedSeats.size >= 3) DeepNavy.copy(alpha = 0.2f) else DeepTeal.copy(alpha = 0.3f)
                                )
                            ) {
                                Text(
                                    text = "${myAppliedSeats.size} / 3",
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = DeepNavy,
                                        fontSize = 15.sp
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // مؤشرات الدوائر الثلاثة للحالة
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            for (index in 1..3) {
                                val isOccupied = index <= myAppliedSeats.size
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(8.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (isOccupied) DeepTeal else SoftGray.copy(alpha = 0.4f)
                                        )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = Turquoise,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (myAppliedSeats.size < 3) {
                                    "يمكنك التقديم على ${3 - myAppliedSeats.size} مقعد إضافي في مشاريع التخرج"
                                } else {
                                    "وصلت للحد الأقصى، يلزم إلغاء طلب نشط للتقديم على مقعد جديد"
                                },
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = DeepNavy.copy(alpha = 0.75f),
                                    fontSize = 12.5.sp
                                )
                            )
                        }
                    }
                }
            }

            // =========================================================================
            // 2. حالة عدم وجود طلبات (Empty State)
            // =========================================================================
            if (myAppliedSeats.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
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
                                    imageVector = Icons.Default.HourglassEmpty,
                                    contentDescription = null,
                                    tint = DeepTeal,
                                    modifier = Modifier.size(38.dp)
                                )
                            }

                            Text(
                                text = "لا توجد طلبات معلقة حالياً",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = DeepNavy,
                                    fontSize = 20.sp
                                ),
                                textAlign = TextAlign.Center
                            )

                            Text(
                                text = "لم تقم بالتقديم على أي مقعد في مشاريع التخرج بعد. يمكنك استكشاف التيمات المتاحة والتقديم فوراً!",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = DeepNavy.copy(alpha = 0.7f),
                                    lineHeight = 22.sp
                                ),
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Button(
                                onClick = onBackClick,
                                shape = RoundedCornerShape(20.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = DeepTeal)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "استكشاف التيمات المتاحة",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = WarmCream
                                    )
                                )
                            }
                        }
                    }
                }
            } else {
                // =========================================================================
                // 3. قائمة الطلبات المقدمة (Applied Seats Cards)
                // =========================================================================
                items(
                    items = myAppliedSeats,
                    key = { it.id }
                ) { seat ->
                    val teamInfo = remember(seat.teamId) {
                        MockDataProvider.sampleTeamsList.find { it.teamId == seat.teamId } ?: MockDataProvider.sampleTeam
                    }

                    val remainingHours = remember(seat.expiresAt) {
                        if (seat.expiresAt != null) {
                            val diffMs = seat.expiresAt - System.currentTimeMillis()
                            val hours = (diffMs / (3600 * 1000L)).toInt()
                            if (hours > 0) hours else 0
                        } else 36
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.4f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            // الترويسة العليا: اسم مشروع التيم + رقم المقعد
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = teamInfo.projectName,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            color = DeepNavy,
                                            fontSize = 18.sp
                                        ),
                                        maxLines = 1
                                    )
                                    Text(
                                        text = "القائد: ${teamInfo.leaderName}",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = DeepNavy.copy(alpha = 0.65f),
                                            fontSize = 12.5.sp
                                        )
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = LightAqua.copy(alpha = 0.5f),
                                    border = BorderStroke(1.dp, DeepTeal.copy(alpha = 0.3f))
                                ) {
                                    Text(
                                        text = "مقعد #${seat.seatNumber} (${seat.seatLabel})",
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = DeepTeal,
                                            fontSize = 12.5.sp
                                        )
                                    )
                                }
                            }

                            // المهارة / التخصص المطلوب
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                color = SoftBeige.copy(alpha = 0.5f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Code,
                                        contentDescription = null,
                                        tint = DeepTeal,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "الدور المطلوب: ${seat.roleRequired}",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = DeepNavy,
                                            fontSize = 14.5.sp
                                        )
                                    )
                                }
                            }

                            // حالة الطلب + مهلة الـ 48 ساعة
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // شارة الحالة
                                val statusBg = when (seat.status) {
                                    SeatStatus.BOOKED -> LightAqua.copy(alpha = 0.6f)
                                    SeatStatus.PENDING -> Peach.copy(alpha = 0.5f)
                                    else -> LightAqua.copy(alpha = 0.3f)
                                }
                                val statusIcon = when (seat.status) {
                                    SeatStatus.BOOKED -> Icons.Default.CheckCircle
                                    else -> Icons.Default.HourglassTop
                                }
                                val statusText = when (seat.status) {
                                    SeatStatus.BOOKED -> "مقبول ومعتمد رسمياً 🔵"
                                    SeatStatus.PENDING -> "قيد المراجعة بانتظار القائد 🟡"
                                    else -> "متاح 🟢"
                                }

                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = statusBg
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = statusIcon,
                                            contentDescription = null,
                                            tint = DeepNavy,
                                            modifier = Modifier.size(15.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = statusText,
                                            style = MaterialTheme.typography.labelMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = DeepNavy,
                                                fontSize = 12.5.sp
                                            )
                                        )
                                    }
                                }

                                // مهلة الـ 48 ساعة
                                if (seat.status == SeatStatus.PENDING) {
                                    Surface(
                                        shape = RoundedCornerShape(16.dp),
                                        color = WarmCream,
                                        border = BorderStroke(1.dp, Peach)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Schedule,
                                                contentDescription = null,
                                                tint = DeepNavy,
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = "متبقي: $remainingHours ساعة ⏳",
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    color = DeepNavy,
                                                    fontSize = 12.sp
                                                )
                                            )
                                        }
                                    }
                                }
                            }

                            // الرسالة / الملاحظة المقدمة (إذا وُجدت)
                            if (!seat.applicantNote.isNullOrEmpty()) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(WarmCream.copy(alpha = 0.6f))
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = null,
                                        tint = Turquoise,
                                        modifier = Modifier
                                            .size(16.dp)
                                            .padding(top = 2.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "ملاحظتك للقائد: \"${seat.applicantNote}\"",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = DeepNavy.copy(alpha = 0.8f),
                                            fontSize = 12.5.sp,
                                            lineHeight = 18.sp
                                        )
                                    )
                                }
                            }

                            // زر سحب وإلغاء الطلب
                            OutlinedButton(
                                onClick = { seatToCancel = seat },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(46.dp),
                                shape = RoundedCornerShape(23.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = Color(0xFFD32F2F)
                                ),
                                border = BorderStroke(1.dp, Color(0xFFE53935).copy(alpha = 0.5f))
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.DeleteOutline,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "إلغاء وسحب الطلب",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFD32F2F),
                                        fontSize = 14.5.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // =========================================================================
    // 4. نافذة تأكيد سحب الطلب (Cancel Confirmation Dialog)
    // =========================================================================
    if (seatToCancel != null) {
        val seat = seatToCancel!!
        AlertDialog(
            onDismissRequest = { seatToCancel = null },
            title = {
                Text(
                    text = "تأكيد سحب وإلغاء الطلب ⚠️",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = DeepNavy
                    )
                )
            },
            text = {
                Text(
                    text = "هل أنت متأكد من سحب طلبك لمقعد (${seat.roleRequired})؟ سيفقد المقعد أسبقيتك وسيصبح متاحاً للطلاب الآخرين.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = DeepNavy.copy(alpha = 0.8f)
                    )
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val seatId = seat.id
                        seatToCancel = null
                        onCancelApplicationClick(seatId)
                        showSuccessCancelBanner = true
                        scope.launch {
                            snackbarHostState.showSnackbar("تم سحب وإلغاء طلبك بنجاح! 🔄")
                            delay(3000)
                            showSuccessCancelBanner = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "نعم، سحب الطلب",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { seatToCancel = null }) {
                    Text(
                        text = "تراجع",
                        color = DeepNavy,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },
            containerColor = WarmCream,
            shape = RoundedCornerShape(20.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MyApplicationsScreenPreview() {
    TeamSeatTheme {
        MyApplicationsScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun MyApplicationsScreenEmptyPreview() {
    TeamSeatTheme {
        MyApplicationsScreen(
            myAppliedSeats = emptyList()
        )
    }
}
