package com.example.cafedealtura_dam.ui.profile

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cafedealtura_dam.R
import com.example.cafedealtura_dam.utils.applyTopInsets

class OrderDetailFragment : Fragment(R.layout.fragment_order_detail) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.applyTopInsets()

        val btnBack = view.findViewById<ImageView>(R.id.btnBack)
        val idOrder = arguments?.getInt("id_order", -1)

        btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        // De momento queda preparado para la siguiente fase
        // donde cargarás el detalle real usando idOrder
    }
}