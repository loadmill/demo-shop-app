package com.loadmill.demoshop.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.loadmill.demoshop.Cart
import com.loadmill.demoshop.CartLine
import com.loadmill.demoshop.CheckoutForm
import com.loadmill.demoshop.Order
import com.loadmill.demoshop.Product
import com.loadmill.demoshop.Screen
import com.loadmill.demoshop.ShopViewModel
import com.loadmill.demoshop.catalogProducts
import com.loadmill.demoshop.formatPrice

private val buttonHeight = 52.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DemoShopApp(viewModel: ShopViewModel = viewModel()) {
    BackHandler(enabled = viewModel.screen != Screen.Catalog) { viewModel.back() }

    val title = when (viewModel.screen) {
        Screen.Catalog -> "Demo Shop"
        Screen.Cart -> "Your cart"
        Screen.Checkout -> "Checkout"
        Screen.Confirmation -> "Order confirmed"
    }
    val showBack = viewModel.screen == Screen.Cart || viewModel.screen == Screen.Checkout

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title, fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    if (showBack) {
                        IconButton(onClick = { viewModel.back() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                },
                actions = {
                    if (viewModel.screen == Screen.Catalog) {
                        CartButton(viewModel.cart.itemCount) { viewModel.goTo(Screen.Cart) }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        Box(Modifier.padding(padding).consumeWindowInsets(padding).fillMaxSize()) {
            when (viewModel.screen) {
                Screen.Catalog -> CatalogScreen(viewModel.cart, viewModel::addToCart)
                Screen.Cart -> CartScreen(
                    cart = viewModel.cart,
                    onIncrease = viewModel::increase,
                    onDecrease = viewModel::decrease,
                    onRemove = viewModel::remove,
                    onContinueShopping = { viewModel.goTo(Screen.Catalog) },
                    onCheckout = { viewModel.goTo(Screen.Checkout) },
                )
                Screen.Checkout -> CheckoutScreen(
                    cart = viewModel.cart,
                    form = viewModel.form,
                    showErrors = viewModel.showErrors,
                    onFormChange = viewModel::updateForm,
                    onPlaceOrder = viewModel::placeOrder,
                )
                Screen.Confirmation -> viewModel.order?.let { ConfirmationScreen(it, viewModel::startOver) }
            }
        }
    }
}

@Composable
private fun CartButton(count: Int, onClick: () -> Unit) {
    TextButton(onClick = onClick, modifier = Modifier.padding(end = 8.dp)) {
        Icon(Icons.Filled.ShoppingCart, contentDescription = null, tint = Color.White)
        Spacer(Modifier.width(6.dp))
        Text("Cart ($count)", color = Color.White, style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
private fun CatalogScreen(cart: Cart, onAdd: (Product) -> Unit) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(catalogProducts, key = { it.id }) { product ->
            ProductCard(product, cart.quantityOf(product.id)) { onAdd(product) }
        }
    }
}

@Composable
private fun ProductCard(product: Product, quantityInCart: Int, onAdd: () -> Unit) {
    Card(Modifier.fillMaxWidth()) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
            ProductImage(product, 72)
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(product.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(product.description, style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(8.dp))
                Text(formatPrice(product.priceCents), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(12.dp))
                Button(onClick = onAdd, modifier = Modifier.fillMaxWidth().height(buttonHeight)) {
                    Icon(Icons.Filled.Add, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Add to cart")
                }
                if (quantityInCart > 0) {
                    Spacer(Modifier.height(6.dp))
                    Text("In cart: $quantityInCart", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
private fun ProductImage(product: Product, sizeDp: Int) {
    Box(
        modifier = Modifier
            .size(sizeDp.dp)
            .background(Color(product.color), RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            product.name.first().toString(),
            color = Color.White,
            style = if (sizeDp >= 64) MaterialTheme.typography.headlineMedium else MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun CartScreen(
    cart: Cart,
    onIncrease: (String) -> Unit,
    onDecrease: (String) -> Unit,
    onRemove: (String) -> Unit,
    onContinueShopping: () -> Unit,
    onCheckout: () -> Unit,
) {
    Column(Modifier.fillMaxSize()) {
        if (cart.isEmpty) {
            Column(
                modifier = Modifier.weight(1f).fillMaxWidth().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Icon(Icons.Filled.ShoppingCart, contentDescription = null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.height(12.dp))
                Text("Your cart is empty", style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(16.dp))
                OutlinedButton(onClick = onContinueShopping, modifier = Modifier.height(buttonHeight)) {
                    Text("Continue shopping")
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(cart.lines, key = { it.product.id }) { line ->
                    CartLineCard(line, onIncrease, onDecrease, onRemove)
                }
            }
        }
        BottomBar {
            TotalRow(cart.totalCents)
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = onCheckout,
                enabled = !cart.isEmpty,
                modifier = Modifier.fillMaxWidth().height(buttonHeight),
            ) {
                Text("Checkout")
            }
        }
    }
}

@Composable
private fun CartLineCard(
    line: CartLine,
    onIncrease: (String) -> Unit,
    onDecrease: (String) -> Unit,
    onRemove: (String) -> Unit,
) {
    val id = line.product.id
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                ProductImage(line.product, 48)
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(line.product.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("${formatPrice(line.product.priceCents)} each", style = MaterialTheme.typography.bodyMedium)
                }
                Text(formatPrice(line.totalCents), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            }
            Spacer(Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                FilledTonalButton(onClick = { onDecrease(id) }, modifier = Modifier.size(width = 56.dp, height = buttonHeight)) {
                    Text("-", style = MaterialTheme.typography.titleLarge)
                }
                Text(
                    "Qty: ${line.quantity}",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.titleMedium,
                )
                FilledTonalButton(onClick = { onIncrease(id) }, modifier = Modifier.size(width = 56.dp, height = buttonHeight)) {
                    Text("+", style = MaterialTheme.typography.titleLarge)
                }
                Spacer(Modifier.weight(1f))
                TextButton(onClick = { onRemove(id) }, modifier = Modifier.height(buttonHeight)) {
                    Icon(Icons.Filled.Delete, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text("Remove")
                }
            }
        }
    }
}

@Composable
private fun CheckoutScreen(
    cart: Cart,
    form: CheckoutForm,
    showErrors: Boolean,
    onFormChange: (CheckoutForm) -> Unit,
    onPlaceOrder: () -> Unit,
) {
    Column(Modifier.fillMaxSize().imePadding()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            SectionTitle("Order summary")
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    cart.lines.forEach { SummaryLine(it) }
                    HorizontalDivider()
                    TotalRow(cart.totalCents)
                }
            }
            SectionTitle("Shipping details")
            FormField("Full name", form.name, showErrors) { onFormChange(form.copy(name = it)) }
            FormField("Email", form.email, showErrors, KeyboardType.Email) { onFormChange(form.copy(email = it)) }
            FormField("Address", form.address, showErrors) { onFormChange(form.copy(address = it)) }
            SectionTitle("Payment (demo card)")
            FormField("Card number", form.cardNumber, showErrors, KeyboardType.Number) { onFormChange(form.copy(cardNumber = it)) }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(Modifier.weight(1f)) {
                    FormField("Expiry (MM/YY)", form.expiry, showErrors) { onFormChange(form.copy(expiry = it)) }
                }
                Box(Modifier.weight(1f)) {
                    FormField("CVC", form.cvc, showErrors, KeyboardType.Number) { onFormChange(form.copy(cvc = it)) }
                }
            }
            Text(
                "This is a demo. No real payment is made.",
                style = MaterialTheme.typography.bodySmall,
            )
        }
        BottomBar {
            if (showErrors && !form.isValid) {
                Text(
                    "Please fill in all fields.",
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(Modifier.height(8.dp))
            }
            Button(onClick = onPlaceOrder, modifier = Modifier.fillMaxWidth().height(buttonHeight)) {
                Text("Place order")
            }
        }
    }
}

@Composable
private fun FormField(
    label: String,
    value: String,
    showErrors: Boolean,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit,
) {
    val isError = showErrors && value.isBlank()
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        isError = isError,
        supportingText = if (isError) {
            { Text("Required") }
        } else {
            null
        },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun ConfirmationScreen(order: Order, onStartOver: () -> Unit) {
    Column(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(
                Icons.Filled.CheckCircle,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = Color(0xFF00A884),
            )
            Text("Order confirmed", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text("Order number: ${order.number}", style = MaterialTheme.typography.titleMedium)
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    order.lines.forEach { SummaryLine(it) }
                    HorizontalDivider()
                    TotalRow(order.totalCents)
                }
            }
        }
        BottomBar {
            Button(onClick = onStartOver, modifier = Modifier.fillMaxWidth().height(buttonHeight)) {
                Text("Start over")
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(text, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
}

@Composable
private fun SummaryLine(line: CartLine) {
    Row(Modifier.fillMaxWidth()) {
        Text("${line.quantity} x ${line.product.name}", modifier = Modifier.weight(1f))
        Text(formatPrice(line.totalCents))
    }
}

@Composable
private fun TotalRow(totalCents: Int) {
    Row(Modifier.fillMaxWidth()) {
        Text("Total", modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text(formatPrice(totalCents), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun BottomBar(content: @Composable () -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
    ) {
        content()
    }
}
