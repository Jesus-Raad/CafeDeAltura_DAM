package com.example.cafedealtura_dam.data

import com.example.cafedealtura_dam.model.Products_coffe

object ProductsRepository {
    private val products = mutableListOf<Products_coffe>()

    fun setProducts(newProducts: List<Products_coffe>) {
        products.clear()
        products.addAll(newProducts)
    }

    fun getProducts(): List<Products_coffe> {º
        return products.toList()
    }

    fun getProductById(id: Int): Products_coffe? {
        return products.find { it.id_coffe == id }
    }

    fun isEmpty(): Boolean {
        return products.isEmpty()
    }

    fun clear() {
        products.clear()
    }
}