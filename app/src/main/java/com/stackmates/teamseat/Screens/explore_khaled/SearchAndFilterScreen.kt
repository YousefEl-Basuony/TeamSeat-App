package com.stackmates.teamseat.Screens.explore_khaled

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.RestartAlt
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material.icons.outlined.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stackmates.teamseat.core.data.MockDataProvider
import com.stackmates.teamseat.core.model.SeatStatus
import com.stackmates.teamseat.core.model.Team
import com.stackmates.teamseat.core.theme.DeepNavy
import com.stackmates.teamseat.core.theme.DeepTeal
import com.stackmates.teamseat.core.theme.LeaderCrownGold
import com.stackmates.teamseat.core.theme.LightAqua
import com.stackmates.teamseat.core.theme.Peach
import com.stackmates.teamseat.core.theme.SeatAvailableBg
import com.stackmates.teamseat.core.theme.SeatLockedBg
import com.stackmates.teamseat.core.theme.SeatLockedGray
import com.stackmates.teamseat.core.theme.SoftBeige
import com.stackmates.teamseat.core.theme.SoftGray
import com.stackmates.teamseat.core.theme.TeamSeatTheme
import com.stackmates.teamseat.core.theme.Turquoise
import com.stackmates.teamseat.core.theme.WarmCream

/**
 * 🔍 المسؤول عن الشاشة: خالد (Khaled) — [KHALED-5]
 * شاشة البحث والفلترة المتقدمة لمشاريع وتيمات الجامعة (Search & Filter Screen)
 * تصميم متميز بمقاييس UI/UX احترافية وجودة عالية متوافقة مع الهوية البصرية لـ TeamSeat
 */
