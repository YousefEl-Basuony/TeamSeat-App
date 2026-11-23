package com.stackmates.teamseat.Screens.admin_youssef

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
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

// =============================================================================
// ألوان الهوية البصرية الرسمية لشاشة كشف التسليم (Executive Palette)
// =============================================================================
private val NavyDark = Color(0xFF0F172A)         // كحلي داكن فاخر للعناوين والأزرار الأساسية
private val NavyMuted = Color(0xFF1E293B)        // كحلي متوسط
private val OrangePrimary = Color(0xFFF97316)    // برتقالي دافئ مميز للأكشن والليدر
private val OrangeLight = Color(0xFFFFF7ED)      // خلفية ناعمة للبرتقالي
private val OrangeBorder = Color(0xFFFDBA74)     // إطار برتقالي
private val EmeraldGreen = Color(0xFF10B981)     // أخضر زمردي للاعتماد والاكتمال
private val EmeraldLight = Color(0xFFECFDF5)     // خلفية خضراء ناعمة
private val EmeraldBorder = Color(0xFFA7F3D0)    // إطار أخضر
private val ScreenBg = Color(0xFFF8FAFC)         // خلفية عامة محايدة ومريحة للعين
private val CardBorderColor = Color(0xFFE2E8F0)  // لون الحدود الموحد للبطاقات
private val TextSecondary = Color(0xFF64748B)    // نصوص توضيحية رمادية
private val TextMuted = Color(0xFF94A3B8)        // نصوص فرعية خفيفة

/**
 * بيانات عضو الكشف الرسمي
 */
private data class RosterMember(
    val seatNumber: String,
    val name: String,
    val roleEn: String,
    val isLeader: Boolean = false,
    val isApproved: Boolean = true
)

