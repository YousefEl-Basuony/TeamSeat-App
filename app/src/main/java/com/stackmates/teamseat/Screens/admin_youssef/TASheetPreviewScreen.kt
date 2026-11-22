package com.stackmates.teamseat.Screens.admin_youssef

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
 * شاشة كشف التسليم والاعتماد الرسمي للمعيدة م. شهد أحمد والدكتور د. محمد كاظم (Official TA Sheet)
 * مصممة بنفس لغة التصميم الفخمة والمقاييس البصرية المكبرة والمريحة للعين في EditTeamSeatsScreen و LockTeamDialog.
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
                            text = "كشف التسليم الرسمي (TA Sheet)",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = DeepNavy,
                                fontSize = 21.sp
                            )
                        )
                        Text(
                            text = "كشف اعتماد أعضاء المشروع وتجميد المقاعد",
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
                        border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.6f))
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
                actions = {
                    Surface(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .size(44.dp),
                        shape = CircleShape,
                        color = Color.White,
                        shadowElevation = 2.dp,
                        border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.6f))
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
                                modifier = Modifier.size(22.dp)
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
                        .padding(horizontal = 20.dp, vertical = 16.dp),
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
                            .weight(1.1f)
                            .height(56.dp),
                        shape = RoundedCornerShape(28.dp),
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
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.FileDownload,
                                    contentDescription = null,
                                    tint = Peach,
                                    modifier = Modifier.size(22.dp)
                                )
                                Text(
                                    text = "تصدير كشف PDF",
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = Color.White
                                    )
                                )
                            }
                        }
                    }

                    // زر قفل واعتماد التيم
                    Button(
                        onClick = onLockTeamClick,
                        modifier = Modifier
                            .weight(1.1f)
                            .height(56.dp),
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DeepTeal
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = if (team.isLocked) Icons.Filled.Lock else Icons.Outlined.Lock,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = if (team.isLocked) "التيم معتمد ومقفل 🔒" else "اعتماد وقفل الفريق",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = Color.White
                                )
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
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 10.dp, bottom = 28.dp)
        ) {

            // =================================================================
            // 1. كارت ترويسة الكلية والمشروع الأكاديمي (Official University Header)
            // =================================================================
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // الصف العلوي: شعار الكلية وبادج التوثيق
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Surface(
                                    modifier = Modifier.size(50.dp),
                                    shape = CircleShape,
                                    color = DeepNavy,
                                    shadowElevation = 2.dp
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Outlined.School,
                                            contentDescription = null,
                                            tint = Peach,
                                            modifier = Modifier.size(26.dp)
                                        )
                                    }
                                }

                                Column {
                                    Text(
                                        text = "كلية الحاسبات والمعلومات",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            color = DeepNavy,
                                            fontSize = 17.sp
                                        )
                                    )
                                    Text(
                                        text = "قسم علوم الحاسب • كشف التسليم المعتمد 2026",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontWeight = FontWeight.SemiBold,
                                            color = DeepTeal,
                                            fontSize = 13.sp
                                        )
                                    )
                                }
                            }

                            // كود التوثيق
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = SoftBeige,
                                border = BorderStroke(1.dp, Peach.copy(alpha = 0.8f))
                            ) {
                                Text(
                                    text = "#TS-${team.teamId.take(6).uppercase()}",
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = DeepNavy,
                                        fontSize = 13.sp
                                    )
                                )
                            }
                        }

                        HorizontalDivider(
                            color = SoftGray.copy(alpha = 0.4f),
                            thickness = 1.dp
                        )

                        // تفاصيل المشروع وحالة الاعتماد
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = team.projectName,
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = DeepNavy,
                                        fontSize = 22.sp
                                    )
                                )

                                Surface(
                                    shape = RoundedCornerShape(20.dp),
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
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Text(
                                            text = if (team.isLocked) "معتمد ومقفل" else "جاهز للتسليم",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = if (team.isLocked) SeatBookedBlue else SeatAvailableGreen,
                                                fontSize = 12.sp
                                            )
                                        )
                                    }
                                }
                            }

                            Text(
                                text = "المقرر: ${team.category} • تاريخ الإصدار: $currentDateStr",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = DeepNavy.copy(alpha = 0.7f),
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 14.sp
                                )
                            )
                        }
                    }
                }
            }

            // =================================================================
            // 2. كارت المشرفين الأكاديميين (Supervisors Card)
            // =================================================================
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(
                            text = "هيئة الإشراف والتقييم الأكاديمي:",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = DeepNavy,
                                fontSize = 16.sp
                            )
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // كارت الدكتور
                            SupervisorPersonCard(
                                title = "الأستاذ المشرف الأكاديمي",
                                name = team.supervisorDoctor,
                                roleSubtitle = "رئيس لجنة التحكيم",
                                icon = Icons.Outlined.VerifiedUser,
                                accentColor = DeepNavy,
                                modifier = Modifier.weight(1f)
                            )

                            // كارت المعيدة
                            SupervisorPersonCard(
                                title = "المعيدة المشرفة المسؤولة",
                                name = team.teachingAssistant,
                                roleSubtitle = "المتابعة واعتماد الكشف",
                                icon = Icons.Outlined.Person,
                                accentColor = DeepTeal,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // =================================================================
            // 3. كشف الطلاب المعتمدين والمقاعد (The Official Roster Sheet)
            // =================================================================
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // ترويسة الجدول
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "كشف المقاعد وأعضاء الفريق المعتمدين:",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = DeepNavy,
                                    fontSize = 17.sp
                                )
                            )

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = LightAqua.copy(alpha = 0.35f),
                                border = BorderStroke(1.dp, Turquoise.copy(alpha = 0.5f))
                            ) {
                                Text(
                                    text = "$bookedCount من $totalCount مقاعد مكتملة",
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = DeepTeal,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                )
                            }
                        }

                        HorizontalDivider(
                            color = SoftGray.copy(alpha = 0.4f),
                            thickness = 1.dp
                        )

                        // قائمة صفوف المقاعد كشف كامل مكبر وواضح جداً
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            seats.forEachIndexed { index, seat ->
                                val isLeader = index == 0
                                val isBooked = seat.status == SeatStatus.BOOKED

                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(16.dp),
                                    color = if (isLeader) Peach.copy(alpha = 0.15f) else WarmCream.copy(alpha = 0.5f),
                                    border = BorderStroke(
                                        width = 1.dp,
                                        color = if (isLeader) Peach else SoftGray.copy(alpha = 0.5f)
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 14.dp, vertical = 12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        // رقم المقعد S1..S6
                                        Surface(
                                            modifier = Modifier.size(44.dp),
                                            shape = RoundedCornerShape(12.dp),
                                            color = if (isLeader) DeepTeal else DeepNavy,
                                            shadowElevation = 1.dp
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Text(
                                                    text = "S${seat.seatNumber}",
                                                    style = MaterialTheme.typography.labelLarge.copy(
                                                        color = Color.White,
                                                        fontWeight = FontWeight.ExtraBold,
                                                        fontSize = 15.sp
                                                    )
                                                )
                                            }
                                        }

                                        // اسم الطالب والتخصص
                                        Column(modifier = Modifier.weight(1f)) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                Text(
                                                    text = if (isBooked) (seat.applicantName ?: "طالب معتمد") else "— مقعد شاغر متاح —",
                                                    style = MaterialTheme.typography.bodyLarge.copy(
                                                        fontWeight = FontWeight.Bold,
                                                        color = if (isBooked) DeepNavy else SoftGray,
                                                        fontSize = 16.sp
                                                    ),
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
                                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp),
                                                            style = MaterialTheme.typography.labelSmall.copy(
                                                                color = Color.White,
                                                                fontWeight = FontWeight.Bold,
                                                                fontSize = 11.sp
                                                            )
                                                        )
                                                    }
                                                }
                                            }

                                            Spacer(modifier = Modifier.height(2.dp))

                                            Text(
                                                text = seat.roleRequired,
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    color = DeepTeal,
                                                    fontWeight = FontWeight.SemiBold,
                                                    fontSize = 13.5.sp
                                                )
                                            )
                                        }

                                        // بادج حالة المقعد
                                        Surface(
                                            shape = RoundedCornerShape(10.dp),
                                            color = if (isBooked) SeatAvailableBg else SeatPendingBg,
                                            border = BorderStroke(0.8.dp, if (isBooked) SeatAvailableGreen else SeatPendingAmber)
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                                            ) {
                                                Icon(
                                                    imageVector = if (isBooked) Icons.Filled.CheckCircle else Icons.Outlined.HourglassTop,
                                                    contentDescription = null,
                                                    tint = if (isBooked) SeatAvailableGreen else SeatPendingAmber,
                                                    modifier = Modifier.size(13.dp)
                                                )
                                                Text(
                                                    text = if (isBooked) "معتمد ✓" else "شاغر",
                                                    style = MaterialTheme.typography.labelSmall.copy(
                                                        fontWeight = FontWeight.Bold,
                                                        color = if (isBooked) SeatAvailableGreen else SeatPendingAmber,
                                                        fontSize = 11.5.sp
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
            }

            // =================================================================
            // 4. كارت الإقرار والتوقيعات الرسمية (Declaration & Signatures)
            // =================================================================
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // تنبيه الإقرار
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = LightAqua.copy(alpha = 0.3f),
                            border = BorderStroke(1.dp, DeepTeal.copy(alpha = 0.35f))
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Info,
                                    contentDescription = null,
                                    tint = DeepTeal,
                                    modifier = Modifier.size(20.dp)
                                )
                                Column {
                                    Text(
                                        text = "إقرار وتسليم رسمي:",
                                        style = MaterialTheme.typography.labelLarge.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = DeepNavy,
                                            fontSize = 14.5.sp
                                        )
                                    )
                                    Text(
                                        text = "يقر قائد الفريق بتوافق جميع المقاعد المسجلة مع معايير الكلية واعتماد أعضاء الفريق، وتُعتبر هذه الوثيقة نهائية ومجمدة بمجرد الاعتماد الرسمي.",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = DeepNavy.copy(alpha = 0.75f),
                                            fontSize = 13.sp,
                                            lineHeight = 18.sp
                                        )
                                    )
                                }
                            }
                        }

                        Text(
                            text = "التوقيعات والاعتمادات الرسمية:",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = DeepNavy,
                                fontSize = 16.sp
                            )
                        )

                        // 3 خانات توقيع كبيرة ومريحة للعين
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            ExecutiveSignatureBox(
                                title = "قائد الفريق",
                                signatory = team.leaderName,
                                isSigned = true,
                                modifier = Modifier.weight(1f)
                            )
                            ExecutiveSignatureBox(
                                title = "المعيدة المشرفة",
                                signatory = "م. شهد أحمد",
                                isSigned = team.isLocked,
                                modifier = Modifier.weight(1f)
                            )
                            ExecutiveSignatureBox(
                                title = "أستاذ المادة",
                                signatory = "د. محمد كاظم",
                                isSigned = team.isLocked,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // مسافة سفلية إضافية لضمان عدم تغطية المحتوى بالـ BottomBar
            item {
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }

    // نافذة تأكيد تصدير ملف الـ PDF بنجاح
    if (showExportSuccessDialog && exportedFile != null) {
        ExportSuccessDialog(
            file = exportedFile!!,
            teamName = team.projectName,
            onDismiss = { showExportSuccessDialog = false }
        )
    }
}

