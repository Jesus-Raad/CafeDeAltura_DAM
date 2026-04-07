package com.example.cafedealtura_dam.dataAPI.responses

import com.example.cafedealtura_dam.model.Direccion

data class AddressesResponse(
    val success: Boolean,
    val addresses: List<Direccion> = emptyList(),
    val error: String? = null
)
