package com.stackmates.teamseat.Screens.auth_noha

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Devices
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stackmates.teamseat.core.theme.*

/**
 * 🌸 شاشة إنشاء الحساب (Register Screen)
 * مسؤولية: نُهى (Noha) — تصميم عصري وجذاب متوافق تماماً مع الهوية البصرية لتطبيق TeamSeat.
 * مزودة بقسم اختيار الدور والتخصص التقني (Designer, Developer, Frontend, QA) بتصميم شبكي (2x2) فائق الأناقة والوضوح.
 */
@Composable
fun RegisterScreen(
    onRegisterClick: (name: String, studentId: String, email: String, pass: String) -> Unit = { _, _, _, _ -> },
    onRegisterWithRoleClick: (name: String, studentId: String, email: String, pass: String, role: String) -> Unit = { _, _, _, _, _ -> },
    onRegisterSuccess: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {},
    onBackToLogin: () -> Unit = onNavigateToLogin
) {
    var fullName by remember { mutableStateOf("") }
    var studentId by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    // التخصص / الدور التقني في الفريق (Designer, Developer, Frontend, QA)
    var selectedRole by remember { mutableStateOf("Developer") }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(WarmCream)
        ) {
            // خلفية التموجات الانسيابية المتطابقة مع شاشة تسجيل الدخول
            RegisterFluidBackground()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 22.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
            Spacer(modifier = Modifier.height(8.dp))

            // شعار الكرسي الأيقوني لـ TeamSeat
            RegisterHeroChairLogo(modifier = Modifier.size(64.dp))

            Spacer(modifier = Modifier.height(6.dp))

            // اسم التطبيق
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Team",
                    fontFamily = TajawalFontFamily,
                    color = DeepNavy,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-0.5).sp
                )
                Text(
                    text = "Seat",
                    fontFamily = TajawalFontFamily,
                    color = DeepTeal,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-0.5).sp
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // عنوان الصفحة
            Text(
                text = "Create Account",
                fontFamily = TajawalFontFamily,
                color = DeepNavy,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "Join TeamSeat and collaborate with your team",
                fontFamily = TajawalFontFamily,
                color = DeepNavy.copy(alpha = 0.65f),
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(18.dp))

            // بطاقة الحقول واختيار التخصص لتجميع العناصر ومنع الإحساس بالفراغ
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.94f)),
                border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.45f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 18.dp),
                    verticalArrangement = Arrangement.spacedBy(13.dp)
                ) {
                    // 1. الاسم بالكامل
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
                                color = DeepNavy.copy(alpha = 0.45f),
                                fontSize = 14.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Person,
                                contentDescription = null,
                                tint = Turquoise,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = SoftBeige.copy(alpha = 0.35f),
                            unfocusedContainerColor = SoftBeige.copy(alpha = 0.2f),
                            focusedBorderColor = DeepTeal,
                            unfocusedBorderColor = SoftGray.copy(alpha = 0.5f),
                            focusedTextColor = DeepNavy,
                            unfocusedTextColor = DeepNavy
                        ),
                        singleLine = true
                    )

                    // 2. الرقم الجامعي (Student ID)
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
                                color = DeepNavy.copy(alpha = 0.45f),
                                fontSize = 14.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Badge,
                                contentDescription = null,
                                tint = Turquoise,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = SoftBeige.copy(alpha = 0.35f),
                            unfocusedContainerColor = SoftBeige.copy(alpha = 0.2f),
                            focusedBorderColor = DeepTeal,
                            unfocusedBorderColor = SoftGray.copy(alpha = 0.5f),
                            focusedTextColor = DeepNavy,
                            unfocusedTextColor = DeepNavy
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    // 3. الإيميل الأكاديمي
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        textStyle = LocalTextStyle.current.copy(
                            fontFamily = TajawalFontFamily,
                            fontSize = 14.sp
                        ),
                        placeholder = {
                            Text(
                                text = "Academic Email",
                                fontFamily = TajawalFontFamily,
                                color = DeepNavy.copy(alpha = 0.45f),
                                fontSize = 14.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Email,
                                contentDescription = null,
                                tint = Turquoise,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = SoftBeige.copy(alpha = 0.35f),
                            unfocusedContainerColor = SoftBeige.copy(alpha = 0.2f),
                            focusedBorderColor = DeepTeal,
                            unfocusedBorderColor = SoftGray.copy(alpha = 0.5f),
                            focusedTextColor = DeepNavy,
                            unfocusedTextColor = DeepNavy
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )

                    // 4. كلمة المرور
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        textStyle = LocalTextStyle.current.copy(
                            fontFamily = TajawalFontFamily,
                            fontSize = 14.sp
                        ),
                        placeholder = {
                            Text(
                                text = "Password",
                                fontFamily = TajawalFontFamily,
                                color = DeepNavy.copy(alpha = 0.45f),
                                fontSize = 14.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Lock,
                                contentDescription = null,
                                tint = Turquoise,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Icon(
                                    imageVector = if (isPasswordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                                    contentDescription = "تبديل الرؤية",
                                    tint = if (isPasswordVisible) DeepTeal else SoftGray,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        },
                        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = SoftBeige.copy(alpha = 0.35f),
                            unfocusedContainerColor = SoftBeige.copy(alpha = 0.2f),
                            focusedBorderColor = DeepTeal,
                            unfocusedBorderColor = SoftGray.copy(alpha = 0.5f),
                            focusedTextColor = DeepNavy,
                            unfocusedTextColor = DeepNavy
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                    )

                    // =========================================================
                    // 5. قسم اختيار التخصص / الدور التقني (Role / Track Selection)
                    // =========================================================
                    Column(
                        modifier = Modifier.padding(top = 2.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Select Your Role",
                                fontFamily = TajawalFontFamily,
                                color = DeepNavy,
                                fontSize = 14.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "تخصصك في الفريق",
                                fontFamily = TajawalFontFamily,
                                color = DeepTeal,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        // شبكة 2x2 للكروت الأنيقة للتخصصات الأربعة
                        // الصف الأول: Designer & Developer
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            RoleOptionCard(
                                title = "Designer",
                                subtitle = "UI/UX & Design",
                                icon = Icons.Outlined.Palette,
                                isSelected = selectedRole == "Designer",
                                onClick = { selectedRole = "Designer" },
                                modifier = Modifier.weight(1f)
                            )
                            RoleOptionCard(
                                title = "Developer",
                                subtitle = "Mobile & Backend",
                                icon = Icons.Outlined.Code,
                                isSelected = selectedRole == "Developer",
                                onClick = { selectedRole = "Developer" },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        // الصف الثاني: Frontend & QA
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            RoleOptionCard(
                                title = "Frontend",
                                subtitle = "Web & Client",
                                icon = Icons.Outlined.Devices,
                                isSelected = selectedRole == "Frontend",
                                onClick = { selectedRole = "Frontend" },
                                modifier = Modifier.weight(1f)
                            )
                            RoleOptionCard(
                                title = "QA",
                                subtitle = "Testing & Review",
                                icon = Icons.Outlined.BugReport,
                                isSelected = selectedRole == "QA",
                                onClick = { selectedRole = "QA" },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // زرار إنشاء الحساب: -> Register
                    Button(
                        onClick = {
                            onRegisterClick(fullName, studentId, email, password)
                            onRegisterWithRoleClick(fullName, studentId, email, password, selectedRole)
                            onRegisterSuccess()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .shadow(
                                elevation = 4.dp,
                                shape = RoundedCornerShape(26.dp),
                                spotColor = DeepTeal.copy(alpha = 0.35f)
                            ),
                        shape = RoundedCornerShape(26.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DeepNavy)
                    ) {
                        Text(
                            text = "Register",
                            fontFamily = TajawalFontFamily,
                            color = Color.White,
                            fontSize = 16.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // الفوتر: Already have an account? Sign In
            Row(
                modifier = Modifier.padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Already have an account? ",
                    fontFamily = TajawalFontFamily,
                    color = DeepNavy.copy(alpha = 0.7f),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Sign In",
                    fontFamily = TajawalFontFamily,
                    color = DeepTeal,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.clickable {
                        onNavigateToLogin()
                        onBackToLogin()
                    }
                )
            }
        }
        }
    }
}

/**
 * 🎴 كارت اختيار التخصص الأنيق (Designer, Developer, Frontend, QA)
 */
@Composable
private fun RoleOptionCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(64.dp),
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) DeepNavy else Color.White,
        border = BorderStroke(
            width = if (isSelected) 1.5.dp else 1.dp,
            color = if (isSelected) Turquoise else SoftGray.copy(alpha = 0.5f)
        ),
        shadowElevation = if (isSelected) 3.dp else 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = if (isSelected) Peach else LightAqua.copy(alpha = 0.45f),
                modifier = Modifier.size(34.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = DeepNavy,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    fontFamily = TajawalFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.5.sp,
                    color = if (isSelected) Color.White else DeepNavy,
                    maxLines = 1
                )
                Text(
                    text = subtitle,
                    fontFamily = TajawalFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 10.sp,
                    color = if (isSelected) WarmCream.copy(alpha = 0.85f) else DeepNavy.copy(alpha = 0.6f),
                    maxLines = 1
                )
            }

            if (isSelected) {
                Icon(
                    imageVector = Icons.Outlined.CheckCircle,
                    contentDescription = null,
                    tint = Peach,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

/**
 * 💺 شعار كرسي TeamSeat
 */
@Composable
fun RegisterHeroChairLogo(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(LightAqua.copy(alpha = 0.45f)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(36.dp)) {
            val w = size.width
            val h = size.height

            // الحلقة البيضاوية التركوازية
            drawArc(
                color = Turquoise,
                startAngle = 135f,
                sweepAngle = 270f,
                useCenter = false,
                topLeft = Offset(w * 0.05f, h * 0.28f),
                size = Size(w * 0.90f, h * 0.44f),
                style = Stroke(width = 3f, cap = StrokeCap.Round)
            )

            // مسند الظهر
            drawRoundRect(
                color = DeepNavy,
                topLeft = Offset(w * 0.24f, h * 0.12f),
                size = Size(w * 0.52f, h * 0.40f),
                cornerRadius = CornerRadius(7f, 7f),
                style = Stroke(width = 3f)
            )

            // قاعدة المقعد
            drawRoundRect(
                color = DeepNavy,
                topLeft = Offset(w * 0.12f, h * 0.52f),
                size = Size(w * 0.76f, h * 0.16f),
                cornerRadius = CornerRadius(4f, 4f),
                style = Stroke(width = 3f)
            )

            // أرجل الكرسي
            drawLine(
                color = DeepNavy,
                start = Offset(w * 0.26f, h * 0.68f),
                end = Offset(w * 0.18f, h * 0.90f),
                strokeWidth = 3f,
                cap = StrokeCap.Round
            )
            drawLine(
                color = DeepNavy,
                start = Offset(w * 0.74f, h * 0.68f),
                end = Offset(w * 0.82f, h * 0.90f),
                strokeWidth = 3f,
                cap = StrokeCap.Round
            )
        }
    }
}

/**
 * 🌊 تموجات الخلفية الانسيابية لتصميم التسجيل
 */
@Composable
fun RegisterFluidBackground() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // تموج علوي ناعم في الزاوية اليسرى
        val topPath = Path().apply {
            moveTo(0f, 0f)
            lineTo(w * 0.35f, 0f)
            cubicTo(
                w * 0.25f, h * 0.08f,
                w * 0.12f, h * 0.06f,
                0f, h * 0.14f
            )
            close()
        }
        drawPath(topPath, color = Turquoise.copy(alpha = 0.22f))

        // تموج سفلي ناعم في الزاوية اليمنى
        val bottomPath = Path().apply {
            moveTo(w, h * 0.86f)
            cubicTo(
                w * 0.85f, h * 0.92f,
                w * 0.72f, h * 0.88f,
                w * 0.60f, h
            )
            lineTo(w, h)
            close()
        }
        drawPath(bottomPath, color = Peach.copy(alpha = 0.25f))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegisterScreenOfficialPreview() {
    TeamSeatTheme {
        RegisterScreen()
    }
}
