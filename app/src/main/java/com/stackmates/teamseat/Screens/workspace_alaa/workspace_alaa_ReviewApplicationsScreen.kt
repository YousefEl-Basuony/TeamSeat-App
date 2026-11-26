package com.stackmates.teamseat.Screens.workspace_alaa

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
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

enum class ApplicationDecision { PENDING, ACCEPTED, REJECTED }

data class SeatApplicant(
    val id: String,
    val studentName: String,
    val universityId: String,
    val role: String,
    val seatCode: String,
    val note: String,
    val status: ApplicationDecision = ApplicationDecision.PENDING
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewApplicationsScreen(
    onBackClick: () -> Unit = {}
) {
    val applicants = remember {
        mutableStateListOf(
            SeatApplicant(
                id = "1",
                studentName = "Sarah Jenkins",
                universityId = "202310412",
                role = "Frontend Developer",
                seatCode = "Seat B1",
                note = "لدي خبرة في Jetpack Compose وتصميم الواجهات المتجاوبة وأرغب بالانضمام."
            ),
            SeatApplicant(
                id = "2",
                studentName = "James Lee",
                universityId = "202310899",
                role = "Backend Developer",
                seatCode = "Seat B2",
                note = "اشتغلت على Firebase Auth و Firestore في مشروعين سابقين."
            ),
            SeatApplicant(
                id = "3",
                studentName = "Mariam Tarek",
                universityId = "202311205",
                role = "UI/UX Designer",
                seatCode = "Seat B3",
                note = "جاهزة لتسليم ملفات Figma والـ Design System بالكامل."
            )
        )
    }

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
                    Text("Review Applications", color = DeepNavy, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = WarmCream)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            items(applicants, key = { it.id }) { applicant ->
                ApplicantReviewCard(
                    applicant = applicant,
                    onAccept = {
                        val idx = applicants.indexOfFirst { it.id == applicant.id }
                        if (idx != -1) applicants[idx] = applicant.copy(status = ApplicationDecision.ACCEPTED)
                    },
                    onReject = {
                        val idx = applicants.indexOfFirst { it.id == applicant.id }
                        if (idx != -1) applicants[idx] = applicant.copy(status = ApplicationDecision.REJECTED)
                    }
                )
            }
        }
    }
}

@Composable
private fun ApplicantReviewCard(
    applicant: SeatApplicant,
    onAccept: () -> Unit,
    onReject: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SoftBeige),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(DeepNavy),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = applicant.studentName.take(1),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(applicant.studentName, fontWeight = FontWeight.Bold, color = DeepNavy, fontSize = 16.sp)
                    Text("${applicant.role} • ${applicant.seatCode}", color = DeepTeal, fontSize = 13.sp)
                }
                Surface(color = LightAqua, shape = RoundedCornerShape(8.dp)) {
                    Text(
                        text = applicant.universityId,
                        color = DeepNavy,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // رسالة المتقدم
            Surface(
                color = WarmCream,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = ""${applicant.note}"",
                    color = DeepNavy.copy(alpha = 0.85f),
                    fontSize = 13.sp,
                    modifier = Modifier.padding(10.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // أزرار القبول والرفض أو عرض النتيجة
            when (applicant.status) {
                ApplicationDecision.PENDING -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = onAccept,
                            colors = ButtonDefaults.buttonColors(containerColor = AcceptGreen),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("قبول وتثبيت", fontWeight = FontWeight.Bold)
                        }
                        Button(
                            onClick = onReject,
                            colors = ButtonDefaults.buttonColors(containerColor = RejectRed),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("رفض", fontWeight = FontWeight.Bold)
                        }
                    }
                }
                ApplicationDecision.ACCEPTED -> {
                    Surface(
                        color = AcceptGreen.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "✓ تم القبول وتثبيت المقعد",
                            color = AcceptGreen,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
                ApplicationDecision.REJECTED -> {
                    Surface(
                        color = RejectRed.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "✕ تم رفض الطلب",
                            color = RejectRed,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ReviewApplicationsScreenPreview() {
    ReviewApplicationsScreen()
}
