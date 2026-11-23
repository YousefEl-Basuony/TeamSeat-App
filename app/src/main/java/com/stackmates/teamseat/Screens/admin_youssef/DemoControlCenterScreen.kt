package com.stackmates.teamseat.Screens.admin_youssef

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stackmates.teamseat.core.data.MockDataProvider
import com.stackmates.teamseat.core.model.DemoPersona
import com.stackmates.teamseat.core.model.User
import com.stackmates.teamseat.core.theme.*

/**
 * 💻 المسؤول عن الشاشة: يوسف (Youssef - Team Lead & Backend)
 * مركز التحكم في العرض التوضيحي السريع للمناقشة (Live Demo Control Center)
 * مصممة بنفس الهيكل والتصميم الفاخر لشاشة LockTeamDialog و EditTeamSeatsScreen:
 * خلفية WarmCream، كروت بيضاء بزوايا 24dp، إحصائيات 2x2، تبديل فوري للشخصيات، ومحاكاة السيناريوهات أمام اللجنة.
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
    var selectedPersona by remember { mutableStateOf(currentPersona) }
    var showResetSuccess by remember { mutableStateOf(false) }
    var showSwitchSuccess by remember { mutableStateOf(false) }
    var isResetting by remember { mutableStateOf(false) }

    val isLeaderActive = selectedPersona == DemoPersona.AHMED_LEADER
    val activeUser = if (isLeaderActive) MockDataProvider.ahmedLeader else MockDataProvider.saraStudent

    Scaffold(
        containerColor = WarmCream,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "مركز تحكم المناقشة (Live Demo)",
                            fontFamily = CairoFontFamily,
                            fontWeight = FontWeight.ExtraBold,
                            color = DeepNavy,
                            fontSize = 20.sp,
                            maxLines = 1
                        )
                        Text(
                            text = "التبديل اللحظي وإدارة سيناريوهات العرض",
                            fontFamily = CairoFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = DeepTeal,
                            fontSize = 13.5.sp,
                            maxLines = 1
                        )
                    }
                },
                navigationIcon = {
                    Surface(
                        modifier = Modifier
                            .padding(start = 12.dp, end = 8.dp)
                            .size(44.dp),
                        shape = CircleShape,
                        color = Color.White,
                        shadowElevation = 2.dp,
                        border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.6f))
                    ) {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "رجوع",
                                tint = DeepNavy,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = WarmCream)
            )
        },
        bottomBar = {
            Surface(
                color = Color.White,
                shadowElevation = 16.dp,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // 1. زر التبديل اللحظي بين الحسابين (Primary Action)
                    Button(
                        onClick = {
                            selectedPersona = if (isLeaderActive) DemoPersona.SARA_STUDENT else DemoPersona.AHMED_LEADER
                            showSwitchSuccess = true
                            showResetSuccess = false
                            onTogglePersonaClick()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                        shape = RoundedCornerShape(28.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.SwapHoriz,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                            Text(
                                text = if (isLeaderActive) "تبديل إلى سارة علي (طالبة باحثة عن مقعد)" else "تبديل إلى أحمد محمود (قائد التيم)",
                                fontFamily = CairoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = Color.White,
                                maxLines = 1
                            )
                        }
                    }

                    // 2. زر إعادة ضبط وتصفير البيانات التجريبية (Secondary Action)
                    OutlinedButton(
                        onClick = {
                            isResetting = true
                            onResetMockDataClick()
                            showResetSuccess = true
                            showSwitchSuccess = false
                            isResetting = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(24.dp),
                        border = BorderStroke(1.dp, DeepNavy.copy(alpha = 0.35f)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = DeepNavy)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.RestartAlt,
                                contentDescription = null,
                                tint = DeepNavy,
                                modifier = Modifier.size(19.dp)
                            )
                            Text(
                                text = "إعادة ضبط وتصفير بيانات الكراسي للتجربة",
                                fontFamily = CairoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = DeepNavy,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            // إشعار نجاح تبديل الحساب
            AnimatedVisibility(
                visible = showSwitchSuccess,
                enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically() + fadeOut()
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = LightAqua.copy(alpha = 0.45f),
                    border = BorderStroke(1.dp, Turquoise.copy(alpha = 0.7f))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = null,
                            tint = DeepTeal,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "تم التبديل بنجاح إلى: ${activeUser.name}",
                            fontFamily = CairoFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = DeepNavy,
                            fontSize = 13.5.sp
                        )
                    }
                }
            }

            // إشعار نجاح تصفير البيانات
            AnimatedVisibility(
                visible = showResetSuccess,
                enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically() + fadeOut()
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = SoftBeige,
                    border = BorderStroke(1.dp, Peach.copy(alpha = 0.8f))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = null,
                            tint = DeepNavy,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "تمت إعادة ضبط جميع المقاعد والطلبات للحالة الأولى بنجاح ✓",
                            fontFamily = CairoFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = DeepNavy,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(top = 8.dp, bottom = 28.dp)
            ) {

                // =============================================================
                // 1. كارت ملخص وضع المناقشة (Hero Metric Dashboard 2x2 Grid)
                // =============================================================
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.5f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                        ) {
                            // الصف العلوي مع مؤشر الحالة والوضع الحي
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = SoftBeige,
                                        modifier = Modifier.padding(bottom = 6.dp)
                                    ) {
                                        Text(
                                            text = "جلسة التقييم والمناقشة 2026",
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                            fontFamily = CairoFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            color = DeepTeal,
                                            fontSize = 12.sp
                                        )
                                    }

                                    Text(
                                        text = "غرفة تحكم العرض الحي (Demo Cockpit)",
                                        fontFamily = CairoFontFamily,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = DeepNavy,
                                        fontSize = 18.5.sp,
                                        maxLines = 1
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "الحساب النشط حالياً: ${activeUser.name}",
                                        fontFamily = CairoFontFamily,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.Gray,
                                        fontSize = 13.5.sp
                                    )
                                }

                                // مؤشر الحالة الدائري الشيك
                                Surface(
                                    modifier = Modifier.size(60.dp),
                                    shape = CircleShape,
                                    color = if (isLeaderActive) DeepNavy else DeepTeal,
                                    shadowElevation = 2.dp
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Icon(
                                                imageVector = if (isLeaderActive) Icons.Outlined.WorkspacePremium else Icons.Outlined.School,
                                                contentDescription = null,
                                                tint = Peach,
                                                modifier = Modifier.size(24.dp)
                                            )
                                            Text(
                                                text = if (isLeaderActive) "قائد" else "طالبة",
                                                fontFamily = CairoFontFamily,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White,
                                                fontSize = 10.sp
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // شبكة إحصائيات المناقشة 2x2 بنظام LockMetricCard المطابق لـ LockTeamDialog
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    DemoControlMetricCard(
                                        modifier = Modifier.weight(1f),
                                        title = "الشخصية النشطة",
                                        value = if (isLeaderActive) "أحمد (قائد التيم)" else "سارة (طالبة)",
                                        icon = Icons.Outlined.Person,
                                        iconTint = DeepNavy,
                                        iconBg = SoftBeige
                                    )
                                    DemoControlMetricCard(
                                        modifier = Modifier.weight(1f),
                                        title = "صلاحيات الحساب",
                                        value = if (isLeaderActive) "إدارة، قفل، وتصدير" else "استعراض وطلب حجز",
                                        icon = Icons.Outlined.AdminPanelSettings,
                                        iconTint = DeepTeal,
                                        iconBg = LightAqua.copy(alpha = 0.45f)
                                    )
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    DemoControlMetricCard(
                                        modifier = Modifier.weight(1f),
                                        title = "حالة البيانات",
                                        value = "6 مقاعد جاهزة",
                                        icon = Icons.Outlined.CheckCircle,
                                        iconTint = Turquoise,
                                        iconBg = LightAqua.copy(alpha = 0.45f)
                                    )
                                    DemoControlMetricCard(
                                        modifier = Modifier.weight(1f),
                                        title = "وضع المحاكاة",
                                        value = "مزامنة لحظية ⚡",
                                        icon = Icons.Outlined.Speed,
                                        iconTint = DeepNavy,
                                        iconBg = Peach.copy(alpha = 0.35f)
                                    )
                                }
                            }
                        }
                    }
                }

                // =============================================================
                // 2. كروت شخصيات العرض واختيار الدور (Persona Selection Cards)
                // =============================================================
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.5f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "شخصيات العرض التوضيحي (Personas)",
                                    fontFamily = CairoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepNavy,
                                    fontSize = 16.sp
                                )
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = SoftBeige
                                ) {
                                    Text(
                                        text = "انقر للتبديل الفوري",
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                        fontFamily = CairoFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        color = DeepTeal,
                                        fontSize = 11.5.sp
                                    )
                                }
                            }

                            HorizontalDivider(color = SoftGray.copy(alpha = 0.4f))

                            // بطاقة شخصية 1: أحمد محمود (قائد التيم)
                            PersonaOptionCard(
                                name = MockDataProvider.ahmedLeader.name,
                                role = "Android Lead • قائد التيم والمشروع",
                                permissions = "إدارة مقاعد التيم، قبول المتقدمين، قفل التيم وتصدير كشف الـ A4 للمعيدة",
                                icon = Icons.Outlined.WorkspacePremium,
                                isSelected = isLeaderActive,
                                onClick = {
                                    if (!isLeaderActive) {
                                        selectedPersona = DemoPersona.AHMED_LEADER
                                        showSwitchSuccess = true
                                        showResetSuccess = false
                                        onTogglePersonaClick()
                                    }
                                }
                            )

                            // بطاقة شخصية 2: سارة علي (طالبة باحثة عن تيم)
                            PersonaOptionCard(
                                name = MockDataProvider.saraStudent.name,
                                role = "UI/UX Designer • تبحث عن مقعد متاح",
                                permissions = "استعراض تيمات الدفعة، تصفح خريطة المقاعد الشاغرة، وتقديم طلب انضمام",
                                icon = Icons.Outlined.School,
                                isSelected = !isLeaderActive,
                                onClick = {
                                    if (isLeaderActive) {
                                        selectedPersona = DemoPersona.SARA_STUDENT
                                        showSwitchSuccess = true
                                        showResetSuccess = false
                                        onTogglePersonaClick()
                                    }
                                }
                            )
                        }
                    }
                }

                // =============================================================
                // 3. كارت سيناريوهات المناقشة الحية (Live Demo Scenarios)
                // =============================================================
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.5f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "سيناريوهات العرض أمام د. محمد كاظم وم. شهد أحمد:",
                                fontFamily = CairoFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = DeepNavy,
                                fontSize = 15.sp
                            )

                            HorizontalDivider(color = SoftGray.copy(alpha = 0.4f))

                            DemoScenarioRow(
                                stepNumber = "1",
                                title = "سيناريو حجز مقعد جديد (منظور الطالب)",
                                description = "التبديل إلى سارة -> فتح خريطة المقاعد -> اختيار المقعد الشاغر -> تأكيد الحجز ومتابعة عداد الـ 48 ساعة."
                            )

                            DemoScenarioRow(
                                stepNumber = "2",
                                title = "سيناريو قبول واعتماد الطلب (منظور القائد)",
                                description = "التبديل إلى أحمد -> استعراض إشعار طلب سارة -> مراجعة ملفها وقبولها -> ينضم الطالب فوراً للمقعد."
                            )

                            DemoScenarioRow(
                                stepNumber = "3",
                                title = "سيناريو قفل التيم وتصدير الكشف (التسليم النهائي)",
                                description = "التبديل إلى أحمد -> مراجعة كشف التسليم الرسمي -> اعتماد وإغلاق التيم -> تصدير كشف A4 PDF للمعيدة."
                            )
                        }
                    }
                }

                // =============================================================
                // 4. صندوق نصائح وإرشادات المناقشة (Advisory Box)
                // =============================================================
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = SoftBeige),
                        border = BorderStroke(1.dp, Peach.copy(alpha = 0.6f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = Peach
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Info,
                                        contentDescription = null,
                                        tint = DeepNavy,
                                        modifier = Modifier
                                            .padding(6.dp)
                                            .size(16.dp)
                                    )
                                }
                                Text(
                                    text = "نقاط القوة التي يجب إبرازها أثناء المناقشة الأكاديمية:",
                                    fontFamily = CairoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepNavy,
                                    fontSize = 14.5.sp
                                )
                            }

                            DemoBulletPoint("حل فوضى تشكيل الفرق: يمنع التطبيق ازدواجية التسجيل والانسحاب العشوائي بفضل نظام قفل التيم.")
                            DemoBulletPoint("التوثيق الأكاديمي الشفاف: كشف تسليم رسمي بنظام A4 PDF باسم المشرفين والمعيدة وتوقيعات الطلاب.")
                            DemoBulletPoint("مرونة الأدوار الفنية: كل مقعد مخصص لدور برمجي محدد (Android, Backend, UI/UX, QA) لتوازن المشروع.")
                        }
                    }
                }
            }
        }
    }
}

/**
 * 🎴 بطاقة خيار الشخصية (Persona Option Card)
 */
