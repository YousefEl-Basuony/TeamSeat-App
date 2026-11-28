package com.stackmates.teamseat.Screens.workspace_alaa

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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.DoneAll
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stackmates.teamseat.core.data.MockDataProvider
import com.stackmates.teamseat.core.model.Team
import com.stackmates.teamseat.core.model.TeamChatMessage
import com.stackmates.teamseat.core.theme.TajawalFontFamily
import com.stackmates.teamseat.core.theme.TeamSeatTheme
import com.stackmates.teamseat.ui.components.StaggeredAnimatedItem
import com.stackmates.teamseat.ui.components.TeamSeatBodyMuted
import com.stackmates.teamseat.ui.components.TeamSeatCardBorderTeal
import com.stackmates.teamseat.ui.components.TeamSeatCardTopTeal
import com.stackmates.teamseat.ui.components.TeamSeatDividerColor
import com.stackmates.teamseat.ui.components.TeamSeatPrimaryTeal
import com.stackmates.teamseat.ui.components.TeamSeatScreenContainer
import com.stackmates.teamseat.ui.components.TeamSeatSoftTealPill
import com.stackmates.teamseat.ui.components.TeamSeatSurfaceColor
import com.stackmates.teamseat.ui.components.TeamSeatTitleDark
import com.stackmates.teamseat.ui.components.UserAvatarContent
import com.stackmates.teamseat.ui.components.bounceClick

