package com.loadmill.demoshop.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.loadmill.demoshop.Cart
import com.loadmill.demoshop.Category
import com.loadmill.demoshop.Product
import com.loadmill.demoshop.R
import com.loadmill.demoshop.formatPrice
import com.loadmill.demoshop.productsIn

@Composable
fun CatalogScreen(
    cart: Cart,
    category: Category?,
    onCategoryChange: (Category?) -> Unit,
    onOpenProduct: (Product) -> Unit,
    onAdd: (Product) -> Unit,
    onOpenCart: () -> Unit,
) {
    val gridState = rememberLazyGridState()
    val scrolled by remember { derivedStateOf { gridState.firstVisibleItemIndex > 0 || gridState.firstVisibleItemScrollOffset > 0 } }
    Column(Modifier.fillMaxSize()) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = ScreenPadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Wordmark(Modifier.weight(1f))
            CartButton(cart.itemCount, onOpenCart)
        }
        ScrollEdge(scrolled)
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            state = gridState,
            contentPadding = PaddingValues(start = ScreenPadding, end = ScreenPadding, top = 8.dp, bottom = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalArrangement = Arrangement.spacedBy(28.dp),
            modifier = Modifier.fillMaxSize(),
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                    Column {
                        Text("NEW SEASON", style = MaterialTheme.typography.labelSmall, color = Muted)
                        Spacer(Modifier.height(6.dp))
                        Text("Everyday essentials,\nmade to last.", style = MaterialTheme.typography.headlineMedium)
                    }
                    PerksBanner()
                    CategoryChips(category, onCategoryChange)
                }
            }
            items(productsIn(category), key = { it.id }) { product ->
                ProductCard(
                    product = product,
                    quantityInCart = cart.quantityOf(product.id),
                    onOpen = { onOpenProduct(product) },
                    onAdd = { onAdd(product) },
                )
            }
        }
    }
}

@Composable
private fun PerksBanner() {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Ink)
            .padding(horizontal = 18.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f)) {
            Text("MEMBER PERKS", style = MaterialTheme.typography.labelSmall, color = Clay)
            Spacer(Modifier.height(4.dp))
            Text("Free shipping & 30-day returns", style = MaterialTheme.typography.titleSmall, color = Color.White)
        }
        Box(
            Modifier
                .size(40.dp)
                .background(Color.White.copy(alpha = 0.12f), CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            LineIcon(R.drawable.ic_truck, null, Modifier.size(22.dp), tint = Color.White)
        }
    }
}

@Composable
private fun CategoryChips(selected: Category?, onSelect: (Category?) -> Unit) {
    val options = listOf<Category?>(null) + Category.entries
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(options) { option ->
            val isSelected = option == selected
            Surface(
                onClick = { onSelect(option) },
                shape = CircleShape,
                color = if (isSelected) Ink else Color.White,
                contentColor = if (isSelected) Color.White else Ink,
                border = if (isSelected) null else BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier.height(38.dp),
            ) {
                Box(Modifier.padding(horizontal = 18.dp), contentAlignment = Alignment.Center) {
                    Text(option?.name ?: "All", style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }
}

@Composable
private fun ProductCard(product: Product, quantityInCart: Int, onOpen: () -> Unit, onAdd: () -> Unit) {
    Column(
        Modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onOpen),
    ) {
        Box {
            ProductImage(product, Modifier.fillMaxWidth().aspectRatio(1f))
            InCartTag(quantityInCart, Modifier.padding(10.dp))
        }
        Spacer(Modifier.height(12.dp))
        Text(product.name, style = MaterialTheme.typography.titleMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text(
            product.tagline,
            style = MaterialTheme.typography.bodySmall,
            color = Muted,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(Modifier.height(10.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(formatPrice(product.priceCents), style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
            Surface(
                onClick = onAdd,
                shape = CircleShape,
                color = Tile,
                contentColor = Ink,
                modifier = Modifier.height(34.dp),
            ) {
                Box(Modifier.padding(horizontal = 12.dp), contentAlignment = Alignment.Center) {
                    Text("Add to cart", style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }
}

@Composable
private fun InCartTag(quantity: Int, modifier: Modifier) {
    AnimatedVisibility(
        visible = quantity > 0,
        enter = fadeIn(tween(200)) + scaleIn(tween(200), initialScale = 0.8f),
        exit = fadeOut(tween(150)),
        modifier = modifier,
    ) {
        Row(
            Modifier
                .background(Color.White, CircleShape)
                .padding(start = 8.dp, end = 10.dp, top = 5.dp, bottom = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            LineIcon(R.drawable.ic_check, null, Modifier.size(14.dp), tint = Success)
            Spacer(Modifier.width(4.dp))
            Text("$quantity in cart", style = MaterialTheme.typography.labelMedium, color = Ink)
        }
    }
}
