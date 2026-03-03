package com.example.cafedealtura_dam.ui.products

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cafedealtura_dam.R

class ProductsFragment : Fragment(R.layout.fragment_products) {

    private val adapter = ProductsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rv = view.findViewById<RecyclerView>(R.id.rvProducts)
        val tvCount = view.findViewById<TextView>(R.id.tvCount)

        rv.layoutManager = GridLayoutManager(requireContext(), 2)
        rv.adapter = adapter

        val products = listOf(
            ProductUiModel("Colombia La Casita", "Colombia", "250g • En grano", "$24.90", R.drawable.coffee_colombia_la_casita),
            ProductUiModel("Colombia Los Naranjos", "Colombia", "250g • En grano", "$26.90", R.drawable.coffee_colombia_los_naranjos),
            ProductUiModel("Costa Rica Monte Bello", "Costa Rica", "250g • En grano", "$27.90", R.drawable.coffee_costa_rica_monte_bello),
            ProductUiModel("Costa Rica Tarrazú", "Costa Rica", "250g • En grano", "$27.90", R.drawable.coffee_costa_rica_tarrazu),
            ProductUiModel("Etiopía Sidamo", "Etiopía", "250g • En grano", "$25.90", R.drawable.coffee_etiopia_sidamo),
            ProductUiModel("Etiopía Yirgacheffe", "Etiopía", "250g • En grano", "$28.90", R.drawable.coffee_etiopia_yirgacheffe),
            ProductUiModel("Kenia Ndunduri", "Kenia", "250g • En grano", "$29.90", R.drawable.coffee_kenia_ndunduri),
            ProductUiModel("Laos Amanecer", "Laos", "250g • En grano", "$23.90", R.drawable.coffee_laos_amanecer)
        )

        tvCount.text = "${products.size} cafés disponibles"
        adapter.submitList(products)
    }
}