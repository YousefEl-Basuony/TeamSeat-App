package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class SeatMapStatus(val label: String) {
    AVAILABLE("Available"),
    PENDING("Pending"),
    BOOKED("Booked"),
    LOCKED("Locked")
}

data class SeatMapSeat(
    val id: String,
    val role: String,
    val status: SeatMapStatus,
    val holder: String? = null
)

// ───── ألوان TeamSeat ─────
private val Navy = Color(0xFF0A344A)
private val Teal = Color(0xFF1B6F7D)
private val Aqua = Color(0xFFA9D5D4)
private val Cream = Color(0xFFFCF2EA)
private val Mist = Color(0xFFE6F2F0)
private val Gold = Color(0xFFF2B84B)

private val HeaderBrush = Brush.linearGradient(listOf(Navy, Teal))

private val DemoSeats = listOf(
    SeatMapSeat("A1", "Lead Developer", SeatMapStatus.BOOKED, "Esraa A."),
    SeatMapSeat("A2", "UI/UX Designer", SeatMapStatus.AVAILABLE),
    SeatMapSeat("B1", "Backend Engineer", SeatMapStatus.PENDING, "Esraa A."),
    SeatMapSeat("B2", "Frontend Developer", SeatMapStatus.AVAILABLE),
    SeatMapSeat("C1", "Android Developer", SeatMapStatus.BOOKED, "Esraa A."),
    SeatMapSeat("C2", "QA Engineer", SeatMapStatus.LOCKED)
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
                    InteractiveSeatMapScreen()
                }
            }
        }
    }
}

private fun SeatMapStatus.base(): Color = when (this) {
    SeatMapStatus.AVAILABLE -> Aqua
    SeatMapStatus.PENDING -> Gold
    SeatMapStatus.BOOKED -> Navy
    SeatMapStatus.LOCKED -> Color(0xFFE3ECEC)
}

private fun SeatMapStatus.text(): Color = when (this) {
    SeatMapStatus.BOOKED -> Color.White
    SeatMapStatus.LOCKED -> Color(0xFF3E5963)
    else -> Navy
}

