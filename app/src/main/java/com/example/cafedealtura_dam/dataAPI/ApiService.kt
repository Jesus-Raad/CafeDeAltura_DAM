package com.example.cafedealtura_dam.dataAPI

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.example.cafedealtura_dam.dataAPI.responses.AddressActionResponse
import com.example.cafedealtura_dam.dataAPI.responses.AddressesResponse
import com.example.cafedealtura_dam.dataAPI.responses.CheckEmailResponse
import com.example.cafedealtura_dam.dataAPI.responses.CreateUserResponse
import com.example.cafedealtura_dam.dataAPI.responses.LoginResponse
import com.example.cafedealtura_dam.dataAPI.responses.ProductsResponse
import com.example.cafedealtura_dam.model.Direccion
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
                        onResult(result.products)
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

        fun getAddresses(
            context: Context,
            idUser: Int,
            onResult: (List<Direccion>) -> Unit,
            onError: (String) -> Unit
        ) {
            val url = ApiConfig.BASE_URL + "get_addresses.php?id_user=$idUser"

            val request = StringRequest(
                Request.Method.GET,
                url,
                { response ->
                    try {
                        val result = Gson().fromJson(response, AddressesResponse::class.java)

                        if (result.success) {
                            onResult(result.addresses)
                        } else {
                            onError(result.error ?: "Error al obtener direcciones")
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

        fun checkEmail(
            context: Context,
            email: String,
            onResult: (Boolean) -> Unit,
            onError: (String) -> Unit
        ) {
            val url = ApiConfig.BASE_URL + "check_email.php"

            val jsonBody = JSONObject().apply {
                put("email", email)
            }

            val request = JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonBody,
                { response ->
                    try {
                        val result = Gson().fromJson(
                            response.toString(),
                            CheckEmailResponse::class.java
                        )

                        if (result.success) {
                            onResult(result.exists)
                        } else {
                            onError(result.error ?: "Error comprobando email")
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

        fun createUser(
            context: Context,
            name: String,
            surname: String,
            email: String,
            phone: String,
            password: String,
            onResult: (String) -> Unit,
            onError: (String) -> Unit
        ) {
            val url = ApiConfig.BASE_URL + "create_user.php"

            val jsonBody = JSONObject().apply {
                put("name", name)
                put("surname", surname)
                put("email", email)
                put("phone", phone)
                put("password", password)
            }

            val request = JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonBody,
                { response ->
                    try {
                        val result = Gson().fromJson(
                            response.toString(),
                            CreateUserResponse::class.java
                        )

                        if (result.success) {
                            onResult(result.message ?: "Usuario creado correctamente")
                        } else {
                            onError(result.error ?: "Error al crear usuario")
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

        fun createAddress(
            context: Context,
            idUser: Int,
            label: String,
            receiver: String,
            street: String,
            city: String,
            postalCode: String,
            phone: String,
            isDefault: Int,
            onResult: (AddressActionResponse) -> Unit,
            onError: (String) -> Unit
        ) {
            val url = ApiConfig.BASE_URL + "create_address.php"

            val jsonBody = JSONObject().apply {
                put("id_user", idUser)
                put("label", label)
                put("receiver", receiver)
                put("street", street)
                put("city", city)
                put("postal_code", postalCode)
                put("phone", phone)
                put("is_default", isDefault)
            }

            val request = JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonBody,
                { response ->
                    try {
                        val result = Gson().fromJson(
                            response.toString(),
                            AddressActionResponse::class.java
                        )

                        if (result.success) {
                            onResult(result)
                        } else {
                            onError(result.error ?: "Error al crear dirección")
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

        fun updateAddress(
            context: Context,
            idAddress: Int,
            idUser: Int,
            label: String,
            receiver: String,
            street: String,
            city: String,
            postalCode: String,
            phone: String,
            isDefault: Int,
            onResult: (AddressActionResponse) -> Unit,
            onError: (String) -> Unit
        ) {
            val url = ApiConfig.BASE_URL + "update_address.php"

            val jsonBody = JSONObject().apply {
                put("id_address", idAddress)
                put("id_user", idUser)
                put("label", label)
                put("receiver", receiver)
                put("street", street)
                put("city", city)
                put("postal_code", postalCode)
                put("phone", phone)
                put("is_default", isDefault)
            }

            val request = JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonBody,
                { response ->
                    try {
                        val result = Gson().fromJson(
                            response.toString(),
                            AddressActionResponse::class.java
                        )

                        if (result.success) {
                            onResult(result)
                        } else {
                            onError(result.error ?: "Error al actualizar dirección")
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

        fun deleteAddress(
            context: Context,
            idAddress: Int,
            idUser: Int,
            onResult: (AddressActionResponse) -> Unit,
            onError: (String) -> Unit
        ) {
            val url = ApiConfig.BASE_URL + "delete_address.php"

            val jsonBody = JSONObject().apply {
                put("id_address", idAddress)
                put("id_user", idUser)
            }

            val request = JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonBody,
                { response ->
                    try {
                        val result = Gson().fromJson(
                            response.toString(),
                            AddressActionResponse::class.java
                        )

                        if (result.success) {
                            onResult(result)
                        } else {
                            onError(result.error ?: "Error al eliminar dirección")
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