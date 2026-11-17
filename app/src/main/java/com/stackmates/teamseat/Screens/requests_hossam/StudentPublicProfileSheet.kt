package com.stackmates.teamseat.Screens.requests_hossam

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.stackmates.teamseat.core.data.MockDataProvider
import com.stackmates.teamseat.core.model.User
import com.stackmates.teamseat.core.theme.TeamSeatTheme

/**
 * ⚡ المسؤول عن المكون: حسام (Hossam) — [HOSSAM-2]
 * بطاقة معاينة بروفايل أي طالب آخر عند الضغط على اسمه (Student Public Profile BottomSheet)
 */
@Composable
fun StudentPublicProfileSheet(
    student: User = MockDataProvider.saraStudent,
    onOpenGithubClick: (url: String) -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    // TODO (حسام): صمم بطاقة منبثقة أنيقة لعرض بيانات ومهارات وروابط أعمال الطالب المتقدم
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "بروفايل الطالب: ${student.name}")
            Text(text = "التخصص: ${student.roleTag} | الكود: ${student.studentId}")
            Text(text = "TODO (حسام): صمم شكل الكارت وأزرار فتح GitHub و LinkedIn هنا")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StudentPublicProfileSheetPreview() {
    TeamSeatTheme {
        StudentPublicProfileSheet()
    }
}
