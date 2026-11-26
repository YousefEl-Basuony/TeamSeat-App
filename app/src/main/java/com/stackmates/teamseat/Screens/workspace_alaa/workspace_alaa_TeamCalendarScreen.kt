package com.stackmates.teamseat.Screens.workspace_alaa

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class TeamMilestone(
    val id: String,
    val title: String,
    val supervisor: String,
    val description: String,
    val dateLabel: String,
    val timeLabel: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamCalendarScreen(
    onBackClick: () -> Unit = {}
) {
    var showAddDialog by remember { mutableStateOf(false) }
    val milestones = remember {
        mutableStateListOf(
            TeamMilestone(
                id = "1",
                title = "Visual Review & Architecture",
                supervisor = "د. محمد كاظم",
                description = "مراجعة فكرة المشروع وتقسيم الشاشات بنظام Agile.",
                dateLabel = "Sun, 29 Nov",
                timeLabel = "10:00 AM"
            ),
            TeamMilestone(
                id = "2",
                title = "Interactive Map & UI Check",
                supervisor = "م. شهد أحمد",
                description = "تسليم واجهات التطبيق الـ 24 والتأكد من عمل الـ Previews.",
                dateLabel = "Tue, 01 Dec",
                timeLabel = "12:30 PM"
            ),
            TeamMilestone(
                id = "3",
                title = "Final TA Sheet & Demo",
                supervisor = "د. محمد كاظم & م. شهد",
                description = "المناقشة النهائية وتصدير كشف الأسماء الرسمي.",
                dateLabel = "Thu, 10 Dec",
                timeLabel = "11:00 AM"
            )
        )
    }

    val days = listOf("24" to "Tue", "25" to "Wed", "26" to "Thu", "27" to "Fri", "28" to "Sat", "29" to "Sun")

    Scaffold(
        containerColor = WarmCream,
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = DeepNavy)
                    }
                },
                title = {
                    Text("Team Calendar", color = DeepNavy, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = WarmCream)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = DeepTeal,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Milestone")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            // شريط الأيام العلوي
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                items(days) { (num, dayName) ->
                    val isSelected = num == "26"
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (isSelected) DeepNavy else SoftBeige)
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Text(dayName, fontSize = 12.sp, color = if (isSelected) LightAqua else DeepTeal)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            num,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else DeepNavy
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Upcoming Deadlines & Meetings",
                fontWeight = FontWeight.Bold,
                color = DeepNavy,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(milestones) { item ->
                    MilestoneCard(milestone = item)
                }
            }
        }

        if (showAddDialog) {
            AddMilestoneDialog(
                onDismiss = { showAddDialog = false },
                onConfirm = { title, supervisor, desc, date, time ->
                    milestones.add(
                        TeamMilestone(
                            id = (milestones.size + 1).toString(),
                            title = title,
                            supervisor = supervisor,
                            description = desc,
                            dateLabel = date,
                            timeLabel = time
                        )
                    )
                    showAddDialog = false
                }
            )
        }
    }
}

@Composable
private fun MilestoneCard(milestone: TeamMilestone) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SoftBeige),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = milestone.title,
                    fontWeight = FontWeight.Bold,
                    color = DeepNavy,
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f)
                )
                Surface(color = Peach, shape = RoundedCornerShape(8.dp)) {
                    Text(
                        text = milestone.supervisor,
                        color = DeepNavy,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = milestone.description,
                color = DeepNavy.copy(alpha = 0.8f),
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Event, contentDescription = null, tint = DeepTeal, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(milestone.dateLabel, color = DeepTeal, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.width(16.dp))
                Icon(Icons.Default.AccessTime, contentDescription = null, tint = DeepTeal, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(milestone.timeLabel, color = DeepTeal, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun AddMilestoneDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var supervisor by remember { mutableStateOf("م. شهد أحمد") }
    var desc by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("Wed, 02 Dec") }
    var time by remember { mutableStateOf("01:00 PM") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = WarmCream,
        title = { Text("إضافة موعد تسليم جديد", color = DeepNavy, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("عنوان التسليم") })
                OutlinedTextField(value = supervisor, onValueChange = { supervisor = it }, label = { Text("الدكتور / المعيدة") })
                OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("التفاصيل") })
                OutlinedTextField(value = date, onValueChange = { date = it }, label = { Text("التاريخ") })
            }
        },
        confirmButton = {
            Button(
                onClick = { if (title.isNotBlank()) onConfirm(title, supervisor, desc, date, time) },
                colors = ButtonDefaults.buttonColors(containerColor = DeepTeal)
            ) {
                Text("حفظ", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("إلغاء", color = DeepNavy)
            }
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TeamCalendarScreenPreview() {
    TeamCalendarScreen()
}
