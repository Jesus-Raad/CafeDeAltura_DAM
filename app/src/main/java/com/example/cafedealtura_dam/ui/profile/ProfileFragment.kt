package com.example.cafedealtura_dam.ui.profile

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cafedealtura_dam.R
import com.example.cafedealtura_dam.data.UserSession
import com.example.cafedealtura_dam.utils.applyTopInsets
import com.google.android.material.button.MaterialButton

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.applyTopInsets()

        val user = UserSession.getUser()

        val name = user?.name ?: "Usuario"
        val email = user?.email ?: ""

        val tvName = view.findViewById<TextView>(R.id.tvUserName)
        val tvEmail = view.findViewById<TextView>(R.id.tvUserEmail)
        val tvInitial = view.findViewById<TextView>(R.id.tvProfileInitial)

        tvName.text = name
        tvEmail.text = email
        tvInitial.text = name.firstOrNull()?.uppercase() ?: "?"

        view.findViewById<View>(R.id.optionOrders).setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_ordersFragment)
        }

        view.findViewById<View>(R.id.optionFavorites).setOnClickListener {
            Toast.makeText(requireContext(), "Favoritos todavía no disponible", Toast.LENGTH_SHORT).show()
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
            UserSession.clearUser()
            findNavController().navigate(R.id.loginFragment)
        }
    }
}