package com.example.cafedealtura_dam.ui.products

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cafedealtura_dam.R
import androidx.navigation.fragment.findNavController
import com.example.cafedealtura_dam.utils.applyTopInsets

class ProductsFragment : Fragment(R.layout.fragment_products) {

    private val adapter = ProductsAdapter { product ->
        findNavController().navigate(R.id.action_productsFragment_to_productDetailFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.applyTopInsets()

        val rv = view.findViewById<RecyclerView>(R.id.rvProducts)
        val tvCount = view.findViewById<TextView>(R.id.tvCount)

        rv.layoutManager = GridLayoutManager(requireContext(), 2)
        rv.adapter = adapter

        val products = ProductData.products

        tvCount.text = "${products.size} cafés disponibles"
        adapter.submitList(products)
    }
}