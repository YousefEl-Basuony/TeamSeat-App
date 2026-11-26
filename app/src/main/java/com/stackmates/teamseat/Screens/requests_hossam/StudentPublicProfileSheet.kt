package com.stackmates.teamseat.Screens.requests_hossam

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Launch
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
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
import com.stackmates.teamseat.core.theme.TajawalFontFamily
import com.stackmates.teamseat.core.theme.TeamSeatTheme
import com.stackmates.teamseat.ui.components.*

/**
 * ⚡ المسؤول عن المكون: حسام (Hossam) — [HOSSAM-2]
 * بطاقة معاينة بروفايل أي طالب منضم أو متقدم (StudentPublicProfileSheet)
 */
@Composable
fun StudentPublicProfileSheet(
    student: User = MockDataProvider.saraStudent,
    onOpenGithubClick: (url: String) -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    val isTeamMember = !student.currentTeamId.isNullOrEmpty()

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(TeamSeatBgOffWhite)
                .border(1.5.dp, TeamSeatCardBorderTeal, RoundedCornerShape(24.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // مقبض السحب + زر الإغلاق + شارة القائد
                Box(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .width(40.dp)
                            .height(5.dp)
                            .clip(CircleShape)
                            .background(TeamSeatCardBorderTeal)
                    )

                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .align(Alignment.CenterStart)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(1.dp, TeamSeatCardBorderTeal, CircleShape)
                            .bounceClick(onClick = onDismiss),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "إغلاق",
                            tint = TeamSeatTitleDark,
                            modifier = Modifier.size(17.dp)
                        )
                    }

                    if (student.isLeader) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.White)
                                .border(1.dp, TeamSeatCardBorderWarm, RoundedCornerShape(10.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Star,
                                    contentDescription = null,
                                    tint = TeamSeatWarmOrange,
                                    modifier = Modifier.size(13.dp)
                                )
                                Text(
                                    text = "قائد الفريق",
                                    fontFamily = TajawalFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    color = TeamSeatWarmOrange,
                                    fontSize = 11.5.sp,
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }
                        }
                    }
                }

                // الصورة الشخصية والاسم والتخصص
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(2.dp, TeamSeatPrimaryTeal, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = student.avatarEmoji, fontSize = 38.sp)
                    }

                    Text(
                        text = student.name.substringBefore(" ("),
                        fontFamily = TajawalFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = TeamSeatTitleDark,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(TeamSeatSoftTealPill)
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Code,
                                contentDescription = null,
                                tint = TeamSeatPrimaryTeal,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = student.roleTag,
                                style = TextStyle(
                                    fontFamily = TajawalFontFamily,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TeamSeatPrimaryTeal,
                                    fontSize = 12.sp,
                                    textDirection = TextDirection.Ltr
                                ),
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }
                }

                // الرقم الجامعي + التخصص
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    TeamSeatCard(
                        modifier = Modifier.weight(1f),
                        contentPadding = 12.dp
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Badge,
                                contentDescription = null,
                                tint = TeamSeatPrimaryTeal,
                                modifier = Modifier.size(19.dp)
                            )
                            Text(
                                text = "الرقم الجامعي",
                                fontFamily = TajawalFontFamily,
                                color = TeamSeatBodyMuted,
                                fontSize = 11.sp
                            )
                            Text(
                                text = student.studentId,
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = TeamSeatTitleDark,
                                fontSize = 13.sp,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }

                    TeamSeatCard(
                        modifier = Modifier.weight(1.3f),
                        contentPadding = 12.dp
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.School,
                                contentDescription = null,
                                tint = TeamSeatPrimaryTeal,
                                modifier = Modifier.size(19.dp)
                            )
                            Text(
                                text = "التخصص",
                                fontFamily = TajawalFontFamily,
                                color = TeamSeatBodyMuted,
                                fontSize = 11.sp
                            )
                            Text(
                                text = student.major.substringBefore(" ("),
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = TeamSeatTitleDark,
                                fontSize = 13.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                // حالة الانضمام
                TeamSeatCard(isWarmHighlight = !isTeamMember, contentPadding = 12.dp) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(if (isTeamMember) TeamSeatPrimaryTeal else TeamSeatWarmOrange),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isTeamMember) Icons.Rounded.VerifiedUser else Icons.Rounded.HourglassTop,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = if (isTeamMember) "عضو منضم رسمياً ✓" else "متاح وبيدوّر على مقعد",
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = TeamSeatTitleDark,
                                fontSize = 14.sp
                            )
                            Text(
                                text = if (isTeamMember) {
                                    "مقعده معتمد في تيم (${student.currentTeamId})"
                                } else {
                                    "جاهز ينضم لمشاريع التخرج دلوقتي"
                                },
                                fontFamily = TajawalFontFamily,
                                color = TeamSeatBodyMuted,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                // الإيميل ورابط GitHub
                TeamSeatCard(contentPadding = 14.dp) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Email,
                                contentDescription = null,
                                tint = TeamSeatPrimaryTeal,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = student.email,
                                style = TextStyle(
                                    fontFamily = TajawalFontFamily,
                                    fontWeight = FontWeight.Medium,
                                    color = TeamSeatTitleDark,
                                    fontSize = 12.5.sp,
                                    textDirection = TextDirection.Ltr
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        if (student.githubUrl.isNotEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(42.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color.White)
                                    .border(1.dp, TeamSeatCardBorderTeal, RoundedCornerShape(10.dp))
                                    .bounceClick { onOpenGithubClick(student.githubUrl) }
                                    .padding(horizontal = 12.dp),
                                contentAlignment = Alignment.Center
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
                                            imageVector = Icons.Rounded.Code,
                                            contentDescription = null,
                                            tint = TeamSeatPrimaryTeal,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text(
                                            text = "شوف حساب GitHub",
                                            fontFamily = TajawalFontFamily,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 12.5.sp,
                                            color = TeamSeatTitleDark
                                        )
                                    }
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Rounded.Launch,
                                        contentDescription = null,
                                        tint = TeamSeatPrimaryTeal,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // زر الإغلاق
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(TeamSeatPrimaryTeal)
                        .bounceClick(onClick = onDismiss),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "اقفل المعاينة",
                        fontFamily = TajawalFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 14.sp,
                        maxLines = 1,
                        softWrap = false
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
        StudentPublicProfileSheet(student = MockDataProvider.saraStudent)
    }
}
