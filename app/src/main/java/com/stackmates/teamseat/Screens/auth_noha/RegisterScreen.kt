package com.example.teamseat.screens.auth_noha

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// درجات الألوان الرسمية لتطبيق TeamSeat
private val DeepNavy   = Color(0xFF0A344A) // العناوين الداكنة
private val DeepTeal   = Color(0xFF1B6F7D) // الأزرار والـ Role المحددة
private val Turquoise  = Color(0xFF469D9F) // الأيقونات والعناصر النشطة
private val LightAqua  = Color(0xFFA9D5D4) // خلفية دائرة الشعار العلوية
private val WarmCream  = Color(0xFFFCF2EA) // الخلفية الرئيسية للشاشة
private val SoftBeige  = Color(0xFFF4E2DD) // خلفية الحقول
private val Peach      = Color(0xFFF4C195) // اللمسات الجمالية
private val SoftGray   = Color(0xFFD3CFD1) // الحدود

// --- أيقونات مرسومة بدقة بالكود لمنع أي أخطاء استدعاء ---

@Composable
fun TopChairBadge(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(54.dp)
            .clip(CircleShape)
            .background(LightAqua.copy(alpha = 0.45f)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(28.dp)) {
            val w = size.width
            val h = size.height

            // مسند الكرسي
            drawRoundRect(
                color = DeepNavy,
                topLeft = Offset(w * 0.25f, h * 0.15f),
                size = Size(w * 0.50f, h * 0.40f),
                cornerRadius = CornerRadius(6f, 6f),
                style = Stroke(width = 2.5f)
            )
            // مقعد الكرسي
            drawRoundRect(
                color = DeepNavy,
                topLeft = Offset(w * 0.12f, h * 0.52f),
                size = Size(w * 0.76f, h * 0.16f),
                cornerRadius = CornerRadius(4f, 4f),
                style = Stroke(width = 2.5f)
            )
            // الأرجل
            drawLine(color = DeepNavy, start = Offset(w * 0.28f, h * 0.68f), end = Offset(w * 0.20f, h * 0.88f), strokeWidth = 2.5f, cap = StrokeCap.Round)
            drawLine(color = DeepNavy, start = Offset(w * 0.72f, h * 0.68f), end = Offset(w * 0.80f, h * 0.88f), strokeWidth = 2.5f, cap = StrokeCap.Round)
        }
    }
}

// أيقونة المستخدم (User)
@Composable
fun UserFieldIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(20.dp)) {
        val w = size.width
        val h = size.height
        drawCircle(color = Turquoise, radius = 5f, center = Offset(w * 0.5f, h * 0.32f), style = Stroke(width = 2f))
        drawArc(
            color = Turquoise,
            startAngle = 190f,
            sweepAngle = 160f,
            useCenter = false,
            topLeft = Offset(w * 0.15f, h * 0.52f),
            size = Size(w * 0.70f, h * 0.45f),
            style = Stroke(width = 2f, cap = StrokeCap.Round)
        )
    }
}

// أيقونة الكارنيه الجامعي (ID Card)
@Composable
fun IdCardFieldIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(20.dp)) {
        val w = size.width
        val h = size.height
        drawRoundRect(
            color = Turquoise,
            topLeft = Offset(w * 0.18f, h * 0.12f),
            size = Size(w * 0.64f, h * 0.76f),
            cornerRadius = CornerRadius(4f, 4f),
            style = Stroke(width = 2f)
        )
        drawCircle(color = Turquoise, radius = 3f, center = Offset(w * 0.5f, h * 0.40f))
        drawLine(color = Turquoise, start = Offset(w * 0.32f, h * 0.65f), end = Offset(w * 0.68f, h * 0.65f), strokeWidth = 2f, cap = StrokeCap.Round)
    }
}

// أيقونة الإيميل (Email Envelope)
@Composable
fun EmailFieldIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(20.dp)) {
        val w = size.width
        val h = size.height
        drawRoundRect(
            color = Turquoise,
            topLeft = Offset(w * 0.12f, h * 0.20f),
            size = Size(w * 0.76f, h * 0.60f),
            cornerRadius = CornerRadius(4f, 4f),
            style = Stroke(width = 2f)
        )
        drawLine(color = Turquoise, start = Offset(w * 0.15f, h * 0.24f), end = Offset(w * 0.5f, h * 0.54f), strokeWidth = 2f, cap = StrokeCap.Round)
        drawLine(color = Turquoise, start = Offset(w * 0.85f, h * 0.24f), end = Offset(w * 0.5f, h * 0.54f), strokeWidth = 2f, cap = StrokeCap.Round)
    }
}

// أيقونة القفل (Lock)
@Composable
fun LockFieldIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(20.dp)) {
        val w = size.width
        val h = size.height
        drawArc(
            color = Turquoise,
            startAngle = 180f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = Offset(w * 0.25f, h * 0.12f),
            size = Size(w * 0.50f, h * 0.45f),
            style = Stroke(width = 2f, cap = StrokeCap.Round)
        )
        drawRoundRect(
            color = Turquoise,
            topLeft = Offset(w * 0.18f, h * 0.42f),
            size = Size(w * 0.64f, h * 0.48f),
            cornerRadius = CornerRadius(4f, 4f),
            style = Stroke(width = 2f)
        )
    }
}