/**
 * 💻 المسؤول عن الشاشة: يوسف (Youssef - Team Lead & Backend)
 * شاشة كشف التسليم الرسمي والاعتماد الأكاديمي (Official TA Roster Report)
 * مصممة بهندسة واجهات عربية متقنة (RTL) ومعالجة لكافة مشاكل المسافات والتداخل والخطوط.
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

    // القائمة الرسمية المعتمدة لأعضاء الفريق الـ 6 ببياناتهم الحقيقية
    val rosterMembers = remember {
        listOf(
            RosterMember(
                seatNumber = "S1",
                name = "يوسف محمد البسيوني",
                roleEn = "Team Leader & Backend (Firebase)",
                isLeader = true,
                isApproved = true
            ),
            RosterMember(
                seatNumber = "S2",
                name = "خالد محمد السعيد",
                roleEn = "Android Developer (Kotlin / Compose)",
                isLeader = false,
                isApproved = true
            ),
            RosterMember(
                seatNumber = "S3",
                name = "حسام الجوهري",
                roleEn = "Android Developer (UI & Navigation)",
                isLeader = false,
                isApproved = true
            ),
            RosterMember(
                seatNumber = "S4",
                name = "إسراء محمد أحمد أبو المعاطى",
                roleEn = "UI/UX Designer",
                isLeader = false,
                isApproved = true
            ),
            RosterMember(
                seatNumber = "S5",
                name = "نها أحمد محمد رجب",
                roleEn = "Frontend & State Management",
                isLeader = false,
                isApproved = true
            ),
            RosterMember(
                seatNumber = "S6",
                name = "آلاء عصام حامد عبدالقادر",
                roleEn = "QA & Documentation",
                isLeader = false,
                isApproved = true
            )
        )
    }

    // تجهيز المقاعد لتوليد ملف الـ PDF متوافقاً مع البيانات الحقيقية
    val pdfSeats = remember(rosterMembers) {
        rosterMembers.mapIndexed { index, m ->
            Seat(
                id = "seat_${index + 1}",
                teamId = team.teamId,
                seatNumber = index + 1,
                roleRequired = m.roleEn,
                status = SeatStatus.BOOKED,
                applicantName = m.name
            )
        }
    }

    val totalCount = rosterMembers.size
    val bookedCount = rosterMembers.count { it.isApproved }
    val isComplete = bookedCount == totalCount

    Scaffold(
        containerColor = ScreenBg,
        topBar = {
            TopAppBar(
                title = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "كشف التسليم الرسمي",
                            fontFamily = CairoFontFamily,
                            fontWeight = FontWeight.ExtraBold,
                            color = NavyDark,
                            fontSize = 18.sp,
                            maxLines = 1,
                            softWrap = false
                        )
                        Text(
                            text = "اعتماد وتوثيق مقاعد الفريق",
                            fontFamily = CairoFontFamily,
                            fontWeight = FontWeight.Medium,
                            color = TextSecondary,
                            fontSize = 12.5.sp,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                },
                navigationIcon = {
                    Surface(
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .size(40.dp),
                        shape = CircleShape,
                        color = Color.White,
                        shadowElevation = 2.dp,
                        border = BorderStroke(1.dp, CardBorderColor)
                    ) {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "رجوع",
                                tint = NavyDark,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                },
                actions = {
                    Surface(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .size(40.dp),
                        shape = CircleShape,
                        color = Color.White,
                        shadowElevation = 2.dp,
                        border = BorderStroke(1.dp, CardBorderColor)
                    ) {
                        IconButton(
                            onClick = {
                                val result = PdfExporter.exportTeamSheetPdf(context, team, pdfSeats)
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
                                tint = OrangePrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ScreenBg)
            )
        },
        bottomBar = {
            // شريط الإجراءات السفلي الثابت (Sticky Bottom Action Bar)
            Surface(
                color = Color.White,
                shadowElevation = 12.dp,
                border = BorderStroke(1.dp, CardBorderColor)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 1. زر تصدير كشف PDF (Primary Action)
                    Button(
                        onClick = {
                            isExporting = true
                            val result = PdfExporter.exportTeamSheetPdf(context, team, pdfSeats)
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
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = NavyDark)
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
                                    tint = OrangePrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = "تصدير كشف PDF",
                                    fontFamily = CairoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color.White,
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }
                        }
                    }

                    // 2. زر اعتماد وقفل التيم (Secondary Action)
                    Button(
                        onClick = onLockTeamClick,
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary)
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
                                fontFamily = CairoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color.White,
                                maxLines = 1,
                                softWrap = false
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
                .padding(paddingValues),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // =================================================================
            // 1. بطاقة ملخص المشروع (Project Summary Card)
            // =================================================================
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, CardBorderColor),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // الصف العلوي: أيقونة المشروع والعنوان وشارة الحالة
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Surface(
                                    modifier = Modifier.size(46.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    color = NavyDark
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Outlined.School,
                                            contentDescription = null,
                                            tint = OrangePrimary,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }

                                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                    Text(
                                        text = "TeamSeat-App",
                                        fontFamily = CairoFontFamily,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = NavyDark,
                                        fontSize = 18.sp,
                                        maxLines = 1,
                                        softWrap = false
                                    )
                                    Text(
                                        text = "مشروع مادة تطوير تطبيقات الهواتف - الفرقة الثالثة حاسبات",
                                        fontFamily = CairoFontFamily,
                                        fontWeight = FontWeight.Medium,
                                        color = TextSecondary,
                                        fontSize = 11.5.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            // شارة الحالة النظيفة غير المنكسرة
                            SingleLineBadge(
                                text = if (isComplete) "جاهز للاعتماد" else "قيد الاستكمال",
                                icon = if (isComplete) Icons.Filled.CheckCircle else Icons.Outlined.HourglassTop,
                                bgColor = if (isComplete) EmeraldLight else OrangeLight,
                                textColor = if (isComplete) EmeraldGreen else OrangePrimary,
                                borderColor = if (isComplete) EmeraldBorder else OrangeBorder
                            )
                        }

                        HorizontalDivider(color = CardBorderColor, thickness = 0.8.dp)

                        // تصنيف المشروع
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "التصنيف الأكاديمي:",
                                fontFamily = CairoFontFamily,
                                fontWeight = FontWeight.Medium,
                                color = TextSecondary,
                                fontSize = 12.5.sp
                            )
                            Text(
                                text = "مشاريع السكاشن / تطوير تطبيقات الأندرويد",
                                fontFamily = CairoFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = NavyDark,
                                fontSize = 12.5.sp,
                                maxLines = 1,
                                softWrap = false
                            )
                        }

                        // شريط التقدم بعرض البطاقة بالكامل
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "حالة اكتمال المقاعد:",
                                    fontFamily = CairoFontFamily,
                                    fontWeight = FontWeight.Medium,
                                    color = TextSecondary,
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = "$bookedCount من $totalCount مقاعد معتمدة (100%)",
                                    fontFamily = CairoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isComplete) EmeraldGreen else OrangePrimary,
                                    fontSize = 12.sp,
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }

                            LinearProgressIndicator(
                                progress = { if (totalCount > 0) bookedCount.toFloat() / totalCount else 0f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(7.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = if (isComplete) EmeraldGreen else OrangePrimary,
                                trackColor = Color(0xFFF1F5F9)
                            )
                        }
                    }
                }
            }

            // =================================================================
            // 2. بطاقة الإشراف الأكاديمي (Academic Supervision Card)
            // =================================================================
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, CardBorderColor),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "هيئة الإشراف والتقييم الأكاديمي:",
                            fontFamily = CairoFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = NavyDark,
                            fontSize = 14.5.sp
                        )

                        // صف أ.د / د. محمد كاظم
                        SupervisionPersonRow(
                            name = "د. محمد كاظم",
                            subtitle = "أستاذ المقرر ورئيس لجنة التقييم",
                            badgeText = "أستاذ المقرر",
                            icon = Icons.Outlined.VerifiedUser,
                            badgeBg = Color(0xFFF1F5F9),
                            badgeColor = NavyDark,
                            badgeBorder = Color(0xFFCBD5E1)
                        )

                        // صف المعيدة المشرفة م. شهد أحمد عبدالسلام
                        SupervisionPersonRow(
                            name = "م. شهد أحمد عبدالسلام",
                            subtitle = "المعيدة المشرفة على المشروع",
                            badgeText = "مسؤولة التسليم",
                            icon = Icons.Outlined.Person,
                            badgeBg = OrangeLight,
                            badgeColor = OrangePrimary,
                            badgeBorder = OrangeBorder
                        )
                    }
                }
            }

            // =================================================================
            // 3. بطاقة كشف مقاعد الفريق المعتمدة (Official Team Roster - 6 Seats)
            // =================================================================
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, CardBorderColor),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "كشف مقاعد الفريق المعتمدة:",
                                fontFamily = CairoFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = NavyDark,
                                fontSize = 14.5.sp
                            )

                            SingleLineBadge(
                                text = "6 مقاعد معتمدة",
                                icon = Icons.Filled.Check,
                                bgColor = EmeraldLight,
                                textColor = EmeraldGreen,
                                borderColor = EmeraldBorder
                            )
                        }

                        HorizontalDivider(color = CardBorderColor, thickness = 0.8.dp)

                        // عرض الأعضاء الـ 6 في بطاقات صفّية منظمة (Compact Clean Rows)
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            rosterMembers.forEach { member ->
                                CleanRosterMemberRow(member = member)
                            }
                        }
                    }
                }
            }

            // مسافة سفلية إضافية لمنع تداخل القائمة مع الشريط السفلي
            item {
                Spacer(modifier = Modifier.height(16.dp))
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
 * 🏷️ شارة نصية نظيفة في سطر واحد بدون أي تداخل أو انكسار
 */
