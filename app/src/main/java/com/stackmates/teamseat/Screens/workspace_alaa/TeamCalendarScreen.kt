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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stackmates.teamseat.core.data.MockDataProvider
import com.stackmates.teamseat.core.model.CalendarEvent
import com.stackmates.teamseat.core.theme.TajawalFontFamily
import com.stackmates.teamseat.core.theme.TeamSeatTheme
import com.stackmates.teamseat.ui.components.StaggeredAnimatedItem
import com.stackmates.teamseat.ui.components.TeamSeatBodyMuted
import com.stackmates.teamseat.ui.components.TeamSeatCard
import com.stackmates.teamseat.ui.components.TeamSeatCardBorderTeal
import com.stackmates.teamseat.ui.components.TeamSeatPrimaryTeal
import com.stackmates.teamseat.ui.components.TeamSeatScreenContainer
import com.stackmates.teamseat.ui.components.TeamSeatSoftTealPill
import com.stackmates.teamseat.ui.components.TeamSeatTitleDark
import com.stackmates.teamseat.ui.components.TeamSeatWarmOrange
import com.stackmates.teamseat.ui.components.bounceClick

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamCalendarScreen(
    events: List<CalendarEvent> = MockDataProvider.sampleCalendarEvents,
    onAddEventClick: (title: String, date: String, time: String) -> Unit = { _, _, _ -> },
    onBackClick: () -> Unit = {}
) {
    var showAddSheet by remember { mutableStateOf(false) }
    var selectedDayIndex by remember { mutableIntStateOf(5) }
    val localEvents = remember(events) { mutableStateListOf(*events.toTypedArray()) }

    val days = listOf(
        "24" to "الثلاثاء",
        "25" to "الأربعاء",
        "26" to "الخميس",
        "27" to "الجمعة",
        "28" to "السبت",
        "29" to "الأحد"
    )

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
                        Text(
                            text = "تقويم التيم",
                            fontFamily = TajawalFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 19.sp,
                            color = TeamSeatTitleDark,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                )
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = { showAddSheet = true },
                    containerColor = TeamSeatPrimaryTeal,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(50),
                    icon = {
                        Icon(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = "إضافة موعد"
                        )
                    },
                    text = {
                        Text(
                            text = "إضافة موعد",
                            fontFamily = TajawalFontFamily,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
            ) {
                // شريط الأيام الأفقي
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    itemsIndexed(days) { idx, (dayNum, dayName) ->
                        val isSelected = idx == selectedDayIndex
                        val pillShape = RoundedCornerShape(14.dp)
                        Column(
                            modifier = Modifier
                                .width(64.dp)
                                .clip(pillShape)
                                .background(if (isSelected) TeamSeatPrimaryTeal else Color.White)
                                .border(
                                    width = 1.5.dp,
                                    color = if (isSelected) TeamSeatPrimaryTeal else TeamSeatCardBorderTeal,
                                    shape = pillShape
                                )
                                .bounceClick { selectedDayIndex = idx }
                                .padding(vertical = 10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = dayName,
                                fontFamily = TajawalFontFamily,
                                fontSize = 11.sp,
                                color = if (isSelected) TeamSeatSoftTealPill else TeamSeatBodyMuted,
                                maxLines = 1,
                                softWrap = false
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = dayNum,
                                fontFamily = TajawalFontFamily,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else TeamSeatTitleDark
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "المواعيد والمناقشات القادمة (${localEvents.size})",
                    fontFamily = TajawalFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = TeamSeatTitleDark
                )
                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 96.dp)
                ) {
                    itemsIndexed(localEvents, key = { _, item -> item.id }) { index, event ->
                        StaggeredAnimatedItem(index = index) {
                            MilestoneCard(event = event)
                        }
                    }
                }
            }
        }

        if (showAddSheet) {
            AddMilestoneBottomSheet(
                onDismiss = { showAddSheet = false },
                onAdd = { title, date, time, type ->
                    val newEvent = CalendarEvent(
                        id = (localEvents.size + 1).toString(),
                        teamId = MockDataProvider.sampleTeam.teamId,
                        title = title,
                        date = date,
                        time = time,
                        type = type
                    )
                    localEvents.add(0, newEvent)
                    onAddEventClick(title, date, time)
                    showAddSheet = false
                }
            )
        }
    }
}

@Composable
private fun MilestoneCard(event: CalendarEvent) {
    val isDeadline = event.type.contains("تسليم") || event.type.contains("ديدلاين")
    TeamSeatCard(
        isWarmHighlight = isDeadline,
        contentPadding = 16.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = event.title,
                fontFamily = TajawalFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 15.5.sp,
                color = TeamSeatTitleDark,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.width(8.dp))
            Surface(
                color = if (isDeadline) Color(0xFFFFF0E0) else TeamSeatSoftTealPill,
                shape = RoundedCornerShape(50)
            ) {
                Text(
                    text = event.type,
                    fontFamily = TajawalFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = if (isDeadline) TeamSeatWarmOrange else TeamSeatPrimaryTeal,
                    maxLines = 1,
                    softWrap = false,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Rounded.CalendarToday,
                    contentDescription = null,
                    tint = TeamSeatPrimaryTeal,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = event.date,
                    fontFamily = TajawalFontFamily,
                    fontSize = 12.5.sp,
                    color = TeamSeatTitleDark,
                    fontWeight = FontWeight.Medium
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Rounded.AccessTime,
                    contentDescription = null,
                    tint = TeamSeatPrimaryTeal,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = event.time,
                    fontFamily = TajawalFontFamily,
                    fontSize = 12.5.sp,
                    color = TeamSeatTitleDark,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddMilestoneBottomSheet(
    onDismiss: () -> Unit,
    onAdd: (title: String, date: String, time: String, type: String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var title by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("الأربعاء 3 ديسمبر") }
    var time by remember { mutableStateOf("02:00 م") }
    var type by remember { mutableStateOf("ميتنج") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "إضافة موعد جديد للتيم",
                fontFamily = TajawalFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = TeamSeatTitleDark
            )
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("عنوان الموعد", fontFamily = TajawalFontFamily) },
                textStyle = LocalTextStyle.current.copy(fontFamily = TajawalFontFamily),
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TeamSeatPrimaryTeal,
                    unfocusedBorderColor = TeamSeatCardBorderTeal
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it },
                    label = { Text("التاريخ", fontFamily = TajawalFontFamily) },
                    textStyle = LocalTextStyle.current.copy(fontFamily = TajawalFontFamily),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TeamSeatPrimaryTeal,
                        unfocusedBorderColor = TeamSeatCardBorderTeal
                    ),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = time,
                    onValueChange = { time = it },
                    label = { Text("الساعة", fontFamily = TajawalFontFamily) },
                    textStyle = LocalTextStyle.current.copy(fontFamily = TajawalFontFamily),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TeamSeatPrimaryTeal,
                        unfocusedBorderColor = TeamSeatCardBorderTeal
                    ),
                    modifier = Modifier.weight(1f)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = "إلغاء",
                        fontFamily = TajawalFontFamily,
                        color = TeamSeatTitleDark,
                        maxLines = 1,
                        softWrap = false
                    )
                }
                Button(
                    onClick = {
                        if (title.isNotBlank()) {
                            onAdd(title.trim(), date.trim(), time.trim(), type)
                        }
                    },
                    enabled = title.isNotBlank(),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TeamSeatPrimaryTeal)
                ) {
                    Text(
                        text = "حفظ الموعد",
                        fontFamily = TajawalFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TeamCalendarScreenPreview() {
    TeamSeatTheme {
        TeamCalendarScreen()
    }
}
