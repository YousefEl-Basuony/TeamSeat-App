package com.stackmates.teamseat.Screens.admin_youssef

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stackmates.teamseat.core.theme.*

/**
 * 💻 المسؤول عن الشاشة: يوسف (Youssef - Team Lead & Backend)
 * شاشة اعتماد وإغلاق الفريق نهائياً قبل تسليم الكشف الرسمي (Lock Team Screen)
 * مصممة بنفس الهوية البصرية الرسمية لشاشة إدارة المقاعد (EditTeamSeatsScreen):
 * خلفية WarmCream، كروت بيضاء فخمة، ألوان DeepNavy و DeepTeal، وخط Tajawal المريح.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LockTeamDialog(
    teamName: String = "StackMates (RoleSpot)",
    totalSeats: Int = 6,
    bookedSeats: Int = 6,
    teachingAssistant: String = "م. شهد أحمد عبدالسلام",
    supervisorDoctor: String = "د. محمد كاظم",
    onConfirmLock: () -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    var isConfirmedByLeader by remember { mutableStateOf(false) }
    var isProcessing by remember { mutableStateOf(false) }
    var showLockedSuccess by remember { mutableStateOf(false) }

    val membersList = listOf(
        Triple("أحمد محمود", "قائد الفريق • Android Lead", true),
        Triple("يوسف أحمد", "Backend Developer (Firebase/Spring)", false),
        Triple("سارة علي", "UI/UX Designer (Figma Lead)", false),
        Triple("عمر خالد", "Flutter Developer", false),
        Triple("كريم صبحي", "AI / ML Engineer", false),
        Triple("منى سعيد", "QA & Software Tester", false)
    )

    Scaffold(
        containerColor = WarmCream,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "اعتماد وإغلاق الفريق",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = DeepNavy,
                                fontSize = 21.sp
                            )
                        )
                        Text(
                            text = "تجميد المقاعد وتجهيز كشف التسليم الأكاديمي",
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
                        IconButton(onClick = onDismiss) {
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
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            if (isConfirmedByLeader && !isProcessing) {
                                isProcessing = true
                                showLockedSuccess = true
                                onConfirmLock()
                            }
                        },
                        enabled = isConfirmedByLeader && !isProcessing,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DeepTeal,
                            disabledContainerColor = SoftGray.copy(alpha = 0.7f),
                            contentColor = Color.White,
                            disabledContentColor = DeepNavy.copy(alpha = 0.4f)
                        ),
                        shape = RoundedCornerShape(28.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        if (isProcessing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White,
                                strokeWidth = 2.5.dp
                            )
                        } else {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = if (isConfirmedByLeader) Color.White else DeepNavy.copy(alpha = 0.4f),
                                    modifier = Modifier.size(22.dp)
                                )
                                Text(
                                    text = if (showLockedSuccess) "تم اعتماد وقفل التيم رسمياً ✓" else "تأكيد إغلاق التيم واعتماد الكشف",
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                )
                            }
                        }
                    }

                    // زر الإلغاء والعودة
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(24.dp),
                        border = BorderStroke(1.dp, DeepTeal.copy(alpha = 0.35f)),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = DeepTeal
                        )
                    ) {
                        Text(
                            text = "العودة للمراجعة والتعديل",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.5.sp,
                                color = DeepTeal
                            )
                        )
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

            // =============================================================
            // 1. كارت ملخص اكتمال الفريق (Hero Metric Dashboard)
            // =============================================================
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
                                    text = teamName,
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = DeepNavy,
                                        fontSize = 20.sp
                                    )
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "اكتملت المقاعد رسمياً ($bookedSeats من أصل $totalSeats مقاعد)",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = Color.Gray,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 14.5.sp
                                    )
                                )
                            }

                            // حلقة التقدم الدائرية الكبيرة (100%)
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.size(64.dp)
                            ) {
                                CircularProgressIndicator(
                                    progress = { 1f },
                                    modifier = Modifier.fillMaxSize(),
                                    color = DeepTeal,
                                    trackColor = SoftBeige,
                                    strokeWidth = 6.dp
                                )
                                Text(
                                    text = "100%",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = DeepNavy,
                                        fontSize = 14.sp
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // شبكة إحصائيات الفريق 2x2
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                LockMetricCard(
                                    modifier = Modifier.weight(1f),
                                    title = "إجمالي المقاعد",
                                    value = "$totalSeats مقاعد",
                                    icon = Icons.Outlined.Groups,
                                    iconTint = DeepNavy,
                                    iconBg = SoftBeige
                                )
                                LockMetricCard(
                                    modifier = Modifier.weight(1f),
                                    title = "الأعضاء المعتمدون",
                                    value = "$bookedSeats طلاب",
                                    icon = Icons.Outlined.VerifiedUser,
                                    iconTint = DeepTeal,
                                    iconBg = SeatBookedBg
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                LockMetricCard(
                                    modifier = Modifier.weight(1f),
                                    title = "المعيدة المشرفة",
                                    value = teachingAssistant,
                                    icon = Icons.Outlined.Person,
                                    iconTint = Turquoise,
                                    iconBg = SeatAvailableBg
                                )
                                LockMetricCard(
                                    modifier = Modifier.weight(1f),
                                    title = "أستاذ المقرر",
                                    value = supervisorDoctor,
                                    icon = Icons.Outlined.School,
                                    iconTint = DeepNavy,
                                    iconBg = SoftBeige
                                )
                            }
                        }
                    }
                }
            }

            // =============================================================
            // 2. كشف تشكيل الفريق المعتمد (Team Roster Preview)
            // =============================================================
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
                            .padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "تشكيل الفريق المعتمد في الكشف",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = DeepNavy,
                                    fontSize = 16.sp
                                )
                            )
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = SeatAvailableBg
                            ) {
                                Text(
                                    text = "جاهز للتسليم ✓",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = DeepTeal,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                )
                            }
                        }

                        HorizontalDivider(color = SoftGray.copy(alpha = 0.4f))

                        membersList.forEachIndexed { index, member ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Surface(
                                        modifier = Modifier.size(34.dp),
                                        shape = CircleShape,
                                        color = if (member.third) SoftBeige else SeatBookedBg
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = "${index + 1}",
                                                style = MaterialTheme.typography.labelLarge.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (member.third) DeepTeal else DeepNavy,
                                                    fontSize = 13.sp
                                                )
                                            )
                                        }
                                    }

                                    Column {
                                        Text(
                                            text = member.first,
                                            style = MaterialTheme.typography.bodyLarge.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = DeepNavy,
                                                fontSize = 14.5.sp
                                            )
                                        )
                                        Text(
                                            text = member.second,
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = Color.Gray,
                                                fontSize = 12.5.sp
                                            )
                                        )
                                    }
                                }

                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = Turquoise,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }

            // =============================================================
            // 3. تنبيه الآثار الإدارية للاعتماد (Advisory Box)
            // =============================================================
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = SoftBeige),
                    border = BorderStroke(1.dp, Peach.copy(alpha = 0.6f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = Peach
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = DeepNavy,
                                    modifier = Modifier
                                        .padding(6.dp)
                                        .size(16.dp)
                                )
                            }
                            Text(
                                text = "ماذا يحدث بعد اعتماد وقفل الفريق؟",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = DeepNavy,
                                    fontSize = 15.sp
                                )
                            )
                        }

                        LockBulletPoint("تجميد المقاعد: لن يتمكن أي عضو من الانسحاب أو تبديل دوره بعد الاعتماد.")
                        LockBulletPoint("تصدير الكشف: تفعيل تحميل كشف المعيدة الرسمي (A4 TA Sheet) المعتمد.")
                        LockBulletPoint("إشعار الفريق: إرسال تأكيد فوري لجميع الأعضاء باعتماد التشكيل النهائي للمشروع.")
                    }
                }
            }

            // =============================================================
            // 4. إقرار قائد الفريق التفاعلي (Leader Confirmation Card)
            // =============================================================
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isConfirmedByLeader = !isConfirmedByLeader },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isConfirmedByLeader) SeatAvailableBg else Color.White
                    ),
                    border = BorderStroke(
                        1.5.dp,
                        if (isConfirmedByLeader) DeepTeal else SoftGray.copy(alpha = 0.7f)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            modifier = Modifier.size(26.dp),
                            shape = RoundedCornerShape(8.dp),
                            color = if (isConfirmedByLeader) DeepTeal else SoftBeige,
                            border = BorderStroke(
                                1.5.dp,
                                if (isConfirmedByLeader) DeepTeal else SoftGray
                            )
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                if (isConfirmedByLeader) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }

                        Text(
                            text = "أقر بصفتي قائد الفريق بمراجعة بيانات المقاعد وأرقام جلوس جميع الأعضاء وجاهزية الكشف للتسليم الرسمي للجنة التقييم.",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = DeepNavy,
                                fontSize = 13.5.sp,
                                lineHeight = 21.sp
                            )
                        )
                    }
                }
            }
        }
    }
}

/**
 * 📊 كارت إحصائية متطابق مع بطاقات شاشة EditTeamSeatsScreen
 */
@Composable
private fun LockMetricCard(
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
        color = iconBg,
        border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.4f))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.size(28.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp
                )
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = DeepNavy,
                    fontSize = 13.5.sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * 🔹 نقطة إرشادية داخل صندوق التنبيه
 */
@Composable
private fun LockBulletPoint(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(top = 8.dp)
                .size(6.dp)
                .clip(CircleShape)
                .background(DeepTeal)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium,
                color = DeepNavy,
                fontSize = 13.5.sp,
                lineHeight = 20.sp
            )
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun LockTeamDialogCleanOfficialPreview() {
    TeamSeatTheme {
        LockTeamDialog()
    }
}