@Composable
private fun SingleLineBadge(
    text: String,
    icon: ImageVector? = null,
    bgColor: Color,
    textColor: Color,
    borderColor: Color
) {
    Surface(
        modifier = Modifier.wrapContentWidth(),
        shape = RoundedCornerShape(8.dp),
        color = bgColor,
        border = BorderStroke(1.dp, borderColor)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = textColor,
                    modifier = Modifier.size(12.dp)
                )
            }
            Text(
                text = text,
                fontFamily = CairoFontFamily,
                fontWeight = FontWeight.Bold,
                color = textColor,
                fontSize = 11.5.sp,
                maxLines = 1,
                softWrap = false
            )
        }
    }
}

/**
 * 👨‍🏫 صف المشرف الأكاديمي النظيف
 */
@Composable
private fun SupervisionPersonRow(
    name: String,
    subtitle: String,
    badgeText: String,
    icon: ImageVector,
    badgeBg: Color,
    badgeColor: Color,
    badgeBorder: Color
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFF8FAFC),
        border = BorderStroke(1.dp, CardBorderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Surface(
                modifier = Modifier.size(38.dp),
                shape = CircleShape,
                color = Color.White,
                border = BorderStroke(1.dp, CardBorderColor)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = NavyDark,
                        modifier = Modifier.size(19.dp)
                    )
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    fontFamily = CairoFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = NavyDark,
                    fontSize = 14.sp,
                    maxLines = 1,
                    softWrap = false
                )
                Text(
                    text = subtitle,
                    fontFamily = CairoFontFamily,
                    fontWeight = FontWeight.Medium,
                    color = TextSecondary,
                    fontSize = 11.5.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            SingleLineBadge(
                text = badgeText,
                bgColor = badgeBg,
                textColor = badgeColor,
                borderColor = badgeBorder
            )
        }
    }
}

