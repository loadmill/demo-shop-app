package com.loadmill.demoshop.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.loadmill.demoshop.Order
import com.loadmill.demoshop.R

@Composable
fun ConfirmationScreen(order: Order, onStartOver: () -> Unit) {
    val reveal = remember { Animatable(0f) }
    LaunchedEffect(Unit) { reveal.animateTo(1f, tween(300, easing = FastOutSlowInEasing)) }

    val scrollState = rememberScrollState()
    Column(Modifier.fillMaxSize()) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = ScreenPadding),
            contentAlignment = Alignment.CenterStart,
        ) {
            Wordmark()
        }
        ScrollEdge(scrollState.value > 0)
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(start = ScreenPadding, end = ScreenPadding, top = 24.dp, bottom = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                Modifier
                    .size(104.dp)
                    .graphicsLayer {
                        val scale = 0.7f + 0.3f * reveal.value
                        scaleX = scale
                        scaleY = scale
                        alpha = reveal.value
                    }
                    .background(Success.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    Modifier
                        .size(68.dp)
                        .background(Success, CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    LineIcon(R.drawable.ic_check, null, Modifier.size(34.dp), tint = Color.White)
                }
            }
            Spacer(Modifier.height(24.dp))
            Text("Order confirmed", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(8.dp))
            Text(
                "Thanks, ${order.customerName.ifBlank { "friend" }}! Your order is on its way.",
                style = MaterialTheme.typography.bodyLarge,
                color = Muted,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(20.dp))
            Row(
                Modifier
                    .background(Tile, CircleShape)
                    .padding(horizontal = 16.dp, vertical = 9.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Order number", style = MaterialTheme.typography.bodySmall, color = Muted)
                Spacer(Modifier.width(8.dp))
                Text(order.number, style = MaterialTheme.typography.titleSmall)
            }
            Spacer(Modifier.height(32.dp))
            SoftPanel {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier
                            .size(44.dp)
                            .background(Color.White, CircleShape),
                        contentAlignment = Alignment.Center,
                    ) {
                        LineIcon(R.drawable.ic_truck, null, Modifier.size(22.dp))
                    }
                    Spacer(Modifier.width(14.dp))
                    Column {
                        Text("Arriving in 3–5 business days", style = MaterialTheme.typography.titleSmall)
                        Text("Free standard delivery", style = MaterialTheme.typography.bodySmall, color = Muted)
                    }
                }
                HorizontalDivider(color = Hairline, modifier = Modifier.padding(vertical = 18.dp))
                OrderLines(order.cart)
                Spacer(Modifier.height(18.dp))
                PriceSummary(order.cart)
            }
        }
        BottomBar {
            PrimaryButton("Start over", onStartOver, Modifier.fillMaxWidth())
        }
    }
}
