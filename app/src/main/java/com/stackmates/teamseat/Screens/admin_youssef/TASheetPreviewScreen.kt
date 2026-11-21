package com.stackmates.teamseat.Screens.admin_youssef

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stackmates.teamseat.core.data.MockDataProvider
import com.stackmates.teamseat.core.model.Seat
import com.stackmates.teamseat.core.model.SeatStatus
import com.stackmates.teamseat.core.model.Team
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
 * شاشة كشف التسليم الرسمي للمعيدة م. شهد أحمد والدكتور د. محمد كاظم (A4 Sheet Layout)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TASheetPreviewScreen(
    team: Team = MockDataProvider.sampleTeam,
    seats: List<Seat> = MockDataProvider.sampleSeats,
    onLockTeamClick: () -> Unit = {},
    onExportPdfClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    Scaffold(
        containerColor = WarmCream,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "كشف اعتماد التيم الرسمي (TA Sheet)",
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
                tonalElevation = 8.dp,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // زر تصدير PDF
                    Button(
                        onClick = onExportPdfClick,
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PictureAsPdf,
                            contentDescription = null,
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "تصدير PDF 📄",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                    }

                    // زر قفل وتأكيد التيم
                    OutlinedButton(
                        onClick = onLockTeamClick,
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = DeepNavy,
                            containerColor = if (team.isLocked) SoftGray.copy(alpha = 0.3f) else Peach.copy(alpha = 0.3f)
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, if (team.isLocked) SoftGray else DeepNavy),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = DeepNavy
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (team.isLocked) "التيم مقفل 🔒" else "قفل التيم 🔒",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = DeepNavy
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ورقة الـ A4 الأنيقة
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(elevation = 6.dp, shape = RoundedCornerShape(16.dp))
                    .border(1.dp, SoftGray, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    // الهيدر الأكاديمي
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "جامعة حلوان - كلية الحاسبات",
                                style = MaterialTheme.typography.labelMedium.copy(color = Turquoise, fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "مشروع التخرج / المقرر الأكاديمي",
                                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                            )
                        }

                        // شارة القفل
                        Surface(
                            color = if (team.isLocked) Peach else LightAqua,
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (team.isLocked) Icons.Default.Lock else Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = DeepNavy,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (team.isLocked) "معتمد ومقفل" else "جاهز للاعتماد",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = DeepNavy
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = SoftBeige, thickness = 2.dp)
                    Spacer(modifier = Modifier.height(16.dp))

                    // تفاصيل المشروع
                    Text(
                        text = team.projectName,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = DeepNavy
                        )
                    )
                    Text(
                        text = team.description,
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.DarkGray),
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // معلومات الدكتور والمعيدة
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(WarmCream)
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "المشرف الأكاديمي:", style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray))
                            Text(text = team.supervisorDoctor, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = DeepNavy))
                        }
                        Column {
                            Text(text = "المعيدة المتابعة:", style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray))
                            Text(text = team.teachingAssistant, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = DeepTeal))
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // عنوان جدول المقاعد
                    Text(
                        text = "جدول توزيع المقاعد والطلاب المعتمدين",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = DeepNavy
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // رؤوس الجدول
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                            .background(DeepTeal)
                            .padding(horizontal = 8.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "المقعد",
                            style = MaterialTheme.typography.labelMedium.copy(color = Color.White, fontWeight = FontWeight.Bold),
                            modifier = Modifier.width(55.dp),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "اسم الطالب",
                            style = MaterialTheme.typography.labelMedium.copy(color = Color.White, fontWeight = FontWeight.Bold),
                            modifier = Modifier.weight(1.2f)
                        )
                        Text(
                            text = "الكود",
                            style = MaterialTheme.typography.labelMedium.copy(color = Color.White, fontWeight = FontWeight.Bold),
                            modifier = Modifier.weight(0.9f)
                        )
                        Text(
                            text = "التخصص",
                            style = MaterialTheme.typography.labelMedium.copy(color = Color.White, fontWeight = FontWeight.Bold),
                            modifier = Modifier.weight(1.1f)
                        )
                    }

                    // صفوف الجدول
                    seats.forEachIndexed { index, seat ->
                        val rowBg = if (index % 2 == 0) SoftBeige.copy(alpha = 0.4f) else Color.White
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(rowBg)
                                .padding(horizontal = 8.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // رقم المقعد
                            Text(
                                text = seat.seatLabel,
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = DeepNavy),
                                modifier = Modifier.width(55.dp),
                                textAlign = TextAlign.Center
                            )

                            // اسم الطالب
                            Row(
                                modifier = Modifier.weight(1.2f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (seat.isLeader) {
                                    Text(text = "👑 ", fontSize = 12.sp)
                                }
                                Text(
                                    text = seat.assignedStudentName ?: "شاغر (متاح)",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = if (seat.assignedStudentName != null) FontWeight.Medium else FontWeight.Normal,
                                        color = if (seat.assignedStudentName != null) DeepNavy else Color.Gray
                                    )
                                )
                            }

                            // الكود الجامعي
                            Text(
                                text = seat.assignedStudentId ?: "---",
                                style = MaterialTheme.typography.bodySmall.copy(color = Color.DarkGray),
                                modifier = Modifier.weight(0.9f)
                            )

                            // التخصص
                            Surface(
                                color = if (seat.status == SeatStatus.BOOKED) LightAqua.copy(alpha = 0.5f) else SoftGray.copy(alpha = 0.3f),
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.weight(1.1f)
                            ) {
                                Text(
                                    text = seat.roleTag,
                                    style = MaterialTheme.typography.labelSmall.copy(color = DeepNavy, fontSize = 10.sp),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                        HorizontalDivider(color = SoftGray.copy(alpha = 0.5f), thickness = 0.5.dp)
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    // خانة التوقيعات الأكاديمية
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "توقيع قائد الفريق",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = DeepNavy)
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            Text(
                                text = team.leaderName,
                                style = MaterialTheme.typography.bodySmall.copy(color = DeepTeal, fontWeight = FontWeight.Medium)
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "توقيع المعيدة / الدكتور",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = DeepNavy)
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            Text(
                                text = "_________________",
                                style = MaterialTheme.typography.bodySmall.copy(color = SoftGray)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TASheetPreviewScreenPreview() {
    TeamSeatTheme {
        TASheetPreviewScreen()
    }
}
