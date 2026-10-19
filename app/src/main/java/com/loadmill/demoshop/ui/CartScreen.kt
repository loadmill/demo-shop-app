package com.loadmill.demoshop.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.loadmill.demoshop.Cart
import com.loadmill.demoshop.CartLine
import com.loadmill.demoshop.R
import com.loadmill.demoshop.formatPrice

@Composable
fun CartScreen(
    cart: Cart,
    onBack: () -> Unit,
    onIncrease: (String) -> Unit,
    onDecrease: (String) -> Unit,
    onRemove: (String) -> Unit,
    onContinueShopping: () -> Unit,
    onCheckout: () -> Unit,
) {
    val listState = rememberLazyListState()
    val scrolled by remember { derivedStateOf { listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 0 } }
    Column(Modifier.fillMaxSize()) {
        ScreenHeader("Your cart", onBack, scrolled && !cart.isEmpty)
        if (cart.isEmpty) {
            EmptyCart(Modifier.weight(1f), onContinueShopping)
        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(start = ScreenPadding, end = ScreenPadding, top = 8.dp, bottom = 24.dp),
            ) {
                items(cart.lines, key = { it.product.id }) { line ->
                    CartLineRow(line, onIncrease, onDecrease, onRemove)
                    HorizontalDivider(color = Hairline, modifier = Modifier.padding(vertical = 18.dp))
                }
                item {
                    SoftPanel {
                        Text("Order summary", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(12.dp))
                        PriceSummary(cart)
                    }
                }
            }
        }
        BottomBar {
            TotalAndAction(cart.totalCents) { modifier ->
                PrimaryButton("Checkout", onCheckout, modifier, enabled = !cart.isEmpty)
            }
        }
    }
}

@Composable
private fun EmptyCart(modifier: Modifier, onContinueShopping: () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            Modifier
                .size(96.dp)
                .background(Tile, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            LineIcon(R.drawable.ic_bag, null, Modifier.size(40.dp))
        }
        Spacer(Modifier.height(24.dp))
        Text("Your cart is empty", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))
        Text(
            "Browse the collection and add something you love.",
            style = MaterialTheme.typography.bodyMedium,
            color = Muted,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(24.dp))
        SecondaryButton("Continue shopping", onContinueShopping)
    }
}

@Composable
private fun CartLineRow(
    line: CartLine,
    onIncrease: (String) -> Unit,
    onDecrease: (String) -> Unit,
    onRemove: (String) -> Unit,
) {
    val id = line.product.id
    Row {
        ProductImage(line.product, Modifier.size(104.dp), RoundedCornerShape(18.dp))
        Spacer(Modifier.width(16.dp))
        Column(Modifier.weight(1f).height(104.dp)) {
            Row {
                Text(line.product.name, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                Text(formatPrice(line.totalCents), style = MaterialTheme.typography.titleMedium)
            }
            Text(
                "${formatPrice(line.product.priceCents)} each",
                style = MaterialTheme.typography.bodySmall,
                color = Muted,
            )
            Spacer(Modifier.weight(1f))
            Row(verticalAlignment = Alignment.CenterVertically) {
                QuantityStepper(line.quantity, { onDecrease(id) }, { onIncrease(id) })
                Spacer(Modifier.weight(1f))
                TextButton(onClick = { onRemove(id) }, contentPadding = PaddingValues(horizontal = 8.dp)) {
                    Text(
                        "Remove",
                        style = MaterialTheme.typography.labelMedium.copy(textDecoration = TextDecoration.Underline),
                        color = Muted,
                    )
                }
            }
        }
    }
}

@Composable
private fun QuantityStepper(quantity: Int, onDecrease: () -> Unit, onIncrease: () -> Unit) {
    Surface(
        shape = CircleShape,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        color = MaterialTheme.colorScheme.surface,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onDecrease, modifier = Modifier.size(40.dp)) {
                LineIcon(R.drawable.ic_minus, "Decrease quantity", Modifier.size(18.dp))
            }
            Text(
                quantity.toString(),
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.widthIn(min = 24.dp),
            )
            IconButton(onClick = onIncrease, modifier = Modifier.size(40.dp)) {
                LineIcon(R.drawable.ic_plus, "Increase quantity", Modifier.size(18.dp))
            }
        }
    }
}
