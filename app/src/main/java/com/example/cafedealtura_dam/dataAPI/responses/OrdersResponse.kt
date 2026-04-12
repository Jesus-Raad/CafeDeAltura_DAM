package com.example.cafedealtura_dam.dataAPI.responses

import com.example.cafedealtura_dam.model.Orders

data class OrdersResponse(
    val success: Boolean,
    val orders: List<Orders> = emptyList(),
    val error: String? = null
)