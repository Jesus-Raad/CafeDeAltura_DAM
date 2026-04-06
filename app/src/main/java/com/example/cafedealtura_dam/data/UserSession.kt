package com.example.cafedealtura_dam.data

import com.example.cafedealtura_dam.model.Users

object UserSession {

    private var currentUser: Users? = null

    fun setUser(user: Users) {
        currentUser = user
    }

    fun getUser(): Users? {
        return currentUser
    }

    fun isLogged(): Boolean {
        return currentUser != null
    }

    fun clearUser() {
        currentUser = null
    }

    fun getUserId(): Int? {
        return currentUser?.id_user
    }
}