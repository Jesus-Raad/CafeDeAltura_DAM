package com.example.cafedealtura_dam.ui.products

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cafedealtura_dam.R
import com.example.cafedealtura_dam.data.ProductsRepository
import com.example.cafedealtura_dam.utils.applyTopInsets

class ProductsFragment : Fragment(R.layout.fragment_products) {

    private val adapter = ProductsAdapter { product ->

        val bundle = Bundle().apply {
            putInt("id_coffe", product.id_coffe)
        }

        findNavController().navigate(
            R.id.action_productsFragment_to_productDetailFragment,
            bundle
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.applyTopInsets()

        val rv = view.findViewById<RecyclerView>(R.id.rvProducts)
        val tvCount = view.findViewById<TextView>(R.id.tvCount)

        rv.layoutManager = GridLayoutManager(requireContext(), 2)
        rv.adapter = adapter

        val products = ProductsRepository.getProducts()

        tvCount.text = "${products.size} cafés disponibles"
        adapter.submitList(products)
    }
}