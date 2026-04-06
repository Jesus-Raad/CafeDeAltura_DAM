package com.example.cafedealtura_dam.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {

    @GET("get_products.php")
    suspend fun getProducts(): ProductsResponse

    @Headers("Content-Type: application/json")
    @POST("login_user.php")
    suspend fun loginUser(
        @Body request: LoginRequest
    ): LoginResponse
}
