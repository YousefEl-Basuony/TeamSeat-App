package com.stackmates.teamseat.Screens.admin_youssef

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stackmates.teamseat.core.data.MockDataProvider
import com.stackmates.teamseat.core.model.Seat
import com.stackmates.teamseat.core.model.SeatStatus
import com.stackmates.teamseat.core.theme.DeepNavy
import com.stackmates.teamseat.core.theme.DeepTeal
import com.stackmates.teamseat.core.theme.LightAqua
import com.stackmates.teamseat.core.theme.Peach
import com.stackmates.teamseat.core.theme.SoftBeige
import com.stackmates.teamseat.core.theme.SoftGray
import com.stackmates.teamseat.core.theme.TeamSeatTheme
import com.stackmates.teamseat.core.theme.Turquoise
import com.stackmates.teamseat.core.theme.WarmCream

/**
 * 💻 المسؤول عن الشاشة: يوسف (Youssef - Team Lead & Backend)
 * شاشة تعديل المقاعد الشاغرة وحذفها أو تغيير الأدوار قبل قفل التيم (Edit Team Seats Screen)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTeamSeatsScreen(
    seats: List<Seat> = MockDataProvider.sampleSeats,
    onSaveSeatsClick: (List<Seat>) -> Unit = {},
    onDeleteSeatClick: (seatId: String) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val editableSeats = remember { mutableStateListOf(*seats.toTypedArray()) }
    var showSavedToast by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = WarmCream,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "إدارة وتعديل مقاعد التيم",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = DeepNavy
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "الرجوع",
                            tint = DeepNavy
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = WarmCream)
            )
        },
        bottomBar = {
            Surface(
                color = SoftBeige,
                shadowElevation = 8.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Button(
                        onClick = {
                            onSaveSeatsClick(editableSeats)
                            showSavedToast = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = null,
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (showSavedToast) "تم حفظ التعديلات بنجاح! ✓" else "حفظ تعديلات المقاعد 💾",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {

            // كارت إحصائيات المقاعد
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                colors = CardDefaults.cardColors(containerColor = SoftBeige),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${editableSeats.size}",
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold, color = DeepNavy)
                        )
                        Text(
                            text = "إجمالي المقاعد",
                            style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(30.dp)
                            .background(SoftGray)
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${editableSeats.count { it.status == SeatStatus.BOOKED }}",
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold, color = DeepTeal)
                        )
                        Text(
                            text = "محجوزة",
                            style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(30.dp)
                            .background(SoftGray)
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${editableSeats.count { it.status == SeatStatus.AVAILABLE }}",
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold, color = Turquoise)
                        )
                        Text(
                            text = "شاغرة (متاحة)",
                            style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                        )
                    }
                }
            }

            Text(
                text = "قائمة مقاعد المشروع (يمكنك تعديل المسمى أو حذف المقعد الشاغر)",
                style = MaterialTheme.typography.labelMedium.copy(color = Color.Gray),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // قائمة المقاعد
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(editableSeats, key = { it.id }) { seat ->
                    SeatItemRow(
                        seat = seat,
                        onRoleChange = { newRole ->
                            val index = editableSeats.indexOfFirst { it.id == seat.id }
                            if (index != -1) {
                                editableSeats[index] = seat.copy(roleTag = newRole)
                            }
                        },
                        onDeleteClick = {
                            editableSeats.removeIf { it.id == seat.id }
                            onDeleteSeatClick(seat.id)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun SeatItemRow(
    seat: Seat,
    onRoleChange: (String) -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // شارة المقعد
            Surface(
                color = if (seat.status == SeatStatus.BOOKED) LightAqua else WarmCream,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.size(46.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = seat.seatLabel,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = DeepNavy
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // معلومات الدور والاسم
            Column(modifier = Modifier.weight(1f)) {
                if (seat.status == SeatStatus.BOOKED) {
                    Text(
                        text = seat.assignedStudentName ?: "محجوز",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = DeepNavy)
                    )
                    Text(
                        text = "التخصص: ${seat.roleTag}",
                        style = MaterialTheme.typography.bodySmall.copy(color = DeepTeal)
                    )
                } else {
                    OutlinedTextField(
                        value = seat.roleTag,
                        onValueChange = onRoleChange,
                        label = { Text("التخصص المطلوب", fontSize = 11.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = WarmCream,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = DeepTeal,
                            unfocusedBorderColor = SoftGray
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // زر الحذف للمقاعد الشاغرة فقط
            if (seat.status == SeatStatus.AVAILABLE && !seat.isLeader) {
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "حذف المقعد",
                        tint = Color.Red.copy(alpha = 0.7f)
                    )
                }
            } else if (seat.isLeader) {
                Surface(
                    color = Peach.copy(alpha = 0.4f),
                    shape = CircleShape,
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = "👑", fontSize = 14.sp)
                    }
                }
            } else {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "محجوز ومقفل",
                    tint = SoftGray
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EditTeamSeatsScreenPreview() {
    TeamSeatTheme {
        EditTeamSeatsScreen()
    }
}
