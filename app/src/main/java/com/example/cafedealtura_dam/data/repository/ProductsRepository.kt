package com.example.cafedealtura_dam.data.repository

import android.util.Log
import com.example.cafedealtura_dam.data.remote.RetrofitInstance
import com.example.cafedealtura_dam.ui.products.ProductData
import com.example.cafedealtura_dam.ui.products.ProductData.products
import com.example.cafedealtura_dam.ui.products.ProductDescriptions
import com.example.cafedealtura_dam.ui.products.ProductUiModel

class ProductsRepository {

    suspend fun getProducts(): List<ProductUiModel> {
        return try {
            RetrofitInstance.api.getProducts().map { apiProduct ->

                val localMatch = ProductData.products.find { local ->
                    local.name.equals(apiProduct.brand, ignoreCase = true)
                }

                ProductUiModel(
                    name = apiProduct.brand,
                    origin = localMatch?.origin ?: apiProduct.category,
                    price = apiProduct.price
                        .replace("$", "")
                        .replace("€", "")
                        .replace(",", ".")
                        .trim()
                        .toDoubleOrNull() ?: (localMatch?.price ?: 0.0),
                    imageUrl = if (apiProduct.img_url.isNotBlank()) {
                        apiProduct.img_url
                    } else {
                        localMatch?.imageUrl ?: ""
                    },
                    description = localMatch?.description
                        ?: ProductDescriptions.getDescription(apiProduct.brand),
                    rating = localMatch?.rating ?: 4.5
                )
            }
        } catch (e: Exception) {
            ProductData.products
        }
        Log.d("API_TEST", "Products loaded: ${products.size}")
    }
}