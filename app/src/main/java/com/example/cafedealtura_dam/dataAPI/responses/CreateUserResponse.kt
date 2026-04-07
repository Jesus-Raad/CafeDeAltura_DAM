package com.example.cafedealtura_dam.dataAPI.responses

data class CreateUserResponse(
    val success: Boolean,
    val message: String? = null,
    val error: String? = null
)
//ApiService.Post.createUser(
//context = requireContext(),
//name = "Jesus",
//surname = "Raad",
//email = "jesus@email.com",
//phone = "612345678",
//password = "123456",
//onResult = { mensaje ->
//    // éxito
//},
//onError = { error ->
//    // error
//}
//)