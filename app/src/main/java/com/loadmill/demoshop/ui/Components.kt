package com.loadmill.demoshop.ui

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.loadmill.demoshop.Cart
import com.loadmill.demoshop.Product
import com.loadmill.demoshop.R
import com.loadmill.demoshop.formatPrice

val ScreenPadding = 20.dp

@Composable
fun Wordmark(modifier: Modifier = Modifier) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        Image(painterResource(R.drawable.ic_logo_mark), contentDescription = null, modifier = Modifier.size(34.dp))
        Spacer(Modifier.width(10.dp))
        Column {
            Text(
                "LOADMILL",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, lineHeight = 12.sp, letterSpacing = 1.6.sp),
                color = Muted,
                maxLines = 1,
            )
            Text(
                "Demo Shop",
                fontFamily = Jakarta,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 21.sp,
                lineHeight = 24.sp,
                letterSpacing = (-0.6).sp,
                color = Ink,
                maxLines = 1,
            )
        }
    }
}

@Composable
fun LineIcon(@DrawableRes icon: Int, contentDescription: String?, modifier: Modifier = Modifier, tint: Color = Ink) {
    Icon(painterResource(icon), contentDescription = contentDescription, tint = tint, modifier = modifier)
}

@Composable
fun CartButton(count: Int, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = Ink,
        contentColor = Color.White,
        modifier = modifier.height(44.dp),
    ) {
        Row(
            Modifier.padding(start = 14.dp, end = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            LineIcon(R.drawable.ic_bag, null, Modifier.size(20.dp), tint = Color.White)
            Spacer(Modifier.width(8.dp))
            Text("Cart", style = MaterialTheme.typography.labelLarge)
            Spacer(Modifier.width(10.dp))
            Box(
                Modifier
                    .size(32.dp)
                    .background(Color.White, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                AnimatedContent(
                    targetState = count,
                    transitionSpec = {
                        (scaleIn(tween(200), initialScale = 0.6f) + fadeIn(tween(200))) togetherWith fadeOut(tween(100))
                    },
                    label = "cartCount",
                ) { value ->
                    Text(value.toString(), style = MaterialTheme.typography.labelLarge, color = Ink)
                }
            }
        }
    }
}

@Composable
fun CircleIconButton(
    @DrawableRes icon: Int,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    container: Color = Color.White,
) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = container,
        border = BorderStroke(1.dp, Hairline),
        modifier = modifier.size(44.dp),
    ) {
        Box(contentAlignment = Alignment.Center) {
            LineIcon(icon, contentDescription, Modifier.size(22.dp))
        }
    }
}

@Composable
fun ScreenHeader(title: String, onBack: (() -> Unit)?, scrolled: Boolean = false) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(horizontal = ScreenPadding),
        contentAlignment = Alignment.Center,
    ) {
        if (onBack != null) {
            CircleIconButton(R.drawable.ic_arrow_back, "Back", onBack, Modifier.align(Alignment.CenterStart))
        }
        Text(title, style = MaterialTheme.typography.titleLarge)
    }
    ScrollEdge(scrolled)
}

@Composable
fun ScrollEdge(visible: Boolean) {
    HorizontalDivider(color = if (visible) Hairline else Color.Transparent)
}

@Composable
fun PrimaryButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier, enabled: Boolean = true) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = Ink,
            contentColor = Color.White,
            disabledContainerColor = Hairline,
            disabledContentColor = Muted,
        ),
        contentPadding = PaddingValues(horizontal = 24.dp),
        modifier = modifier.height(56.dp),
    ) {
        Text(text, style = MaterialTheme.typography.labelLarge.copy(fontSize = 16.sp))
    }
}

@Composable
fun SecondaryButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    OutlinedButton(
        onClick = onClick,
        shape = CircleShape,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Ink),
        contentPadding = PaddingValues(horizontal = 24.dp),
        modifier = modifier.height(52.dp),
    ) {
        Text(text, style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
fun ProductImage(product: Product, modifier: Modifier = Modifier, shape: Shape = RoundedCornerShape(20.dp)) {
    Image(
        painter = painterResource(product.image),
        contentDescription = product.name,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .clip(shape)
            .background(Tile),
    )
}

@Composable
fun BottomBar(content: @Composable ColumnScope.() -> Unit) {
    Column(Modifier.fillMaxWidth().background(Color.White)) {
        HorizontalDivider(color = Hairline)
        Column(Modifier.padding(horizontal = ScreenPadding, vertical = 14.dp), content = content)
    }
}

@Composable
fun TotalAndAction(totalCents: Int, action: @Composable (Modifier) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.padding(end = 20.dp)) {
            Text("Total", style = MaterialTheme.typography.bodySmall, color = Muted)
            Text(formatPrice(totalCents), style = MaterialTheme.typography.headlineSmall)
        }
        action(Modifier.weight(1f))
    }
}

@Composable
fun SoftPanel(modifier: Modifier = Modifier, padding: Dp = 20.dp, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Panel)
            .padding(padding),
        content = content,
    )
}

@Composable
fun SummaryRow(label: String, value: String, valueColor: Color = Ink) {
    Row(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = Muted, modifier = Modifier.weight(1f))
        Text(value, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold), color = valueColor)
    }
}

@Composable
fun PriceSummary(cart: Cart) {
    val items = if (cart.itemCount == 1) "1 item" else "${cart.itemCount} items"
    SummaryRow("Subtotal ($items)", formatPrice(cart.subtotalCents))
    SummaryRow("Shipping", if (cart.shippingCents == 0) "Free" else formatPrice(cart.shippingCents), Success)
    HorizontalDivider(color = Hairline, modifier = Modifier.padding(vertical = 12.dp))
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text("Total", style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
        Text(formatPrice(cart.totalCents), style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
    }
}

@Composable
fun OrderLines(cart: Cart) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        cart.lines.forEach { line ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                ProductImage(line.product, Modifier.size(56.dp), RoundedCornerShape(14.dp))
                Spacer(Modifier.width(14.dp))
                Column(Modifier.weight(1f)) {
                    Text(line.product.name, style = MaterialTheme.typography.titleSmall)
                    Text("Qty ${line.quantity}", style = MaterialTheme.typography.bodySmall, color = Muted)
                }
                Text(formatPrice(line.totalCents), style = MaterialTheme.typography.titleSmall)
            }
        }
    }
}

@Composable
fun SectionTitle(text: String, modifier: Modifier = Modifier) {
    Text(text, style = MaterialTheme.typography.titleLarge, modifier = modifier)
}
