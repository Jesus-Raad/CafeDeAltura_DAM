package com.example.cafedealtura_dam.ui.confi

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cafedealtura_dam.R
import com.google.android.material.button.MaterialButton

class SettingsFragment : Fragment(R.layout.fragment_settings) {

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
        val btnLogout = view.findViewById<MaterialButton>(R.id.btnLogout)
        val itemChangePassword = view.findViewById<View>(R.id.itemChangePassword)

        btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        itemChangePassword.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_changePasswordFragment)
        }

        btnLogout.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }
    }
}