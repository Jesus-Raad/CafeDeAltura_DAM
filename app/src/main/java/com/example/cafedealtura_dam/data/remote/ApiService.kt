package com.example.cafedealtura_dam.data.remote

import retrofit2.http.GET

interface ApiService {

    @GET("get_products.php")
    suspend fun getProducts(): ProductsResponse
}