@Composable
fun SearchAndFilterScreen(
    teams: List<Team> = MockDataProvider.sampleTeamsList,
    roleFilters: List<String> = listOf("الكل", "Android", "Backend", "UI/UX", "Flutter", "AI"),
    onTeamClick: (teamId: String) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf(roleFilters.firstOrNull() ?: "الكل") }
    val focusManager = LocalFocusManager.current

    // منطق الفلترة الفوري والذكي للفرق
    val filteredTeams = teams.filter { team ->
        val queryTrimmed = searchQuery.trim()

        // 1. فلترة نص البحث (اسم المشروع، المادة، الوصف، اسم القائد، أو المشرف)
        val matchesQuery = queryTrimmed.isEmpty() ||
                team.projectName.contains(queryTrimmed, ignoreCase = true) ||
                team.category.contains(queryTrimmed, ignoreCase = true) ||
                team.description.contains(queryTrimmed, ignoreCase = true) ||
                team.leaderName.contains(queryTrimmed, ignoreCase = true) ||
                team.supervisorDoctor.contains(queryTrimmed, ignoreCase = true)

        // 2. فلترة التخصص الهندسي والتقني
        val teamRoles = getRolesForTeam(team)
        val isAllRole = selectedRole == "الكل" || selectedRole.equals("All", ignoreCase = true)
        val matchesRole = isAllRole || teamRoles.any { role ->
            role.contains(selectedRole, ignoreCase = true)
        } || team.description.contains(selectedRole, ignoreCase = true)

        matchesQuery && matchesRole
    }

    val isFilterActive = searchQuery.isNotBlank() || (selectedRole != "الكل" && !selectedRole.equals("All", ignoreCase = true))

    Scaffold(
        containerColor = WarmCream,
        topBar = {
            SearchTopHeader(
                onBackClick = onBackClick,
                isFilterActive = isFilterActive,
                onResetFilters = {
                    searchQuery = ""
                    selectedRole = roleFilters.firstOrNull() ?: "الكل"
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(top = 8.dp, bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // =================================================================
            // 1. شريط البحث الذكي (Smart Search Bar)
            // =================================================================
            item {
                Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        color = Color.White,
                        border = BorderStroke(1.2.dp, SoftGray.copy(alpha = 0.8f)),
                        shadowElevation = 2.dp
                    ) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = {
                                Text(
                                    text = "ابحث باسم المشروع، المادة، القائد...",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = Color.Gray,
                                        fontSize = 14.sp
                                    )
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.Search,
                                    contentDescription = "بحث",
                                    tint = DeepTeal,
                                    modifier = Modifier.size(22.dp)
                                )
                            },
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { searchQuery = "" }) {
                                        Surface(
                                            shape = CircleShape,
                                            color = SoftBeige,
                                            modifier = Modifier.size(24.dp)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Icon(
                                                    imageVector = Icons.Default.Close,
                                                    contentDescription = "مسح البحث",
                                                    tint = DeepNavy,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(20.dp),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = DeepTeal,
                                unfocusedBorderColor = Color.Transparent,
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                cursorColor = DeepTeal
                            )
                        )
                    }
                }
            }

            // =================================================================
            // 2. شريط الفلاتر الأفقية حسب التخصص (Specialty Filter Chips)
            // =================================================================
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Outlined.Tune,
                                contentDescription = null,
                                tint = DeepTeal,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "تصفية حسب التخصص المطلوب:",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = DeepNavy,
                                    fontSize = 13.5.sp
                                )
                            )
                        }

                        if (selectedRole != "الكل" && !selectedRole.equals("All", ignoreCase = true)) {
                            Text(
                                text = "إظهار الكل",
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { selectedRole = roleFilters.firstOrNull() ?: "الكل" }
                                    .padding(horizontal = 4.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = DeepTeal,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(roleFilters) { role ->
                            val isSelected = selectedRole == role
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = if (isSelected) DeepTeal else Color.White,
                                border = if (isSelected) null else BorderStroke(1.dp, SoftGray.copy(alpha = 0.8f)),
                                shadowElevation = if (isSelected) 3.dp else 1.dp,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .clickable { selectedRole = role }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                    }
                                    Text(
                                        text = role,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = if (isSelected) Color.White else DeepNavy,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                            fontSize = 13.5.sp
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // =================================================================
            // 3. شريط إحصائيات وعنوان نتائج البحث (Results Count Header)
            // =================================================================
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = if (isFilterActive) "نتائج البحث والفلترة" else "جميع المشاريع المتاحة",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = DeepNavy,
                                fontSize = 17.5.sp
                            )
                        )
                        Text(
                            text = if (isFilterActive) "الفرق التي تطابق معايير اختيارك" else "تصفح المشاريع واختر الفريق الأنسب لمهاراتك",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.Gray,
                                fontSize = 12.sp
                            )
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (filteredTeams.isNotEmpty()) LightAqua.copy(alpha = 0.6f) else SoftBeige,
                        border = BorderStroke(1.dp, DeepTeal.copy(alpha = 0.2f))
                    ) {
                        Text(
                            text = "${filteredTeams.size} فرق",
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = DeepTeal,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        )
                    }
                }
            }

            // =================================================================
            // 4. قائمة كروت النتائج أو رسالة عدم وجود نتائج (Results Feed)
            // =================================================================
            if (filteredTeams.isEmpty()) {
                item {
                    Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
                        EmptySearchResultsCard(
                            searchQuery = searchQuery,
                            selectedRole = selectedRole,
                            onResetClick = {
                                searchQuery = ""
                                selectedRole = roleFilters.firstOrNull() ?: "الكل"
                            }
                        )
                    }
                }
            } else {
                items(filteredTeams, key = { it.teamId }) { team ->
                    Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                        SearchTeamCard(
                            team = team,
                            highlightedRole = if (selectedRole != "الكل") selectedRole else null,
                            onClick = { onTeamClick(team.teamId) }
                        )
                    }
                }
            }
        }
    }
}

