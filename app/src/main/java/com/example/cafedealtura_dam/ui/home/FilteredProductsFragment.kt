package com.example.cafedealtura_dam.ui.home

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cafedealtura_dam.R
import com.example.cafedealtura_dam.data.ProductsRepository
import com.example.cafedealtura_dam.ui.products.ProductsAdapter
import com.example.cafedealtura_dam.utils.applyTopInsets
import java.text.Normalizer

class FilteredProductsFragment : Fragment(R.layout.fragment_filtered_products) {

    private var category: String? = null
    private var origin: String? = null
    private var query: String? = null

    private lateinit var adapter: ProductsAdapter

    private fun normalizeText(text: String): String {
        return Normalizer.normalize(text, Normalizer.Form.NFD)
            .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
            .lowercase()
            .trim()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        category = arguments?.getString("category")
        origin = arguments?.getString("origin")
        query = arguments?.getString("query")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.applyTopInsets()

        val btnBack = view.findViewById<ImageButton>(R.id.btnBackFiltered)
        val tvTitle = view.findViewById<TextView>(R.id.tvFilteredTitle)
        val tvCount = view.findViewById<TextView>(R.id.tvFilteredCount)
        val rvProducts = view.findViewById<RecyclerView>(R.id.rvFilteredProducts)

        btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        adapter = ProductsAdapter { product ->
            val bundle = Bundle().apply {
                putInt("id_coffe", product.id_coffe)
            }

            findNavController().navigate(
                R.id.action_filteredProductsFragment_to_productDetailFragment,
                bundle
            )
        }

        rvProducts.layoutManager = GridLayoutManager(requireContext(), 2)
        rvProducts.adapter = adapter

        tvTitle.text = when {
            !origin.isNullOrBlank() -> origin
            !category.isNullOrBlank() -> category
            else -> "Productos"
        }

        val allProducts = ProductsRepository.getProducts()
        val normalizedQuery = query?.let { normalizeText(it) }

        val filteredProducts = allProducts.filter { product ->

            val productName = normalizeText(product.brand)
            val productOrigin = normalizeText(product.origin ?: "")
            val productDescription = normalizeText(product.description ?: "")

            val matchesCategory = category.isNullOrBlank() ||
                    when (normalizeText(category!!)) {
                        "en grano", "grano" -> product.category == 1
                        "molido" -> product.category == 2
                        "especial" -> product.category == 3
                        else -> true
                    }

            val matchesOrigin = origin.isNullOrBlank() ||
                    productOrigin.contains(normalizeText(origin!!)) ||
                    productName.contains(normalizeText(origin!!))

            val matchesQuery = normalizedQuery.isNullOrBlank() ||
                    productName.contains(normalizedQuery) ||
                    productOrigin.contains(normalizedQuery) ||
                    productDescription.contains(normalizedQuery)

            matchesCategory && matchesOrigin && matchesQuery
        }

        adapter.submitList(filteredProducts)
        tvCount.text = "${filteredProducts.size} cafés disponibles"
    }
}