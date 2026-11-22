package com.stackmates.teamseat.Screens.auth_noha

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

// درجات الألوان الرسمية لتطبيق TeamSeat
private val DeepNavy   = Color(0xFF0A344A) // العناوين الداكنة
private val DeepTeal   = Color(0xFF1B6F7D) // الأزرار وعناصر التركيز
private val Turquoise  = Color(0xFF469D9F) // التركواز للأيقونات واللمسات
private val LightAqua  = Color(0xFFA9D5D4) // خلفيات الآورا والبادج
private val WarmCream  = Color(0xFFFAF5EE) // الخلفية الكريمية الدافئة للشاشة
private val SoftGray   = Color(0xFFD8D3D3) // النقاط غير النشطة
private val Peach      = Color(0xFFF4C195) // اللمسات الدافئة

// بيانات كل شريحة
data class OnboardingSlideItem(
    val tag: String,
    val titleLine1: String,
    val highlightTitle: String,
    val description: String,
    val illustration: @Composable () -> Unit
)

// رسم الخلفية الانسيابية مع التموجات وشبكة النقاط المتطابقة تماماً مع التصميم
@Composable
fun OnboardingFluidBackground(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // 1. اللون الأساسي الكريمي الدافئ
        drawRect(color = WarmCream)

        // 2. التموج العلوي على اليسار (أكوا فاتح وانسيابي)
        val waveTop1 = Path().apply {
            moveTo(0f, 0f)
            lineTo(w * 0.38f, 0f)
            cubicTo(
                w * 0.26f, h * 0.08f,
                w * 0.08f, h * 0.09f,
                0f, h * 0.22f
            )
            close()
        }
        drawPath(path = waveTop1, color = LightAqua.copy(alpha = 0.38f))

        val waveTop2 = Path().apply {
            moveTo(0f, 0f)
            lineTo(w * 0.22f, 0f)
            cubicTo(
                w * 0.14f, h * 0.05f,
                w * 0.05f, h * 0.06f,
                0f, h * 0.14f
            )
            close()
        }
        drawPath(path = waveTop2, color = Turquoise.copy(alpha = 0.25f))

        // 3. التموجات السفلية على اليمين والأسفل
        val waveBottom1 = Path().apply {
            moveTo(w, h * 0.68f)
            cubicTo(
                w * 0.82f, h * 0.74f,
                w * 0.78f, h * 0.86f,
                w * 0.60f, h
            )
            lineTo(w, h)
            close()
        }
        drawPath(path = waveBottom1, color = LightAqua.copy(alpha = 0.40f))

        val waveBottom2 = Path().apply {
            moveTo(w, h * 0.76f)
            cubicTo(
                w * 0.86f, h * 0.82f,
                w * 0.82f, h * 0.92f,
                w * 0.72f, h
            )
            lineTo(w, h)
            close()
        }
        drawPath(path = waveBottom2, color = Turquoise.copy(alpha = 0.30f))

        val waveBottomAcross = Path().apply {
            moveTo(w * 0.30f, h)
            cubicTo(
                w * 0.55f, h * 0.94f,
                w * 0.80f, h * 0.96f,
                w, h * 0.88f
            )
            lineTo(w, h)
            close()
        }
        drawPath(path = waveBottomAcross, color = LightAqua.copy(alpha = 0.25f))

        // 4. مصفوفة النقاط بالأعلى يساراً (4 أعمدة × 4 صفوف)
        val dotRadius = 2.4.dp.toPx()
        val dotSpacing = 12.dp.toPx()
        val startTopX = 22.dp.toPx()
        val startTopY = 82.dp.toPx()
        for (r in 0 until 4) {
            for (c in 0 until 4) {
                drawCircle(
                    color = Turquoise.copy(alpha = 0.32f),
                    radius = dotRadius,
                    center = Offset(startTopX + c * dotSpacing, startTopY + r * dotSpacing)
                )
            }
        }

        // 5. مصفوفة النقاط بالأسفل يساراً بجوار الزرار (4 أعمدة × 4 صفوف)
        val startBottomX = 22.dp.toPx()
        val startBottomY = h - 135.dp.toPx()
        for (r in 0 until 4) {
            for (c in 0 until 4) {
                drawCircle(
                    color = Turquoise.copy(alpha = 0.32f),
                    radius = dotRadius,
                    center = Offset(startBottomX + c * dotSpacing, startBottomY + r * dotSpacing)
                )
            }
        }
    }
}

