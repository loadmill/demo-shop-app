package com.loadmill.demoshop

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CartTest {
    private val backpack = catalogProducts.first { it.name == "Backpack" }
    private val bottle = catalogProducts.first { it.name == "Water Bottle" }

    @Test
    fun addingTheSameProductTwiceIncreasesItsQuantity() {
        val cart = Cart().add(backpack).add(backpack).add(bottle)

        assertEquals(2, cart.lines.size)
        assertEquals(2, cart.quantityOf(backpack.id))
        assertEquals(3, cart.itemCount)
    }

    @Test
    fun totalReflectsQuantities() {
        val cart = Cart().add(backpack).increase(backpack.id).add(bottle)

        assertEquals(2 * 4999 + 1999, cart.totalCents)
        assertEquals("$119.97", formatPrice(cart.totalCents))
    }

    @Test
    fun decreasingToZeroRemovesTheLine() {
        val cart = Cart().add(backpack).add(bottle).decrease(backpack.id)

        assertEquals(0, cart.quantityOf(backpack.id))
        assertEquals(listOf(bottle), cart.lines.map { it.product })
    }

    @Test
    fun removeDropsTheWholeLine() {
        val cart = Cart().add(backpack).increase(backpack.id).remove(backpack.id)

        assertTrue(cart.isEmpty)
        assertEquals(0, cart.totalCents)
    }

    @Test
    fun placingAnOrderAndStartingOverClearsTheCart() {
        val viewModel = ShopViewModel()
        viewModel.addToCart(backpack)
        viewModel.goTo(Screen.Checkout)

        viewModel.placeOrder()

        assertEquals(Screen.Confirmation, viewModel.screen)
        assertEquals(4999, viewModel.order?.totalCents)
        assertTrue(viewModel.order!!.number.startsWith("DS-"))
        assertTrue(viewModel.cart.isEmpty)

        viewModel.startOver()

        assertEquals(Screen.Catalog, viewModel.screen)
        assertEquals(null, viewModel.order)
        assertTrue(viewModel.cart.isEmpty)
    }

    @Test
    fun placingAnOrderWithABlankFieldShowsErrors() {
        val viewModel = ShopViewModel()
        viewModel.addToCart(backpack)
        viewModel.updateForm(viewModel.form.copy(email = " "))

        viewModel.placeOrder()

        assertTrue(viewModel.showErrors)
        assertEquals(Screen.Catalog, viewModel.screen)
        assertEquals(1, viewModel.cart.itemCount)
    }
}
