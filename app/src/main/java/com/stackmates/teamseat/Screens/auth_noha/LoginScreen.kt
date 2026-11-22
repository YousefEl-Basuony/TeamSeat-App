package com.example.teamseat.screens.auth_noha

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// درجات الألوان الرسمية لتطبيق TeamSeat
private val DeepNavy   = Color(0xFF0A344A) // العناوين الداكنة
private val DeepTeal   = Color(0xFF1B6F7D) // الأزرار الأساسية
private val Turquoise  = Color(0xFF469D9F) // الأيقونات والعناصر النشطة
private val LightAqua  = Color(0xFFA9D5D4) // خلفية دائرة الشعار العلوية
private val WarmCream  = Color(0xFFFCF2EA) // الخلفية الرئيسية للشاشة
private val SoftBeige  = Color(0xFFF4E2DD) // خلفية الحقول
private val Peach      = Color(0xFFF4C195) // اللمسات الجمالية
private val SoftGray   = Color(0xFFD3CFD1) // الحدود

// شعار الكرسي الدائري
@Composable
fun LoginChairBadge(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(56.dp)
            .clip(CircleShape)
            .background(LightAqua.copy(alpha = 0.45f)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(30.dp)) {
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

// أيقونة الإيميل
@Composable
fun LoginEmailIcon(modifier: Modifier = Modifier) {
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

// أيقونة القفل
@Composable
fun LoginLockIcon(modifier: Modifier = Modifier) {
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

// أيقونة العين
@Composable
fun LoginEyeIcon(isVisible: Boolean, modifier: Modifier = Modifier) {
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

// أيقونة Google الملونة
@Composable
fun GoogleColoredIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(20.dp)) {
        val w = size.width
        val h = size.height
        drawCircle(
            color = Color(0xFF4285F4),
            radius = w * 0.45f,
            center = Offset(w * 0.5f, h * 0.5f),
            style = Stroke(width = 3f)
        )
        drawCircle(
            color = Color(0xFFEA4335),
            radius = w * 0.2f,
            center = Offset(w * 0.5f, h * 0.5f)
        )
    }
}

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit = {},
    onNavigateToRegister: () -> Unit = {},
    onForgotPassword: () -> Unit = {}
) {
    var emailOrId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(false) }

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
            // الهيدر واللوجو
            LoginChairBadge(modifier = Modifier.padding(top = 16.dp))

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "TeamSeat",
                color = DeepNavy,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Welcome Back!",
                color = DeepNavy,
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Sign in to continue your journey",
                color = DeepNavy.copy(alpha = 0.65f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(28.dp))

            // 1. حقل الإيميل أو الرقم الجامعي
            OutlinedTextField(
                value = emailOrId,
                onValueChange = { emailOrId = it },
                placeholder = { Text("Email or Student ID", color = DeepNavy.copy(alpha = 0.5f), fontSize = 15.sp) },
                trailingIcon = { LoginEmailIcon(modifier = Modifier.padding(end = 12.dp)) },
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

            Spacer(modifier = Modifier.height(14.dp))

            // 2. حقل الباسورد (العين على اليسار والقفل على اليمين)
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
                        LoginEyeIcon(isVisible = isPasswordVisible)
                    }
                },
                trailingIcon = { LoginLockIcon(modifier = Modifier.padding(end = 12.dp)) },
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

            Spacer(modifier = Modifier.height(12.dp))

            // سطر Remember Me و Forgot Password
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Forgot Password على اليسار
                Text(
                    text = "Forgot Password?",
                    color = DeepTeal,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { onForgotPassword() }
                )

                // Remember Me على اليمين
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { rememberMe = !rememberMe }
                ) {
                    Text(
                        text = "Remember Me",
                        color = DeepNavy.copy(alpha = 0.7f),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .border(
                                width = 1.5.dp,
                                color = if (rememberMe) DeepTeal else SoftGray,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .background(
                                color = if (rememberMe) DeepTeal else Color.Transparent,
                                shape = RoundedCornerShape(4.dp)
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // زرار تسجيل الدخول الرئيسي: -> Log In
            Button(
                onClick = onLoginSuccess,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .shadow(6.dp, RoundedCornerShape(27.dp), spotColor = DeepTeal.copy(alpha = 0.35f)),
                shape = RoundedCornerShape(27.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DeepTeal)
            ) {
                Text(
                    text = "→ Log In",
                    color = WarmCream,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            // خط OR الفاصل
            Row(
                modifier = Modifier.fillMaxWidth(0.85f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = SoftGray.copy(alpha = 0.6f))
                Text(
                    text = "OR",
                    color = DeepNavy.copy(alpha = 0.5f),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 14.dp)
                )
                HorizontalDivider(modifier = Modifier.weight(1f), color = SoftGray.copy(alpha = 0.6f))
            }

            Spacer(modifier = Modifier.height(22.dp))

            // زرار Continue with Google
            OutlinedButton(
                onClick = { /* Google Login */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(26.dp),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
                border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.7f))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Continue with Google",
                        color = DeepNavy,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    GoogleColoredIcon(modifier = Modifier.size(18.dp))
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // الفوتر: Don't have an account? Sign Up
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Don't have an account? ",
                    color = DeepNavy.copy(alpha = 0.65f),
                    fontSize = 14.sp
                )
                Text(
                    text = "Sign Up",
                    color = DeepTeal,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onNavigateToRegister() }
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}
