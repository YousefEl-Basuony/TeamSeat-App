package com.stackmates.teamseat.Screens.admin_youssef

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stackmates.teamseat.core.theme.*
import kotlin.math.roundToInt

/**
 * 🔒 شاشة تأكيد واعتماد الفريق (Lock Team Modal Sheet)
 * مصممة بخط كايرو (Cairo) العصري المريح، وعناصر بصرية تفاعلية تجذب المستخدم (Visual Delight).
 * مسؤولية: يوسف (Youssef - Team Lead & Backend)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LockTeamDialog(
    teamName: String = "StackMates (RoleSpot)",
    totalSeats: Int = 6,
    bookedSeats: Int = 6,
    teachingAssistant: String = "م. شهد أحمد عبدالسلام",
    supervisorDoctor: String = "د. محمد كاظم",
    onConfirmLock: () -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    var isConfirmed by remember { mutableStateOf(false) }
    var isLockedSuccess by remember { mutableStateOf(false) }

    // أنيميشن النبض للهالة الذهبية
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepNavy.copy(alpha = 0.85f))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onDismiss
            ),
        contentAlignment = Alignment.Center
    ) {
        // بطاقة الديزاين المركزية الفخمة (Card)
        Card(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {} // منع إغلاق الديالوج عند النقر على محتوى الكارت
                )
                .shadow(
                    elevation = 24.dp,
                    shape = RoundedCornerShape(32.dp),
                    spotColor = Turquoise.copy(alpha = 0.35f)
                ),
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = WarmCream),
            border = BorderStroke(1.5.dp, SoftBeige)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 22.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 1. شريط علوي صغير مع زر إغلاق ناعم
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Peach.copy(alpha = 0.3f),
                        border = BorderStroke(1.dp, Peach.copy(alpha = 0.7f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .clip(CircleShape)
                                    .background(DeepTeal)
                            )
                            Text(
                                text = "إجراء إداري رسمي",
                                fontFamily = CairoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.5.sp,
                                color = DeepNavy
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(1.dp, SoftGray.copy(alpha = 0.5f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "إغلاق",
                            tint = DeepNavy,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // 2. الهيرو المرئي التفاعلي: القفل الذهبي مع الهالة النبضية
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    // حلقة متوهجة نابضة
                    Box(
                        modifier = Modifier
                            .size(96.dp)
                            .scale(pulseScale)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(
                                        Peach.copy(alpha = 0.45f),
                                        Turquoise.copy(alpha = 0.15f),
                                        Color.Transparent
                                    )
                                )
                            )
                    )

                    // دائرة القفل المركزية
                    Box(
                        modifier = Modifier
                            .size(74.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(
                                        DeepNavy,
                                        DeepTeal
                                    )
                                )
                            )
                            .border(2.5.dp, Peach, CircleShape)
                            .shadow(8.dp, CircleShape, spotColor = DeepNavy),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isConfirmed) Icons.Default.Lock else Icons.Default.LockOpen,
                            contentDescription = "قفل",
                            tint = Peach,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    // شارة التوثيق
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.BottomEnd)
                            .offset(x = (-2).dp, y = (-2).dp)
                            .clip(CircleShape)
                            .background(Turquoise)
                            .border(1.5.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                // 3. العناوين بخط كايرو العصري
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "اعتماد وقفل التيم نهائياً",
                        fontFamily = CairoFontFamily,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 22.sp,
                        color = DeepNavy,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "تجميد المقاعد وتجهيز كشف التسليم الرسمي للمعيدة",
                        fontFamily = CairoFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 13.5.sp,
                        color = DeepNavy.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center
                    )
                }

                // 4. تمثيل مرئي جذاب للكراسي المكتملة (The 6 Visual Seats Strip)
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Groups,
                                    contentDescription = null,
                                    tint = DeepTeal,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = teamName,
                                    fontFamily = CairoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = DeepNavy
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Turquoise.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = "مكتمل 6/6 ✓",
                                    fontFamily = CairoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.5.sp,
                                    color = DeepTeal,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        // الأفاتار التفاعلي للمقاعد الـ 6
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            val roles = listOf("👑 قائد", "Android", "Backend", "UI/UX", "Flutter", "AI")
                            roles.forEachIndexed { index, role ->
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(34.dp)
                                            .clip(CircleShape)
                                            .background(
                                                if (index == 0) DeepTeal else LightAqua.copy(alpha = 0.5f)
                                            )
                                            .border(
                                                1.dp,
                                                if (index == 0) Peach else Turquoise,
                                                CircleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "${index + 1}",
                                            fontFamily = CairoFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = if (index == 0) Color.White else DeepNavy
                                        )
                                    }
                                    Text(
                                        text = role,
                                        fontFamily = CairoFontFamily,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 9.5.sp,
                                        color = DeepNavy.copy(alpha = 0.8f),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }

                // 5. بطاقة البيانات السريعة للإشراف الأكاديمي
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    InfoPillCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Outlined.School,
                        label = "المعيدة المشرفة",
                        value = "م. شهد أحمد"
                    )
                    InfoPillCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Outlined.Person,
                        label = "أستاذ المادة",
                        value = "د. محمد كاظم"
                    )
                }

                // 6. صندوق الأمان الملون (Safety Info)
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = SoftBeige.copy(alpha = 0.8f),
                    border = BorderStroke(1.dp, Peach.copy(alpha = 0.7f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Peach
                        ) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = null,
                                tint = DeepNavy,
                                modifier = Modifier
                                    .padding(6.dp)
                                    .size(16.dp)
                            )
                        }
                        Text(
                            text = "بمجرد القفل: يتم تجميد الكراسي وتصدير الكشف فوراً لمنع أي تعديل أو انسحاب.",
                            fontFamily = CairoFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp,
                            color = DeepNavy,
                            lineHeight = 17.sp
                        )
                    }
                }

                // 7. زر الإجراء التفاعلي الجذاب (Interactive Action Slider / Button)
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // الزر الرئيسي الجذاب بتدرج ألوان فخم
                    Button(
                        onClick = {
                            isConfirmed = true
                            onConfirmLock()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .shadow(
                                elevation = 8.dp,
                                shape = RoundedCornerShape(18.dp),
                                spotColor = DeepNavy.copy(alpha = 0.4f)
                            ),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DeepNavy,
                            contentColor = Color.White
                        ),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = Peach,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "تأكيد وقفل التيم رسميـاً",
                                fontFamily = CairoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = Turquoise,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    // زر الإلغاء الشفاف الأنيق
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "الرجوع والتعديل لاحقاً",
                            fontFamily = CairoFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = DeepTeal
                        )
                    }
                }
            }
        }
    }
}

/**
 * 🏷️ كبسولة عرض بيانات مختصرة وأنيقة
 */
@Composable
private fun InfoPillCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    value: String
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.4f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = LightAqua.copy(alpha = 0.35f)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = DeepTeal,
                    modifier = Modifier
                        .padding(6.dp)
                        .size(16.dp)
                )
            }

            Column {
                Text(
                    text = label,
                    fontFamily = CairoFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 10.5.sp,
                    color = DeepNavy.copy(alpha = 0.6f)
                )
                Text(
                    text = value,
                    fontFamily = CairoFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.5.sp,
                    color = DeepNavy,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LockTeamDialogAttractivePreview() {
    TeamSeatTheme {
        LockTeamDialog()
    }
}
