package com.stackmates.teamseat.Screens.admin_youssef

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AdminPanelSettings
import androidx.compose.material.icons.rounded.Block
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.LockOpen
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stackmates.teamseat.core.model.Team
import com.stackmates.teamseat.core.model.User
import com.stackmates.teamseat.core.theme.TajawalFontFamily
import com.stackmates.teamseat.ui.components.TeamSeatBodyMuted
import com.stackmates.teamseat.ui.components.TeamSeatCard
import com.stackmates.teamseat.ui.components.TeamSeatCardBorderTeal
import com.stackmates.teamseat.ui.components.TeamSeatDangerRed
import com.stackmates.teamseat.ui.components.TeamSeatPrimaryTeal
import com.stackmates.teamseat.ui.components.TeamSeatScreenContainer
import com.stackmates.teamseat.ui.components.TeamSeatSoftTealPill
import com.stackmates.teamseat.ui.components.TeamSeatTitleDark
import com.stackmates.teamseat.ui.components.TeamSeatWarmOrange
import com.stackmates.teamseat.ui.components.bounceClick

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuperAdminScreen(
    adminEmail: String,
    users: List<User>,
    teams: List<Team>,
    onToggleBanUser: (uid: String, banned: Boolean) -> Unit,
    onToggleAdminUser: (uid: String, isAdmin: Boolean) -> Unit = { _, _ -> },
    onDeleteUser: (uid: String) -> Unit,
    onToggleLockTeam: (teamId: String, locked: Boolean) -> Unit,
    onDeleteTeam: (teamId: String) -> Unit,
    onBackClick: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0 = Users, 1 = Teams
    var userToDelete by remember { mutableStateOf<User?>(null) }
    var teamToDelete by remember { mutableStateOf<Team?>(null) }

    TeamSeatScreenContainer(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent),
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "رجوع",
                            tint = TeamSeatTitleDark
                        )
                    }
                },
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "لوحة تحكم المسؤول",
                            fontFamily = TajawalFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = TeamSeatTitleDark
                        )
                        Text(
                            text = "Super Admin • $adminEmail",
                            fontFamily = TajawalFontFamily,
                            fontSize = 11.sp,
                            color = TeamSeatPrimaryTeal,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            )
        }
    ) { safePadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = safePadding.calculateTopPadding() + 8.dp,
                bottom = safePadding.calculateBottomPadding()
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                TeamSeatCard(isWarmHighlight = true, contentPadding = 14.dp) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFFF0E0)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.AdminPanelSettings,
                                contentDescription = null,
                                tint = TeamSeatWarmOrange
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "إحصائيات المنصة الحية",
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = TeamSeatTitleDark,
                                fontSize = 14.5.sp
                            )
                            Text(
                                text = "${users.size} مستخدم (${users.count { it.isBanned }} محظور) • ${teams.size} تيم (${teams.count { it.isLocked }} مقفول)",
                                fontFamily = TajawalFontFamily,
                                color = TeamSeatBodyMuted,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AdminTabButton(
                        title = "المستخدمون (${users.size})",
                        selected = selectedTab == 0,
                        modifier = Modifier.weight(1f),
                        onClick = { selectedTab = 0 }
                    )
                    AdminTabButton(
                        title = "التيمات (${teams.size})",
                        selected = selectedTab == 1,
                        modifier = Modifier.weight(1f),
                        onClick = { selectedTab = 1 }
                    )
                }
            }

            if (selectedTab == 0) {
                items(users, key = { it.uid }) { u ->
                    val isSelfAdmin = u.email.equals(adminEmail, ignoreCase = true)
                    TeamSeatCard(isWarmHighlight = u.isBanned, contentPadding = 14.dp) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(TeamSeatSoftTealPill),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Person,
                                        contentDescription = null,
                                        tint = TeamSeatPrimaryTeal
                                    )
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = u.name.ifBlank { "بدون اسم" },
                                        fontFamily = TajawalFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        color = TeamSeatTitleDark,
                                        fontSize = 14.5.sp
                                    )
                                    Text(
                                        text = "${u.email} • ID: ${u.studentId.ifBlank { "—" }}",
                                        fontFamily = TajawalFontFamily,
                                        color = TeamSeatBodyMuted,
                                        fontSize = 11.5.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = u.roleTag,
                                        fontFamily = TajawalFontFamily,
                                        color = TeamSeatPrimaryTeal,
                                        fontSize = 11.5.sp
                                    )
                                }
                                Surface(
                                    color = when {
                                        isSelfAdmin -> TeamSeatSoftTealPill
                                        u.isBanned -> Color(0xFFFFEBEE)
                                        else -> TeamSeatSoftTealPill
                                    },
                                    shape = RoundedCornerShape(50)
                                ) {
                                    Text(
                                        text = when {
                                            isSelfAdmin || u.isAdmin -> "Admin"
                                            u.isBanned -> "محظور"
                                            else -> "نشط"
                                        },
                                        fontFamily = TajawalFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        color = if (u.isBanned) TeamSeatDangerRed else TeamSeatPrimaryTeal,
                                        fontSize = 11.sp,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            if (!isSelfAdmin) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedButton(
                                        onClick = { onToggleBanUser(u.uid, !u.isBanned) },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(10.dp),
                                        border = BorderStroke(
                                            1.dp,
                                            if (u.isBanned) TeamSeatPrimaryTeal else TeamSeatWarmOrange
                                        )
                                    ) {
                                        Icon(
                                            imageVector = if (u.isBanned) Icons.Rounded.CheckCircle else Icons.Rounded.Block,
                                            contentDescription = null,
                                            tint = if (u.isBanned) TeamSeatPrimaryTeal else TeamSeatWarmOrange,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.size(6.dp))
                                        Text(
                                            text = if (u.isBanned) "فك الحظر" else "حظر الحساب",
                                            fontFamily = TajawalFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            color = if (u.isBanned) TeamSeatPrimaryTeal else TeamSeatWarmOrange,
                                            fontSize = 12.sp
                                        )
                                    }
                                    OutlinedButton(
                                        onClick = { userToDelete = u },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(10.dp),
                                        border = BorderStroke(1.dp, TeamSeatDangerRed)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.Delete,
                                            contentDescription = null,
                                            tint = TeamSeatDangerRed,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.size(6.dp))
                                        Text(
                                            text = "حذف البيانات",
                                            fontFamily = TajawalFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            color = TeamSeatDangerRed,
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                                OutlinedButton(
                                    onClick = { onToggleAdminUser(u.uid, !u.isAdmin) },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp),
                                    border = BorderStroke(
                                        1.dp,
                                        if (u.isAdmin) TeamSeatWarmOrange else TeamSeatPrimaryTeal
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.AdminPanelSettings,
                                        contentDescription = null,
                                        tint = if (u.isAdmin) TeamSeatWarmOrange else TeamSeatPrimaryTeal,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.size(6.dp))
                                    Text(
                                        text = if (u.isAdmin) "إزالة صلاحية الأدمن" else "ترقية لأدمن (إشراف معك)",
                                        fontFamily = TajawalFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        color = if (u.isAdmin) TeamSeatWarmOrange else TeamSeatPrimaryTeal,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                items(teams, key = { it.teamId }) { t ->
                    TeamSeatCard(isWarmHighlight = t.isLocked, contentPadding = 14.dp) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(TeamSeatSoftTealPill),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Groups,
                                        contentDescription = null,
                                        tint = TeamSeatPrimaryTeal
                                    )
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = t.projectName,
                                        fontFamily = TajawalFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        color = TeamSeatTitleDark,
                                        fontSize = 14.5.sp
                                    )
                                    Text(
                                        text = "القائد: ${t.leaderName} • ${t.totalSeats} مقاعد",
                                        fontFamily = TajawalFontFamily,
                                        color = TeamSeatBodyMuted,
                                        fontSize = 12.sp
                                    )
                                }
                                Surface(
                                    color = if (t.isLocked) Color(0xFFFFF0E0) else TeamSeatSoftTealPill,
                                    shape = RoundedCornerShape(50)
                                ) {
                                    Text(
                                        text = if (t.isLocked) "مقفل" else "مفتوح",
                                        fontFamily = TajawalFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        color = if (t.isLocked) TeamSeatWarmOrange else TeamSeatPrimaryTeal,
                                        fontSize = 11.sp,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { onToggleLockTeam(t.teamId, !t.isLocked) },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp),
                                    border = BorderStroke(1.dp, TeamSeatPrimaryTeal)
                                ) {
                                    Icon(
                                        imageVector = if (t.isLocked) Icons.Rounded.LockOpen else Icons.Rounded.Lock,
                                        contentDescription = null,
                                        tint = TeamSeatPrimaryTeal,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.size(6.dp))
                                    Text(
                                        text = if (t.isLocked) "فتح التيم" else "قفل التيم",
                                        fontFamily = TajawalFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        color = TeamSeatPrimaryTeal,
                                        fontSize = 12.sp
                                    )
                                }
                                OutlinedButton(
                                    onClick = { teamToDelete = t },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp),
                                    border = BorderStroke(1.dp, TeamSeatDangerRed)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Delete,
                                        contentDescription = null,
                                        tint = TeamSeatDangerRed,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.size(6.dp))
                                    Text(
                                        text = "حذف التيم",
                                        fontFamily = TajawalFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        color = TeamSeatDangerRed,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        val targetUser = userToDelete
        if (targetUser != null) {
            AlertDialog(
                onDismissRequest = { userToDelete = null },
                title = {
                    Text(
                        text = "حذف بيانات المستخدم",
                        fontFamily = TajawalFontFamily,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Text(
                        text = "هل تريد حذف بيانات ${targetUser.name} (${targetUser.email}) وإفراغ أي مقعد يشغله؟",
                        fontFamily = TajawalFontFamily
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            onDeleteUser(targetUser.uid)
                            userToDelete = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = TeamSeatDangerRed)
                    ) {
                        Text("حذف", fontFamily = TajawalFontFamily, color = Color.White)
                    }
                },
                dismissButton = {
                    OutlinedButton(onClick = { userToDelete = null }) {
                        Text("إلغاء", fontFamily = TajawalFontFamily)
                    }
                }
            )
        }

        val targetTeam = teamToDelete
        if (targetTeam != null) {
            AlertDialog(
                onDismissRequest = { teamToDelete = null },
                title = {
                    Text(
                        text = "حذف التيم نهائياً",
                        fontFamily = TajawalFontFamily,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Text(
                        text = "هل أنت متأكد من حذف تيم ${targetTeam.projectName}؟ سيتم حذف كل مقاعده وإخطار الأعضاء.",
                        fontFamily = TajawalFontFamily
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            onDeleteTeam(targetTeam.teamId)
                            teamToDelete = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = TeamSeatDangerRed)
                    ) {
                        Text("حذف التيم", fontFamily = TajawalFontFamily, color = Color.White)
                    }
                },
                dismissButton = {
                    OutlinedButton(onClick = { teamToDelete = null }) {
                        Text("إلغاء", fontFamily = TajawalFontFamily)
                    }
                }
            )
        }
    }
}

@Composable
private fun AdminTabButton(
    title: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(42.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (selected) TeamSeatPrimaryTeal else Color.White)
            .border(
                1.dp,
                if (selected) TeamSeatPrimaryTeal else TeamSeatCardBorderTeal,
                RoundedCornerShape(12.dp)
            )
            .bounceClick(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            fontFamily = TajawalFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = if (selected) Color.White else TeamSeatTitleDark
        )
    }
}
