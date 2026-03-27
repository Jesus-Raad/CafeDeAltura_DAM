package com.example.cafedealtura_dam.model

data class Direccion(
    val id: Int,
    val nombre: String,           // "Casa", "Oficina"
    val nombrePersona: String,    // "María García"
    val calle: String,            // "Calle Principal #123"
    val ciudad: String,           // "Bogotá, Colombia"
    val codigoPostal: String,     // "CP: 110111"
    val telefono: String,         // "+57 300 123 4567"
    val esPredeterminada: Boolean = false
)