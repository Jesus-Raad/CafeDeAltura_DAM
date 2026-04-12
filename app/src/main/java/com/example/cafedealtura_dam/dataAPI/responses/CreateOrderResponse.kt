package com.example.cafedealtura_dam.dataAPI.responses

data class CreateOrderResponse(
    val success: Boolean,
    val message: String? = null,
    val id_order: Int? = null,
    val subtotal_order: Double? = null,
    val shipping_cost: Double? = null,
    val total_order: Double? = null,
    val error: String? = null
)