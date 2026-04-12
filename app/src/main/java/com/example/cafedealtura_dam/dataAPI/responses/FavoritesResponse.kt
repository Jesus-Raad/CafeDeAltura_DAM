package com.example.cafedealtura_dam.dataAPI.responses

import com.example.cafedealtura_dam.model.Products_coffe

data class FavoritesResponse(
    val success: Boolean,
    val favorites: List<Products_coffe> = emptyList(),
    val error: String? = null
)