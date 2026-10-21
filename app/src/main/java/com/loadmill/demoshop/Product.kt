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
        "Rust ripstop, 20L",
        "A rust-orange ripstop daypack with a tonal windmill pattern, cyan zip pulls and a woven Loadmill label. A front zip pocket and side pockets keep your everyday gear close.",
        4999,
        Category.Gear,
        R.drawable.product_backpack,
    ),
    Product(
        "water-bottle",
        "Water Bottle",
        "Stay in the flow",
        "A forest-green insulated steel bottle with bold Stay in the Flow lettering and a winding flowchart illustration. Finished with a matte powder coat and a screw-top lid.",
        1999,
        Category.Gear,
        R.drawable.product_water_bottle,
    ),
    Product(
        "jersey",
        "Jersey",
        "Loadmill blue mesh",
        "A blue mesh basketball jersey with cyan side panels, striped trim and stitched Loadmill lettering and icon. Teamwear for your next game or your next release.",
        7999,
        Category.Apparel,
        R.drawable.product_jersey,
    ),
    Product(
        "hat",
        "Hat",
        "Ship it. Sleep well.",
        "A washed-black cotton cap with cream Ship it. Sleep well. embroidery, a tiny cyan sparkle and a curved brim. An understated piece from the Loadmill collection.",
        2999,
        Category.Accessories,
        R.drawable.product_hat,
    ),
    Product(
        "headphones",
        "Headphones",
        "Plum focus mode",
        "Plum-purple over-ear headphones with concentric ripple detailing, soft cushions and a subtle Loadmill engraving. Switch into focus mode with a little less noise.",
        9999,
        Category.Accessories,
        R.drawable.product_headphones,
    ),
    Product(
        "t-shirt",
        "T-Shirt",
        "Let it run",
        "A cream cotton crew neck from the Loadmill Leisure Club. A laid-back robot lounges between browser windows beneath the words Let It Run, in a vintage-style graphic print.",
        1499,
        Category.Apparel,
        R.drawable.product_tshirt,
    ),
)

fun productsIn(category: Category?): List<Product> =
    if (category == null) catalogProducts else catalogProducts.filter { it.category == category }

fun formatPrice(cents: Int): String = String.format(Locale.US, "$%d.%02d", cents / 100, cents % 100)
