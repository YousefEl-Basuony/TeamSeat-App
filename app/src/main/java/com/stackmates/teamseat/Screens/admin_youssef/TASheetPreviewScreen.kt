package com.stackmates.teamseat.Screens.admin_youssef

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
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
 * مصممة بهوية TeamSeat العصرية بالكامل المطابقة للموك آب المعتمد:
 * كروت ناعمة بيضاء، خلفية انسيابية دافئة، مقاعد واضحة، وخط تجوال (Tajawal) المتناسق.
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
    val progressPercent = if (totalCount > 0) ((bookedCount.toFloat() / totalCount) * 100).toInt() else 0

    val currentDateStr = remember {
        SimpleDateFormat("yyyy/MM/dd", Locale("ar")).format(Date())
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmCream)
    ) {
        // تموجات الخلفية الانسيابية الهادئة المتطابقة مع هوية التطبيق
        TASheetFluidBackground()

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = "كشف التسليم الرسمي",
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.ExtraBold,
                                color = DeepNavy,
                                fontSize = 20.sp,
                                maxLines = 1
                            )
                            Text(
                                text = "اعتماد وتوثيق مقاعد التيم للمعيدة",
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.Medium,
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
                        Surface(
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .size(42.dp),
                            shape = CircleShape,
                            color = Color.White,
                            shadowElevation = 2.dp,
                            border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.5f))
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
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            },
            bottomBar = {
                Surface(
                    color = Color.White.copy(alpha = 0.96f),
                    shadowElevation = 12.dp,
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                    border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
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
                                .height(52.dp)
                                .shadow(4.dp, RoundedCornerShape(26.dp), spotColor = DeepNavy.copy(alpha = 0.3f)),
                            shape = RoundedCornerShape(26.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = DeepNavy)
                        ) {
                            if (isExporting) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
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
                                        fontFamily = TajawalFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.5.sp,
                                        color = Color.White
                                    )
                                }
                            }
                        }

                        // زر اعتماد وقفل الفريق
                        Button(
                            onClick = onLockTeamClick,
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp)
                                .shadow(4.dp, RoundedCornerShape(26.dp), spotColor = DeepTeal.copy(alpha = 0.3f)),
                            shape = RoundedCornerShape(26.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = DeepTeal)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = if (team.isLocked) Icons.Filled.Lock else Icons.Outlined.Lock,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = if (team.isLocked) "معتمد ومقفل 🔒" else "اعتماد وقفل التيم",
                                    fontFamily = TajawalFontFamily,
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
                    .padding(horizontal = 18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(top = 8.dp, bottom = 24.dp)
            ) {

                // =============================================================
                // 1. كارت ملخص المشروع الأنيق (Modern Hero Project Card)
                // =============================================================
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.5f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // الصف العلوي: اللوجو واسم المشروع
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
                                        shape = RoundedCornerShape(12.dp),
                                        color = LightAqua.copy(alpha = 0.45f)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                imageVector = Icons.Outlined.School,
                                                contentDescription = null,
                                                tint = DeepNavy,
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }
                                    }

                                    Column {
                                        Text(
                                            text = team.projectName,
                                            fontFamily = TajawalFontFamily,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = DeepNavy,
                                            fontSize = 19.sp
                                        )
                                        Text(
                                            text = team.category,
                                            fontFamily = TajawalFontFamily,
                                            fontWeight = FontWeight.Medium,
                                            color = DeepTeal,
                                            fontSize = 13.sp
                                        )
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (team.isLocked) SeatBookedBg else SeatAvailableBg,
                                    border = BorderStroke(1.dp, if (team.isLocked) SeatBookedBlue else SeatAvailableGreen)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
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
                                            text = if (team.isLocked) "معتمد ومقفل" else "جاهز للاعتماد",
                                            fontFamily = TajawalFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            color = if (team.isLocked) SeatBookedBlue else SeatAvailableGreen,
                                            fontSize = 11.5.sp
                                        )
                                    }
                                }
                            }

                            HorizontalDivider(
                                color = SoftGray.copy(alpha = 0.35f),
                                thickness = 0.8.dp
                            )

                            // شريط نسبة اكتمال المقاعد
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "اكتمال مقاعد التيم الرسمية",
                                        fontFamily = TajawalFontFamily,
                                        fontWeight = FontWeight.Medium,
                                        color = DeepNavy.copy(alpha = 0.7f),
                                        fontSize = 12.5.sp
                                    )
                                    Text(
                                        text = "$bookedCount من $totalCount مقاعد معتمدة ($progressPercent%)",
                                        fontFamily = TajawalFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        color = DeepTeal,
                                        fontSize = 12.5.sp
                                    )
                                }

                                LinearProgressIndicator(
                                    progress = { if (totalCount > 0) bookedCount.toFloat() / totalCount else 0f },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(8.dp)
                                        .clip(RoundedCornerShape(4.dp)),
                                    color = DeepTeal,
                                    trackColor = SoftBeige
                                )
                            }
                        }
                    }
                }

                // =============================================================
                // 2. كارت المشرفين الأكاديميين (Supervisors Card)
                // =============================================================
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
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
                                text = "الإشراف الأكاديمي والمتابعة:",
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = DeepNavy,
                                fontSize = 15.sp
                            )

                            // المشرف الأكاديمي
                            SupervisorModernRow(
                                title = "الأستاذ المشرف الأكاديمي",
                                name = team.supervisorDoctor,
                                badge = "رئيس اللجنة",
                                icon = Icons.Outlined.VerifiedUser,
                                accentColor = DeepNavy
                            )

                            // المعيدة المسؤولة
                            SupervisorModernRow(
                                title = "المعيدة المشرفة على المشروع",
                                name = team.teachingAssistant,
                                badge = "مسؤولة التسليم",
                                icon = Icons.Outlined.Person,
                                accentColor = DeepTeal
                            )
                        }
                    }
                }

                // =============================================================
                // 3. كشف المقاعد والأعضاء (The Official Roster Sheet)
                // =============================================================
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.5f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "كشف مقاعد الفريق المعتمدة:",
                                    fontFamily = TajawalFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepNavy,
                                    fontSize = 15.sp
                                )

                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = SoftBeige,
                                    border = BorderStroke(1.dp, Peach.copy(alpha = 0.7f))
                                ) {
                                    Text(
                                        text = "$totalCount مقاعد رسمية",
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                        fontFamily = TajawalFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        color = DeepNavy,
                                        fontSize = 12.sp
                                    )
                                }
                            }

                            HorizontalDivider(
                                color = SoftGray.copy(alpha = 0.35f),
                                thickness = 0.8.dp
                            )

                            // قائمة المقاعد الـ 6 بتصميم مطابق للموك آب
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                seats.forEachIndexed { index, seat ->
                                    val isLeader = index == 0
                                    val isBooked = seat.status == SeatStatus.BOOKED

                                    ModernSeatRow(
                                        seat = seat,
                                        isLeader = isLeader,
                                        isBooked = isBooked
                                    )
                                }
                            }
                        }
                    }
                }

                // =============================================================
                // 4. كارت الإقرار والاعتماد الرسمي (Official Declaration)
                // =============================================================
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.5f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
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
                                            fontFamily = TajawalFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            color = DeepNavy,
                                            fontSize = 13.5.sp
                                        )
                                        Text(
                                            text = "يقر قائد الفريق بصحة توزيع المقاعد المعتمدة أعلاه، ويتم تثبيت الكشف رسمياً لدى المعيدة المشرفة بعد قفل الفريق.",
                                            fontFamily = TajawalFontFamily,
                                            fontWeight = FontWeight.Normal,
                                            color = DeepNavy.copy(alpha = 0.75f),
                                            fontSize = 12.sp,
                                            lineHeight = 17.sp
                                        )
                                    }
                                }
                            }

                            // خانات التوقيعات الثلاثة
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                ModernSignatureBox(
                                    title = "قائد الفريق",
                                    signatory = team.leaderName,
                                    isSigned = true,
                                    modifier = Modifier.weight(1f)
                                )
                                ModernSignatureBox(
                                    title = "المعيدة المشرفة",
                                    signatory = "م. شهد أحمد",
                                    isSigned = team.isLocked,
                                    modifier = Modifier.weight(1f)
                                )
                                ModernSignatureBox(
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
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }

    // مودال نجاح تصدير ملف الـ PDF
    if (showExportSuccessDialog && exportedFile != null) {
        ExportSuccessDialog(
            file = exportedFile!!,
            teamName = team.projectName,
            onDismiss = { showExportSuccessDialog = false }
        )
    }
}

