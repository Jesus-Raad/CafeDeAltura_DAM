package com.example.cafedealtura_dam.ui.autent

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cafedealtura_dam.R
import com.example.cafedealtura_dam.utils.applyTopInsets

class RegisterFragment : Fragment(R.layout.fragment_register) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.applyTopInsets()

        val tvLogin = view.findViewById<View>(R.id.tvLogin)
        val btnBack = view.findViewById<ImageView>(R.id.btnBack)

        tvLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}