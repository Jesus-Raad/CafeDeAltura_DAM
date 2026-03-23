package com.example.cafedealtura_dam.model

import java.io.Serializable

data class Orders_item(
    val id_order_item: Int,
    val id_order: Int,
    val id_product: Int,
    val quantity: Int,
    val total_amount: Double
): Serializable
