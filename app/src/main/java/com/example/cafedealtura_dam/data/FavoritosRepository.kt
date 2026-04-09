package com.example.cafedealtura_dam.data

import com.example.cafedealtura_dam.model.Products_coffe

object FavoritosRepository {

    private val favoritosIds = mutableSetOf<Int>()

    fun toggle(id: Int) {
        if (favoritosIds.contains(id)) {
            favoritosIds.remove(id)
        } else {
            favoritosIds.add(id)
        }
    }

    fun isFavorito(id: Int): Boolean {
        return favoritosIds.contains(id)
    }

    fun getFavoritos(): List<Products_coffe> {
        return ProductsRepository.getProducts()
            .filter { favoritosIds.contains(it.id_coffe) }
    }

    fun isEmpty(): Boolean {
        return favoritosIds.isEmpty()
    }

    fun clear() {
        favoritosIds.clear()
    }
}