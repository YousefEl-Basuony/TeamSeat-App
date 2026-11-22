package com.stackmates.teamseat.Screens.admin_youssef

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.material.icons.filled.Verified
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
 * شاشة كشف التسليم الرسمي للمعيدة م. شهد أحمد والدكتور د. محمد كاظم (A4 Official TA Sheet Screen)
 * مصممة بهيئة وثيقة أكاديمية A4 بيضاء رسمية فاخرة بنفس لغة تصميم EditTeamSeatsScreen و LockTeamDialog.
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
        SimpleDateFormat("yyyy/MM/dd • HH:mm", Locale("ar")).format(Date())
    }

    Scaffold(
        containerColor = WarmCream,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "كشف التسليم الرسمي (TA Sheet)",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = DeepNavy,
                                fontSize = 20.sp,
                                fontFamily = TajawalFontFamily
                            )
                        )
                        Text(
                            text = "المعاينة الأكاديمية لاعتماد وتجميد مقاعد المشروع",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = DeepTeal,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.5.sp,
                                fontFamily = TajawalFontFamily
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
                    // زر تصدير سريع في الـ TopBar
                    Surface(
                        modifier = Modifier
                            .padding(end = 12.dp)
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
                                contentDescription = "تصدير كشف PDF",
                                tint = DeepNavy,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = WarmCream)
            )
        },
        bottomBar = {
            // شريط الإجراءات التنفيذية العائم في أسفل الشاشة
            ExecutiveBottomBar(
                isLocked = team.isLocked,
                isExporting = isExporting,
                onExportClick = {
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
                onLockClick = onLockTeamClick
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. شريط تنبيه الحالة الأكاديمية (Status Banner)
            item {
                StatusNoticeBanner(
                    isLocked = team.isLocked,
                    isFullyBooked = isTeamFullyBooked,
                    bookedCount = bookedCount,
                    totalCount = totalCount
                )
            }

            // 2. ورقة الـ A4 الأكاديمية الرسمية الفاخرة
            item {
                A4DocumentSheet(
                    team = team,
                    seats = seats,
                    currentDateStr = currentDateStr,
                    isFullyBooked = isTeamFullyBooked
                )
            }

            // مسافة في النهاية لتجنب تداخل شريط الأزرار العائم
            item {
                Spacer(modifier = Modifier.height(16.dp))
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
 * 📜 ورقة الـ A4 الأكاديمية الرسمية
 */
@Composable
private fun A4DocumentSheet(
    team: Team,
    seats: List<Seat>,
    currentDateStr: String,
    isFullyBooked: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(14.dp),
                spotColor = DeepNavy.copy(alpha = 0.25f)
            ),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.5.dp, SoftGray.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ترويسة الورقة الأكاديمية (Header)
            AcademicSheetHeader(team = team, currentDateStr = currentDateStr)

            HorizontalDivider(
                color = SoftGray.copy(alpha = 0.5f),
                thickness = 1.dp
            )

            // بطاقة بيانات المشروع والمشرفين الأكاديميين (4 خانات)
            SupervisorsMetadataGrid(team = team)

            HorizontalDivider(
                color = SoftGray.copy(alpha = 0.5f),
                thickness = 1.dp
            )

            // جدول المقاعد والطلاب المعتمدين (Official Roster Table)
            Text(
                text = "قائمة المقاعد وأعضاء الفريق المعتمدين:",
                fontFamily = TajawalFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = DeepNavy
            )

            AcademicRosterTable(seats = seats)

            // إقرار المسؤولية وقفل الفريق
            AcademicCommitmentNotice(team = team, isFullyBooked = isFullyBooked)

            HorizontalDivider(
                color = SoftGray.copy(alpha = 0.5f),
                thickness = 1.dp
            )

            // قسم التوقيعات والأختام الرسمية
            SignaturesAndSealSection(team = team)
        }
    }
}

/**
 * 🏛️ ترويسة كشف التسليم الأكاديمي
 */
@Composable
private fun AcademicSheetHeader(
    team: Team,
    currentDateStr: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        // الشعار الأكاديمي مع اسم الكلية
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(46.dp),
                shape = RoundedCornerShape(12.dp),
                color = DeepNavy,
                shadowElevation = 2.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Outlined.School,
                        contentDescription = "الكلية",
                        tint = WarmCream,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            Column {
                Text(
                    text = "جامعة حلوان • كلية الحاسبات والمعلومات",
                    fontFamily = TajawalFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.5.sp,
                    color = DeepNavy
                )
                Text(
                    text = "قسم علوم الحاسب • نظام إدارة مقاعد الفرق",
                    fontFamily = TajawalFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 11.5.sp,
                    color = DeepTeal
                )
            }
        }

        // كود توثيق المستند
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = SoftBeige.copy(alpha = 0.5f),
            border = BorderStroke(1.dp, Peach.copy(alpha = 0.6f))
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "كود المستند:",
                    fontFamily = TajawalFontFamily,
                    fontSize = 9.sp,
                    color = DeepNavy.copy(alpha = 0.6f)
                )
                Text(
                    text = "#TS-${team.teamId.take(6).uppercase()}",
                    fontFamily = TajawalFontFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 11.sp,
                    color = DeepNavy
                )
            }
        }
    }

    // عنوان الكشف بخط عريض
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "كشف اعتماد وتسليم أعضاء مشروع التخرج",
            fontFamily = TajawalFontFamily,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 17.5.sp,
            color = DeepNavy,
            textAlign = TextAlign.Center
        )
        Text(
            text = "تاريخ الإصدار: $currentDateStr",
            fontFamily = TajawalFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 11.5.sp,
            color = DeepNavy.copy(alpha = 0.6f),
            textAlign = TextAlign.Center
        )
    }
}

