package com.example.cafedealtura_dam.ui.profile

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.cafedealtura_dam.R
import com.google.android.material.button.MaterialButton

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btn = view.findViewById<MaterialButton>(R.id.btnLogout)
        btn.setOnClickListener {
            Toast.makeText(requireContext(), "Sesión cerrada (demo)", Toast.LENGTH_SHORT).show()
        }
        view.findViewById<View>(R.id.optionOrders).setOnClickListener {
            Toast.makeText(requireContext(), "Ir a pedidos", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<View>(R.id.optionFavorites).setOnClickListener {
            Toast.makeText(requireContext(), "Ir a favoritos", Toast.LENGTH_SHORT).show()
        }
    }
}