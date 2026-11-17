package com.stackmates.teamseat.Screens.seatmap_esraa

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.stackmates.teamseat.core.data.MockDataProvider
import com.stackmates.teamseat.core.model.Seat
import com.stackmates.teamseat.core.theme.TeamSeatTheme

/**
 * 💺 المسؤول عن المكون: إسراء (Esraa) — [ESRAA-1]
 * النافذة السفلية لحجز المقعد المتاح (Seat Booking Bottom Sheet)
 */
@Composable
fun SeatBookingBottomSheetContent(
    selectedSeat: Seat = MockDataProvider.sampleSeats[3],
    onConfirmBookingClick: (note: String) -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    // TODO (إسراء): صممي محتوى الـ Bottom Sheet هنا (رقم المقعد + التخصص + TextField لرسالة التقديم + زر احجز المقعد)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "حجز مقعد رقم ${selectedSeat.seatNumber}: ${selectedSeat.roleRequired}")
            Text(text = "TODO (إسراء): أضيفي حقل رسالة التقديم وزر تأكيد الحجز هنا")
            Button(
                onClick = { onConfirmBookingClick("مهتم بالانضمام للتيم!") },
                modifier = Modifier.padding(top = 12.dp)
            ) {
                Text("احجز المقعد 💺")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SeatBookingBottomSheetPreview() {
    TeamSeatTheme {
        SeatBookingBottomSheetContent()
    }
}
