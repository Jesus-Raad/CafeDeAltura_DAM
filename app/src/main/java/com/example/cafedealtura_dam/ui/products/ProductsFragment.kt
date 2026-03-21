package com.example.cafedealtura_dam.ui.products

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cafedealtura_dam.R

import com.example.cafedealtura_dam.data.repository.ProductsRepository
import kotlinx.coroutines.launch

class ProductsFragment : Fragment(R.layout.fragment_products) {

    private lateinit var adapter: ProductsAdapter
    private val repository = ProductsRepository()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvProducts = view.findViewById<RecyclerView>(R.id.rvProducts)
        val tvCount = view.findViewById<TextView>(R.id.tvCount)

        adapter = ProductsAdapter(emptyList())

        rvProducts.layoutManager = GridLayoutManager(requireContext(), 2)
        rvProducts.adapter = adapter

        lifecycleScope.launch {
            try {
                val products = repository.getProducts()
                adapter.updateData(products)
                tvCount.text = "${products.size} cafés disponibles"
            } catch (e: Exception) {
                tvCount.text = "Error cargando productos"
            }
        }
    }
}