data class ChatMessage(
    val id: String,
    val senderName: String,
    val text: String,
    val timestamp: String,
    val isMe: Boolean,
    val senderAvatarEmoji: String = "👩‍💻",
    val senderPhotoBase64: String = "",
    val isReadByOthers: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamGroupChatScreen(
    team: Team = MockDataProvider.sampleTeam,
    liveMessages: List<TeamChatMessage>? = null,
    currentUserId: String = "",
    onSendMessageClick: (messageText: String) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    var messageInput by remember { mutableStateOf("") }
    val localMessages = remember {
        mutableStateListOf(
            ChatMessage("1", "آلاء", "السلام عليكم يا شباب، رفعت شاشات الـ Workspace على البرانش.", "10:15 ص", isMe = true),
            ChatMessage("2", "سارة", "عاش يا آلاء! أنا خلصت شاشات التقديم وطلباتي.", "10:17 ص", isMe = false),
            ChatMessage("3", "يوسف", "ممتاز، هنراجع كل الشاشات النهارده قبل تسليم م. شهد.", "10:19 ص", isMe = false),
            ChatMessage("4", "آلاء", "تمام جداً، خلونا نتابع المواعيد في تقويم التيم.", "10:21 ص", isMe = true)
        )
    }
    val displayMessages = remember(liveMessages, localMessages.size, currentUserId) {
        liveMessages?.map { msg ->
            ChatMessage(
                id = msg.id,
                senderName = msg.senderName,
                text = msg.text,
                timestamp = msg.timeLabel,
                isMe = currentUserId.isNotBlank() && msg.senderId == currentUserId,
                senderAvatarEmoji = msg.senderAvatarEmoji,
                senderPhotoBase64 = msg.senderPhotoBase64,
                isReadByOthers = msg.readBy.any { it.isNotBlank() && it != currentUserId }
            )
        } ?: localMessages
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        TeamSeatScreenContainer(
            topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent
                    ),
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
                                text = "شات الفريق",
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = TeamSeatTitleDark,
                                maxLines = 1,
                                softWrap = false
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Groups,
                                    contentDescription = null,
                                    tint = TeamSeatPrimaryTeal,
                                    modifier = Modifier.size(13.dp)
                                )
                                Text(
                                    text = "${team.projectName} • ${team.totalSeats} أعضاء",
                                    fontFamily = TajawalFontFamily,
                                    fontSize = 11.5.sp,
                                    color = TeamSeatPrimaryTeal,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                )
            },
            bottomBar = {
                Surface(
                    color = Color.White,
                    shadowElevation = 8.dp
                ) {
                    Column {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(TeamSeatDividerColor)
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .navigationBarsPadding()
                                .imePadding()
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = messageInput,
                                onValueChange = { messageInput = it },
                                textStyle = LocalTextStyle.current.copy(
                                    fontFamily = TajawalFontFamily,
                                    fontSize = 14.sp
                                ),
                                placeholder = {
                                    Text(
                                        text = "اكتب رسالتك للفريق...",
                                        fontFamily = TajawalFontFamily,
                                        color = TeamSeatBodyMuted
                                    )
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(24.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color(0xFFF6FAFA),
                                    unfocusedContainerColor = Color(0xFFF6FAFA),
                                    focusedBorderColor = TeamSeatPrimaryTeal,
                                    unfocusedBorderColor = TeamSeatCardBorderTeal
                                ),
                                maxLines = 3
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(TeamSeatPrimaryTeal)
                                    .bounceClick {
                                        val trimmed = messageInput.trim()
                                        if (trimmed.isNotEmpty()) {
                                            if (liveMessages == null) {
                                                localMessages.add(
                                                    ChatMessage(
                                                        id = (localMessages.size + 1).toString(),
                                                        senderName = "أنا",
                                                        text = trimmed,
                                                        timestamp = "الآن",
                                                        isMe = true
                                                    )
                                                )
                                            }
                                            onSendMessageClick(trimmed)
                                            messageInput = ""
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Rounded.Send,
                                    contentDescription = "إرسال",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(top = 8.dp, bottom = 16.dp)
            ) {
                itemsIndexed(displayMessages, key = { _, msg -> msg.id }) { index, msg ->
                    StaggeredAnimatedItem(index = index) {
                        ChatBubble(message = msg)
                    }
                }
            }
        }
    }
}

@Composable
private fun ChatBubble(message: ChatMessage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isMe) Arrangement.Start else Arrangement.End,
        verticalAlignment = Alignment.Bottom
    ) {
        Column(
            horizontalAlignment = if (message.isMe) Alignment.Start else Alignment.End
        ) {
            if (!message.isMe) {
                Text(
                    text = message.senderName,
                    fontFamily = TajawalFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.5.sp,
                    color = TeamSeatPrimaryTeal,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
            val bubbleShape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (message.isMe) 4.dp else 16.dp,
                bottomEnd = if (message.isMe) 16.dp else 4.dp
            )
            Box(
                modifier = Modifier
                    .widthIn(max = 270.dp)
                    .clip(bubbleShape)
                    .then(
                        if (message.isMe) {
                            Modifier.background(TeamSeatPrimaryTeal)
                        } else {
                            Modifier
                                .background(Brush.verticalGradient(listOf(TeamSeatCardTopTeal, TeamSeatSurfaceColor)))
                                .border(1.5.dp, TeamSeatCardBorderTeal, bubbleShape)
                        }
                    )
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Column {
                    Text(
                        text = message.text,
                        fontFamily = TajawalFontFamily,
                        color = if (message.isMe) Color.White else TeamSeatTitleDark,
                        fontSize = 14.sp,
                        lineHeight = 21.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.align(Alignment.End),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = message.timestamp,
                            fontFamily = TajawalFontFamily,
                            color = if (message.isMe) TeamSeatSoftTealPill else TeamSeatBodyMuted,
                            fontSize = 10.5.sp
                        )
                        if (message.isMe) {
                            Icon(
                                imageVector = if (message.isReadByOthers) Icons.Rounded.DoneAll else Icons.Rounded.Check,
                                contentDescription = if (message.isReadByOthers) "تمت القراءة" else "تم الإرسال",
                                tint = if (message.isReadByOthers) Color(0xFF80F3E8) else TeamSeatSoftTealPill,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }
        }
        if (!message.isMe) {
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(TeamSeatSoftTealPill)
                    .border(1.dp, TeamSeatCardBorderTeal, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                UserAvatarContent(
                    avatarEmoji = message.senderAvatarEmoji,
                    photoBase64 = message.senderPhotoBase64,
                    emojiFontSizeSp = 16f
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TeamGroupChatScreenPreview() {
    TeamSeatTheme {
        TeamGroupChatScreen()
    }
}
