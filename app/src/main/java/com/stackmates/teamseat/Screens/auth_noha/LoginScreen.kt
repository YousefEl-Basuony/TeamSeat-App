package com.stackmates.teamseat.Screens.auth_noha

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
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
import com.stackmates.teamseat.core.theme.*

/**
 * 🌸 شاشة تسجيل الدخول (Login Screen)
 * مسؤولية: نُهى (Noha) — تصميم متطابق بالملي مع الموك آب المعتمد
 * مستخدم فيه نفس خط تجوال (Tajawal) المعتمد في شاشات يوسف، مع المسافات المتناسقة وشعار Google الرسمي.
 */
@Composable
fun LoginScreen(
    onLoginClick: (email: String, pass: String) -> Unit = { _, _ -> },
    onLoginSuccess: () -> Unit = {},
    onNavigateToRegister: () -> Unit = {},
    onForgotPassword: () -> Unit = {}
) {
    var emailOrId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmCream)
    ) {
        // 1. خلفية التموجات الانسيابية الهادئة في الزوايا لمنع الفراغ
        LoginFluidBackground()

        // 2. المحتوى الرئيسي المتناسق مع الشاشة
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // شعار الكرسي الأيقوني لـ TeamSeat مع الحلقة التركوازية
            LoginHeroChairLogo(modifier = Modifier.size(76.dp))

            Spacer(modifier = Modifier.height(10.dp))

            // اسم التطبيق
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Team",
                    fontFamily = TajawalFontFamily,
                    color = DeepNavy,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-0.5).sp
                )
                Text(
                    text = "Seat",
                    fontFamily = TajawalFontFamily,
                    color = DeepTeal,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-0.5).sp
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // عنوان الترحيب
            Text(
                text = "Welcome Back!",
                fontFamily = TajawalFontFamily,
                color = DeepNavy,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "Sign in to continue",
                fontFamily = TajawalFontFamily,
                color = DeepNavy.copy(alpha = 0.65f),
                fontSize = 14.5.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(26.dp))

            // بطاقة الحقول (Form Card) لتجميع العناصر ومنع الإحساس بالفراغ
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.92f)),
                border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.45f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // حقل الإيميل أو الرقم الجامعي
                    OutlinedTextField(
                        value = emailOrId,
                        onValueChange = { emailOrId = it },
                        placeholder = {
                            Text(
                                text = "Email or Student ID",
                                fontFamily = TajawalFontFamily,
                                color = DeepNavy.copy(alpha = 0.45f),
                                fontSize = 14.5.sp
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
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = SoftBeige.copy(alpha = 0.4f),
                            unfocusedContainerColor = SoftBeige.copy(alpha = 0.25f),
                            focusedBorderColor = DeepTeal,
                            unfocusedBorderColor = SoftGray.copy(alpha = 0.5f),
                            focusedTextColor = DeepNavy,
                            unfocusedTextColor = DeepNavy
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )

                    // حقل كلمة المرور
                    Column {
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            placeholder = {
                                Text(
                                    text = "Password",
                                    fontFamily = TajawalFontFamily,
                                    color = DeepNavy.copy(alpha = 0.45f),
                                    fontSize = 14.5.sp
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
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = SoftBeige.copy(alpha = 0.4f),
                                unfocusedContainerColor = SoftBeige.copy(alpha = 0.25f),
                                focusedBorderColor = DeepTeal,
                                unfocusedBorderColor = SoftGray.copy(alpha = 0.5f),
                                focusedTextColor = DeepNavy,
                                unfocusedTextColor = DeepNavy
                            ),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                        )

                        // زر Forgot Password على اليمين
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = "Forgot Password?",
                                fontFamily = TajawalFontFamily,
                                color = DeepTeal,
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.clickable { onForgotPassword() }
                            )
                        }
                    }

                    // زر تسجيل الدخول الرئيسي
                    Button(
                        onClick = {
                            onLoginClick(emailOrId, password)
                            onLoginSuccess()
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
                            text = "Log In",
                            fontFamily = TajawalFontFamily,
                            color = Color.White,
                            fontSize = 16.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // خط OR الفاصل
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            color = SoftGray.copy(alpha = 0.6f)
                        )
                        Text(
                            text = "or",
                            fontFamily = TajawalFontFamily,
                            color = DeepNavy.copy(alpha = 0.5f),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )
                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            color = SoftGray.copy(alpha = 0.6f)
                        )
                    }

                    // زر تسجيل الدخول بواسطة Google مع الأيقونة الملونة الرسمية
                    OutlinedButton(
                        onClick = { /* Google Login */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(25.dp),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, SoftGray.copy(alpha = 0.8f))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            GoogleOfficialLogo(modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Continue with Google",
                                fontFamily = TajawalFontFamily,
                                color = DeepNavy,
                                fontSize = 14.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // الفوتر: Don't have an account? Sign Up
            Row(
                modifier = Modifier.padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Don't have an account? ",
                    fontFamily = TajawalFontFamily,
                    color = DeepNavy.copy(alpha = 0.7f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Sign Up",
                    fontFamily = TajawalFontFamily,
                    color = DeepTeal,
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.clickable { onNavigateToRegister() }
                )
            }
        }
    }
}