// أيقونة العين (Eye)
@Composable
fun EyeFieldIcon(isVisible: Boolean, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(20.dp)) {
        val w = size.width
        val h = size.height
        drawArc(
            color = if (isVisible) DeepTeal else SoftGray,
            startAngle = 30f,
            sweepAngle = 120f,
            useCenter = false,
            topLeft = Offset(w * 0.1f, h * 0.1f),
            size = Size(w * 0.8f, h * 0.6f),
            style = Stroke(width = 2f, cap = StrokeCap.Round)
        )
        drawArc(
            color = if (isVisible) DeepTeal else SoftGray,
            startAngle = 210f,
            sweepAngle = 120f,
            useCenter = false,
            topLeft = Offset(w * 0.1f, h * 0.3f),
            size = Size(w * 0.8f, h * 0.6f),
            style = Stroke(width = 2f, cap = StrokeCap.Round)
        )
        drawCircle(color = if (isVisible) DeepTeal else SoftGray, radius = 3.5f, center = Offset(w * 0.5f, h * 0.5f))
        if (!isVisible) {
            drawLine(color = SoftGray, start = Offset(w * 0.2f, h * 0.2f), end = Offset(w * 0.8f, h * 0.8f), strokeWidth = 2f, cap = StrokeCap.Round)
        }
    }
}

// كارت الدور / التخصص في الشبكة 2x2
@Composable
fun RoleCardItem(
    title: String,
    iconSymbol: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(72.dp)
            .shadow(if (isSelected) 4.dp else 1.dp, RoundedCornerShape(16.dp), spotColor = DeepTeal.copy(alpha = 0.2f))
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) DeepTeal else SoftGray.copy(alpha = 0.6f),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = iconSymbol,
                color = if (isSelected) DeepTeal else Turquoise,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                color = if (isSelected) DeepNavy else DeepNavy.copy(alpha = 0.8f),
                fontSize = 14.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {}
) {
    var fullName by remember { mutableStateOf("") }
    var studentId by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    // الدور المحدد
    var selectedRole by remember { mutableStateOf("Developer") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmCream)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // الهيدر واللوجو بالأعلى
            TopChairBadge(modifier = Modifier.padding(top = 10.dp))

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "TeamSeat",
                color = DeepNavy,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Create Account",
                color = DeepNavy,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Join TeamSeat and start your journey",
                color = DeepNavy.copy(alpha = 0.65f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 1. حقل الاسم (Full Name) مع الأيقونة على اليمين
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                placeholder = { Text("Full Name", color = DeepNavy.copy(alpha = 0.5f), fontSize = 15.sp) },
                trailingIcon = { UserFieldIcon(modifier = Modifier.padding(end = 12.dp)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = DeepTeal,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = DeepNavy,
                    unfocusedTextColor = DeepNavy
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 2. حقل الرقم الجامعي (Student ID)
            OutlinedTextField(
                value = studentId,
                onValueChange = { studentId = it },
                placeholder = { Text("Student ID", color = DeepNavy.copy(alpha = 0.5f), fontSize = 15.sp) },
                trailingIcon = { IdCardFieldIcon(modifier = Modifier.padding(end = 12.dp)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = DeepTeal,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = DeepNavy,
                    unfocusedTextColor = DeepNavy
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 3. حقل الإيميل (Academic Email)
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Academic Email", color = DeepNavy.copy(alpha = 0.5f), fontSize = 15.sp) },
                trailingIcon = { EmailFieldIcon(modifier = Modifier.padding(end = 12.dp)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = DeepTeal,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = DeepNavy,
                    unfocusedTextColor = DeepNavy
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 4. حقل كلمة المرور (Password): العين على اليسار والقفل على اليمين زي الصورة بالظبط!
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Password", color = DeepNavy.copy(alpha = 0.5f), fontSize = 15.sp) },
                leadingIcon = {
                    Box(
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .clickable { isPasswordVisible = !isPasswordVisible }
                    ) {
                        EyeFieldIcon(isVisible = isPasswordVisible)
                    }
                },
                trailingIcon = { LockFieldIcon(modifier = Modifier.padding(end = 12.dp)) },
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = DeepTeal,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = DeepNavy,
                    unfocusedTextColor = DeepNavy
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(20.dp))

            // قسم اختيار التخصص: Select Your Role
            Text(
                text = "Select Your Role",
                color = DeepNavy,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                textAlign = TextAlign.End
            )

            Spacer(modifier = Modifier.height(8.dp))

            // شبكة 2x2 للكروت المتطابقة مع الصورة
            // الصف الأول: Designer و Developer
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                RoleCardItem(
                    title = "Designer",
                    iconSymbol = "🎨",
                    isSelected = selectedRole == "Designer",
                    onClick = { selectedRole = "Designer" },
                    modifier = Modifier.weight(1f)
                )
                RoleCardItem(
                    title = "Developer",
                    iconSymbol = "</>",
                    isSelected = selectedRole == "Developer",
                    onClick = { selectedRole = "Developer" },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // الصف الثاني: QA و Frontend
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                RoleCardItem(
                    title = "QA",
                    iconSymbol = "🐞",
                    isSelected = selectedRole == "QA",
                    onClick = { selectedRole = "QA" },
                    modifier = Modifier.weight(1f)
                )
                RoleCardItem(
                    title = "Frontend",
                    iconSymbol = "🖥️",
                    isSelected = selectedRole == "Frontend",
                    onClick = { selectedRole = "Frontend" },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // زرار إنشاء الحساب: -> Create Account
            Button(
                onClick = onRegisterSuccess,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .shadow(6.dp, RoundedCornerShape(27.dp), spotColor = DeepTeal.copy(alpha = 0.35f)),
                shape = RoundedCornerShape(27.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DeepTeal)
            ) {
                Text(
                    text = "→ Create Account",
                    color = WarmCream,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // الفوتر: Already have an account? Sign In
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Already have an account? ",
                    color = DeepNavy.copy(alpha = 0.65f),
                    fontSize = 14.sp
                )
                Text(
                    text = "Sign In",
                    color = DeepTeal,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onNavigateToLogin() }
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen()
}
