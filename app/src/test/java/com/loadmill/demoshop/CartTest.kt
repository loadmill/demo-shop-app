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
        assertEquals(4999, viewModel.order?.cart?.totalCents)
        assertEquals("Jane", viewModel.order?.customerName)
        assertTrue(viewModel.order!!.number.startsWith("DS-"))
        assertTrue(viewModel.cart.isEmpty)

        viewModel.startOver()

        assertEquals(Screen.Catalog, viewModel.screen)
        assertEquals(null, viewModel.order)
        assertEquals(null, viewModel.selectedProduct)
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

    @Test
    fun totalIsSubtotalPlusFreeShipping() {
        val cart = Cart().add(backpack).add(bottle).increase(bottle.id)

        assertEquals(4999 + 2 * 1999, cart.subtotalCents)
        assertEquals(0, cart.shippingCents)
        assertEquals(cart.subtotalCents, cart.totalCents)
        assertEquals(0, Cart().totalCents)
    }

    @Test
    fun categoryFilterKeepsOnlyMatchingProducts() {
        assertEquals(catalogProducts, productsIn(null))
        assertEquals(listOf("Backpack", "Water Bottle"), productsIn(Category.Gear).map { it.name })
        assertTrue(productsIn(Category.Apparel).all { it.category == Category.Apparel })
    }

    @Test
    fun backpackIsTheFirstProductInTheCatalog() {
        assertEquals("Backpack", catalogProducts.first().name)
    }

    @Test
    fun backFromCartReturnsToTheProductItWasOpenedFrom() {
        val viewModel = ShopViewModel()
        viewModel.openProduct(backpack)
        viewModel.addToCart(backpack)
        viewModel.goTo(Screen.Cart)

        viewModel.back()
        assertEquals(Screen.Detail, viewModel.screen)
        assertEquals(backpack, viewModel.selectedProduct)

        viewModel.back()
        assertEquals(Screen.Catalog, viewModel.screen)
        assertEquals(null, viewModel.selectedProduct)

        viewModel.goTo(Screen.Cart)
        viewModel.back()
        assertEquals(Screen.Catalog, viewModel.screen)
    }

    @Test
    fun startingOverResetsTheCategoryFilter() {
        val viewModel = ShopViewModel()
        viewModel.selectCategory(Category.Accessories)

        viewModel.startOver()

        assertEquals(null, viewModel.category)
    }
}
