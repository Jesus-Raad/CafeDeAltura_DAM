package com.example.cafedealtura_dam.dataAPI.responses

data class CheckEmailResponse(
    val success: Boolean,
    val exists: Boolean,
    val error: String? = null
)