/**
 * 📋 شبكة بيانات المشرفين والمقرر (4 خانات منظمة)
 */
@Composable
private fun SupervisorsMetadataGrid(team: Team) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MetadataItemCard(
                label = "اسم المشروع المعتمد",
                value = team.projectName,
                icon = Icons.Outlined.WorkspacePremium,
                accentColor = DeepNavy,
                modifier = Modifier.weight(1.2f)
            )
            MetadataItemCard(
                label = "المقرر / الفئة",
                value = team.category,
                icon = Icons.Outlined.Category,
                accentColor = DeepTeal,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MetadataItemCard(
                label = "الأستاذ المشرف الأكاديمي",
                value = team.supervisorDoctor,
                icon = Icons.Outlined.VerifiedUser,
                accentColor = DeepNavy,
                modifier = Modifier.weight(1f)
            )
            MetadataItemCard(
                label = "المعيدة المشرفة المسؤولة",
                value = team.teachingAssistant,
                icon = Icons.Outlined.Person,
                accentColor = DeepTeal,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

/**
 * 🏷️ خانة بيانات مفردة داخل كارت المستند
 */
@Composable
private fun MetadataItemCard(
    label: String,
    value: String,
    icon: ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        color = WarmCream.copy(alpha = 0.5f),
        border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.45f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            Surface(
                modifier = Modifier.size(28.dp),
                shape = CircleShape,
                color = accentColor.copy(alpha = 0.12f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(15.dp)
                    )
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    fontFamily = TajawalFontFamily,
                    fontSize = 10.sp,
                    color = DeepNavy.copy(alpha = 0.6f),
                    maxLines = 1
                )
                Text(
                    text = value,
                    fontFamily = TajawalFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = DeepNavy,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

/**
 * 📊 جدول المقاعد الرسمي (Academic Roster Table)
 */
@Composable
private fun AcademicRosterTable(seats: List<Seat>) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.6f)),
        color = Color.White
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // صف العناوين الرئيسي (Header)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DeepNavy)
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "#",
                    fontFamily = TajawalFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.5.sp,
                    color = Color.White,
                    modifier = Modifier.width(28.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "التخصص / المقعد",
                    fontFamily = TajawalFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.5.sp,
                    color = Color.White,
                    modifier = Modifier.weight(1.3f)
                )
                Text(
                    text = "اسم الطالب المعتمد",
                    fontFamily = TajawalFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.5.sp,
                    color = Color.White,
                    modifier = Modifier.weight(1.4f)
                )
                Text(
                    text = "الحالة",
                    fontFamily = TajawalFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.5.sp,
                    color = Color.White,
                    modifier = Modifier.width(62.dp),
                    textAlign = TextAlign.Center
                )
            }

            // صفوف الطلاب والمقاعد
            seats.forEachIndexed { index, seat ->
                val isLeaderSeat = index == 0
                val isBooked = seat.status == SeatStatus.BOOKED
                val isRowEven = index % 2 == 0

                val rowBg = when {
                    isLeaderSeat -> Peach.copy(alpha = 0.15f)
                    isRowEven -> Color.White
                    else -> SoftBeige.copy(alpha = 0.25f)
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(rowBg)
                        .padding(horizontal = 10.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // رقم المقعد
                    Text(
                        text = "S${seat.seatNumber}",
                        fontFamily = TajawalFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = if (isLeaderSeat) DeepTeal else DeepNavy,
                        modifier = Modifier.width(28.dp),
                        textAlign = TextAlign.Center
                    )

                    // التخصص
                    Row(
                        modifier = Modifier.weight(1.3f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        if (isLeaderSeat) {
                            Text(text = "👑", fontSize = 11.sp)
                        }
                        Text(
                            text = seat.roleRequired,
                            fontFamily = TajawalFontFamily,
                            fontWeight = if (isLeaderSeat) FontWeight.Bold else FontWeight.SemiBold,
                            fontSize = 11.5.sp,
                            color = DeepNavy,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // اسم الطالب
                    Text(
                        text = if (isBooked) (seat.applicantName ?: "طالب معتمد") else "— مقعد شاغر —",
                        fontFamily = TajawalFontFamily,
                        fontWeight = if (isBooked) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 11.5.sp,
                        color = if (isBooked) DeepNavy else SoftGray,
                        modifier = Modifier.weight(1.4f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    // حالة المقعد
                    val statusText = when (seat.status) {
                        SeatStatus.BOOKED -> "معتمد"
                        SeatStatus.AVAILABLE -> "متاح"
                        SeatStatus.PENDING -> "معلق"
                        SeatStatus.LOCKED -> "مقفل"
                    }
                    val statusBg = when (seat.status) {
                        SeatStatus.BOOKED -> SeatBookedBg
                        SeatStatus.AVAILABLE -> SeatAvailableBg
                        SeatStatus.PENDING -> SeatPendingBg
                        SeatStatus.LOCKED -> SoftBeige
                    }
                    val statusColor = when (seat.status) {
                        SeatStatus.BOOKED -> SeatBookedBlue
                        SeatStatus.AVAILABLE -> SeatAvailableGreen
                        SeatStatus.PENDING -> SeatPendingAmber
                        SeatStatus.LOCKED -> DeepNavy
                    }

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = statusBg,
                        border = BorderStroke(0.5.dp, statusColor.copy(alpha = 0.5f)),
                        modifier = Modifier.width(62.dp)
                    ) {
                        Text(
                            text = statusText,
                            fontFamily = TajawalFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            color = statusColor,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }

                if (index < seats.lastIndex) {
                    HorizontalDivider(
                        color = SoftGray.copy(alpha = 0.3f),
                        thickness = 0.6.dp
                    )
                }
            }
        }
    }
}

/**
 * ⚖️ إقرار التيم والاعتماد
 */
@Composable
private fun AcademicCommitmentNotice(
    team: Team,
    isFullyBooked: Boolean
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = LightAqua.copy(alpha = 0.25f),
        border = BorderStroke(1.dp, Turquoise.copy(alpha = 0.4f)),
        modifier = Modifier.fillMaxWidth()
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
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = "إقرار وتعهد أكاديمي:",
                    fontFamily = TajawalFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.5.sp,
                    color = DeepNavy
                )
                Text(
                    text = "يقر قائد الفريق (${team.leaderName}) بأن المقاعد المسجلة تم الاتفاق عليها مع كامل الأعضاء، ولا يجوز تعديل أو تبديل المقاعد بعد اعتماد المعيدة م. شهد أحمد والدكتور المشرف.",
                    fontFamily = TajawalFontFamily,
                    fontSize = 10.5.sp,
                    color = DeepNavy.copy(alpha = 0.75f),
                    lineHeight = 15.sp
                )
            }
        }
    }
}

/**
 * ✍️ خانات التوقيعات والأختام الرسمية
 */
@Composable
private fun SignaturesAndSealSection(team: Team) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "التوقيعات والاعتمادات الرسمية:",
                fontFamily = TajawalFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 12.5.sp,
                color = DeepNavy
            )

            // الختم الرقمي الدائري
            DigitalAcademicSeal()
        }

        // الخانات الثلاثة للتوقيع
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SignatureBox(
                title = "قائد الفريق",
                signatory = team.leaderName,
                isSigned = true,
                modifier = Modifier.weight(1f)
            )
            SignatureBox(
                title = "المعيدة المشرفة",
                signatory = "م. شهد أحمد",
                isSigned = team.isLocked,
                modifier = Modifier.weight(1f)
            )
            SignatureBox(
                title = "أستاذ المادة",
                signatory = "د. محمد كاظم",
                isSigned = team.isLocked,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

/**
 * 🖋️ صندوق التوقيع الفردي
 */
@Composable
private fun SignatureBox(
    title: String,
    signatory: String,
    isSigned: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(78.dp),
        shape = RoundedCornerShape(8.dp),
        color = WarmCream.copy(alpha = 0.4f),
        border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.6f))
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
                fontSize = 10.sp,
                color = DeepNavy.copy(alpha = 0.65f),
                fontWeight = FontWeight.Medium
            )

            if (isSigned) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = null,
                        tint = DeepTeal,
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text = "معتمد إلكترونياً",
                        fontFamily = TajawalFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.sp,
                        color = DeepTeal
                    )
                }
            } else {
                Text(
                    text = ".......................",
                    fontSize = 11.sp,
                    color = SoftGray
                )
            }

            Text(
                text = signatory,
                fontFamily = TajawalFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 10.5.sp,
                color = DeepNavy,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * 🏵️ الختم الرقمي الأكاديمي الدائري
 */
@Composable
private fun DigitalAcademicSeal(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(46.dp)
            .clip(CircleShape)
            .background(DeepTeal.copy(alpha = 0.12f))
            .border(1.5.dp, DeepTeal, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(38.dp)) {
            val w = size.width
            val h = size.height

            // دائرة داخلية
            drawCircle(
                color = Turquoise.copy(alpha = 0.5f),
                radius = w * 0.42f,
                style = Stroke(width = 1.2f)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Verified,
                contentDescription = null,
                tint = DeepTeal,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = "معتمد",
                fontFamily = TajawalFontFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 7.5.sp,
                color = DeepTeal
            )
        }
    }
}

