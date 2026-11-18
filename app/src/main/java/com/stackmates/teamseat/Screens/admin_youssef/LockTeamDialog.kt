package com.stackmates.teamseat.Screens.admin_youssef

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.stackmates.teamseat.core.theme.TeamSeatTheme

/**
 * 💻 المسؤول عن المكون: يوسف (Youssef - Team Lead & Backend)
 * نافذة تأكيد إغلاق واعتماد التيم نهائياً قبل تسليم الكشف (Lock Team Dialog)
 */
@Composable
fun LockTeamDialog(
    onConfirmLock: () -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    // TODO (يوسف): حسّن تصميم نافذة التأكيد بأيقونة القفل والتنبيه
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("اعتماد وإغلاق التيم نهائياً 🔒") },
        text = {
            Text("هل أنت متأكد من إغلاق التيم؟ بعد الاعتماد لن يتمكن أي عضو من الانسحاب أو تعديل المقاعد استعداداً لتسليم الكشف للمعيدة.")
        },
        confirmButton = {
            Button(
                onClick = onConfirmLock,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("نعم، إغلاق التيم واعتمد")
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
