package com.example.cafedealtura_dam.data.remote

data class ProductsResponse(
    val success: Boolean,
    val products: List<ProductResponse>
)