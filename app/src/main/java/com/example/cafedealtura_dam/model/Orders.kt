package com.example.cafedealtura_dam.model

import java.io.Serializable
import java.util.Date

data class Orders(
    val id_order: Int,
    val id_user: Int,
    val date: String,
    val total_amount:  Double,
    val status_order: Int
): Serializable
