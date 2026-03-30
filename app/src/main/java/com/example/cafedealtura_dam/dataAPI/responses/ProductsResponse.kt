package com.example.cafedealtura_dam.dataAPI.responses

import com.example.cafedealtura_dam.model.Products_coffe

data class ProductsResponse(
    val success: Boolean,
    val products: List<Products_coffe>
)