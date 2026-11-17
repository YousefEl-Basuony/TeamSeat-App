package com.stackmates.teamseat.Screens.workspace_alaa

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.stackmates.teamseat.core.theme.TeamSeatTheme

/**
 * 📑 المسؤول عن المكون: آلاء (Alaa) — [ALAA-1]
 * نافذة تأكيد إغلاق التيم نهائياً قبل تسليم الكشف (Lock Team Dialog)
 */
@Composable
fun LockTeamDialog(
    onConfirmLock: () -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    // TODO (آلاء): حسّني تصميم نافذة التأكيد بأيقونة القفل وألوان التحذير المناسبة
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("اعتماد وإغلاق التيم نهائياً 🔒") },
        text = {
            Text("هل أنت متأكد من إغلاق التيم؟ بعد الاعتماد لن يتمكن أي عضو من الانسحاب أو تعديل المقاعد استعداداً لتسليم الكشف.")
        },
        confirmButton = {
            Button(onClick = onConfirmLock) {
                Text("نعم، إغلاق التيم")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("إلغاء")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun LockTeamDialogPreview() {
    TeamSeatTheme {
        LockTeamDialog()
    }
}
