package com.example.cafedealtura_dam.ui.profile

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cafedealtura_dam.R
import com.example.cafedealtura_dam.data.FavoritosRepository
import com.example.cafedealtura_dam.ui.products.ProductsAdapter

class FavoritosFragment : Fragment(R.layout.fragment_favoritos) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnBack = view.findViewById<ImageButton>(R.id.btnBack)
        val rvFavoritos = view.findViewById<RecyclerView>(R.id.rvFavoritos)
        val tvContador = view.findViewById<TextView>(R.id.tvContador)

        // Solo productos marcados como favoritos
        val productos = FavoritosRepository.getFavoritos()

        val n = productos.size
        tvContador.text = if (n == 1) "1 café guardado" else "$n cafés guardados"

        val adapter = ProductsAdapter { product ->
            findNavController().navigate(
                R.id.action_favoritosFragment_to_productDetailFragment,
                bundleOf("id_coffe" to product.id_coffe)
            )
        }

        rvFavoritos.layoutManager = GridLayoutManager(requireContext(), 2)
        rvFavoritos.adapter = adapter
        adapter.submitList(productos)

        btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }
}