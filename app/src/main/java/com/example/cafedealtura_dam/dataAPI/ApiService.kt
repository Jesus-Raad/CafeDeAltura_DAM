package com.example.cafedealtura_dam.dataAPI

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.example.cafedealtura_dam.model.Products_coffe
import com.google.gson.Gson

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
}