package com.stackmates.teamseat.Screens.seatmap_esraa

import com.stackmates.teamseat.core.theme.TajawalFontFamily
import com.stackmates.teamseat.core.theme.TeamSeatTheme
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

// ───── ألوان TeamSeat ─────
private val Navy = Color(0xFF0A344A)
private val Teal = Color(0xFF136F78)
private val Turquoise = Color(0xFF469D9F)
private val Aqua = Color(0xFFA9D5D4)
private val Cream = Color(0xFFF8FAFA)
private val Mist = Color(0xFFE6F2F0)

private val HeaderBrush = Brush.linearGradient(listOf(Navy, Teal))
private val ButtonBrush = Brush.horizontalGradient(listOf(Teal, Turquoise))

@Composable
private fun ChairIcon(
    color: Color,
    modifier: Modifier = Modifier.size(40.dp)
) {
    Canvas(modifier = modifier) {
        val canvasSize = this.size
        val w = canvasSize.width
        val h = canvasSize.height
        drawRoundRect(
            color = color,
            topLeft = Offset(w * 0.2f, 0f),
            size = Size(w * 0.6f, h * 0.5f),
            cornerRadius = CornerRadius(w * 0.15f, w * 0.15f)
        )
        drawRoundRect(
            color = color,
            topLeft = Offset(0f, h * 0.38f),
            size = Size(w * 0.2f, h * 0.34f),
            cornerRadius = CornerRadius(w * 0.1f, w * 0.1f)
        )
        drawRoundRect(
            color = color,
            topLeft = Offset(w * 0.8f, h * 0.38f),
            size = Size(w * 0.2f, h * 0.34f),
            cornerRadius = CornerRadius(w * 0.1f, w * 0.1f)
        )
        drawRoundRect(
            color = color,
            topLeft = Offset(w * 0.12f, h * 0.55f),
            size = Size(w * 0.76f, h * 0.2f),
            cornerRadius = CornerRadius(w * 0.1f, w * 0.1f)
        )
        drawRect(
            color = color,
            topLeft = Offset(w * 0.46f, h * 0.75f),
            size = Size(w * 0.08f, h * 0.13f)
        )
        drawRoundRect(
            color = color,
            topLeft = Offset(w * 0.2f, h * 0.88f),
            size = Size(w * 0.6f, h * 0.08f),
            cornerRadius = CornerRadius(w * 0.04f, w * 0.04f)
        )
    }
}

@Composable
private fun TopBar(
    title: String,
    subtitle: String,
    onBack: () -> Unit
) {
    val shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, shape = shape, spotColor = Navy)
            .clip(shape)
            .background(HeaderBrush)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(start = 8.dp, end = 16.dp, top = 12.dp, bottom = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Text(
                    text = "←",
                    color = Cream,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Column {
                Text(
                    text = title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
                Text(
                    text = subtitle,
                    color = Mist,
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
private fun Modifier.staggeredEntrance(index: Int): Modifier {
    val inspection = LocalInspectionMode.current
    val progress = remember { Animatable(if (inspection) 1f else 0f) }
    LaunchedEffect(Unit) {
        delay(index * 90L)
        progress.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = 0.65f,
                stiffness = Spring.StiffnessLow
            )
        )
    }
    return this.graphicsLayer {
        val p = progress.value
        alpha = p.coerceIn(0f, 1f)
        val s = 0.8f + 0.2f * p
        scaleX = s
        scaleY = s
        translationY = (1f - p) * 70f
    }
}

@Composable
private fun Modifier.bounceClick(onClick: () -> Unit): Modifier {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.94f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "press"
    )
    return this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .clickable(
            interactionSource = interaction,
            indication = null,
            onClick = onClick
        )
}

private fun Modifier.screenBackground(): Modifier =
    background(Brush.verticalGradient(listOf(Cream, Mist)))

@Composable
private fun GradientButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(16.dp)
    val buttonAlpha by animateFloatAsState(
        targetValue = if (enabled) 1f else 0.45f,
        label = "btnAlpha"
    )
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp)
            .graphicsLayer { alpha = buttonAlpha }
            .shadow(
                elevation = if (enabled) 8.dp else 0.dp,
                shape = shape,
                spotColor = Teal
            )
            .clip(shape)
            .background(ButtonBrush)
            .then(
                if (enabled && !loading) {
                    Modifier.bounceClick(onClick)
                } else {
                    Modifier
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        AnimatedContent(targetState = loading, label = "btnContent") { isLoading ->
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 2.5.dp
                )
            } else {
                Text(
                    text = text,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
private fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Teal,
    unfocusedBorderColor = Turquoise,
    focusedLabelColor = Teal,
    unfocusedLabelColor = Teal.copy(alpha = 0.8f),
    cursorColor = Teal,
    focusedContainerColor = Color.White,
    unfocusedContainerColor = Color.White,
    disabledContainerColor = Color.White.copy(alpha = 0.6f)
)

@Composable
private fun StepIndicator(
    current: Int,
    modifier: Modifier = Modifier,
    total: Int = 2
) {
    val inspection = LocalInspectionMode.current
    var started by remember { mutableStateOf(inspection) }
    LaunchedEffect(Unit) { started = true }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(total) { i ->
            val fraction by animateFloatAsState(
                targetValue = if (i < current && started) 1f else 0f,
                animationSpec = tween(
                    durationMillis = 700,
                    delayMillis = i * 250,
                    easing = FastOutSlowInEasing
                ),
                label = "step$i"
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(7.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Aqua)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(4.dp))
                        .background(ButtonBrush)
                )
            }
        }
    }
}

