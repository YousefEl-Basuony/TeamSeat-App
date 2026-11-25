package com.stackmates.teamseat.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material.icons.rounded.HealthAndSafety
import androidx.compose.material.icons.rounded.LaptopMac
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Map
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp

private val IllusDeepTeal = Color(0xFF136F78)
private val IllusSoftMint = Color(0xFF4DB6AC)
private val IllusWarmOrange = Color(0xFFF97316)
private val IllusBackdrop = Color(0xFFE0F2F1)

/**
 * 🎨 رسم توضيحي متعدد الطبقات (Duotone Vector Illustration)
 */
@Composable
fun ProjectCategoryIllustration(
    category: String,
    modifier: Modifier = Modifier
) {
    val lower = category.lowercase()
    val isNavigationOrService = lower.contains("ar") ||
        lower.contains("navigation") ||
        lower.contains("campus") ||
        lower.contains("ai") ||
        lower.contains("sanayee") ||
        lower.contains("صنايعي") ||
        lower.contains("ملاحة")

    val isHealth = lower.contains("clinic") ||
        lower.contains("health") ||
        lower.contains("طب")

    Box(
        modifier = modifier
            .size(56.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(IllusBackdrop),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val w = size.width
            val h = size.height

            drawCircle(
                color = IllusSoftMint.copy(alpha = 0.28f),
                radius = w * 0.42f,
                center = Offset(w * 0.72f, h * 0.72f)
            )

            if (isNavigationOrService) {
                val foldPath = Path().apply {
                    moveTo(w * 0.15f, h * 0.28f)
                    lineTo(w * 0.45f, h * 0.2f)
                    lineTo(w * 0.75f, h * 0.3f)
                    lineTo(w * 0.75f, h * 0.78f)
                    lineTo(w * 0.45f, h * 0.7f)
                    lineTo(w * 0.15f, h * 0.78f)
                    close()
                }
                drawPath(foldPath, color = Color.White.copy(alpha = 0.65f))
            } else {
                drawCircle(
                    color = Color.White.copy(alpha = 0.55f),
                    radius = w * 0.34f,
                    center = Offset(w * 0.28f, h * 0.3f)
                )
            }

            drawCircle(
                color = IllusWarmOrange,
                radius = 4.dp.toPx(),
                center = Offset(w * 0.82f, h * 0.2f)
            )
        }

        when {
            isNavigationOrService -> {
                Icon(
                    imageVector = Icons.Rounded.Map,
                    contentDescription = null,
                    tint = IllusSoftMint,
                    modifier = Modifier
                        .size(30.dp)
                        .offset(y = 4.dp)
                )
                Icon(
                    imageVector = Icons.Rounded.LocationOn,
                    contentDescription = null,
                    tint = IllusDeepTeal,
                    modifier = Modifier
                        .size(24.dp)
                        .offset(x = 2.dp, y = (-7).dp)
                )
            }

            isHealth -> {
                Icon(
                    imageVector = Icons.Rounded.HealthAndSafety,
                    contentDescription = null,
                    tint = IllusDeepTeal,
                    modifier = Modifier.size(32.dp)
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(6.dp)
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(IllusWarmOrange)
                )
            }

            else -> {
                Icon(
                    imageVector = Icons.Rounded.LaptopMac,
                    contentDescription = null,
                    tint = IllusDeepTeal,
                    modifier = Modifier.size(32.dp)
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(5.dp)
                        .size(18.dp)
                        .clip(CircleShape)
                        .background(IllusSoftMint),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Code,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }
    }
}
