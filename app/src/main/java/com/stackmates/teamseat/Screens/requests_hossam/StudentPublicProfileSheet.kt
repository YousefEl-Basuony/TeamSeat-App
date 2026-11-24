package com.stackmates.teamseat.Screens.requests_hossam

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Launch
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stackmates.teamseat.core.data.MockDataProvider
import com.stackmates.teamseat.core.model.User
import com.stackmates.teamseat.core.theme.*

/**
 * ⚡ المسؤول عن المكون: حسام (Hossam) — [HOSSAM-2]
 * بطاقة معاينة بروفايل أي طالب منضم أو متقدم (Student Public Profile BottomSheet)
 * تصميم عصري فاخر بهوية ألوان TeamSeat الرسمية (Color.kt) وخط Cairo
 * يقدم نظرة شاملة على المهارات، والروابط، والرقم الجامعي، وحالة الفريق.
 */
@Composable
fun StudentPublicProfileSheet(
    student: User = MockDataProvider.saraStudent,
    onOpenGithubClick: (url: String) -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    val isTeamMember = !student.currentTeamId.isNullOrEmpty()

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.5f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // =========================================================================
                // 0. مقبض السحب العلوي (Sheet Handle) + زر الإغلاق
                // =========================================================================
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    // مقبض السحب في المنتصف
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .width(42.dp)
                            .height(5.dp)
                            .clip(CircleShape)
                            .background(SoftGray.copy(alpha = 0.8f))
                    )

                    // زر الإغلاق على اليمين (RTL Start)
                    Surface(
                        modifier = Modifier
                            .size(34.dp)
                            .align(Alignment.CenterEnd),
                        shape = CircleShape,
                        color = SoftBeige.copy(alpha = 0.6f),
                        border = BorderStroke(0.8.dp, SoftGray.copy(alpha = 0.6f))
                    ) {
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.size(34.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "إغلاق",
                                tint = DeepNavy,
                                modifier = Modifier.size(17.dp)
                            )
                        }
                    }

                    // شارة قائد الفريق إذا كان الطالب قائداً
                    if (student.isLeader) {
                        Surface(
                            modifier = Modifier.align(Alignment.CenterStart),
                            shape = RoundedCornerShape(12.dp),
                            color = Peach.copy(alpha = 0.6f),
                            border = BorderStroke(1.dp, Peach)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = DeepNavy,
                                    modifier = Modifier.size(13.dp)
                                )
                                Text(
                                    text = "قائد الفريق 👑",
                                    fontFamily = CairoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepNavy,
                                    fontSize = 11.5.sp,
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }
                        }
                    }
                }

                // =========================================================================
                // 1. قسم الصورة الشخصية والاسم والتخصص
                // =========================================================================
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // صورة الـ Avatar مع إطار ناعم
                    Surface(
                        modifier = Modifier.size(84.dp),
                        shape = CircleShape,
                        color = WarmCream,
                        border = BorderStroke(2.5.dp, Turquoise),
                        shadowElevation = 2.dp
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = student.avatarEmoji,
                                fontSize = 40.sp
                            )
                        }
                    }

                    // اسم الطالب
                    Text(
                        text = student.name,
                        fontFamily = CairoFontFamily,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 19.sp,
                        color = DeepNavy,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        softWrap = false
                    )

                    // شارة التخصص التقني
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = LightAqua.copy(alpha = 0.4f),
                        border = BorderStroke(1.dp, Turquoise.copy(alpha = 0.6f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Code,
                                contentDescription = null,
                                tint = DeepTeal,
                                modifier = Modifier.size(15.dp)
                            )
                            Text(
                                text = student.roleTag,
                                fontFamily = CairoFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = DeepTeal,
                                fontSize = 12.5.sp,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // =========================================================================
                    // 2. كروت المعلومات الأكاديمية (الرقم الجامعي + التخصص)
                    // =========================================================================
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // كارت الرقم الجامعي
                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(14.dp),
                            color = WarmCream.copy(alpha = 0.6f),
                            border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.5f))
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Badge,
                                    contentDescription = null,
                                    tint = DeepTeal,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = "الرقم الجامعي",
                                    fontFamily = CairoFontFamily,
                                    fontWeight = FontWeight.Medium,
                                    color = DeepNavy.copy(alpha = 0.65f),
                                    fontSize = 11.sp
                                )
                                Text(
                                    text = student.studentId,
                                    fontFamily = CairoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepNavy,
                                    fontSize = 13.5.sp,
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }
                        }

                        // كارت التخصص الدراسي
                        Surface(
                            modifier = Modifier.weight(1.3f),
                            shape = RoundedCornerShape(14.dp),
                            color = WarmCream.copy(alpha = 0.6f),
                            border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.5f))
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.School,
                                    contentDescription = null,
                                    tint = DeepTeal,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = "التخصص الدراسي",
                                    fontFamily = CairoFontFamily,
                                    fontWeight = FontWeight.Medium,
                                    color = DeepNavy.copy(alpha = 0.65f),
                                    fontSize = 11.sp
                                )
                                Text(
                                    text = student.major,
                                    fontFamily = CairoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepNavy,
                                    fontSize = 12.5.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }

                    // =========================================================================
                    // 3. كارت حالة الانضمام للفريق
                    // =========================================================================
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = if (isTeamMember) LightAqua.copy(alpha = 0.35f) else SoftBeige.copy(alpha = 0.45f),
                        border = BorderStroke(
                            1.dp,
                            if (isTeamMember) Turquoise.copy(alpha = 0.7f) else Peach.copy(alpha = 0.8f)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Surface(
                                modifier = Modifier.size(38.dp),
                                shape = CircleShape,
                                color = if (isTeamMember) DeepTeal else Peach
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = if (isTeamMember) Icons.Outlined.VerifiedUser else Icons.Default.HourglassTop,
                                        contentDescription = null,
                                        tint = if (isTeamMember) Color.White else DeepNavy,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }

                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(
                                    text = if (isTeamMember) "عضو منضم رسمياً ✓" else "متاح ومتقدم للحجز ⏳",
                                    fontFamily = CairoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepNavy,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = if (isTeamMember) {
                                        "تم اعتماد المقعد في تيم (${student.currentTeamId})"
                                    } else {
                                        "يبحث عن مقعد متاح في مشاريع التخرج"
                                    },
                                    fontFamily = CairoFontFamily,
                                    fontWeight = FontWeight.Normal,
                                    color = DeepNavy.copy(alpha = 0.75f),
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }

                    // =========================================================================
                    // 4. معرض الأعمال والتواصل (GitHub & Email)
                    // =========================================================================
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = WarmCream.copy(alpha = 0.35f)),
                        border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.5f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Link,
                                    contentDescription = null,
                                    tint = DeepTeal,
                                    modifier = Modifier.size(17.dp)
                                )
                                Text(
                                    text = "روابط التواصل ومعرض الأعمال",
                                    fontFamily = CairoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepNavy,
                                    fontSize = 13.5.sp
                                )
                            }

                            // البريد الإلكتروني الجامعي
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                color = Color.White,
                                border = BorderStroke(0.8.dp, SoftGray.copy(alpha = 0.5f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Email,
                                        contentDescription = null,
                                        tint = DeepTeal,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = student.email,
                                        fontFamily = CairoFontFamily,
                                        fontWeight = FontWeight.Medium,
                                        color = DeepNavy,
                                        fontSize = 12.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }

                            // رابط GitHub التفاعلي
                            if (student.githubUrl.isNotEmpty()) {
                                OutlinedButton(
                                    onClick = { onOpenGithubClick(student.githubUrl) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(44.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = Color.White,
                                        contentColor = DeepNavy
                                    ),
                                    border = BorderStroke(1.dp, DeepTeal.copy(alpha = 0.6f))
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
                                                imageVector = Icons.Default.Code,
                                                contentDescription = null,
                                                tint = DeepTeal,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Text(
                                                text = "حساب GitHub / Portfolio",
                                                fontFamily = CairoFontFamily,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.5.sp,
                                                color = DeepNavy
                                            )
                                        }

                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.Launch,
                                            contentDescription = null,
                                            tint = DeepTeal,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // =========================================================================
                    // 5. زر الإغلاق النهائي (Dismiss Action)
                    // =========================================================================
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DeepNavy)
                    ) {
                        Text(
                            text = "إغلاق المعاينة",
                            fontFamily = CairoFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StudentPublicProfileSheetEnhancedPreview() {
    TeamSeatTheme {
        StudentPublicProfileSheet(
            student = MockDataProvider.saraStudent
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StudentPublicProfileSheetLeaderEnhancedPreview() {
    TeamSeatTheme {
        StudentPublicProfileSheet(
            student = MockDataProvider.ahmedLeader
        )
    }
}