/**
 * 💺 صف المقعد الأنيق والمريح في كشف الفريق
 */
@Composable
private fun ModernSeatRow(
    seat: Seat,
    isLeader: Boolean,
    isBooked: Boolean
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = if (isLeader) Peach.copy(alpha = 0.18f) else SoftBeige.copy(alpha = 0.35f),
        border = BorderStroke(
            width = 1.dp,
            color = if (isLeader) Peach else SoftGray.copy(alpha = 0.55f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // أيقونة المقعد الشيك (رمز الكرسي المعتمد)
            Surface(
                modifier = Modifier.size(42.dp),
                shape = RoundedCornerShape(10.dp),
                color = if (isLeader) DeepTeal else if (isBooked) DeepNavy else SoftGray.copy(alpha = 0.6f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "S${seat.seatNumber}",
                        fontFamily = TajawalFontFamily,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }

            // الاسم والتخصص
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = if (isBooked) (seat.applicantName ?: "طالب معتمد") else "— مقعد شاغر متاح —",
                        fontFamily = TajawalFontFamily,
                        fontWeight = if (isBooked) FontWeight.Bold else FontWeight.Medium,
                        color = if (isBooked) DeepNavy else DeepNavy.copy(alpha = 0.5f),
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
                                text = "القائد",
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp),
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 10.5.sp
                            )
                        }
                    }
                }

                Text(
                    text = seat.roleRequired,
                    fontFamily = TajawalFontFamily,
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
                        text = if (isBooked) "معتمد" else "شاغر",
                        fontFamily = TajawalFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = if (isBooked) SeatAvailableGreen else SeatPendingAmber,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}

