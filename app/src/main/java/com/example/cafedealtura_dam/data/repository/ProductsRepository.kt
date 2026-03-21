package com.example.cafedealtura_dam.data.repository

import com.example.cafedealtura_dam.data.remote.RetrofitInstance
import com.example.cafedealtura_dam.ui.products.ProductUiModel

class ProductsRepository {

    suspend fun getProducts(): List<ProductUiModel> {
        return try {
            RetrofitInstance.api.getProducts().map {
                ProductUiModel(
                    name = it.name,
                    origin = it.origin,
                    price = it.price,
                    imageUrl = it.imageUrl,
                    description = it.description,
                    rating = it.rating
                )
            }
        } catch (e: Exception) {
            emptyList() // fallback
        }
    }
}