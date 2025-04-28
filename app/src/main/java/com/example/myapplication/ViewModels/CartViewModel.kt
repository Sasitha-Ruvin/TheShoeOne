package com.example.myapplication.ViewModels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.example.myapplication.models.CartItem
import java.util.UUID

class CartViewModel : ViewModel() {
    private val _cartItems = mutableStateListOf<CartItem>()
    val cartItems: SnapshotStateList<CartItem> = _cartItems

    // Add item to cart
    fun addToCart(
        imageResId: Int? = null,
        imageUrl: String? = null,
        name: String,
        category: String,
        color: String = "Default",
        size: String,
        quantity: Int,
        price: Double,
        isFromApi: Boolean = false
    ) {
        // Check if the item is already in the cart with the same specs
        val existingItemIndex = _cartItems.indexOfFirst {
            it.name == name && it.size == size && it.color == color
        }

        if (existingItemIndex != -1) {
            // Update quantity if item already exists
            val existingItem = _cartItems[existingItemIndex]
            val updatedItem = existingItem.copy(quantity = existingItem.quantity + quantity)
            _cartItems[existingItemIndex] = updatedItem
        } else {
            // Add new item
            val id = UUID.randomUUID().toString()
            _cartItems.add(
                CartItem(
                    id = id,
                    imageResId = imageResId,
                    imageUrl = imageUrl,
                    name = name,
                    category = category,
                    color = color,
                    size = size,
                    quantity = quantity,
                    price = price,
                    isFromApi = isFromApi
                )
            )
        }
    }

    // Remove item from cart
    fun removeFromCart(id: String) {
        _cartItems.removeIf { it.id == id }
    }

    // Clear cart
    fun clearCart() {
        _cartItems.clear()
    }

    // Update item quantity
    fun updateQuantity(id: String, quantity: Int) {
        val index = _cartItems.indexOfFirst { it.id == id }
        if (index != -1) {
            val item = _cartItems[index]
            _cartItems[index] = item.copy(quantity = quantity)
        }
    }

    // Calculate total price
    fun getSubtotal(): Double {
        return _cartItems.sumOf { it.price * it.quantity }
    }

    // Calculate shipping cost (fixed at 1500 for this example)
    fun getShippingCost(): Double {
        return if (_cartItems.isNotEmpty()) 1500.0 else 0.0
    }

    // Calculate total with shipping
    fun getTotal(): Double {
        return getSubtotal() + getShippingCost()
    }
}