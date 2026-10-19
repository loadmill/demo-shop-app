package com.loadmill.demoshop.ui

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.loadmill.demoshop.R

val Ink = Color(0xFF151515)
val Muted = Color(0xFF6E6C68)
val Hairline = Color(0xFFECEAE6)
val Tile = Color(0xFFF4F3F1)
val Panel = Color(0xFFF8F7F5)
val Clay = Color(0xFFE07A4E)
val Success = Color(0xFF1E7F4F)

val Jakarta = FontFamily(
    Font(R.font.plus_jakarta_sans_regular, FontWeight.Normal),
    Font(R.font.plus_jakarta_sans_medium, FontWeight.Medium),
    Font(R.font.plus_jakarta_sans_semibold, FontWeight.SemiBold),
    Font(R.font.plus_jakarta_sans_bold, FontWeight.Bold),
    Font(R.font.plus_jakarta_sans_extrabold, FontWeight.ExtraBold),
)

private fun style(size: Int, lineHeight: Int, weight: FontWeight, tracking: Double = 0.0) = TextStyle(
    fontFamily = Jakarta,
    fontWeight = weight,
    fontSize = size.sp,
    lineHeight = lineHeight.sp,
    letterSpacing = tracking.sp,
)

private val typography = Typography(
    displaySmall = style(32, 38, FontWeight.Bold, -0.9),
    headlineMedium = style(28, 34, FontWeight.Bold, -0.7),
    headlineSmall = style(22, 28, FontWeight.Bold, -0.4),
    titleLarge = style(18, 24, FontWeight.SemiBold, -0.2),
    titleMedium = style(16, 22, FontWeight.SemiBold, -0.1),
    titleSmall = style(14, 20, FontWeight.SemiBold),
    bodyLarge = style(16, 24, FontWeight.Normal),
    bodyMedium = style(14, 21, FontWeight.Normal),
    bodySmall = style(13, 18, FontWeight.Normal),
    labelLarge = style(15, 20, FontWeight.SemiBold),
    labelMedium = style(13, 18, FontWeight.SemiBold),
    labelSmall = style(11, 16, FontWeight.Bold, 1.2),
)

private val colors = lightColorScheme(
    primary = Ink,
    onPrimary = Color.White,
    primaryContainer = Tile,
    onPrimaryContainer = Ink,
    secondary = Ink,
    onSecondary = Color.White,
    background = Color.White,
    onBackground = Ink,
    surface = Color.White,
    onSurface = Ink,
    surfaceVariant = Tile,
    onSurfaceVariant = Muted,
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Panel,
    surfaceContainer = Panel,
    surfaceContainerHigh = Tile,
    surfaceContainerHighest = Tile,
    outline = Color(0xFFD9D6D1),
    outlineVariant = Hairline,
    error = Color(0xFFC62828),
)

private val shapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(32.dp),
)

@Composable
fun DemoShopTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = colors, typography = typography, shapes = shapes, content = content)
}
