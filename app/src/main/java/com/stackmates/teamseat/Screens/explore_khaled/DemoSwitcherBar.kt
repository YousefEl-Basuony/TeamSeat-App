package com.stackmates.teamseat.Screens.explore_khaled

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.stackmates.teamseat.core.data.MockDataProvider
import com.stackmates.teamseat.core.model.DemoPersona
import com.stackmates.teamseat.core.model.User
import com.stackmates.teamseat.core.theme.TeamSeatTheme

/**
 * 🔍 المسؤول عن المكون: خالد (Khaled) — [KHALED-1]
 * شريط التبديل اللحظي بين شخصية القائد والطالبة وقت المناقشة (Live Demo Switcher Bar)
 */
@Composable
fun DemoSwitcherBar(
    currentPersona: DemoPersona = DemoPersona.AHMED_LEADER,
    currentUser: User = MockDataProvider.ahmedLeader,
    onTogglePersona: () -> Unit = {}
) {
    // TODO (خالد): صمم شريط أنيق هنا يعرض اسم الحساب الحالي وزر للتبديل السريع بين (أحمد - ليدر) و(سارة - طالبة)
    Card(
        onClick = onTogglePersona,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = "🔄 وضع العرض (Demo): ${currentUser.name} — اضغط للتبديل (تصميم خالد)",
            modifier = Modifier.padding(12.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DemoSwitcherBarPreview() {
    TeamSeatTheme {
        DemoSwitcherBar()
    }
}
