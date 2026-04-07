package com.example.cafedealtura_dam.model

data class Direccion(
    val id: Int,
    val id_user: Int,
    val location: String,        // "Casa", "Oficina"
    val receptor: String,        // "María García"
    val street: String,          // "Calle Principal #123"
    val city: String,            // "Bogotá, Colombia"
    val poste_code: String,      // "CP: 110111"
    val phone: String,           // "+57 300 123 4567"
    val isPredeterminate: Boolean = false
)