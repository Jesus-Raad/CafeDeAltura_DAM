package com.example.cafedealtura_dam.ui.products

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cafedealtura_dam.R
import com.example.cafedealtura_dam.data.ProductsRepository
import com.example.cafedealtura_dam.dataAPI.ApiService
import com.example.cafedealtura_dam.model.Products_coffe
import com.example.cafedealtura_dam.utils.applyTopInsets

class ProductsFragment : Fragment(R.layout.fragment_products) {

    private var allProducts: List<Products_coffe> = emptyList()
    private var selectedCategory: Int? = null

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
        val btnTodos = view.findViewById<Button>(R.id.btnTodos)
        val btnGrano = view.findViewById<Button>(R.id.btnGrano)
        val btnMolido = view.findViewById<Button>(R.id.btnMolido)
        val btnEspecial = view.findViewById<Button>(R.id.btnEspecial)

        rv.layoutManager = GridLayoutManager(requireContext(), 2)
        rv.adapter = adapter

        val filterBtns = listOf(btnTodos, btnGrano, btnMolido, btnEspecial)

        btnTodos.setOnClickListener {
            selectedCategory = null
            updateFilterButtons(filterBtns, btnTodos)
            applyFilter(tvCount)
        }
        btnGrano.setOnClickListener {
            selectedCategory = 1
            updateFilterButtons(filterBtns, btnGrano)
            applyFilter(tvCount)
        }
        btnMolido.setOnClickListener {
            selectedCategory = 2
            updateFilterButtons(filterBtns, btnMolido)
            applyFilter(tvCount)
        }
        btnEspecial.setOnClickListener {
            selectedCategory = 3
            updateFilterButtons(filterBtns, btnEspecial)
            applyFilter(tvCount)
        }

        // si información está en cache, la cargamos
        if (!ProductsRepository.isEmpty()) {
            allProducts = ProductsRepository.getProducts()
            applyFilter(tvCount)
        } else {
            // descargamos información de api
            ApiService.Get.getProducts(
                context = requireContext(),
                onResult = { products ->
                    ProductsRepository.setProducts(products)
                    allProducts = products
                    requireActivity().runOnUiThread {
                        applyFilter(tvCount)
                    }
                },
                onError = { error ->
                    requireActivity().runOnUiThread {
                        tvCount.text = "Error al cargar productos"
                    }
                }
            )
        }
    }

    private fun applyFilter(tvCount: TextView) {
        val filtered = if (selectedCategory == null) {
            allProducts
        } else {
            allProducts.filter { it.category == selectedCategory }
        }
        tvCount.text = "${filtered.size} cafés disponibles"
        adapter.submitList(filtered)
    }

    private fun updateFilterButtons(allBtns: List<Button>, active: Button) {
        allBtns.forEach { btn ->
            if (btn == active) {
                btn.setBackgroundResource(R.drawable.bg_filter_active)
                btn.setTextColor(resources.getColor(android.R.color.white, null))
            } else {
                btn.setBackgroundResource(R.drawable.bg_filter_inactive)
                btn.setTextColor(resources.getColor(R.color.cafe_marron_suave, null))
            }
        }
    }
}