package com.stackmates.teamseat.Screens.admin_youssef

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stackmates.teamseat.core.data.MockDataProvider
import com.stackmates.teamseat.core.model.DemoPersona
import com.stackmates.teamseat.core.model.User
import com.stackmates.teamseat.core.theme.TajawalFontFamily
import com.stackmates.teamseat.core.theme.TeamSeatTheme
import com.stackmates.teamseat.ui.components.*

/**
 * 💻 المسؤول عن الشاشة: يوسف (Youssef - Team Lead & Backend)
 * شاشة تبديل الحساب (DemoControlCenterScreen)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DemoControlCenterScreen(
    currentPersona: DemoPersona = DemoPersona.AHMED_LEADER,
    currentUser: User = MockDataProvider.ahmedLeader,
    onTogglePersonaClick: () -> Unit = {},
    onResetMockDataClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val context = LocalContext.current
    var selectedPersona by remember { mutableStateOf(currentPersona) }
    var showResetSheet by remember { mutableStateOf(false) }

    val isLeaderActive = selectedPersona == DemoPersona.AHMED_LEADER

    TeamSeatScreenContainer(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "بدّل الحساب",
                        fontFamily = TajawalFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = TeamSeatTitleDark,
                        fontSize = 18.sp,
                        maxLines = 1,
                        softWrap = false
                    )
                },
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { safePadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(safePadding)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                AnimatedContent(
                    targetState = isLeaderActive,
                    transitionSpec = {
                        (slideInHorizontally(tween(260)) { it / 5 } + fadeIn(tween(260)))
                            .togetherWith(slideOutHorizontally(tween(200)) { -it / 5 } + fadeOut(tween(200)))
                    },
                    label = "activePersonaBannerTransition"
                ) { leaderActive ->
                    TeamSeatCard(
                        isWarmHighlight = true,
                        contentPadding = 14.dp
                    ) {
                        Text(
                            text = if (leaderActive) {
                                "الحساب الشغال دلوقتي: أحمد محمود (قائد التيم)"
                            } else {
                                "الحساب الشغال دلوقتي: سارة علي (عضو / متقدم)"
                            },
                            fontFamily = TajawalFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = TeamSeatTitleDark,
                            fontSize = 13.sp,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }

                AccountSwitchCard(
                    name = "أحمد محمود",
                    role = "قائد الفريق",
                    emoji = "👑",
                    isActive = isLeaderActive,
                    onClick = {
                        if (!isLeaderActive) {
                            selectedPersona = DemoPersona.AHMED_LEADER
                            onTogglePersonaClick()
                            Toast.makeText(context, "تم التبديل لحساب قائد التيم", Toast.LENGTH_SHORT).show()
                        }
                    }
                )

                AccountSwitchCard(
                    name = "سارة علي",
                    role = "عضو / متقدم",
                    emoji = "🎓",
                    isActive = !isLeaderActive,
                    onClick = {
                        if (isLeaderActive) {
                            selectedPersona = DemoPersona.SARA_STUDENT
                            onTogglePersonaClick()
                            Toast.makeText(context, "تم التبديل لحساب سارة", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White)
                    .border(1.2.dp, TeamSeatCardBorderTeal, RoundedCornerShape(14.dp))
                    .bounceClick { showResetSheet = true },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Refresh,
                        contentDescription = null,
                        tint = TeamSeatPrimaryTeal,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "رجّع التجربة من الأول",
                        fontFamily = TajawalFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.5.sp,
                        color = TeamSeatPrimaryTeal,
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }
        }
    }

    if (showResetSheet) {
        ModalBottomSheet(
            onDismissRequest = { showResetSheet = false },
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
                    text = "إعادة ضبط التجربة",
                    fontFamily = TajawalFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = TeamSeatTitleDark,
                    fontSize = 16.sp
                )
                Text(
                    text = "عايز ترجّع المقاعد والطلبات لحالتها الأساسية من الأول؟",
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
                            .background(TeamSeatPrimaryTeal)
                            .bounceClick {
                                showResetSheet = false
                                onResetMockDataClick()
                                Toast.makeText(context, "تمام! التجربة رجعت زي الأول", Toast.LENGTH_SHORT).show()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "تأكيد الضبط",
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
                            .bounceClick { showResetSheet = false },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "إلغاء",
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

@Composable
private fun AccountSwitchCard(
    name: String,
    role: String,
    emoji: String,
    isActive: Boolean,
    onClick: () -> Unit
) {
    TeamSeatCard(
        isWarmHighlight = isActive,
        contentPadding = 16.dp,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(
                            1.2.dp,
                            if (isActive) TeamSeatCardBorderWarm else TeamSeatCardBorderTeal,
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = emoji,
                        fontSize = 22.sp
                    )
                }

                Column(verticalArrangement = Arrangement.Center) {
                    Text(
                        text = name,
                        fontFamily = TajawalFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = TeamSeatTitleDark,
                        fontSize = 15.sp,
                        maxLines = 1,
                        softWrap = false
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = role,
                        fontFamily = TajawalFontFamily,
                        fontWeight = FontWeight.Medium,
                        color = TeamSeatBodyMuted,
                        fontSize = 12.5.sp,
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }

            AnimatedContent(
                targetState = isActive,
                transitionSpec = {
                    (slideInHorizontally(tween(220)) { -it / 4 } + fadeIn(tween(220)))
                        .togetherWith(slideOutHorizontally(tween(180)) { it / 4 } + fadeOut(tween(180)))
                },
                label = "personaBadgeTransition"
            ) { active ->
                if (active) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(TeamSeatWarmOrange)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "شغّال",
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White,
                                fontSize = 12.sp,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.White)
                            .border(1.dp, TeamSeatCardBorderTeal, RoundedCornerShape(10.dp))
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "بدّل",
                            fontFamily = TajawalFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = TeamSeatPrimaryTeal,
                            fontSize = 12.sp,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DemoControlCenterScreenPreview() {
    TeamSeatTheme {
        DemoControlCenterScreen()
    }
}
