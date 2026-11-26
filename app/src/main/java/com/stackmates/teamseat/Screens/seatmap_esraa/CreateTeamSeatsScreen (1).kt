package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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

// ───── ألوان TeamSeat ─────
private val Navy = Color(0xFF0A344A)
private val Teal = Color(0xFF1B6F7D)
private val Turquoise = Color(0xFF469D9F)
private val Aqua = Color(0xFFA9D5D4)
private val Cream = Color(0xFFFCF2EA)
private val Mist = Color(0xFFE6F2F0)

private val HeaderBrush = Brush.linearGradient(listOf(Navy, Teal))
private val ButtonBrush = Brush.horizontalGradient(listOf(Teal, Turquoise))

private val SeatRoles = listOf(
    "Lead Developer",
    "UI/UX Designer",
    "Backend Engineer",
    "Frontend Developer",
    "Android Developer",
    "QA Engineer"
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Cream
                ) {
                    CreateTeamSeatsScreen(seatCount = 6)
                }
            }
        }
    }
}

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
fun CreateTeamSeatsScreen(
    seatCount: Int = 6,
    onBack: () -> Unit = {},
    onPublish: (List<String>) -> Unit = {}
) {
    val roles = remember {
        mutableStateListOf<String>().apply {
            repeat(seatCount) { add(SeatRoles[it % SeatRoles.size]) }
        }
    }
    var publishing by remember { mutableStateOf(false) }

    LaunchedEffect(publishing) {
        if (publishing) {
            delay(1000)
            onPublish(roles.toList())
            publishing = false
        }
    }

    Scaffold(
        modifier = Modifier.screenBackground(),
        containerColor = Color.Transparent,
        topBar = { TopBar("Create Team", "Step 2 of 2 · Seats", onBack) },
        bottomBar = {
            Surface(color = Cream, shadowElevation = 12.dp) {
                GradientButton(
                    text = "Publish Team",
                    loading = publishing,
                    modifier = Modifier
                        .navigationBarsPadding()
                        .padding(16.dp),
                    onClick = { publishing = true }
                )
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                StepIndicator(current = 2)
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = "Assign a role to each seat",
                    fontWeight = FontWeight.Bold,
                    fontSize = 19.sp,
                    color = Navy
                )
                Text(
                    text = "$seatCount seats",
                    fontSize = 13.sp,
                    color = Teal
                )
            }
            itemsIndexed(roles) { index, role ->
                SeatRoleRow(
                    seatNumber = index + 1,
                    role = role,
                    modifier = Modifier.staggeredEntrance(index)
                ) {
                    roles[index] = it
                }
            }
        }
    }
}

@Composable
private fun SeatRoleRow(
    seatNumber: Int,
    role: String,
    modifier: Modifier = Modifier,
    onRoleChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val shape = RoundedCornerShape(18.dp)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .shadow(4.dp, shape = shape, spotColor = Teal)
            .clip(shape)
            .background(Color.White)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(HeaderBrush),
            contentAlignment = Alignment.Center
        ) {
            ChairIcon(color = Color.White, modifier = Modifier.size(26.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Seat $seatNumber",
                fontSize = 12.sp,
                color = Teal,
                fontWeight = FontWeight.SemiBold
            )
            Box {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { expanded = true }
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AnimatedContent(
                        targetState = role,
                        transitionSpec = {
                            (slideInVertically { it } + fadeIn()) togetherWith
                                (slideOutVertically { -it } + fadeOut())
                        },
                        label = "role"
                    ) { r ->
                        Text(
                            text = r,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Navy
                        )
                    }
                    Text(
                        text = "⌄",
                        color = Teal,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(Cream)
                ) {
                    SeatRoles.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(text = option, color = Navy) },
                            onClick = {
                                onRoleChange(option)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "4 - Create Team Seats")
@Composable
fun CreateTeamSeatsScreenPreview() {
    MaterialTheme {
        CreateTeamSeatsScreen(seatCount = 6)
    }
}
