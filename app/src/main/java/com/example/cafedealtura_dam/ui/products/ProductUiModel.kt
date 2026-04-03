package com.example.cafedealtura_dam.ui.products

data class ProductUiModel(
    val name: String,
    val origin: String,
    val price: Double,
    val imageUrl: String,
    val description: String,
    val rating: Double,
    val grindType: String = "grano",
    val category: String = ""
)