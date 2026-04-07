package com.example.cafedealtura_dam.model

data class Direccion(
    val id_address: Int,
    val id_user: Int,
    val label: String,
    val receiver: String,
    val street: String,
    val city: String,
    val postal_code: String,
    val phone: String,
    val is_default: Int
)