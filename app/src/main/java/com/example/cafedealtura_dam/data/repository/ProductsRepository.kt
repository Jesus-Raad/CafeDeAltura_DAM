package com.example.cafedealtura_dam.data.repository

import com.example.cafedealtura_dam.data.remote.ApiClient
import com.example.cafedealtura_dam.ui.products.ProductUiModel

class ProductsRepository {

    suspend fun getProducts(): List<ProductUiModel> {
        return ApiClient.apiService.getProducts().map { product ->

            val originText = when {
                !product.origin.isNullOrBlank() -> product.origin
                else -> "Origen no disponible"
            }

            val weightText = if (product.weight != null) {
                "${product.weight}g"
            } else {
                "250g"
            }

            ProductUiModel(
                name = product.brand,
                origin = originText,
                meta = "$weightText • ${product.category}",
                price = "${product.price} €",
                imageUrl = product.img_url
            )
        }
    }
}