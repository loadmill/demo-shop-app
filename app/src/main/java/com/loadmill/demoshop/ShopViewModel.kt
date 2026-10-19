package com.loadmill.demoshop

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlin.random.Random

enum class Screen { Catalog, Cart, Checkout, Confirmation }

data class CheckoutForm(
    val name: String = "Jane Doe",
    val email: String = "jane.doe@example.com",
    val address: String = "123 Demo Street, Springfield",
    val cardNumber: String = "4242 4242 4242 4242",
    val expiry: String = "12/30",
    val cvc: String = "123",
) {
    val isValid: Boolean
        get() = listOf(name, email, address, cardNumber, expiry, cvc).all { it.isNotBlank() }
}

data class Order(val number: String, val lines: List<CartLine>, val totalCents: Int)

class ShopViewModel : ViewModel() {
    var screen by mutableStateOf(Screen.Catalog)
        private set
    var cart by mutableStateOf(Cart())
        private set
    var form by mutableStateOf(CheckoutForm())
        private set
    var showErrors by mutableStateOf(false)
        private set
    var order by mutableStateOf<Order?>(null)
        private set

    fun addToCart(product: Product) {
        cart = cart.add(product)
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
        screen = target
    }

    fun back(): Boolean {
        screen = when (screen) {
            Screen.Catalog -> return false
            Screen.Cart -> Screen.Catalog
            Screen.Checkout -> Screen.Cart
            Screen.Confirmation -> {
                startOver()
                return true
            }
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
            lines = cart.lines,
            totalCents = cart.totalCents,
        )
        cart = Cart()
        showErrors = false
        screen = Screen.Confirmation
    }

    fun startOver() {
        cart = Cart()
        form = CheckoutForm()
        showErrors = false
        order = null
        screen = Screen.Catalog
    }
}
