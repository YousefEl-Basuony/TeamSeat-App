package com.stackmates.teamseat.Screens.requests_hossam

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stackmates.teamseat.core.data.MockDataProvider
import com.stackmates.teamseat.core.model.User
import com.stackmates.teamseat.core.theme.DeepNavy
import com.stackmates.teamseat.core.theme.DeepTeal
import com.stackmates.teamseat.core.theme.LightAqua
import com.stackmates.teamseat.core.theme.Peach
import com.stackmates.teamseat.core.theme.SoftBeige
import com.stackmates.teamseat.core.theme.SoftGray
import com.stackmates.teamseat.core.theme.TeamSeatTheme
import com.stackmates.teamseat.core.theme.WarmCream

/**
 * ⚡ المسؤول عن الشاشة: حسام (Hossam) — [HOSSAM-1]
 * شاشة ملفي الشخصي وتعديل مهاراتي (My Profile Screen)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyProfileScreen(
    user: User = MockDataProvider.saraStudent,
    onSaveChangesClick: (updatedRole: String, updatedGithub: String) -> Unit = { _, _ -> },
    onLogoutClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    var roleTag by remember { mutableStateOf(user.roleTag) }
    var githubUrl by remember { mutableStateOf(user.githubUrl) }
    val isTeamMember = !user.currentTeamId.isNullOrEmpty()

    Scaffold(
        containerColor = WarmCream,
        topBar = {
            TopAppBar(
                title = { Text("ملفي الشخصي", fontWeight = FontWeight.Bold, color = DeepNavy) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "رجوع", tint = DeepNavy)
                    }
                },
                actions = {
                    IconButton(onClick = onLogoutClick) {
                        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "تسجيل الخروج", tint = Color(0xFFC0392B))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = WarmCream)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. صورة رمزية (Avatar) + اسم الطالب والرقم الجامعي
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(LightAqua),
                contentAlignment = Alignment.Center
            ) {
                Text(text = user.avatarEmoji, fontSize = 42.sp)
            }

            Text(
                text = user.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = DeepNavy
            )

            Text(
                text = "الرقم الجامعي: ${user.studentId} • ${user.major}",
                style = MaterialTheme.typography.bodyMedium,
                color = DeepNavy.copy(alpha = 0.7f)
            )

            // 2. حالة الانضمام الحالية (منضم لتيم أو يبحث عن مقعد)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isTeamMember) LightAqua.copy(alpha = 0.6f) else Peach.copy(alpha = 0.4f)
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = if (isTeamMember) "✅" else "⏳", fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (isTeamMember) "منضم لتيم معتمد" else "يبحث عن مقعد شاغر",
                            fontWeight = FontWeight.Bold,
                            color = DeepNavy
                        )
                        Text(
                            text = if (isTeamMember) "تم قبولك في تيم (#${user.currentTeamId})" else "لم تنضم لأي تيم بعد",
                            style = MaterialTheme.typography.bodySmall,
                            color = DeepNavy.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            // 3. كارت تعديل التخصص ورابط GitHub
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SoftBeige)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = roleTag,
                        onValueChange = { roleTag = it },
                        label = { Text("التخصص الأساسي") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = DeepTeal) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = WarmCream,
                            unfocusedContainerColor = WarmCream,
                            focusedBorderColor = DeepTeal,
                            unfocusedBorderColor = SoftGray
                        )
                    )

                    OutlinedTextField(
                        value = githubUrl,
                        onValueChange = { githubUrl = it },
                        label = { Text("رابط GitHub / Portfolio") },
                        leadingIcon = { Icon(Icons.Default.Code, contentDescription = null, tint = DeepTeal) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = WarmCream,
                            unfocusedContainerColor = WarmCream,
                            focusedBorderColor = DeepTeal,
                            unfocusedBorderColor = SoftGray
                        )
                    )

                    OutlinedTextField(
                        value = user.email,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("البريد الجامعي") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = DeepTeal) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = WarmCream,
                            unfocusedContainerColor = WarmCream,
                            focusedBorderColor = SoftGray,
                            unfocusedBorderColor = SoftGray
                        )
                    )
                }
            }

            // 4. أزرار حفظ التعديلات وتسجيل الخروج
            Button(
                onClick = { onSaveChangesClick(roleTag, githubUrl) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DeepTeal, contentColor = WarmCream)
            ) {
                Icon(Icons.Default.Save, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("حفظ التعديلات", fontWeight = FontWeight.Bold)
            }

            OutlinedButton(
                onClick = onLogoutClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFC0392B))
            ) {
                Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("تسجيل الخروج", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MyProfileScreenPreview() {
    TeamSeatTheme {
        MyProfileScreen()
    }
}
