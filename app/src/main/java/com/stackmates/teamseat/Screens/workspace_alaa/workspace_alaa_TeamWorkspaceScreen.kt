package com.stackmates.teamseat.Screens.workspace_alaa

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// باليتة ألوان مشروع TeamSeat الرسمية
val DeepNavy = Color(0xFF0A344A)     // العناوين الرئيسية والخلفيات الداكنة
val DeepTeal = Color(0xFF1B6F7D)     // الأزرار الأساسية والـ Navbar
val Turquoise = Color(0xFF469D9F)    // الأيقونات والعناصر النشطة
val LightAqua = Color(0xFFA9D5D4)    // الخلفيات الثانوية
val WarmCream = Color(0xFFFCF2EA)    // الخلفية الرئيسية للتطبيق
val SoftBeige = Color(0xFFF4E2DD)    // الكروت والـ Input Fields
val Peach = Color(0xFFF4C195)        // لمسات جمالية وأزرار ثانوية
val SoftGray = Color(0xFFD3CFD1)     // الحدود والعناصر غير النشطة

// ألوان إضافية لحالات القبول والرفض
val AcceptGreen = Color(0xFF2E7D32)
val RejectRed = Color(0xFFC62828)

data class WorkspaceMember(
    val id: String,
    val name: String,
    val role: String,
    val seatCode: String,
    val isLeader: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamWorkspaceScreen(
    teamName: String = "Alpha Team - TeamSeat",
    completionProgress: Float = 0.70f,
    githubUrl: String = "https://github.com/stackmates/teamseat",
    driveUrl: String = "https://drive.google.com",
    members: List<WorkspaceMember> = sampleWorkspaceMembers(),
    onOpenInteractiveMap: () -> Unit = {}
) {
    val context = LocalContext.current

    Scaffold(
        containerColor = WarmCream,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Team Workspace",
                            color = DeepNavy,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Text(
                            text = teamName,
                            color = DeepTeal,
                            fontSize = 12.sp
                        )
                    }
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
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // 1. كارت نسبة اكتمال التيم
            item {
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
                                text = "Project Progress",
                                fontWeight = FontWeight.Bold,
                                color = DeepNavy,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "${(completionProgress * 100).toInt()}%",
                                fontWeight = FontWeight.ExtraBold,
                                color = DeepTeal,
                                fontSize = 16.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        LinearProgressIndicator(
                            progress = { completionProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            color = DeepTeal,
                            trackColor = LightAqua
                        )
                    }
                }
            }

            // 2. كروتي روابط GitHub و Google Drive
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ResourceLinkCard(
                        title = "GitHub Repo",
                        subtitle = "Source Code",
                        icon = Icons.Default.Code,
                        containerColor = DeepNavy,
                        contentColor = Color.White,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(githubUrl))
                            context.startActivity(intent)
                        }
                    )
                    ResourceLinkCard(
                        title = "Drive Link",
                        subtitle = "Docs & Assets",
                        icon = Icons.Default.FolderShared,
                        containerColor = DeepTeal,
                        contentColor = Color.White,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(driveUrl))
                            context.startActivity(intent)
                        }
                    )
                }
            }

            // زر سريع لخريطة المقاعد التفاعلية
            item {
                OutlinedButton(
                    onClick = onOpenInteractiveMap,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = LightAqua.copy(alpha = 0.35f),
                        contentColor = DeepNavy
                    )
                ) {
                    Icon(Icons.Default.EventSeat, contentDescription = null, tint = DeepTeal)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Interactive Seat Map", fontWeight = FontWeight.SemiBold)
                }
            }

            // 3. عنوان الأعضاء المقبولين
            item {
                Text(
                    text = "Accepted Members (${members.size})",
                    fontWeight = FontWeight.Bold,
                    color = DeepNavy,
                    fontSize = 17.sp
                )
            }

            // قائمة الأعضاء المقبولين بأدوارهم
            items(members) { member ->
                AcceptedMemberCard(member = member)
            }
        }
    }
}

@Composable
private fun ResourceLinkCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        modifier = modifier.clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = title, tint = contentColor)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(title, color = contentColor, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Text(subtitle, color = contentColor.copy(alpha = 0.8f), fontSize = 12.sp)
        }
    }
}

@Composable
private fun AcceptedMemberCard(member: WorkspaceMember) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SoftBeige),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(if (member.isLeader) Peach else Turquoise),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = member.name.take(1).uppercase(),
                    color = if (member.isLeader) DeepNavy else Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = member.name,
                        fontWeight = FontWeight.Bold,
                        color = DeepNavy,
                        fontSize = 15.sp
                    )
                    if (member.isLeader) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            color = Peach,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "Leader",
                                color = DeepNavy,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
                Text(
                    text = member.role,
                    color = DeepTeal,
                    fontSize = 13.sp
                )
            }
            Surface(
                color = LightAqua,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = member.seatCode,
                    color = DeepNavy,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }
    }
}

private fun sampleWorkspaceMembers() = listOf(
    WorkspaceMember("1", "Ahmed Mahmoud", "Team Leader & Full Stack", "A1", isLeader = true),
    WorkspaceMember("2", "Alaa Essam", "UI/UX & Android Developer", "A2"),
    WorkspaceMember("3", "Alice Adel", "Lead Backend Developer", "B1"),
    WorkspaceMember("4", "David Nabil", "Database & QA Engineer", "B2")
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TeamWorkspaceScreenPreview() {
    TeamWorkspaceScreen()
}
