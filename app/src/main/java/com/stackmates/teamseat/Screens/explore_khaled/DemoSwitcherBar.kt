package com.stackmates.teamseat.Screens.explore_khaled

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stackmates.teamseat.core.data.MockDataProvider
import com.stackmates.teamseat.core.model.DemoPersona
import com.stackmates.teamseat.core.model.User
import com.stackmates.teamseat.core.theme.DeepNavy
import com.stackmates.teamseat.core.theme.DeepTeal
import com.stackmates.teamseat.core.theme.LeaderCrownGold
import com.stackmates.teamseat.core.theme.LightAqua
import com.stackmates.teamseat.core.theme.Peach
import com.stackmates.teamseat.core.theme.SoftBeige
import com.stackmates.teamseat.core.theme.SoftGray
import com.stackmates.teamseat.core.theme.TeamSeatTheme
import com.stackmates.teamseat.core.theme.Turquoise
import com.stackmates.teamseat.core.theme.WarmCream

/**
 * 🔍 المسؤول عن المكون: خالد (Khaled) — [KHALED-1]
 * شريط التبديل اللحظي بين شخصية القائد والطالبة وقت المناقشة (Live Demo Switcher Bar)
 * تصميم متميز بمقاييس UI/UX احترافية وجودة عالية متوافقة مع الهوية البصرية لـ TeamSeat
 */
@Composable
fun DemoSwitcherBar(
    currentPersona: DemoPersona = DemoPersona.AHMED_LEADER,
    currentUser: User = MockDataProvider.ahmedLeader,
    onTogglePersona: () -> Unit = {}
) {
    val isLeader = currentPersona == DemoPersona.AHMED_LEADER || currentUser.isLeader

    // ألوان وسمات الشخصية الحالية
    val accentColor = if (isLeader) DeepNavy else DeepTeal
    val avatarBgColor = if (isLeader) Peach.copy(alpha = 0.4f) else LightAqua.copy(alpha = 0.5f)
    val roleBadgeColor = if (isLeader) SoftBeige else SeatBadgeAqua
    val roleTitle = if (isLeader) "قائد الفريق (Leader) 👑" else "طالبة باحثة عن تيم 🎓"
    val switchTargetName = if (isLeader) "التبديل لسارة 👩‍💻" else "التبديل لأحمد 👨‍💻"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable(role = Role.Button, onClick = onTogglePersona),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(
            1.2.dp,
            if (isLeader) Peach.copy(alpha = 0.9f) else LightAqua.copy(alpha = 0.9f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // بيانات الحساب النشط حالياً
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // أفاتار الشخصية مع شارة اللون
                Surface(
                    shape = CircleShape,
                    color = avatarBgColor,
                    border = BorderStroke(1.2.dp, if (isLeader) LeaderCrownGold else Turquoise),
                    modifier = Modifier.size(46.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = currentUser.avatarEmoji,
                            fontSize = 24.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    // وسم وضع المناقشة والعرض التجريبي
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = WarmCream,
                            border = BorderStroke(0.6.dp, SoftGray)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Sync,
                                    contentDescription = null,
                                    tint = DeepTeal,
                                    modifier = Modifier.size(10.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "LIVE DEMO",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = DeepNavy,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 9.5.sp,
                                        letterSpacing = 0.5.sp
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = roleTitle,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = accentColor,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // اسم المستخدم الحالي
                    Text(
                        text = currentUser.name.substringBefore(" ("),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = DeepNavy,
                            fontSize = 15.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // زر التبديل التفاعلي البارز
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = if (isLeader) DeepTeal else DeepNavy,
                shadowElevation = 2.dp,
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .clickable(role = Role.Button, onClick = onTogglePersona)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 11.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.SwapHoriz,
                        contentDescription = "تبديل الحساب",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = switchTargetName,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.5.sp
                        )
                    )
                }
            }
        }
    }
}

// لون شارة الطالبة المتوافق مع الهوية
private val SeatBadgeAqua = LightAqua.copy(alpha = 0.6f)

// =============================================================================
// Preview (معاينة كلا الشخصيتين معاً)
// =============================================================================
@Preview(showBackground = true)
@Composable
private fun DemoSwitcherBarPreview() {
    TeamSeatTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // شخصية 1: أحمد قائد الفريق
            DemoSwitcherBar(
                currentPersona = DemoPersona.AHMED_LEADER,
                currentUser = MockDataProvider.ahmedLeader
            )

            // شخصية 2: سارة طالبة تبحث عن تيم
            DemoSwitcherBar(
                currentPersona = DemoPersona.SARA_STUDENT,
                currentUser = MockDataProvider.saraStudent
            )
        }
    }
}