/**
 * 🌐 شعار Google الرسمي بالألوان الأربعة الدقيقة
 */
@Composable
fun GoogleOfficialLogo(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(20.dp)) {
        val w = size.width
        val h = size.height
        val stroke = w * 0.22f
        val r = (w - stroke) / 2f
        val cx = w / 2f
        val cy = h / 2f

        // 1. القوس الأحمر العلوي
        drawArc(
            color = Color(0xFFEA4335),
            startAngle = 180f,
            sweepAngle = 100f,
            useCenter = false,
            topLeft = Offset(stroke / 2f, stroke / 2f),
            size = Size(r * 2, r * 2),
            style = Stroke(width = stroke, cap = StrokeCap.Butt)
        )

        // 2. القوس الأصفر الأيسر
        drawArc(
            color = Color(0xFFFBBC05),
            startAngle = 125f,
            sweepAngle = 60f,
            useCenter = false,
            topLeft = Offset(stroke / 2f, stroke / 2f),
            size = Size(r * 2, r * 2),
            style = Stroke(width = stroke, cap = StrokeCap.Butt)
        )

        // 3. القوس الأخضر السفلي
        drawArc(
            color = Color(0xFF34A853),
            startAngle = 20f,
            sweepAngle = 110f,
            useCenter = false,
            topLeft = Offset(stroke / 2f, stroke / 2f),
            size = Size(r * 2, r * 2),
            style = Stroke(width = stroke, cap = StrokeCap.Butt)
        )

        // 4. القوس والخط الأزرق الأيمن
        drawArc(
            color = Color(0xFF4285F4),
            startAngle = -35f,
            sweepAngle = 60f,
            useCenter = false,
            topLeft = Offset(stroke / 2f, stroke / 2f),
            size = Size(r * 2, r * 2),
            style = Stroke(width = stroke, cap = StrokeCap.Butt)
        )

        // الخط الأزرق الأفقي الأوسط لرمز حرف G
        drawLine(
            color = Color(0xFF4285F4),
            start = Offset(cx - w * 0.05f, cy),
            end = Offset(cx + r + stroke / 2f, cy),
            strokeWidth = stroke,
            cap = StrokeCap.Square
        )
    }
}

/**
 * 💺 شعار كرسي TeamSeat مع الحلقة التركوازية المدارية
 */
@Composable
fun LoginHeroChairLogo(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(LightAqua.copy(alpha = 0.45f)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(44.dp)) {
            val w = size.width
            val h = size.height

            // الحلقة البيضاوية التركوازية التي تدور حول الكرسي
            drawArc(
                color = Turquoise,
                startAngle = 135f,
                sweepAngle = 270f,
                useCenter = false,
                topLeft = Offset(w * 0.05f, h * 0.28f),
                size = Size(w * 0.90f, h * 0.44f),
                style = Stroke(width = 3.5f, cap = StrokeCap.Round)
            )

            // مسند الظهر المريح للكرسي
            drawRoundRect(
                color = DeepNavy,
                topLeft = Offset(w * 0.24f, h * 0.12f),
                size = Size(w * 0.52f, h * 0.40f),
                cornerRadius = CornerRadius(8f, 8f),
                style = Stroke(width = 3.5f)
            )

            // قاعدة مقعد الكرسي
            drawRoundRect(
                color = DeepNavy,
                topLeft = Offset(w * 0.12f, h * 0.52f),
                size = Size(w * 0.76f, h * 0.16f),
                cornerRadius = CornerRadius(5f, 5f),
                style = Stroke(width = 3.5f)
            )

            // أرجل الكرسي الأنيقة
            drawLine(
                color = DeepNavy,
                start = Offset(w * 0.26f, h * 0.68f),
                end = Offset(w * 0.18f, h * 0.90f),
                strokeWidth = 3.5f,
                cap = StrokeCap.Round
            )
            drawLine(
                color = DeepNavy,
                start = Offset(w * 0.74f, h * 0.68f),
                end = Offset(w * 0.82f, h * 0.90f),
                strokeWidth = 3.5f,
                cap = StrokeCap.Round
            )
        }
    }
}

/**
 * 🌊 تموجات الخلفية الانسيابية لتصميم تسجيل الدخول
 */
@Composable
fun LoginFluidBackground() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // تموج علوي ناعم في الزاوية اليمنى
        val topPath = Path().apply {
            moveTo(w * 0.65f, 0f)
            cubicTo(
                w * 0.75f, h * 0.08f,
                w * 0.88f, h * 0.05f,
                w, h * 0.14f
            )
            lineTo(w, 0f)
            close()
        }
        drawPath(topPath, color = Turquoise.copy(alpha = 0.22f))

        // تموج سفلي ناعم في الزاوية اليسرى
        val bottomPath = Path().apply {
            moveTo(0f, h * 0.86f)
            cubicTo(
                w * 0.15f, h * 0.92f,
                w * 0.28f, h * 0.88f,
                w * 0.40f, h
            )
            lineTo(0f, h)
            close()
        }
        drawPath(bottomPath, color = Peach.copy(alpha = 0.25f))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenOfficialPreview() {
    TeamSeatTheme {
        LoginScreen()
    }
}