// =============================================================================
// مكون الهيدر العلوي المتميز (SearchTopHeader)
// =============================================================================
@Composable
private fun SearchTopHeader(
    onBackClick: () -> Unit,
    isFilterActive: Boolean,
    onResetFilters: () -> Unit
) {
    Surface(
        color = WarmCream,
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // زر الرجوع الدائري الأنيق
                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.7f)),
                    shadowElevation = 2.dp,
                    modifier = Modifier.size(42.dp)
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "رجوع",
                            tint = DeepNavy,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = "البحث واستكشاف الفرق",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = DeepNavy,
                            fontSize = 19.sp
                        )
                    )
                    Text(
                        text = "اعثر على المقعد المثالي في مشاريع تخرج جامعتك",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = DeepTeal,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp
                        )
                    )
                }
            }

            // زر إعادة ضبط الفلاتر عند تفعيلها
            if (isFilterActive) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SoftBeige,
                    border = BorderStroke(1.dp, Peach.copy(alpha = 0.6f)),
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onResetFilters() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 9.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.RestartAlt,
                            contentDescription = "إعادة ضبط",
                            tint = DeepNavy,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "إعادة ضبط",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = DeepNavy,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.5.sp
                            )
                        )
                    }
                }
            }
        }
    }
}

// =============================================================================
// كارت نتائج البحث للمشروع المتقن (SearchTeamCard)
// =============================================================================
@Composable
private fun SearchTeamCard(
    team: Team,
    highlightedRole: String?,
    onClick: () -> Unit
) {
    val isLocked = team.isLocked
    val availableSeatsCount = if (team.teamId == MockDataProvider.sampleTeam.teamId) {
        MockDataProvider.sampleSeats.count { it.status == SeatStatus.AVAILABLE }
    } else if (isLocked) {
        0
    } else {
        (team.totalSeats - 2).coerceAtLeast(1)
    }

    val bookedSeatsCount = team.totalSeats - availableSeatsCount
    val capacityFraction = if (team.totalSeats > 0) (bookedSeatsCount.toFloat() / team.totalSeats) else 0.5f

    val rolesAvailable = getRolesForTeam(team)

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 210.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.6f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(11.dp)
        ) {
            // الهيدر: اسم المادة + شارة حالة المقاعد
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SoftBeige
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.School,
                            contentDescription = null,
                            tint = DeepTeal,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = team.category,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = DeepTeal,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.5.sp
                            )
                        )
                    }
                }

                if (isLocked) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = SeatLockedBg,
                        border = BorderStroke(0.8.dp, SeatLockedGray)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "فريق مقفل",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color.Gray,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                } else {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = SeatAvailableBg
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.CheckCircle,
                                contentDescription = null,
                                tint = DeepTeal,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "$availableSeatsCount مقاعد متاحة",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = DeepTeal,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.5.sp
                                )
                            )
                        }
                    }
                }
            }

            // اسم المشروع الرئيسي
            Text(
                text = team.projectName,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = DeepNavy,
                    fontSize = 17.5.sp
                )
            )

            // وصف المشروع
            Text(
                text = team.description,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.DarkGray,
                    fontSize = 13.5.sp,
                    lineHeight = 20.sp
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // شريط تقدم اكتمال الفريق
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "الأعضاء: $bookedSeatsCount/${team.totalSeats} مقاعد",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.Gray,
                            fontSize = 11.5.sp
                        )
                    )
                    Text(
                        text = if (isLocked) "مكتمل" else "شاغر: $availableSeatsCount",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = if (isLocked) Color.Gray else DeepTeal,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.5.sp
                        )
                    )
                }
                LinearProgressIndicator(
                    progress = { capacityFraction },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = if (isLocked) Color.Gray else DeepTeal,
                    trackColor = SoftBeige
                )
            }

            // شرائح التخصصات المطلوبة مع تمييز التخصص المفلتر إن وجد
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                rolesAvailable.take(3).forEach { role ->
                    val isHighlighted = highlightedRole != null && role.contains(highlightedRole, ignoreCase = true)
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isHighlighted) Peach.copy(alpha = 0.5f) else WarmCream,
                        border = BorderStroke(
                            if (isHighlighted) 1.2.dp else 0.7.dp,
                            if (isHighlighted) Peach else SoftGray.copy(alpha = 0.8f)
                        )
                    ) {
                        Text(
                            text = role,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = DeepNavy,
                                fontSize = 11.5.sp,
                                fontWeight = if (isHighlighted) FontWeight.Bold else FontWeight.Medium
                            )
                        )
                    }
                }
                if (rolesAvailable.size > 3) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = SoftBeige
                    ) {
                        Text(
                            text = "+${rolesAvailable.size - 3}",
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = DeepTeal,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // خط فاصل ناعم
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(SoftGray.copy(alpha = 0.4f))
            )

            // الفوتر: القائد + الدكتور المشرف + زر الاستعراض
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = LeaderCrownGold.copy(alpha = 0.35f),
                        modifier = Modifier.size(22.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Outlined.WorkspacePremium,
                                contentDescription = null,
                                tint = DeepNavy,
                                modifier = Modifier.size(13.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = team.leaderName.substringBefore(" ("),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = DeepNavy,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.5.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Icon(
                        imageVector = Icons.Outlined.Groups,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "${team.totalSeats} مقاعد",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.Gray,
                            fontSize = 11.5.sp
                        )
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (isLocked) "تفاصيل الفريق" else "تصفح المقاعد",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = if (isLocked) Color.Gray else DeepTeal,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = if (isLocked) Color.Gray else DeepTeal,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}

// =============================================================================
// حالة عدم وجود نتائج للبحث (EmptySearchResultsCard)
// =============================================================================
@Composable
private fun EmptySearchResultsCard(
    searchQuery: String,
    selectedRole: String,
    onResetClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.6f)),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = SoftBeige,
                modifier = Modifier.size(68.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Outlined.SearchOff,
                        contentDescription = null,
                        tint = DeepTeal,
                        modifier = Modifier.size(34.dp)
                    )
                }
            }

            Text(
                text = "لا توجد فرق مطابقة للبحث",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = DeepNavy,
                    fontSize = 17.5.sp
                )
            )

            Text(
                text = if (searchQuery.isNotBlank() && selectedRole != "الكل") {
                    "لم نجد أي مشروع يطابق عبارة \"$searchQuery\" مع التخصص المحدد ($selectedRole)."
                } else if (searchQuery.isNotBlank()) {
                    "لم يتم العثور على أي مشروع يتضمن كلمة \"$searchQuery\"."
                } else {
                    "لا توجد فرق تطلب تخصص ($selectedRole) حالياً."
                },
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color.Gray,
                    fontSize = 13.sp,
                    lineHeight = 19.sp
                ),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Button(
                onClick = onResetClick,
                colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.RestartAlt,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(17.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "مسح معايير الفلترة والبحث",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.5.sp
                    )
                )
            }
        }
    }
}

// =============================================================================
// دوال مساعدة لربط الأدوار والتخصصات بالفرق
// =============================================================================
private fun getRolesForTeam(team: Team): List<String> {
    return if (team.teamId == MockDataProvider.sampleTeam.teamId) {
        MockDataProvider.sampleSeats.map { it.roleRequired.substringBefore(" (") }.distinct()
    } else {
        when (team.teamId) {
            "team_beta_2" -> listOf("Flutter", "UI/UX", "Backend", "AI")
            "team_gamma_3" -> listOf("Android", "Backend", "AI")
            else -> listOf("Android", "Backend", "UI/UX", "Flutter")
        }
    }
}

// =============================================================================
// Preview
// =============================================================================
@Preview(showBackground = true)
@Composable
private fun SearchAndFilterScreenPreview() {
    TeamSeatTheme {
        SearchAndFilterScreen()
    }
}
