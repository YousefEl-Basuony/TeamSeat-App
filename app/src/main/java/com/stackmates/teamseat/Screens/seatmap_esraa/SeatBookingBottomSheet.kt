package com.stackmates.teamseat.Screens.seatmap_esraa

import com.stackmates.teamseat.core.theme.TajawalFontFamily
import com.stackmates.teamseat.core.theme.TeamSeatTheme
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// ───── ألوان TeamSeat ─────
private val Navy = Color(0xFF0A344A)
private val Teal = Color(0xFF136F78)
private val Turquoise = Color(0xFF469D9F)
private val Aqua = Color(0xFFA9D5D4)
private val Cream = Color(0xFFF8FAFA)

private val HeaderBrush = Brush.linearGradient(listOf(Navy, Teal))
private val ButtonBrush = Brush.horizontalGradient(listOf(Teal, Turquoise))

private enum class BookingPhase {
    Form,
    Loading,
    Success
}

@Composable
private fun ChairIcon(
    color: Color,
    modifier: Modifier = Modifier.size(40.dp)
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
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
fun SeatBookingBottomSheetContent(
    onConfirmBookingClick: (String) -> Unit,
    onDismiss: () -> Unit
) {
    SeatBookingBottomSheet(
        seatId = "S4",
        role = "Android Developer",
        teamName = "TeamSeat-App",
        onDismiss = onDismiss,
        onConfirm = onConfirmBookingClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeatBookingBottomSheet(
    seatId: String,
    role: String,
    teamName: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    fun closeThen(after: () -> Unit) {
        scope.launch { sheetState.hide() }.invokeOnCompletion { after() }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Cream,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        SeatBookingSheetContent(
            seatId = seatId,
            role = role,
            teamName = teamName,
            onCancel = { closeThen(onDismiss) },
            onConfirm = { note -> closeThen { onConfirm(note) } }
        )
    }
}

@Composable
fun SeatBookingSheetContent(
    seatId: String,
    role: String,
    teamName: String,
    onCancel: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var note by remember { mutableStateOf("") }
    var phase by remember { mutableStateOf(BookingPhase.Form) }

    LaunchedEffect(phase) {
        when (phase) {
            BookingPhase.Loading -> {
                delay(1000)
                phase = BookingPhase.Success
            }
            BookingPhase.Success -> {
                delay(1500)
                onConfirm(note)
            }
            BookingPhase.Form -> Unit
        }
    }

    ProvideTextStyle(LocalTextStyle.current.copy(fontFamily = TajawalFontFamily)) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 20.dp)
            .padding(bottom = 20.dp)
    ) {
        AnimatedContent(targetState = phase == BookingPhase.Success, label = "sheet") { done ->
            if (done) {
                SuccessPanel(seatId = seatId, role = role)
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(58.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .background(HeaderBrush),
                            contentAlignment = Alignment.Center
                        ) {
                            ChairIcon(color = Color.White, modifier = Modifier.size(34.dp))
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = "Book Seat $seatId",
                                fontWeight = FontWeight.Bold,
                                fontSize = 21.sp,
                                color = Navy
                            )
                            Text(
                                text = teamName,
                                fontSize = 13.sp,
                                color = Teal
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Aqua)
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Seat Role", color = Navy, fontSize = 13.sp)
                        Text(
                            text = role,
                            color = Navy,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    OutlinedTextField(
                        value = note,
                        onValueChange = { note = it },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = LocalTextStyle.current.copy(fontFamily = TajawalFontFamily),
                        minLines = 3,
                        enabled = phase == BookingPhase.Form,
                        label = { Text("Quick message to the leader") },
                        placeholder = { Text("e.g., I have experience in this role...") },
                        shape = RoundedCornerShape(14.dp),
                        colors = fieldColors()
                    )

                    GradientButton(
                        text = "Confirm Booking",
                        loading = phase == BookingPhase.Loading,
                        onClick = { phase = BookingPhase.Loading }
                    )

                    TextButton(
                        onClick = onCancel,
                        enabled = phase == BookingPhase.Form,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Cancel", color = Navy)
                    }
                }
            }
        }
    }
    }
}

@Composable
private fun SuccessPanel(seatId: String, role: String) {
    val inspection = LocalInspectionMode.current
    val scale = remember { Animatable(if (inspection) 1f else 0f) }

    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(88.dp)
                .graphicsLayer {
                    scaleX = scale.value
                    scaleY = scale.value
                }
                .clip(CircleShape)
                .background(Brush.linearGradient(listOf(Turquoise, Teal))),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "✓",
                color = Color.White,
                fontSize = 44.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Text(
            text = "Request sent!",
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            color = Navy
        )

        Text(
            text = "Seat $seatId · $role is now waiting for the leader's approval",
            fontSize = 14.sp,
            color = Teal,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "2 - Booking Sheet")
@Composable
fun SeatBookingSheetPreview() {
    TeamSeatTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Navy.copy(alpha = 0.62f)),
            contentAlignment = Alignment.BottomCenter
        ) {
            Surface(
                color = Cream,
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
            ) {
                Box(modifier = Modifier.padding(top = 12.dp)) {
                    SeatBookingSheetContent(
                        seatId = "A2",
                        role = "UI/UX Designer",
                        teamName = "Alpha Team",
                        onCancel = {},
                        onConfirm = {}
                    )
                }
            }
        }
    }
}
