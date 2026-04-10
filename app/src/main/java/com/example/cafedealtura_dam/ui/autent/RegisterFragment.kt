package com.example.cafedealtura_dam.ui.autent

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.cafedealtura_dam.R
import com.example.cafedealtura_dam.dataAPI.ApiService
import com.example.cafedealtura_dam.utils.applyTopInsets
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class RegisterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        view.applyTopInsets()

        val btnBack = view.findViewById<ImageView>(R.id.btnBack)
        val tvLogin = view.findViewById<TextView>(R.id.tvLogin)

        val etName = view.findViewById<EditText>(R.id.etName)
        val etSurname = view.findViewById<EditText>(R.id.etSurname)
        val etEmail = view.findViewById<EditText>(R.id.etEmail)
        val etPhone = view.findViewById<EditText>(R.id.etPhone)
        val etPassword = view.findViewById<TextInputEditText>(R.id.etPassword)
        val etConfirmPassword = view.findViewById<TextInputEditText>(R.id.etConfirmPassword)
        val cbTerms = view.findViewById<CheckBox>(R.id.cbTerms)
        val btnRegister = view.findViewById<MaterialButton>(R.id.btnRegister)

        tvLogin.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        btnRegister.setOnClickListener {

            val name = etName.text.toString().trim()
            val surname = etSurname.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val phone = etPhone.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()

            // 🔹 VALIDACIONES

            if (name.isEmpty()) {
                etName.error = "Introduce tu nombre"
                etName.requestFocus()
                return@setOnClickListener
            }

            if (!name.matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$"))) {
                etName.error = "Nombre inválido (solo letras)"
                etName.requestFocus()
                return@setOnClickListener
            }

            if (surname.isEmpty()) {
                etSurname.error = "Introduce tus apellidos"
                etSurname.requestFocus()
                return@setOnClickListener
            }

            if (!surname.matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$"))) {
                etSurname.error = "Apellidos inválidos (solo letras)"
                etSurname.requestFocus()
                return@setOnClickListener
            }

            if (email.isEmpty()) {
                etEmail.error = "Introduce tu correo"
                etEmail.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.error = "Correo no válido"
                etEmail.requestFocus()
                return@setOnClickListener
            }

            if (phone.isEmpty()) {
                etPhone.error = "Introduce tu teléfono"
                etPhone.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                etPassword.error = "Introduce una contraseña"
                etPassword.requestFocus()
                return@setOnClickListener
            }

            if (password.length < 6) {
                etPassword.error = "La contraseña debe tener al menos 6 caracteres"
                etPassword.requestFocus()
                return@setOnClickListener
            }

            if (confirmPassword.isEmpty()) {
                etConfirmPassword.error = "Confirma la contraseña"
                etConfirmPassword.requestFocus()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                etConfirmPassword.error = "Las contraseñas no coinciden"
                etConfirmPassword.requestFocus()
                return@setOnClickListener
            }

            if (!cbTerms.isChecked) {
                Toast.makeText(
                    requireContext(),
                    "Debes aceptar los términos",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // 🔹 DESACTIVAR BOTÓN
            btnRegister.isEnabled = false

            // 🔹 PASO 1: COMPROBAR EMAIL
            ApiService.Post.checkEmail(
                context = requireContext(),
                email = email,
                onResult = { exists ->

                    if (exists) {
                        btnRegister.isEnabled = true
                        etEmail.error = "Este correo ya está registrado"
                        etEmail.requestFocus()
                    } else {

                        // 🔹 PASO 2: CREAR USUARIO
                        ApiService.Post.createUser(
                            context = requireContext(),
                            name = name,
                            surname = surname,
                            email = email,
                            phone = phone,
                            password = password,
                            onResult = { message ->
                                btnRegister.isEnabled = true
                                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                                requireActivity().onBackPressedDispatcher.onBackPressed()
                            },
                            onError = { error ->
                                btnRegister.isEnabled = true
                                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                },
                onError = { error ->
                    btnRegister.isEnabled = true
                    Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                }
            )
        }

        return view
    }
}