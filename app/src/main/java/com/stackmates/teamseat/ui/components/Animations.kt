package com.stackmates.teamseat.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.coroutines.delay

/**
 * 🎯 تأثير ضغط مطاطي ناعم (Flutter-style Micro-interaction)
 * يعطي إحساساً حياً وسريع الاستجابة عند الضغط على الكروت والفلاتر والأزرار.
 */
fun Modifier.bounceClick(
    enabled: Boolean = true,
    scaleDown: Float = 0.96f,
    onClick: () -> Unit
): Modifier = composed {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed && enabled) scaleDown else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "bounceClickScale"
    )

    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .clickable(
            enabled = enabled,
            interactionSource = interactionSource,
            indication = null,
            onClick = onClick
        )
}

/**
 * ✨ ظهور متتابع ناعم للعناصر (Staggered Entrance Animation)
 * تأخير 50ms لكل عنصر حسب ترتيبه لإعطاء دخول انسيابي احترافي.
 */
@Composable
fun StaggeredAnimatedItem(
    index: Int,
    delayPerItemMs: Long = 50L,
    content: @Composable () -> Unit
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(index * delayPerItemMs)
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(
            animationSpec = tween(durationMillis = 320, easing = FastOutSlowInEasing)
        ) + slideInVertically(
            animationSpec = tween(durationMillis = 340, easing = FastOutSlowInEasing),
            initialOffsetY = { fullHeight -> fullHeight / 4 }
        )
    ) {
        content()
    }
}

/**
 * 📊 تحريك سلس لأشرطة التقدم والنسب المئوية من 0% إلى القيمة المستهدفة خلال 800ms.
 */
@Composable
fun rememberAnimatedProgress(
    targetProgress: Float,
    durationMillis: Int = 800
): State<Float> {
    var started by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        started = true
    }

    return animateFloatAsState(
        targetValue = if (started) targetProgress.coerceIn(0f, 1f) else 0f,
        animationSpec = tween(
            durationMillis = durationMillis,
            easing = FastOutSlowInEasing
        ),
        label = "smoothProgressAnimation"
    )
}
