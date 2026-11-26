package com.stackmates.teamseat.Screens.requests_hossam

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stackmates.teamseat.core.data.MockDataProvider
import com.stackmates.teamseat.core.model.User
import com.stackmates.teamseat.core.theme.TajawalFontFamily
import com.stackmates.teamseat.core.theme.TeamSeatTheme
import com.stackmates.teamseat.ui.components.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * ⚡ المسؤول عن الشاشة: حسام (Hossam) — [HOSSAM-1]
 * شاشة ملفي الشخصي وتعديل المهارات (MyProfileScreen)
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MyProfileScreen(
    user: User = MockDataProvider.saraStudent,
    onSaveChangesClick: (updatedRole: String, updatedGithub: String) -> Unit = { _, _ -> },
    onLogoutClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    var selectedRole by remember { mutableStateOf(user.roleTag) }
    var githubUrl by remember { mutableStateOf(user.githubUrl) }
    var linkedinUrl by remember { mutableStateOf(user.linkedinUrl) }
    var showLogoutSheet by remember { mutableStateOf(false) }
    var showSavedBanner by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val availableRoles = remember {
        listOf(
            "Android Developer",
            "Backend Developer",
            "UI/UX Designer",
            "Flutter Developer",
            "AI / ML Engineer",
            "QA & Tester"
        )
    }

    val isTeamMember = !user.currentTeamId.isNullOrEmpty()

    TeamSeatScreenContainer(
        snackbarHostState = snackbarHostState,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                navigationIcon = {
                    Box(
                        modifier = Modifier
                            .padding(start = 12.dp, end = 6.dp)
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(1.dp, TeamSeatCardBorderTeal, CircleShape)
                            .bounceClick(onClick = onBackClick),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "رجوع",
                            tint = TeamSeatTitleDark,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                title = {
                    Column {
                        Text(
                            text = "حسابي ومهاراتي",
                            fontFamily = TajawalFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = TeamSeatTitleDark,
                            fontSize = 18.sp,
                            maxLines = 1,
                            softWrap = false
                        )
                        Text(
                            text = "ظبّط تخصصك وروابطك عشان التيمات تلاقيك بسهولة",
                            fontFamily = TajawalFontFamily,
                            fontWeight = FontWeight.Normal,
                            color = TeamSeatBodyMuted,
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                },
                actions = {
                    Box(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(1.dp, TeamSeatDangerRed.copy(alpha = 0.35f), CircleShape)
                            .bounceClick { showLogoutSheet = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.Logout,
                            contentDescription = "تسجيل خروج",
                            tint = TeamSeatDangerRed,
                            modifier = Modifier.size(19.dp)
                        )
                    }
                }
            )
        },
        bottomBar = {}
    ) { safePadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = safePadding.calculateTopPadding() + 8.dp,
                bottom = 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                AnimatedVisibility(
                    visible = showSavedBanner,
                    enter = slideInVertically() + fadeIn(),
                    exit = slideOutVertically() + fadeOut()
                ) {
                    TeamSeatCard(contentPadding = 12.dp) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(26.dp)
                                    .clip(CircleShape)
                                    .background(TeamSeatPrimaryTeal),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                            Text(
                                text = "تم تحديث بيانات حسابك بنجاح ✓",
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = TeamSeatTitleDark,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            // 1. بطاقة الهوية الشخصية
            item {
                TeamSeatCard(contentPadding = 16.dp) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .border(1.5.dp, TeamSeatCardBorderTeal, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = user.avatarEmoji, fontSize = 30.sp)
                        }

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = user.name.substringBefore(" ("),
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = TeamSeatTitleDark,
                                fontSize = 16.5.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "${user.studentId} • ${user.major.substringBefore(" (")}",
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.Medium,
                                color = TeamSeatBodyMuted,
                                fontSize = 12.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.White)
                                    .border(1.dp, TeamSeatCardBorderTeal, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = selectedRole,
                                    style = TextStyle(
                                        fontFamily = TajawalFontFamily,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TeamSeatPrimaryTeal,
                                        fontSize = 11.5.sp,
                                        textDirection = TextDirection.Ltr
                                    ),
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }
                        }
                    }
                }
            }

            // 2. حالة الانضمام للتيم
            item {
                TeamSeatCard(isWarmHighlight = !isTeamMember, contentPadding = 14.dp) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(if (isTeamMember) TeamSeatPrimaryTeal else TeamSeatWarmOrange),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isTeamMember) Icons.Rounded.VerifiedUser else Icons.Rounded.HourglassTop,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = if (isTeamMember) "منضم لتيم رسمياً ✓" else "لسه بدوّر على تيم",
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = TeamSeatTitleDark,
                                fontSize = 14.5.sp
                            )
                            Text(
                                text = if (isTeamMember) {
                                    "مقعدك متسجل في (${user.currentTeamId})"
                                } else {
                                    "ممكن تبعت لحد 3 طلبات حجز في نفس الوقت"
                                },
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.Normal,
                                color = TeamSeatBodyMuted,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            // 3. اختيار التخصص الأساسي
            item {
                TeamSeatCard(contentPadding = 16.dp) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "تخصصك الأساسي",
                            fontFamily = TajawalFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = TeamSeatTitleDark,
                            fontSize = 15.sp
                        )

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            availableRoles.forEach { role ->
                                val isSelected = selectedRole == role
                                Box(
                                    modifier = Modifier
                                        .height(34.dp)
                                        .clip(RoundedCornerShape(17.dp))
                                        .background(if (isSelected) TeamSeatPrimaryTeal else Color.White)
                                        .border(
                                            1.dp,
                                            if (isSelected) TeamSeatPrimaryTeal else TeamSeatCardBorderTeal,
                                            RoundedCornerShape(17.dp)
                                        )
                                        .bounceClick { selectedRole = role }
                                        .padding(horizontal = 12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = role,
                                        style = TextStyle(
                                            fontFamily = TajawalFontFamily,
                                            fontWeight = FontWeight.SemiBold,
                                            color = if (isSelected) Color.White else TeamSeatTitleDark,
                                            fontSize = 12.sp,
                                            textDirection = TextDirection.Ltr
                                        ),
                                        maxLines = 1,
                                        softWrap = false
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 4. روابط GitHub و LinkedIn
            item {
                TeamSeatCard(contentPadding = 16.dp) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = "روابط شغلك والتواصل",
                            fontFamily = TajawalFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = TeamSeatTitleDark,
                            fontSize = 15.sp
                        )

                        OutlinedTextField(
                            value = githubUrl,
                            onValueChange = { githubUrl = it },
                            singleLine = true,
                            label = {
                                Text(
                                    text = "رابط GitHub",
                                    fontFamily = TajawalFontFamily,
                                    fontSize = 12.sp
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Rounded.Code,
                                    contentDescription = null,
                                    tint = TeamSeatPrimaryTeal
                                )
                            },
                            textStyle = TextStyle(
                                fontFamily = TajawalFontFamily,
                                fontSize = 13.sp,
                                textDirection = TextDirection.Ltr
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedBorderColor = TeamSeatPrimaryTeal,
                                unfocusedBorderColor = TeamSeatCardBorderTeal
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = linkedinUrl,
                            onValueChange = { linkedinUrl = it },
                            singleLine = true,
                            label = {
                                Text(
                                    text = "رابط LinkedIn",
                                    fontFamily = TajawalFontFamily,
                                    fontSize = 12.sp
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Rounded.Link,
                                    contentDescription = null,
                                    tint = TeamSeatPrimaryTeal
                                )
                            },
                            textStyle = TextStyle(
                                fontFamily = TajawalFontFamily,
                                fontSize = 13.sp,
                                textDirection = TextDirection.Ltr
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedBorderColor = TeamSeatPrimaryTeal,
                                unfocusedBorderColor = TeamSeatCardBorderTeal
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                        .height(50.dp)
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(14.dp),
                            ambientColor = TeamSeatPrimaryTeal,
                            spotColor = TeamSeatPrimaryTeal
                        )
                        .clip(RoundedCornerShape(14.dp))
                        .background(TeamSeatPrimaryTeal)
                        .bounceClick {
                            onSaveChangesClick(selectedRole, githubUrl)
                            showSavedBanner = true
                            scope.launch {
                                snackbarHostState.showSnackbar("تمام! حفظنا تعديلات حسابك")
                                delay(2500)
                                showSavedBanner = false
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Save,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(19.dp)
                        )
                        Text(
                            text = "احفظ التعديلات",
                            fontFamily = TajawalFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 14.5.sp,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }
            }
        }
    }

    if (showLogoutSheet) {
        ModalBottomSheet(
            onDismissRequest = { showLogoutSheet = false },
            containerColor = TeamSeatBgOffWhite,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "تسجيل الخروج",
                    fontFamily = TajawalFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = TeamSeatTitleDark,
                    fontSize = 16.sp
                )
                Text(
                    text = "متأكد إنك عايز تخرج من حسابك دلوقتي؟",
                    fontFamily = TajawalFontFamily,
                    fontWeight = FontWeight.Normal,
                    color = TeamSeatBodyMuted,
                    fontSize = 13.sp
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(TeamSeatDangerRed)
                            .bounceClick {
                                showLogoutSheet = false
                                onLogoutClick()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "اخرج",
                            fontFamily = TajawalFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                            fontSize = 13.sp,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .border(1.2.dp, TeamSeatCardBorderTeal, RoundedCornerShape(12.dp))
                            .bounceClick { showLogoutSheet = false },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "خلّيك",
                            fontFamily = TajawalFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = TeamSeatTitleDark,
                            fontSize = 13.sp,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun MyProfileScreenPreview() {
    TeamSeatTheme {
        MyProfileScreen()
    }
}
