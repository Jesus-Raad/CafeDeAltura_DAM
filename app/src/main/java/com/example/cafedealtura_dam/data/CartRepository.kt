package com.example.cafedealtura_dam.data

import com.example.cafedealtura_dam.model.CartItem
import com.example.cafedealtura_dam.model.Products_coffe

object CartRepository {

    private val items = mutableListOf<CartItem>()

    fun addProduct(product: Products_coffe) {
        val existingItem = items.find { it.product.idCoffe == product.idCoffe }

        if (existingItem != null) {
            existingItem.quantity++
        } else {
            items.add(CartItem(product, 1))
        }
    }

    fun getItems(): List<CartItem> {
        return items.toList()
    }

    fun clearCart() {
        items.clear()
    }

    fun removeProduct(productId: Int) {
        items.removeAll { it.product.idCoffe == productId}
    }
}