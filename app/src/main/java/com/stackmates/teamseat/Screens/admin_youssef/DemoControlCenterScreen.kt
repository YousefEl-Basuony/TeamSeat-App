package com.stackmates.teamseat.Screens.admin_youssef

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.stackmates.teamseat.core.model.DemoPersona
import com.stackmates.teamseat.core.model.User
import com.stackmates.teamseat.core.theme.*

// ألوان مريحة وعملية متوافقة مع شاشات التطبيق
private val ScreenBg = Color(0xFFF8FAFC)
private val NavyDark = Color(0xFF0F172A)
private val DeepTeal = Color(0xFF1B6F7D)
private val OrangePrimary = Color(0xFFF97316)
private val OrangeLight = Color(0xFFFFF7ED)
private val EmeraldGreen = Color(0xFF10B981)
private val EmeraldLight = Color(0xFFECFDF5)
private val CardBorderColor = Color(0xFFE2E8F0)
private val TextSecondary = Color(0xFF64748B)

/**
 * 💻 المسؤول عن الشاشة: يوسف (Youssef - Team Lead & Backend)
 * شاشة التبديل السريع بين الحسابات أثناء العرض والمناقشة (Offline Demo Account Switcher)
 * تصميم بسيط جداً، غير مزدحم، وسريع الاستخدام بلمسة واحدة بدون الحاجة للتمرير (Single Viewport - No Scrolling).
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

    Scaffold(
        containerColor = ScreenBg,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "تبديل حساب العرض (Offline)",
                            fontFamily = CairoFontFamily,
                            fontWeight = FontWeight.ExtraBold,
                            color = NavyDark,
                            fontSize = 18.sp,
                            maxLines = 1,
                            softWrap = false
                        )
                        Text(
                            text = "اختر الشخصية لتجربة دورة الحجز والاعتماد فوراً",
                            fontFamily = CairoFontFamily,
                            fontWeight = FontWeight.Medium,
                            color = TextSecondary,
                            fontSize = 12.sp,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                },
                navigationIcon = {
                    Surface(
                        modifier = Modifier
                            .padding(start = 12.dp, end = 6.dp)
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ScreenBg)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // الجزء العلوي: شريط الحالة + البطاقتان الكبيرتان للتبديل الفوري
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // 1. شريط حالة الحساب الحالي (Compact Active Banner)
                Surface(
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = if (isLeaderActive) OrangeLight else EmeraldLight,
                    border = BorderStroke(
                        1.dp,
                        if (isLeaderActive) OrangePrimary.copy(alpha = 0.5f) else EmeraldGreen.copy(alpha = 0.5f)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = null,
                                tint = if (isLeaderActive) OrangePrimary else EmeraldGreen,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = if (isLeaderActive) {
                                    "الحساب النشط الآن: أحمد محمود (قائد التيم)"
                                } else {
                                    "الحساب النشط الآن: سارة علي (طالبة)"
                                },
                                fontFamily = CairoFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = NavyDark,
                                fontSize = 13.sp,
                                maxLines = 1,
                                softWrap = false
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (isLeaderActive) OrangePrimary else EmeraldGreen
                        ) {
                            Text(
                                text = "مُفعّل",
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp),
                                fontFamily = CairoFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 10.5.sp,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }
                }

                // 2. الكارت الأول — قائد الفريق (Team Leader)
                PersonaBigCard(
                    name = "أحمد محمود",
                    roleArabic = "قائد التيم",
                    roleEnglish = "Team Leader",
                    description = "إنشاء التيم، مراجعة الطلبات، وقفل الكشف النهائي.",
                    icon = Icons.Outlined.WorkspacePremium,
                    isActive = isLeaderActive,
                    accentColor = OrangePrimary,
                    accentLightColor = OrangeLight,
                    onClick = {
                        if (!isLeaderActive) {
                            selectedPersona = DemoPersona.AHMED_LEADER
                            onTogglePersonaClick()
                            Toast.makeText(context, "تم التحويل إلى: أحمد محمود (قائد التيم)", Toast.LENGTH_SHORT).show()
                        }
                    }
                )

                // 3. الكارت الثاني — طالبة متقدمة (Student)
                PersonaBigCard(
                    name = "سارة علي",
                    roleArabic = "طالبة متقدمة",
                    roleEnglish = "UI/UX & Android",
                    description = "استعراض التيمات، اختيار مقعد من الخريطة، وإرسال طلب حجز.",
                    icon = Icons.Outlined.School,
                    isActive = !isLeaderActive,
                    accentColor = EmeraldGreen,
                    accentLightColor = EmeraldLight,
                    onClick = {
                        if (isLeaderActive) {
                            selectedPersona = DemoPersona.SARA_STUDENT
                            onTogglePersonaClick()
                            Toast.makeText(context, "تم التحويل إلى: سارة علي (طالبة متقدمة)", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }

            // 4. زر إعادة ضبط البيانات التجريبية (Reset Demo Data) في الأسفل مع نافذة تأكيد
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { showResetConfirmDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, CardBorderColor),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White,
                        contentColor = TextSecondary
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.RestartAlt,
                            contentDescription = null,
                            tint = TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "إعادة ضبط المقاعد التجريبية (Reset)",
                            fontFamily = CairoFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = TextSecondary,
                            fontSize = 13.sp,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }
            }
        }
    }

    // نافذة تأكيد إعادة ضبط البيانات لتجنب الضغط بالخطأ أثناء العرض
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
                        color = OrangeLight,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Outlined.RestartAlt,
                                contentDescription = null,
                                tint = OrangePrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Text(
                        text = "تأكيد إعادة الضبط",
                        fontFamily = CairoFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = NavyDark,
                        fontSize = 16.5.sp
                    )
                }
            },
            text = {
                Text(
                    text = "هل تريد إعادة ضبط جميع المقاعد والطلبات للحالة الأولى؟ سيتم تصفير أي حجز تم أثناء العرض الحالي.",
                    fontFamily = CairoFontFamily,
                    color = TextSecondary,
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
                    colors = ButtonDefaults.buttonColors(containerColor = NavyDark)
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
                    border = BorderStroke(1.dp, CardBorderColor)
                ) {
                    Text(
                        text = "إلغاء",
                        fontFamily = CairoFontFamily,
                        fontWeight = FontWeight.Medium,
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                }
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = Color.White
        )
    }
}

/**
 * 🎴 كارت الشخصية الكبير التفاعلي بلمسة واحدة (Single Tap Persona Card)
 */