/**
 * 👨‍🏫 كارت المشرف الأكاديمي الفردي
 */
@Composable
private fun SupervisorPersonCard(
    title: String,
    name: String,
    roleSubtitle: String,
    icon: ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = WarmCream.copy(alpha = 0.6f),
        border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    modifier = Modifier.size(36.dp),
                    shape = CircleShape,
                    color = accentColor.copy(alpha = 0.15f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = accentColor,
                            modifier = Modifier.size(19.dp)
                        )
                    }
                }
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = DeepNavy.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Column {
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = DeepNavy,
                        fontSize = 15.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = roleSubtitle,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Medium,
                        color = DeepTeal,
                        fontSize = 12.5.sp
                    ),
                    maxLines = 1
                )
            }
        }
    }
}

/**
 * ✍️ خانة التوقيع التنفيذية
 */
@Composable
private fun ExecutiveSignatureBox(
    title: String,
    signatory: String,
    isSigned: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(96.dp),
        shape = RoundedCornerShape(14.dp),
        color = WarmCream.copy(alpha = 0.45f),
        border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.55f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = DeepNavy.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
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
                        modifier = Modifier.size(15.dp)
                    )
                    Text(
                        text = "معتمد إلكترونياً",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = DeepTeal,
                            fontSize = 11.sp
                        )
                    )
                }
            } else {
                Text(
                    text = ".......................",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = SoftGray,
                        fontSize = 13.sp
                    )
                )
            }

            Text(
                text = signatory,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = DeepNavy,
                    fontSize = 13.sp
                ),
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
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 14.5.sp
                    )
                )
            }
        },
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = SeatAvailableBg,
                    modifier = Modifier.size(38.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            tint = SeatAvailableGreen,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
                Text(
                    text = "تم تصدير الكشف بنجاح!",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = DeepNavy,
                        fontSize = 18.sp
                    )
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "تم إنشاء كشف التسليم الرسمي بنجاح لمشروع ($teamName) وتجهيزه كملف PDF بحجم A4 جاهز للتسليم للمعيدة.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = DeepNavy.copy(alpha = 0.8f),
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                )

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SoftBeige.copy(alpha = 0.5f),
                    border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.6f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "اسم الملف المُصدَّر:",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = DeepNavy.copy(alpha = 0.6f),
                                fontSize = 11.5.sp
                            )
                        )
                        Text(
                            text = file.name,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = DeepNavy,
                                fontSize = 13.5.sp
                            )
                        )
                    }
                }
            }
        },
        shape = RoundedCornerShape(24.dp),
        containerColor = Color.White
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TASheetPreviewScreenExecutivePreview() {
    TeamSeatTheme {
        TASheetPreviewScreen()
    }
}
