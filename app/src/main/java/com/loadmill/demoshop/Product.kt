package com.loadmill.demoshop

import androidx.annotation.DrawableRes
import java.util.Locale

enum class Category { Gear, Apparel, Accessories }

data class Product(
    val id: String,
    val name: String,
    val tagline: String,
    val description: String,
    val priceCents: Int,
    val category: Category,
    @DrawableRes val image: Int,
)

val catalogProducts = listOf(
    Product(
        "backpack",
        "Backpack",
        "Leather daypack, 20L",
        "A water-resistant 20L daypack in full-grain leather with a padded laptop sleeve, two side pockets and a front zip pocket for the small stuff.",
        4999,
        Category.Gear,
        R.drawable.product_backpack,
    ),
    Product(
        "water-bottle",
        "Water Bottle",
        "Insulated steel, 750 ml",
        "Double-wall insulated steel bottle that keeps drinks cold for 24 hours or hot for 12. Matte powder coat and a leak-proof lid.",
        1999,
        Category.Gear,
        R.drawable.product_water_bottle,
    ),
    Product(
        "sneakers",
        "Sneakers",
        "Lightweight runner",
        "Lightweight everyday running shoes with a breathable mesh upper and a responsive foam midsole for mile after mile of comfort.",
        7999,
        Category.Apparel,
        R.drawable.product_sneakers,
    ),
    Product(
        "sunglasses",
        "Sunglasses",
        "Polarized browline",
        "Polarized gradient lenses in a classic browline frame with metal rims and adjustable nose pads. Hard case included.",
        2999,
        Category.Accessories,
        R.drawable.product_sunglasses,
    ),
    Product(
        "headphones",
        "Headphones",
        "Wireless, 30h battery",
        "Wireless over-ear headphones with active noise cancelling, soft memory-foam cushions and up to 30 hours of battery life.",
        9999,
        Category.Accessories,
        R.drawable.product_headphones,
    ),
    Product(
        "t-shirt",
        "T-Shirt",
        "Organic cotton crew",
        "A soft, mid-weight crew neck tee in 100% organic cotton with a relaxed fit that holds its shape wash after wash.",
        1499,
        Category.Apparel,
        R.drawable.product_tshirt,
    ),
)

fun productsIn(category: Category?): List<Product> =
    if (category == null) catalogProducts else catalogProducts.filter { it.category == category }

fun formatPrice(cents: Int): String = String.format(Locale.US, "$%d.%02d", cents / 100, cents % 100)
