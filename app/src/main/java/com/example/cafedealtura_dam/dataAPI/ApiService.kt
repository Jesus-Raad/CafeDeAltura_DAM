package com.example.cafedealtura_dam.dataAPI

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.example.cafedealtura_dam.dataAPI.responses.LoginResponse
import com.example.cafedealtura_dam.dataAPI.responses.ProductsResponse
import com.example.cafedealtura_dam.model.Products_coffe
import com.example.cafedealtura_dam.model.Users
import com.google.gson.Gson
import org.json.JSONObject

object ApiService {

    object Get {
        fun getProducts(
            context: Context,
            onResult: (List<Products_coffe>) -> Unit,
            onError: (String) -> Unit
        ) {
            val url = ApiConfig.BASE_URL + "get_products.php"

            val request = StringRequest(
                Request.Method.GET,
                url,
                { response ->
                    try {
                        val result = Gson().fromJson(response, ProductsResponse::class.java)
                        val lista = result.products
                        onResult(lista)
                    } catch (e: Exception) {
                        onError("Error parseando: ${e.message}")
                    }
                },
                { error ->
                    onError(error.message ?: "Error")
                }
            )

            VolleySingleton.getInstance(context).addToRequestQueue(request)
        }
    }
    object Post {
        fun loginUser(
            context: Context,
            email: String,
            password: String,
            onResult: (Users) -> Unit,
            onError: (String) -> Unit
        ) {
            val url = ApiConfig.BASE_URL + "login_user.php"

            val jsonBody = JSONObject().apply {
                put("email", email)
                put("password", password)
            }

            val request = JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonBody,
                { response ->
                    try {
                        val result = Gson().fromJson(response.toString(), LoginResponse::class.java)

                        if (result.success && result.user != null) {
                            onResult(result.user)
                        } else {
                            onError(result.error ?: "Error en login")
                        }

                    } catch (e: Exception) {
                        onError("Error parseando: ${e.message}")
                    }
                },
                { error ->
                    onError(error.message ?: "Error en la petición")
                }
            )

            VolleySingleton.getInstance(context).addToRequestQueue(request)
        }
    }
}