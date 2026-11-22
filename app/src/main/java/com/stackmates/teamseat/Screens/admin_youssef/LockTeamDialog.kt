package com.stackmates.teamseat.Screens.admin_youssef

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stackmates.teamseat.core.theme.*

/**
 * 💻 المسؤول عن الشاشة: يوسف (Youssef - Team Lead & Backend)
 * شاشة ونافذة تأكيد إغلاق واعتماد التيم نهائياً قبل تسليم الكشف الرسمي (Lock Team Dialog)
 * تصميم تنفيذي فخم، مريح للعين، وبأعلى معايير الـ UX للعمليات الحساسة.
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

    val scrollState = rememberScrollState()

    Scaffold(
        containerColor = WarmCream,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "اعتماد وإغلاق الفريق",
                        fontFamily = TajawalFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = DeepNavy
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "رجوع",
                            tint = DeepNavy
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = WarmCream)
            )
        },
        bottomBar = {
            // شريط الأزرار السفلي الثابت
            Surface(
                color = Color.White,
                shadowElevation = 14.dp,
                border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // الزر الرئيسي للاعتماد
                    Button(
                        onClick = {
                            if (isConfirmedByLeader && !isProcessing) {
                                isProcessing = true
                                onConfirmLock()
                            }
                        },
                        enabled = isConfirmedByLeader && !isProcessing,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DeepNavy,
                            contentColor = Color.White,
                            disabledContainerColor = SoftGray.copy(alpha = 0.6f),
                            disabledContentColor = DeepNavy.copy(alpha = 0.4f)
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        if (isProcessing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Peach,
                                strokeWidth = 2.5.dp
                            )
                        } else {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = if (isConfirmedByLeader) Peach else DeepNavy.copy(alpha = 0.4f),
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = "تأكيد القفل والاعتماد النهائي",
                                    fontFamily = TajawalFontFamily,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 15.5.sp
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
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, DeepTeal.copy(alpha = 0.4f)),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = DeepTeal
                        )
                    ) {
                        Text(
                            text = "العودة للمراجعة والتعديل",
                            fontFamily = TajawalFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. أيقونة وشعار الأمان المركزي (Executive Security Badge)
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
            ) {
                // هالة دائرية خارجية متدرجة
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    Turquoise.copy(alpha = 0.25f),
                                    Peach.copy(alpha = 0.15f),
                                    Color.Transparent
                                )
                            )
                        )
                )

                // الدائرة الوسطى
                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .clip(CircleShape)
                        .background(DeepNavy)
                        .border(3.dp, Peach.copy(alpha = 0.8f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "قفل التيم",
                        tint = Peach,
                        modifier = Modifier.size(36.dp)
                    )
                }

                // علامة توثيق صغيرة في الزاوية
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .align(Alignment.BottomEnd)
                        .offset(x = (-4).dp, y = (-4).dp)
                        .clip(CircleShape)
                        .background(DeepTeal)
                        .border(2.dp, WarmCream, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // 2. العناوين والنصوص الإرشادية
            Text(
                text = "اعتماد وقفل التيم نهائياً",
                fontFamily = TajawalFontFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 23.sp,
                color = DeepNavy,
                textAlign = TextAlign.Center
            )

            Text(
                text = "إجراء إداري رسمي لتجميد المقاعد واستخراج كشف التسليم الأكاديمي للجنة التقييم.",
                fontFamily = TajawalFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.5.sp,
                color = DeepNavy.copy(alpha = 0.72f),
                textAlign = TextAlign.Center,
                lineHeight = 22.sp,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            // 3. كارت ملخص بيانات الفريق (Team Status Summary Card)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(22.dp),
                        spotColor = DeepNavy.copy(alpha = 0.08f)
                    ),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.45f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "ملخص حالة التيم الحالي",
                        fontFamily = TajawalFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = DeepNavy
                    )

                    HorizontalDivider(color = SoftGray.copy(alpha = 0.4f))

                    // اسم المشروع
                    SummaryRowItem(
                        icon = Icons.Outlined.Groups,
                        iconTint = DeepTeal,
                        label = "اسم الفريق:",
                        value = teamName
                    )

                    // حالة المقاعد
                    SummaryRowItem(
                        icon = Icons.Outlined.EventSeat,
                        iconTint = Turquoise,
                        label = "حالة المقاعد:",
                        value = "$bookedSeats من أصل $totalSeats مقاعد (مكتمل)"
                    )

                    // المعيدة المشرفة
                    SummaryRowItem(
                        icon = Icons.Outlined.School,
                        iconTint = DeepNavy,
                        label = "المعيدة المشرفة:",
                        value = teachingAssistant
                    )

                    // المشرف العام
                    SummaryRowItem(
                        icon = Icons.Outlined.Person,
                        iconTint = DeepTeal,
                        label = "أستاذ المادة:",
                        value = supervisorDoctor
                    )
                }
            }

            // 4. صندوق التنبيهات والآثار الإدارية (What Happens After Lock)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = SoftBeige.copy(alpha = 0.7f)
                ),
                border = BorderStroke(1.dp, Peach.copy(alpha = 0.65f))
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
                                imageVector = Icons.Default.Security,
                                contentDescription = null,
                                tint = DeepNavy,
                                modifier = Modifier
                                    .padding(6.dp)
                                    .size(16.dp)
                            )
                        }
                        Text(
                            text = "ماذا يترتب على هذا الإجراء؟",
                            fontFamily = TajawalFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = DeepNavy
                        )
                    }

                    WarningBulletPoint(
                        text = "تجميد القائمة: لن يتمكن أي عضو من الانسحاب أو تبديل مقعده."
                    )
                    WarningBulletPoint(
                        text = "إيقاف الطلبات: سيتم رفض أي طلبات انضمام جديدة تلقائياً."
                    )
                    WarningBulletPoint(
                        text = "اعتماد الكشف الرسمي: تفعيل زر تصدير الـ PDF المعتمد للتسليم."
                    )
                }
            }

            // 5. إقرار قائد الفريق التفاعلي (Leader Confirmation Checkbox)
            Surface(
                onClick = { isConfirmedByLeader = !isConfirmedByLeader },
                shape = RoundedCornerShape(16.dp),
                color = if (isConfirmedByLeader) LightAqua.copy(alpha = 0.35f) else Color.White,
                border = BorderStroke(
                    1.5.dp,
                    if (isConfirmedByLeader) DeepTeal else SoftGray.copy(alpha = 0.6f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(RoundedCornerShape(7.dp))
                            .background(if (isConfirmedByLeader) DeepTeal else SoftBeige)
                            .border(
                                1.5.dp,
                                if (isConfirmedByLeader) DeepTeal else SoftGray,
                                RoundedCornerShape(7.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isConfirmedByLeader) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Text(
                        text = "أقر بصفتي قائد الفريق بمراجعة بيانات المقاعد وأرقام جلوس الأعضاء وجاهزيتها للتسليم الرسمي.",
                        fontFamily = TajawalFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.5.sp,
                        color = DeepNavy,
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

/**
 * 📋 عنصر سطر الملخص داخل الكارت
 */
@Composable
private fun SummaryRowItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = iconTint.copy(alpha = 0.12f)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier
                        .padding(6.dp)
                        .size(18.dp)
                )
            }
            Text(
                text = label,
                fontFamily = TajawalFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 13.5.sp,
                color = DeepNavy.copy(alpha = 0.7f)
            )
        }

        Text(
            text = value,
            fontFamily = TajawalFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = DeepNavy
        )
    }
}

/**
 * ⚠️ نقطة إرشادية داخل صندوق التنبيه
 */
@Composable
private fun WarningBulletPoint(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(top = 7.dp)
                .size(6.dp)
                .clip(CircleShape)
                .background(DeepTeal)
        )
        Text(
            text = text,
            fontFamily = TajawalFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 13.5.sp,
            color = DeepNavy.copy(alpha = 0.85f),
            lineHeight = 20.sp
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun LockTeamDialogOfficialPreview() {
    TeamSeatTheme {
        LockTeamDialog()
    }
}
