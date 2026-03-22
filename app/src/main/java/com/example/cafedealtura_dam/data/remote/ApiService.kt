package com.example.cafedealtura_dam.data.remote

import retrofit2.http.GET

interface ApiService {

    @GET("mi_api/get_products.php")
    suspend fun getProducts(): List<ProductResponse>
}