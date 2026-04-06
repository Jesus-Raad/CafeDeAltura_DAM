package com.example.cafedealtura_dam.data.remote

data class LoginResponse(
    val success: Boolean,
    val user: User?
)

data class User(
    val id_user: Int,
    val name: String,
    val surname: String?,
    val user_name: String,
    val rol: String
)