// علامات الشرطات الإشعاعية الجمالية بجوار العنوان
@Composable
fun TitleAccentRays(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(18.dp)) {
        val stroke = 3.dp.toPx()
        drawLine(
            color = DeepTeal,
            start = Offset(size.width * 0.25f, size.height * 0.88f),
            end = Offset(size.width * 0.55f, size.height * 0.18f),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )
        drawLine(
            color = DeepTeal,
            start = Offset(size.width * 0.65f, size.height * 0.72f),
            end = Offset(size.width * 0.95f, size.height * 0.32f),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )
    }
}

// شعار الكرسي الدائري لـ TeamSeat
@Composable
fun TeamSeatChairLogo(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(52.dp)
            .clip(CircleShape)
            .background(LightAqua.copy(alpha = 0.45f)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(28.dp)) {
            val w = size.width
            val h = size.height

            // الحلقة البيضاوية التركوازية
            drawArc(
                color = Turquoise,
                startAngle = 135f,
                sweepAngle = 270f,
                useCenter = false,
                topLeft = Offset(w * 0.05f, h * 0.30f),
                size = Size(w * 0.90f, h * 0.40f),
                style = Stroke(width = 2.5f, cap = StrokeCap.Round)
            )

            // مسند الكرسي
            drawRoundRect(
                color = DeepNavy,
                topLeft = Offset(w * 0.25f, h * 0.15f),
                size = Size(w * 0.50f, h * 0.38f),
                cornerRadius = CornerRadius(6f, 6f),
                style = Stroke(width = 2.5f)
            )
            // مقعد الكرسي
            drawRoundRect(
                color = DeepNavy,
                topLeft = Offset(w * 0.14f, h * 0.52f),
                size = Size(w * 0.72f, h * 0.16f),
                cornerRadius = CornerRadius(4f, 4f),
                style = Stroke(width = 2.5f)
            )
            // أرجل الكرسي
            drawLine(color = DeepNavy, start = Offset(w * 0.28f, h * 0.68f), end = Offset(w * 0.20f, h * 0.88f), strokeWidth = 2.5f, cap = StrokeCap.Round)
            drawLine(color = DeepNavy, start = Offset(w * 0.72f, h * 0.68f), end = Offset(w * 0.80f, h * 0.88f), strokeWidth = 2.5f, cap = StrokeCap.Round)
        }
    }
}

