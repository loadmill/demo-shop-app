package com.loadmill.demoshop

data class CartLine(val product: Product, val quantity: Int) {
    val totalCents: Int get() = product.priceCents * quantity
}

data class Cart(val lines: List<CartLine> = emptyList()) {
    val itemCount: Int get() = lines.sumOf { it.quantity }
    val subtotalCents: Int get() = lines.sumOf { it.totalCents }
    val shippingCents: Int get() = 0
    val totalCents: Int get() = subtotalCents + shippingCents
    val isEmpty: Boolean get() = lines.isEmpty()

    fun quantityOf(productId: String): Int = lines.find { it.product.id == productId }?.quantity ?: 0

    fun add(product: Product): Cart =
        if (lines.any { it.product.id == product.id }) {
            setQuantity(product.id, quantityOf(product.id) + 1)
        } else {
            Cart(lines + CartLine(product, 1))
        }

    fun increase(productId: String): Cart = setQuantity(productId, quantityOf(productId) + 1)

    fun decrease(productId: String): Cart = setQuantity(productId, quantityOf(productId) - 1)

    fun remove(productId: String): Cart = Cart(lines.filterNot { it.product.id == productId })

    private fun setQuantity(productId: String, quantity: Int): Cart =
        if (quantity <= 0) {
            remove(productId)
        } else {
            Cart(lines.map { if (it.product.id == productId) it.copy(quantity = quantity) else it })
        }
}
