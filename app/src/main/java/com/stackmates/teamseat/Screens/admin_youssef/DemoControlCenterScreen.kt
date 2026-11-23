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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
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
import com.stackmates.teamseat.core.model.DemoPersona
import com.stackmates.teamseat.core.model.User
import com.stackmates.teamseat.core.theme.*

/**
 * 💻 المسؤول عن الشاشة: يوسف (Youssef - Team Lead & Backend)
 * مركز التحكم في العرض التوضيحي السريع للمناقشة (Live Demo Control Center)
 * مصممة بهوية TeamSeat الرسمية الفاخرة بألوان Color.kt حصراً:
 * (DeepNavy, DeepTeal, Turquoise, LightAqua, WarmCream, SoftBeige, Peach, SoftGray)
 * وتصميم تنفيذي فخم تفاعلي بلمسة واحدة للتبديل الفوري بين السيناريوهات والشخصيات.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DemoControlCenterScreen(
    currentPersona: DemoPersona = DemoPersona.AHMED_LEADER,
    currentUser: User = MockDataProvider.ahmedLeader,
    onTogglePersonaClick: () -> Unit = {},
    onResetMockDataClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val context = LocalContext.current
    var selectedPersona by remember { mutableStateOf(currentPersona) }
    var showResetConfirmDialog by remember { mutableStateOf(false) }

    val isLeaderActive = selectedPersona == DemoPersona.AHMED_LEADER

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmCream)
    ) {
        // تموجات الخلفية الانسيابية الهادئة بألوان الهوية (Turquoise & Peach)
        DemoAmbientBackground()

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = "مركز تحكم المناقشة (Live Demo)",
                                fontFamily = CairoFontFamily,
                                fontWeight = FontWeight.ExtraBold,
                                color = DeepNavy,
                                fontSize = 19.sp,
                                maxLines = 1,
                                softWrap = false
                            )
                            Text(
                                text = "التبديل اللحظي وإدارة سيناريوهات العرض",
                                fontFamily = CairoFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                color = DeepTeal,
                                fontSize = 12.5.sp,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    },
                    navigationIcon = {
                        Surface(
                            modifier = Modifier
                                .padding(start = 12.dp, end = 6.dp)
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
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            },
            bottomBar = {
                // شريط التثبيت السفلي الملموم لزر إعادة ضبط البيانات
                Surface(
                    color = Color.White.copy(alpha = 0.95f),
                    shadowElevation = 10.dp,
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                    border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "تصفير وإعادة ضبط الجلسة",
                                fontFamily = CairoFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = DeepNavy,
                                fontSize = 13.5.sp
                            )
                            Text(
                                text = "إعادة المقاعد والطلبات لحالتها الأولية للتجربة",
                                fontFamily = CairoFontFamily,
                                color = DeepTeal,
                                fontSize = 11.5.sp,
                                maxLines = 1
                            )
                        }

                        Button(
                            onClick = { showResetConfirmDialog = true },
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = DeepNavy),
                            modifier = Modifier.height(44.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.RestartAlt,
                                    contentDescription = null,
                                    tint = Peach,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = "إعادة ضبط (Reset)",
                                    fontFamily = CairoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 12.5.sp,
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }
                        }
                    }
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 18.dp, vertical = 10.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // =============================================================
                // 1. شريط الحالة النشطة العلوي (Active Persona Banner)
                // =============================================================
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = if (isLeaderActive) Peach.copy(alpha = 0.25f) else LightAqua.copy(alpha = 0.45f),
                    border = BorderStroke(
                        1.2.dp,
                        if (isLeaderActive) Peach else Turquoise.copy(alpha = 0.7f)
                    ),
                    shadowElevation = 1.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
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
                                color = if (isLeaderActive) DeepNavy else DeepTeal
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = if (isLeaderActive) Icons.Outlined.WorkspacePremium else Icons.Outlined.School,
                                        contentDescription = null,
                                        tint = if (isLeaderActive) Peach else Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }

                            Column {
                                Text(
                                    text = "الحساب النشط حالياً في التطبيق:",
                                    fontFamily = CairoFontFamily,
                                    fontWeight = FontWeight.Medium,
                                    color = DeepNavy.copy(alpha = 0.7f),
                                    fontSize = 11.5.sp
                                )
                                Text(
                                    text = if (isLeaderActive) "أحمد محمود (قائد التيم 👑)" else "سارة علي (طالبة باحثة 👩‍💻)",
                                    fontFamily = CairoFontFamily,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = DeepNavy,
                                    fontSize = 14.5.sp,
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isLeaderActive) DeepNavy else DeepTeal
                        ) {
                            Text(
                                text = "مُفعّل الآن ✓",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                fontFamily = CairoFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 11.sp,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }
                }

                // =============================================================
                // 2. كارت الشخصية 1: أحمد محمود (قائد الفريق - Team Leader)
                // =============================================================
                ExecutivePersonaCard(
                    name = "أحمد محمود",
                    roleBadge = "قائد التيم",
                    englishRole = "Team Leader & Android Lead",
                    description = "يملك صلاحيات إدارة وتعديل مقاعد المشروع، قبول ورفض المتقدمين، اعتماد وقفل التيم، وتصدير كشف الـ A4 الرسمي للمعيدة.",
                    capabilities = listOf("إدارة وتعديل المقاعد", "قبول طلبات الانضمام", "قفل التيم وتصدير PDF"),
                    icon = Icons.Outlined.WorkspacePremium,
                    isActive = isLeaderActive,
                    badgeColor = DeepTeal,
                    accentColor = Peach,
                    onClick = {
                        if (!isLeaderActive) {
                            selectedPersona = DemoPersona.AHMED_LEADER
                            onTogglePersonaClick()
                            Toast.makeText(context, "تم التبديل إلى حساب: أحمد محمود (قائد التيم)", Toast.LENGTH_SHORT).show()
                        }
                    }
                )

                // =============================================================
                // 3. كارت الشخصية 2: سارة علي (طالبة باحثة عن مقعد - Student Applicant)
                // =============================================================
                ExecutivePersonaCard(
                    name = "سارة علي",
                    roleBadge = "طالبة متقدمة",
                    englishRole = "UI/UX Designer & Student",
                    description = "تمثل منظور الطالب الباحث عن تيم: استعراض المشاريع الشاغرة، اختيار المقعد من خريطة الكراسي، وإرسال طلب الحجز لليدر.",
                    capabilities = listOf("استعراض التيمات الشاغرة", "حجز مقعد من خريطة السينما", "متابعة مهلة الـ 48 ساعة"),
                    icon = Icons.Outlined.School,
                    isActive = !isLeaderActive,
                    badgeColor = DeepNavy,
                    accentColor = Turquoise,
                    onClick = {
                        if (isLeaderActive) {
                            selectedPersona = DemoPersona.SARA_STUDENT
                            onTogglePersonaClick()
                            Toast.makeText(context, "تم التبديل إلى حساب: سارة علي (طالبة متقدمة)", Toast.LENGTH_SHORT).show()
                        }
                    }
                )

                // =============================================================
                // 4. كارت الإرشادات الذكية للمناقشة (Defense Tips Card)
                // =============================================================
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
                    border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = SoftBeige,
                                modifier = Modifier.size(28.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Outlined.Lightbulb,
                                        contentDescription = null,
                                        tint = DeepTeal,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                            Text(
                                text = "كيف تستعرض السيناريو أمام د. محمد كاظم وم. شهد؟",
                                fontFamily = CairoFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = DeepNavy,
                                fontSize = 13.5.sp
                            )
                        }

                        Text(
                            text = "1. ابدأ بحساب سارة وقدّم على مقعد متاح -> 2. بدّل فوراً لأحمد وأظهر إشعار القبول -> 3. افتح كشف التسليم واقفل التيم لاستعراض ملف الـ PDF الرسمي.",
                            fontFamily = CairoFontFamily,
                            fontWeight = FontWeight.Medium,
                            color = DeepNavy.copy(alpha = 0.75f),
                            fontSize = 12.sp,
                            lineHeight = 17.5.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }

    // نافذة تأكيد تصفير البيانات التجريبية
    if (showResetConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showResetConfirmDialog = false },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Peach.copy(alpha = 0.35f),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Outlined.RestartAlt,
                                contentDescription = null,
                                tint = DeepNavy,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Text(
                        text = "تأكيد إعادة الضبط",
                        fontFamily = CairoFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = DeepNavy,
                        fontSize = 16.5.sp
                    )
                }
            },
            text = {
                Text(
                    text = "هل تريد تصفير بيانات المقاعد والطلبات وإعادتها لحالتها الأولية؟ يُنصح بذلك قبل بدء جولة تجريبية جديدة أمام اللجنة.",
                    fontFamily = CairoFontFamily,
                    color = DeepNavy.copy(alpha = 0.8f),
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onResetMockDataClick()
                        showResetConfirmDialog = false
                        Toast.makeText(context, "تمت إعادة ضبط بيانات المقاعد بنجاح ✓", Toast.LENGTH_SHORT).show()
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DeepNavy)
                ) {
                    Text(
                        text = "نعم، إعادة ضبط",
                        fontFamily = CairoFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 13.sp
                    )
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showResetConfirmDialog = false },
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, SoftGray)
                ) {
                    Text(
                        text = "إلغاء",
                        fontFamily = CairoFontFamily,
                        fontWeight = FontWeight.Medium,
                        color = DeepNavy,
                        fontSize = 13.sp
                    )
                }
            },
            shape = RoundedCornerShape(18.dp),
            containerColor = Color.White
        )
    }
}

/**
 * 👑 كارت الشخصية التنفيذي الفاخر (Executive Persona Card)
 */
