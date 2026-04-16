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
                    tilNewPassword.error = "La contraseña no cumple los requisitos"
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
                    Toast.makeText(requireContext(), "Pantalla lista para conectar con la API", Toast.LENGTH_SHORT).show()
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