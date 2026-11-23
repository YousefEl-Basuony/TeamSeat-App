package com.stackmates.teamseat.Screens.admin_youssef

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stackmates.teamseat.core.data.MockDataProvider
import com.stackmates.teamseat.core.model.DemoPersona
import com.stackmates.teamseat.core.model.User
import com.stackmates.teamseat.core.theme.CairoFontFamily
import com.stackmates.teamseat.core.theme.TeamSeatTheme

// الألوان المحددة بدقة للتصميم فائق البساطة (Ultra-Minimalist Palette)
private val ScreenBg = Color(0xFFF8FAFC)
private val CardWhite = Color(0xFFFFFFFF)
private val BorderInactive = Color(0xFFE2E8F0)
private val TextPrimary = Color(0xFF0F172A)
private val TextSecondary = Color(0xFF64748B)

// ألوان تمييز الحساب النشط (برتقالي دافئ)
private val OrangeWarm = Color(0xFFF97316)
private val OrangeWarmBg = Color(0xFFFFF7ED)

/**
 * 💻 المسؤول عن الشاشة: يوسف (Youssef - Team Lead & Backend)
 * شاشة تبديل الحساب فائقة البساطة (Ultra-Minimalist Demo Account Switcher)
 * تتكون بدقة من 4 عناصر فقط لا غير:
 * 1. الهيدر: زر رجوع على اليمين + عنوان "تبديل الحساب"
 * 2. كارت الحساب الأول: أحمد محمود (قائد الفريق)
 * 3. كارت الحساب الثاني: سارة علي (عضو / متقدم)
 * 4. الزر السفلي: إعادة ضبط التجربة
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
    var showResetDialog by remember { mutableStateOf(false) }

    val isLeaderActive = selectedPersona == DemoPersona.AHMED_LEADER

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Scaffold(
            containerColor = ScreenBg,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "تبديل الحساب",
                            fontFamily = CairoFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            fontSize = 20.sp,
                            maxLines = 1,
                            softWrap = false
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = onBackClick,
                            modifier = Modifier.padding(start = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "رجوع",
                                tint = TextPrimary,
                                modifier = Modifier.size(24.dp)
                            )
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
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // الكروت في الجزء العلوي بمسافات مريحة
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // 1. كارت أحمد محمود (قائد الفريق)
                    AccountSwitchCard(
                        name = "أحمد محمود",
                        role = "قائد الفريق",
                        emoji = "👑",
                        isActive = isLeaderActive,
                        onClick = {
                            if (!isLeaderActive) {
                                selectedPersona = DemoPersona.AHMED_LEADER
                                onTogglePersonaClick()
                                Toast.makeText(context, "تم التبديل إلى حساب قائد الفريق", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )

                    // 2. كارت سارة علي (عضو / متقدم)
                    AccountSwitchCard(
                        name = "سارة علي",
                        role = "عضو / متقدم",
                        emoji = "🎓",
                        isActive = !isLeaderActive,
                        onClick = {
                            if (isLeaderActive) {
                                selectedPersona = DemoPersona.SARA_STUDENT
                                onTogglePersonaClick()
                                Toast.makeText(context, "تم التبديل إلى حساب الطالبة (سارة)", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }

                // 3. الزر السفلي البسيط: إعادة ضبط التجربة
                OutlinedButton(
                    onClick = { showResetDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, BorderInactive),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = CardWhite,
                        contentColor = TextSecondary
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Refresh,
                            contentDescription = null,
                            tint = TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "إعادة ضبط التجربة",
                            fontFamily = CairoFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = TextSecondary,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }
            }
        }

        // نافذة تأكيد إعادة ضبط التجربة
        if (showResetDialog) {
            AlertDialog(
                onDismissRequest = { showResetDialog = false },
                containerColor = CardWhite,
                shape = RoundedCornerShape(20.dp),
                title = {
                    Text(
                        text = "إعادة ضبط التجربة",
                        fontFamily = CairoFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        fontSize = 17.sp
                    )
                },
                text = {
                    Text(
                        text = "هل تريد استرجاع الحالة الافتراضية للمقاعد والطلبات؟",
                        fontFamily = CairoFontFamily,
                        fontWeight = FontWeight.Normal,
                        color = TextSecondary,
                        fontSize = 14.sp
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showResetDialog = false
                            onResetMockDataClick()
                            Toast.makeText(context, "تمت إعادة ضبط التجربة بنجاح", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = TextPrimary),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "تأكيد",
                            fontFamily = CairoFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = CardWhite
                        )
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showResetDialog = false }) {
                        Text(
                            text = "إلغاء",
                            fontFamily = CairoFontFamily,
                            fontWeight = FontWeight.Medium,
                            color = TextSecondary
                        )
                    }
                }
            )
        }
    }
}

/**
 * كارت تبديل الحساب فائق البساطة (ارتفاع 88.dp وحواف 20.dp)
 */
@Composable
private fun AccountSwitchCard(
    name: String,
    role: String,
    emoji: String,
    isActive: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(88.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        color = if (isActive) OrangeWarmBg else CardWhite,
        border = BorderStroke(
            width = if (isActive) 2.dp else 1.dp,
            color = if (isActive) OrangeWarm else BorderInactive
        ),
        shadowElevation = if (isActive) 2.dp else 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // الجانب الأيمن: الأيقونة الدائرية + الاسم والصفة
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.weight(1f)
            ) {
                // الأيقونة الدائرية (حجم 50.dp)
                Surface(
                    modifier = Modifier.size(50.dp),
                    shape = CircleShape,
                    color = if (isActive) OrangeWarm.copy(alpha = 0.12f) else ScreenBg,
                    border = BorderStroke(1.dp, if (isActive) OrangeWarm.copy(alpha = 0.35f) else BorderInactive)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = emoji,
                            fontSize = 22.sp
                        )
                    }
                }

                // الاسم والصفة بسطر واحد لكل منهما بدون أي انكسار أو اقتطاع
                Column(
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = name,
                        fontFamily = CairoFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        fontSize = 16.sp,
                        maxLines = 1,
                        softWrap = false
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = role,
                        fontFamily = CairoFontFamily,
                        fontWeight = FontWeight.Medium,
                        color = TextSecondary,
                        fontSize = 13.sp,
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }

            // الجانب الأيسر: شارة نشط أو زر تبديل
            if (isActive) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = OrangeWarm,
                    modifier = Modifier.wrapContentWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "نشط",
                            fontFamily = CairoFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 12.sp,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }
            } else {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = ScreenBg,
                    border = BorderStroke(1.dp, BorderInactive),
                    modifier = Modifier.wrapContentWidth()
                ) {
                    Text(
                        text = "تبديل",
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                        fontFamily = CairoFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        fontSize = 12.sp,
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DemoControlCenterScreenMinimalPreview() {
    TeamSeatTheme {
        DemoControlCenterScreen()
    }
}
