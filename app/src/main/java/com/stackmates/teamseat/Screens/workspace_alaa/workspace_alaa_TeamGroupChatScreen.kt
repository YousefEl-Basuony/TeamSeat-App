package com.stackmates.teamseat.Screens.workspace_alaa

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
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

data class ChatMessage(
    val id: String,
    val senderName: String,
    val text: String,
    val timestamp: String,
    val isMe: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamGroupChatScreen(
    teamTitle: String = "Team Group Chat",
    activeMembersCount: Int = 5,
    onBackClick: () -> Unit = {}
) {
    var messageInput by remember { mutableStateOf("") }
    val messages = remember {
        mutableStateListOf(
            ChatMessage("1", "Alaa", "السلام عليكم يا شباب، رفعت شاشات الـ Workspace على البرانش.", "10:15 AM", isMe = true),
            ChatMessage("2", "Alice", "عاش يا آلاء! أنا خلصت الـ API الخاص بحجز المقاعد.", "10:17 AM", isMe = false),
            ChatMessage("3", "David", "ممتاز، هراجع الـ UI النهارده قبل تسليم م. شهد.", "10:19 AM", isMe = false),
            ChatMessage("4", "Alaa", "تمام جداً، خلونا نراجع المواعيد في التاب بتاع الـ Calendar.", "10:21 AM", isMe = true)
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
                    Column {
                        Text(teamTitle, color = DeepNavy, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("$activeMembersCount active members", color = Turquoise, fontSize = 12.sp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = WarmCream)
            )
        },
        bottomBar = {
            Surface(
                color = WarmCream,
                tonalElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = messageInput,
                        onValueChange = { messageInput = it },
                        placeholder = { Text("Type a message...", color = DeepNavy.copy(alpha = 0.5f)) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = SoftBeige,
                            unfocusedContainerColor = SoftBeige,
                            focusedBorderColor = DeepTeal,
                            unfocusedBorderColor = SoftGray
                        ),
                        maxLines = 3
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            if (messageInput.isNotBlank()) {
                                messages.add(
                                    ChatMessage(
                                        id = (messages.size + 1).toString(),
                                        senderName = "Alaa",
                                        text = messageInput.trim(),
                                        timestamp = "Now",
                                        isMe = true
                                    )
                                )
                                messageInput = ""
                            }
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(DeepTeal)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            items(messages) { msg ->
                ChatBubbleItem(message = msg)
            }
        }
    }
}

@Composable
private fun ChatBubbleItem(message: ChatMessage) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (message.isMe) Alignment.End else Alignment.Start
    ) {
        Text(
            text = if (message.isMe) "You" else message.senderName,
            fontSize = 11.sp,
            color = DeepNavy.copy(alpha = 0.7f),
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
        Surface(
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (message.isMe) 16.dp else 4.dp,
                bottomEnd = if (message.isMe) 4.dp else 16.dp
            ),
            color = if (message.isMe) DeepTeal else SoftBeige,
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
                Text(
                    text = message.text,
                    color = if (message.isMe) Color.White else DeepNavy,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = message.timestamp,
                    color = if (message.isMe) LightAqua else DeepNavy.copy(alpha = 0.6f),
                    fontSize = 10.sp,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TeamGroupChatScreenPreview() {
    TeamGroupChatScreen()
}
