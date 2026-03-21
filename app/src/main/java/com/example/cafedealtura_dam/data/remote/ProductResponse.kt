package com.example.cafedealtura_dam.data.remote

data class ProductResponse(
    val id: Int,
    val name: String,
    val origin: String,
    val price: String,
    val imageUrl: String,
    val description: String,
    val rating: Double
)