@Composable
private fun ExecutivePersonaCard(
    name: String,
    roleBadge: String,
    englishRole: String,
    description: String,
    capabilities: List<String>,
    icon: ImageVector,
    isActive: Boolean,
    badgeColor: Color,
    accentColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isActive) Color.White else Color.White.copy(alpha = 0.85f)
        ),
        border = BorderStroke(
            width = if (isActive) 2.dp else 1.dp,
            color = if (isActive) DeepTeal else SoftGray.copy(alpha = 0.6f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isActive) 4.dp else 1.5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // الصف العلوي: الأيقونة، الاسم، الدور، وزر/شارة الحالة
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // أيقونة الشخصية الفاخرة
                    Surface(
                        modifier = Modifier.size(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        color = if (isActive) DeepTeal else DeepNavy,
                        shadowElevation = if (isActive) 2.dp else 0.dp
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = if (isActive) Peach else LightAqua,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = name,
                                fontFamily = CairoFontFamily,
                                fontWeight = FontWeight.ExtraBold,
                                color = DeepNavy,
                                fontSize = 16.5.sp,
                                maxLines = 1,
                                softWrap = false
                            )

                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = if (isActive) Peach.copy(alpha = 0.35f) else SoftBeige
                            ) {
                                Text(
                                    text = roleBadge,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    fontFamily = CairoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepNavy,
                                    fontSize = 11.sp,
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }
                        }

                        Text(
                            text = englishRole,
                            fontFamily = CairoFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = DeepTeal,
                            fontSize = 12.sp,
                            maxLines = 1,
                            softWrap = false,
                            style = TextStyle(textDirection = TextDirection.Ltr)
                        )
                    }
                }

                // شارة نشط حالياً أو زر التحويل بنقرة واحدة
                if (isActive) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = DeepTeal,
                        shadowElevation = 1.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = "نشط حالياً ✓",
                                fontFamily = CairoFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 11.5.sp,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }
                } else {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = SoftBeige.copy(alpha = 0.6f),
                        border = BorderStroke(1.dp, SoftGray)
                    ) {
                        Text(
                            text = "تبديل لهذا الحساب ←",
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
                            fontFamily = CairoFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = DeepNavy,
                            fontSize = 11.sp,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }
            }

            // الوصف التوضيحي لصلاحية الحساب
            Text(
                text = description,
                fontFamily = CairoFontFamily,
                fontWeight = FontWeight.Normal,
                color = DeepNavy.copy(alpha = 0.8f),
                fontSize = 12.5.sp,
                lineHeight = 17.5.sp
            )

            HorizontalDivider(
                color = SoftGray.copy(alpha = 0.45f),
                thickness = 0.8.dp
            )

            // شرائح الصلاحيات السريعة (3 Feature Chips)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                capabilities.forEach { cap ->
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        color = if (isActive) LightAqua.copy(alpha = 0.35f) else SoftBeige.copy(alpha = 0.45f),
                        border = BorderStroke(0.6.dp, SoftGray.copy(alpha = 0.6f))
                    ) {
                        Text(
                            text = cap,
                            modifier = Modifier.padding(vertical = 4.dp, horizontal = 4.dp),
                            fontFamily = CairoFontFamily,
                            fontWeight = FontWeight.Medium,
                            color = DeepNavy,
                            fontSize = 10.5.sp,
                            maxLines = 1,
                            textAlign = TextAlign.Center,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

/**
 * 🌊 تموجات الخلفية الانسيابية لتصميم TeamSeat
 */
@Composable
private fun DemoAmbientBackground() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        val topPath = Path().apply {
            moveTo(0f, 0f)
            lineTo(w * 0.45f, 0f)
            cubicTo(
                w * 0.32f, h * 0.08f,
                w * 0.14f, h * 0.06f,
                0f, h * 0.15f
            )
            close()
        }
        drawPath(topPath, color = Turquoise.copy(alpha = 0.16f))

        val bottomPath = Path().apply {
            moveTo(w, h * 0.85f)
            cubicTo(
                w * 0.84f, h * 0.92f,
                w * 0.74f, h * 0.89f,
                w * 0.62f, h
            )
            lineTo(w, h)
            close()
        }
        drawPath(bottomPath, color = Peach.copy(alpha = 0.22f))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DemoControlCenterScreenExecutivePreview() {
    TeamSeatTheme {
        DemoControlCenterScreen()
    }
}
