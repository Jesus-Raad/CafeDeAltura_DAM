package com.example.cafedealtura_dam.ui.profile

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cafedealtura_dam.R
import com.google.android.material.button.MaterialButton

class ProfileFragment : Fragment(R.layout.fragment_profile) {

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

        val optionOrders = view.findViewById<LinearLayout>(R.id.optionOrders)
        val optionFavorites = view.findViewById<LinearLayout>(R.id.optionFavorites)
        val optionAddress = view.findViewById<LinearLayout>(R.id.optionAddress)
        val optionPayment = view.findViewById<LinearLayout>(R.id.optionPayment)
        val optionSettings = view.findViewById<LinearLayout>(R.id.optionSettings)
        val btnLogout = view.findViewById<MaterialButton>(R.id.btnLogout)

        optionOrders.setOnClickListener {
            Toast.makeText(requireContext(), "Próximamente", Toast.LENGTH_SHORT).show()
        }

        optionFavorites.setOnClickListener {
            Toast.makeText(requireContext(), "Próximamente", Toast.LENGTH_SHORT).show()
        }

        optionAddress.setOnClickListener {
            Toast.makeText(requireContext(), "Próximamente", Toast.LENGTH_SHORT).show()
        }

        optionPayment.setOnClickListener {
            Toast.makeText(requireContext(), "Próximamente", Toast.LENGTH_SHORT).show()
        }

        optionSettings.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_settingsFragment)
        }

        btnLogout.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }
    }
}