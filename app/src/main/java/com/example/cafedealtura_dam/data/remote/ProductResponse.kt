package com.example.cafedealtura_dam.data.remote

data class ProductResponse(
    val id_coffe: Int,
    val brand: String,
    val price: String,
    val img_url: String,
    val available: Int,
    val category: String
)