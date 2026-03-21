package com.example.cafedealtura_dam.data.remote

import retrofit2.http.GET

interface ApiService {

    @GET("products")
    suspend fun getProducts(): List<ProductResponse>
}