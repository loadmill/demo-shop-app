package com.loadmill.demoshop

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlin.random.Random

enum class Screen { Catalog, Detail, Cart, Checkout, Confirmation }

data class CheckoutForm(
    val name: String = "Jane Doe",
    val email: String = "jane.doe@example.com",
    val address: String = "123 Demo Street, Springfield",
    val cardNumber: String = "4242 4242 4242 4242",
    val expiry: String = "12/30",
    val cvc: String = "123",
) {
    val isValid: Boolean
        get() = listOf(name, email, address, cardNumber, expiry, cvc).all { it.isNotBlank() } && "@" in email
}

data class Order(val number: String, val cart: Cart, val customerName: String)

class ShopViewModel : ViewModel() {
    var screen by mutableStateOf(Screen.Catalog)
        private set
    var cart by mutableStateOf(Cart())
        private set
    var category by mutableStateOf<Category?>(null)
        private set
    var selectedProduct by mutableStateOf<Product?>(null)
        private set
    var form by mutableStateOf(CheckoutForm())
        private set
    var showErrors by mutableStateOf(false)
        private set
    var order by mutableStateOf<Order?>(null)
        private set
    var addedProduct by mutableStateOf<Product?>(null)
        private set

    fun selectCategory(newCategory: Category?) {
        category = newCategory
    }

    fun openProduct(product: Product) {
        selectedProduct = product
        screen = Screen.Detail
    }

    fun addToCart(product: Product) {
        cart = cart.add(product)
    }

    fun addToCartFromDetail(product: Product) {
        addToCart(product)
        addedProduct = product
    }

    fun dismissAddedPrompt() {
        addedProduct = null
    }

    fun increase(productId: String) {
        cart = cart.increase(productId)
    }

    fun decrease(productId: String) {
        cart = cart.decrease(productId)
    }

    fun remove(productId: String) {
        cart = cart.remove(productId)
    }

    fun updateForm(newForm: CheckoutForm) {
        form = newForm
    }

    fun goTo(target: Screen) {
        addedProduct = null
        if (target == Screen.Catalog) selectedProduct = null
        screen = target
    }

    fun back(): Boolean {
        if (addedProduct != null) {
            dismissAddedPrompt()
            return true
        }
        when (screen) {
            Screen.Catalog -> return false
            Screen.Detail -> goTo(Screen.Catalog)
            Screen.Cart -> goTo(if (selectedProduct != null) Screen.Detail else Screen.Catalog)
            Screen.Checkout -> goTo(Screen.Cart)
            Screen.Confirmation -> continueShopping()
        }
        return true
    }

    fun placeOrder() {
        if (!form.isValid || cart.isEmpty) {
            showErrors = true
            return
        }
        order = Order(
            number = "DS-" + Random.nextInt(100000, 1000000),
            cart = cart,
            customerName = form.name.trim().substringBefore(' '),
        )
        cart = Cart()
        showErrors = false
        screen = Screen.Confirmation
    }

    fun continueShopping() {
        cart = Cart()
        form = CheckoutForm()
        showErrors = false
        order = null
        category = null
        selectedProduct = null
        screen = Screen.Catalog
    }
}
