package com.example.cafedealtura_dam.model

import java.io.Serializable

data class Products_coffe (
    val id_coffe: Int,
    val brand: String,
    val price: Double,
    val img_url: String? = null,
    val available: Int,
    val category: Int? = null,
    val weight: Int,
    val origin: String?=null,
    val description: String? = null
): Serializable