@Composable
private fun PersonaOptionCard(
    name: String,
    role: String,
    permissions: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) Peach.copy(alpha = 0.22f) else SoftBeige.copy(alpha = 0.35f),
        border = BorderStroke(
            width = if (isSelected) 1.5.dp else 1.dp,
            color = if (isSelected) DeepTeal else SoftGray.copy(alpha = 0.6f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                modifier = Modifier.size(44.dp),
                shape = CircleShape,
                color = if (isSelected) DeepTeal else DeepNavy
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (isSelected) Peach else Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = name,
                        fontFamily = CairoFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = DeepNavy,
                        fontSize = 15.sp
                    )

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = if (isSelected) DeepTeal else SoftGray.copy(alpha = 0.4f)
                    ) {
                        Text(
                            text = if (isSelected) "النشط حالياً ✓" else "انقر للتبديل",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            fontFamily = CairoFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else DeepNavy.copy(alpha = 0.7f),
                            fontSize = 10.5.sp
                        )
                    }
                }

                Text(
                    text = role,
                    fontFamily = CairoFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = DeepTeal,
                    fontSize = 12.5.sp
                )

                Text(
                    text = permissions,
                    fontFamily = CairoFontFamily,
                    color = DeepNavy.copy(alpha = 0.7f),
                    fontSize = 11.5.sp,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

/**
 * 📊 بطاقة إحصائية داخلية لغرفة التحكم (Demo Metric Card)
 */
@Composable
private fun DemoControlMetricCard(
    title: String,
    value: String,
    icon: ImageVector,
    iconTint: Color,
    iconBg: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(78.dp),
        shape = RoundedCornerShape(14.dp),
        color = WarmCream.copy(alpha = 0.6f),
        border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = iconBg,
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(19.dp)
                    )
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontFamily = CairoFontFamily,
                    fontWeight = FontWeight.Medium,
                    color = DeepNavy.copy(alpha = 0.65f),
                    fontSize = 11.5.sp,
                    maxLines = 1
                )
                Text(
                    text = value,
                    fontFamily = CairoFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = DeepNavy,
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

/**
 * 🎬 صف سيناريو المناقشة
 */
@Composable
private fun DemoScenarioRow(
    stepNumber: String,
    title: String,
    description: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = WarmCream.copy(alpha = 0.5f),
        border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.45f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Surface(
                modifier = Modifier.size(28.dp),
                shape = CircleShape,
                color = DeepTeal
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = stepNumber,
                        fontFamily = CairoFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 13.sp
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = title,
                    fontFamily = CairoFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = DeepNavy,
                    fontSize = 13.5.sp
                )
                Text(
                    text = description,
                    fontFamily = CairoFontFamily,
                    fontWeight = FontWeight.Normal,
                    color = DeepNavy.copy(alpha = 0.75f),
                    fontSize = 12.sp,
                    lineHeight = 17.sp
                )
            }
        }
    }
}

/**
 * 💡 نقطة توجيهية داخل كارت النصائح
 */
@Composable
private fun DemoBulletPoint(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "•",
            fontFamily = CairoFontFamily,
            fontWeight = FontWeight.Bold,
            color = DeepTeal,
            fontSize = 16.sp
        )
        Text(
            text = text,
            fontFamily = CairoFontFamily,
            color = DeepNavy.copy(alpha = 0.85f),
            fontSize = 13.sp,
            lineHeight = 19.sp
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DemoControlCenterScreenExecutivePreview() {
    TeamSeatTheme {
        DemoControlCenterScreen()
    }
}
