package com.fiveBoys.rustore.ui

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fiveBoys.rustore.R
import kotlin.random.Random

@Composable
fun OnboardingScreen(
    onContinue: () -> Unit,
    onShown: () -> Unit = {},
    markShown: () -> Unit
) {
    LaunchedEffect(Unit) {
        markShown()
        onShown()
    }

    // Анимации для плавающих иконок
    val infiniteTransition = rememberInfiniteTransition()

    val floatAnimation1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 4000
                0.0f at 0
                0.5f at 2000
                1.0f at 4000
            },
            repeatMode = RepeatMode.Reverse
        )
    )

    val floatAnimation2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 3500
                0.0f at 0
                0.7f at 1500
                1.0f at 3500
            },
            repeatMode = RepeatMode.Reverse
        )
    )

    val floatAnimation3 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 4500
                0.0f at 0
                0.3f at 2000
                1.0f at 4500
            },
            repeatMode = RepeatMode.Reverse
        )
    )

    // Анимация появления контента
    val contentAlpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 1000, delayMillis = 500)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1A73E8),
                        Color(0xFF4285F4),
                        Color(0xFF669DF6)
                    )
                )
            )
    ) {
        // Фоновые декоративные элементы
        Box(modifier = Modifier.fillMaxSize()) {
            // Полупрозрачные иконки приложений на фоне
            FloatingAppIcon(
                iconRes = R.mipmap.ic_ozon, // Замените на вашу иконку Ozon
                x = 0.15f,
                y = 0.2f,
                floatAnimation = floatAnimation1,
                size = 100.dp
            )

            FloatingAppIcon(
                iconRes = R.mipmap.ic_wildberries, // Замените на вашу иконку Wildberries
                x = 0.95f,
                y = 0.3f,
                floatAnimation = floatAnimation2,
                size = 80.dp
            )

            FloatingAppIcon(
                iconRes = R.mipmap.ic_telegram, // Замените на вашу иконку VK
                x = 0.35f,
                y = 0.85f,
                floatAnimation = floatAnimation3,
                size = 100.dp
            )

            FloatingAppIcon(
                iconRes = R.mipmap.ic_vk, // Замените на вашу иконку Telegram
                x = 0.45f,
                y = 0.35f,
                floatAnimation = floatAnimation1,
                size = 300.dp
            )

            FloatingAppIcon(
                iconRes = R.mipmap.ic_yandex, // Замените на вашу иконку Yandex
                x = 0.9f,
                y = 0.8f,
                floatAnimation = floatAnimation2,
                size = 75.dp
            )

            FloatingAppIcon(
                iconRes = R.mipmap.ic_sber, // Замените на вашу иконку Sber
                x = 0.1f,
                y = 0.5f,
                floatAnimation = floatAnimation3,
                size = 95.dp
            )

            // Декоративные круги
            AnimatedCircle(
                x = 0.15f,
                y = 0.1f,
                floatAnimation = floatAnimation1,
                size = 120.dp
            )

            AnimatedCircle(
                x = 0.85f,
                y = 0.15f,
                floatAnimation = floatAnimation2,
                size = 80.dp
            )

            AnimatedCircle(
                x = 0.7f,
                y = 0.9f,
                floatAnimation = floatAnimation3,
                size = 150.dp
            )
        }

        // Основной контент
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
                .alpha(contentAlpha),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Верхняя часть с логотипом
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(Modifier.height(60.dp))

                // Анимированная карточка с логотипом
                AnimatedLogoCard()

                Spacer(Modifier.height(32.dp))

                Text(
                    "RuStore",
                    fontSize = 46.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 0.5.sp
                )

                Spacer(Modifier.height(12.dp))
            }

            // Центральная часть с текстом
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Официальный магазин приложений для Android",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    lineHeight = 32.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(Modifier.height(24.dp))

                Text(
                    "Любимые приложения уже здесь",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }

            // Нижняя часть с кнопкой
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                AnimatedContinueButton(onContinue = onContinue)

                Spacer(Modifier.height(32.dp))

                Text(
                    "Безопасно • Быстро • Бесплатно",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun FloatingAppIcon(
    iconRes: Int,
    x: Float,
    y: Float,
    floatAnimation: Float,
    size: androidx.compose.ui.unit.Dp
) {
    val offsetY = floatAnimation * 20

    Box(
        modifier = Modifier
            .offset(
                x = (x * 360 - 40).dp,
                y = (y * 800 - 40 + offsetY).dp
            )
            .size(size)
            .alpha(0.75f)
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
private fun AnimatedCircle(
    x: Float,
    y: Float,
    floatAnimation: Float,
    size: androidx.compose.ui.unit.Dp
) {
    val offsetY = floatAnimation * 15

    Box(
        modifier = Modifier
            .offset(
                x = (x * 400 - 60).dp,
                y = (y * 800 - 60 + offsetY).dp
            )
            .size(size)
            .background(
                color = Color.White.copy(alpha = 0.08f),
                shape = RoundedCornerShape(100.dp)
            )
    )
}

@Composable
private fun AnimatedLogoCard() {
    val rotation by animateFloatAsState(
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 3000
                0f at 0
                5f at 1500
                0f at 3000
            },
            repeatMode = RepeatMode.Reverse
        )
    )

    val scale by animateFloatAsState(
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 2000
                1.0f at 0
                1.02f at 1000
                1.0f at 2000
            },
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .size(120.dp)
            .shadow(
                elevation = 24.dp,
                shape = RoundedCornerShape(24.dp),
                clip = true
            )
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
            .padding(16.dp)
            .graphicsLayer {
                rotationZ = rotation
                scaleX = scale
                scaleY = scale
            },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.mipmap.ic_rustore),
            contentDescription = null,
            modifier = Modifier.size(100.dp),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
private fun AnimatedAppBadge(name: String, color: Color, index: Int) {
    val offsetY by animateFloatAsState(
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 2000
                0f at 0
                -5f at 1000
                0f at 2000
            },
            repeatMode = RepeatMode.Reverse
        )
    )

    val badgeAlpha by animateFloatAsState( // Переименовал переменную
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 2000
                0.7f at 0
                1f at 1000
                0.7f at 2000
            },
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .graphicsLayer {
                translationY = offsetY
                alpha = badgeAlpha // Используем переименованную переменную
            }
            .clip(RoundedCornerShape(12.dp))
            .background(color)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = name,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
