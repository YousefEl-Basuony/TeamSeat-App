package com.stackmates.teamseat.Screens.admin_youssef

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stackmates.teamseat.core.data.MockDataProvider
import com.stackmates.teamseat.core.model.Seat
import com.stackmates.teamseat.core.model.SeatStatus
import com.stackmates.teamseat.core.model.Team
import com.stackmates.teamseat.core.theme.*
import com.stackmates.teamseat.core.utils.PdfExporter
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 💻 المسؤول عن الشاشة: يوسف (Youssef - Team Lead & Backend)
 * شاشة كشف التسليم الرسمي والاعتماد الأكاديمي (Official TA Sheet)
 * مصممة بهندسة بصرية فخمة ومريحة للعين وخالية من أي نصوص مقطوعة أو مشاكل في الاتجاهات (RTL).
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
    var showExportSuccessDialog by remember { mutableStateOf(false) }

    val totalCount = seats.size
    val bookedCount = seats.count { it.status == SeatStatus.BOOKED }
    val isTeamFullyBooked = totalCount > 0 && bookedCount == totalCount

    val currentDateStr = remember {
        SimpleDateFormat("yyyy/MM/dd", Locale("ar")).format(Date())
    }

    Scaffold(
        containerColor = WarmCream,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "كشف التسليم والاعتماد الأكاديمي",
                            fontFamily = CairoFontFamily,
                            fontWeight = FontWeight.ExtraBold,
                            color = DeepNavy,
                            fontSize = 19.sp,
                            maxLines = 1
                        )
                        Text(
                            text = "توثيق مقاعد وأعضاء مشروع التخرج",
                            fontFamily = CairoFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = DeepTeal,
                            fontSize = 13.sp,
                            maxLines = 1
                        )
                    }
                },
                navigationIcon = {
                    Surface(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
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
                    Surface(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .size(42.dp),
                        shape = CircleShape,
                        color = DeepTeal.copy(alpha = 0.12f),
                        border = BorderStroke(1.dp, DeepTeal.copy(alpha = 0.3f))
                    ) {
                        IconButton(
                            onClick = {
                                val result = PdfExporter.exportTeamSheetPdf(context, team, seats)
                                result.onSuccess { file ->
                                    exportedFile = file
                                    showExportSuccessDialog = true
                                    onExportPdfClick()
                                }.onFailure { e ->
                                    Toast.makeText(context, "فشل تصدير الكشف: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.FileDownload,
                                contentDescription = "تحميل PDF",
                                tint = DeepTeal,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = WarmCream)
            )
        },
        bottomBar = {
            Surface(
                color = Color.White,
                shadowElevation = 16.dp,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // زر تصدير PDF
                    Button(
                        onClick = {
                            isExporting = true
                            val result = PdfExporter.exportTeamSheetPdf(context, team, seats)
                            isExporting = false
                            result.onSuccess { file ->
                                exportedFile = file
                                showExportSuccessDialog = true
                                onExportPdfClick()
                            }.onFailure { e ->
                                Toast.makeText(context, "فشل تصدير الملف: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                        },
                        enabled = !isExporting,
                        modifier = Modifier
                            .weight(1f)
                            .height(54.dp),
                        shape = RoundedCornerShape(27.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DeepNavy),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        if (isExporting) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(22.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.FileDownload,
                                    contentDescription = null,
                                    tint = Peach,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = "تصدير كشف PDF",
                                    fontFamily = CairoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.5.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    // زر قفل واعتماد التيم
                    Button(
                        onClick = onLockTeamClick,
                        modifier = Modifier
                            .weight(1f)
                            .height(54.dp),
                        shape = RoundedCornerShape(27.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = if (team.isLocked) Icons.Filled.Lock else Icons.Outlined.Lock,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(19.dp)
                            )
                            Text(
                                text = if (team.isLocked) "معتمد ومقفل 🔒" else "اعتماد وقفل التيم",
                                fontFamily = CairoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.5.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(top = 10.dp, bottom = 28.dp)
        ) {

            // =================================================================
            // 1. كارت ترويسة الكلية وبيانات المشروع (Official Academic Header)
            // =================================================================
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // ترويسة الكلية والكود
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Surface(
                                    modifier = Modifier.size(46.dp),
                                    shape = CircleShape,
                                    color = DeepNavy,
                                    shadowElevation = 2.dp
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Outlined.School,
                                            contentDescription = null,
                                            tint = Peach,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }

                                Column {
                                    Text(
                                        text = "كلية الحاسبات والذكاء الاصطناعي",
                                        fontFamily = CairoFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        color = DeepNavy,
                                        fontSize = 15.sp
                                    )
                                    Text(
                                        text = "كشف التسليم الرسمي • مشاريع 2026",
                                        fontFamily = CairoFontFamily,
                                        fontWeight = FontWeight.Medium,
                                        color = DeepTeal,
                                        fontSize = 12.sp
                                    )
                                }
                            }

                            // بادج كود الكشف
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = SoftBeige,
                                border = BorderStroke(1.dp, Peach.copy(alpha = 0.8f))
                            ) {
                                Text(
                                    text = "#TS-${team.teamId.take(5).uppercase()}",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    fontFamily = CairoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepNavy,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        HorizontalDivider(
                            color = SoftGray.copy(alpha = 0.4f),
                            thickness = 1.dp
                        )

                        // اسم المشروع والمقرر
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = team.projectName,
                                    fontFamily = CairoFontFamily,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = DeepNavy,
                                    fontSize = 20.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "المقرر: ${team.category}",
                                    fontFamily = CairoFontFamily,
                                    fontWeight = FontWeight.SemiBold,
                                    color = DeepTeal,
                                    fontSize = 13.sp
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = if (team.isLocked) SeatBookedBg else SeatAvailableBg,
                                border = BorderStroke(1.dp, if (team.isLocked) SeatBookedBlue else SeatAvailableGreen)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = if (team.isLocked) Icons.Filled.Lock else Icons.Filled.CheckCircle,
                                        contentDescription = null,
                                        tint = if (team.isLocked) SeatBookedBlue else SeatAvailableGreen,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Text(
                                        text = if (team.isLocked) "معتمد ومقفل" else "جاهز للتسليم",
                                        fontFamily = CairoFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        color = if (team.isLocked) SeatBookedBlue else SeatAvailableGreen,
                                        fontSize = 11.5.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // =================================================================
            // 2. كارت المشرفين الأكاديميين (Academic Supervisors - بدون أي قص)
            // =================================================================
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "هيئة الإشراف والتقييم الأكاديمي:",
                            fontFamily = CairoFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = DeepNavy,
                            fontSize = 15.sp
                        )

                        // صف الدكتور المشرف (Full Width - بدون أي قطع بالاسم)
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            color = WarmCream.copy(alpha = 0.55f),
                            border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.45f))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Surface(
                                    modifier = Modifier.size(40.dp),
                                    shape = CircleShape,
                                    color = DeepNavy
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Outlined.VerifiedUser,
                                            contentDescription = null,
                                            tint = WarmCream,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "الأستاذ المشرف الأكاديمي",
                                        fontFamily = CairoFontFamily,
                                        fontWeight = FontWeight.Medium,
                                        color = DeepNavy.copy(alpha = 0.65f),
                                        fontSize = 11.5.sp
                                    )
                                    Text(
                                        text = team.supervisorDoctor,
                                        fontFamily = CairoFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        color = DeepNavy,
                                        fontSize = 15.sp
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = DeepNavy.copy(alpha = 0.1f)
                                ) {
                                    Text(
                                        text = "رئيس اللجنة",
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                        fontFamily = CairoFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        color = DeepNavy,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }

                        // صف المعيدة المشرفة (Full Width - بدون أي قطع بالاسم)
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            color = WarmCream.copy(alpha = 0.55f),
                            border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.45f))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Surface(
                                    modifier = Modifier.size(40.dp),
                                    shape = CircleShape,
                                    color = DeepTeal
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Outlined.Person,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "المعيدة المشرفة على المشروع",
                                        fontFamily = CairoFontFamily,
                                        fontWeight = FontWeight.Medium,
                                        color = DeepNavy.copy(alpha = 0.65f),
                                        fontSize = 11.5.sp
                                    )
                                    Text(
                                        text = team.teachingAssistant,
                                        fontFamily = CairoFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        color = DeepNavy,
                                        fontSize = 15.sp
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = DeepTeal.copy(alpha = 0.12f)
                                ) {
                                    Text(
                                        text = "مسؤولة التسليم",
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                        fontFamily = CairoFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        color = DeepTeal,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // =================================================================
            // 3. كشف المقاعد والطلاب المعتمدين (Official Roster Table)
            // =================================================================
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // هيدر الكشف مع نسبة الاكتمال بنظام أفقي سليم 100% بدون أي انضغاط رأسي
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "كشف المقاعد وأعضاء الفريق المعتمدين:",
                                fontFamily = CairoFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = DeepNavy,
                                fontSize = 15.sp,
                                modifier = Modifier.weight(1f)
                            )

                            // بادج عدد المقاعد (أفقي ومريح للعين تماماً)
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = LightAqua.copy(alpha = 0.4f),
                                border = BorderStroke(1.dp, Turquoise.copy(alpha = 0.6f))
                            ) {
                                Text(
                                    text = "$bookedCount من $totalCount مقاعد مكتملة",
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    fontFamily = CairoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepTeal,
                                    fontSize = 12.sp,
                                    maxLines = 1
                                )
                            }
                        }

                        HorizontalDivider(
                            color = SoftGray.copy(alpha = 0.4f),
                            thickness = 1.dp
                        )

                        // قائمة المقاعد كصفوف مستقلة وواضحة جداً
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            seats.forEachIndexed { index, seat ->
                                val isLeader = index == 0
                                val isBooked = seat.status == SeatStatus.BOOKED

                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(14.dp),
                                    color = if (isLeader) Peach.copy(alpha = 0.15f) else WarmCream.copy(alpha = 0.45f),
                                    border = BorderStroke(
                                        width = 1.dp,
                                        color = if (isLeader) Peach else SoftGray.copy(alpha = 0.5f)
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 12.dp, vertical = 11.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        // رقم المقعد S1..S6
                                        Surface(
                                            modifier = Modifier.size(42.dp),
                                            shape = RoundedCornerShape(10.dp),
                                            color = if (isLeader) DeepTeal else DeepNavy
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Text(
                                                    text = "S${seat.seatNumber}",
                                                    fontFamily = CairoFontFamily,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.White,
                                                    fontSize = 14.sp
                                                )
                                            }
                                        }

                                        // اسم العضو والتخصص
                                        Column(modifier = Modifier.weight(1f)) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                Text(
                                                    text = if (isBooked) (seat.applicantName ?: "طالب معتمد") else "— مقعد شاغر متاح —",
                                                    fontFamily = CairoFontFamily,
                                                    fontWeight = if (isBooked) FontWeight.Bold else FontWeight.Medium,
                                                    color = if (isBooked) DeepNavy else SoftGray,
                                                    fontSize = 15.sp,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                                if (isLeader) {
                                                    Surface(
                                                        shape = RoundedCornerShape(6.dp),
                                                        color = DeepTeal
                                                    ) {
                                                        Text(
                                                            text = "👑 القائد",
                                                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp),
                                                            fontFamily = CairoFontFamily,
                                                            fontWeight = FontWeight.Bold,
                                                            color = Color.White,
                                                            fontSize = 10.5.sp
                                                        )
                                                    }
                                                }
                                            }

                                            Text(
                                                text = seat.roleRequired,
                                                fontFamily = CairoFontFamily,
                                                fontWeight = FontWeight.SemiBold,
                                                color = DeepTeal,
                                                fontSize = 12.5.sp
                                            )
                                        }

                                        // بادج حالة المقعد
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = if (isBooked) SeatAvailableBg else SeatPendingBg,
                                            border = BorderStroke(0.8.dp, if (isBooked) SeatAvailableGreen else SeatPendingAmber)
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                                            ) {
                                                Icon(
                                                    imageVector = if (isBooked) Icons.Filled.CheckCircle else Icons.Outlined.HourglassTop,
                                                    contentDescription = null,
                                                    tint = if (isBooked) SeatAvailableGreen else SeatPendingAmber,
                                                    modifier = Modifier.size(12.dp)
                                                )
                                                Text(
                                                    text = if (isBooked) "معتمد ✓" else "شاغر",
                                                    fontFamily = CairoFontFamily,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (isBooked) SeatAvailableGreen else SeatPendingAmber,
                                                    fontSize = 11.sp
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // =================================================================
            // 4. كارت الإقرار الأكاديمي والتوقيعات الرسمية
            // =================================================================
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = LightAqua.copy(alpha = 0.35f),
                            border = BorderStroke(1.dp, DeepTeal.copy(alpha = 0.3f))
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Info,
                                    contentDescription = null,
                                    tint = DeepTeal,
                                    modifier = Modifier.size(18.dp)
                                )
                                Column {
                                    Text(
                                        text = "إقرار وتسليم رسمي:",
                                        fontFamily = CairoFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        color = DeepNavy,
                                        fontSize = 13.5.sp
                                    )
                                    Text(
                                        text = "يقر قائد الفريق بصحة توزيع المقاعد المعتمدة وفقاً لمتطلبات المقرر الدراسي، وتُعتبر هذه الوثيقة نهائية ومجمدة بعد الاعتماد الأكاديمي.",
                                        fontFamily = CairoFontFamily,
                                        fontWeight = FontWeight.Normal,
                                        color = DeepNavy.copy(alpha = 0.75f),
                                        fontSize = 12.sp,
                                        lineHeight = 17.sp
                                    )
                                }
                            }
                        }

                        Text(
                            text = "التوقيعات والاعتمادات الرسمية:",
                            fontFamily = CairoFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = DeepNavy,
                            fontSize = 14.5.sp
                        )

                        // 3 خانات توقيع مريحة بدون ضغط
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            AcademicSignatureBlock(
                                title = "قائد الفريق",
                                signatory = team.leaderName,
                                isSigned = true,
                                modifier = Modifier.weight(1f)
                            )
                            AcademicSignatureBlock(
                                title = "المعيدة المشرفة",
                                signatory = "م. شهد أحمد",
                                isSigned = team.isLocked,
                                modifier = Modifier.weight(1f)
                            )
                            AcademicSignatureBlock(
                                title = "أستاذ المقرر",
                                signatory = "د. محمد كاظم",
                                isSigned = team.isLocked,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }

    // نافذة تأكيد نجاح تصدير ملف الـ PDF
    if (showExportSuccessDialog && exportedFile != null) {
        ExportSuccessDialog(
            file = exportedFile!!,
            teamName = team.projectName,
            onDismiss = { showExportSuccessDialog = false }
        )
    }
}

/**
 * 🖋️ صندوق التوقيع الأكاديمي المريح
 */
@Composable
private fun AcademicSignatureBlock(
    title: String,
    signatory: String,
    isSigned: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(90.dp),
        shape = RoundedCornerShape(12.dp),
        color = WarmCream.copy(alpha = 0.5f),
        border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.55f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontFamily = CairoFontFamily,
                fontWeight = FontWeight.SemiBold,
                color = DeepNavy.copy(alpha = 0.7f),
                fontSize = 11.5.sp
            )

            if (isSigned) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = null,
                        tint = DeepTeal,
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text = "معتمد ✓",
                        fontFamily = CairoFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = DeepTeal,
                        fontSize = 10.5.sp
                    )
                }
            } else {
                Text(
                    text = "..................",
                    fontFamily = CairoFontFamily,
                    color = SoftGray,
                    fontSize = 11.sp
                )
            }

            Text(
                text = signatory,
                fontFamily = CairoFontFamily,
                fontWeight = FontWeight.Bold,
                color = DeepNavy,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * 🎉 نافذة تأكيد نجاح تصدير ملف الـ PDF
 */
@Composable
private fun ExportSuccessDialog(
    file: File,
    teamName: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = onDismiss,
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DeepNavy)
            ) {
                Text(
                    text = "تم، حسناً",
                    fontFamily = CairoFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        },
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = SeatAvailableBg,
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            tint = SeatAvailableGreen,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Text(
                    text = "تم تصدير الكشف بنجاح!",
                    fontFamily = CairoFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = DeepNavy,
                    fontSize = 17.sp
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "تم إنشاء كشف التسليم الرسمي بنجاح لمشروع ($teamName) وتجهيزه كملف PDF بحجم A4 جاهز للتسليم للمعيدة.",
                    fontFamily = CairoFontFamily,
                    fontWeight = FontWeight.Normal,
                    color = DeepNavy.copy(alpha = 0.8f),
                    fontSize = 13.5.sp,
                    lineHeight = 18.sp
                )

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = SoftBeige.copy(alpha = 0.5f),
                    border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.6f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "اسم الملف المُصدَّر:",
                            fontFamily = CairoFontFamily,
                            color = DeepNavy.copy(alpha = 0.6f),
                            fontSize = 11.sp
                        )
                        Text(
                            text = file.name,
                            fontFamily = CairoFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = DeepNavy,
                            fontSize = 12.5.sp
                        )
                    }
                }
            }
        },
        shape = RoundedCornerShape(20.dp),
        containerColor = Color.White
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TASheetPreviewScreenCairoPreview() {
    TeamSeatTheme {
        TASheetPreviewScreen()
    }
}