/**
 * 👨‍🏫 صف المشرف الأكاديمي الفردي
 */
@Composable
private fun SupervisorModernRow(
    title: String,
    name: String,
    badge: String,
    icon: ImageVector,
    accentColor: Color
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = WarmCream.copy(alpha = 0.5f),
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
                color = accentColor.copy(alpha = 0.15f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontFamily = TajawalFontFamily,
                    fontWeight = FontWeight.Medium,
                    color = DeepNavy.copy(alpha = 0.65f),
                    fontSize = 11.5.sp
                )
                Text(
                    text = name,
                    fontFamily = TajawalFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = DeepNavy,
                    fontSize = 14.5.sp
                )
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = accentColor.copy(alpha = 0.12f)
            ) {
                Text(
                    text = badge,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                    fontFamily = TajawalFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = accentColor,
                    fontSize = 11.sp
                )
            }
        }
    }
}

/**
 * 🖋️ صندوق التوقيع الأكاديمي الأنيق
 */
@Composable
private fun ModernSignatureBox(
    title: String,
    signatory: String,
    isSigned: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(86.dp),
        shape = RoundedCornerShape(12.dp),
        color = WarmCream.copy(alpha = 0.45f),
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
                fontFamily = TajawalFontFamily,
                fontWeight = FontWeight.Medium,
                color = DeepNavy.copy(alpha = 0.7f),
                fontSize = 11.sp
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
                        text = "معتمد",
                        fontFamily = TajawalFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = DeepTeal,
                        fontSize = 10.5.sp
                    )
                }
            } else {
                Text(
                    text = "...........",
                    color = SoftGray,
                    fontSize = 11.sp
                )
            }

            Text(
                text = signatory,
                fontFamily = TajawalFontFamily,
                fontWeight = FontWeight.Bold,
                color = DeepNavy,
                fontSize = 11.5.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * 🌊 تموجات الخلفية الانسيابية لتصميم TeamSeat
 */
@Composable
private fun TASheetFluidBackground() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        val topPath = Path().apply {
            moveTo(0f, 0f)
            lineTo(w * 0.4f, 0f)
            cubicTo(
                w * 0.3f, h * 0.08f,
                w * 0.15f, h * 0.06f,
                0f, h * 0.14f
            )
            close()
        }
        drawPath(topPath, color = Turquoise.copy(alpha = 0.18f))

        val bottomPath = Path().apply {
            moveTo(w, h * 0.88f)
            cubicTo(
                w * 0.85f, h * 0.93f,
                w * 0.75f, h * 0.90f,
                w * 0.65f, h
            )
            lineTo(w, h)
            close()
        }
        drawPath(bottomPath, color = Peach.copy(alpha = 0.22f))
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
                    fontFamily = TajawalFontFamily,
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
                    fontFamily = TajawalFontFamily,
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
                    fontFamily = TajawalFontFamily,
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
                            fontFamily = TajawalFontFamily,
                            color = DeepNavy.copy(alpha = 0.6f),
                            fontSize = 11.sp
                        )
                        Text(
                            text = file.name,
                            fontFamily = TajawalFontFamily,
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
private fun TASheetPreviewScreenFullPreview() {
    TeamSeatTheme {
        TASheetPreviewScreen()
    }
}