// رسمة الشريحة 1: Build Your Dream Team
@Composable
fun DreamTeamIllustration(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(220.dp)
            .clip(CircleShape)
            .background(LightAqua.copy(alpha = 0.4f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                shape = CircleShape,
                color = Color.White,
                shadowElevation = 8.dp,
                modifier = Modifier.size(90.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Outlined.Groups,
                        contentDescription = null,
                        tint = DeepTeal,
                        modifier = Modifier.size(52.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = DeepNavy
                ) {
                    Text(
                        text = "Team Lead 👑",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Turquoise
                ) {
                    Text(
                        text = "Developer 💻",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

// رسمة الشريحة 2: Manage Your Seats
@Composable
fun ManageSeatsIllustration(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(220.dp)
            .clip(CircleShape)
            .background(LightAqua.copy(alpha = 0.4f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                shape = CircleShape,
                color = Color.White,
                shadowElevation = 8.dp,
                modifier = Modifier.size(90.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Outlined.EventSeat,
                        contentDescription = null,
                        tint = DeepTeal,
                        modifier = Modifier.size(50.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Peach
            ) {
                Text(
                    text = "Seat B3 • Reserved ✓",
                    color = DeepNavy,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                )
            }
        }
    }
}

// رسمة الشريحة 3: Work Together Smarter
@Composable
fun CollaborationIllustration(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(220.dp)
            .clip(CircleShape)
            .background(LightAqua.copy(alpha = 0.4f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                shape = CircleShape,
                color = Color.White,
                shadowElevation = 8.dp,
                modifier = Modifier.size(90.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Outlined.RocketLaunch,
                        contentDescription = null,
                        tint = DeepTeal,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = DeepNavy
            ) {
                Text(
                    text = "Sync & Progress 🚀",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                )
            }
        }
    }
}

// رسمة الشريحة 4: Project Success
@Composable
fun GoalsIllustration(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(220.dp)
            .clip(CircleShape)
            .background(LightAqua.copy(alpha = 0.4f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                shape = CircleShape,
                color = Color.White,
                shadowElevation = 8.dp,
                modifier = Modifier.size(90.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Outlined.EmojiEvents,
                        contentDescription = null,
                        tint = DeepTeal,
                        modifier = Modifier.size(50.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Turquoise
            ) {
                Text(
                    text = "A4 TA Sheet Ready 📄",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                )
            }
        }
    }
}

@Composable
fun OnboardingScreen(
    onFinishOnboarding: () -> Unit = {},
    onFinish: () -> Unit = onFinishOnboarding,
    onSkip: () -> Unit = onFinishOnboarding
) {
    val items = listOf(
        OnboardingSlideItem(
            tag = "WORK SMARTER TOGETHER",
            titleLine1 = "Build Your",
            highlightTitle = "Dream Team",
            description = "Find teammates, reserve seats\nand collaborate easily!",
            illustration = { DreamTeamIllustration() }
        ),
        OnboardingSlideItem(
            tag = "SMART RESERVATION",
            titleLine1 = "Manage Your",
            highlightTitle = "Seats",
            description = "Book and organize seats\nfor your team smoothly.",
            illustration = { ManageSeatsIllustration() }
        ),
        OnboardingSlideItem(
            tag = "TEAM COLLABORATION",
            titleLine1 = "Work Together",
            highlightTitle = "Smarter",
            description = "Coordinate schedules and boost\nyour team productivity!",
            illustration = { CollaborationIllustration() }
        ),
        OnboardingSlideItem(
            tag = "PROJECT SUCCESS",
            titleLine1 = "Achieve Your",
            highlightTitle = "Goals",
            description = "Complete your project milestones\ntogether on time!",
            illustration = { GoalsIllustration() }
        )
    )

    val pagerState = rememberPagerState(pageCount = { items.size })
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // خلفية التموجات والنقاط
        OnboardingFluidBackground()

        // المحتوى الرئيسي
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // الهيدر العلوي: لوجو TeamSeat في المنتصف و زر Skip على اليمين
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp)
            ) {
                // الشعار واسم التطبيق في المنتصف
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TeamSeatChairLogo()
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Team",
                            color = DeepNavy,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = (-0.5).sp
                        )
                        Text(
                            text = "Seat",
                            color = DeepTeal,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = (-0.5).sp
                        )
                    }
                }

                // زرار Skip > في أقصى اليمين
                Row(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 10.dp)
                        .clickable { onFinishOnboarding() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Skip",
                        color = DeepTeal,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = ">",
                        color = DeepTeal,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            // السلايدر (HorizontalPager)
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { pageIndex ->
                val item = items[pageIndex]

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // مساحة الرسمة التفاعلية
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(230.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        item.illustration()
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // البادج الأنيق: — [ WORK SMARTER TOGETHER ] —
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .width(24.dp)
                                .height(1.5.dp)
                                .background(DeepTeal.copy(alpha = 0.6f))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = LightAqua.copy(alpha = 0.50f)
                        ) {
                            Text(
                                text = item.tag,
                                color = DeepTeal,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.2.sp,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 5.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .width(24.dp)
                                .height(1.5.dp)
                                .background(DeepTeal.copy(alpha = 0.6f))
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // العنوان السطر الأول
                    Text(
                        text = item.titleLine1,
                        color = DeepNavy,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center,
                        letterSpacing = (-0.5).sp
                    )

                    // العنوان السطر الثاني ومعه العلامات الجمالية //
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = item.highlightTitle,
                            color = DeepTeal,
                            fontSize = 34.sp,
                            fontWeight = FontWeight.ExtraBold,
                            textAlign = TextAlign.Center,
                            letterSpacing = (-0.5).sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        TitleAccentRays()
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // الوصف
                    Text(
                        text = item.description,
                        color = DeepNavy.copy(alpha = 0.68f),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp
                    )
                }
            }

            // الجزء السفلي: مؤشر النقاط والزرار
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // مؤشر النقاط (4 نقاط)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 20.dp)
                ) {
                    repeat(items.size) { index ->
                        val isSelected = pagerState.currentPage == index
                        Box(
                            modifier = Modifier
                                .height(8.dp)
                                .width(if (isSelected) 26.dp else 8.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) DeepTeal else SoftGray)
                        )
                    }
                }

                // زرار Next ->
                Button(
                    onClick = {
                        if (pagerState.currentPage < items.size - 1) {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        } else {
                            onFinishOnboarding()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(54.dp)
                        .shadow(6.dp, RoundedCornerShape(27.dp), spotColor = DeepTeal.copy(alpha = 0.35f)),
                    shape = RoundedCornerShape(27.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DeepTeal)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = if (pagerState.currentPage < items.size - 1) "Next" else "Get Started",
                            color = WarmCream,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = WarmCream,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OnboardingScreenPreview() {
    OnboardingScreen()
}
