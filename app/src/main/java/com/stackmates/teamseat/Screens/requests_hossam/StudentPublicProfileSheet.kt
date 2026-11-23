package com.stackmates.teamseat.Screens.requests_hossam

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stackmates.teamseat.core.data.MockDataProvider
import com.stackmates.teamseat.core.model.User
import com.stackmates.teamseat.core.theme.DeepNavy
import com.stackmates.teamseat.core.theme.DeepTeal
import com.stackmates.teamseat.core.theme.LeaderCrownGold
import com.stackmates.teamseat.core.theme.LightAqua
import com.stackmates.teamseat.core.theme.Peach
import com.stackmates.teamseat.core.theme.SoftGray
import com.stackmates.teamseat.core.theme.TeamSeatTheme
import com.stackmates.teamseat.core.theme.Turquoise
import com.stackmates.teamseat.core.theme.WarmCream

/**
 * ⚡ المسؤول عن المكون: حسام (Hossam) — [HOSSAM-2]
 * بطاقة معاينة بروفايل أي طالب آخر عند الضغط على اسمه (Student Public Profile BottomSheet)
 * تصميم منبثق مميز مليء بالأيقونات والتنسيقات المتناسقة مع هوية TeamSeat
 */
@Composable
fun StudentPublicProfileSheet(
    student: User = MockDataProvider.saraStudent,
    onOpenGithubClick: (url: String) -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    val isTeamMember = !student.currentTeamId.isNullOrEmpty()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = WarmCream),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            // =========================================================================
            // 0. مقبض السحب العلوي (Sheet Drag Indicator) + زر الإغلاق
            // =========================================================================
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(DeepTeal, Turquoise, LightAqua)
                        )
                    )
            ) {
                // مقبض السحب بالمنتصف
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .width(42.dp)
                        .height(5.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.8f))
                )

                // زر الإغلاق
                Surface(
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .size(32.dp)
                        .align(Alignment.CenterStart),
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.9f)
                ) {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "إغلاق",
                            tint = DeepNavy,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // شارة القائد إن وُجدت
                if (student.isLeader) {
                    Surface(
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .align(Alignment.CenterEnd),
                        shape = RoundedCornerShape(14.dp),
                        color = LeaderCrownGold,
                        shadowElevation = 2.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = DeepNavy,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "قائد الفريق 👑",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = DeepNavy,
                                    fontSize = 11.5.sp
                                )
                            )
                        }
                    }
                }
            }

            // =========================================================================
            // 1. قسم الصورة الشخصية والاسم (Avatar & Header Profile)
            // =========================================================================
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // صورة الـ Avatar مع إطار دائري أنيق
                Box(
                    modifier = Modifier
                        .size(88.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(
                            width = 3.dp,
                            brush = Brush.sweepGradient(listOf(Turquoise, DeepTeal, LightAqua, Turquoise)),
                            shape = CircleShape
                        )
                        .padding(4.dp)
                        .clip(CircleShape)
                        .background(LightAqua.copy(alpha = 0.6f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = student.avatarEmoji,
                        fontSize = 42.sp
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = student.name,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        color = DeepNavy
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))

                // شارة التخصص المسمى بها
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = LightAqua.copy(alpha = 0.45f),
                    border = BorderStroke(1.dp, DeepTeal.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Code,
                            contentDescription = null,
                            tint = DeepTeal,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = student.roleTag,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = DeepTeal,
                                fontSize = 13.sp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // =========================================================================
                // 2. كروت المعلومات الأكاديمية (Academic Details Chips)
                // =========================================================================
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // الرقم الجامعي
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        color = Color.White,
                        border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.4f))
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Badge,
                                contentDescription = null,
                                tint = DeepTeal,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "الرقم الجامعي",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = DeepNavy.copy(alpha = 0.6f),
                                    fontSize = 11.5.sp
                                )
                            )
                            Text(
                                text = student.studentId,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = DeepNavy,
                                    fontSize = 13.5.sp
                                )
                            )
                        }
                    }

                    // الكلية / التخصص
                    Surface(
                        modifier = Modifier.weight(1.4f),
                        shape = RoundedCornerShape(14.dp),
                        color = Color.White,
                        border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.4f))
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.School,
                                contentDescription = null,
                                tint = DeepTeal,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "التخصص الدراسي",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = DeepNavy.copy(alpha = 0.6f),
                                    fontSize = 11.5.sp
                                )
                            )
                            Text(
                                text = student.major,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = DeepNavy,
                                    fontSize = 13.sp
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // =========================================================================
                // 3. كارت حالة الانضمام (Team Status)
                // =========================================================================
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = if (isTeamMember) LightAqua.copy(alpha = 0.35f) else Peach.copy(alpha = 0.3f),
                    border = BorderStroke(
                        1.dp,
                        if (isTeamMember) DeepTeal.copy(alpha = 0.4f) else Peach
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(if (isTeamMember) DeepTeal else Peach),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isTeamMember) Icons.Outlined.VerifiedUser else Icons.Default.HourglassTop,
                                contentDescription = null,
                                tint = if (isTeamMember) Color.White else DeepNavy,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Text(
                                text = if (isTeamMember) "عضو منضم رسمياً" else "متاح ومتقدم للحجز",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = DeepNavy,
                                    fontSize = 14.5.sp
                                )
                            )
                            Text(
                                text = if (isTeamMember) {
                                    "تم اعتماد المقعد في تيم (#${student.currentTeamId})"
                                } else {
                                    "يبحث عن مقعد متاح في مشاريع التخرج"
                                },
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = DeepNavy.copy(alpha = 0.75f),
                                    fontSize = 12.sp
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // =========================================================================
                // 4. معرض الأعمال والتواصل (GitHub & Social Links)
                // =========================================================================
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.4f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Link,
                                contentDescription = null,
                                tint = DeepTeal,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "روابط معرض الأعمال والتواصل",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = DeepNavy,
                                    fontSize = 15.sp
                                )
                            )
                        }

                        // البريد الإلكتروني
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            color = WarmCream.copy(alpha = 0.5f)
                        ) {
                            Row(
                                modifier = Modifier.padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Email,
                                    contentDescription = null,
                                    tint = DeepTeal,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = student.email,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = DeepNavy,
                                        fontSize = 12.5.sp
                                    )
                                )
                            }
                        }

                        // رابط GitHub (زر تفاعلي)
                        if (student.githubUrl.isNotEmpty()) {
                            OutlinedButton(
                                onClick = { onOpenGithubClick(student.githubUrl) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = DeepTeal),
                                border = BorderStroke(1.dp, DeepTeal.copy(alpha = 0.5f))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Code,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "تصفح حساب GitHub / Portfolio",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Launch,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }

                        // رابط LinkedIn (إن وُجد)
                        if (student.linkedinUrl.isNotEmpty()) {
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                color = WarmCream.copy(alpha = 0.5f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null,
                                        tint = DeepTeal,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = student.linkedinUrl,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = DeepNavy,
                                            fontSize = 12.5.sp
                                        ),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // =========================================================================
                // 5. زر الإغلاق الرئيسي (Dismiss Button)
                // =========================================================================
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DeepTeal,
                        contentColor = WarmCream
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                ) {
                    Text(
                        text = "إغلاق المعاينة",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = WarmCream,
                            fontSize = 15.sp
                        )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StudentPublicProfileSheetPreview() {
    TeamSeatTheme {
        StudentPublicProfileSheet(
            student = MockDataProvider.saraStudent
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StudentPublicProfileSheetLeaderPreview() {
    TeamSeatTheme {
        StudentPublicProfileSheet(
            student = MockDataProvider.ahmedLeader
        )
    }
}
