package com.example.cafedealtura_dam.dataAPI.responses

data class UpdatePasswordResponse(
    val success: Boolean,
    val message: String? = null,
    val error: String? = null
)