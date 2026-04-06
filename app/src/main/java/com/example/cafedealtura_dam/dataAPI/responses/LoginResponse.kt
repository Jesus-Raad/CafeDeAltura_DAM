package com.example.cafedealtura_dam.dataAPI.responses

import com.example.cafedealtura_dam.model.Users

data class LoginResponse(
    val success: Boolean,
    val user: Users? = null,
    val error: String? = null
)
