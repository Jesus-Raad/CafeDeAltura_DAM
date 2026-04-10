package com.example.cafedealtura_dam.ui.profile

data class OrderUiModel(
    val date: String,
    val orderCode: String,
    val status: String,
    val items: List<OrderItemUiModel>,
    val total: Double
)