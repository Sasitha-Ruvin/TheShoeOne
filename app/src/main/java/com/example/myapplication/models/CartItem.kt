package com.example.myapplication.models

data class CartItem(
    val id: String,
    val imageResId: Int? = null,
    val imageUrl: String? = null,
    val name: String,
    val category: String,
    val color: String = "Default",
    val size: String,
    val quantity: Int,
    val price: Double,
    val isFromApi: Boolean = false
)