/**
 * 📢 شريط تنبيه الحالة الأكاديمية أعلى الصفحة
 */
@Composable
private fun StatusNoticeBanner(
    isLocked: Boolean,
    isFullyBooked: Boolean,
    bookedCount: Int,
    totalCount: Int
) {
    val bgColor = if (isLocked) SeatBookedBg else if (isFullyBooked) SeatAvailableBg else SeatPendingBg
    val borderColor = if (isLocked) SeatBookedBlue else if (isFullyBooked) SeatAvailableGreen else SeatPendingAmber
    val icon = if (isLocked) Icons.Filled.Lock else if (isFullyBooked) Icons.Outlined.CheckCircle else Icons.Outlined.HourglassTop
    val title = if (isLocked) {
        "كشف معتمد ومقفل رسمياً 🔒"
    } else if (isFullyBooked) {
        "الفريق مكتمل وجاهز للاعتماد النهائي ($bookedCount / $totalCount)"
    } else {
        "مسودة كشف تسليم • مقاعد شاغرة ($bookedCount / $totalCount)"
    }
    val subtitle = if (isLocked) {
        "تم تجميد كافة المقاعد واعتمادها رسمياً، يمكنك تصدير الملف كـ PDF للتسليم المباشر."
    } else if (isFullyBooked) {
        "تم حجز كافة المقاعد بنجاح، يمكنك الآن اعتماد وإغلاق الفريق لتجميد الكشف رسمياً."
    } else {
        "يجب شغل كافة المقاعد قبل تسليم الكشف النهائي للمعيدة م. شهد أحمد."
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = bgColor,
        border = BorderStroke(1.2.dp, borderColor.copy(alpha = 0.6f)),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = borderColor.copy(alpha = 0.2f),
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = borderColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontFamily = TajawalFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.5.sp,
                    color = DeepNavy
                )
                Text(
                    text = subtitle,
                    fontFamily = TajawalFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 11.sp,
                    color = DeepNavy.copy(alpha = 0.75f),
                    lineHeight = 15.sp
                )
            }
        }
    }
}

