package com.example.cafedealtura_dam.model

import java.io.Serializable

data class Products_coffe (
    val idCoffe: Int,
    val brand: String,
    val price: Double,
    val imgURL: Int? = null,
    val available: Boolean,
    val category: String? = null,
    val weight: Int,
    val origin: String?=null,
    val description: String? = null
    ): Serializable