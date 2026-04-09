package com.example.cafedealtura_dam.ui.profile

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cafedealtura_dam.R
import com.example.cafedealtura_dam.utils.applyTopInsets
import com.google.android.material.button.MaterialButton
import android.widget.TextView
import com.example.cafedealtura_dam.utils.SessionManager

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.applyTopInsets()

        val sessionManager = SessionManager(requireContext())

        val name = sessionManager.getUserName()
        val email = sessionManager.getUserEmail()

        val tvName = view.findViewById<TextView>(R.id.tvUserName)
        val tvEmail = view.findViewById<TextView>(R.id.tvUserEmail)
        val tvInitial = view.findViewById<TextView>(R.id.tvProfileInitial)

        // Setear datos
        tvName.text = name
        tvEmail.text = email

        // Inicial automática
        tvInitial.text = name.firstOrNull()?.uppercase() ?: "?"

        view.findViewById<View>(R.id.optionOrders).setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_ordersFragment)
        }

        view.findViewById<View>(R.id.optionFavorites).setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_favoritosFragment)
        }

        view.findViewById<View>(R.id.optionAddress).setOnClickListener {

            Toast.makeText(requireContext(), "Dirección todavía no disponible", Toast.LENGTH_SHORT).show()

            findNavController().navigate(R.id.direccionesFragment)

        }

        view.findViewById<View>(R.id.optionPayment).setOnClickListener {
            findNavController().navigate(R.id.paymentMethodsFragment)
        }
        view.findViewById<View>(R.id.optionSettings).setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_settingsFragment)
        }

        view.findViewById<MaterialButton>(R.id.btnLogout).setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }
    }
}