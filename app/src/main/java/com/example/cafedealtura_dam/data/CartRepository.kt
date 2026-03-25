package com.example.cafedealtura_dam.data

import com.example.cafedealtura_dam.model.CartItem
import com.example.cafedealtura_dam.model.Products_coffe

object CartRepository {

    private val items = mutableListOf<CartItem>()

    private const val SHIPPING_COST = 5.0
    private const val FREE_SHIPPING_MIN = 50.0

    fun addProduct(product: Products_coffe) {
        val existingItem = items.find { it.product.id_coffe == product.id_coffe }

        if (existingItem != null) {
            existingItem.quantity++
        } else {
            items.add(CartItem(product, 1))
        }
    }

    fun getItems(): List<CartItem> {
        return items.toList()
    }

    fun isEmpty(): Boolean {
        return items.isEmpty()
    }

    fun clearCart() {
        items.clear()
    }

    fun removeProduct(productId: Int) {
        items.removeAll { it.product.id_coffe == productId }
    }

    fun increaseQuantity(productId: Int) {
        val item = items.find { it.product.id_coffe == productId }
        if (item != null) {
            item.quantity++
        }
    }

    fun decreaseQuantity(productId: Int) {
        val item = items.find { it.product.id_coffe == productId }

        if (item != null) {
            if (item.quantity > 1) {
                item.quantity--
            } else {
                removeProduct(productId)
            }
        }
    }

    fun deleteItem(productId: Int) {
        removeProduct(productId)
    }

    fun getTotalItemsCount(): Int {
        return items.sumOf { it.quantity }
    }

    fun getSubtotal(): Double {
        return items.sumOf { it.product.price * it.quantity }
    }

    fun getShippingCost(): Double {
        return if (getSubtotal() > FREE_SHIPPING_MIN) 0.0 else SHIPPING_COST
    }

    fun hasFreeShipping(): Boolean {
        return getShippingCost() == 0.0
    }

    fun getTotal(): Double {
        return getSubtotal() + getShippingCost()
    }
}