/**
 * ⚡ شريط الأزرار التنفيذي في أسفل الشاشة
 */
@Composable
private fun ExecutiveBottomBar(
    isLocked: Boolean,
    isExporting: Boolean,
    onExportClick: () -> Unit,
    onLockClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 8.dp,
        border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // زر تصدير PDF
            Button(
                onClick = onExportClick,
                enabled = !isExporting,
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .shadow(3.dp, RoundedCornerShape(26.dp), spotColor = DeepNavy.copy(alpha = 0.3f)),
                shape = RoundedCornerShape(26.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DeepNavy)
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
                            modifier = Modifier.size(19.dp)
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
                onClick = onLockClick,
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .shadow(3.dp, RoundedCornerShape(26.dp), spotColor = DeepTeal.copy(alpha = 0.3f)),
                shape = RoundedCornerShape(26.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isLocked) DeepTeal else DeepTeal
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = if (isLocked) Icons.Filled.Lock else Icons.Outlined.Lock,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = if (isLocked) "التيم معتمد ومقفل 🔒" else "اعتماد وقفل الفريق",
                        fontFamily = TajawalFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }
            }
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
                    fontFamily = TajawalFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color.White
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
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 17.sp,
                    color = DeepNavy
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "تم إنشاء كشف التسليم الرسمي بنجاح لمشروع ($teamName) وتجهيزه كملف PDF قياسي بحجم A4.",
                    fontFamily = TajawalFontFamily,
                    fontSize = 13.5.sp,
                    color = DeepNavy.copy(alpha = 0.8f),
                    lineHeight = 18.sp
                )

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SoftBeige.copy(alpha = 0.4f),
                    border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.6f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "اسم الملف:",
                            fontFamily = TajawalFontFamily,
                            fontSize = 11.sp,
                            color = DeepNavy.copy(alpha = 0.6f)
                        )
                        Text(
                            text = file.name,
                            fontFamily = TajawalFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.5.sp,
                            color = DeepNavy
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
