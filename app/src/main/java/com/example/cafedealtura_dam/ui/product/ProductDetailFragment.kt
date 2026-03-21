package com.example.cafedealtura_dam.ui.product

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cafedealtura_dam.R
import com.example.cafedealtura_dam.utils.applyTopInsets

class ProductDetailFragment : Fragment(R.layout.fragment_product_detail) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.applyTopInsets()

        val btnBack = view.findViewById<ImageButton>(R.id.btnBack)

        btnBack.setOnClickListener {
            Log.d("DETAIL_TEST", "Flecha pulsada")
            Toast.makeText(requireContext(), "Flecha pulsada", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }
    }
}