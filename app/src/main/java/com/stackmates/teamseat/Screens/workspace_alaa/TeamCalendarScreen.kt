package com.stackmates.teamseat.Screens.workspace_alaa

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTimePickerState
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
import androidx.compose.ui.draw.scale
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
import com.stackmates.teamseat.ui.components.TeamSeatSurfaceColor
import com.stackmates.teamseat.ui.components.TeamSeatTitleDark
import com.stackmates.teamseat.ui.components.TeamSeatWarmOrange
import com.stackmates.teamseat.ui.components.bounceClick
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
                                .background(if (isSelected) TeamSeatPrimaryTeal else TeamSeatSurfaceColor)
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
    var selectedDateMillis by remember { mutableStateOf(System.currentTimeMillis()) }
    var selectedHour by remember { mutableIntStateOf(14) }
    var selectedMinute by remember { mutableIntStateOf(0) }
    var type by remember { mutableStateOf("ميتنج") }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val formattedDate = remember(selectedDateMillis) {
        SimpleDateFormat("EEEE d MMMM", Locale("ar")).format(Date(selectedDateMillis))
    }
    val formattedTime = remember(selectedHour, selectedMinute) {
        val isPm = selectedHour >= 12
        val hour12 = when {
            selectedHour == 0 -> 12
            selectedHour > 12 -> selectedHour - 12
            else -> selectedHour
        }
        val minStr = selectedMinute.toString().padStart(2, '0')
        "$hour12:$minStr ${if (isPm) "م" else "ص"}"
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = TeamSeatSurfaceColor,
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("ميتنج", "تسليم", "مناقشة").forEach { option ->
                    val selected = type == option
                    val chipBg by animateColorAsState(
                        targetValue = if (selected) TeamSeatPrimaryTeal else TeamSeatSoftTealPill,
                        label = "typeChipBg"
                    )
                    val chipScale by animateFloatAsState(
                        targetValue = if (selected) 1.04f else 1f,
                        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                        label = "typeChipScale"
                    )
                    Surface(
                        color = chipBg,
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .scale(chipScale)
                            .bounceClick { type = option }
                    ) {
                        Text(
                            text = option,
                            fontFamily = TajawalFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = if (selected) Color.White else TeamSeatPrimaryTeal,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                        )
                    }
                }
            }

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("عنوان الموعد", fontFamily = TajawalFontFamily) },
                textStyle = LocalTextStyle.current.copy(
                    fontFamily = TajawalFontFamily,
                    color = TeamSeatTitleDark
                ),
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
                TeamSeatCard(
                    modifier = Modifier.weight(1f),
                    contentPadding = 12.dp,
                    onClick = { showDatePicker = true }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.CalendarToday,
                            contentDescription = "اختر التاريخ",
                            tint = TeamSeatPrimaryTeal,
                            modifier = Modifier.size(18.dp)
                        )
                        Column {
                            Text(
                                text = "التاريخ",
                                fontFamily = TajawalFontFamily,
                                fontSize = 11.sp,
                                color = TeamSeatBodyMuted
                            )
                            Text(
                                text = formattedDate,
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.5.sp,
                                color = TeamSeatTitleDark,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                TeamSeatCard(
                    modifier = Modifier.weight(1f),
                    contentPadding = 12.dp,
                    onClick = { showTimePicker = true }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.AccessTime,
                            contentDescription = "اختر الساعة",
                            tint = TeamSeatPrimaryTeal,
                            modifier = Modifier.size(18.dp)
                        )
                        Column {
                            Text(
                                text = "الساعة",
                                fontFamily = TajawalFontFamily,
                                fontSize = 11.sp,
                                color = TeamSeatBodyMuted
                            )
                            Text(
                                text = formattedTime,
                                fontFamily = TajawalFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = TeamSeatTitleDark,
                                maxLines = 1
                            )
                        }
                    }
                }
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
                            onAdd(title.trim(), formattedDate, formattedTime, type)
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

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = selectedDateMillis)
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { selectedDateMillis = it }
                        showDatePicker = false
                    }
                ) {
                    Text("تأكيد", fontFamily = TajawalFontFamily, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("إلغاء", fontFamily = TajawalFontFamily)
                }
            }
        ) {
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + scaleIn(initialScale = 0.94f),
                exit = fadeOut() + scaleOut(targetScale = 0.94f)
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }

    if (showTimePicker) {
        val timePickerState = rememberTimePickerState(
            initialHour = selectedHour,
            initialMinute = selectedMinute,
            is24Hour = false
        )
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            title = {
                Text(
                    text = "اختر الساعة",
                    fontFamily = TajawalFontFamily,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn() + scaleIn(initialScale = 0.92f),
                    exit = fadeOut() + scaleOut(targetScale = 0.92f)
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        TimePicker(state = timePickerState)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        selectedHour = timePickerState.hour
                        selectedMinute = timePickerState.minute
                        showTimePicker = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = TeamSeatPrimaryTeal)
                ) {
                    Text("تأكيد", fontFamily = TajawalFontFamily, color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showTimePicker = false }) {
                    Text("إلغاء", fontFamily = TajawalFontFamily)
                }
            }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TeamCalendarScreenPreview() {
    TeamSeatTheme {
        TeamCalendarScreen()
    }
}
