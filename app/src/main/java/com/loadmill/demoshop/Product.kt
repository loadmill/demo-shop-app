package com.loadmill.demoshop

import java.util.Locale

data class Product(
    val id: String,
    val name: String,
    val description: String,
    val priceCents: Int,
    val color: Long,
)

val catalogProducts = listOf(
    Product("backpack", "Backpack", "Water-resistant 20L daypack with a laptop sleeve.", 4999, 0xFF1E5EFF),
    Product("water-bottle", "Water Bottle", "Insulated steel bottle that keeps drinks cold for 24h.", 1999, 0xFF00A884),
    Product("sneakers", "Sneakers", "Lightweight everyday running shoes.", 7999, 0xFFE5484D),
    Product("sunglasses", "Sunglasses", "Polarized lenses with a classic frame.", 2999, 0xFF7C3AED),
    Product("headphones", "Headphones", "Wireless over-ear headphones with 30h battery.", 9999, 0xFF111827),
    Product("t-shirt", "T-Shirt", "Soft organic cotton crew neck tee.", 1499, 0xFFF59E0B),
)

fun formatPrice(cents: Int): String = String.format(Locale.US, "$%d.%02d", cents / 100, cents % 100)