@Composable
fun CreateTeamInfoScreen(
    onNextStepClick: (String, String, String, Int) -> Unit,
    onBackClick: () -> Unit
) {
    CreateTeamInfoScreen(
        onBack = onBackClick,
        onNext = onNextStepClick
    )
}

@Composable
fun CreateTeamInfoScreen(
    onBack: () -> Unit = {},
    onNext: (name: String, course: String, description: String, seatCount: Int) -> Unit = { _, _, _, _ -> }
) {
    var name by remember { mutableStateOf("") }
    var course by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var seatCount by remember { mutableFloatStateOf(6f) }
    val count = seatCount.roundToInt()
    val valid = name.isNotBlank() && course.isNotBlank()

    ProvideTextStyle(LocalTextStyle.current.copy(fontFamily = TajawalFontFamily)) {
        Scaffold(
            modifier = Modifier.screenBackground(),
            containerColor = Color.Transparent,
            topBar = { TopBar("Create Team", "Step 1 of 2 · Info", onBack) }
        ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StepIndicator(current = 1, modifier = Modifier.staggeredEntrance(0))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Project Name") },
                textStyle = LocalTextStyle.current.copy(fontFamily = TajawalFontFamily),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .staggeredEntrance(1),
                shape = RoundedCornerShape(14.dp),
                colors = fieldColors()
            )
            OutlinedTextField(
                value = course,
                onValueChange = { course = it },
                label = { Text("Course") },
                textStyle = LocalTextStyle.current.copy(fontFamily = TajawalFontFamily),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .staggeredEntrance(2),
                shape = RoundedCornerShape(14.dp),
                colors = fieldColors()
            )
            OutlinedTextField(
                value = desc,
                onValueChange = { desc = it },
                label = { Text("Description / Goal") },
                textStyle = LocalTextStyle.current.copy(fontFamily = TajawalFontFamily),
                minLines = 4,
                modifier = Modifier
                    .fillMaxWidth()
                    .staggeredEntrance(3),
                shape = RoundedCornerShape(14.dp),
                colors = fieldColors()
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .staggeredEntrance(4)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Required Seat Count",
                        fontWeight = FontWeight.SemiBold,
                        color = Navy
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(ButtonBrush)
                            .padding(horizontal = 14.dp, vertical = 5.dp)
                    ) {
                        AnimatedContent(
                            targetState = count,
                            transitionSpec = {
                                if (targetState > initialState) {
                                    (slideInVertically { it } + fadeIn()) togetherWith
                                        (slideOutVertically { -it } + fadeOut())
                                } else {
                                    (slideInVertically { -it } + fadeIn()) togetherWith
                                        (slideOutVertically { it } + fadeOut())
                                }
                            },
                            label = "count"
                        ) { c ->
                            Text(
                                text = "$c Seats",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
                Slider(
                    value = seatCount,
                    onValueChange = { seatCount = it },
                    valueRange = 2f..8f,
                    steps = 5,
                    colors = SliderDefaults.colors(
                        thumbColor = Teal,
                        activeTrackColor = Teal,
                        inactiveTrackColor = Aqua,
                        activeTickColor = Cream,
                        inactiveTickColor = Turquoise
                    )
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "2", fontSize = 12.sp, color = Navy)
                    Text(text = "8", fontSize = 12.sp, color = Navy)
                }
                Spacer(modifier = Modifier.height(12.dp))
                SeatPreviewRow(count = count)
            }

            GradientButton(
                text = "Next",
                enabled = valid,
                modifier = Modifier.staggeredEntrance(5),
                onClick = { onNext(name, course, desc, count) }
            )
        }
        }
    }
}

@Composable
private fun SeatPreviewRow(count: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(8) { i ->
            val active = i < count
            val color by animateColorAsState(
                targetValue = if (active) Teal else Aqua,
                animationSpec = tween(300),
                label = "mini$i"
            )
            val scale by animateFloatAsState(
                targetValue = if (active) 1f else 0.7f,
                animationSpec = spring(Spring.DampingRatioMediumBouncy),
                label = "miniScale$i"
            )
            ChairIcon(
                color = color,
                modifier = Modifier
                    .size(30.dp)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "3 - Create Team Info")
@Composable
fun CreateTeamInfoScreenPreview() {
    TeamSeatTheme {
        CreateTeamInfoScreen()
    }
}
