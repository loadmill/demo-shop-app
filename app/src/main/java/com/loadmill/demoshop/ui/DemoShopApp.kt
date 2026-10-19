package com.loadmill.demoshop.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.loadmill.demoshop.Order
import com.loadmill.demoshop.Product
import com.loadmill.demoshop.Screen
import com.loadmill.demoshop.ShopViewModel

private data class Destination(val screen: Screen, val product: Product?, val order: Order?)

@Composable
fun DemoShopApp(viewModel: ShopViewModel = viewModel()) {
    BackHandler(enabled = viewModel.screen != Screen.Catalog) { viewModel.back() }

    Surface(color = MaterialTheme.colorScheme.background, modifier = Modifier.fillMaxSize()) {
        AnimatedContent(
            targetState = Destination(viewModel.screen, viewModel.selectedProduct, viewModel.order),
            contentKey = { it.screen },
            transitionSpec = {
                val direction = if (targetState.screen.ordinal >= initialState.screen.ordinal) 1 else -1
                (fadeIn(tween(220, delayMillis = 40)) + slideInHorizontally(tween(260)) { direction * it / 14 }) togetherWith
                    fadeOut(tween(120))
            },
            label = "screen",
            modifier = Modifier.fillMaxSize().safeDrawingPadding(),
        ) { destination ->
            when (destination.screen) {
                Screen.Catalog -> CatalogScreen(
                    cart = viewModel.cart,
                    category = viewModel.category,
                    onCategoryChange = viewModel::selectCategory,
                    onOpenProduct = viewModel::openProduct,
                    onAdd = viewModel::addToCart,
                    onOpenCart = { viewModel.goTo(Screen.Cart) },
                )
                Screen.Detail -> destination.product?.let { product ->
                    ProductDetailScreen(
                        product = product,
                        cart = viewModel.cart,
                        onBack = { viewModel.back() },
                        onAdd = { viewModel.addToCart(product) },
                        onOpenCart = { viewModel.goTo(Screen.Cart) },
                    )
                }
                Screen.Cart -> CartScreen(
                    cart = viewModel.cart,
                    onBack = { viewModel.back() },
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
                    onBack = { viewModel.back() },
                    onFormChange = viewModel::updateForm,
                    onPlaceOrder = viewModel::placeOrder,
                )
                Screen.Confirmation -> destination.order?.let { ConfirmationScreen(it, viewModel::startOver) }
            }
        }
    }
}