private fun AnimatedContinueButton(onContinue: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition()

    // Усиленная анимация elevation
    val elevationAnim by infiniteTransition.animateFloat(
        initialValue = 8f,
        targetValue = 20f, // Увеличили максимальную тень
        animationSpec = infiniteRepeatable(
            animation = tween(800), // Ускорили анимацию
            repeatMode = RepeatMode.Reverse
        )
    )

    // Более выраженная анимация масштаба
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.08f, // Увеличили масштаб
        animationSpec = infiniteRepeatable(
            animation = tween(1200),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Добавим анимацию цвета фона
    val backgroundColor by infiniteTransition.animateColor(
        initialValue = Color.White,
        targetValue = Color(0xFFE8F0FE), // Легкий голубой оттенок
        animationSpec = infiniteRepeatable(
            animation = tween(1500),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Анимация градиентной обводки
    val borderWidth by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        )
    )

    Button(
        onClick = onContinue,
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp) // Немного увеличили высоту
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                shadowElevation = elevationAnim
                shape = RoundedCornerShape(16.dp)
            },
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor, // Анимируемый цвет фона
            contentColor = Color(0xFF1A73E8)
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = elevationAnim.dp,
            pressedElevation = 12.dp
        ),
        border = ButtonDefaults.outlinedButtonBorder.copy(
            width = borderWidth.dp,
            brush = Brush.horizontalGradient(
                colors = listOf(
                    Color(0xFF4285F4),
                    Color(0xFF34A853),
                    Color(0xFFFBBC05),
                    Color(0xFFEA4335)
                )
            )
        )
    ) {
        Text(
            "Продолжить",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold, // Сделали текст жирнее
            letterSpacing = 1.sp, // Увеличили межбуквенное расстояние
//            modifier = Modifier.shadow(2.dp) // Тень у текста
        )
    }
}