private fun SeatMapStatus.chair(): Color = when (this) {
    SeatMapStatus.AVAILABLE -> Teal
    SeatMapStatus.BOOKED -> Color.White
    SeatMapStatus.PENDING -> Navy
    SeatMapStatus.LOCKED -> Color(0xFF6F8A92)
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
fun InteractiveSeatMapScreen(
    teamName: String = "Alpha Team",
    leaderName: String = "Esraa A.",
    seats: List<SeatMapSeat> = DemoSeats,
    onBack: () -> Unit = {},
    onAvailableSeatClick: (SeatMapSeat) -> Unit = {}
) {
    var selectedId by remember { mutableStateOf<String?>(null) }
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val filled = seats.count { it.status == SeatMapStatus.BOOKED }

    Scaffold(
        modifier = Modifier.screenBackground(),
        containerColor = Color.Transparent,
        topBar = { TopBar(title = "Seat Map", subtitle = teamName, onBack = onBack) },
        snackbarHost = {
            SnackbarHost(hostState = snackbar) { data ->
                Snackbar(
                    snackbarData = data,
                    shape = RoundedCornerShape(14.dp),
                    containerColor = Navy,
                    contentColor = Cream
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            LeaderCard(
                leader = leaderName,
                filled = filled,
                total = seats.size,
                modifier = Modifier.staggeredEntrance(0)
            )
            Legend(
                seats = seats,
                modifier = Modifier.staggeredEntrance(1)
            )
            FrontBar(modifier = Modifier.staggeredEntrance(2))

            seats.chunked(2).forEachIndexed { rowIndex, row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    row.forEachIndexed { col, seat ->
                        SeatCard(
                            seat = seat,
                            selected = selectedId == seat.id,
                            modifier = Modifier
                                .weight(1f)
                                .staggeredEntrance(rowIndex * 2 + col + 3)
                        ) {
                            if (seat.status == SeatMapStatus.AVAILABLE) {
                                selectedId = seat.id
                                onAvailableSeatClick(seat)
                            } else {
                                scope.launch {
                                    snackbar.currentSnackbarData?.dismiss()
                                    snackbar.showSnackbar(
                                        when (seat.status) {
                                            SeatMapStatus.PENDING -> "This seat has a pending request"
                                            SeatMapStatus.BOOKED -> "This seat is already booked"
                                            else -> "This seat is locked by the leader"
                                        }
                                    )
                                }
                            }
                        }
                    }
                    if (row.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun LeaderCard(
    leader: String,
    filled: Int,
    total: Int,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(24.dp)
    val bob by rememberInfiniteTransition(label = "crown").animateFloat(
        initialValue = 0f,
        targetValue = -10f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bob"
    )
    val progress by animateFloatAsState(
        targetValue = if (total == 0) 0f else filled.toFloat() / total,
        animationSpec = tween(1000, easing = FastOutSlowInEasing),
        label = "progress"
    )
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(10.dp, shape = shape, spotColor = Teal)
            .clip(shape)
            .background(HeaderBrush)
            .padding(18.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(Cream),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "👑",
                    fontSize = 27.sp,
                    modifier = Modifier.graphicsLayer { translationY = bob }
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Team Leader", color = Mist, fontSize = 12.sp)
                Text(
                    text = leader,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                AnimatedContent(
                    targetState = filled,
                    transitionSpec = {
                        (slideInVertically { it } + fadeIn()) togetherWith
                            (slideOutVertically { -it } + fadeOut())
                    },
                    label = "filled"
                ) { c ->
                    Text(
                        text = "$c / $total",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                }
                Text(text = "seats filled", color = Mist, fontSize = 12.sp)
            }
        }
        Spacer(modifier = Modifier.height(14.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.22f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .fillMaxHeight()
                    .clip(CircleShape)
                    .background(Brush.horizontalGradient(listOf(Aqua, Gold)))
            )
        }
    }
}

@Composable
private fun Legend(
    seats: List<SeatMapSeat>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SeatMapStatus.entries.forEach { status ->
            Column(
                modifier = Modifier
                    .weight(1f)
                    .shadow(3.dp, shape = RoundedCornerShape(14.dp))
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White)
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(status.base())
                            .border(1.dp, Teal.copy(alpha = 0.4f), CircleShape)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "${seats.count { it.status == status }}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Navy
                    )
                }
                Text(text = status.label, fontSize = 10.5.sp, color = Teal)
            }
        }
    }
}

@Composable
private fun FrontBar(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(Aqua.copy(alpha = 0.4f), Aqua, Aqua.copy(alpha = 0.4f))
                )
            )
            .padding(vertical = 9.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "FRONT",
            color = Navy,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            letterSpacing = 4.sp
        )
    }
}

@Composable
private fun SeatCard(
    seat: SeatMapSeat,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(22.dp)
    val bg by animateColorAsState(seat.status.base(), tween(600), label = "bg")
    val fg by animateColorAsState(seat.status.text(), tween(600), label = "fg")
    val chair by animateColorAsState(seat.status.chair(), tween(600), label = "chair")
    val pulse by rememberInfiniteTransition(label = "pulse").animateFloat(
        initialValue = 0.2f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(900), RepeatMode.Reverse),
        label = "pulseA"
    )
    val elevation by animateDpAsState(if (selected) 16.dp else 6.dp, label = "elev")
    val selScale by animateFloatAsState(
        targetValue = if (selected) 1.05f else 1f,
        animationSpec = spring(Spring.DampingRatioMediumBouncy),
        label = "sel"
    )
    val borderColor = when {
        selected -> Teal
        seat.status == SeatMapStatus.PENDING -> Navy.copy(alpha = pulse)
        else -> Color.Transparent
    }
    val badge = when (seat.status) {
        SeatMapStatus.AVAILABLE -> "+"
        SeatMapStatus.PENDING -> "…"
        SeatMapStatus.BOOKED -> "✓"
        SeatMapStatus.LOCKED -> "■"
    }

    Column(
        modifier = modifier
            .graphicsLayer {
                scaleX = selScale
                scaleY = selScale
            }
            .bounceClick(onClick)
            .shadow(elevation, shape = shape, spotColor = Teal)
            .clip(shape)
            .background(bg)
            .background(
                Brush.verticalGradient(listOf(Color.White.copy(alpha = 0.2f), Color.Transparent))
            )
            .then(
                if (seat.status == SeatMapStatus.LOCKED) {
                    Modifier.drawBehind {
                        drawRoundRect(
                            color = Color(0xFF8FA6AD),
                            style = Stroke(
                                width = 2.dp.toPx(),
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(14f, 10f))
                            ),
                            cornerRadius = CornerRadius(22.dp.toPx())
                        )
                    }
                } else {
                    Modifier
                }
            )
            .border(3.dp, borderColor, shape)
            .padding(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = seat.id,
                color = fg,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 17.sp
            )
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .clip(CircleShape)
                    .background(fg.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = badge,
                    color = fg,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        ChairIcon(color = chair, modifier = Modifier.size(56.dp))
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = seat.role,
            color = fg,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            minLines = 2,
            maxLines = 2
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = seat.holder ?: if (seat.status == SeatMapStatus.AVAILABLE) "Tap to book" else seat.status.label,
            color = fg.copy(alpha = 0.85f),
            fontSize = 11.sp
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "1 - Seat Map")
@Composable
fun InteractiveSeatMapScreenPreview() {
    MaterialTheme {
        InteractiveSeatMapScreen()
    }
}
