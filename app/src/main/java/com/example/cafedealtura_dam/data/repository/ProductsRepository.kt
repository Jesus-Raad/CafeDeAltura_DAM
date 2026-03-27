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
            val apiProducts = RetrofitInstance.api.getProducts().products  // ← .products
            Log.d("REPO", "API OK: ${apiProducts.size} products")

            apiProducts.map { apiProduct ->
                val localMatch = ProductData.products.find { local ->
                    local.name.equals(apiProduct.brand, ignoreCase = true)
                }
                ProductUiModel(
                    name = apiProduct.brand,
                    origin = apiProduct.origin,
                    price = apiProduct.price.toDoubleOrNull() ?: 0.0,
                    imageUrl = apiProduct.img_url,
                    description = apiProduct.description,
                    rating = localMatch?.rating ?: 4.5
                )
            }
        } catch (e: Exception) {
            Log.e("REPO", "API FAILED: ${e.message}")
            ProductData.products
        }
    }

}
