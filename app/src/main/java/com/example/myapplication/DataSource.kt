package com.example.myapplication

import com.example.myapplication.models.Product

class DataSource {

    fun loadTrending(): List<Product> {
        return listOf(
            Product(R.drawable.shoe366, R.string.product_name_puma, R.string.product_category_puma, R.string.product_price_puma, sizes = listOf("5", "7.5", "8.5", "9", "10"), thumbnails = listOf(R.drawable.shoe366, R.drawable.shoe36)),
            Product(R.drawable.shoe32, R.string.product_name_adidas, R.string.product_category_adidas, R.string.product_price_adidas, sizes = listOf("6", "7", "8", "9", "10"), thumbnails = listOf(R.drawable.shoe32, R.drawable.shoe322)),
            Product(R.drawable.shoe37, R.string.product_name_nike, R.string.product_category_nike, R.string.product_price_nike, sizes = listOf("5", "6", "8.5", "9", "10"), thumbnails = listOf(R.drawable.shoe37, R.drawable.shoe377)),
            Product(R.drawable.shoe26, R.string.product_name_nike11, R.string.product_category_nike11, R.string.product_price_nike11, sizes = listOf("6.5", "7", "8", "9", "10"), thumbnails = listOf(R.drawable.shoe26, R.drawable.shoe55)),
            )
    }

    fun loadNike(): List<Product>{
        return listOf(
            Product(R.drawable.shoe27, R.string.product_name_nike1, R.string.product_category_nike1, R.string.product_price_nike1, sizes = listOf("6", "7", "8", "9", "10"), thumbnails = listOf(R.drawable.shoe27, R.drawable.shoe277, R.drawable.shoe2777)),
            Product(R.drawable.shoe28, R.string.product_name_nike2, R.string.product_category_nike2, R.string.product_price_nike2, sizes = listOf("5", "7", "8", "9.5", "10"), thumbnails = listOf(R.drawable.shoe28, R.drawable.shoe288, R.drawable.shoe2888)),
            Product(R.drawable.shoe29, R.string.product_name_nike3, R.string.product_category_nike3, R.string.product_price_nike3, sizes = listOf("6", "7", "8", "9", "10"), thumbnails = listOf(R.drawable.shoe29, R.drawable.shoe299)),
            Product(R.drawable.shoe30, R.string.product_name_nike4, R.string.product_category_nike4, R.string.product_price_nike4, sizes = listOf("6.5", "7", "8", "9", "10"), thumbnails = listOf(R.drawable.shoe30, R.drawable.shoe300, R.drawable.shoe3000)),
            Product(R.drawable.shoe31, R.string.product_name_nike5, R.string.product_category_nike5, R.string.product_price_nike5, sizes = listOf("5", "8", "8.5", "9", "10"), thumbnails = listOf(R.drawable.shoe31, R.drawable.shoe311, R.drawable.shoe3111)),
        )
    }
    fun loadAdidas(): List<Product> {
        return listOf(
            Product(R.drawable.shoe32, R.string.product_name_adidas0, R.string.product_category_adidas0, R.string.product_price_adidas0, sizes = listOf("6", "7", "8", "9", "10"), thumbnails = listOf(R.drawable.shoe32, R.drawable.shoe322)),
            Product(R.drawable.shoe33, R.string.product_name_adidas2, R.string.product_category_adidas2, R.string.product_price_adidas2, sizes = listOf("6", "7", "8", "9", "10"), thumbnails = listOf(R.drawable.shoe33, R.drawable.shoe333)),
            Product(R.drawable.shoe17, R.string.product_name_adidas3, R.string.product_category_adidas3, R.string.product_price_adidas3, sizes = listOf("6", "7", "8", "9", "10"), thumbnails = listOf(R.drawable.shoe17, R.drawable.shoe17)),
            Product(R.drawable.shoe34, R.string.product_name_adidas4, R.string.product_category_adidas4, R.string.product_price_adidas4, sizes = listOf("6", "7", "8", "9", "10"), thumbnails = listOf(R.drawable.shoe34, R.drawable.shoe34, R.drawable.shoe34)),
            Product(R.drawable.shoe35, R.string.product_name_adidas5, R.string.product_category_adidas5, R.string.product_price_adidas5, sizes = listOf("6", "7", "8", "9", "10"), thumbnails = listOf(R.drawable.shoe35, R.drawable.shoe35, R.drawable.shoe35)),
        )
    }

}