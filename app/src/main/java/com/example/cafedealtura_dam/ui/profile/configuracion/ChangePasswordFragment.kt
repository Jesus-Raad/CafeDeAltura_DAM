package com.example.cafedealtura_dam.ui.profile.configuracion

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cafedealtura_dam.R
import com.example.cafedealtura_dam.data.UserSession
import com.example.cafedealtura_dam.dataAPI.ApiService
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ChangePasswordFragment : Fragment(R.layout.fragment_change_password) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            v.setPadding(
                v.paddingLeft,
                systemBars.top,
                v.paddingRight,
                v.paddingBottom
            )

            insets
        }

        val btnBack = view.findViewById<ImageView>(R.id.btnBack)
        val btnCancel = view.findViewById<MaterialButton>(R.id.btnCancel)
        val btnUpdatePassword = view.findViewById<MaterialButton>(R.id.btnUpdatePassword)

        val tilCurrentPassword = view.findViewById<TextInputLayout>(R.id.tilCurrentPassword)
        val tilNewPassword = view.findViewById<TextInputLayout>(R.id.tilNewPassword)
        val tilConfirmPassword = view.findViewById<TextInputLayout>(R.id.tilConfirmPassword)

        val etCurrentPassword = view.findViewById<TextInputEditText>(R.id.etCurrentPassword)
        val etNewPassword = view.findViewById<TextInputEditText>(R.id.etNewPassword)
        val etConfirmPassword = view.findViewById<TextInputEditText>(R.id.etConfirmPassword)

        btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        btnCancel.setOnClickListener {
            findNavController().popBackStack()
        }

        btnUpdatePassword.setOnClickListener {
            tilCurrentPassword.error = null
            tilNewPassword.error = null
            tilConfirmPassword.error = null

            val currentPassword = etCurrentPassword.text?.toString()?.trim().orEmpty()
            val newPassword = etNewPassword.text?.toString()?.trim().orEmpty()
            val confirmPassword = etConfirmPassword.text?.toString()?.trim().orEmpty()

            when {
                currentPassword.isEmpty() -> {
                    tilCurrentPassword.error = "Introduce tu contraseña actual"
                }

                newPassword.isEmpty() -> {
                    tilNewPassword.error = "Introduce una nueva contraseña"
                }

                !isValidPassword(newPassword) -> {
                    tilNewPassword.error =
                        "Debe tener mínimo 8 caracteres, mayúscula, minúscula y número"
                }

                confirmPassword.isEmpty() -> {
                    tilConfirmPassword.error = "Confirma tu nueva contraseña"
                }

                newPassword != confirmPassword -> {
                    tilConfirmPassword.error = "Las contraseñas no coinciden"
                }

                currentPassword == newPassword -> {
                    tilNewPassword.error = "La nueva contraseña debe ser diferente"
                }

                else -> {
                    val idUser = UserSession.getUserId()

                    if (idUser == null) {
                        Toast.makeText(
                            requireContext(),
                            "No hay un usuario logueado",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@setOnClickListener
                    }

                    btnUpdatePassword.isEnabled = false

                    ApiService.Post.updatePassword(
                        context = requireContext(),
                        idUser = idUser,
                        currentPassword = currentPassword,
                        newPassword = newPassword,
                        onResult = { result ->
                            btnUpdatePassword.isEnabled = true

                            Toast.makeText(
                                requireContext(),
                                result.message ?: "Contraseña actualizada correctamente",
                                Toast.LENGTH_SHORT
                            ).show()

                            etCurrentPassword.text?.clear()
                            etNewPassword.text?.clear()
                            etConfirmPassword.text?.clear()

                            findNavController().popBackStack()
                        },
                        onError = { error ->
                            btnUpdatePassword.isEnabled = true
                            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }

    private fun isValidPassword(password: String): Boolean {
        val hasMinLength = password.length >= 8
        val hasUppercase = password.any { it.isUpperCase() }
        val hasLowercase = password.any { it.isLowerCase() }
        val hasDigit = password.any { it.isDigit() }

        return hasMinLength && hasUppercase && hasLowercase && hasDigit
    }
}