package com.example.cafedealtura_dam.model

import java.io.Serializable
import java.util.Date

data class Orders(
    val id_order: Int,
    val id_user: Int,
    val user_name: String,
    val date: Date,
    val total_amount:  Double,
    val status_order: Boolean
): Serializable
