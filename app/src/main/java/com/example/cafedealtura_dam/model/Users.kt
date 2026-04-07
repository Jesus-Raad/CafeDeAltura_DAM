package com.example.cafedealtura_dam.model

import java.io.Serializable

data class Users(
    val id_user: Int,
    val name: String,
    val surname: String? = null,
    val password: String? = null,
    val rol: String,
    val email: String
) : Serializable