// ProductResponse.kt
package com.example.cafedealtura_dam.data.remote

data class ProductResponse(
    val id_coffe: Int,
    val brand: String,
    val price: Double,
    val img_url: String,
    val available: Int,
    val category: String,
    val origin: String,
    val weight: Int,

    val altitude: String = "1,400 - 1,800 msnm",
    val process: String = "Lavado",
    val notes: String = "Chocolate, Caramelo, Nuez"
)