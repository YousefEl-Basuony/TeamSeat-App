package com.stackmates.teamseat.Screens.auth_noha

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

// درجات الألوان المتطابقة 100% مع التصميم
private val DarkNavyBackground = Color(0xFF092938) // كحلي غامق داكن وفخم
private val DeepTealWave       = Color(0xFF135061) // موجة التركواز الداكن
private val BrightTurquoise    = Color(0xFF26B4A6) // تركواز زاهي مضيء لكلمة Seat واللوجو
private val PeachSandWave      = Color(0xFFF2C6AA) // موجة الرمل الخوخي الدافئة
private val WarmCreamWave      = Color(0xFFFAF1E8) // موجة البيج الفاتحة في الأسفل
private val InactiveDotColor   = Color(0xFF8BA5A8) // لون النقاط غير النشطة

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit = {},
    onNavigateToNext: () -> Unit = onSplashFinished
) {
    LaunchedEffect(Unit) {
        delay(2000L) // مدة ثانيتين
        onSplashFinished()
        onNavigateToNext()
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // 1. رسم التموجات الانسيابية الدقيقة
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // الخلفية الكحلية الداكنة الفخمة
            drawRect(color = DarkNavyBackground)

            // انحناء علوي ناعم أعلى اليمين
            val topWave = Path().apply {
                moveTo(w * 0.35f, 0f)
                cubicTo(w * 0.55f, h * 0.12f, w * 0.78f, h * 0.18f, w, h * 0.22f)
                lineTo(w, 0f)
                close()
            }
            drawPath(topWave, color = DeepTealWave.copy(alpha = 0.55f))

            // تموج سفلي 1 (تركواز غامق)
            val wave1 = Path().apply {
                moveTo(0f, h * 0.65f)
                cubicTo(w * 0.25f, h * 0.74f, w * 0.60f, h * 0.62f, w, h * 0.68f)
                lineTo(w, h)
                lineTo(0f, h)
                close()
            }
            drawPath(wave1, color = DeepTealWave)

            // تموج سفلي 2 (خوخي رملي دافئ من اليمين)
            val wave2 = Path().apply {
                moveTo(w * 0.10f, h * 0.82f)
                cubicTo(w * 0.40f, h * 0.76f, w * 0.70f, h * 0.66f, w, h * 0.70f)
                lineTo(w, h)
                lineTo(w * 0.10f, h)
                close()
            }
            drawPath(wave2, color = PeachSandWave)

            // تموج سفلي 3 (بيج فاتح دافئ في القاعدة)
            val wave3 = Path().apply {
                moveTo(0f, h * 0.78f)
                cubicTo(w * 0.35f, h * 0.85f, w * 0.68f, h * 0.76f, w, h * 0.82f)
                lineTo(w, h)
                lineTo(0f, h)
                close()
            }
            drawPath(wave3, color = WarmCreamWave)
        }

        // 2. المحتوى في المنتصف (اللوجو البارز والكلام الواضح)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // لوجو الكرسي الأبيض البارز مع الحلقة التركواز
            Canvas(modifier = Modifier.size(135.dp)) {
                val w = size.width
                val h = size.height

                // الحلقة التركواز البيضاوية الواضحة
                drawArc(
                    color = BrightTurquoise,
                    startAngle = 135f,
                    sweepAngle = 270f,
                    useCenter = false,
                    topLeft = Offset(w * 0.05f, h * 0.30f),
                    size = Size(w * 0.90f, h * 0.40f),
                    style = Stroke(width = 9f, cap = StrokeCap.Round)
                )

                // مسند الكرسي الأبيض المليان
                drawRoundRect(
                    color = Color.White,
                    topLeft = Offset(w * 0.29f, h * 0.14f),
                    size = Size(w * 0.42f, h * 0.38f),
                    cornerRadius = CornerRadius(28f, 28f)
                )

                // مقعد الكرسي الأفقي العريض
                drawRoundRect(
                    color = Color.White,
                    topLeft = Offset(w * 0.16f, h * 0.53f),
                    size = Size(w * 0.68f, h * 0.14f),
                    cornerRadius = CornerRadius(16f, 16f)
                )

                // أرجل الكرسي العريضة
                drawLine(
                    color = Color.White,
                    start = Offset(w * 0.35f, h * 0.67f),
                    end = Offset(w * 0.21f, h * 0.88f),
                    strokeWidth = 16f,
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = Color.White,
                    start = Offset(w * 0.65f, h * 0.67f),
                    end = Offset(w * 0.79f, h * 0.88f),
                    strokeWidth = 16f,
                    cap = StrokeCap.Round
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // اسم التطبيق: Team بالأبيض و Seat بالتركواز
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.White, fontWeight = FontWeight.Bold)) {
                        append("Team")
                    }
                    withStyle(style = SpanStyle(color = BrightTurquoise, fontWeight = FontWeight.Bold)) {
                        append("Seat")
                    }
                },
                fontSize = 44.sp,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // الشعار الترحيبي سطرين
            Text(
                text = "Find your team.\nTake your seat.",
                color = Color.White.copy(alpha = 0.92f),
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                lineHeight = 25.sp
            )
        }

        // 3. مؤشر الـ 3 نقط تحت على الموجة الفاتحة
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 54.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(InactiveDotColor)
            )
            Box(
                modifier = Modifier
                    .size(11.dp)
                    .clip(CircleShape)
                    .background(DarkNavyBackground)
            )
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(InactiveDotColor)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen()
}