@Composable
private fun PersonaBigCard(
    name: String,
    roleArabic: String,
    roleEnglish: String,
    description: String,
    icon: ImageVector,
    isActive: Boolean,
    accentColor: Color,
    accentLightColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isActive) accentLightColor.copy(alpha = 0.5f) else Color.White
        ),
        border = BorderStroke(
            width = if (isActive) 1.8.dp else 1.dp,
            color = if (isActive) accentColor else CardBorderColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isActive) 3.dp else 1.5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // الصف العلوي: الأيقونة، الاسم، الدور، وشارة الحالة
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
                    // أيقونة الشخصية الدائرية
                    Surface(
                        modifier = Modifier.size(44.dp),
                        shape = CircleShape,
                        color = if (isActive) accentColor else Color(0xFFF1F5F9)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = if (isActive) Color.White else NavyDark,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = name,
                            fontFamily = CairoFontFamily,
                            fontWeight = FontWeight.ExtraBold,
                            color = NavyDark,
                            fontSize = 16.sp,
                            maxLines = 1,
                            softWrap = false
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = roleArabic,
                                fontFamily = CairoFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                color = TextSecondary,
                                fontSize = 12.sp,
                                maxLines = 1,
                                softWrap = false
                            )
                            Text(
                                text = "•",
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                            Text(
                                text = roleEnglish,
                                fontFamily = CairoFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = if (isActive) accentColor else TextSecondary,
                                fontSize = 11.5.sp,
                                maxLines = 1,
                                softWrap = false,
                                style = TextStyle(textDirection = TextDirection.Ltr)
                            )
                        }
                    }
                }

                // شارة نشط حالياً أو زر التحويل
                if (isActive) {
                    Surface(
                        modifier = Modifier.wrapContentWidth(),
                        shape = RoundedCornerShape(8.dp),
                        color = accentColor,
                        shadowElevation = 1.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
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
                        modifier = Modifier.wrapContentWidth(),
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFF1F5F9),
                        border = BorderStroke(1.dp, CardBorderColor)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "تبديل لهذا الحساب ←",
                                fontFamily = CairoFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = NavyDark,
                                fontSize = 11.sp,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }
                }
            }

            HorizontalDivider(
                color = CardBorderColor.copy(alpha = 0.7f),
                thickness = 0.8.dp
            )

            // وصف الصلاحية في سطر واحد بدون انكسار
            Text(
                text = description,
                fontFamily = CairoFontFamily,
                fontWeight = FontWeight.Medium,
                color = if (isActive) NavyDark.copy(alpha = 0.85f) else TextSecondary,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DemoControlCenterScreenSimplePreview() {
    TeamSeatTheme {
        DemoControlCenterScreen()
    }
}
