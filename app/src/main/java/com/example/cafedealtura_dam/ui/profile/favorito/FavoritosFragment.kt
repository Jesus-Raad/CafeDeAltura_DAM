package com.example.cafedealtura_dam.ui.profile.favorito

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cafedealtura_dam.R
import com.example.cafedealtura_dam.data.UserSession
import com.example.cafedealtura_dam.dataAPI.ApiService
import com.example.cafedealtura_dam.ui.products.ProductsAdapter

class FavoritosFragment : Fragment(R.layout.fragment_favoritos) {

    private lateinit var rvFavoritos: RecyclerView
    private lateinit var tvContador: TextView
    private lateinit var adapter: ProductsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnBack = view.findViewById<ImageButton>(R.id.btnBack)
        rvFavoritos = view.findViewById(R.id.rvFavoritos)
        tvContador = view.findViewById(R.id.tvContador)

        adapter = ProductsAdapter { product ->
            findNavController().navigate(
                R.id.action_favoritosFragment_to_productDetailFragment,
                bundleOf("id_coffe" to product.id_coffe)
            )
        }

        rvFavoritos.layoutManager = GridLayoutManager(requireContext(), 2)
        rvFavoritos.adapter = adapter

        btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        loadFavorites()
    }

    override fun onResume() {
        super.onResume()
        loadFavorites()
    }

    private fun loadFavorites() {
        val user = UserSession.getUser()

        if (user == null) {
            adapter.submitList(emptyList())
            updateCounter(0)
            Toast.makeText(
                requireContext(),
                "Debes iniciar sesión",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        ApiService.Get.getFavorites(
            context = requireContext(),
            idUser = user.id_user,
            onResult = { favorites ->
                adapter.submitList(favorites)
                updateCounter(favorites.size)
            },
            onError = { error ->
                adapter.submitList(emptyList())
                updateCounter(0)
                Toast.makeText(
                    requireContext(),
                    error,
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
    }

    private fun updateCounter(total: Int) {
        tvContador.text = if (total == 1) {
            "1 café guardado"
        } else {
            "$total cafés guardados"
        }
    }
}