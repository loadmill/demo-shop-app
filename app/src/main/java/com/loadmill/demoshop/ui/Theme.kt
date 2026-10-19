package com.loadmill.demoshop.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val colors = lightColorScheme(
    primary = Color(0xFF1E5EFF),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDCE6FF),
    onPrimaryContainer = Color(0xFF001A55),
    background = Color(0xFFF7F8FA),
    surface = Color.White,
    error = Color(0xFFD32F2F),
)

@Composable
fun DemoShopTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = colors, content = content)
}
