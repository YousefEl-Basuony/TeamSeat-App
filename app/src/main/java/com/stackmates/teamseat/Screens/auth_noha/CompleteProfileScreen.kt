package com.stackmates.teamseat.Screens.auth_noha

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Devices
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stackmates.teamseat.core.theme.TajawalFontFamily
import com.stackmates.teamseat.core.theme.TeamSeatTheme

private val CpDeepNavy = Color(0xFF0A344A)
private val CpDeepTeal = Color(0xFF136F78)
private val CpTurquoise = Color(0xFF469D9F)
private val CpWarmCream = Color(0xFFFAF5EE)
private val CpSoftBeige = Color(0xFFF4E2DD)
private val CpSoftGray = Color(0xFFD8D3D3)

@Composable
fun CompleteProfileScreen(
    initialName: String = "",
    onSaveProfileClick: (name: String, studentId: String, role: String) -> Unit = { _, _, _ -> },
    errorMessage: String? = null,
    isLoading: Boolean = false
) {
    var fullName by remember(initialName) { mutableStateOf(initialName) }
    var studentId by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf("Developer") }



    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(CpWarmCream)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 22.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Team",
                        fontFamily = TajawalFontFamily,
                        color = CpDeepNavy,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "Seat",
                        fontFamily = TajawalFontFamily,
                        color = CpDeepTeal,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Complete Your Profile",
                    fontFamily = TajawalFontFamily,
                    color = CpDeepNavy,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "Add your Student ID & Role to join teams",
                    fontFamily = TajawalFontFamily,
                    color = CpDeepNavy.copy(alpha = 0.65f),
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(20.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.96f)),
                    border = BorderStroke(1.dp, CpSoftGray.copy(alpha = 0.45f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp, vertical = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        OutlinedTextField(
                            value = fullName,
                            onValueChange = { fullName = it },
                            textStyle = LocalTextStyle.current.copy(
                                fontFamily = TajawalFontFamily,
                                fontSize = 14.sp
                            ),
                            placeholder = {
                                Text(
                                    text = "Full Name",
                                    fontFamily = TajawalFontFamily,
                                    color = CpDeepNavy.copy(alpha = 0.45f),
                                    fontSize = 14.sp
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.Person,
                                    contentDescription = null,
                                    tint = CpTurquoise,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = CpSoftBeige.copy(alpha = 0.35f),
                                unfocusedContainerColor = CpSoftBeige.copy(alpha = 0.2f),
                                focusedBorderColor = CpDeepTeal,
                                unfocusedBorderColor = CpSoftGray.copy(alpha = 0.5f),
                                focusedTextColor = CpDeepNavy,
                                unfocusedTextColor = CpDeepNavy
                            ),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = studentId,
                            onValueChange = { studentId = it },
                            textStyle = LocalTextStyle.current.copy(
                                fontFamily = TajawalFontFamily,
                                fontSize = 14.sp
                            ),
                            placeholder = {
                                Text(
                                    text = "Student ID",
                                    fontFamily = TajawalFontFamily,
                                    color = CpDeepNavy.copy(alpha = 0.45f),
                                    fontSize = 14.sp
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.Badge,
                                    contentDescription = null,
                                    tint = CpTurquoise,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = CpSoftBeige.copy(alpha = 0.35f),
                                unfocusedContainerColor = CpSoftBeige.copy(alpha = 0.2f),
                                focusedBorderColor = CpDeepTeal,
                                unfocusedBorderColor = CpSoftGray.copy(alpha = 0.5f),
                                focusedTextColor = CpDeepNavy,
                                unfocusedTextColor = CpDeepNavy
                            ),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Select Your Role",
                                fontFamily = TajawalFontFamily,
                                color = CpDeepNavy,
                                fontSize = 14.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "تخصصك في الفريق",
                                fontFamily = TajawalFontFamily,
                                color = CpDeepTeal,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            CompleteRoleChip(
                                title = "Designer",
                                icon = Icons.Outlined.Palette,
                                selected = selectedRole == "Designer",
                                onClick = { selectedRole = "Designer" },
                                modifier = Modifier.weight(1f)
                            )
                            CompleteRoleChip(
                                title = "Developer",
                                icon = Icons.Outlined.Code,
                                selected = selectedRole == "Developer",
                                onClick = { selectedRole = "Developer" },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            CompleteRoleChip(
                                title = "Frontend",
                                icon = Icons.Outlined.Devices,
                                selected = selectedRole == "Frontend",
                                onClick = { selectedRole = "Frontend" },
                                modifier = Modifier.weight(1f)
                            )
                            CompleteRoleChip(
                                title = "QA",
                                icon = Icons.Outlined.BugReport,
                                selected = selectedRole == "QA",
                                onClick = { selectedRole = "QA" },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        if (!errorMessage.isNullOrBlank()) {
                            Surface(
                                color = Color(0xFFFDECEC),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = errorMessage,
                                    fontFamily = TajawalFontFamily,
                                    color = Color(0xFFC62828),
                                    fontSize = 12.5.sp,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                )
                            }
                        }

                        Button(
                            onClick = {
                                if (!isLoading) {
                                    onSaveProfileClick(fullName, studentId, selectedRole)
                                }
                            },
                            enabled = !isLoading,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .shadow(
                                    elevation = 4.dp,
                                    shape = RoundedCornerShape(26.dp),
                                    spotColor = CpDeepTeal.copy(alpha = 0.35f)
                                ),
                            shape = RoundedCornerShape(26.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = CpDeepNavy)
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    strokeWidth = 2.dp,
                                    modifier = Modifier.size(20.dp)
                                )
                            } else {
                                Text(
                                    text = "Save & Continue",
                                    fontFamily = TajawalFontFamily,
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CompleteRoleChip(
    title: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        color = if (selected) CpDeepTeal.copy(alpha = 0.12f) else Color.White,
        border = BorderStroke(
            width = if (selected) 1.5.dp else 1.dp,
            color = if (selected) CpDeepTeal else CpSoftGray.copy(alpha = 0.6f)
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (selected) CpDeepTeal else CpDeepNavy.copy(alpha = 0.65f),
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = title,
                    fontFamily = TajawalFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.5.sp,
                    color = if (selected) CpDeepTeal else CpDeepNavy
                )
            }
            if (selected) {
                Icon(
                    imageVector = Icons.Outlined.CheckCircle,
                    contentDescription = null,
                    tint = CpDeepTeal,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CompleteProfileScreenPreview() {
    TeamSeatTheme {
        CompleteProfileScreen(initialName = "Youssef")
    }
}
