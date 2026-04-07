package com.example.cafedealtura_dam.dataAPI.responses

data class AddressActionResponse(
    val success: Boolean,
    val message: String? = null,
    val error: String? = null,
    val id_address: Int? = null
)