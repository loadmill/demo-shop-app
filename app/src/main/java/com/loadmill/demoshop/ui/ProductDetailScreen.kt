package com.loadmill.demoshop.ui

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.loadmill.demoshop.Cart
import com.loadmill.demoshop.Product
import com.loadmill.demoshop.R
import com.loadmill.demoshop.formatPrice

@Composable
fun ProductDetailScreen(
    product: Product,
    cart: Cart,
    onBack: () -> Unit,
    onAdd: () -> Unit,
    onOpenCart: () -> Unit,
) {
    val quantityInCart = cart.quantityOf(product.id)
    Column(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
        ) {
            Box(Modifier.padding(start = 12.dp, end = 12.dp, top = 8.dp)) {
                ProductImage(product, Modifier.fillMaxWidth().aspectRatio(1f), RoundedCornerShape(28.dp))
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    CircleIconButton(R.drawable.ic_arrow_back, "Back", onBack)
                    Spacer(Modifier.weight(1f))
                    CartButton(cart.itemCount, onOpenCart)
                }
            }
            Column(Modifier.padding(horizontal = 24.dp, vertical = 24.dp)) {
                Text(product.category.name.uppercase(), style = MaterialTheme.typography.labelSmall, color = Muted)
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(product.name, style = MaterialTheme.typography.headlineMedium, modifier = Modifier.weight(1f))
                    Text(formatPrice(product.priceCents), style = MaterialTheme.typography.headlineSmall)
                }
                Spacer(Modifier.height(4.dp))
                Text(product.tagline, style = MaterialTheme.typography.bodyMedium, color = Muted)
                HorizontalDivider(color = Hairline, modifier = Modifier.padding(vertical = 20.dp))
                Text(product.description, style = MaterialTheme.typography.bodyLarge, color = Ink.copy(alpha = 0.82f))
                Spacer(Modifier.height(24.dp))
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Perk(R.drawable.ic_truck, "Free shipping", "Delivered in 3–5 business days")
                    Perk(R.drawable.ic_return, "Free 30-day returns", "Changed your mind? Send it back")
                }
            }
        }
        BottomBar {
            AnimatedVisibility(
                visible = quantityInCart > 0,
                enter = fadeIn(tween(200)) + expandVertically(tween(200)),
            ) {
                Row(Modifier.padding(bottom = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                    LineIcon(R.drawable.ic_check, null, Modifier.size(18.dp), tint = Success)
                    Spacer(Modifier.width(6.dp))
                    Text(
                        "$quantityInCart in your cart",
                        style = MaterialTheme.typography.labelMedium,
                        color = Success,
                    )
                }
            }
            PrimaryButton("Add to cart", onAdd, Modifier.fillMaxWidth())
        }
    }
}

@Composable
private fun Perk(@DrawableRes icon: Int, title: String, subtitle: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier
                .size(44.dp)
                .background(Tile, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            LineIcon(icon, null, Modifier.size(22.dp))
        }
        Spacer(Modifier.width(14.dp))
        Column {
            Text(title, style = MaterialTheme.typography.titleSmall)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = Muted)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddedToCartSheet(
    product: Product,
    onViewCart: () -> Unit,
    onContinueShopping: () -> Unit,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        containerColor = Color.White,
        contentColor = Ink,
        tonalElevation = 0.dp,
        scrimColor = Ink.copy(alpha = 0.4f),
        dragHandle = { BottomSheetDefaults.DragHandle(color = Hairline) },
    ) {
        Column(Modifier.padding(start = ScreenPadding, end = ScreenPadding, bottom = 16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .size(28.dp)
                        .background(Success, CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    LineIcon(R.drawable.ic_check, null, Modifier.size(16.dp), tint = Color.White)
                }
                Spacer(Modifier.width(10.dp))
                Text("Added to cart", style = MaterialTheme.typography.titleLarge)
            }
            Spacer(Modifier.height(20.dp))
            SoftPanel(padding = 14.dp) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ProductImage(product, Modifier.size(64.dp), RoundedCornerShape(16.dp))
                    Spacer(Modifier.width(14.dp))
                    Column(Modifier.weight(1f)) {
                        Text(product.name, style = MaterialTheme.typography.titleMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text(
                            product.tagline,
                            style = MaterialTheme.typography.bodySmall,
                            color = Muted,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Text(formatPrice(product.priceCents), style = MaterialTheme.typography.titleMedium)
                }
            }
            Spacer(Modifier.height(24.dp))
            PrimaryButton("View cart", onViewCart, Modifier.fillMaxWidth())
            Spacer(Modifier.height(12.dp))
            SecondaryButton("Continue shopping", onContinueShopping, Modifier.fillMaxWidth().height(56.dp))
        }
    }
}
