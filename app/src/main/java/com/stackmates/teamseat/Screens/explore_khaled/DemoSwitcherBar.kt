package com.stackmates.teamseat.Screens.explore_khaled

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SwapHoriz
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stackmates.teamseat.core.data.MockDataProvider
import com.stackmates.teamseat.core.model.DemoPersona
import com.stackmates.teamseat.core.model.User
import com.stackmates.teamseat.core.theme.TeamSeatTheme

private val SwitcherAquaSurface = Color(0xFFEDF7F7)
private val SwitcherAquaBorder = Color(0xFFB8E0E1)
private val SwitcherPureWhite = Color(0xFFFFFFFF)

/**
 * 🔍 المسؤول عن المكون: خالد (Khaled) — [KHALED-1]
 * شريط التبديل اللحظي المصغر بين شخصية القائد والطالبة وقت المناقشة (Live Demo Switcher Chip)
 */
@Composable
fun DemoSwitcherBar(
    currentPersona: DemoPersona = DemoPersona.AHMED_LEADER,
    currentUser: User = MockDataProvider.ahmedLeader,
    onTogglePersona: () -> Unit = {}
) {
    val isLeader = currentPersona == DemoPersona.AHMED_LEADER || currentUser.isLeader
    val activeRoleLabel = if (isLeader) "وضع العرض: قائد الفريق" else "وضع العرض: طالبة"
    val switchLabel = if (isLeader) "تبديل لطالبة" else "تبديل لقائد"

    Surface(
        modifier = Modifier
            .heightIn(min = 36.dp)
            .clip(RoundedCornerShape(50))
            .clickable(role = Role.Button, onClick = onTogglePersona),
        shape = RoundedCornerShape(50),
        color = SwitcherAquaSurface,
        shadowElevation = 1.dp,
        border = BorderStroke(1.dp, SwitcherAquaBorder)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = activeRoleLabel,
                style = MaterialTheme.typography.labelMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp
                ),
                maxLines = 1,
                softWrap = false
            )

            Surface(
                shape = RoundedCornerShape(50),
                color = SwitcherPureWhite,
                border = BorderStroke(1.dp, SwitcherAquaBorder)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 9.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.SwapHoriz,
                        contentDescription = "تبديل الحساب",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = switchLabel,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.5.sp
                        ),
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }
        }
    }
}

// =============================================================================
// Preview (معاينة كلا الشخصيتين معاً)
// =============================================================================
@Preview(showBackground = true)
@Composable
private fun DemoSwitcherBarPreview() {
    TeamSeatTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            DemoSwitcherBar(
                currentPersona = DemoPersona.AHMED_LEADER,
                currentUser = MockDataProvider.ahmedLeader
            )
            DemoSwitcherBar(
                currentPersona = DemoPersona.SARA_STUDENT,
                currentUser = MockDataProvider.saraStudent
            )
        }
    }
}