/**
 * 💺 بطاقة صف العضو النظيفة (Compact Clean Row)
 */
@Composable
private fun CleanRosterMemberRow(member: RosterMember) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = if (member.isLeader) OrangeLight.copy(alpha = 0.5f) else Color(0xFFF8FAFC),
        border = BorderStroke(
            width = 1.dp,
            color = if (member.isLeader) OrangeBorder else CardBorderColor
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // شارة رقم المقعد S1..S6
            Surface(
                modifier = Modifier.size(36.dp),
                shape = RoundedCornerShape(8.dp),
                color = if (member.isLeader) OrangePrimary else NavyDark
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = member.seatNumber,
                        fontFamily = CairoFontFamily,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        fontSize = 13.sp,
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }

            // اسم العضو والدور التقني بالإنجليزية بمحاذاة سليمة
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = member.name,
                        fontFamily = CairoFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = NavyDark,
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (member.isLeader) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = OrangePrimary,
                            modifier = Modifier.wrapContentWidth()
                        ) {
                            Text(
                                text = "قائد الفريق",
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.5.dp),
                                fontFamily = CairoFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 10.sp,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }
                }

                // الدور التقني بالإنجليزية بمحاذاة سليمة من اليسار لليمين بدون تشوه السطر
                Text(
                    text = member.roleEn,
                    fontFamily = CairoFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = TextSecondary,
                    fontSize = 11.5.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        textDirection = TextDirection.Ltr,
                        textAlign = TextAlign.Start
                    )
                )
            }

            // شارة الاعتماد المعتمدة في سطر واحد
            SingleLineBadge(
                text = "معتمد ✓",
                icon = Icons.Filled.Check,
                bgColor = EmeraldLight,
                textColor = EmeraldGreen,
                borderColor = EmeraldBorder
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
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NavyDark)
            ) {
                Text(
                    text = "تم، حسناً",
                    fontFamily = CairoFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 13.5.sp
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
                    color = EmeraldLight,
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            tint = EmeraldGreen,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Text(
                    text = "تم تصدير الكشف بنجاح!",
                    fontFamily = CairoFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = NavyDark,
                    fontSize = 16.5.sp
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "تم إنشاء كشف التسليم الرسمي بنجاح لمشروع ($teamName) وتجهيزه كملف PDF قياسي بحجم A4 جاهز للتسليم.",
                    fontFamily = CairoFontFamily,
                    fontWeight = FontWeight.Normal,
                    color = TextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFF1F5F9),
                    border = BorderStroke(1.dp, CardBorderColor),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "اسم الملف المُصدَّر:",
                            fontFamily = CairoFontFamily,
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                        Text(
                            text = file.name,
                            fontFamily = CairoFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = NavyDark,
                            fontSize = 12.5.sp
                        )
                    }
                }
            }
        },
        shape = RoundedCornerShape(16.dp),
        containerColor = Color.White
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TASheetPreviewScreenReportPreview() {
    TeamSeatTheme {
        TASheetPreviewScreen()
    }
}
