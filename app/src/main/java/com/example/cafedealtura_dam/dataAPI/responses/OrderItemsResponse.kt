package com.example.cafedealtura_dam.dataAPI.responses

import com.example.cafedealtura_dam.model.Orders_item

data class OrderItemsResponse(
    val success: Boolean,
    val items: List<Orders_item> = emptyList(),
    val error: String? = null,
    val message: String? = null
)